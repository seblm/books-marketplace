package bourse.agent.sdd;

// pour l'iterator et le HashMap
import java.util.*;

/** Les livres possédés. */
public class ListeLivre {

    /** Variables d'instance. */
    /** La liste de livres (indéxée par l'id du livre). */
    private java.util.HashMap liste;

    /** Constructeurs. */
    /** Construit une liste vide de livres. */
    public ListeLivre() {
        liste = new java.util.HashMap();
    }

    /** Méthodes. */
    /**
     * Renvoie l'ensemble des valeurs de la liste. Utile seulement pour
     * instancier un itérateur.
     */
    public Collection getValeurs() {
        return liste.values();
    }

    /** Renvoie la liste des livres. */
    public java.util.HashMap getListe() {
        return this.liste;
    }

    /** Ajoute un nouveau livre. */
    public void ajouter(bourse.sdd.Livre l) {
        this.liste.put(new java.lang.Integer(l.getId()), l);
    }

    /** Retire un livre d'après l'id du livre. */
    public void supprimer(bourse.sdd.Livre l) {
        this.liste.remove(new java.lang.Integer(l.getId()));
    }

    /** Retire un livre à partir de son id. */
    public void supprimer(int id) {
        this.liste.remove(new java.lang.Integer(id));
    }

    /** Méthode d'affichage. */
    public String toString(int decalage) {
        String delta = "";
        for (int i = 0; i < decalage; i++)
            delta += " ";
        String output = "";
        java.util.Iterator parcours = this.liste.values().iterator();
        while (parcours.hasNext()) {
            output += delta + ((bourse.sdd.Livre) parcours.next()).toString(decalage + 1) + "\n";
        }
        if (output.length() == 0)
            return output;
        else
            return output.substring(0, output.length() - 1);
    }

    /** Méthode de test. */
    public static void main(String argc[]) {
        ListeLivre l = new ListeLivre();
        bourse.sdd.Livre l1 = new bourse.sdd.Livre("l1", "a2", new bourse.protocole.Categorie(), "poche", "O'reilly",
                (float) 50.65, (float) 0.45, "2004-01-01", "222222222", 1, "Seb", (float) 65);
        bourse.sdd.Livre l2 = new bourse.sdd.Livre("l2", "a1", new bourse.protocole.Categorie(), "poche", "Casterman",
                (float) 40.75, (float) 0.85, "1954-04-12", "222XX2254", 2, "protocoleman", (float) 50);
        bourse.sdd.Livre l3 = new bourse.sdd.Livre("l2", "a1", new bourse.protocole.Categorie(), "poche", "Casterman",
                (float) 40.75, (float) 0.27, "1954-04-12", "222XX2254", 3, "arno", (float) 12);
        l.ajouter(l1);
        l.ajouter(l2);
        l.ajouter(l3);
        System.out.println(l.toString(0));
        l.supprimer(3);
        System.out.println(l.toString(5));
        l.supprimer(l2);
        System.out.println(l.toString(2));
    }
}
