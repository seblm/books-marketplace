package bourse.agent.sdd;

/** On utilise la classe de programme prédéfinie dans ce package. */
import bourse.sdd.*;
import java.util.*;

/** Donne toutes les Prévisons (une liste de ProgrammePro). */
public class ListeProgramme {

    /** Variables d'instances. */
    /** Donne la liste des ProgrammePro. */
    private TreeMap liste;
    /** Donne la date à laquelle la liste a été téléchargée. */
    private long date;

    /** Constructeur. */
    /** Construit un programme vide : c'est à dire une liste de ProgrammePro. */
    public ListeProgramme() {
        this.liste = new TreeMap();
        this.date = new Date().getTime();
    }

    /** Construit un programme à partir d'une linked list de programme. */
    public ListeProgramme(java.util.LinkedList l) {
        this.liste = new TreeMap();
        ListIterator parcours = l.listIterator();
        while (parcours.hasNext()) {
            this.ajouter((ProgrammePro) parcours.next());
        }
        this.date = new Date().getTime();
    }

    /** Méthodes. */
    /** le premier est à l'indice 1... */
    public bourse.sdd.ProgrammePro getIeme(int i) {
        ProgrammePro p = new ProgrammePro(0, null);
        Iterator parcours = this.liste.values().iterator();
        while (parcours.hasNext() & (i != 0)) {
            p = (ProgrammePro) parcours.next();
            i--;
        }
        return p;
    }

    /** Ajouter une prevision (un ProgrammePro). */
    public void ajouter(bourse.sdd.ProgrammePro p) {
        this.liste.put(new java.lang.Integer(p.getNum()), p);
    }

    /** Méthode d'affichage. */
    public String toString(int decalage) {
        String delta = "";
        for (int i = 0; i < decalage; i++)
            delta += " ";
        String output = delta + "date = " + String.valueOf(this.date) + ", liste = \n";
        java.util.Iterator parcours = this.liste.values().iterator();
        while (parcours.hasNext()) {
            output += ((ProgrammePro) parcours.next()).toString(decalage + 1) + "\n";
        }
        return output.substring(0, output.length() - 1);
    }

    /** Méthode de test. */
    public static void main(String argc[]) {
        ListeProgramme p = new ListeProgramme();
        bourse.sdd.Livre l1 = new bourse.sdd.Livre("l1", "a2", new bourse.protocole.Categorie(), "poche", "O'reilly",
                (float) 50.65, (float) 0.45, "2004-01-01", "222222222", 1, "Seb", (float) 65);
        bourse.sdd.Livre l2 = new bourse.sdd.Livre("l2", "a1", new bourse.protocole.Categorie(), "poche", "Casterman",
                (float) 40.75, (float) 0.85, "1954-04-12", "222XX2254", 2, "protocoleman", (float) 50);
        bourse.sdd.Livre l3 = new bourse.sdd.Livre("l2", "a1", new bourse.protocole.Categorie(), "poche", "Casterman",
                (float) 40.75, (float) 0.27, "1954-04-12", "222XX2254", 3, "arno", (float) 12);
        p.ajouter(new ProgrammePro(12, l1));
        p.ajouter(new ProgrammePro(13, l2));
        p.ajouter(new ProgrammePro(14, l3));
        System.out.println(p.toString(0));

        p = new ListeProgramme();
        System.out.println(p.toString(0));

        p.ajouter(new ProgrammePro(12, l1));
        p.ajouter(new ProgrammePro(13, l2));
        p.ajouter(new ProgrammePro(12, l3)); // deux enchères ont meme numéro :
                                             // on écrase l'ancien.
        System.out.println(p.toString(0));
    }
}
