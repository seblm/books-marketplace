package bourse.agent.sdd;

import java.util.*;
import bourse.protocole.*;
import bourse.sdd.*;
import bourse.agent.*;

public class ListeAgentMemoire {

    /** Variables d'instances. */
    private Memoire pere;
    /** Donne la liste des agents. */
    private HashMap liste;
    /** Lien vers la fenetre centrale. */
    private bourse.agent.Visualisation fenetre;

    /** Constructeur. */
    /** Construit une liste d'agent vide. */
    public ListeAgentMemoire(bourse.agent.Visualisation fenetre, Memoire m) {
        this.pere = m;
        this.liste = new HashMap();
        this.fenetre = fenetre;
    }

    /** Méthodes. */
    public int taille() {
        return this.liste.size();
    }

    /** Rafraichir l'affichage de la liste. */
    public void refresh() {
        int numeroLigne = 0;
        // Réinstanciation du JTable pour que sa taille soit celle de la
        // mémoire.
        javax.swing.table.DefaultTableModel tm = new javax.swing.table.DefaultTableModel(new String[] { "Nom",
                "Catégorie", "Présent", "Fréquence des catégories" }, liste.size());
        javax.swing.JTable tableau = new javax.swing.JTable(tm);
        Iterator parcours = this.liste.values().iterator();
        while (parcours.hasNext()) {
            ((AgentMemoire) parcours.next()).toRow(tableau, numeroLigne);
            numeroLigne++;
        }
        fenetre.getJScrollPaneAgentMemoire().remove(fenetre.getTableauPdmMemoire());
        fenetre.getJScrollPaneAgentMemoire().setViewportView(tableau);
    }

    /** Changer la présence de tous les agents. */
    public void setPresent(boolean present) {
        Iterator parcours = this.liste.values().iterator();
        while (parcours.hasNext()) {
            ((AgentMemoire) parcours.next()).setPresent(present);
        }
    }

    /** Ajouter un agent concurrent. */
    public void ajouter(AgentMemoire a) {
        this.liste.put(a.getNom(), a);
    }

    /** Vrai si la liste contient un agent nommé nom. */
    public boolean contient(String nom) {
        return this.liste.containsKey(nom);
    }

    /**
     * Met a jour la catégorie des adversaires et la fréquence des catégories de
     * livres de chaque agent.
     */
    public void miseAJourCategorie() {
        Iterator parcours = this.liste.values().iterator();
        bourse.agent.sdd.AgentMemoire courant;
        /** Le nombre de livres de la catégorie i */
        int[] nb = new int[5];
        for (int i = 0; i < 5; i++)
            nb[i] = 0;
        int total = 0;
        int maxnb;
        int ind;
        /** pour chaque agent, */
        while (parcours.hasNext()) {
            courant = ((AgentMemoire) parcours.next());
            bourse.agent.sdd.ListeLivre l = this.pere.getPossessions().possede(courant.getNom());
            /** parcourir les livres et renvoyer le max. */
            Iterator parcoursLivre = l.getValeurs().iterator();
            int categorieCourante;
            /** on met le tableau de stats à zéro. */
            for (int i = 0; i < 5; i++)
                nb[i] = 0;
            /** le total des transaction vaut zéro. */
            total = 0;
            /** pour chaque livre possédé, */
            while (parcoursLivre.hasNext()) {
                categorieCourante = new bourse.protocole.Categorie(((Livre) parcoursLivre.next()).getCategorie())
                        .getCode();
                if (categorieCourante != 5) {
                    /** incrémentation du nombre de livre de la catégorie i */
                    nb[categorieCourante] = nb[categorieCourante] + 1;
                    total++;
                }
            }
            /**
             * si le total des transaction n'est pas nul, on met à jour les
             * statistiques.
             */
            if (total != 0) {
                for (int i = 0; i < 5; i++)
                    courant.getStatsCategorie()[i] = (float) ((float) nb[i] / (float) total);
            }
            /**
             * mise à jour de la catégorie : la catégorie la plus fréquemment
             * achetée.
             */
            maxnb = 0;
            ind = 5;
            for (int i = 0; i < 5; i++) {
                if (maxnb < nb[i]) {
                    maxnb = nb[i];
                    ind = i;
                }
            }
            courant.setCategorie(new Categorie(ind));
        }
    }

