package bourse.agent.sdd;

import java.util.HashMap;
import java.util.Map;

import bourse.protocole.ResultBye;
import bourse.sdd.PDMPro;

/**
 * Stocke les informations recues du protocole ou de la bd concernant les pdms.
 */
public class ListePdm {
    
    /**
     * Liste des pdms.
     */
    private Map<String, Pdm> liste;
    
    /**
     * Construit une liste de pdms vide.
     */
    public ListePdm() {
    	liste = new HashMap<String, Pdm>();
    }
    
    /**
     * Construit une liste à partir d'une requete bye.
     */
    public ListePdm(ResultBye msg) {
        liste = new HashMap<String, Pdm>();
        for (PDMPro p : msg.getListepdm()) {
            ajouter(new Pdm(p.getNom(), p.getAdresse()));
        }
    }
    
    /**
     * Renvoie la liste.
     */
    protected Map<String, Pdm> getListe() {
    	return this.liste;
    }
    
    /**
     * Ajouter une pdm.
     */
    public void ajouter(Pdm pdm) {
    	liste.put(pdm.getNom(), pdm);
    }
    
    /**
     * Supprimer une pdm.
     */
    public void supprimer(Pdm pdm) {
    	liste.remove(pdm.getNom());
    }
    
    /**
     * Accès à une pdm de la liste à partir de son nom.
     */
    public Pdm acceder(String nom) {
    	return liste.get(nom);
    }
    
    /**
     * Renvoie vrai si la liste est vide.
     */
    public boolean estVide() {
    	return liste.isEmpty();
    }
    
    /**
     * Méthode d'affichage.
     */
    public String toString() {
        String output = "[";
        for (final Pdm pdm : liste.values()) {
        	output += pdm.toString(0) + ";\n ";
        }
        if (output.length() <= 2) {
        	return output + "]";
        } else {
        	return output.substring(0, output.length()-2) + "]";
        }
    }
    
}
