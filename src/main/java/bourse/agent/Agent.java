package bourse.agent;

import static bourse.agent.sdd.Action.aucune;
import static bourse.agent.sdd.Action.bilan;
import static bourse.agent.sdd.Etat.actionChoisie;
import static bourse.agent.sdd.Etat.attenteDeclenchementEnchere;
import static bourse.agent.sdd.Etat.attentePropositionEnchere;
import static bourse.agent.sdd.Etat.attenteRESULTATdeSaVente;
import static bourse.agent.sdd.Etat.attenteRESULTBYE;
import static bourse.agent.sdd.Etat.attenteRESULTWELCOME;
import static bourse.agent.sdd.Etat.connaitPdms;
import static bourse.agent.sdd.Etat.connectePhysiquement;
import static bourse.agent.sdd.Etat.enchereDeuxOuCinq;
import static bourse.agent.sdd.Etat.enchereInteressante;
import static bourse.agent.sdd.Etat.enchereTrois;
import static bourse.agent.sdd.Etat.enchereUnOuQuatre;
import static bourse.agent.sdd.Etat.initial;
import static bourse.agent.sdd.Etat.modeEnchere;
import static bourse.agent.sdd.Etat.nonConnecte;
import static bourse.agent.sdd.Etat.pdmChoisie;
import static bourse.agent.sdd.Etat.pret;
import static bourse.agent.sdd.Etat.quitter;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.sql.ResultSet;
import java.util.Random;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import bourse.agent.ia.Aleatoire;
import bourse.agent.ia.Decision;
import bourse.agent.ia.Humain;
import bourse.agent.ia.Ia;
import bourse.agent.sdd.Action;
import bourse.agent.sdd.Etat;
import bourse.agent.sdd.ListeLivre;
import bourse.agent.sdd.ListePdm;
import bourse.agent.sdd.ListeProgramme;
import bourse.agent.sdd.Pdm;
import bourse.agent.sdd.PdmMemoire;
import bourse.protocole.Categorie;
import bourse.reseau.Ip;

/**
 * Gère l'agent courtier.
 */
public class Agent {

    /**
     * Vrai pour un affichage complet, faux pour avoir aucun message en sortie standard.
     */
    private static boolean verbose;

    /**
     * Variables de l'action de l'agent.
     */
    private Action action;

    /**
     * Pour accéder à la liste des pdms actives à l'initialisation de l'agent.
     */
    private RequetesAgent bd;

    /**
     * Le numéro de la catégorie attribuée.
     */
    private Categorie categorie;

    /**
     * L'environnement actuel de l'agent.
     */
    private Environnement environnement;

    /**
     * Variables de l'état courant de l'agent.
     */
    private Etat etat;

    /**
     * Variable de l'état suivant de l'agent.
     */
    private Etat etatSuivant;

    /**
     * Le nom du groupe.
     */
    private static final String GROUPE = "Groupe-E";

    private static final int ERROR = -1;

    private static final int FIRST_TIME = -1;

    /**
     * Le nom de la pdm hôte.
     */
    private String hote;

    /**
     * La mémoire : ce qui est sûr.
     */
    private final Memoire memoire;

    /**
     * Le nom de l'Agent.
     */
    private String nom;

    /**
     * Le client pour communiquer avec la Pdm.
     */
    private ConnexionPdm pdmConnectee;

    /**
     * Le solde de son compte.
     */
    private float portefeuille;

    /**
     * Le centre de decision.
     * 
     * TODO should be final
     */
    private Decision decision;

    /**
     * Expérimentation
     */
    private Visualisation fenetre;

