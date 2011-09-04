package bourse.agent.ia;

import java.util.Iterator;
import java.util.Random;
import static bourse.agent.sdd.Action.bilan;
import bourse.agent.Agent;
import bourse.agent.sdd.Action;
import bourse.agent.sdd.ListeLivre;
import bourse.placeDeMarche.enchere.Enchere;
import bourse.protocole.ProposeVente;
import bourse.sdd.Livre;

public class Aleatoire extends Decision {
    
    private static final float THRESHOLD = 5;
    
    /**
     * Creates a new instance of Aleatoire
     */
    public Aleatoire(Agent appelant) {
        super(appelant);
    }
    
    /**
     * Algorithme de choix d'une action.
     */
    public void choixAction() {
        if (pere.getWallet() < THRESHOLD) {
            System.out.println("<<< Aléatoire >>> J'ai décidé de partir.");
            pere.setAction(bilan);
        } else {
            Action a = null;
            do {
                a = Action.randomAction();
                pere.setAction(a);
            } while (a == bilan);
            System.out.print("<<< Aléatoire >>> J'ai décidé ");
            switch (a) {
                case adversaires    : System.out.println("de demander la liste des agents connectés."); break;
                case attenteEnchere : System.out.println("d'attendre une enchère."); break;
                case aucune         : System.out.println("de ne rien faire."); break;
                case bilan          : System.out.println("de faire le bilan."); break;
                case migrer         : System.out.println("de migrer"); break;
                case programme      : System.out.println("de demander le programme."); break;
                case vendre         : System.out.println("de vendre un de mes livres."); break;
            }
        }
    }
    
    /**
     * On donne en entrée la liste des livres que l'on possède, l'agorithme
     * doit déterminer le livre à vendre. On retourne un objet
     * ProposeVente prêt à être exporté.
     */
    public ProposeVente choixLivreAVendre(ListeLivre l) {
        // L'algorithme choisit le premier livre qui n'appartient pas à la catégorie de l'agent.
        Iterator<Livre> i = l.getListe().values().iterator();
        boolean trouve = false;
        Livre livre = null;
        while (i.hasNext() && !trouve) {
            livre = (Livre)i.next();
            trouve = (!livre.getCategorie().equals(pere.getCategorie()));
        }
        return new ProposeVente(Enchere.NOM[new Random().nextInt(Enchere.NOM.length)], new Random().nextFloat()*livre.getPrixAchat(), livre.getId());
    }
    
    /**
     * C'est le prix qui sera envoyé à la pdm dans le message du protocole.
     */
    public float choixPrix() {
        Livre livre = pere.getEnvironnement().getCourante().getLivre();
        return livre.getPrix();
    }
    
    /**
     * C'est l'évalutation préalable du prix maximum que l'agent est prêt à
     * débourser pour vendre son livre.
     */
    public float choixPrixMax() {
        return pere.getWallet();
    }
    
    /**
     * C'est l'évaluation de l'intérêt porté par le livre en vente.
     */
    public boolean livreInteressant() {
        return pere.getEnvironnement().getCourante().getLivre().getCategorie().equals(pere.getCategorie());
    }
    
    /**
     * Renvoie le temps d'attente en millisecondes. Ce temps est utilisé par
     * l'agent pour "annuler" une attente de réponse à une de ses requêtes
     * synchronisées.
     */
    public long timeout() {
        return 0;
    }
    
    /**
     * Renvoie vrai si la vente est intéressante.
     */
    public boolean venteInteressante() {
        return new Random().nextBoolean();
    }
    /**
     * C'est l'évaluation de l'intérêt porté par le livre en vente.
     */
    public boolean livreInteressant(Livre l, int typeEnchere, float miseAPrix) {
        return new Random().nextBoolean();
    }
    
}
