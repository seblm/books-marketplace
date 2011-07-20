package bourse.agent.sdd;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import bourse.agent.Memoire;
import bourse.agent.Visualisation;
import bourse.protocole.Categorie;
import bourse.sdd.Livre;

public class ListeAgentMemoire {

	private Memoire pere;

	/**
	 * Donne la liste des agents.
	 */
	private Map<String, AgentMemoire> liste;

	/**
	 * Lien vers la fenetre centrale.
	 */
	private Visualisation fenetre;

	/**
	 * Construit une liste d'agent vide.
	 */
	public ListeAgentMemoire(bourse.agent.Visualisation fenetre, Memoire m) {
		pere = m;
		liste = new HashMap<String, AgentMemoire>();
		this.fenetre = fenetre;
	}

	public int taille() {
		return liste.size();
	}

	/**
	 * Rafraichir l'affichage de la liste.
	 */
	public void refresh() {
		int numeroLigne = 0;
		// Réinstanciation du JTable pour que sa taille soit celle de la
		// mémoire.
		DefaultTableModel tm = new DefaultTableModel(new String[] { "Nom", "Catégorie", "Présent",
				"Fréquence des catégories" }, liste.size());
		JTable tableau = new JTable(tm);
		for (AgentMemoire agentMemoire : liste.values()) {
			agentMemoire.toRow(tableau, numeroLigne);
			numeroLigne++;
		}
		fenetre.getJScrollPaneAgentMemoire().remove(fenetre.getTableauPdmMemoire());
		fenetre.getJScrollPaneAgentMemoire().setViewportView(tableau);
	}

	/**
	 * Changer la présence de tous les agents.
	 */
	public void setPresent(boolean present) {
		for (AgentMemoire agentMemoire : liste.values()) {
			agentMemoire.setPresent(present);
		}
	}

	/**
	 * Ajouter un agent concurrent.
	 */
	public void ajouter(AgentMemoire a) {
		liste.put(a.getNom(), a);
	}

	/**
	 * Vrai si la liste contient un agent nommé nom.
	 */
	public boolean contient(String nom) {
		return liste.containsKey(nom);
	}

	/**
	 * Met à jour la catégorie des adversaires et la fréquence des catégories de
	 * livres de chaque agent.
	 */
	public void miseAJourCategorie() {
		// Le nombre de livres de la catégorie i
		int[] nb = new int[5];
		for (int i = 0; i < 5; i++)
			nb[i] = 0;
		int total = 0;
		int maxnb;
		int ind;
		/** pour chaque agent, */
		for (AgentMemoire agentMemoire : liste.values()) {
			ListeLivre l = pere.getPossessions().possede(agentMemoire.getNom());
			// parcourir les livres et renvoyer le max.
			int categorieCourante;
			// on met le tableau de stats à zéro.
			for (int i = 0; i < 5; i++)
				nb[i] = 0;
			// le total des transaction vaut zéro.
			total = 0;
			// pour chaque livre possédé,
			for (Livre livre : l.getValeurs()) {
				categorieCourante = new Categorie(livre.getCategorie()).getCode();
				if (categorieCourante != 5) {
					// incrémentation du nombre de livre de la catégorie i
					nb[categorieCourante] = nb[categorieCourante] + 1;
					total++;
				}
			}
			// si le total des transaction n'est pas nul, on met à jour les
			// statistiques.
			if (total != 0) {
				for (int i = 0; i < 5; i++) {
					agentMemoire.getStatsCategorie()[i] = (float) ((float) nb[i] / (float) total);
				}
			}
			// mise à jour de la catégorie : la catégorie la plus fréquemment
			// achetée.
			maxnb = 0;
			ind = 5;
			for (int i = 0; i < 5; i++) {
				if (maxnb < nb[i]) {
					maxnb = nb[i];
					ind = i;
				}
			}
			agentMemoire.setCategorie(new Categorie(ind));
		}
	}

	/**
	 * Méthode de mise à jour globale à partir d'une liste d'agents. Typiquement
	 * : après avoir téléchargé la nouvelle liste.
	 */
	public void miseAJour(ListeAgent l) {
		setPresent(false);
		for (Agent agent : l.valeurs()) {
			if (contient(agent.getNom())) {
				liste.get(agent.getNom()).setPresent(true);
			} else {
				ajouter(new AgentMemoire(agent.getNom(), new Categorie(Categorie.AUCUNE), true));
			}
		}
		miseAJourCategorie();
		refresh();
	}

	/**
	 * Mettre à jour les listes avec une liste de String représentant les noms
	 * des agents.
	 */
	public void miseAJour(List<String> listeAgents) {
		ListeAgent destination = new ListeAgent();
		for (String nomAgent : listeAgents) {
			destination.ajouter(new Agent(nomAgent));
		}
		this.miseAJour(destination);
	}

	/**
	 * Méthode d'affichage.
	 */
	public String toString(int decalage) {
		String output = new String();
		for (AgentMemoire agentMemoire : liste.values()) {
			output += agentMemoire.toString(decalage) + "\n";
		}
		if (output.length() == 0) {
			return output;
		} else {
			return output.substring(0, output.length() - 1);
		}
	}

	/**
	 * Renvoie le nombre d'agents connéctés de la même catégoruie.
	 */
	public int concurrent(Categorie cat, String nom) {
		int cpt = 0;
		// pour chaque agent connecté,
		for (AgentMemoire am : liste.values()) {
			// si l'agent courant n'est notre agent.
			if (!(am.getNom().equals(nom))) {
				// si l'agent est présent.
				if (am.getPresent()) {
					// si l'agent a la catégorie voulue
					if (am.getCategorie().equals(cat)) {
						cpt++;
					}
				}
			}
		}
		return cpt;
	}

	public float coefficientCategorie(Categorie c) {
		float sommeCategorie = 0;
		int nbAgents = 0;
		float meilleureStat = 0;
		for (AgentMemoire am : liste.values()) {
			if (am.getPresent()) {
				nbAgents++;
				float statC = am.getStatsCategorie()[c.getCode()];
				sommeCategorie += statC;
				if (meilleureStat < statC) {
					meilleureStat = statC;
				}
			}
		}
		if (meilleureStat > 0.8) {
			return meilleureStat;
		} else {
			return sommeCategorie / ((nbAgents == 0) ? 1 : nbAgents);
		}
	}

}
