package bourse.agent;

import java.io.IOException;
import bourse.agent.sdd.*;
import bourse.agent.ia.*;
import bourse.protocole.*;

/** Gère l'agent courtier. */
public class Agent {
    
    /** Variables d'instances. */
    /** Vrai pour un affichage complet, faux pour avoir aucun message en sortie standard. */
    private static boolean verbose;
    /** Variables de l'action de l'agent. */
    private Action action;  
    /** Pour accéder à la liste des pdms actives à l'initialisation de l'agent. */
    private RequetesAgent bd;
    /** Le numéro de la catégorie attribuée. */
    private Categorie categorie;
    /** L'environnement actuel de l'agent. */
    private Environnement environnement;
    /** Variables de l'état courant de l'agent. */
    private Etat etat;
    /** Variable de l'état suivant de l'agent. */
    private Etat etatSuivant;
    /** Le nom du groupe. */
    private static final String groupe = "Groupe-E";
    /** Le nom de la pdm hôte. */
    private String hote;
    /** La mémoire : ce qui est sûr. */
    private Memoire memoire;
    /** Le nom de l'Agent. */
    private String nom;
    /** Le client pour communiquer avec la Pdm. */
    private ConnexionPdm pdmConnectee;
    /** Le solde de son compte. */
    private float portefeuille;
    /** Le centre de decision. */
    private Decision decision;
    /** Expérimentation */
    private Visualisation fenetre;
    /** Le serveur pour attendre les communications d'autres agents. */
    /** Le client pour contacter d'autres agents. */
    
    /** Constructeurs. */
    /** Constructeur d'agent par défaut. */
    public Agent(String nom) {
        this.fenetre = new Visualisation();
        setWallet(0);
        setNom(nom);
        setCategorie(new Categorie(Categorie.AUCUNE));
        Memoire m = new Memoire(this, fenetre);
        setMemoire(m);
        setEnvironnement(new Environnement());
        setEtat(Etat.initial);
        setAction(new Action(Action.aucune));
        try { this.bd = new RequetesAgent(verbose);
        } catch (java.sql.SQLException e) { e.getLocalizedMessage(); System.exit(-1);
        } catch (java.lang.ClassNotFoundException e) { e.getLocalizedMessage(); System.exit(-1);
        } catch (java.lang.InstantiationException e) { e.getLocalizedMessage(); System.exit(-1);
        } catch (java.lang.IllegalAccessException e) { e.getLocalizedMessage(); System.exit(-1); }
    }
    
    /** Assesseurs, modifieurs. */
    /** Renvoie l'etat de l'agent. */
    public int getEtat() { return this.etat.getEtat(); }
    /** Met à jour l'état de l'agent et ordonne à la boucle qu'il y a eu un changement et met à jour cette valeur dans l'inteface.. */
    public void setEtat(int e) { this.etat = new Etat(e); this.fenetre.setEtat(new Etat(e).toString(0)); }
    /** Renvoie l'état suivant de l'agent modifié par le thread de réception des messages. */
    public int getEtatSuivant() { return etatSuivant.getEtat(); }
    /** Modifie l'état suivant de l'agent. */
    public void setEtatSuivant(int _etatSuivant) { etatSuivant = new Etat(_etatSuivant); }
    /** Renvoie la pdm à laquelle il est connectée et met à jour cette valeur dans l'inteface. */
    public PdmMemoire getCurrentPdm() { return this.memoire.getPdms().acceder(this.hote); }
    /**Retourne la valeur du portefeuille. */
    public float getWallet(){return this.portefeuille;}
    /** Met à jour la somme d'argent et met à jour cette valeur dans l'inteface. */
    public void setWallet(float somme) { this.portefeuille = somme; this.fenetre.setSolde(String.valueOf(somme)); }
    /**retourne la categorie courante*/
    public Categorie getCategorie() { return this.categorie; }
    /** Met à jour la catégorie et met à jour cette valeur dans l'inteface. */
    public void setCategorie(Categorie categorie) { this.categorie = categorie; this.fenetre.setCategorie(categorie.toString()); }
    /** Renvoie l'action. */
    public int getAction() { return this.action.getAction(); }
    /** Met à jour l'action et met à jour cette valeur dans l'inteface. */
    public void setAction(Action a) { this.action = a; this.fenetre.setAction(a.toString(0)); }
    /** Renvoie l'environnement courant. */
    public Environnement getEnvironnement() { return this.environnement; }
    /** Modifie l'environnement courant et met à jour cette valeur dans l'inteface. */
    public void setEnvironnement(Environnement e) { this.environnement = e; this.fenetre.setEnvironnement(e.toString(0)); }
    /** Renvoie la mémoire actuelle. */
    public Memoire getMemoire() { return this.memoire; }
    /** Modifie la mémoire de l'agent. */
    public void setMemoire(Memoire m) { this.memoire = m; }
    /** Modifie le nom de la pdm actuelle. */
    public void setHote(String nom) { this.hote = nom; this.fenetre.setHote(nom); }
    /** Modifie le nom de l'agent. */
    public void setNom(String nom) { this.nom = Agent.groupe + "-" + nom; this.fenetre.setNom(this.nom); }
    /** Renvoie le nom de l'agent sous la forme Groupe-E-xxx où xxx est le prénom de l'agent. */
    public String getNom() { return this.nom; }
    /** Renvoie la fenêtre. */
    public Visualisation getFenetre() { return this.fenetre; }
    /** Modifie lengthmode verbeux. */
    public void setVerbose(boolean verbose) { Agent.verbose = verbose; }
    public void setDecision(bourse.agent.ia.Decision _decision) { this.decision = _decision; }
    public Decision getDecision() { return decision; }
        
