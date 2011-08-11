package bourse.placeDeMarche.enchere;

import java.util.SortedMap;
import java.util.TreeMap;

import bourse.protocole.PropositionEnchereA;
import bourse.protocole.PropositionEnchereP;
import bourse.protocole.Resultat;
import bourse.sdd.Livre;

public abstract class EnchereReponseUnique extends Enchere {

    public static int TIMEOUT = 5;

    protected SortedMap<Float, String> propositions;

    public SortedMap<Float, String> getProposition() {
        return propositions;
    }

    public void setProposition(SortedMap<Float, String> propositions) {
        this.propositions = propositions;
    }

    public abstract void actualiser(PropositionEnchereA propositionEnchere, String nom);

    public abstract Resultat resolution();

    public abstract PropositionEnchereP annonce();

    /**
     * Utilisé lorsqu'un agent se connecte "au milieu" d'une enchère
     */
    public PropositionEnchereP reAnnonce() {
        return this.annonce();
    }

    /**
     * Creates a new instance of EnchereReponseUnique
     */
    public EnchereReponseUnique(int numEnchere, Livre livre) {
        super(numEnchere, livre);
        propositions = new TreeMap<Float, String>();
    }

    protected EnchereReponseUnique(int numEnchere, float prixVente, int idLivre) {
        super(numEnchere, prixVente, idLivre);
        propositions = new TreeMap<Float, String>();
    }

}
