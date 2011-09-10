package bourse.protocole;

import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;

public class Inconnu extends bourse.protocole.Protocole {
    
    public Inconnu() {
        super(new TypeMessage(TypeMessage.TM_INCONNU));
    }

    public final Document toDOM() { return null; }
    protected final void toClass(org.w3c.dom.Element e) { }
    
    public String toXML() { return null; }
    
}
