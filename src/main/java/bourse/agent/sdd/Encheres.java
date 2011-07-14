package bourse.agent.sdd;

import java.util.*;

/** Donne toutes les encheres prévues par la pdm courante. */
public class Encheres { 
/** Variables d'instances. */
    /** donne la liste des encheres. */
    private LinkedList encheres;
    
    /** Constructeur. */
    /** Construit une liste d' encheres vide. */
    public Encheres() { this.encheres = new LinkedList(); }
    
    /** Méthodes. */
    /** Ajouter une nouvelle enchere avec les valeurs essentielles. */
    public void ajouter(Enchere e) {
        this.encheres.add(new Enchere(e));
    }
    /** Renvoie la p-ième enchere de la liste. */
    public Enchere get(int index) throws IndexOutOfBoundsException { return ((Enchere)this.encheres.get(index)); }
    /** Nettoyer la liste = supprimer toutes les encheres. */
    public void nettoyer() {
        ListIterator parcours = this.encheres.listIterator();
        while (parcours.hasNext()) { parcours.remove(); }
    }
    /** Méthode d'affichage. */
    public String toString(int decalage) {
        String delta = "";
        for (int i=0; i<decalage; i++) delta += " ";
        String output = "";
        ListIterator parcours = this.encheres.listIterator();
        while (parcours.hasNext()) { output += ((Enchere)parcours.next()).toString(decalage+1) + "\n"; }
        if (output.length() == 0) return output;
        else return output.substring(0, output.length()-1);
    }
    /** Programme principal. */
    public static void main(String[] argc) {
        Encheres e = new Encheres();
        bourse.sdd.Livre l1 = new bourse.sdd.Livre("l1", "a2", new bourse.protocole.Categorie(), "poche", "O'reilly", (float)50.65, (float)0.45, "2004-01-01", "222222222", 1, "Seb", (float)65);
        bourse.sdd.Livre l2 = new bourse.sdd.Livre("l2", "a1", new bourse.protocole.Categorie(), "poche", "Casterman", (float)40.75, (float)0.85, "1954-04-12", "222XX2254", 2, "protocoleman", (float)50);
        bourse.sdd.Livre l3 = new bourse.sdd.Livre("l2", "a1", new bourse.protocole.Categorie(), "poche", "Casterman", (float)40.75, (float)0.27, "1954-04-12", "222XX2254", 3, "arno", (float)12);
        e.ajouter(new Enchere(10, l1, (float)60, 12, (float)10, 1, "Agent-A"));
        e.ajouter(new Enchere(11, l2, (float)50, 35, (float)5, 1, "Agent-A"));
        e.ajouter(new Enchere(12, l3, (float)50, 35, (float)5, 2, "Agent-B"));
        System.out.println(e.toString(0));
    }
}
