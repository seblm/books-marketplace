package bourse.protocole;

import org.w3c.dom.Document;

public class Inconnu extends bourse.protocole.Protocole {
    
    public Inconnu() {
        super(TypeMessage.INCONNU);
    }

    public final Document toDOM() { return null; }
    protected final void toClass(org.w3c.dom.Element e) { }
    
    public String toXML() { return null; }
    
}
