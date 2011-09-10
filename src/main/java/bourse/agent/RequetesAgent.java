package bourse.agent;

import java.sql.*;

/** Génère l'ensemble des requetes nécessaires à l'agent. */
public class RequetesAgent extends bourse.reseau.Bd {
    /** Constructeur. */
    public RequetesAgent(boolean verbose) throws SQLException, java.lang.ClassNotFoundException, java.lang.InstantiationException, java.lang.IllegalAccessException{
        super(verbose);
    }
    /** Toutes les pdms. */
    public ResultSet getPdMs() throws SQLException {
        return this.resultat("SELECT * FROM pdms;");
    }
    /** L'adresse IP de la pdm en fonction de son nom. */
    public String getPdmIP(String nom) throws SQLException {
        ResultSet r = this.resultat("SELECT adresse FROM pdms WHERE nom=" + nom + ";");
        return r.getString("adresse").split(":")[0];
    }
    /** Le port de la pdm en fonction de son nom. */
    public String getPdmPort(String nom) throws SQLException {
        ResultSet r = this.resultat("SELECT adresse FROM pdms WHERE nom=" + nom + ";");
        return r.getString("adresse").split(":")[1];
    }
    /** Le résultat d'un agent. (categorie, id, titre, points, argent) */
    public ResultSet getResultPerBook(String nomAgent) throws SQLException {
        return this.resultat("SELECT livres.categorie, id, titre, round( prixn * etat / prixachat, 2 ) points, argent FROM livres, items, agents WHERE livres.isbn = items.isbn AND proprio = nomAgent AND livres.categorie = agents.categorie AND nomagent = '" + nomAgent + "' ORDER BY points desc");
    }
    /** Pour tester */
    public static void main(String argc[]) {
        try {
            RequetesAgent ra = new RequetesAgent(true);
            ResultSet r = ra.getPdMs();
            if (r.next()) System.out.println(r.getString("adresse").split(":")[0]);
        } catch (Exception e) { e.printStackTrace(System.err); }
    }
}
