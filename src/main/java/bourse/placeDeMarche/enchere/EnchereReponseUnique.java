package bourse.placeDeMarche.enchere;

import java.util.TreeMap;
import bourse.sdd.*;
import bourse.protocole.*;

public abstract class EnchereReponseUnique extends Enchere {
    
    public static int TIMEOUT = 5;
    
    protected TreeMap propositions;
    
    public TreeMap getProposition(){
        return this.propositions;
    }
    public void setProposition(TreeMap propositions){
        this.propositions=propositions;
    }
    
    public abstract void actualiser(PropositionEnchereA propositionEnchere,String nom);
    public abstract Resultat resolution();
    public abstract PropositionEnchereP annonce();
    
    /** Utilisé lorsqu'un agent se connecte "au milieu" d'une enchère   */
    public PropositionEnchereP reAnnonce() {
        return this.annonce();
    }
    
    /** Creates a new instance of EnchereReponseUnique */
    public EnchereReponseUnique(int numEnchere,Livre livre) {
        super(numEnchere,livre);
        propositions = new TreeMap();
    }
       
    protected EnchereReponseUnique(int numEnchere, float prixVente, int idLivre) {
        super(numEnchere, prixVente, idLivre);
        propositions = new TreeMap();
    }

}