    /**
     * Méthode de mise à jour globale à partir d'une liste d'agents. Typiquement
     * : après avoir téléchargé la nouvelle liste.
     */
    public void miseAJour(ListeAgent l) {
        this.setPresent(false);
        Iterator parcours = l.valeurs().iterator();
        Agent courant;
        while (parcours.hasNext()) {
            courant = ((Agent) parcours.next());
            if (this.contient(courant.getNom()))
                ((AgentMemoire) this.liste.get(courant.getNom())).setPresent(true);
            else
                this.ajouter(new AgentMemoire(courant.getNom(), new Categorie(Categorie.AUCUNE), true));
        }
        this.miseAJourCategorie();
        refresh();
    }

    /**
     * Mettre à jour les listes avec une liste de String représentant les noms
     * des agents.
     */
    public void miseAJour(LinkedList l) {
        ListeAgent destination = new ListeAgent();
        ListIterator parcours = l.listIterator();
        while (parcours.hasNext()) {
            destination.ajouter(new Agent(((String) parcours.next())));
        }
        this.miseAJour(destination);
    }

    /** Méthode d'affichage. */
    public String toString(int decalage) {
        String output = "";
        Iterator parcours = this.liste.values().iterator();
        while (parcours.hasNext()) {
            output += ((AgentMemoire) parcours.next()).toString(decalage) + "\n";
        }
        if (output.length() == 0)
            return output;
        else
            return output.substring(0, output.length() - 1);
    }

    /** Renvoie le nombre d'agents connéctés de la même catégoruie. */
    public int concurrent(Categorie cat, String nom) {
        int cpt = 0;
        Iterator parcours = this.liste.values().iterator();
        /** pour chaque agent connecté, */
        while (parcours.hasNext()) {
            AgentMemoire am = (AgentMemoire) parcours.next();
            /** si l'agent courant n'est notre agent. */
            if (!(am.getNom().equals(nom)))
                /** si l'agent est présent. */
                if (am.getPresent())
                    /** si l'agent a la catégorie voulue */
                    if (am.getCategorie().equals(cat))
                        cpt++;
        }
        return cpt;
    }

    /***/
    public float coefficientCategorie(Categorie c) {
        float sommeCategorie = 0;
        int nbAgents = 0;
        float meilleureStat = 0;
        Iterator parcours = this.liste.values().iterator();
        AgentMemoire am;
        while (parcours.hasNext()) {
            am = (AgentMemoire) parcours.next();
            if (am.getPresent()) {
                nbAgents++;
                float statC = am.getStatsCategorie()[c.getCode()];
                sommeCategorie += statC;
                if (meilleureStat < statC)
                    meilleureStat = statC;
            }
        }
        if (meilleureStat > 0.8)
            return meilleureStat;
        else
            return (sommeCategorie / ((nbAgents == 0) ? 1 : nbAgents));
    }

    /** Programme principal. */
    public static void main(String[] argc) {
        java.io.BufferedReader in = new java.io.BufferedReader(new java.io.InputStreamReader(System.in));
        bourse.agent.Visualisation visu = new bourse.agent.Visualisation();
        visu.show();
        Memoire m = new Memoire(new bourse.agent.Agent("test"), visu);
        System.out.println("initialisation : ");
        m.getAgents().ajouter(new AgentMemoire("Arnaud", new Categorie(Categorie.INFO), true));
        m.getAgents().ajouter(new AgentMemoire("Jerôme", new Categorie(Categorie.INFO), false));
        m.getAgents().ajouter(new AgentMemoire("Eric", new Categorie(Categorie.BD), true));
        m.getAgents().refresh();

        try {
            in.readLine();
        } catch (Exception e) {
        }

        System.out.println("maj 1 : ");
        Agent a1 = new bourse.agent.sdd.Agent("Arnaud"); // arnaud toujours
                                                         // present.
        Agent a2 = new bourse.agent.sdd.Agent("Sebastien"); // sebastien
                                                            // nouveau.
        Agent a3 = new bourse.agent.sdd.Agent("Jerôme"); // jerome de nouveau
                                                         // present.
        ListeAgent l = new ListeAgent();
        l.ajouter(a1);
        l.ajouter(a2);
        l.ajouter(a3);
        m.getAgents().miseAJour(l);
        System.out.println(m.getAgents().toString(1));
        m.getAgents().refresh();

        try {
            in.readLine();
        } catch (Exception e) {
        }

        System.out.println("maj 2 : ");
        java.util.LinkedList maj = new java.util.LinkedList();
        maj.add("Eric"); // Eric est de retour.
        maj.add("Espion"); // Espion est nouveau.
        m.getAgents().miseAJour(maj);
        System.out.println(m.getAgents().toString(1));
        m.getAgents().refresh();
    }
}
