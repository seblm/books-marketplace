package bourse.placeDeMarche;

import java.io.IOException;
import java.net.Socket;

import bourse.placeDeMarche.enchere.Enchere;
import bourse.placeDeMarche.enchere.EnchereReponseBoucle;
import bourse.placeDeMarche.enchere.EnchereReponseMultiple;
import bourse.placeDeMarche.enchere.EnchereReponseUnique;
import bourse.protocole.Admin;
import bourse.protocole.Categorie;
import bourse.protocole.Erreur;
import bourse.protocole.Programme;
import bourse.protocole.ProposeVente;
import bourse.protocole.PropositionEnchereA;
import bourse.protocole.Protocole;
import bourse.protocole.ResultAgents;
import bourse.protocole.ResultBye;
import bourse.protocole.ResultProposeVente;
import bourse.protocole.ResultWelcome;
import bourse.protocole.TypeMessage;
import bourse.protocole.Welcome;
import bourse.sdd.Livre;
import bourse.sdd.ProgrammePro;

/** Gère une connexion de la place de marché vers l'agent. */
public class ConnexionAgent extends bourse.reseau.ManagerConnexion {
    
    private Agent agent;
    private PlaceDeMarche pdm;
    private int port;
    
    public Agent getAgent() { return this.agent; }
    public int getPort() { return this.port; }
    public ConnexionAgent(Socket socket, PlaceDeMarche pdm, boolean verbose) throws IOException, java.util.MissingResourceException {
        super(socket, Protocole.MOTIF_FIN_FICHIER_XML, verbose);
        if (pdm != null)
            this.pdm = pdm;
        else
            throw new java.util.MissingResourceException("Le thread de connexion à un agent doit absolument connaître sa place de marché.", this.getClass().getName(), "pdm");
        this.agent = new Agent(socket.getPort());
        this.port = socket.getPort();
    }
    public void run() {
        if (pdm.getVerbose()) System.out.println("Démarrage d'une connexion vers l'agent localisé à " + this.getHostAddress() + ":" + agent.getPort());
        super.run(); // Commence à écouter les transmissions.
    }
    public String toString() { return this.agent.getNomAgent(); }
    public String toHtml() { return agent.toHtml(); }
    protected void traiter(String message) {
        if (message == null) { // La connexion a été coupée par le destinataire.
            if (pdm.getSalleDesVentes().estIdentifie(this.agent))
                pdm.getSalleDesVentes().supprimerAgent(this);
            this.agent = null;
            this.pdm = null;
        } else {
            Protocole msg = Protocole.newInstance(message);
            if (pdm.getVerbose()) System.out.println(" IN " + msg.getType().toString());
            Protocole reponse = null;
            Protocole reponse2 = null;
            switch (msg.getType().getValue()) {
                case TypeMessage.TM_WELCOME :
                    // Instancie le bon type de message.
                    Welcome m = (Welcome)msg;
                    // Crée un agent contenant le nom venant du message et le port venant de la connexion
                    bourse.placeDeMarche.Agent nouvelAgent = new bourse.placeDeMarche.Agent(port);
                    nouvelAgent.setNom(m.getNom());
                    if (pdm.getSalleDesVentes().estIdentifie(nouvelAgent.getNomAgent())) { // La salle des ventes contient un agent du même nom : l'agent envoie un WELCOME étrange...
                        if (pdm.getSalleDesVentes().estIdentifie(nouvelAgent)) { // L'agent a renvoyé un WELCOME alors qu'il était déjà identifié.
                            reponse = new ResultWelcome(agent.getArgent(), agent.getCategorie());
                        } else { // L'agent a envoyé un WELCOME sur une nouvelle connexion alors qu'il était toujours connu de la place de marché.
                            // On supprime la précédente connexion
                            ConnexionAgent ancienAgent = pdm.getSalleDesVentes().getConnexionAgent(nouvelAgent.getNomAgent());
                            agent = new bourse.placeDeMarche.Agent(ancienAgent.getAgent());
                            agent.setPort(this.port);
                            pdm.getSalleDesVentes().identifierAgent(this);
                            ancienAgent.deconnecter();
                            reponse = new ResultWelcome(agent.getArgent(), agent.getCategorie());
                        }
                    } else { // L'agent a envoyé un WELCOME et la place de marché ne connaît pas ce nom d'agent.
                        // Vérification de l'existence de l'agent dans la base de données.
                        if (pdm.getRequetes().agentPresentDansBaseDeDonnees(agent)) { // L'agent est présent dans la base de données et ses informations sont actualisées.
                            // Vérification de la non-réplication de l'agent sur d'autres places de marché.
                            if (agent.getNomPDM().equalsIgnoreCase("HOME")) { // L'agent n'est connecté à aucune place de marché, on peut l'accepter.
                                agent.setNomPDM(pdm.getNom());
                                pdm.getRequetes().inscrireAgent(agent);
                                pdm.getSalleDesVentes().identifierAgent(this);
                                reponse = new ResultWelcome(agent.getArgent(), agent.getCategorie());
                                if (pdm.getCommissairePriseur().getEnchereCourante() != null) // Le commissaire priseur a lancé une enchère, nous allons donc lui réannoncer l'enchère.
                                    reponse2 = pdm.getCommissairePriseur().getEnchereCourante().reAnnonce();
                            } else // L'agent est déjà connecté sur une autre place de marché
                                reponse = new Erreur("Duplication", "Désolé, vous êtes déjà connecté sur une autre place de marché.", this.agent.getNomPDM(), pdm.getRequetes().getAdressePdm(this.agent.getNomPDM()));
                        } else { // C'est visiblement la première fois que l'agent se connecte puisqu'il n'est pas présent dans la base de données.
                            agent.setNom(m.getNom());
                            agent.setNomPDM(pdm.getNom());
                            agent.setCategorie(new Categorie());
                            agent.setArgent(pdm.getRequetes().soldeDeDepart());
                            pdm.getRequetes().inscrireAgent(agent);
                            pdm.getSalleDesVentes().identifierAgent(this);
                            reponse = new ResultWelcome(agent.getArgent(), agent.getCategorie());
                            if (pdm.getCommissairePriseur().getEnchereCourante() != null) // Le commissaire priseur a lancé une enchère, nous allons donc lui réannoncer l'enchère.
                                reponse2 = pdm.getCommissairePriseur().getEnchereCourante().reAnnonce();
                        }
                    }
                    break;
                case TypeMessage.TM_BYE :
                    if (agent.getBloque()) // L'agent est bloqué par la place de marché : il ne peut pas sortir.
                        reponse = new Erreur("Bloque", "Vous êtes bloqué chez moi, niark niark !");
                    else { // On autorise l'agent à partir.
                        pdm.getRequetes().desinscrireAgent(this.getAgent());
                        pdm.getSalleDesVentes().supprimerAgent(this);
                        reponse = new ResultBye(this.pdm.getRequetes().getAdressesPdm());
                    }
                    break;
                case TypeMessage.TM_REQUETE_PROGRAMME :
                    reponse = new Programme(this.pdm.getCommissairePriseur().getProgramme().getListeProgramme());
                    break;
                case TypeMessage.TM_PROPOSE_VENTE :
                    ProposeVente proposeVente = (ProposeVente)msg;
                    Livre livreAgent = pdm.getRequetes().getLivre(proposeVente.getId());
                    if (livreAgent != null) // L'item correspond à un livre.
                        if ((proposeVente.getPrix() > 0.0 && (proposeVente.getNom() == 3 || proposeVente.getNom() == 4)) && livreAgent.getProprietaire().equalsIgnoreCase(agent.getNomAgent())) // La mise à prix n'est pas nulle
                            if (pdm.getCommissairePriseur().getEnchereCourante().getLivre().getId() != proposeVente.getId() && pdm.getCommissairePriseur().getProgramme().insertionPossible(pdm.getNom(), proposeVente.getId())) {
                                reponse = new ResultProposeVente(proposeVente.getId());
                                pdm.getRequetes().supprimerDerniereEnchere(((ProgrammePro)pdm.getCommissairePriseur().getProgramme().getListeProgramme().getLast()).getLivre());
                                pdm.getCommissairePriseur().getProgramme().ajouterVente(livreAgent, pdm.getNom(), proposeVente.getNom(), proposeVente.getPrix());
                            } else
                                reponse = new Erreur("Zerovente", "Désolé mais il n'y a vraiment plus de place dans le programme (pourtant, notre pdm gère l'insertion d'enchère sur tout le programme !).", "autreVendeur");
                        else
                            reponse = new Erreur("Zerovente", "Vous avez proposé un prix négatif ou le livre ne vous appartient pas petit coquin.", "nonValide");
                    else
                        reponse = new Erreur("Zerovente", "L'id du livre est incorrect : tu pourrais faire plus attention !", "nonValide");
                    break;
                case TypeMessage.TM_PROPOSITION_ENCHERE_A : if (pdm.getCommissairePriseur().getEnchereCourante() != null) {// Si non, l'agent a envoyé trop tard sa proposition...
                    int typeEnchereCourante = pdm.getCommissairePriseur().getEnchereCourante().getTypeEnchere();
                    Enchere enchereCourante = pdm.getCommissairePriseur().getEnchereCourante();
                    switch (typeEnchereCourante) {
                        case Enchere.ENCHERE_TROIS :
                            if (((PropositionEnchereA)msg).getNumero() == enchereCourante.getNumEnchere() // Le numéro de l'enchère est correct
                                 && ((PropositionEnchereA)msg).getEnchere() <= this.agent.getArgent()     // L'agent a assez d'argent pour enchérir
                                 && ((EnchereReponseMultiple)enchereCourante).getPrixCourant()+enchereCourante.getPas()<=((PropositionEnchereA)msg).getEnchere() // L'agent a proposé suffisamment
                                 && !enchereCourante.getVendeur().equals(this.agent.getNomAgent())        // L'agent n'est pas le vendeur
                            ) {
                                // On a besoin de réveiller le commissaire priseur.
                                pdm.getCommissairePriseur().vousAvezUnMessage((PropositionEnchereA)msg,this);
                                agent.setBloque(true);
                            }
                            break;
                        case Enchere.ENCHERE_QUATRE :
                            if (((PropositionEnchereA)msg).getNumero() == enchereCourante.getNumEnchere() // Le numéro de l'enchère est correct
                                 && ((EnchereReponseBoucle)enchereCourante).getPrixCourant() <= this.agent.getArgent() // L'agent a assez d'argent pour acquérir le livre.
                                 && !enchereCourante.getVendeur().equals(this.agent.getNomAgent())        // L'agent n'est pas le vendeur
                            ) {
                                // On a besoin de réveiller le commissaire priseur.
                                pdm.getCommissairePriseur().vousAvezUnMessage((PropositionEnchereA)msg,this);
                                agent.setBloque(true);
                            }
                            break;
                        case Enchere.ENCHERE_UN :
                            if (((PropositionEnchereA)msg).getNumero() == enchereCourante.getNumEnchere() // Le numéro de l'enchère est correct
                                 && enchereCourante.getPrixCourant() <= this.agent.getArgent()            // L'agent a assez d'argent pour enchérir
                                 && !enchereCourante.getVendeur().equals(this.agent.getNomAgent())        // L'agent n'est pas le vendeur
                            ) {
                                ((EnchereReponseUnique)(pdm.getCommissairePriseur().getEnchereCourante())).actualiser((PropositionEnchereA)msg, agent.getNomAgent());
                                agent.setBloque(true);
                            }
                        default : // Il reste le type Vickrey et Plis scellé
                            if (((PropositionEnchereA)msg).getNumero() == enchereCourante.getNumEnchere() // Le numéro de l'enchère est correct
                                 && ((PropositionEnchereA)msg).getEnchere() <= this.agent.getArgent()      // L'agent a assez d'argent pour enchérir
                                 && ((PropositionEnchereA)msg).getEnchere() > 0                           // L'agent a proposé un montant cohérent
                                 && !enchereCourante.getVendeur().equals(this.agent.getNomAgent())        // L'agent n'est pas le vendeur
                            ) {

                            ((EnchereReponseUnique)(pdm.getCommissairePriseur().getEnchereCourante())).actualiser((PropositionEnchereA)msg, agent.getNomAgent());
                            agent.setBloque(true);
                            }
                    }
                }
                break;
                case TypeMessage.TM_REQUETE_AGENTS :
                    reponse = new ResultAgents(pdm.getSalleDesVentes().agentsToListeDeNoms());
                    break;
                case TypeMessage.TM_ADMIN :
                    switch (((Admin)msg).getTypeRequete()) {
                        case Admin.ADMIN_DOC :
                            try {
                                ecrire(pdm.toHtml());
                            } catch (java.io.IOException e) { e.printStackTrace(System.err); }
                            break;
                        case Admin.ADMIN_TERMINER :
                            this.pdm.terminer();
                    }
                    deconnecter();
                    break;
            }
            if (reponse != null) { // Le message reçu nécessite une réponse immédiate de la pdm (car il n'est pas vide).
                String xml = reponse.toXML(reponse.toDOM());
                try {
                    this.ecrire(xml);
                    if (pdm.getVerbose()) System.out.println("OUT " + reponse.getType().toString());
                } catch (java.io.IOException e) { if (pdm.getVerbose()) System.err.println(e); }
                if (reponse2 != null) { // Le message reçu nécessite une seconde réponse immédiate (typiquement un broadcast d'enchère courante).
                    xml = reponse2.toXML(reponse2.toDOM());
                    try {
                        this.ecrire(xml);
                        if (pdm.getVerbose()) System.out.println("OUT " + reponse2.getType().toString());
                    } catch (java.io.IOException e) { if (pdm.getVerbose()) System.err.println(e); }
                }
            }
            if (!pdm.getSalleDesVentes().estIdentifie(agent.getNomAgent())) // L'agent n'est vraiment plus présent dans la salle des ventes.
                pdm.getRequetes().desinscrireAgent(agent);
        }
    }
    public boolean possede(Livre livre) { return this.pdm.getRequetes().agentPossede(this.getAgent(), livre); }
    /** Appelé par ThreadLecture lorsque la connexion est terminée. */
    public void deconnecter() {
        System.out.println("Fermeture de la connexion vers l'agent localisé à " + this.getHostAddress());
        super.deconnecter();
        pdm.getSalleDesVentes().supprimerAgent(this);
    }
}