package bourse.protocole;

import org.w3c.dom.*;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;

import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import java.io.ByteArrayInputStream;



public class Welcome extends Protocole {
    
    private String nom;
    private String description;
    
    public String getNom() { return this.nom; }
    
    public Welcome(String nom, String description) {
        super(new TypeMessage(TypeMessage.TM_WELCOME));
        this.nom = nom;
        this.description = description;
    }
    
    public Welcome(Element type) {
        super(new TypeMessage(TypeMessage.TM_WELCOME));
        this.toClass(type);
    }
    
    protected void toClass(Element type) {
        NodeList noeuds = type.getChildNodes();
        Element agent = (Element)noeuds.item(0);
        this.nom = agent.getAttribute("NOM");
        try {
            this.description = ((Text)agent.getChildNodes().item(0)).getNodeValue();
        } catch (Exception e) { }
    }
    
    public Document toDOM() {
        Document document = null;
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            document = builder.newDocument();
            Element root = document.createElement("MSG");
            document.appendChild(root);
            Element type = document.createElement("WELCOME");
            root.appendChild(type);
            Element agent = document.createElement("AGENT");
            type.appendChild(agent);
            Attr nom = document.createAttribute("NOM");
            nom.setValue(this.nom);
            agent.setAttributeNode(nom);
            agent.appendChild(document.createTextNode(this.description));
        } catch (Exception e) {
            e.printStackTrace(System.err);
        }
        return document;

    }
    
    public String toXML() {
        return super.toXML(this.toDOM());
    }
    
    public static void main(String args[]) {
        String p=new Welcome("essai", "description").toXML();
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
            p= new Welcome(typeDOME).toXML();
            System.out.println(p);   
        } catch (Exception e) {
            System.err.print("Protocole: ");
            e.printStackTrace(System.err);
        }
        
    }
    
}