    /**
     * Constructeur d'agent par défaut.
     */
    public Agent(String nom) {
        this.fenetre = new Visualisation();
        setWallet(0);
        setNom(nom);
        setCategorie(new Categorie(Categorie.AUCUNE));
        memoire = new Memoire(this, fenetre);
        setEnvironnement(new Environnement());
        setEtat(Etat.initial);
        setAction(aucune);
        try {
            this.bd = new RequetesAgent(verbose);
        } catch (java.sql.SQLException e) {
            e.getLocalizedMessage();
            System.exit(ERROR);
        } catch (java.lang.ClassNotFoundException e) {
            e.getLocalizedMessage();
            System.exit(ERROR);
        } catch (java.lang.InstantiationException e) {
            e.getLocalizedMessage();
            System.exit(ERROR);
        } catch (java.lang.IllegalAccessException e) {
            e.getLocalizedMessage();
            System.exit(ERROR);
        }
    }

    /**
     * Renvoie l'etat de l'agent.
     */
    public int getEtat() {
        return this.etat.getEtat();
    }

    /**
     * Met à jour l'état de l'agent et ordonne à la boucle qu'il y a eu un changement et met à jour cette valeur dans
     * l'inteface..
     */
    public void setEtat(int e) {
        this.etat = new Etat(e);
        this.fenetre.setEtat(new Etat(e).toString(0));
    }

    /**
     * Modifie l'état suivant de l'agent.
     */
    public void setEtatSuivant(int etatSuivant) {
        this.etatSuivant = new Etat(etatSuivant);
    }

    /**
     * Renvoie la pdm à laquelle il est connectée et met à jour cette valeur dans l'inteface.
     */
    public PdmMemoire getCurrentPdm() {
        return this.memoire.getPdms().acceder(this.hote);
    }

    /**
     * Retourne la valeur du portefeuille.
     */
    public float getWallet() {
        return this.portefeuille;
    }

    /**
     * Met à jour la somme d'argent et met à jour cette valeur dans l'inteface.
     */
    public void setWallet(float somme) {
        this.portefeuille = somme;
        this.fenetre.setSolde(String.valueOf(somme));
    }

    /**
     * Retourne la categorie courante
     */
    public Categorie getCategorie() {
        return this.categorie;
    }

    /**
     * Met à jour la catégorie et met à jour cette valeur dans l'inteface.
     */
    public void setCategorie(Categorie categorie) {
        this.categorie = categorie;
        this.fenetre.setCategorie(categorie.toString());
    }

    /**
     * Renvoie l'action.
     */
    public Action getAction() {
        return this.action;
    }

    /**
     * Met à jour l'action et met à jour cette valeur dans l'inteface.
     */
    public void setAction(Action a) {
        this.action = a;
        this.fenetre.setAction(a.toString());
    }

    /**
     * Renvoie l'environnement courant.
     */
    public Environnement getEnvironnement() {
        return this.environnement;
    }

    /**
     * Modifie l'environnement courant et met à jour cette valeur dans l'inteface.
     */
    private void setEnvironnement(Environnement e) {
        this.environnement = e;
        this.fenetre.setEnvironnement(e.toString(0));
    }

    /**
     * Renvoie la mémoire actuelle.
     */
    public Memoire getMemoire() {
        return this.memoire;
    }

    /**
     * Modifie le nom de la pdm actuelle.
     */
    public void setHote(String nom) {
        this.hote = nom;
        this.fenetre.setHote(nom);
    }

    /**
     * Modifie le nom de l'agent.
     */
    private void setNom(String nom) {
        this.nom = Agent.GROUPE + "-" + nom;
        this.fenetre.setNom(this.nom);
    }

    /**
     * Renvoie le nom de l'agent sous la forme Groupe-E-xxx où xxx est le prénom de l'agent.
     */
    public String getNom() {
        return this.nom;
    }

    /**
     * Renvoie la fenêtre.
     */
    public Visualisation getFenetre() {
        return this.fenetre;
    }

    /**
     * Modifie le mode verbeux.
     */
    public void setVerbose(boolean verbose) {
        Agent.verbose = verbose;
    }

    public Decision getDecision() {
        return decision;
    }

