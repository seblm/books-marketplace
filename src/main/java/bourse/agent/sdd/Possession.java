package bourse.agent.sdd;

import bourse.sdd.*;
import java.util.*;

/** Cette classe stocke les données relatives aux possessions. */
public class Possession {

    /** Variables d'instance. */
    /** Le Livre en question. */
    private Livre livre;
    /**
     * La date de la vente en temps systeme (0 si le livre appartient à une
     * pdm).
     */
    private long date;
    /** En instance de vente */
    private boolean instanceDeVente = false;

    /** Constructeur. */
    /** Construit une nouvelle possession d'agent grâce à un livre. */
    public Possession(Livre li) {
        livre = li;
        date = new java.util.Date().getTime();
        instanceDeVente = false;
    }

    /** Construit une nouvelle possession d'agent grâce à un livre et une date. */
    public Possession(Livre li, long date) {
        this.livre = li;
        this.date = date;
    }

    /** Méthode d'affichage. */
    public String toString(int decalage) {
        String delta = "";
        for (int i = 0; i < decalage; i++)
            delta += " ";
        return delta + "livre = " + "\n" + livre.toString(decalage + 1) + "\n" + delta + "en instance de vente = "
                + instanceDeVente + "\n" + delta + "date = " + String.valueOf(this.date);
    }

    /** Renvoie le livre de la possession. */
    public Livre getLivre() {
        return this.livre;
    }

    /** Renvoie la date système. */
    public long getdate() {
        return this.date;
    }

    /** Renvoie vrai si le bouquin est en vente. */
    public boolean getInstanceDeVente() {
        return instanceDeVente;
    }

    /** Modifie la vente du bouquin. */
    public void setInstanceDeVente(boolean _instanceDeVente) {
        instanceDeVente = _instanceDeVente;
    }

    /** Méthode d'affichage dans un javax.swing.JTable */
    public void toRow(javax.swing.JTable tableau, int numeroLigne) {
        tableau.setValueAt(String.valueOf(getdate()), numeroLigne, 0);
        tableau.setValueAt(String.valueOf(getLivre().getId()), numeroLigne, 1);
        tableau.setValueAt(getLivre().getProprietaire(), numeroLigne, 2);
        tableau.setValueAt(getLivre().getCategorie().toString(), numeroLigne, 3);
        tableau.setValueAt(String.valueOf(getLivre().getPrix()), numeroLigne, 4);
        tableau.setValueAt(String.valueOf(getLivre().getEtat()), numeroLigne, 5);
        tableau.setValueAt(String.valueOf(getLivre().getPrixAchat()), numeroLigne, 6);
        tableau.setValueAt(getLivre().getTitre(), numeroLigne, 7);
        tableau.setValueAt(getLivre().getAuteur(), numeroLigne, 8);
        tableau.setValueAt(getLivre().getDateParu(), numeroLigne, 9);
        tableau.setValueAt(getLivre().getEditeur(), numeroLigne, 10);
        tableau.setValueAt(getLivre().getFormat(), numeroLigne, 11);
        tableau.setValueAt(getLivre().getIsbn(), numeroLigne, 12);
    }

    /** La méthode main de test. */
    public static void main(String[] argc) {
        java.io.BufferedReader in = new java.io.BufferedReader(new java.io.InputStreamReader(System.in));
        bourse.agent.Visualisation visu = new bourse.agent.Visualisation();
        visu.show();
        javax.swing.table.DefaultTableModel tm = new javax.swing.table.DefaultTableModel(new String[] { "Date", "Id",
                "Propriétaire", "Catégorie", "Prix", "Etat", "Prix d'achat", "Titre", "Auteur", "Date de parution",
                "Editeur", "Format", "ISBN" }, 3);
        javax.swing.JTable tableau = new javax.swing.JTable(tm);
        bourse.sdd.Livre l1 = new bourse.sdd.Livre("l1", "a2", new bourse.protocole.Categorie(), "poche", "O'reilly",
                (float) 50.65, (float) 0.45, "2004-01-01", "222222222", 1, "Eric", (float) 60);
        bourse.sdd.Livre l2 = new bourse.sdd.Livre("l2", "a1", new bourse.protocole.Categorie(), "poche", "Casterman",
                (float) 40.75, (float) 0.85, "1954-04-12", "222XX2254", 2, "protocoleman", (float) 200);
        bourse.sdd.Livre l3 = new bourse.sdd.Livre("l2", "a1", new bourse.protocole.Categorie(), "poche", "Casterman",
                (float) 40.75, (float) 0.27, "1954-04-12", "222XX2254", 3, "Seb", (float) 25.51);
        Possession t1 = new Possession(l1);
        Possession t2 = new Possession(l2);
        Possession t3 = new Possession(l3);
        System.out.println(t1.toString(0));
        t1.toRow(tableau, 0);
        System.out.println(t2.toString(5));
        t2.toRow(tableau, 1);
        System.out.println(t3.toString(2));
        t3.toRow(tableau, 2);
        visu.getJScrollPanePossessionMemoire().remove(visu.getTableauPossessionMemoire());
        visu.getJScrollPanePossessionMemoire().setViewportView(tableau);
    }
}
