package bourse.protocole;

import org.w3c.dom.*;
import bourse.sdd.*;

//import  javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;

import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import java.io.ByteArrayInputStream;
import java.util.LinkedList;

 

public class ResultBye extends Protocole {
    
  
   private LinkedList listepdm;
   /** Retourne une liste chainée remplie de PDMPro. */
   public LinkedList getListepdm() { return this.listepdm; }
    
    
    
    /** Instancie un nouveau message de fin de session en envoyant la liste des
     * places de marché présentes sur la base de données.
     * @param LinkedListe liste est une liste chaînée remplie de PDMPro. */
   public ResultBye(LinkedList liste) {
        super(new TypeMessage(TypeMessage.TM_RESULT_BYE));
        if (liste == null)
            listepdm = new LinkedList();
        else
            listepdm=liste;
        
    }
    
    public ResultBye(Element type) {
        super(new TypeMessage(TypeMessage.TM_RESULT_BYE));
        this.toClass(type);
    }
    
    protected void toClass(Element type) {
        NodeList noeuds = type.getChildNodes();
        this.listepdm=new LinkedList();
        for(int i=0;i<noeuds.getLength();i++)
        {
            Element pdm = (Element)noeuds.item(i);
            PDMPro p=new PDMPro(pdm.getAttribute("NOM"), pdm.getAttribute("ADRESSE"));
            this.listepdm.add(p);
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
            Element type = document.createElement("RESULTBYE");
            root.appendChild(type);
            for(int i=0;i<this.listepdm.size();i++)
            {
            Element pdm = document.createElement("PDM");
            type.appendChild(pdm);
            Attr nomAttr = document.createAttribute("NOM");
            nomAttr.setValue(((PDMPro)this.listepdm.get(i)).getNom());
            pdm.setAttributeNode(nomAttr);
            Attr adresseAttr = document.createAttribute("ADRESSE");
            adresseAttr.setValue(((PDMPro)this.listepdm.get(i)).getAdresse());
            pdm.setAttributeNode(adresseAttr);
            }
        } catch (Exception e) {
            e.printStackTrace(System.err);
        }
        return document;

    }
    
    public String toXML() {
        return super.toXML(this.toDOM());
    }
    
    public static void main(String args[]) {
        
        LinkedList pdmliste=new LinkedList();
        
        PDMPro plm=new PDMPro("Prem", "192.12.26.56");
        pdmliste.add(0, plm);
        PDMPro plm2=new PDMPro("Deux", "192.12.26.57");
        pdmliste.add(1, plm2);
        PDMPro plm3=new PDMPro("Prem", "192.12.26.56");
        pdmliste.add(2, plm3);
        
        String p=new ResultBye(pdmliste).toXML();
        System.out.println(p);
        
        ResultBye r = (bourse.protocole.ResultBye)bourse.protocole.Protocole.newInstance("<MSG><RESULTBYE><PDM NOM=\"jhlfdhsl\" ADRESSE=\"192.168.1.1:24\"/><PDM NOM=\"coucou\" ADRESSE=\"1.1.1.1:1\"/></RESULTBYE></MSG>");
        System.out.println(r.toXML());

        /*
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
            Element typeDOM = (Element)noeuds.item(0);
            Element typeDOME = (Element)typeDOM;
            p= new ResultBye(typeDOME).toXML();
            System.out.println(p);   
        } catch (Exception e) {
            System.err.print("Protocole: ");
            e.printStackTrace(System.err);
        }
        */
    }

}