    /**
     * Méthode d'affichage qui présente de facon lisible l'objet.
     */
    public String toString(int decalage) {
        String delta = "";
        for (int i = 0; i < decalage; i++)
            delta += " ";
        return delta + "Nom = " + nom + "\n" + delta + "Solde = " + portefeuille + "\n" + delta + "Decision = "
                + decision + "\n" + delta + "Catégorie = " + categorie.toString(0) + "\n" + delta + "Etat = "
                + etat.toString(0) + "\n" + delta + "Action = " + action.toString() + "\n" + delta + "Memoire =\n"
                + memoire.toString(decalage + 1) + "\n" + delta + "Environnement =\n"
                + environnement.toString(decalage + 1) + "\n" + delta + "Hote = " + this.hote;
    }

    /**
     * Cherche la liste des adresses des pdms depuis la bd.
     */
    private void getPdmsFromBd() {
        if (getEtat() == initial) {
            System.out.println("Transition : Récupération les pdms déclarées dans la bd.");
            try {
                ResultSet r = bd.getPdMs();
                ListePdm l = new ListePdm();
                while (r.next()) {
                    l.ajouter(new Pdm(r.getString("nom"), r.getString("adresse")));
                }
                memoire.getPdms().miseAJour(l);
            } catch (java.sql.SQLException e) {
                System.err.println(e.getLocalizedMessage());
                setEtat(quitter);
            }
            setEtat(connaitPdms);
        }
    }

    /**
     * Affiche les résultats de l'agent dans l'onglet.
     */
    public void showResults() { // cat, id, titre, points, argent
        if (new Etat(getEtat()).acceptAsynchronus()) {
            String output = "";
            float sommePoints = 0;
            try {
                java.sql.ResultSet r = bd.getResultPerBook(nom);
                if (r.next()) {
                    output = "Argent = " + r.getString("argent") + "\n";
                    output += "Titre = " + r.getString("titre") + " Id = " + r.getString("id") + " Points = "
                            + r.getFloat("points") + " Catégorie = " + r.getString("categorie") + "\n";
                    sommePoints += r.getFloat("points");
                }
                while (r.next()) {
                    output += "Titre = " + r.getString("titre") + " Id = " + r.getString("id") + " Points = "
                            + r.getFloat("points") + " Catégorie = " + r.getString("categorie") + "\n";
                    sommePoints += r.getFloat("points");
                }
                output += "Total des points = " + sommePoints;
                fenetre.setResultat(output);
            } catch (java.sql.SQLException e) {
                e.printStackTrace(System.err);
            }
            System.out.println(output);
        }
    }

    /**
     * Déconnexion physique.
     */
    private void deconnexionPhysique() {
        if (getEtat() == connaitPdms) {
            System.out.println("Transition : déconnexion physique.");
            this.showResults();
            pdmConnectee.deconnecter();
            try {
                bd.deconnexion();
            } catch (java.sql.SQLException e) {
                System.out.println("Impossible de se déconnecter de la bd.");
            }
            setEtat(nonConnecte); // état 8.
        }
    }

    /**
     * Connexion physique à la pdm.
     */
    private void connexionPhysique(String ip, int port) throws IOException {
        if (this.getEtat() == pdmChoisie) {
            if (this.pdmConnectee != null) {
                this.pdmConnectee.deconnecter();
            }
            System.out.println("Transition : connexion physique à : " + this.getCurrentPdm().getAdresse().toString());
            this.pdmConnectee = new ConnexionPdm(this, verbose);
            this.pdmConnectee.start();
            this.setEtat(connectePhysiquement);
        } else {
            System.err.println("Transition : echec de la connexion vers "
                    + this.getCurrentPdm().getAdresse().toString());
        }
    }

