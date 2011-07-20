package bourse.agent.sdd;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import bourse.sdd.Livre;

/**
 * Les livres possédés.
 */
public class ListeLivre {

	/**
	 * La liste de livres (indéxée par l'id du livre).
	 */
	private Map<Integer, Livre> liste;

	/**
	 * Construit une liste vide de livres.
	 */
	public ListeLivre() {
		liste = new HashMap<Integer, Livre>();
	}

	/**
	 * Renvoie l'ensemble des valeurs de la liste. Utile seulement pour
	 * instancier un itérateur.
	 */
	public Collection<Livre> getValeurs() {
		return liste.values();
	}

	/**
	 * Renvoie la liste des livres.
	 */
	public Map<Integer, Livre> getListe() {
		return liste;
	}

	/**
	 * Ajoute un nouveau livre.
	 */
	public void ajouter(Livre l) {
		liste.put(new java.lang.Integer(l.getId()), l);
	}

	/**
	 * Retire un livre d'après l'id du livre.
	 */
	public void supprimer(Livre l) {
		liste.remove(new java.lang.Integer(l.getId()));
	}

	/**
	 * Retire un livre à partir de son id.
	 */
	public void supprimer(int id) {
		this.liste.remove(new java.lang.Integer(id));
	}

	/**
	 * Méthode d'affichage.
	 */
	public String toString(int decalage) {
		String delta = "";
		for (int i = 0; i < decalage; i++)
			delta += " ";
		String output = "";
		for (Livre livre : liste.values()) {
			output += delta + livre.toString(decalage + 1) + "\n";
		}
		if (output.length() == 0)
			return output;
		else
			return output.substring(0, output.length() - 1);
	}

}
