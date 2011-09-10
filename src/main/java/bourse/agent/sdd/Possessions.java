package bourse.agent.sdd;

import bourse.sdd.*;
import java.util.*;
import bourse.agent.*;

/** Donne toutes les Possessions que l'agent connait. */
public class Possessions {

    /** Variables d'instances. */
    private Memoire pere;
    /**
     * Donne la liste des possessions indexées par le numéro identifiant l'item
     * (le livre).
     */
    private HashMap liste;
    /** Lien vers la fenetre centrale. */
    private bourse.agent.Visualisation fenetre;

    /** Constructeur. */
    /** Construit une liste de possessions vide. */
    public Possessions(bourse.agent.Visualisation fenetre, Memoire memoire) {
        this.liste = new HashMap();
        this.fenetre = fenetre;
        this.pere = memoire;
    }

    /** Méthodes. */
    /** Récupérer une possession à partir de la clé du livre. */
    public Possession get(int id) {
        return (Possession) (this.liste.get(new Integer(id)));
    }

    /** Récupérer une possession à partir du livre. */
    public Possession get(Livre l) {
        return (Possession) (this.liste.get(new Integer(l.getId())));
    }

    /** Rafraichir l'affichage de la liste. */
    public void refresh() {
        int numeroLigne = 0;
        // Réinstanciation du JTable pour que sa taille soit celle de la
        // mémoire.
        javax.swing.table.DefaultTableModel tm = new javax.swing.table.DefaultTableModel(new String[] { "Date", "Id",
                "Propriétaire", "Catégorie", "Prix", "Etat", "Prix d'achat", "Titre", "Auteur", "Date de parution",
                "Editeur", "Format", "ISBN" }, liste.size());
        javax.swing.JTable tableau = new javax.swing.JTable(tm);
        Iterator parcours = this.liste.values().iterator();
        while (parcours.hasNext()) {
            ((Possession) parcours.next()).toRow(tableau, numeroLigne);
            numeroLigne++;
        }
        fenetre.getJScrollPanePossessionMemoire().remove(fenetre.getTableauPossessionMemoire());
        fenetre.getJScrollPanePossessionMemoire().setViewportView(tableau);
        /** En même temps, mise à jour des stats sur la catégorie. */
        this.pere.getAgents().refresh();
    }

    /**
     * Ajouter un livre à la date actuelle. Attention, écrase l'ancienne
     * posséssion. Car si on ne précise pas la date, c'est que le livre est plus
     * récent.
     */
    public void ajouter(bourse.sdd.Livre l) {
        this.liste.put(new java.lang.Integer(l.getId()), new Possession(l));
        refresh();
    }

    /** Ajouter une possession c'est-à-dire le livre et la date spécifiée. */
    public void ajouter(Possession t) {
        // si le livre est déjà connu.
        if (liste.containsKey(new Integer(t.getLivre().getId()))) {
            Possession existante = get(t.getLivre());
            // comparer les dates
            if (t.getdate() > existante.getdate())
                // l'objet à ajouter est plus récent : on doit l'ajouter.
                liste.put(new Integer(t.getLivre().getId()), t);
            // sinon, on garde la posséssion la plus récente.
        }
        // sinon, on l'ajoute.
        else
            liste.put(new Integer(t.getLivre().getId()), t);
        refresh();
    }

    /** retire une possession de la liste. */
    public void supprimer(bourse.sdd.Livre l) {
        this.liste.remove(new java.lang.Integer(l.getId()));
    }

    /** retire une possession de la liste d'après l'id du livre. */
    public void supprimer(int id) {
        this.liste.remove(new java.lang.Integer(id));
    }

    /** renvoie la liste des livres possédés par le propriétaire (agent ou pdm). */
    public ListeLivre possede(String nom) {
        ListeLivre li = new ListeLivre();
        Possession t;
        java.util.Iterator parcours = this.liste.values().iterator();
        while (parcours.hasNext()) {
            t = ((Possession) parcours.next());
            if (t.getLivre().getProprietaire().equals(nom))
                li.ajouter(t.getLivre());
        }
        return li;
    }

    public Collection getValues() {
        return this.liste.values();
    }

    /** Méthode d'affichage. */
    public String toString(int decalage) {
        String delta = "";
        for (int i = 0; i < decalage; i++)
            delta += " ";
        String output = "";
        java.util.Iterator parcours = this.liste.values().iterator();
        while (parcours.hasNext()) {
            output += delta + "possession =\n" + ((Possession) parcours.next()).toString(decalage + 1) + "\n";
        }
        if (output.length() == 0)
            return output;
        else
            return output.substring(0, output.length() - 1);
    }

    /** Méthode de test. */
    public static void main(String argc[]) {
        java.io.BufferedReader in = new java.io.BufferedReader(new java.io.InputStreamReader(System.in));
        bourse.agent.Visualisation visu = new bourse.agent.Visualisation();
        visu.show();
        Possessions l = new Possessions(visu, new Memoire(new bourse.agent.Agent("test"), visu));
        bourse.sdd.Livre l1 = new bourse.sdd.Livre("l1", "a2", new bourse.protocole.Categorie(), "poche", "O'reilly",
                (float) 50.65, (float) 0.45, "2004-01-01", "222222222", 1, "Eric", (float) 40);
        bourse.sdd.Livre l2 = new bourse.sdd.Livre("l2", "a1", new bourse.protocole.Categorie(), "poche", "Casterman",
                (float) 40.75, (float) 0.85, "1954-04-12", "222XX2254", 2, "PDM", (float) 0);
        bourse.sdd.Livre l3 = new bourse.sdd.Livre("l2", "a1", new bourse.protocole.Categorie(), "poche", "Casterman",
                (float) 40.75, (float) 0.27, "1954-04-12", "222XX2254", 3, "Eric", (float) 41);
        bourse.sdd.Livre l4 = new bourse.sdd.Livre("l2", "a1", new bourse.protocole.Categorie(), "poche", "Casterman",
                (float) 40.75, (float) 0.27, "1954-04-12", "222XX2254", 4, "protocoleman", (float) 85);
        l.ajouter(l1);
        l.ajouter(l2);
        l.ajouter(l3);
        l.ajouter(l4);
        l.refresh();
        bourse.sdd.Livre l5 = new bourse.sdd.Livre(l1);
        l5.setPrixAchat((float) 50.5);
        l.ajouter(new Possession(l1, (new java.util.Date().getTime()) - 3600)); // c'est
                                                                                // comme
                                                                                // si
                                                                                // on
                                                                                // voulait
                                                                                // ajouter
                                                                                // le
                                                                                // livre
                                                                                // acheté
                                                                                // à
                                                                                // 50.5
                                                                                // il
                                                                                // y
                                                                                // a
                                                                                // une
                                                                                // heure.
                                                                                // On
                                                                                // ne
                                                                                // garde
                                                                                // que
                                                                                // le
                                                                                // livre
                                                                                // acheté
                                                                                // à
                                                                                // 40
                                                                                // acheté
                                                                                // maintenant.
        System.out.println(l.toString(10));
        l.refresh();
        System.out.println("les livres possédes par protocoleman = \n" + l.possede("protocoleman").toString(0));
        System.out.println("les livres possédes par Eric = \n" + l.possede("Eric").toString(0));
        System.out.println(l.toString(5));
        l.supprimer(l2);
        System.out.println(l.toString(2));
        l.refresh();
    }
}