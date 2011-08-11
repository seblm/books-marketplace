package bourse.placeDeMarche.enchere;

import java.util.Random;

import bourse.protocole.PropositionEnchereA;
import bourse.protocole.PropositionEnchereP;
import bourse.protocole.Resultat;
import bourse.sdd.Livre;

/**
 * @author pechot
 */
public class EnchereCinq extends EnchereReponseUnique {

    /**
     * Creates a new instance of EnchereDeux
     */
    public EnchereCinq(int numEnchere, Livre livre) {
        super(numEnchere, livre);
        this.typeEnchere = Enchere.ENCHERE_CINQ;
    }

    public EnchereCinq(int numEnchere, float prixVente, int idLivre) {
        super(numEnchere, prixVente, idLivre);
        this.typeEnchere = Enchere.ENCHERE_CINQ;
    }

    public void actualiser(PropositionEnchereA propositionEnchere, String nom) {
        Random rand = new Random();
        if (propositions.containsKey(propositionEnchere.getEnchere())) {
            if (rand.nextBoolean()) {
                // s'il y a deux propositions identiques on tire au hasard pour
                // savoir lequel on conserve
                propositions.put(propositionEnchere.getEnchere(), nom);
            }
        } else {
            propositions.put(propositionEnchere.getEnchere(), nom);
        }
    }

    public PropositionEnchereP annonce() {
        return new PropositionEnchereP(Enchere.NOM[typeEnchere], numEnchere, TIMEOUT, livre);
    }

    public Resultat resolution() {
        switch (propositions.size()) {
        case 0:
            return new Resultat(livre, livre.getProprietaire(), prixCourant);
        case 1:
            return new Resultat(livre, propositions.get(propositions.lastKey()), propositions.lastKey());
        default:
            String nom = propositions.get(propositions.lastKey());
            propositions.remove(propositions.lastKey());
            return new Resultat(livre, nom, propositions.lastKey());
        }
    }

}
