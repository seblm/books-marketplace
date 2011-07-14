package bourse.placeDeMarche;

import java.util.*;
import bourse.protocole.*;
import bourse.sdd.*;
import bourse.placeDeMarche.enchere.*;

/** Le commissaire priseur est en quelque sorte le chef d'orchestre de la gestion
 * des enchères. C'est lui qui lance les enchères et qui gère le programme.
 */
public class CommissairePriseur extends Thread {
    
    /** Le nombre d'enchères prévues par le commissaire priseur. */
    public static final int TAILLE_PROGRAMME = 5;
    
    private Programme programme;
    private Enchere enchereCourante = null;
    private PlaceDeMarche placeDeMarche;
    /** Symbolise le fait de savoir si le commissaire priseur est prêt à recevoir
     * des messages de la part des agents. */
    private boolean attendsDesPropositions;
    private PropositionEnchereA message;
    /** Représente le numéro de l'enchère la plus avancée prévue par le commissaire
     * priseur. */
    private int numEnchere;
    /** Le nombre de transactions restantes à exécuter. */
    private int nbTransactionsRestantes = Protocole.transactionsParPlaceDeMarche;
    /** Accède au programme prévu par le commissaire priseur. */
    public Programme getProgramme() { return this.programme; }
    public Enchere getEnchereCourante() { return this.enchereCourante; }
    private void incrementerNumEnchere() { this.numEnchere++; }
    private void decrementerNumEnchere() { this.numEnchere--; }
    private void decrementerNbTransactionsRestantes() {
        if (nbTransactionsRestantes > 0)
            nbTransactionsRestantes--;
    }
    
    public CommissairePriseur(PlaceDeMarche placeDeMarche) {
        this.placeDeMarche = placeDeMarche;
        this.programme = new Programme(new LinkedList());
        this.numEnchere = 1;
        this.attendsDesPropositions = true;
        this.message = null;
    }
    
    /** Récuppère la première des enchères prévue au programme, la stocke dans
     * la donnée membre <i>enchere courante</i> et l'enlève du programme.
     * Cependant, la méthode s'assure que si l'enchère est une proposition d'enchère
     * d'un agent, celui-ci soit bien présent sur la place de marché et le cas échéant
     * le bloque.
     */
    private void setEnchereCourante() {
        try {
            ProgrammePro enchere = (ProgrammePro)programme.getListeProgramme().removeFirst();
            creerItem();
            Livre livre = enchere.getLivre();
            if (!livre.getProprietaire().equalsIgnoreCase(placeDeMarche.getNom()))
                //vendeur different de la pdm
            {
                if (placeDeMarche.getSalleDesVentes().estIdentifie(livre.getProprietaire()))
                { // le vendeur est identifié
                    placeDeMarche.getSalleDesVentes().getConnexionAgent(livre.getProprietaire()).getAgent().setBloque(true);
                    enchereCourante = Enchere.newInstance(enchere.getNum(), enchere.getPrixVente(), livre.getId(), enchere.getTypeEnchere());
                }
                else
                { // le vendeur n'est plus présent
                    setEnchereCourante();
                }
            }
            else
            { // C'est la place de marché qui réalise la vente.
                enchereCourante = Enchere.newInstance(enchere.getNum(), livre);
            }
        } catch (NoSuchElementException e) { if (this.placeDeMarche.verbose) System.err.println("Le programme est vide, je "); }
    }
    
    /** Crée un nouveau livre prit au hasard parmi tous les livres de la base de 
     * données. Ne fait rien si le commissaire priseur a déjà fait plus de transactions
     * que 100 - tailleDuProgramme. */
    private void creerItem() {
        if (numEnchere <= Protocole.transactionsParPlaceDeMarche) {
            // Nous pouvons ajouter une nouvelle enchère au programme.
            programme.getListeProgramme().add(new ProgrammePro(this.numEnchere, placeDeMarche.getRequetes().creerItem()));
            this.incrementerNumEnchere();
        }
    }
    
    /** Cette méthode synchronisée pourra être appelée par plusieurs agents en
     * même temps, mais elle ne s'exécutera toujours qu'une seule fois.
     * Elle met à disposition son message et réveille le commissaire priseur.
     */
    public synchronized void vousAvezUnMessage(PropositionEnchereA message, ConnexionAgent connexionAgent) {
        System.out.println("Le commissaire Priseur a un message");
        while (!attendsDesPropositions) { try { wait(); } catch (InterruptedException e) { } }
        attendsDesPropositions = false;
        this.message = message;
        if (enchereCourante.getTypeEnchere() == Enchere.ENCHERE_TROIS)
            ((EnchereTrois)this.enchereCourante).setConnexionAgent(connexionAgent);
        else if (enchereCourante.getTypeEnchere() == Enchere.ENCHERE_QUATRE)
            ((EnchereQuatre)this.enchereCourante).setConnexionAgent(connexionAgent);
        this.notifyAll();
    }
    
