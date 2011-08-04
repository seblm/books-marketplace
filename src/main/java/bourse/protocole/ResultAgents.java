package bourse.protocole;

import java.util.LinkedList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class ResultAgents extends Protocole {
    
    /**
     * Une liste chaînée remplie de chaînes de caractères stoquant des nom d'agents.
     */
    private LinkedList<String> listeAgents;
    
	public List<String> getListeAgents() {
    	return this.listeAgents;
    }
    
    public void setListeAgents(LinkedList<String> listeAgents) {
    	this.listeAgents = listeAgents;
    }
    
    public ResultAgents(LinkedList<String> liste) {
        super(new TypeMessage(TypeMessage.TM_RESULT_AGENTS));
        this.listeAgents=liste;
        
    }
    
    public ResultAgents(Element type) {
        super(new TypeMessage(TypeMessage.TM_RESULT_AGENTS));
        this.toClass(type);
    }
    
    protected void toClass(Element type) {
        NodeList noeuds = type.getChildNodes();
        this.listeAgents=new LinkedList<String>();
        for(int i=0;i<noeuds.getLength();i++) {
        	if (noeuds.item(i).getNodeType() == Node.TEXT_NODE) {
        		continue;
        	}
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
            for(int i=0;i<this.listeAgents.size();i++) {
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
    
}
