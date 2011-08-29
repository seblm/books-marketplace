package bourse.agent;

import java.io.IOException;

import bourse.agent.sdd.Action;
import bourse.agent.sdd.Enchere;
import bourse.agent.sdd.Etat;
import bourse.agent.sdd.ListePdm;
import bourse.agent.sdd.ListeProgramme;
import bourse.protocole.Categorie;
import bourse.protocole.Erreur;
import bourse.protocole.Programme;
import bourse.protocole.PropositionEnchereP;
import bourse.protocole.Protocole;
import bourse.protocole.ResultAgents;
import bourse.protocole.ResultBye;
import bourse.protocole.ResultProposeVente;
import bourse.protocole.ResultWelcome;
import bourse.protocole.Resultat;

/* Gère la communication vers la Pdm : lance une connexion. */
public final class ConnexionPdm extends bourse.reseau.ManagerConnexion {
    /** Une ConnexionPdm doit connaître l'agent qui le contient. */
    private Agent papa;
    /** Crée une nouvelle instance de ConnexionPdm */
    public ConnexionPdm(Agent pere, boolean verbose) throws IOException, java.util.MissingResourceException {
        super(new java.net.Socket(pere.getCurrentPdm().getAdresse().ipToString(), pere.getCurrentPdm().getAdresse().getPort()), Protocole.MOTIF_FIN_FICHIER_XML, verbose);
        this.papa = pere;
    }
    /** Exécutée lorsque l'agent se connecte à une pdm. */
    public void run() {
        if (getVerbose()) System.out.println("Démarrage d'une connexion " + this);
        super.run();
    }
    /** Gestion/Traitement des messages reçus. */
    protected synchronized void traiter(String message) {
        int etatEntrant = this.papa.getEtat();
        // si le message n'est pas initialisé, c'est que la pdm a quitté "brutalement".
        try {
            if (message == null) { throw new java.lang.NullPointerException("Perte de la connexion."); }
            else {
                Protocole msg = Protocole.newInstance(message);
                switch (msg.getType()) {
                    case RESULTWELCOME :
                        System.out.println("Transition : recu un message welcome agent");
                        this.papa.getFenetre().addInputMessage("welcome agent");
                        if (this.papa.getEtat() == Etat.attenteRESULTWELCOME) {
                            this.papa.setEtatSuivant(Etat.pret);
                            /** On vient d'être accepté chez une pdm, on place
                             *  son compteur d'action réalisées à 0. */
                            this.papa.getEnvironnement().setNombreActions(0);
                            this.papa.getCurrentPdm().setVisitee(true);
                            this.papa.setHote(this.papa.getCurrentPdm().getNom());
                            this.papa.setWallet(((ResultWelcome)msg).getSolde());
                            this.papa.setCategorie(new Categorie(((ResultWelcome)msg).getCategorie()));
                            this.papa.synchroniser();
                        } else {
                            String export = new bourse.protocole.Erreur("Inattendu", "désolé, je ne m'attendais pas à une confirmation de connexion de votre part...").toXML();
                            try {
                                this.ecrire(export);
                                this.papa.getFenetre().addOutputMessage("erreur (inattendu)");
                            } catch (IOException e) { e.printStackTrace(System.err); }
                        }
                        break;
                    case RESULTBYE :
                        System.out.println("Transition : Recu un message result bye.");
                        this.papa.getFenetre().addInputMessage("result bye");
                        if (this.papa.getEtat() == 7) {
                            this.papa.setEtatSuivant(2);
                            this.papa.getCurrentPdm().setVisitee(true); // mise à jour de la pdm qu'on vient de quitter.
                            this.papa.getMemoire().getPdms().miseAJour(new ListePdm((ResultBye)msg));
                            this.papa.synchroniser();
                        }
                        else {
                            String export = new bourse.protocole.Erreur("Inattendu", "désolé, je ne m'attendais pas à une confirmation de connexion de votre part...", "", "").toXML();
                            try {
                                this.ecrire(export);
                                this.papa.getFenetre().addOutputMessage("erreur (inattendu)");
                            } catch (IOException e) { e.printStackTrace(System.err); }
                        }
                        break;
                    case ERREUR :
                        System.out.println("Transition : Type de l'erreur : " + ((Erreur)msg).getNom());
                        this.papa.getFenetre().addInputMessage("erreur " + ((Erreur)msg).getNom());
                        if (this.papa.getEtat() == 5) {
                            if ((((Erreur)msg).getNom()).equalsIgnoreCase("Duplication")) {
                                this.papa.setEtatSuivant(Etat.connaitPdms);
                                this.papa.getCurrentPdm().setActive(false);
                                this.papa.synchroniser();
                            }
                        } else if (this.papa.getEtat() == 7) {
                            if ((((Erreur)msg).getNom()).equalsIgnoreCase("Bloque")) {
                                this.papa.setEtatSuivant(Etat.pret);
                                this.papa.synchroniser();
                            }
                        } else if (this.papa.getEtat() == 10) {
                            if ((((Erreur)msg).getNom()).equals("Zerovente")) {
                                if ((((Erreur)msg).getRaison()).equals("neutre")){
                                    System.out.println("Vente refusée car la pdm est neutre.");
                                    this.papa.getCurrentPdm().setActive(false);
                                    this.papa.setEtatSuivant(6);
                                }
                                else if ((((Erreur)msg).getRaison()).equals("nonValide")){
                                    System.out.println("Vente refusée par la pdm.");
                                    this.papa.setEtatSuivant(6);
                                }
                                else if ((((Erreur)msg).getRaison()).equals("nonImplémenté")){
                                    System.out.println("Vente refusée car la pdm n'implémente pas ce type d'enchère.");
                                    this.papa.setEtatSuivant(6);
                                    this.papa.getCurrentPdm().setNonEnchereGeree(this.papa.getEnvironnement().getTypeDemande());
                                }
                                else if ((((Erreur)msg).getRaison()).equals("aute vendeur")){
                                    System.out.println("Vente refusée car la pdm a déjà acceptée une autre vente pour le tour suivant.");
                                    this.papa.setEtatSuivant(6);
                                }
                            }
                        }
                        break;
                    case RESULTAGENTS :
                        this.papa.getFenetre().addInputMessage("result agent");
                        System.out.println("Transition : Recu un message result Agents.");
                        if (new Etat(etatEntrant).acceptAsynchronus()) {
                            this.papa.getMemoire().getAgents().miseAJour(((ResultAgents)msg).getListeAgents()); // mise à jour de la liste d'agent.
                        }
                        break;
                    case PROPOSITIONENCHEREP :
                        System.out.println("Transition : Recu un message proposition enchere de la pdm.");
                        this.papa.getFenetre().addInputMessage("proposition enchere");
                        PropositionEnchereP m = (PropositionEnchereP)msg;
                        this.papa.getEnvironnement().setCourante(new Enchere(m.getNumeroEnchere(), m.getLivre(), m.getValeurEnchere(), m.getTemps(), m.getPas(), Enchere.enchereToCode(m.getNom()), m.getAgent()));
                        this.papa.getMemoire().getPdms().acceder(this.papa.getCurrentPdm().getNom()).setNumeroDernierTour(((PropositionEnchereP)msg).getNumeroEnchere());
                        /** mettre à jour la moyenne */
                        this.papa.getMemoire().refreshTemps();
                        this.papa.getMemoire().getPdms().refresh();
                        if (papa.getEnvironnement().getNombreActions() > 1) { // Dans le cas contraire, on ignore la proposition.
                            if (new Etat(etatEntrant).acceptAsynchronus()) {
                                /** exceptions parmi les états connéctés. */
                                if (this.papa.getEtat() == 7)
                                    this.papa.setEtatSuivant(7);
                                else if (this.papa.getEtat() ==  10)
                                    this.papa.setEtatSuivant(10);
                                else if (this.papa.getEtat() == 9)
                                    this.papa.setEtatSuivant(12);
                                else if (this.papa.getEtat() == 6) {
                                    this.papa.setEtatSuivant(12);
                                    this.papa.synchroniser();
                                }
                                else if (this.papa.getEtat() == 11) {
                                    if (this.papa.getEnvironnement().getCourante().getLivre().getProprietaire().equalsIgnoreCase(this.papa.getNom())) { // on est bien l'initiateur de la vente.
                                        this.papa.setEtatSuivant(13);
                                        this.papa.synchroniser();
                                    } else // c'est une autre enchère qui débute.
                                        this.papa.setEtatSuivant(11);
                                } else // si entre temps, on a déjà choisi ces actions, on a pas le droit de rentrer en enchère.
                                    if (this.papa.getAction() != Action.migrer && this.papa.getAction() != Action.bilan)
                                        this.papa.setEtatSuivant(12); /** comportement par défaut pour les états connéctés. */
                            }
                        }   
                        break;
                    case RESULTAT :
                        System.out.println("Transition : Recu un message de résutat d'enchère.");
                        this.papa.getFenetre().addInputMessage("resultat");
                        if (new Etat(etatEntrant).acceptAsynchronus()) {
                            if (this.papa.getEtat() == 13)  {
                                this.papa.setEtatSuivant(6);
                                this.papa.synchroniser();
                            }
                            /** Si on est l'acheteur d'un bouquin. */
                            if (((Resultat)msg).getAcheteur().equalsIgnoreCase(this.papa.getNom())) {
                                /** on met à jour le solde de l'agent. */
                                float f = this.papa.getWallet() - ((Resultat)msg).getEnchere();
                                this.papa.setWallet(f);
                            }
                            /** Si on est l'ancien propriétaire du bouquin. */
                            if (((Resultat)msg).getLivre().getProprietaire().equalsIgnoreCase(this.papa.getNom())) {
                                /** Si on est pas l'acheteur du bouquin. */
                                if (!(((Resultat)msg).getAcheteur().equalsIgnoreCase(this.papa.getNom()))) {
                                    /** On vient de vendre son bouquin, donc il faut mettre à jour son solde. */
                                    float f = this.papa.getWallet() + ((Resultat)msg).getEnchere();
                                    this.papa.setWallet(f);
                                }
                            }
                            bourse.sdd.Livre l = ((Resultat)msg).getLivre();
                            l.setPrixAchat(((Resultat)msg).getEnchere());
                            l.setProprietaire(((Resultat)msg).getAcheteur());
                            this.papa.getMemoire().getPossessions().ajouter(l);
                            this.papa.showResults();
                        }
                        break;
                    case PROGRAMME :
                        System.out.println("Transition : Recu un message programme.");
                        this.papa.getFenetre().addInputMessage("programme");
                        if (new Etat(etatEntrant).acceptAsynchronus()) {
                            this.papa.getCurrentPdm().setProgramme(new ListeProgramme(((Programme)msg).getListeProgramme()));
                        }
                        break;
                    case RESULTPROPOSEVENTE :
                        this.papa.getFenetre().addInputMessage("result propose vente");
                        System.out.println("Transition : result propose vente");
                        ResultProposeVente rpv = (ResultProposeVente)msg;
                        if (this.papa.getEtat() == 10) {
                            papa.getMemoire().getPossessions().get(rpv.getId()).setInstanceDeVente(true);
                            this.papa.setEtatSuivant(11);
                            this.papa.synchroniser();
                        }
                        break;
                    /** Messages non gérés par l'agent. */
                    default:
                        this.papa.getFenetre().addInputMessage("Transition : NON GERE");
                        System.out.println("Message non géré : " + msg.getType());
                        String export = new bourse.protocole.Erreur("Inattendu", "désolé, en tant qu'agent, je ne gère pas cette requête.").toXML();
                        try {
                            this.ecrire(export);
                            this.papa.getFenetre().addOutputMessage("erreur (inattendu)");
                        } catch (IOException e) { e.printStackTrace(System.err); }
                        break;
                }
            }
        } catch (java.lang.NullPointerException e) { e.printStackTrace(System.err); this.papa.setEtatSuivant(Etat.connaitPdms); }
    }
    
    /** ne jamais appeller de l'extérieur.  */
    public void close() {
    }
    
}