    /** Annule l'enchère courante en décrémentant chaque numéro d'enchères dans
     * le programme. */
    private void annulerEnchere() {
        decrementerNumEnchere();
        ListIterator i = programme.getListeProgramme().listIterator();
        while (i.hasNext())
            ((ProgrammePro)i.next()).decrementerNumEnchere();
    }
    
    /** Le commissaire priseur a détecté qu'il y avait plus de 3 agents connectés
     * en dehors du vendeur éventuel de l'enchère et de ce fait démarre une enchère. */
    public void demarrerEnchere() {
        setEnchereCourante();
        if (placeDeMarche.getVerbose()) System.out.println("OUT PROPOSITIONENCHEREP");
        placeDeMarche.getSalleDesVentes().envoyerIdentifies(enchereCourante.annonce().toXML());
        Resultat resultat = null;
        switch (enchereCourante.getTypeEnchere()) {
        case Enchere.ENCHERE_UN :
        case Enchere.ENCHERE_DEUX :
        case Enchere.ENCHERE_CINQ :
            // Il s'agit d'enchères à réponse unique. Le commissaire priseur ne fait que s'endormir pendant la durée du timeout.
            try {
                wait(((EnchereReponseUnique)enchereCourante).TIMEOUT * 1000);
            } catch (InterruptedException e) { }
            resultat = enchereCourante.resolution();
            break;
        case Enchere.ENCHERE_TROIS :
            // Tant que j'attends des propositions, je dors TIMEOUT secondes.
            attendsDesPropositions = true;
            while (attendsDesPropositions) {
                try { wait(((EnchereReponseMultiple)enchereCourante).TIMEOUT * 1000); } catch (InterruptedException e) { }
                if (this.message == null)
                    // Le timeout a été atteint
                    attendsDesPropositions = false;
                else {
                    // Le commissaire priseur s'est fait réveillé par un agent.
                    enchereCourante.setPrixCourant(message.getEnchere());
                    if (placeDeMarche.getVerbose()) System.out.println("OUT PROPOSITIONENCHEREP");
                    placeDeMarche.getSalleDesVentes().envoyerIdentifies(((EnchereTrois)enchereCourante).actualiser(message,((EnchereReponseMultiple)enchereCourante).getConnexionAgent().getAgent().getNomAgent()).toXML());
                    attendsDesPropositions = true;
                    notifyAll();
                    message = null;
                }
            }
            ConnexionAgent dernierAgentEncherisseur = ((EnchereReponseMultiple)enchereCourante).getConnexionAgent();
            if (dernierAgentEncherisseur == null) // Aucun agent n'a enchéri, la vente est annulée.
                resultat = ((EnchereTrois)enchereCourante).resolution();
            else
                resultat = ((EnchereTrois)enchereCourante).resolution(enchereCourante.getPrixCourant(), dernierAgentEncherisseur.getAgent().getNomAgent());
            break;
        case Enchere.ENCHERE_QUATRE :
            attendsDesPropositions = true;
            while (message == null && enchereCourante.getPrixCourant() > 0.15*enchereCourante.getPrixDepart()) {
                // Dès que je reçois une proposition, j'arrete d'attendre
                try { wait(((EnchereReponseBoucle)enchereCourante).TIMEOUT * 1000); } catch (InterruptedException e) { }
                if (message == null) { // On est arrivé au TIMEOUT
                    if (placeDeMarche.getVerbose()) System.out.println("OUT PROPOSITIONENCHEREP");
                    placeDeMarche.getSalleDesVentes().envoyerIdentifies(((EnchereQuatre)enchereCourante).actualiser().toXML());
                }
            }
        }
        // Création d'une copie du livre vendu qui va sauvegarder le nouveau prix et le nouveau proprietaire.
        Livre nouveauLivre = new Livre(enchereCourante.getLivre());
        // Si c'est une enchère descendante, on résoud l'enchère
        if (enchereCourante.getTypeEnchere() == Enchere.ENCHERE_QUATRE) {
            if (message != null) { // On a reçu une proposition valable de la part d'un agent.
                nouveauLivre.setProprietaire(((EnchereReponseBoucle)enchereCourante).getConnexionAgent().getAgent().getNomAgent());
                resultat = ((EnchereQuatre)enchereCourante).resolution(enchereCourante.getPrixCourant(), ((EnchereReponseBoucle)enchereCourante).getConnexionAgent().getAgent().getNomAgent());
            } else // L'enchère n'a pas trouvé preneur et est arrivé à 1% du prix de départ
                resultat = enchereCourante.resolution();
            message = null;
        }
        // Le commissaire vient de se réveiller. Il va maintenant résoudre l'enchère.
        enchereCourante.setPrixCourant(resultat.getEnchere());

        if (!resultat.getAcheteur().equals(enchereCourante.getVendeur())) {
            // L'enchère a réussie. Le vendeur doit donc se voir retirer le livre contre l'argent de l'acheteur.
            System.out.println("CommissairePriseur : L'enchère a trouvé preneur.\nresultat.getAcheteur() = " + resultat.getAcheteur());
            if (!placeDeMarche.getRequetes().transaction(enchereCourante.getLivre().getId(), enchereCourante.getVendeur(), resultat.getAcheteur(), enchereCourante.getPrixCourant())) {
                // La mise à jour des informations a échouée. Il faut modifier le message de résultat et réordonner les numéros d'enchères courantes.
                resultat.setAcheteur(enchereCourante.getVendeur());
                annulerEnchere();
                System.out.println("CommissairePriseur : La transaction a échouée.");
            } else {
                // La transaction a bien eu lieu. Nous devons changer le propriétaire du livre, décrémenter le solde de l'agent, libérer les agents, décrémenter le nombre de transactions restantes.
                nouveauLivre.setProprietaire(resultat.getAcheteur());
                nouveauLivre.setPrixAchat(enchereCourante.getPrixCourant());
                
                // Retrait de l'argent à l'acheteur
                Agent acheteur = placeDeMarche.getSalleDesVentes().getConnexionAgent(resultat.getAcheteur()).getAgent();
                acheteur.setArgent(acheteur.getArgent() - enchereCourante.getPrixCourant());
                
                // Credit de l'argent au vendeur si c'est un agent
                ConnexionAgent connexionAgent = placeDeMarche.getSalleDesVentes().getConnexionAgent(enchereCourante.getVendeur());
                if (connexionAgent != null) { // C'est un agent qui a vendu.
                    Agent vendeur = connexionAgent.getAgent();
                    vendeur.setArgent(vendeur.getArgent() + enchereCourante.getPrixCourant());
                }

                decrementerNbTransactionsRestantes();
                System.out.println("CommissairePriseur : La transaction a réussie.");
            }
        } else {
            annulerEnchere();
            System.out.println("Commissaire Priseur : l'enchère n'a pas trouvée preneur.");
        }
        placeDeMarche.getSalleDesVentes().libererAgents();
        if (placeDeMarche.getVerbose()) System.out.println("OUT RESULTAT");
        placeDeMarche.getSalleDesVentes().envoyerIdentifies(resultat.toXML());
        enchereCourante.setLivre(nouveauLivre);
    }
    
