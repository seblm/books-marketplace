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

 

public class ResultAgents extends Protocole {
    
    /** Une liste chaînée remplie de chaînes de caractères stoquant des nom d'agents. */
    private LinkedList listeAgents;
    
    public LinkedList getListeAgents() { return this.listeAgents; }
    public void setListeAgents(LinkedList listeAgents) { this.listeAgents = listeAgents; }
    
    public ResultAgents(LinkedList liste) {
        super(new TypeMessage(TypeMessage.TM_RESULT_AGENTS));
        this.listeAgents=liste;
        
    }
    
    public ResultAgents(Element type) {
        super(new TypeMessage(TypeMessage.TM_RESULT_AGENTS));
        this.toClass(type);
    }
    
    protected void toClass(Element type) {
        NodeList noeuds = type.getChildNodes();
        this.listeAgents=new LinkedList();
        for(int i=0;i<noeuds.getLength();i++)
        {
            Element agent = (Element)noeuds.item(i);
            this.listeAgents.add(agent.getAttribute("NOM"));
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
            Element type = document.createElement("RESULTAGENTS");
            root.appendChild(type);
            for(int i=0;i<this.listeAgents.size();i++)
            {
            Element agent = document.createElement("AGENT");
            type.appendChild(agent);
            Attr nom = document.createAttribute("NOM");
            nom.setNodeValue((String)this.listeAgents.get(i));
            agent.setAttributeNode(nom);
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
        
        LinkedList listeAgents=new LinkedList();
       /* int i=12;
        float pr=0;
        float et= (float)0.4;
        bourse.Livre livre=new bourse.Livre("lupin", "leblanc", "polar", "poch", "belin", pr, et,"15/11/00","yetet",i); 
        ProgrammePro plm=new ProgrammePro(1,livre);
        pdmliste.add(0, plm);
        bourse.Livre livre2=new bourse.Livre("peterpan", "disney", "bd", "poch", "belin", pr, et,"15/11/00","yetet",i); 
        ProgrammePro plm2=new ProgrammePro(2,livre2);
        pdmliste.add(1, plm2);
        bourse.Livre livre3=new bourse.Livre("roi lion", "disney", "bd", "poch", "belin", pr, et,"15/11/00","yetet",i); 
        ProgrammePro plm3=new ProgrammePro(3,livre3);
        pdmliste.add(2, plm3);*/
        listeAgents.add(0,"groupe-E.seb");
        listeAgents.add(1,"groupe-E.eric");
        listeAgents.add(2,"groupe-E.arnaud");
        listeAgents.add(3,"groupe-E.protocoleman");
        String p=new ResultAgents(listeAgents).toXML();
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
            p= new ResultAgents(typeDOME).toXML();
            System.out.println(p);   
        } catch (Exception e) {
            System.err.print("Protocole: ");
            e.printStackTrace(System.err);
        }
        
    }
    
}
