package bourse.placeDeMarche.enchere;

import bourse.protocole.*;
import bourse.sdd.*;

public class EnchereQuatre extends EnchereReponseBoucle {

    private long topChrono;

    public EnchereQuatre(int numEnchere, Livre livre) {
        super(numEnchere, livre);
        topChrono = 0;
        this.typeEnchere = Enchere.ENCHERE_QUATRE;
    }

    public EnchereQuatre(int numEnchere, float prixVente, int idLivre) {
        super(numEnchere, prixVente, idLivre);
        this.typeEnchere = Enchere.ENCHERE_QUATRE;
        topChrono = 0;
    }

    public PropositionEnchereP annonce() {
        topChrono = new java.util.Date().getTime();
        return new PropositionEnchereP(NOM[typeEnchere], numEnchere, TIMEOUT, getPas(), livre, getPrixDepart());
    }

    /** Utilisé lorsqu'un agent se connecte "au milieu" d'une enchère. */
    public PropositionEnchereP reAnnonce() {
        long tempsRestant = new java.util.Date().getTime() - topChrono;
        return new PropositionEnchereP(NOM[typeEnchere], numEnchere, (int) tempsRestant, getPas(), livre,
                getPrixCourant());
    }

    public PropositionEnchereP actualiser() {
        // Le message propositionEnchere est validé par le commissaire priseur.
        this.prixCourant -= getPas();
        this.topChrono = new java.util.Date().getTime();
        return new PropositionEnchereP(Enchere.NOM[typeEnchere], numEnchere, TIMEOUT, getPas(), livre, prixCourant);
    }

    public Resultat resolution(float prixResultat, String acheteur) {
        return new Resultat(livre, acheteur, prixResultat);
    }

    public Resultat resolution() {
        return resolution(livre.getPrixAchat(), livre.getProprietaire());
    }

}