    /**
     * Connexion à la PdM via le protocole.
     */
    private void welcomePdm() {
        if (this.getEtat() == connectePhysiquement) {
            System.out.println("Transition : envoyer welcome.");
            String export = new bourse.protocole.Welcome(this.nom, "Coucou !").toXML();
            try {
                this.pdmConnectee.ecrire(export);
                this.fenetre.addOutputMessage("welcome");
            } catch (IOException e) {
                e.printStackTrace(System.err);
            }
            this.setEtat(attenteRESULTWELCOME);
        }
    }

    /**
     * Déconnexion à la PdM via le protocole.
     */
    private void byePdm() {
        if (this.getEtat() == pret) {
            System.out.println("Transition : envoyer bye.");
            String export = new bourse.protocole.Bye("Au revoir belle pdm !").toXML();
            try {
                this.pdmConnectee.ecrire(export);
                this.fenetre.addOutputMessage("bye");
            } catch (IOException e) {
                e.printStackTrace(System.err);
            }
            this.setEtat(attenteRESULTBYE);
        }
    }

    /**
     * Envoyer une proposition de vente.
     */
    private void proposeVente() {
        if (getEtat() == actionChoisie) {
            System.out.println("Transition : envoyer propose vente.");
            ListeLivre l = this.memoire.getPossessions().possede(this.nom);
            System.out.println("livres possédés par " + this.nom + ":" + l.toString(10));
            try {
                String export = this.decision.choixLivreAVendre(l).toXML();
                try {
                    this.pdmConnectee.ecrire(export);
                    this.fenetre.addOutputMessage("propose vente");
                } catch (IOException e) {
                    e.printStackTrace(System.err);
                }
            } catch (NullPointerException e) {
                // on n'a pas trouvé de livre.
            }
            if (getEtat() == actionChoisie)
                this.setEtat(attentePropositionEnchere);
        }
    }

    /**
     * Envoi d'une demande de programme.
     */
    private void demandeProgramme() {
        if (this.getEtat() == pret || getEtat() == actionChoisie) {
            System.out.println("Transition : envoi d'une requete programme.");
            String export = new bourse.protocole.RequeteProgramme().toXML();
            try {
                this.pdmConnectee.ecrire(export);
                this.fenetre.addOutputMessage("requete programme");
            } catch (IOException e) {
                e.printStackTrace(System.err);
            }
            // mise à jour de la date de téléchargement de la liste des agents connectés
            environnement.setDateListeProgramme();
            while (this.environnement.getNombreActions() <= 1)
                try {
                    this.wait(100);
                } catch (InterruptedException e) {
                }
        }
    }

    /**
     * Envoi d'une demande d'agents connectés.
     */
    private void demandeAdversaires() {
        if (this.getEtat() == pret || getEtat() == actionChoisie) {
            System.out.println("Transition : envoi d'une requete agents.");
            String export = new bourse.protocole.RequeteAgents().toXML();
            try {
                this.pdmConnectee.ecrire(export);
                this.fenetre.addOutputMessage("requete agents");
            } catch (IOException e) {
                e.printStackTrace(System.err);
            }
            // mise à jour de la date de téléchargement de la liste des agents connectés
            environnement.setDateListeAgent();
        }
    }

    /**
     * Fait un notifyAll à l'interieur de l'agent (évite au thread de l'appeller directement).
     */
    public synchronized void synchroniser() {
        this.notifyAll();
    }

    /**
     * Traitement de l'enchère à prendre ou à laisser.
     */
    private void enchereUnOuQuatre() {
        System.out.println("Transition : envoyer proposition enchère.");
        String export = new bourse.protocole.PropositionEnchereA(getEnvironnement().getCourante().getNumeroEnchere(),
                getEnvironnement().getCourante().getValeurEnchere()).toXML();
        try {
            this.pdmConnectee.ecrire(export);
            this.fenetre.addOutputMessage("proposition enchere");
        } catch (IOException e) {
            e.printStackTrace(System.err);
        }
        this.setEtat(attenteRESULTATdeSaVente);
    }

