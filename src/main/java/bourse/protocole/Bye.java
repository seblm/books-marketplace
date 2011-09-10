package bourse.protocole;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;

public final class Bye extends bourse.protocole.Protocole {
    
    private String commentaire;
    
    /** Crée une nouvelle instance du type de message Bye. */
    public Bye(String commentaire) {
        super(new TypeMessage(TypeMessage.TM_BYE));
        this.commentaire = commentaire;
    }
    
    public Bye() {
        super(new TypeMessage(TypeMessage.TM_BYE));
        this.commentaire = "";
    }
    
    public Bye(Element type) {
        super(new TypeMessage(TypeMessage.TM_BYE));
        this.toClass(type);
    }
    
    protected final void toClass(Element type) {
        if (type.hasChildNodes())
            this.commentaire = ((Text)type.getChildNodes().item(0)).getNodeValue();
        else
            this.commentaire="";
    }
    
    public final Document toDOM() {
        Document document = null;
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            document = builder.newDocument();
            Element root = document.createElement("MSG");
            Element type = document.createElement("BYE");
            type.appendChild(document.createTextNode(this.commentaire));
            //type.appendChild(document.createCDATASection("Au revoir !"));
            root.appendChild(type);
            document.appendChild(root);
        } catch (Exception e) {
            e.printStackTrace(System.err);
        }
        return document;
    }
    
    public String toXML() {
        return super.toXML(this.toDOM());
    }
    
}
