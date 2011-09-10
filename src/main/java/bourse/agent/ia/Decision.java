package bourse.agent.ia;

import bourse.agent.sdd.*;

/** Centralise les méthodes d'ia. */
public abstract class Decision {
    
    /** Variables d'instances. */
    /** Lien vers l'agent. */
    protected bourse.agent.Agent pere;
    
    /** Méthodes */
    /** Constructeur. */
    public Decision(bourse.agent.Agent appelant) { this.pere = appelant; }
    /** Méthode de choix d'une pdm.
     *   1° on doit connaître les pdms actives (etat = 2).
     *   2° choisir la première pdm active non visitée
     *   3° sinon la première pdm active visitée
     *   4° initialiser la pdm courante.
     *   5° si on a pas trouvé, la pdm courante n'est pas modifiée. */
    public void choixPdm() {
        if (pere.getEtat() == Etat.connaitPdms) { // l'agent doit être dans le bon état.
            if (!pere.getMemoire().getPdms().aucuneActive()) { // la liste doit contenir des pdms actives
                PdmMemoire choisie = pere.getMemoire().getPdms().premiereActiveNonVisitee();
                if (choisie == null) choisie = pere.getMemoire().getPdms().premiereActiveVisitee();
                if (choisie != null) { // on a trouvé une pdm satisfaisante.
                    pere.setEtat(Etat.pdmChoisie); // changer d'état
                    pere.setHote(choisie.getNom()); // mettre à jour le nom de la pdm choisie.
                    choisie.setVisitee(true); // on visite la pdm. 
                    pere.getMemoire().getPdms().ajouter(choisie); // mise à jour de la pdm choisie dans la mémoire.
                } else System.out.println("Aucune pdm active visitée ou active non visitée trouvée.");
            } else System.out.println("Aucune pdm n'est active.");
        } else System.out.println("Impossible de choisir une pdm dans ces conditions.");
    }
    /** Renvoie le temps d'attente en millisecondes. Ce temps est urilisé par
     *  l'agent pour "annuler" une attente de réponse à une de ses requêtes
     *  synchronisées. */
    public abstract long timeout();
    /** Algorithme de choix d'une action. */
    public abstract void choixAction();
    /** C'est le prix qui sera envoyé à la pdm dans le message du protocole. */
    public abstract float choixPrix();
    /** C'est l'évalutation préalable du prix maximum que l'agent est prêt à
     *  débourser pour vendre son livre. */
    public abstract float choixPrixMax();
    /** C'est l'évaluation de l'intérêt porté par le livre en vente. */
    public abstract boolean livreInteressant(bourse.sdd.Livre l, int typeEnchere, float miseAPrix);
    /** On donne en entrée la liste des livres que l'on possède, l'agorithme
     *  doit déterminer le livre à vendre. On retourne un objet
     *  bourse.protocole.ProposeVente prêt à être exporté. */
    public abstract bourse.protocole.ProposeVente choixLivreAVendre(ListeLivre l);
    /** Renvoie vrai si la vente est intéressante. */
    public abstract boolean venteInteressante();
}
