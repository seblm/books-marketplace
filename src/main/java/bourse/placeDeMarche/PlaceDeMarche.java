package bourse.placeDeMarche;

import java.sql.*;
import java.io.*;
import java.net.*;
import java.util.LinkedList;

public class PlaceDeMarche {
    
    private CommissairePriseur commissairePriseur;
    private SalleDesVentes salleDesVentes;
    private PdmServeur serveur;
    private Requetes requetes;
    private boolean accepterAgents;
    private String nom;
    public boolean verbose;
    private boolean alive;
    private static String NOM_PAR_DEFAUT = "Groupe-E";
    public static int PORT_PAR_DEFAUT = 1981;
    private int port;
    
    public CommissairePriseur getCommissairePriseur() { return this.commissairePriseur; }
    public int getPort() { return this.port; }
    public String getNom() { return this.nom; }
    public boolean getVerbose() { return this.verbose; }
    public boolean isAlive() { return this.alive; }
    public void setNom(String nom) { this.nom = nom; }
    public synchronized boolean getAccepterAgents() { return this.accepterAgents; }
    /** Correspond au souhait de terminer la place de marché */
    public synchronized SalleDesVentes getSalleDesVentes() { return this.salleDesVentes; }
    public synchronized PdmServeur getPdmServeur() { return serveur; }
    public synchronized Requetes getRequetes() { return requetes; }
    
    PlaceDeMarche(String nom, int port, boolean verbose) {
        this.port = port;
        try {
            // Erreurs fatales
            this.serveur = new PdmServeur(this);
            this.requetes = new Requetes(this, false);
        } catch (Exception e) {
            if (this.verbose) System.err.println("PlaceDeMarche: Erreur " + e);
            System.exit(1);
        }
        this.verbose = verbose;
        this.alive = true;
        this.accepterAgents = true;
        this.nom = nom;
        this.salleDesVentes = new SalleDesVentes(this);
        this.commissairePriseur = new CommissairePriseur(this);
    }

    public void run() {
        this.requetes.declarationPlaceDeMarche(this.nom);
        this.serveur.start();
        this.commissairePriseur.start();
    }
    
    public synchronized void terminer() {
        this.accepterAgents = false;
        // Terminaison du serveur : puisqu'il est bloqué sur accept(), nous nous connectons une dernière fois chez lui avant de déconnecter aussitôt.
        try {
            new Socket(serveur.getSocketServeur().getInetAddress(), port).close();
        } catch (Exception e) { if (verbose) System.err.println(e); }
        // Terminaison du commissaire priseur
        this.commissairePriseur.interrupt();
        // Terminaison de la SalleDesVentes et des ConnexionsAgents
        salleDesVentes.supprimerTousLesAgents();
        try { requetes.deconnexion(); } catch (Exception e) { }
    }
    
    public String toString() { return this.nom; }
    public String toHtml() {
        String sortie = "<html>\n <head>\n  <title>Place de march&eacute;</title>\n </head>\n <body>\n  <h1>la place de march&eacute; " + getNom() + "</h1>\n";
        sortie += salleDesVentes.toHtml();
        sortie += commissairePriseur.toHtml();
        sortie += " </body>\n</html>";
        return sortie;
    }
    
    public static void main(String args[]) {
        String nom = NOM_PAR_DEFAUT;
        int port = PORT_PAR_DEFAUT;
        switch (args.length) {
            case 1 :
                try { port = Integer.parseInt(args[0]); } catch (NumberFormatException e) { nom = args[0]; }
                break;
            case 2 :
                try {
                    port = Integer.parseInt(args[0]);
                    nom = args[1];
                } catch (NumberFormatException e) {
                    try {
                        port = Integer.parseInt(args[1]);
                        nom = args[0];
                    } catch (NumberFormatException ePrime) {
                        System.err.println("Erreur lors du parsing du port. Le port et le nom par défaut seront utilisés.\nUsage: java PlaceDeMarche [nom] [port d'écoute]");
                    }
                }
                break;
        }
        System.out.println("Démarrage de la place de marché " + nom + " sur le port " + port + ".");
        PlaceDeMarche placeDeMarche = new PlaceDeMarche(nom, port, true);
        placeDeMarche.run();
    }
}