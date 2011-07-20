package bourse.agent.sdd;

import java.util.*;

public class ListeAgent {
    
    /** Variables d'instances. */
    /** Donne la liste des agents. */
    private Map<String, Agent> liste;
    
    /** Constructeur. */
    /** Construit une liste d'agent vide. */
    public ListeAgent() { this.liste = new HashMap<String, Agent>(); }
    
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
        for (Agent agent : liste.values()) {
        	output += delta + agent.toString(0) + "\n";
        }
        if (output.length() == 0) return output;
        else return output.substring(0, output.length()-1);
    }
    /** Renvoie les valeurs du hashmap. Utile pour initialiser des iterator en 
     *  dehors de la classe. */
    public Collection<Agent> valeurs() { return this.liste.values(); }
}
