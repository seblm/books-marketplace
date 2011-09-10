package bourse.placeDeMarche.enchere;

import bourse.protocole.*;
import bourse.sdd.*;

public class EnchereTrois extends EnchereReponseMultiple {

    private long topChrono;

    public EnchereTrois(int numEnchere, Livre livre) {
        super(numEnchere, livre);
        typeEnchere = Enchere.ENCHERE_TROIS;
        topChrono = 0;
    }

    public EnchereTrois(int numEnchere, float prixVente, int idLivre) {
        super(numEnchere, prixVente, idLivre);
        typeEnchere = Enchere.ENCHERE_TROIS;
        topChrono = 0;
    }

    public PropositionEnchereP annonce() {
        topChrono = new java.util.Date().getTime();
        return new PropositionEnchereP(Enchere.NOM[typeEnchere], numEnchere, TIMEOUT, getPas(), livre, prixCourant);
    }

    public PropositionEnchereP reAnnonce() {
        long tempsRestant = new java.util.Date().getTime() - topChrono;
        return new PropositionEnchereP(Enchere.NOM[typeEnchere], numEnchere, (int) tempsRestant, getPas(), livre,
                prixCourant);
    }

    public PropositionEnchereP actualiser(PropositionEnchereA propositionEnchere, String nomAgentEncherisseur) {
        // Le message propositionEnchere est validé par le commissaire priseur.
        this.prixCourant = propositionEnchere.getEnchere();
        this.topChrono = new java.util.Date().getTime();
        this.nomAgent = nomAgentEncherisseur;
        return new PropositionEnchereP(Enchere.NOM[typeEnchere], numEnchere, TIMEOUT, getPas(), livre, prixCourant,
                nomAgentEncherisseur);
    }

    /** Appelé par le commisaire priseur lorsque l'enchère a été résolue. */
    public Resultat resolution(float prixResultat, String nomAcheteur) {
        return new Resultat(livre, nomAcheteur, prixResultat);
    }

    /** Appelé par le commissaire priseur lorsque l'enchère n'a pas été résolue. */
    public Resultat resolution() {
        return resolution(livre.getPrixAchat(), livre.getProprietaire());
    }

    public String toString() {
        return "Enchère ascendante";
    }

}
