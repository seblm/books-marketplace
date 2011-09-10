package bourse.protocole;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;

public final class Admin extends bourse.protocole.Protocole {

    private int type;

    public static final int ADMIN_DOC = 0;
    public static final int ADMIN_TERMINER = 1;
    public static final String[] REQUETE = { "index.html", "terminer" };

    public int getTypeRequete() {
        return type;
    }

    /** Crée une nouvelle instance du type de message Admin. */
    public Admin(int type) {
        super(new TypeMessage(TypeMessage.TM_ADMIN));
        this.type = type;
    }

    public Admin(Element node) {
        super(new TypeMessage(TypeMessage.TM_ADMIN));
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