    /** Méthodes. */
    /** Méthode d'affichage qui présente de facon lisible l'objet. */
    public String toString(int decalage) {
        String delta = "";
        for (int i=0; i<decalage; i++) delta += " ";
        return delta + "Nom = " + nom + "\n"
             + delta + "Solde = " + portefeuille + "\n"
             + delta + "Decision = " + decision + "\n"
             + delta + "Catégorie = " + categorie.toString(0) + "\n"
             + delta + "Etat = " + etat.toString(0)+ "\n"
             + delta + "Action = " + action.toString(0) + "\n"
             + delta + "Memoire =\n" + memoire.toString(decalage+1) + "\n"
             + delta + "Environnement =\n" + environnement.toString(decalage+1) + "\n"
             + delta + "Hote = " + this.hote;
    }
    /** Cherche la liste des adresses des pdms depuis la bd. */
    public void getPdmsFromBd() {
        if (getEtat() == Etat.initial) {
            System.out.println("Transition : Récupération les pdms déclarées dans la bd.");
            try {
                java.sql.ResultSet r = bd.getPdMs();
                ListePdm l = new ListePdm();
                while (r.next()) l.ajouter(new Pdm(r.getString("nom"), r.getString("adresse")));
                memoire.getPdms().miseAJour(l);
            } catch (java.sql.SQLException e) {
                System.err.println(e.getLocalizedMessage());
                setEtat(Etat.quitter);
            }
            setEtat(Etat.connaitPdms);
        }
    }
    /** Affiche les résultats de l'agent dans l'onglet. */
    public void showResults() { // cat, id, titre, points, argent
        if (new Etat(getEtat()).acceptAsynchronus()) {
            String output = "";
            float sommePoints = 0;
            try {
                java.sql.ResultSet r = bd.getResultPerBook(nom);
                if (r.next()) {
                    output = "Argent = " + r.getString("argent") + "\n";
                    output += "Titre = " + r.getString("titre")
                            + " Id = " + r.getString("id")
                            + " Points = " + r.getFloat("points")
                            + " Catégorie = " + r.getString("categorie") + "\n";
                    sommePoints += r.getFloat("points");
                }
                while (r.next()) {
                    output += "Titre = " + r.getString("titre")
                            + " Id = " + r.getString("id")
                            + " Points = " + r.getFloat("points")
                            + " Catégorie = " + r.getString("categorie") + "\n";
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
    /** Déconnexion physique. */
    public void deconnexionPhysique() {
        if (getEtat() == Etat.connaitPdms) {
            System.out.println("Transition : déconnexion physique.");
            this.showResults();
            pdmConnectee.deconnecter();
            try { bd.deconnexion(); } catch (java.sql.SQLException e) { System.out.println("Impossible de se déconnecter de la bd."); }
            setEtat(Etat.nonConnecte); // état 8.
        }
    }
    /** Connexion physique à la pdm. */
    public void connexionPhysique(String ip, int port) throws java.net.ConnectException, java.io.IOException {
        if (this.getEtat() == Etat.pdmChoisie) {
            if (this.pdmConnectee != null) this.pdmConnectee.deconnecter();
            System.out.println("Transition : connexion physique à : " + this.getCurrentPdm().getAdresse().toString());
            this.pdmConnectee = new ConnexionPdm(this, verbose);
            this.pdmConnectee.start();
            this.setEtat(Etat.connectePhysiquement);
        } else
            System.err.println("Transition : echec de la connexion vers " + this.getCurrentPdm().getAdresse().toString());
    }
    /** Connexion à la PdM via le protocole. */
    public void welcomePdm() {
        if (this.getEtat() == Etat.connectePhysiquement) {
            System.out.println("Transition : envoyer welcome.");
            String export = new bourse.protocole.Welcome(this.nom, "Coucou !").toXML();
            try {
                this.pdmConnectee.ecrire(export);
                this.fenetre.addOutputMessage("welcome");
            } catch (IOException e) { e.printStackTrace(System.err); }
            this.setEtat(Etat.attenteRESULTWELCOME);
        }
    }
    /** Déconnexion à la PdM via le protocole. */
    public void byePdm() {
        if (this.getEtat() == Etat.pret) {
            System.out.println("Transition : envoyer bye.");
            String export = new bourse.protocole.Bye("Au revoir belle pdm !").toXML();
            try {
                this.pdmConnectee.ecrire(export);
                this.fenetre.addOutputMessage("bye");
            } catch (IOException e) { e.printStackTrace(System.err); }
            this.setEtat(Etat.attenteRESULTBYE);
        }
    }
    /** Envoyer une proposition de vente. */
    public void proposeVente() {
        if (getEtat() == Etat.actionChoisie) {
            System.out.println("Transition : envoyer propose vente.");
            ListeLivre l = this.memoire.getPossessions().possede(this.nom);
            System.out.println("livres possédés par " + this.nom + ":" + l.toString(10));
            try {
                String export = this.decision.choixLivreAVendre(l).toXML();
                try {
                    this.pdmConnectee.ecrire(export);
                    this.fenetre.addOutputMessage("propose vente");
                } catch (IOException e) { e.printStackTrace(System.err); }
            } catch (java.lang.NullPointerException e) { /** on a pas trouvé de livre. */ }
            if (getEtat() == Etat.actionChoisie) this.setEtat(Etat.attentePropositionEnchere);;
        }
    }
    /** Envoi d'une demande de programme. */
    public void demandeProgramme() {
        if (this.getEtat() == Etat.pret || getEtat() == Etat.actionChoisie) {
            System.out.println("Transition : envoi d'une requete programme.");
            String export = new bourse.protocole.RequeteProgramme().toXML();
            try {
                this.pdmConnectee.ecrire(export);
                this.fenetre.addOutputMessage("requete programme");
            } catch (IOException e) { e.printStackTrace(System.err); }
            /** mise à jour de la date de téléchargement de la liste des agents
             *  connéctés */
            environnement.setDateListeProgramme();
            while (this.environnement.getNombreActions() <= 1)
                try { this.wait(100); } catch (InterruptedException e) { }
        }
    }
    /** Envoi d'une demande d'agents connectés. */
    public void demandeAdversaires() {
        if (this.getEtat() == Etat.pret || getEtat() == Etat.actionChoisie) {
            System.out.println("Transition : envoi d'une requete agents.");
            String export = new bourse.protocole.RequeteAgents().toXML();
            try {
                this.pdmConnectee.ecrire(export);
                this.fenetre.addOutputMessage("requete agents");
            } catch (IOException e) { e.printStackTrace(System.err); }
            /** mise à jour de la date de téléchargement de la liste des agents
             *  connéctés */
            environnement.setDateListeAgent();
        }
    }
    /** Fait un notifyAll à l'interieur de l'agent (évite au thread de l'appeller directement). */
    public synchronized void synchroniser() { this.notifyAll(); }
    /** Traitement de l'enchère à prendre ou à laisser. */
    public void enchereUnOuQuatre() {
        System.out.println("Transition : envoyer proposition enchère.");
        String export = new bourse.protocole.PropositionEnchereA(getEnvironnement().getCourante().getNumeroEnchere(), getEnvironnement().getCourante().getValeurEnchere()).toXML();
        try {
            this.pdmConnectee.ecrire(export);
            this.fenetre.addOutputMessage("proposition enchere");
        } catch (IOException e) { e.printStackTrace(System.err); }
        this.setEtat(Etat.attenteRESULTATdeSaVente);
    }
    public void enchereDeuxCinq() {
        System.out.println("Transition : envoyer proposition enchère.");
        float prix = decision.choixPrix();
        String export = new bourse.protocole.PropositionEnchereA(getEnvironnement().getCourante().getNumeroEnchere(), prix).toXML();
        try {
            this.pdmConnectee.ecrire(export);
            this.fenetre.addOutputMessage("proposition enchere");
        } catch (IOException e) { e.printStackTrace(System.err); }
        this.setEtat(Etat.attenteRESULTATdeSaVente);
    }
    public void enchereTrois() {
        System.out.println("Transition : envoyer proposition enchère.");
        float prix2 = decision.choixPrix();
        String export3 = new bourse.protocole.PropositionEnchereA(getEnvironnement().getCourante().getNumeroEnchere(), prix2).toXML();
        try {
            this.pdmConnectee.ecrire(export3);
            this.fenetre.addOutputMessage("proposition enchere");
        } catch (IOException e) { e.printStackTrace(System.err); }
        setEtat(Etat.pret);
    }
    /** Méthode qui éxécute le code de l'agent.*/
    public synchronized void run() {
        do {
            this.fenetre.setEnvironnement(getEnvironnement().toString(0));
            System.out.println("Etat       : " + new Etat(getEtat()).toString(0));
            setEtatSuivant(-1);
            switch (getEtat()) {
                case 0:
                    break;
                case 1:
                    this.getPdmsFromBd(); /** etat = 2 */
                    break;
                case 2:
                    if (getAction() == Action.bilan) deconnexionPhysique();
                    else if (memoire.getPdms().aucuneActive()) {
                        setEtat(Etat.initial); // état 1.
                        try { wait(5000); } catch(java.lang.InterruptedException e) { e.printStackTrace(System.err); }
                    } else /*if (getAction() == Action.aucune || getAction() == Action.migrer )*/ {
                        decision.choixPdm();
                        System.out.println("Transition : choix de la pdm.");
                    }
                    break;
                case 3:
                    try {
                        this.connexionPhysique(this.getCurrentPdm().getAdresse().ipToString(), this.getCurrentPdm().getAdresse().getPort()); // etat = 4
                    } catch (java.net.ConnectException e) {
                         // echec de connexion : la pdm est injoignable donc inactive.
                        System.err.println(e.getLocalizedMessage());
                        this.getCurrentPdm().setActive(false);
                        this.setEtat(Etat.connaitPdms);
                    } catch (java.io.IOException e) {
                        // echec : la pdm est injoignable donc inactive.
                        e.printStackTrace(System.err);
                        this.getCurrentPdm().setActive(false);
                        this.setEtat(Etat.connaitPdms);
                    }
                    break;
                case 4:
                    this.welcomePdm();
                    break;
                case 5:
                    try {
                        this.wait(decision.timeout());
                        if (getEtat() == 5) setEtat(Etat.connaitPdms);
                    } catch (InterruptedException e) {
                        e.printStackTrace(System.err);
                        setEtat(Etat.pret);
                    }
                    break;
                case 6:
                    decision.choixAction();
                    /** Dès qu'on fait une action, on doit incrémenter le compteur d'action courante. */
                    environnement.setNombreActions(environnement.getNombreActions() + 1);
                    if (this.getAction() == Action.aucune) { } // on ne fait rien
                    else if (this.getAction() == Action.bilan || this.getAction() == Action.migrer) byePdm();
                 // else if (this.getAction() == Action.vendre) this.proposeVente();
                    else if (this.getAction() == Action.programme) this.demandeProgramme();
                    else if (this.getAction() == Action.adversaires) this.demandeAdversaires();
                    else if (this.getAction() == Action.attenteEnchere)
                        try {
                            this.wait(decision.timeout());
                        } catch (InterruptedException e) {
                            e.printStackTrace(System.err);
                            setEtat(Etat.pret);
                        }
                    break;
                case 7:
                    try {
                        this.wait(decision.timeout());
                        setEtat(Etat.pret); // on a pas bougé détat alors on revient.
                    } catch (InterruptedException e) {
                        e.printStackTrace(System.err);
                        setEtat(Etat.pret);
                    }                   
                    break;
                case 8:
                    setEtat(Etat.quitter);
                    break;
                case 9:
                    /*
                    this.demandeAdversaires();
                    this.demandeProgramme();
                    */
                    if (this.decision.venteInteressante()) {
                        this.proposeVente();
                    } else
                        setEtat(Etat.pret);
                    break;
                case 10:
                    try {
                        this.wait(decision.timeout());
                        if (getEtat() == 10) setEtat(Etat.pret); // on a pas bougé d'état alors on revient.
                    } catch (InterruptedException e) {
                        e.printStackTrace(System.err);
                        setEtat(Etat.pret);
                    }
                    break;
                case 11:
                    try {
                        this.wait(decision.timeout());
                        if (getEtat() == 11) setEtat(Etat.pret); // on a pas bougé d'état alors on revient.
                    } catch (InterruptedException e) {
                        e.printStackTrace(System.err);
                        setEtat(Etat.pret);
                    }
                    break;
                case 12:
                    System.out.println("\n");
                    boolean interessant = decision.livreInteressant(environnement.getCourante().getLivre(), environnement.getCourante().getType(), environnement.getCourante().getValeurEnchere());
                    System.out.println("\n");
                    if (interessant)
                        this.setEtat(14);
                    else
                        this.setEtat(9);
                    break;
                case 13:
                    try {
                        this.wait(decision.timeout());
                        setEtat(Etat.pret);
                    } catch (InterruptedException e) {
                        e.printStackTrace(System.err);
                        setEtat(Etat.pret);
                    }
                    break;
                case 14:
                    int type = getEnvironnement().getCourante().getType();
                    if (type == 1 || type == 4) {
                        setEtat(Etat.enchereUnOuQuatre);
                        System.out.println("Transition : Enchere 1 ou 4");
                    } else if (type == 2 || type == 5) {
                        setEtat(Etat.enchereDeuxOuCinq);
                        System.out.println("Transition : Enchere 2 ou 5");
                    } else if (type == 3) {
                        setEtat(Etat.enchereTrois);
                        System.out.println("Transition : Enchere 3");
                    }
                    break;
                case 15:
                    enchereUnOuQuatre();                   
                    break;
                case 16:
                    enchereDeuxCinq();
                    break;
                case 17:
                    enchereTrois();
                    break;
                default:
                    setEtat(Etat.quitter);
                    break;
            }
            if (getEtatSuivant() != -1)
                setEtat(getEtatSuivant());
        } while (this.getEtat() != 0);
    }
    
    /** Programme principal. */
    public static void main(String arg[]) {
        /** initialisation */
        final bourse.agent.Agent a = new bourse.agent.Agent("");
        a.fenetre.setVisible(true);
        /** on crée la fenetre de dialogue parente à l'agent et bloquante (modale). */
        final javax.swing.JDialog demarrage = new javax.swing.JDialog(a.fenetre, true);
        
        javax.swing.JPanel jPanelPrincipal = new javax.swing.JPanel();
        
        javax.swing.JPanel jPanelAgent = new javax.swing.JPanel();
        javax.swing.JLabel jLabelNomAgent = new javax.swing.JLabel();
        final javax.swing.JComboBox jComboBoxNomAgent = new javax.swing.JComboBox();
        javax.swing.JLabel jLabelControleAgent = new javax.swing.JLabel();
        final javax.swing.JComboBox jComboBoxControleAgent = new javax.swing.JComboBox();
        
        javax.swing.JPanel jPanelPdm = new javax.swing.JPanel();
        javax.swing.JLabel jLabelAdresseIpPdm = new javax.swing.JLabel();
        final javax.swing.JTextField jTextFieldAdresseIpPdm = new javax.swing.JTextField();
        javax.swing.JLabel jLabelPortPdm = new javax.swing.JLabel();
        final javax.swing.JTextField jTextFieldPortPdm = new javax.swing.JTextField();
        final javax.swing.JCheckBox jCheckBoxVerbose = new javax.swing.JCheckBox();
        
        javax.swing.JPanel jPanelValidation = new javax.swing.JPanel();
        javax.swing.JButton jButtonValider = new javax.swing.JButton();
        
        demarrage.getContentPane().setLayout(new java.awt.FlowLayout());
        demarrage.addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                System.exit(0);
            }
        });
        jPanelPrincipal.setLayout(new java.awt.BorderLayout());
        jPanelAgent.setLayout(new java.awt.GridLayout(3, 2));
        jLabelNomAgent.setText("Nom");
        jPanelAgent.add(jLabelNomAgent);
        jComboBoxNomAgent.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Arnaud", "Barney", "Corie", "Doris", "Eric", "Felicien", "Gatien", "Hortentia", "Icare", "Jerome", "Kholia", "Luther", "Mohamed", "Natacha", "Omer", "Petra", "Quincy", "Rebecca", "Sebastien", "Thadeus", "Ursule", "Vincenzo", "Whitney", "Xena", "Yseult", "Zdzislawa" }));
        jComboBoxNomAgent.setSelectedIndex(new java.util.Random().nextInt(26));
        jPanelAgent.add(jComboBoxNomAgent);
        jLabelControleAgent.setText("Contr\u00f4le");
        jPanelAgent.add(jLabelControleAgent);
        jComboBoxControleAgent.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Intelligence Artificielle", "Humain", "Aléatoire" }));
        jPanelAgent.add(jComboBoxControleAgent);
        jCheckBoxVerbose.setText("Affichage complet");
        jCheckBoxVerbose.setSelected(false);
        jPanelAgent.add(jCheckBoxVerbose);
        jPanelPrincipal.add(jPanelAgent, java.awt.BorderLayout.NORTH);
        jPanelPdm.setLayout(new java.awt.GridLayout(2, 2));
        jPanelPdm.setBorder(new javax.swing.border.TitledBorder("Pdm (facultatif)"));
        jLabelAdresseIpPdm.setText("Adresse Ip");
        jPanelPdm.add(jLabelAdresseIpPdm);
        jPanelPdm.add(jTextFieldAdresseIpPdm);
        jLabelPortPdm.setText("Port");
        jPanelPdm.add(jLabelPortPdm);
        jTextFieldPortPdm.setColumns(15);
        jPanelPdm.add(jTextFieldPortPdm);
        jPanelPrincipal.add(jPanelPdm, java.awt.BorderLayout.CENTER);
        jButtonValider.setText("Lancer");
        /** action à réaliser quand on clique sur le bouton lancer. */
        jButtonValider.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                a.setNom((String)jComboBoxNomAgent.getSelectedItem());
                switch (jComboBoxControleAgent.getSelectedIndex()) {
                    case 0 : a.decision = new bourse.agent.ia.Ia(a);        break;
                    case 1 : a.decision = new bourse.agent.ia.Humain(a);    break;
                    case 2 : a.decision = new bourse.agent.ia.Aleatoire(a); break;
                }
                if (!jTextFieldAdresseIpPdm.getText().equals("") || !jTextFieldPortPdm.getText().equals("")) {
                    bourse.reseau.Ip adresse = new bourse.reseau.Ip(jTextFieldAdresseIpPdm.getText()+":"+jTextFieldPortPdm.getText());
                    a.getMemoire().getPdms().ajouter(new PdmMemoire("Par défaut", adresse.toString(), false, true, new ListeProgramme(),0));
                    a.setEtat(Etat.connaitPdms);
                }
                else a.setEtat(Etat.initial);
                a.setVerbose(jCheckBoxVerbose.isSelected());
                demarrage.dispose();
            }
        });
        jPanelValidation.add(jButtonValider);
        jPanelPrincipal.add(jPanelValidation, java.awt.BorderLayout.SOUTH);
        demarrage.getContentPane().add(jPanelPrincipal);
        demarrage.pack();
        if (arg.length == 0) {
            /** on affiche la fenêtre bloquante de choix du nom. */
            demarrage.setVisible(true);
            /** lorsque la fenêtre rend la main, (l'utilisateur a voulu démarrer
             *  l'agent), on lance l'agent. */
        } else {
            /** on initialise l'agent sans l'aide de l'utilisateur. */
            a.setNom(arg[0]);
            if (arg.length >= 1)
                a.decision = new bourse.agent.ia.Ia(a);
            else
                a.decision = new bourse.agent.ia.Aleatoire(a);
            a.setEtat(Etat.initial);
            a.setVerbose(false);
        }
        a.run();
        System.out.println("C'est la fin.");
    }
}
