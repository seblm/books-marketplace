package bourse.protocole;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public final class Admin extends Protocole {
    
    private int type;
    
    public static final int ADMIN_DOC = 0;
    public static final int ADMIN_TERMINER = 1;
    public static final String[] REQUETE = {"index.html", "terminer"};
    
    public int getTypeRequete() { return type; }
    
    /** Crée une nouvelle instance du type de message Admin. */
    public Admin(int type) {
        super(TypeMessage.ADMIN);
        this.type = type;
    }
    
    public Admin(Element node) {
        super(TypeMessage.ADMIN);
        toClass(node);
    }
    
    protected final void toClass(Element type) {
        if (type.getAttributeNode("REQUETE").getValue().equals("index.html"))
            this.type = ADMIN_DOC;
        else
            this.type = ADMIN_TERMINER;
    }
    
    public final Document toDOM() {
        Document document = null;
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            document = builder.newDocument();
            Element root = document.createElement("MSG");
            Element type = document.createElement("ADMIN");
            type.setAttribute("REQUETE", REQUETE[this.type]);
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