    private void enchereDeuxCinq() {
        System.out.println("Transition : envoyer proposition enchère.");
        float prix = decision.choixPrix();
        String export = new bourse.protocole.PropositionEnchereA(getEnvironnement().getCourante().getNumeroEnchere(),
                prix).toXML();
        try {
            this.pdmConnectee.ecrire(export);
            this.fenetre.addOutputMessage("proposition enchere");
        } catch (IOException e) {
            e.printStackTrace(System.err);
        }
        this.setEtat(attenteRESULTATdeSaVente);
    }

    private void enchereTrois() {
        System.out.println("Transition : envoyer proposition enchère.");
        float prix2 = decision.choixPrix();
        String export3 = new bourse.protocole.PropositionEnchereA(getEnvironnement().getCourante().getNumeroEnchere(),
                prix2).toXML();
        try {
            this.pdmConnectee.ecrire(export3);
            this.fenetre.addOutputMessage("proposition enchere");
        } catch (IOException e) {
            e.printStackTrace(System.err);
        }
        setEtat(pret);
    }

    /**
     * Méthode qui éxécute le code de l'agent.
     */
    private synchronized void run() {
        do {
            this.fenetre.setEnvironnement(getEnvironnement().toString(0));
            System.out.println("Etat       : " + new Etat(getEtat()).toString(0));
            setEtatSuivant(FIRST_TIME);
            switch (getEtat()) {
            case quitter:
                break;
            case initial:
                this.getPdmsFromBd();
                /** etat = 2 */
                break;
            case connaitPdms:
                if (action == bilan)
                    deconnexionPhysique();
                else if (memoire.getPdms().aucuneActive()) {
                    setEtat(initial); // état 1.
                    try {
                        wait(5000);
                    } catch (java.lang.InterruptedException e) {
                        e.printStackTrace(System.err);
                    }
                } else {
                    decision.choixPdm();
                    System.out.println("Transition : choix de la pdm.");
                }
                break;
            case pdmChoisie:
                try {
                    this.connexionPhysique(this.getCurrentPdm().getAdresse().ipToString(), this.getCurrentPdm()
                            .getAdresse().getPort()); // etat = 4
                } catch (java.net.ConnectException e) {
                    // echec de connexion : la pdm est injoignable donc inactive.
                    System.err.println(e.getLocalizedMessage());
                    this.getCurrentPdm().setActive(false);
                    this.setEtat(connaitPdms);
                } catch (java.io.IOException e) {
                    // echec : la pdm est injoignable donc inactive.
                    e.printStackTrace(System.err);
                    this.getCurrentPdm().setActive(false);
                    this.setEtat(connaitPdms);
                }
                break;
            case connectePhysiquement:
                this.welcomePdm();
                break;
            case attenteRESULTWELCOME:
                try {
                    this.wait(decision.timeout());
                    if (getEtat() == attenteRESULTWELCOME) {
                        setEtat(connaitPdms);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace(System.err);
                    setEtat(pret);
                }
                break;
            case pret:
                decision.choixAction();
                // Dès qu'on fait une action, on doit incrémenter le compteur d'action courante.
                environnement.setNombreActions(environnement.getNombreActions() + 1);
                switch (action) {
                case bilan:
                case migrer:
                    byePdm();
                    break;
                case programme:
                    demandeProgramme();
                    break;
                case adversaires:
                    demandeAdversaires();
                    break;
                case attenteEnchere:
                    try {
                        this.wait(decision.timeout());
                    } catch (InterruptedException e) {
                        e.printStackTrace(System.err);
                        setEtat(pret);
                    }
                }
                break;
            case attenteRESULTBYE:
                try {
                    this.wait(decision.timeout());
                    setEtat(pret); // on n'a pas bougé détat alors on revient.
                } catch (InterruptedException e) {
                    e.printStackTrace(System.err);
                    setEtat(pret);
                }
                break;
            case nonConnecte:
                setEtat(quitter);
                break;
            case actionChoisie:
                if (this.decision.venteInteressante()) {
                    this.proposeVente();
                } else
                    setEtat(pret);
                break;
            case attentePropositionEnchere:
                try {
                    this.wait(decision.timeout());
                    if (getEtat() == attentePropositionEnchere) {
                        setEtat(pret); // on n'a pas bougé d'état alors on revient.
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace(System.err);
                    setEtat(pret);
                }
                break;
            case attenteDeclenchementEnchere:
                try {
                    this.wait(decision.timeout());
                    if (getEtat() == attenteDeclenchementEnchere) {
                        setEtat(pret); // on n'a pas bougé d'état alors on revient.
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace(System.err);
                    setEtat(pret);
                }
                break;
            case modeEnchere:
                System.out.println("\n");
                boolean interessant = decision.livreInteressant(environnement.getCourante().getLivre(), environnement
                        .getCourante().getType(), environnement.getCourante().getValeurEnchere());
                System.out.println("\n");
                if (interessant) {
                    this.setEtat(enchereInteressante);
                } else {
                    this.setEtat(actionChoisie);
                }
                break;
            case attenteRESULTATdeSaVente:
                try {
                    this.wait(decision.timeout());
                    setEtat(pret);
                } catch (InterruptedException e) {
                    e.printStackTrace(System.err);
                    setEtat(pret);
                }
                break;
            case enchereInteressante:
                int type = getEnvironnement().getCourante().getType();
                if (type == 1 || type == 4) {
                    setEtat(enchereUnOuQuatre);
                    System.out.println("Transition : Enchere 1 ou 4");
                } else if (type == 2 || type == 5) {
                    setEtat(enchereDeuxOuCinq);
                    System.out.println("Transition : Enchere 2 ou 5");
                } else if (type == 3) {
                    setEtat(enchereTrois);
                    System.out.println("Transition : Enchere 3");
                }
                break;
            case enchereUnOuQuatre:
                enchereUnOuQuatre();
                break;
            case enchereDeuxOuCinq:
                enchereDeuxCinq();
                break;
            case enchereTrois:
                enchereTrois();
                break;
            default:
                setEtat(quitter);
                break;
            }
            if (etatSuivant.getEtat() != FIRST_TIME) {
                setEtat(etatSuivant.getEtat());
            }
        } while (this.getEtat() != quitter);
    }

    /**
     * Programme principal.
     */
    public static void main(String arg[]) {
        // initialisation
        final Agent a = new Agent("");
        a.fenetre.setVisible(true);
        // on crée la fenêtre de dialogue parente à l'agent et bloquante (modale).
        final JDialog demarrage = new JDialog(a.fenetre, true);

        JPanel jPanelPrincipal = new JPanel();

        JPanel jPanelAgent = new JPanel();
        JLabel jLabelNomAgent = new JLabel();
        final JComboBox jComboBoxNomAgent = new JComboBox();
        JLabel jLabelControleAgent = new JLabel();
        final JComboBox jComboBoxControleAgent = new JComboBox();

        JPanel jPanelPdm = new JPanel();
        JLabel jLabelAdresseIpPdm = new JLabel();
        final JTextField jTextFieldAdresseIpPdm = new JTextField();
        JLabel jLabelPortPdm = new JLabel();
        final JTextField jTextFieldPortPdm = new JTextField();
        final JCheckBox jCheckBoxVerbose = new JCheckBox();

        JPanel jPanelValidation = new JPanel();
        JButton jButtonValider = new JButton();

        demarrage.getContentPane().setLayout(new FlowLayout());
        demarrage.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent evt) {
                System.exit(0);
            }
        });
        jPanelPrincipal.setLayout(new BorderLayout());
        jPanelAgent.setLayout(new GridLayout(3, 2));
        jLabelNomAgent.setText("Nom");
        jPanelAgent.add(jLabelNomAgent);
        final String[] defaultAgentNames = new String[] { "Arnaud", "Barney", "Corie", "Doris", "Eric", "Felicien",
                "Gatien", "Hortentia", "Icare", "Jerome", "Kholia", "Luther", "Mohamed", "Natacha", "Omer", "Petra",
                "Quincy", "Rebecca", "Sebastien", "Thadeus", "Ursule", "Vincenzo", "Whitney", "Xena", "Yseult",
                "Zdzislawa" };
        jComboBoxNomAgent.setModel(new DefaultComboBoxModel(defaultAgentNames));
        jComboBoxNomAgent.setSelectedIndex(new Random().nextInt(defaultAgentNames.length));
        jPanelAgent.add(jComboBoxNomAgent);
        jLabelControleAgent.setText("Contr\u00f4le");
        jPanelAgent.add(jLabelControleAgent);
        jComboBoxControleAgent.setModel(new DefaultComboBoxModel(new String[] { "Intelligence Artificielle", "Humain",
                "Aléatoire" }));
        jPanelAgent.add(jComboBoxControleAgent);
        jCheckBoxVerbose.setText("Affichage complet");
        jCheckBoxVerbose.setSelected(false);
        jPanelAgent.add(jCheckBoxVerbose);
        jPanelPrincipal.add(jPanelAgent, BorderLayout.NORTH);
        jPanelPdm.setLayout(new GridLayout(2, 2));
        jPanelPdm.setBorder(new TitledBorder("Pdm (facultatif)"));
        jLabelAdresseIpPdm.setText("Adresse Ip");
        jPanelPdm.add(jLabelAdresseIpPdm);
        jPanelPdm.add(jTextFieldAdresseIpPdm);
        jLabelPortPdm.setText("Port");
        jPanelPdm.add(jLabelPortPdm);
        jTextFieldPortPdm.setColumns(15);
        jPanelPdm.add(jTextFieldPortPdm);
        jPanelPrincipal.add(jPanelPdm, BorderLayout.CENTER);
        jButtonValider.setText("Lancer");
        // action à réaliser quand on clique sur le bouton lancer.
        jButtonValider.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                a.setNom((String) jComboBoxNomAgent.getSelectedItem());
                switch (jComboBoxControleAgent.getSelectedIndex()) {
                case 0:
                    a.decision = new Ia(a);
                    break;
                case 1:
                    a.decision = new Humain(a);
                    break;
                case 2:
                    a.decision = new Aleatoire(a);
                    break;
                }
                if (!jTextFieldAdresseIpPdm.getText().equals("") || !jTextFieldPortPdm.getText().equals("")) {
                    Ip adresse = new Ip(jTextFieldAdresseIpPdm.getText() + ":" + jTextFieldPortPdm.getText());
                    a.getMemoire()
                            .getPdms()
                            .ajouter(
                                    new PdmMemoire("Par défaut", adresse.toString(), false, true, new ListeProgramme(),
                                            0));
                    a.setEtat(connaitPdms);
                } else {
                    a.setEtat(initial);
                }
                a.setVerbose(jCheckBoxVerbose.isSelected());
                demarrage.dispose();
            }
        });
        jPanelValidation.add(jButtonValider);
        jPanelPrincipal.add(jPanelValidation, BorderLayout.SOUTH);
        demarrage.getContentPane().add(jPanelPrincipal);
        demarrage.pack();
        if (arg.length == 0) {
            // on affiche la fenêtre bloquante de choix du nom.
            demarrage.setVisible(true);
            // lorsque la fenêtre rend la main, (l'utilisateur a voulu démarrer l'agent), on lance l'agent.
        } else {
            // on initialise l'agent sans l'aide de l'utilisateur.
            a.setNom(arg[0]);
            if (arg.length >= 1) {
                a.decision = new Ia(a);
            } else {
                a.decision = new Aleatoire(a);
            }
            a.setEtat(initial);
            a.setVerbose(false);
        }
        a.run();
        System.out.println("C'est la fin.");
    }
}
