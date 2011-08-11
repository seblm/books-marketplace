package bourse.placeDeMarche.enchere;

import bourse.protocole.PropositionEnchereA;
import bourse.protocole.PropositionEnchereP;
import bourse.protocole.Resultat;
import bourse.sdd.Livre;

/**
 * @author pechot
 */
public class EnchereUn extends EnchereReponseUnique {

    float compteur;

    /**
     * Creates a new instance of EnchereUn
     */
    public EnchereUn(int numEnchere, Livre livre) {
        super(numEnchere, livre);
        typeEnchere = Enchere.ENCHERE_UN;
        compteur = 0;
    }

    public EnchereUn(int numEnchere, float prixVente, int idLivre) {
        super(numEnchere, prixVente, idLivre);
        typeEnchere = Enchere.ENCHERE_UN;
        compteur = 0;
    }

    public PropositionEnchereP annonce() {
        return new PropositionEnchereP(Enchere.NOM[typeEnchere], numEnchere, TIMEOUT, livre, prixCourant);
    }

    public Resultat resolution() {
        if (propositions.isEmpty()) {
            return new Resultat(livre, livre.getProprietaire(), prixCourant);
        } else {
            float choix = Enchere.generateurAleatoire.nextInt(propositions.size());
            return new Resultat(livre, propositions.get(choix), prixCourant);
        }
    }

    public void actualiser(PropositionEnchereA propositionEnchere, String nom) {
        propositions.put(compteur, nom);
        compteur++;
    }

}
