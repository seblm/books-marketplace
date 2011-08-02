package bourse.agent.sdd;

import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import bourse.sdd.ProgrammePro;

/**
 * Donne toutes les Prévisons (une liste de ProgrammePro).
 */
public class ListeProgramme {
       
    /**
     * Donne la liste des ProgrammePro.
     */
    private Map<Integer, ProgrammePro> liste;
    
    /**
     * Donne la date à laquelle la liste a été téléchargée.
     */
    private long date;
    
    /**
     * Construit un programme vide : c'est à dire une liste de ProgrammePro.
     */
    public ListeProgramme() {
        this.liste = new TreeMap<Integer, ProgrammePro>();
        this.date = new Date().getTime();
    }
    
    /**
     * Construit un programme à partir d'une linked list de programme.
     */
    public ListeProgramme(List<ProgrammePro> l) {
        this.liste = new TreeMap<Integer, ProgrammePro>();
        for (final ProgrammePro programmePro : l) {
        	this.ajouter(programmePro);
        }
        this.date = new Date().getTime();
    }
    
    /**
     * le premier est à l'indice 1...
     */
    public ProgrammePro getIeme(int i) {
        ProgrammePro p = new ProgrammePro(0, null);
        Iterator<ProgrammePro> parcours = this.liste.values().iterator();
        while (parcours.hasNext() & (i!=0)) {
            p = parcours.next(); 
            i--;
        }
        return p;
    }
    
    /**
     * Ajouter une prevision (un ProgrammePro).
     */
    public void ajouter(ProgrammePro p) {
         this.liste.put(new Integer(p.getNum()), p);
    }
    
    /**
     * Méthode d'affichage.
     */
    public String toString(int decalage) {
        String delta = "";
        for (int i=0; i<decalage; i++) {
        	delta += " ";
        }
        String output = delta + "date = " + String.valueOf(this.date) + ", liste = \n";
        Iterator<ProgrammePro> parcours = this.liste.values().iterator();
        while (parcours.hasNext()) {
        	output += ((ProgrammePro)parcours.next()).toString(decalage+1) + "\n";
        }
        return output.substring(0, output.length()-1);
    }
    
    /**
     * Méthode de test.
     */
    public static void main(String argc[]) {
        ListeProgramme p = new ListeProgramme();
        bourse.sdd.Livre l1 = new bourse.sdd.Livre("l1", "a2", new bourse.protocole.Categorie(), "poche", "O'reilly", (float)50.65, (float)0.45, "2004-01-01", "222222222", 1, "Seb", (float)65);
        bourse.sdd.Livre l2 = new bourse.sdd.Livre("l2", "a1", new bourse.protocole.Categorie(), "poche", "Casterman", (float)40.75, (float)0.85, "1954-04-12", "222XX2254", 2, "protocoleman", (float)50);
        bourse.sdd.Livre l3 = new bourse.sdd.Livre("l2", "a1", new bourse.protocole.Categorie(), "poche", "Casterman", (float)40.75, (float)0.27, "1954-04-12", "222XX2254", 3, "arno", (float)12);
        p.ajouter(new ProgrammePro(12, l1));
        p.ajouter(new ProgrammePro(13, l2));
        p.ajouter(new ProgrammePro(14, l3));
        System.out.println(p.toString(0));
        
        p = new ListeProgramme();
        System.out.println(p.toString(0));
        
        p.ajouter(new ProgrammePro(12, l1));
        p.ajouter(new ProgrammePro(13, l2));
        p.ajouter(new ProgrammePro(12, l3)); // deux enchères ont meme numéro : on écrase l'ancien.
        System.out.println(p.toString(0));
    }
}
