package bourse.placeDeMarche.enchere;

import java.sql.*;
import bourse.protocole.*;
import bourse.sdd.*;

public class Requetes extends bourse.reseau.Bd {
    
    public Requetes(boolean verbose) throws Exception { super(verbose); }
    
    /** Récuppère les informations d'un livre.
     * @return une instance de Livre. Attention : peut retourner null si l'id est
     * inconnu. */
    public Livre getLivre(final int id) {
        Livre livre = null;
        try {
            String requete = "SELECT Categorie, Titre, Auteur, DatePar, Editeur, Format, PrixN, livres.ISBN, Etat, Proprio, PrixAchat FROM livres, items WHERE livres.ISBN = items.ISBN AND items.ID = '" + id + "';";
            ResultSet r = this.resultat(requete);
            if (r.next())
                livre = new Livre(r.getString("Titre"), r.getString("Auteur"), new Categorie(r.getString("Categorie")), r.getString("Format"), r.getString("Editeur"), r.getFloat("PrixN"), r.getFloat("Etat"), r.getString("DatePar"), r.getString("ISBN"), id, r.getString("Proprio"), r.getFloat("PrixAchat"));
        } catch (SQLException e) { if (verbose) e.printStackTrace(); }
        return livre;
    }
    
    /** Récuppère depuis la base de données le propriétaire d'un livre, sachant
     * son id. */
    public String getProprietaire(int id) {
        String proprietaire = "";
        try {
            String requete = "SELECT Proprio FROM items WHERE ID = '" + id + "';";
            ResultSet r = this.resultat(requete);
            if (verbose) System.out.println(requete);
            proprietaire = r.getString("Proprietaire");
        } catch (SQLException e) { if (verbose) System.err.println(e); }
        return proprietaire;
    }
    
}
