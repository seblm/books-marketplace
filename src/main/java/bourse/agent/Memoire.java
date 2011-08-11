package bourse.agent;

import java.util.Iterator;

import bourse.agent.sdd.AideDecisionVente;
import bourse.agent.sdd.ListeAgentMemoire;
import bourse.agent.sdd.ListePdmMemoire;
import bourse.agent.sdd.PdmMemoire;
import bourse.agent.sdd.Possessions;

/**
 * Répertorie toutes les informations utiles pour un agent de conserver.
 */
public class Memoire {
	
    /**
     * Pdms.
     */
    private ListePdmMemoire pdms;
    
    /**
     * L'ensemble des agents qu'il connait.
     */
    private ListeAgentMemoire agents;
    
    /**
     * L'ensemble des transactions.
     */
    private Possessions possessions;
    
    /**
     * Stocke les statisiques intéressantes pour aider à la decision d'une vente.
     */
    private AideDecisionVente stats;
    
    /**
     * Stocke le temps comme la moyenne actuelle des tours de chaque pdm. Entre 1 et 100.
     */
    private int temps = 0;
    
    /**
     * Stocke l'id du livre qu'on a décidé de vendre.
     */
    private int livreAVendre;
    
    /**
     * Constructeurs par défaut.
     */
	public Memoire(bourse.agent.Agent pere, Visualisation fenetre) {
        this.pdms = new ListePdmMemoire(fenetre); 
        this.agents = new ListeAgentMemoire(fenetre, this);
        this.possessions = new Possessions(fenetre, this);
        this.stats = new AideDecisionVente(pere);
        this.temps = 0;
    }
    
    /**
     * Renvoie la liste des pdm.
     */
    public ListePdmMemoire getPdms() {
    	return this.pdms;
    }
    
    /**
     * Renvoie la liste des livres.
     */
    public ListeAgentMemoire getAgents() {
    	return this.agents;
    }
    
    /**
     * Renvoie la liste des transactions.
     */
    public Possessions getPossessions() {
    	return this.possessions;
    }
    
    /**
     * Modifie le temps.
     */
    public void setTemps(int temps) {
    	this.temps = temps;
    }
    
    /**
     * Renvoie le temps
     */
    public int getTemps() {
    	return this.temps;
    }
    
    public int getLivreAVendre() {
    	return livreAVendre;
    }
    
    public void setLivreAVendre(int _id) {
    	livreAVendre = _id;
    }
    
    public AideDecisionVente getAideDecisionVente() {
    	return stats;
    }
    
    /**
     * Méthode d'affichage.
     */
    public String toString(int decalage) {
        String delta = "";
        for (int i=0; i<decalage; i++) delta += " ";
        return delta + "pdms :\n" + this.pdms.toString(decalage+1) + "\n"
             + delta + "agents :\n" + this.agents.toString(decalage+1) + "\n"
             + delta + "possessions :\n" + this.possessions.toString(decalage+1) + "\n"
             + delta + "temps :\n" + this.temps;
    }
    
    public void refreshTemps() {
        ListePdmMemoire liste = this.getPdms();
		Iterator<PdmMemoire> parcours = liste.getListe().values().iterator();
        int total = 0;
        int cpt = 0;
        while (parcours.hasNext()) {
            total += ((PdmMemoire)parcours.next()).getNumeroDernierTour();
            cpt++;
        }
        temps = total / cpt; 
    }
    
}
