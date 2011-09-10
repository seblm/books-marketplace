/*
 * EnchereDeux.java
 *
 * Created on 28 janvier 2004, 13:26
 */

package bourse.placeDeMarche.enchere;

import bourse.sdd.*;
import bourse.protocole.*;
import java.util.TreeMap;
import java.util.Random;

/**
 *
 * @author  pechot
 */
public class EnchereCinq extends EnchereReponseUnique {
    
    /** Creates a new instance of EnchereDeux */
    public EnchereCinq(int numEnchere, Livre livre) {
        super(numEnchere,livre);
        this.typeEnchere=Enchere.ENCHERE_CINQ;
    }
    
    public EnchereCinq(int numEnchere, float prixVente, int idLivre) {
        super(numEnchere, prixVente, idLivre);
        this.typeEnchere =  Enchere.ENCHERE_CINQ;
    }
    
    public void actualiser(PropositionEnchereA propositionEnchere, String nom) {
       Random rand=new Random();
       if (propositions.containsKey(new Float(propositionEnchere.getEnchere())))
       {
            if (rand.nextBoolean())//s'il y a deux propositions identique on tire au hasard pour savoir lequel on conserve
                propositions.put(new Float(propositionEnchere.getEnchere()),nom);
       }
       else
           propositions.put(new Float(propositionEnchere.getEnchere()),nom);        
    }
    
    public PropositionEnchereP annonce() {
         return new PropositionEnchereP(Enchere.NOM[typeEnchere], numEnchere, TIMEOUT, livre);
    }
    
       
    public Resultat resolution() {
        switch(propositions.size())
        {
            case 0:
            {
                return new Resultat((Livre)livre,livre.getProprietaire(),this.prixCourant);
            }
            case 1:
            {
                return new Resultat((Livre)livre,(String)propositions.get(propositions.lastKey()),((Float)propositions.lastKey()).floatValue());
            }
            default:
            {
                String nom=(String)propositions.get(propositions.lastKey());
                propositions.remove(propositions.lastKey());
                return new Resultat((Livre)livre,nom,((Float)propositions.lastKey()).floatValue());
            }
        }
    }
        
}