    /** Permet de réveiller le commissaire priseur. */
    public synchronized void reveilleToi() { this.notifyAll(); }
    
    public synchronized void run() {
        System.out.println("Arrivée du commissaire priseur dans la salle des ventes.");
        // Le commissaire priseur réserve TAILLE_PROGRAMME livres au début.
        for (int i = 0; i < CommissairePriseur.TAILLE_PROGRAMME; i++) { creerItem(); }
        while (nbTransactionsRestantes >= 0 && placeDeMarche.getAccepterAgents()) {
            while (!placeDeMarche.getSalleDesVentes().plusDeTroisAgentsIdentifies() && placeDeMarche.getAccepterAgents()) {
                enchereCourante = null;
                // Il n'y a pas encore assez d'agents identifiés pour que le commissaire priseur lance une enchère.
                try { wait(); } catch (InterruptedException e) { System.err.println("J'ai été interrompu alors que j'attendais qu'il y ait assez d'agents identifiés pour lancer une enchère."); }
                System.out.println("Commissaire priseur : je me suis fait réveillé.");
                notifyAll();
            }
            demarrerEnchere();
        }
    }
    
    public String toHtml() {
        String sortie = "<h2>Commissaire Priseur</h2>\n";
        sortie += "<p>Il me reste <b>" + nbTransactionsRestantes + "</b> transactions avant la fin de la session.<br>J'anticipe " + TAILLE_PROGRAMME + " ench&egrave;res dans mon programme.</p>";
        if (enchereCourante != null)
            sortie += "<h3>Ench&egrave;re courante</h3>" + enchereCourante.toHtml();
        sortie += "<h3>Programme</h3>" + programme.toHtml();
        return sortie;
    }
    
}