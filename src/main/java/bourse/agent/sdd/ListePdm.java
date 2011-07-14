package bourse.agent.sdd;

import java.util.*;
import bourse.sdd.*;

/** Stocke les informations recues du protocole ou de la bd concernant les pdms. */
public class ListePdm {
    
    /** Variables d'instances. */
    /** Liste des pdms. */
    private HashMap liste;
    
    /** Constructeur. */
    /** Construit une liste de pdms vide. */
    public ListePdm() { this.liste = new HashMap(); }
    /** Construit une liste à partir d'une requete bye. */
    public ListePdm(bourse.protocole.ResultBye msg) {
        this.liste = new HashMap();
        java.util.ListIterator parcours = msg.getListepdm().listIterator();
        while (parcours.hasNext()) {
            PDMPro p = (PDMPro)parcours.next();
            ajouter(new bourse.agent.sdd.Pdm(p.getNom(), p.getAdresse()));
        }
    }
    
    /** Méthodes. */
    /** Renvoie la liste. */
    protected HashMap getListe() { return this.liste; }
    /** Ajouter une pdm. */
    public void ajouter(Pdm pdm) { this.liste.put(pdm.getNom(), pdm); }
    /** Supprimer une pdm. */
    public void supprimer(Pdm pdm) { this.liste.remove(pdm.getNom()); }
    /** Accès à une pdm de la liste à partir de son nom. */
    public Pdm acceder(String nom) { return (Pdm)this.liste.get(nom); }
    /** Renvoie vrai si la liste est vide. */
    public boolean estVide() { return this.liste.isEmpty(); }
    /** Méthode d'affichage. */
    public String toString() {
        String output = "[";
        Iterator parcours = this.liste.values().iterator();
        while (parcours.hasNext()) { output += ((Pdm)parcours.next()).toString(0) + ";\n "; }
        if (output.length() <= 2) return output + "]";
        else return output.substring(0, output.length()-2) + "]";
    }
    /** Programme principal. */
    public static void main(String[] argc) {
        ListePdm l = new ListePdm();
        Pdm p1 = new Pdm("p1", "192.168.1.2:8080");
        Pdm p2 = new Pdm("p2", "HOME");
        l.ajouter(p1);
        l.ajouter(p2);
        System.out.println(l.toString());
        l = new ListePdm((bourse.protocole.ResultBye)bourse.protocole.Protocole.newInstance("<MSG><RESULTBYE><PDM NOM=\"jhlfdhsl\" ADRESSE=\"192.168.1.1:24\"/></RESULTBYE></MSG>"));
        System.out.println(l.toString());
    }
}
