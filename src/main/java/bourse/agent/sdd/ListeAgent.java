package bourse.agent.sdd;

import java.util.*;

public class ListeAgent {
    
    /** Variables d'instances. */
    /** Donne la liste des agents. */
    private HashMap liste;
    
    /** Constructeur. */
    /** Construit une liste d'agent vide. */
    public ListeAgent() { this.liste = new HashMap(); }
    
    /** Méthodes. */
    /** Ajouter un agent concurrent. */
    public void ajouter(Agent a) { this.liste.put(a.getNom(), a); }
    /** Vrai si la liste contient un agent nommé nom. */
    public boolean contient(String nom) { return this.liste.containsKey(nom); }
    /** Méthode d'affichage. */
    public String toString(int decalage) {
        String delta ="";
        for (int i=0; i<decalage; i++) delta += " ";
        String output = "";
        Iterator parcours = this.liste.values().iterator();
        while (parcours.hasNext()) { output += delta + ((Agent)parcours.next()).toString(0) + "\n"; }
        if (output.length() == 0) return output;
        else return output.substring(0, output.length()-1);
    }
    /** Renvoie les valeurs du hashmap. Utile pour initialiser des iterator en 
     *  dehors de la classe. */
    public Collection valeurs() { return this.liste.values(); }
    /** Programme principal. */
    public static void main(String[] argc) {
        ListeAgent a = new ListeAgent();
        System.out.println("initialisation : ");
        a.ajouter(new Agent("a1"));
        a.ajouter(new Agent("a2"));
        a.ajouter(new Agent("a3"));
        System.out.println(a.toString(5));
    }
}
