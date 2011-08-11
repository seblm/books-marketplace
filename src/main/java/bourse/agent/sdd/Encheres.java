package bourse.agent.sdd;

import java.util.LinkedList;

/**
 * Donne toutes les encheres prévues par la pdm courante.
 */
public class Encheres { 

	/**
	 * donne la liste des encheres.
	 */
    private LinkedList<Enchere> encheres;
    
    /**
     * Construit une liste d' encheres vide.
     */
    public Encheres() {
    	this.encheres = new LinkedList<Enchere>();
    }
    
    /**
     * Ajouter une nouvelle enchere avec les valeurs essentielles.
     */
    public void ajouter(Enchere e) {
        this.encheres.add(new Enchere(e));
    }
    
    /**
     * Renvoie la p-ième enchere de la liste.
     */
    public Enchere get(int index) throws IndexOutOfBoundsException {
    	return this.encheres.get(index);
    }
    
    /**
     * Nettoyer la liste = supprimer toutes les encheres.
     */
    public void nettoyer() {
    	encheres.clear();
    }
    
    /**
     * Méthode d'affichage.
     */
    public String toString(int decalage) {
        String output = "";
        for (final Enchere enchere : encheres) {
        	output += enchere.toString(decalage+1) + "\n";
        }
        if (output.isEmpty()) {
        	return output;
        } else {
        	return output.substring(0, output.length()-1);
        }
    }

}
