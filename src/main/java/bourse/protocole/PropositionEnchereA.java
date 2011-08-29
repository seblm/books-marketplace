package bourse.protocole;

import org.w3c.dom.*;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;

import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import java.io.ByteArrayInputStream;

public class PropositionEnchereA extends bourse.protocole.Protocole {
    
    private int numero;
    private float enchere;
  
    public float getEnchere(){
        return this.enchere;
    }
    /** Retourne le numéro de l'enchère sur lequel l'agent a envoyé. */
    public int getNumero() { return this.numero; }
    
    
    public PropositionEnchereA(int numero, float enchere) {
        super(TypeMessage.PROPOSITIONENCHEREA);
        this.enchere = enchere;
        this.numero=numero;
    }
    
    public PropositionEnchereA(Element type) {
        super(TypeMessage.PROPOSITIONENCHEREA);
        this.toClass(type);
    }
    
    protected void toClass(Element type) {
        this.numero = Integer.valueOf(type.getAttribute("NUMERO")).intValue();
        NodeList noeuds = type.getChildNodes();
        Element enchere = (Element)noeuds.item(0);
        Text noeud=(Text)enchere.getFirstChild();
        this.enchere = Float.valueOf(noeud.getNodeValue()).floatValue();
     
    }
    
    public org.w3c.dom.Document toDOM() {
         Document document = null;
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            document = builder.newDocument();
            Element root = document.createElement("MSG");
            document.appendChild(root);
            Element type = document.createElement("PROPOSITIONENCHERE");
            root.appendChild(type);
            Attr numero = document.createAttribute("NUMERO");
            numero.setValue(String.valueOf(this.numero));
            type.setAttributeNode(numero);
            Element encherelm =document.createElement("ENCHERE");
            type.appendChild(encherelm);
            encherelm.appendChild(document.createTextNode(String.valueOf(enchere)));
        } catch (Exception e) {
            e.printStackTrace(System.err);
        }
        return document;
    }

    public String toXML() {
        return super.toXML(this.toDOM());
    }
    
    public static void main(String args[]) {
        float enchere=1546;int num=50;
        String p=new PropositionEnchereA(num,enchere).toXML();
        System.out.println(p);
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
            p= new PropositionEnchereA(typeDOME).toXML();
            System.out.println(p);   
        } catch (Exception e) {
            System.err.print("Protocole: ");
            e.printStackTrace(System.err);
        }
    }
      
}
