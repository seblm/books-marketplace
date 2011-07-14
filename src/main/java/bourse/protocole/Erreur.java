/*
 * Erreur.java
 *
 * Created on 15 janvier 2004, 18:48
 */

package bourse.protocole;

import org.w3c.dom.*;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;

import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import java.io.ByteArrayInputStream;
/**
 *
 * @author  pechot
 */
public class Erreur extends Protocole {
    
    private String nom;
    
    public String getNom() { return this.nom; }
    
    private String message;
    private String pdmnom;
    private String adresse;
    private String raison;
    
    public String getRaison(){
        return this.raison;
    }
    /** Creates a new instance of Erreur general */
    public Erreur(String nom,String message,String pdmnom,String adresse,String raison) {
        super(new TypeMessage(TypeMessage.TM_ERREUR));
        this.nom=nom;
        this.message=message;
        this.pdmnom=pdmnom;
        this.adresse=adresse;
        this.raison=raison;
    }
    /** Creates a new instance of Erreur pour erreur ZEROVENTE*/
     public Erreur(String nom,String message,String raison) {
        super(new TypeMessage(TypeMessage.TM_ERREUR));
        this.nom=nom;
        this.message=message;
        this.pdmnom=pdmnom;
        this.adresse=adresse;
        this.raison=raison;
    }
    /** Creates a new instance of Erreur pour erreur DUPLICATION */
    public Erreur(String nom,String message,String pdmnom,String adresse) {
        super(new TypeMessage(TypeMessage.TM_ERREUR));
        this.nom=nom;
        this.message=message;
        this.pdmnom=pdmnom;
        this.adresse=adresse;
        this.raison="";
    }
    /** Creates a new instance of Erreur simple evitant le passage de chaine vide */
    public Erreur(String nom,String message){
        super(new TypeMessage(TypeMessage.TM_ERREUR));
        this.nom=nom;
        this.message=message;
        this.pdmnom="";
        this.adresse="";
        this.raison="";
    }
    
    public Erreur(Element type) {
        super(new TypeMessage(TypeMessage.TM_ERREUR));
        this.toClass(type);
    }
    
    public String toXML() {
        return super.toXML(this.toDOM());
    }
     
    protected void toClass(Element type) {
        this.nom = type.getAttribute("NOM");
         try {
                this.message = ((Text)type.getChildNodes().item(0)).getNodeValue();
                
            } catch (Exception e) { }
        
        if(this.nom.equalsIgnoreCase("DUPLICATION"))//(type.getChildNodes().getLength()>=2)
        {
            try {
                Element pdmElem = ((Element)type.getChildNodes().item(1));
                this.pdmnom=pdmElem.getAttribute("NOM");
                this.adresse=pdmElem.getAttribute("ADRESSE");
                //((Text)pdmElem.getChildNodes().item(0)).getNodeValue();
            } catch (Exception e) { }
          this.raison="";
        }
        else
            if(this.nom.equalsIgnoreCase("ZEROVENTE"))
            {
                try {
                Element pdmElem = ((Element)type.getChildNodes().item(1));
                this.raison=pdmElem.getAttribute("TYPE");
                
                } catch (Exception e) { }
        
                this.pdmnom="";
                this.adresse="";
            }
            else
            {
               this.pdmnom="";
               this.adresse=""; 
               this.raison="";
            }
       
        
    }
    
    public Document toDOM() {
        Document document = null;
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            document = builder.newDocument();
            Element root = document.createElement("MSG");
            document.appendChild(root);
            Element type = document.createElement("ERREUR");
            root.appendChild(type);
            Attr nom = document.createAttribute("NOM");
            nom.setValue(this.nom);
            type.setAttributeNode(nom);
            type.appendChild(document.createTextNode(this.message));
            if(this.nom.equalsIgnoreCase("DUPLICATION"))
            {
                Element pdm=document.createElement("PDM");
                type.appendChild(pdm);
                Attr pdmnom= document.createAttribute("NOM");
                pdmnom.setValue(this.pdmnom);
                pdm.setAttributeNode(pdmnom);
                Attr adresse=document.createAttribute("ADRESSE");
                adresse.setValue(this.adresse);
                pdm.setAttributeNode(adresse);
            }
            else
                if(this.nom.equalsIgnoreCase("ZEROVENTE"))
            {
                Element pdm=document.createElement("RAISON");
                type.appendChild(pdm);
                Attr raison= document.createAttribute("TYPE");
                raison.setValue(this.raison);
                pdm.setAttributeNode(raison);
            }
            
        } catch (Exception e) {
            e.printStackTrace(System.err);
        }
        return document;
    }
    
     public static void main(String args[]) {
        String p=new Erreur("Zerovente", "description","dte","1234","trop de blabla").toXML();
        System.out.println(p);
         Protocole message = null;
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        // D'après le tutorial JAXP, ces variables fixées à true permettent à
        // l'application de se concentrer sur l'analyse sémantique.
        factory.setCoalescing(true);
        factory.setExpandEntityReferences(true);
        factory.setIgnoringComments(true);
        factory.setIgnoringElementContentWhitespace(true);
        try {
            // factory.setValidating(true);
            DocumentBuilder builder = factory.newDocumentBuilder();
            
            // La définition de ErrorHandler est inspirée de
            // http://java.sun.com/j2ee/1.4/docs/tutorial/doc/JAXPDOM3.html#wp64106
            builder.setErrorHandler(new org.xml.sax.ErrorHandler() {
                // ignore fatal errors (an exception is guaranteed)
                public void fatalError(SAXParseException exception) throws SAXException { }
                // treat validation errors as fatal
                public void error(SAXParseException e) throws SAXParseException { throw e; }
                // dump warnings too
                public void warning(SAXParseException err) throws SAXParseException {
                    System.out.println("** Warning"
                    + ", line " + err.getLineNumber()
                    + ", uri " + err.getSystemId());
                    System.out.println("   " + err.getMessage());
                }
            }
            );
            Document document = builder.parse(new ByteArrayInputStream(p.getBytes("UTF-8")), Protocole.BASE_DTD);
            Element root = document.getDocumentElement();
            NodeList noeuds = root.getChildNodes();
            Element typeDOM = (Element)noeuds.item(1);
            Element typeDOME = (Element)typeDOM;
            p= new Erreur(typeDOME).toXML();
            System.out.println(p);   
        } catch (Exception e) {
            System.err.print("Protocole: ");
            e.printStackTrace(System.err);
        }
       
     }
    
}
