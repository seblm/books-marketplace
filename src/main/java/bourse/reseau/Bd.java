package bourse.reseau;
import java.sql.*;
import java.lang.ClassNotFoundException;
import java.lang.InstantiationError;
import java.lang.IllegalAccessException;
import bourse.protocole.Protocole;

public class Bd {
    /** Connection pour se connecter à la bd. */
    private Connection connexion;
    /** Statement pour envoyer des requetes à la bd.. */
    private Statement declaration;
    /** Détermine si l'instance doit afficher toutes les requêtes qu'elle transmet
     * au SGBD ou pas. */
    protected boolean verbose;

    /** Constructeur de Bd et vérification de la connexion avec le sgbd. */
    public Bd(boolean verbose) throws SQLException, ClassNotFoundException, InstantiationException, IllegalAccessException {
        // chargement du pilote MySQL à la construction.
        try { Class.forName("org.gjt.mm.mysql.Driver").newInstance(); } //loads the driver
        catch (ClassNotFoundException e) { throw new ClassNotFoundException("Impossible de trouver le driver MM."); }
        catch (InstantiationException e) { throw new InstantiationException("Impossible d'instancier le driver MM."); }
        catch (IllegalAccessException e) { throw new IllegalAccessException("Impossible d'accéder au driver MM."); }        
        // vérification de la connexion avec le sgbd.
        try { this.connexion(); }
        catch (SQLException e) {
            System.out.println(Protocole.parametresBd);
            throw new SQLException("Impossible de se connecter au sgbd : mauvais paramètres.");
        }
        try { this.deconnexion(); }
        catch (SQLException e) { throw new SQLException("Impossible de fermer la connexion avec le sgbd."); }
        this.verbose = verbose;
    }

    /** Connexion à la bd et initialisation du statement pour envoyer des requêtes. */
    protected void connexion() throws java.sql.SQLException {
        try {
            this.connexion = DriverManager.getConnection("jdbc:mysql://" + Protocole.parametresBd.getHote() + ":" + Protocole.parametresBd.getPort() + "/" + Protocole.parametresBd.getBaseDeDonnees(), Protocole.parametresBd.getUtilisateur(), Protocole.parametresBd.getMotDePasse());
            this.declaration = this.connexion.createStatement();
        } catch (SQLException e) {
            if (verbose) e.printStackTrace(System.err);
            throw new SQLException("Impossible de se connecter à la bd : mauvais paramètres.");
        }
    }
    /** Déconnexion de la bd (fermeture du statement et de la connexion. */
    public void deconnexion() throws SQLException {
        this.declaration.close();
        this.connexion.close();
    }
    /** Exécuter la requête et récupèrer le résultat. */
    public ResultSet resultat(String query) throws SQLException {
        //if (this.connexion.isClosed())
            this.connexion();
        ResultSet resultat = this.declaration.executeQuery(query);
        this.connexion.close();
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
    
    /** Méthode de test. */
    public static void main(String arcg[]) {
        try {
            Bd b = new Bd(true);
            ResultSet r = b.resultat("SELECT * FROM livres;");
            while (r.next())
                System.out.println("Titre = " + r.getString("Titre") + ", Prix = " + r.getFloat("PrixN"));
            r = b.resultat("SELECT COUNT(*) FROM livres;");
            if (r.next()) System.out.println("Le nombre du livres entrés = " + r.getInt(1));
        } catch (Exception e) { e.printStackTrace(System.err); }
    }
    
}
