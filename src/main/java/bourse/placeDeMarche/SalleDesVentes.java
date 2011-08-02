package bourse.placeDeMarche;

import java.io.IOException;
import java.util.LinkedList;

/**
 * Gère l'enregistrement et le départ des agents.
 */
public class SalleDesVentes {

	private LinkedList<ConnexionAgent> agentsIdentifies;
	private PlaceDeMarche placeDeMarche;

	public SalleDesVentes(PlaceDeMarche placeDeMarche) {
		this.agentsIdentifies = new LinkedList<ConnexionAgent>();
		this.placeDeMarche = placeDeMarche;
	}

	/**
	 * Retourne la connexion de l'agent identifié par le nom de l'agent.
	 */
	public ConnexionAgent getConnexionAgent(String nomAgent) {
		for (final ConnexionAgent agentIdentifie : agentsIdentifies) {
			if (agentIdentifie.getAgent().getNomAgent()
					.equalsIgnoreCase(nomAgent)) {
				return agentIdentifie;
			}
		}
		return null;
	}

	/**
	 * Supprime l'agent de la salle des ventes.
	 */
	public void supprimerAgent(ConnexionAgent agent) {
		this.agentsIdentifies.remove(agent);
	}

	/**
	 * Supprimer tous les agents de la salle des ventes.
	 */
	public void supprimerTousLesAgents() {
		agentsIdentifies.clear();
	}

	/**
	 * Identifie l'agent et réveille le commissaire priseur afin qu'il démarre
	 * une enchère si le nombre d'agents identifiés n'était pas suffisant
	 * jusqu'à maintenant.
	 */
	public synchronized void identifierAgent(ConnexionAgent agent) {
		this.agentsIdentifies.add(agent);
		placeDeMarche.getCommissairePriseur().reveilleToi();
	}

	/**
	 * Libère tous les agents bloqués
	 */
	public synchronized void libererAgents() {
		for (final ConnexionAgent agentIdentifie : agentsIdentifies) {
			agentIdentifie.getAgent().setBloque(false);
		}
	}

	/**
	 * @return true si le nombre d'agents identifies est supérieur ou égal à 3.
	 */
	public synchronized boolean plusDeTroisAgentsIdentifies() {
		return agentsIdentifies.size() >= 3;
	}

	/**
	 * @return Vrai si au moins 3 agents autre que l'agent donné en paramètre
	 *         sont identifiés sur la place de marché.
	 */
	public synchronized boolean plusDeTroisAgentsIdentifies(Agent agentVendeur) {
		int borne = 3;
		if (estIdentifie(agentVendeur)) {
			borne = 4;
		}
		return agentsIdentifies.size() >= borne;
	}

	/**
	 * Détermine si un agent est identifié dans la salle des ventes. Cette
	 * méthode est une approximation de résultat sur la vraie structure de
	 * données stockant les agents connectés car elle ne compare l'existence que
	 * sur le nom de l'agent alors que le vrai test inclue aussi un numéro de
	 * port. Cependant, on s'est assuré lors de l'enregistrement de l'agent que
	 * la connexion vers un nom d'agent est unique.
	 * 
	 * @return true si l'agent est identifié, false sinon.
	 */
	public boolean estIdentifie(String nomAgent) {
		for (final ConnexionAgent agentIdentifie : agentsIdentifies) {
			if (agentIdentifie.getAgent().getNomAgent()
					.equalsIgnoreCase(nomAgent)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Détermine si un agent est identifié dans la salle des ventes. Utilise la
	 * méthode bourse.placeDeMarche.Agent.equals(Agent) qui compare à la fois le
	 * nom de l'agent et son port de connexion.
	 * 
	 * @return true si l'agent est identifié, false sinon.
	 */
	public synchronized boolean estIdentifie(Agent agent) {
		for (final ConnexionAgent agentIdentifie : agentsIdentifies) {
			if (agentIdentifie.getAgent().equals(agent)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Détermine si une connexion identifiée par son port communique avec un
	 * agent qui est identifié.
	 */
	public boolean estIdentifie(int port) {
		for (final ConnexionAgent agentIdentifie : agentsIdentifies) {
			if (agentIdentifie.getPort() == port) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Envoie un message à tous les agents identifiés dans la salle des ventes.
	 */
	public synchronized void envoyerIdentifies(String message) {
		for (final ConnexionAgent agentIdentifie : agentsIdentifies) {
			try {
				agentIdentifie.ecrire(message);
			} catch (IOException e) {
				e.printStackTrace(System.err);
			}
		}
	}

	/**
	 * Récupère la liste des agents identifiés à la place de marché. Utile
	 * notamment pour génerer une requête contenant la liste des agents
	 * connectés.
	 * 
	 * @return une liste chaînée remplie de chaînes de caractères et
	 *         représentant les noms des agents.
	 */
	public synchronized LinkedList<String> agentsToListeDeNoms() {
		LinkedList<String> resultat = new LinkedList<String>();
		for (final ConnexionAgent agentIdentifie : agentsIdentifies) {
			resultat.add(agentIdentifie.getAgent().getNomAgent());
		}
		return resultat;
	}

	public String toHtml() {
		String sortie = "<h2>SalleDesVentes</h2>\n<h3>Agents identifi&eacute;s</h3>\nIl y a "
				+ agentsIdentifies.size()
				+ " agents identifi&eacute;s : <ol>\n";
		if (agentsIdentifies.size() > 0) {
			for (final ConnexionAgent agentIdentifie : agentsIdentifies) {
				sortie += "<li>" + agentIdentifie.toHtml() + "</li>\n";
			}
		}
		return sortie;
	}
}