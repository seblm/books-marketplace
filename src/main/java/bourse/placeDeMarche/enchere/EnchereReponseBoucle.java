package bourse.placeDeMarche.enchere;

import bourse.sdd.*;
import bourse.placeDeMarche.*;

public abstract class EnchereReponseBoucle extends Enchere {

    public static int TIMEOUT = 1;
    protected ConnexionAgent agent;
    
    public ConnexionAgent getConnexionAgent() { return agent; }
    public void setConnexionAgent(ConnexionAgent connexionAgent) { this.agent = connexionAgent; }

    public abstract bourse.protocole.PropositionEnchereP annonce();
    public abstract bourse.protocole.Resultat resolution();
    
    
    protected EnchereReponseBoucle(int numEnchere, Livre livre) { super(numEnchere, livre); }
    protected EnchereReponseBoucle(int numEnchere, float prixVente, int idLivre) { super(numEnchere, prixVente, idLivre); }

    protected void decrementerPrixCourant() { prixCourant *= 1 - Enchere.POURCENTAGE_PAS; }

}