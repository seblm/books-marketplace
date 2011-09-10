package bourse.placeDeMarche.enchere;

import bourse.protocole.*;
import bourse.sdd.*;
import bourse.placeDeMarche.*;

public abstract class EnchereReponseMultiple extends Enchere {

    public static int TIMEOUT = 5;

    private ConnexionAgent agent;

    public float getPrixCourant() {
        return this.prixCourant;
    }

    public void setPrixCourant(float prixCourant) {
        this.prixCourant = prixCourant;
    }

    public ConnexionAgent getConnexionAgent() {
        return this.agent;
    }

    public void setConnexionAgent(ConnexionAgent connexionAgent) {
        this.agent = connexionAgent;
    }

    public abstract PropositionEnchereP actualiser(PropositionEnchereA propostionEnchere, String nomAgentEncherisseur);

    public abstract PropositionEnchereP annonce();

    public abstract Resultat resolution();

    protected void incrementerPrixCourant() {
        prixCourant *= 1 + Enchere.POURCENTAGE_PAS;
    }

    protected EnchereReponseMultiple(int numEnchere, Livre livre) {
        super(numEnchere, livre);
    }

    protected EnchereReponseMultiple(int numEnchere, float prixVente, int idLivre) {
        super(numEnchere, prixVente, idLivre);
    }
}
