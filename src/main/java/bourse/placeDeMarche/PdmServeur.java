package bourse.placeDeMarche;

import java.io.IOException;
import java.net.PortUnreachableException;
import java.util.MissingResourceException;

import bourse.reseau.Serveur;

/**
 * Écoute les demandes de connexion physique des agents et lance les threads qui
 * s'occuperont de chacun d'eux.
 */
public class PdmServeur extends Serveur {

	private PlaceDeMarche pdm;

	public PdmServeur(PlaceDeMarche pdm) throws PortUnreachableException, MissingResourceException {
		super(pdm != null ? pdm.getPort() : 1981);
		if (pdm != null) {
			this.pdm = pdm;
		} else {
			throw new MissingResourceException("Le serveur doit absolument connaître sa place de marché.",
					PdmServeur.class.getName(), "pdm");
		}
	}

	public void run() {
		if (pdm.getVerbose()) {
			System.out.println("Démarrage du serveur de la place de marché.");
		}
		while (pdm.getAccepterAgents()) {
			try {
				ConnexionAgent connexionAgent = new ConnexionAgent(getSocketServeur().accept(), this.pdm, false);
				connexionAgent.start();
			} catch (IOException e) {
				System.err.println(e);
			}
		}
	}

}