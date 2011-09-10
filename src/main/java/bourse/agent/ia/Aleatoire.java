package bourse.agent.ia;

public class Aleatoire extends Decision {

    /** Creates a new instance of Aleatoire */
    public Aleatoire(bourse.agent.Agent appelant) {
        super(appelant);
    }

    /** Algorithme de choix d'une action. */
    public void choixAction() {
        if (pere.getWallet() < (float) 5.0) {
            System.out.println("<<< Aleatoire >>> J'ai décidé de partir.");
            pere.setAction(new bourse.agent.sdd.Action(bourse.agent.sdd.Action.bilan));
        } else {
            bourse.agent.sdd.Action a = null;
            do {
                a = new bourse.agent.sdd.Action();
                pere.setAction(a);
            } while (a.getAction() == bourse.agent.sdd.Action.bilan);
            System.out.print("<<< Aleaoire >>> J'ai décidé ");
            switch (a.getAction()) {
            case bourse.agent.sdd.Action.adversaires:
                System.out.println("de demander la liste des agents connectés.");
                break;
            case bourse.agent.sdd.Action.attenteEnchere:
                System.out.println("d'attendre une enchère.");
                break;
            case bourse.agent.sdd.Action.aucune:
                System.out.println("de ne rien faire.");
                break;
            case bourse.agent.sdd.Action.bilan:
                System.out.println("de faire le bilan.");
                break;
            case bourse.agent.sdd.Action.migrer:
                System.out.println("de migrer");
                break;
            case bourse.agent.sdd.Action.programme:
                System.out.println("de demander le programme.");
                break;
            case bourse.agent.sdd.Action.vendre:
                System.out.println("de vendre un de mes livres.");
                break;
            }
        }
    }

    /**
     * On donne en entrée la liste des livres que l'on possède, l'agorithme doit
     * déterminer le livre à vendre. On retourne un objet
     * bourse.protocole.ProposeVente prêt à être exporté.
     */
    public bourse.protocole.ProposeVente choixLivreAVendre(bourse.agent.sdd.ListeLivre l) {
        // L'algorithme choisit le premier livre qui n'appartient pas à la
        // catégorie de l'agent.
        java.util.Iterator i = l.getListe().values().iterator();
        boolean trouve = false;
        bourse.sdd.Livre livre = null;
        while (i.hasNext() && !trouve) {
            livre = (bourse.sdd.Livre) i.next();
            trouve = (!livre.getCategorie().equals(pere.getCategorie()));
        }
        return new bourse.protocole.ProposeVente(
                bourse.placeDeMarche.enchere.Enchere.NOM[new java.util.Random().nextInt(5)],
                new java.util.Random().nextFloat() * livre.getPrixAchat(), livre.getId());
    }

    /** C'est le prix qui sera envoyé à la pdm dans le message du protocole. */
    public float choixPrix() {
        bourse.sdd.Livre livre = pere.getEnvironnement().getCourante().getLivre();
        return livre.getPrix();
    }

    /**
     * C'est l'évalutation préalable du prix maximum que l'agent est prêt à
     * débourser pour vendre son livre.
     */
    public float choixPrixMax() {
        return pere.getWallet();
    }

    /** C'est l'évaluation de l'intérêt porté par le livre en vente. */
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

    /** Renvoie vrai si la vente est intéressante. */
    public boolean venteInteressante() {
        return new java.util.Random().nextBoolean();
    }

    /** C'est l'évaluation de l'intérêt porté par le livre en vente. */
    public boolean livreInteressant(bourse.sdd.Livre l, int typeEnchere, float miseAPrix) {
        return new java.util.Random().nextBoolean();
    }

}
