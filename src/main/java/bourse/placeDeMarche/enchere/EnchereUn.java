/*
 * EnchereUn.java
 *
 * Created on 22 janvier 2004, 13:22
 */

package bourse.placeDeMarche.enchere;

import bourse.sdd.*;
import bourse.protocole.*;
import java.util.TreeMap;

/**
 * 
 * @author pechot
 */
public class EnchereUn extends EnchereReponseUnique {

    Integer compteur;

    /** Creates a new instance of EnchereUn */
    public EnchereUn(int numEnchere, Livre livre) {
        super(numEnchere, livre);
        this.typeEnchere = Enchere.ENCHERE_UN;
        this.compteur = new Integer(0);
    }

    public EnchereUn(int numEnchere, float prixVente, int idLivre) {
        super(numEnchere, prixVente, idLivre);
        this.typeEnchere = Enchere.ENCHERE_UN;
        this.compteur = new Integer(0);
    }

    public PropositionEnchereP annonce() {
        return new PropositionEnchereP(Enchere.NOM[typeEnchere], numEnchere, TIMEOUT, livre, this.prixCourant);
    }

    public Resultat resolution() {

        if (propositions.isEmpty()) {
            System.out.println("EnchereUn : Il n'y a eu aucune proposition.");
            return new Resultat((Livre) this.livre, livre.getProprietaire(), this.prixCourant);
        } else {
            int choix = Enchere.generateurAleatoire.nextInt(propositions.size());
            return new Resultat((Livre) this.livre, (String) propositions.get(new Integer(choix)), this.prixCourant);
        }
    }

    public void actualiser(PropositionEnchereA propositionEnchere, String nom) {
        propositions.put(compteur, nom);
        compteur = new Integer(compteur.intValue() + 1);
    }

}
