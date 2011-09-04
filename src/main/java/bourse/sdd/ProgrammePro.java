/*
 * Created on 20 janvier 2004, 10:07
 */

package bourse.sdd;

import bourse.placeDeMarche.enchere.Enchere;

/**
 * @author pechot
 */
public class ProgrammePro {

    private int numEnchere;

    public Livre livre;

    private int typeEnchere;

    private float prixVente;

    /**
     * Constructeur appelé lorsque le type de l'enchère n'est pas connue.
     */
    public ProgrammePro(int num, Livre livre) {
        numEnchere = num;
        this.livre = livre;
        typeEnchere = Enchere.ENCHERE_INCONNUE;
        prixVente = 0f;
    }

    /**
     * Constructeur appelé lorsque l'agent propose une vente.
     */
    public ProgrammePro(int num, Livre livre, int typeEnchere, float prixVente) {
        this.numEnchere = num;
        this.livre = livre;
        this.typeEnchere = typeEnchere;
        this.prixVente = prixVente;
    }

    public int getNum() {
        return numEnchere;
    }

    public float getPrixVente() {
        return prixVente;
    }

    public int getTypeEnchere() {
        return typeEnchere;
    }

    public Livre getLivre() {
        return livre;
    }

    public void decrementerNumEnchere() {
        numEnchere--;
    }

    public void incrementerNumEnchere() {
        numEnchere++;
    }

    /**
     * Méthode d'affichage qui décale l'affichage de décalage.
     */
    public String toString(int decalage) {
        String delta = "";
        for (int i = 0; i < decalage; i++)
            delta += " ";
        return delta + "numéro = " + this.numEnchere + ", livre = [ " + this.livre.toString(0) + " ]";
    }

    @Override
    public boolean equals(Object object) {
        if (object == null || !(object instanceof ProgrammePro)) {
            return false;
        }
        final ProgrammePro programmePro = (ProgrammePro) object;
        return numEnchere == programmePro.numEnchere && prixVente == programmePro.prixVente
                && typeEnchere == programmePro.typeEnchere && livre == programmePro.livre;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 31 * hash + numEnchere;
        hash = 31 * hash + (null == livre ? 0 : livre.hashCode());
        hash = 31 * hash + typeEnchere;
        hash = 31 * hash + new Float(prixVente).hashCode();
        return hash;
    }

}
