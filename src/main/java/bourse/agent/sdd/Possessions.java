package bourse.agent.sdd;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import bourse.agent.Memoire;
import bourse.agent.Visualisation;
import bourse.sdd.Livre;

/**
 * Donne toutes les Possessions que l'agent connait.
 */
public class Possessions {

	private Memoire pere;

	/**
	 * Donne la liste des possessions indexées par le numéro identifiant l'item
	 * (le livre).
	 */
	private Map<Integer, Possession> liste;

	/**
	 * Lien vers la fenetre centrale.
	 */
	private bourse.agent.Visualisation fenetre;

	/**
	 * Construit une liste de possessions vide.
	 */
	public Possessions(Visualisation fenetre, Memoire memoire) {
		liste = new HashMap<Integer, Possession>();
		this.fenetre = fenetre;
		pere = memoire;
	}

	/**
	 * Récupérer une possession à partir de la clé du livre.
	 */
	public Possession get(int id) {
		return liste.get(id);
	}

	/**
	 * Récupérer une possession à partir du livre.
	 */
	public Possession get(Livre l) {
		return liste.get(l.getId());
	}

	/**
	 * Rafraichir l'affichage de la liste.
	 */
	public void refresh() {
		int numeroLigne = 0;
		// Réinstanciation du JTable pour que sa taille soit celle de la
		// mémoire.
		DefaultTableModel tm = new DefaultTableModel(new String[] { "Date", "Id", "Propriétaire", "Catégorie", "Prix",
				"Etat", "Prix d'achat", "Titre", "Auteur", "Date de parution", "Editeur", "Format", "ISBN" },
				liste.size());
		JTable tableau = new JTable(tm);
		for (Possession possession : liste.values()) {
			possession.toRow(tableau, numeroLigne);
			numeroLigne++;
		}
		fenetre.getJScrollPanePossessionMemoire().remove(fenetre.getTableauPossessionMemoire());
		fenetre.getJScrollPanePossessionMemoire().setViewportView(tableau);
		// En même temps, mise à jour des stats sur la catégorie.
		pere.getAgents().refresh();
	}

	/**
	 * Ajouter un livre à la date actuelle. Attention, écrase l'ancienne
	 * possession. Car si on ne précise pas la date, c'est que le livre est plus
	 * récent.
	 */
	public void ajouter(bourse.sdd.Livre l) {
		liste.put(l.getId(), new Possession(l));
		refresh();
	}

	/**
	 * Ajouter une possession c'est-à-dire le livre et la date spécifiée.
	 */
	public void ajouter(Possession t) {
		// si le livre est déjà connu.
		if (liste.containsKey(t.getLivre().getId())) {
			Possession existante = get(t.getLivre());
			// comparer les dates
			if (t.getdate() > existante.getdate()) {
				// l'objet à ajouter est plus récent : on doit l'ajouter.
				liste.put(t.getLivre().getId(), t);
			}
			// sinon, on garde la possession la plus récente.
		}
		// sinon, on l'ajoute.
		else {
			liste.put(t.getLivre().getId(), t);
		}
		refresh();
	}

	/**
	 * retire une possession de la liste.
	 */
	public void supprimer(Livre l) {
		liste.remove(l.getId());
	}

	/**
	 * retire une possession de la liste d'après l'id du livre.
	 */
	public void supprimer(int id) {
		liste.remove(id);
	}

	/**
	 * renvoie la liste des livres possédés par le propriétaire (agent ou pdm).
	 */
	public ListeLivre possede(String nom) {
		ListeLivre li = new ListeLivre();
		for (Possession possession : liste.values()) {
			if (possession.getLivre().getProprietaire().equals(nom)) {
				li.ajouter(possession.getLivre());
			}
		}
		return li;
	}

	public Collection<Possession> getValues() {
		return liste.values();
	}

	/**
	 * Méthode d'affichage.
	 */
	public String toString(int decalage) {
		String delta = "";
		for (int i = 0; i < decalage; i++)
			delta += " ";
		String output = new String();
		for (Possession possession : liste.values()) {
			output += delta + "possession =\n" + possession.toString(decalage + 1) + "\n";
		}
		if (output.length() == 0) {
			return output;
		} else {
			return output.substring(0, output.length() - 1);
		}
	}

}