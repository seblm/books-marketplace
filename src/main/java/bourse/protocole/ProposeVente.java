package bourse.protocole;

import org.w3c.dom.*;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;

import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import java.io.ByteArrayInputStream;

public class ProposeVente extends Protocole {
    
    private String nom;
    private float pas;
    private float prix;
    private int id ;
    
    public int getNom() {
        String typeEnchere=nom.toUpperCase();
        if (typeEnchere.equals("ENCHEREUN")) return 1;
        else if(typeEnchere.equals("ENCHEREDEUX")) return 2;
        else if(typeEnchere.equals("ENCHERETROIS")) return 3;
        else if(typeEnchere.equals("ENCHEREQUATRE")) return 4;
        else if(typeEnchere.equals("ENCHERECINQ")) return 5;
        else return 0;
    }
    public float getPrix() { return prix; }
    public int getId() { return id; }
    
    public ProposeVente(String nom,float prix, int id) {
        super(new TypeMessage(TypeMessage.TM_PROPOSE_VENTE));
        this.nom=nom;
        this.prix = prix;
        this.id=id;
    }
    
        
    public ProposeVente(Element type) {
        super(new TypeMessage(TypeMessage.TM_PROPOSE_VENTE));
        this.toClass(type);
    }
    
    protected void toClass(Element type) {
        this.nom = type.getAttribute("NOM");
        NodeList noeuds = type.getChildNodes();
        Element livre = (Element)noeuds.item(0);
        this.id = Integer.valueOf(livre.getAttribute("ID")).intValue();
        Element enchere = (Element)noeuds.item(1);
        Text noeud=(Text)enchere.getFirstChild();
        this.prix = Float.valueOf(noeud.getNodeValue()).floatValue();
     
    }
    
    public org.w3c.dom.Document toDOM() {
         Document document = null;
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            document = builder.newDocument();
            Element root = document.createElement("MSG");
            document.appendChild(root);
            Element type = document.createElement("PROPOSEVENTE");
            root.appendChild(type);
            Attr nom = document.createAttribute("NOM");
            nom.setValue(this.nom);
            type.setAttributeNode(nom);
            Element livre =document.createElement("LIVRE");
            type.appendChild(livre);
            Attr idelm = document.createAttribute("ID");
            idelm.setValue(String.valueOf(id));
            livre.setAttributeNode(idelm);
            Element encherelm =document.createElement("ENCHERE");
            type.appendChild(encherelm);
            encherelm.appendChild(document.createTextNode(String.valueOf(prix)));
        } catch (Exception e) {
            e.printStackTrace(System.err);
        }
        return document;
    }

    public String toXML() {
        return super.toXML(this.toDOM());
    }
    
    public static void main(String args[]) {
        float solde=1546;
        String p=new ProposeVente("Montante",solde,12).toXML();
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
            Element typeDOM = (Element)noeuds.item(0);
            Element typeDOME = (Element)typeDOM;
            p= new ProposeVente(typeDOME).toXML();
            System.out.println(p);
            
            Protocole d = Protocole.newInstance("<MSG><PROPOSEVENTE NOM=\"\"><LIVRE ID=\"15\"></LIVRE><ENCHERE>10</ENCHERE></PROPOSEVENTE></MSG>");
            System.out.println(((ProposeVente)d).getType());
        } catch (Exception e) {
            System.err.print("Protocole: ");
            e.printStackTrace(System.err);
        }
    }
      
}
