package bourse.reseau;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.h2.tools.Server;

public class Bd {

	/**
     * Connection pour se connecter à la bd.
     */
    private Connection connexion;
    
    /**
     * Statement pour envoyer des requetes à la bd.
     */
    private Statement declaration;
    
    /**
     * Détermine si l'instance doit afficher toutes les requêtes qu'elle transmet
     * au SGBD ou pas.
     */
    protected boolean verbose;
    
    private boolean inMemory;

    /**
     * Constructeur de Bd et vérification de la connexion avec le sgbd.
     */
    public Bd(boolean verbose) throws SQLException, ClassNotFoundException, InstantiationException, IllegalAccessException {
    	inMemory = false;
        // vérification de la connexion avec le sgbd.
        try { this.connexion(); }
        catch (SQLException e) {
            throw new SQLException("Impossible de se connecter au sgbd : mauvais paramètres.");
        }
        try { this.deconnexion(); }
        catch (SQLException e) { throw new SQLException("Impossible de fermer la connexion avec le sgbd."); }
        this.verbose = verbose;
    }
    
    public Bd(final Connection connection) throws SQLException {
    	connexion = connection;
    	declaration = connexion.createStatement();
    	inMemory = connection.getMetaData().getURL().contains("mem");
    }
    
    public Bd(boolean verbose, boolean inMemory) throws SQLException {
    	this.inMemory = inMemory;
    	this.verbose = verbose;
    	this.connexion();
    }

    /**
     * Connexion à la bd et initialisation du statement pour envoyer des requêtes.
     */
    protected void connexion() throws SQLException {
        this.connexion = DriverManager.getConnection("jdbc:h2:" + (inMemory?"mem:bourse-aux-livres":"tcp://localhost/bourse-aux-livres"), "SA", "");
        this.declaration = this.connexion.createStatement();
        if (inMemory) {
        	init();
        }
    }
    
    /** Déconnexion de la bd (fermeture du statement et de la connexion. */
    public void deconnexion() throws SQLException {
        this.declaration.close();
        this.connexion.close();
    }
    /** Exécuter la requête et récupèrer le résultat. */
    public ResultSet resultat(String query) throws SQLException {
        if (this.connexion.isClosed()) {
            this.connexion();
        }
        ResultSet resultat = this.declaration.executeQuery(query);
        return resultat;
    }
    /** Exécuter une commande sur la db (le nombre de colonnes affectées par la
     *  requete, 0 sinon). */
    public int requete(String requete) throws SQLException {
        if (this.connexion.isClosed())
            this.connexion();
        int resultat = this.declaration.executeUpdate(requete);
        try { this.deconnexion(); } catch (SQLException e) { }
        return resultat;
    }
    /** Méthode d'affichage standard. */
    public String toString() {
        return this.connexion.toString();
    }
    
    private void init() throws SQLException {
    	requete("CREATE TABLE livres (Categorie VARCHAR(50) NOT NULL, Titre VARCHAR(100) NOT NULL, Auteur VARCHAR(50) NOT NULL, DatePar DATE NOT NULL, Editeur VARCHAR(50) NOT NULL, Format VARCHAR(20) NOT NULL, PrixN DOUBLE DEFAULT '0' NOT NULL, ISBN VARCHAR(50) NOT NULL, PRIMARY KEY (ISBN));");
    	requete("CREATE TABLE agents (nomPDM VARCHAR(50) NOT NULL, nomAgent VARCHAR(50) NOT NULL, argent DOUBLE NOT NULL, categorie VARCHAR(50) NOT NULL, PRIMARY KEY (nomAgent));");
    	requete("CREATE TABLE pdms (nom varchar(50) NOT NULL default '', adresse varchar(50) NOT NULL default '', PRIMARY KEY (nom));");
    	requete("CREATE TABLE items (ID INT(6) NOT NULL auto_increment, ISBN VARCHAR(50) NOT NULL, Etat FLOAT DEFAULT '1' NOT NULL, Proprio VARCHAR(50) NOT NULL, PrixAchat FLOAT DEFAULT '0' NOT NULL, PRIMARY KEY (ID));");
    }

    public static void main(String[] args) throws SQLException, ClassNotFoundException, InstantiationException, IllegalAccessException {
    	Server server = null;
    	try {
    		server = Server.createTcpServer("-tcp").start();
    		Bd bd = new Bd(true);
    		bd.init();
    		server.stop();
    		server = Server.createTcpServer("-tcp", "-ifExists").start();
    	} finally {
    		if (server != null) {
    			server.stop();
    		}
    	}
    }

}
