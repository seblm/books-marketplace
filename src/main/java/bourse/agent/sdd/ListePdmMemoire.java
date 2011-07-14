package bourse.agent.sdd;

import java.util.*;

/** Donne toutes les pdms que l'agent connait */
public class ListePdmMemoire {
    
    /** Variables d'instances. */
    /** Liste des pdms. */
    private HashMap liste;
    /** Lien vers la fenetre centrale. */
    private bourse.agent.Visualisation fenetre;
    
    /** Constructeur. */
    /** Construit une liste de pdms vide. */
    public ListePdmMemoire(bourse.agent.Visualisation fenetre) {
        this.liste = new HashMap();
        this.fenetre = fenetre;
    }
    
    /** Méthodes. */
    /** Retourne la liste. */
    public HashMap getListe() { return this.liste; }
    /** Rafraichir l'affichage de la liste. */
    public void refresh() {
        int numeroLigne = 0;
        // Réinstanciation du JTable pour que sa taille soit celle de la mémoire.
        javax.swing.table.DefaultTableModel tm = new javax.swing.table.DefaultTableModel(
            new String [] {"Nom", "Adresse", "Active", "Visitée", "Enchères gérées", "Numero du tour"},
            liste.size()
        );
        javax.swing.JTable tableau = new javax.swing.JTable(tm);
        Iterator parcours = this.liste.values().iterator();
        while (parcours.hasNext()) {
            ((PdmMemoire)parcours.next()).toRow(tableau, numeroLigne);
            numeroLigne++;
        }
        fenetre.getJScrollPanePdmMemoire().remove(fenetre.getTableauPdmMemoire());
        fenetre.getJScrollPanePdmMemoire().setViewportView(tableau);
    }
    /** Ajouter une pdm. */
    public void ajouter(PdmMemoire pdm) { liste.put(pdm.getNom(), pdm); }
    /** Supprimer une pdm. */
    public void supprimer(PdmMemoire pdm) { this.liste.remove(pdm.getNom()); }
    /** Accès à une pdm de la liste à partir de son nom. */
    public PdmMemoire acceder(String nom) { return (PdmMemoire)this.liste.get(nom); }
    /** Renvoie vrai si la pdm existe dans la liste. */
    protected boolean existe(String nom) { return this.liste.containsKey(nom); }
    /** Renvoie vrai si aucune pdm n'est active dans la liste. */
    public boolean aucuneActive() {
        Iterator parcours = this.liste.values().iterator();
        while (parcours.hasNext()) {
            if (((PdmMemoire)parcours.next()).getActive()) return false;
        } return true;
    }
    /** Renvoie la première pdm active non visitée, null sinon. */
    public PdmMemoire premiereActiveNonVisitee() {
        Iterator parcours = this.liste.values().iterator();
        PdmMemoire current;
        while (parcours.hasNext()) {
            current = (PdmMemoire)(parcours.next());
            if (current.getActive() && !current.getVisitee()) return current;
        } return null;
    }
    /** Renvoie une autre pdm active visitée. */
    public PdmMemoire premiereActiveVisitee() {
        Iterator parcours = this.liste.values().iterator();
        PdmMemoire current;
        // copie des pdms actives et visitées
        LinkedList copiePdmActives = new LinkedList();
        while (parcours.hasNext()) {
            current = (PdmMemoire)(parcours.next());
            if (current.getActive() && current.getVisitee()) copiePdmActives.add(current);
        }
        // Sélection au hasar d'une pdm active et visitée
        int index = new java.util.Random().nextInt(copiePdmActives.size());
        return (PdmMemoire)copiePdmActives.get(index);
    }
    /** Change tous les états d'activation de la liste. Méthode dangeureuse. */
    private void setActivee(boolean activee) {
        Iterator parcours = this.liste.values().iterator();
        while (parcours.hasNext()) {
            ((PdmMemoire)parcours.next()).setActive(activee);
        }
    }
    /** Renvoie vrai si la liste est vide. */
    public boolean estVide() { return this.liste.isEmpty(); }
    /** Méthode de mise à jour globale à partir d'une liste de pdm de l'environnement.
     *  Typiquement : après avoir téléchargé la nouvelle liste.*/
    public void miseAJour(ListePdm l) {
        this.setActivee(false); // par défaut, toutes les pdms sont désactivées.
        Iterator parcours = l.getListe().values().iterator();
        PdmMemoire ancienne;
        Pdm nouvelle;
        PdmMemoire aInserer;
        while (parcours.hasNext()) {
            nouvelle = (Pdm)parcours.next(); // on a la pdm à sauvegarder.
            if (existe(nouvelle.getNom())) { // on doit mettre à jour la pdm sans l'écraser.
                ancienne = acceder(nouvelle.getNom()); // on a l'ancienne pdm.
                aInserer = new PdmMemoire(nouvelle.getNom(), nouvelle.getAdresse().toString(), ancienne.getVisitee(), true, ancienne.getProgramme(), ancienne.getNumeroDernierTour());
            } else // on peut ajouter la pdm.
                aInserer = new PdmMemoire(nouvelle);
            ajouter(aInserer);
        }
        refresh();
    }
    /** Méthode d'affichage. */
    public String toString(int decalage) {
        String delta = "";
        for (int i=0; i<decalage; i++) delta += " ";
        String output = "";
        Iterator parcours = this.liste.values().iterator();
        while (parcours.hasNext()) { output += ((PdmMemoire)parcours.next()).toString(decalage) + "\n"; }
        if (output.length() == 0) return output;
        else return output.substring(0, output.length()-1);
    }
    /** Programme principal. */
    public static void main(String[] argc) {
        java.io.BufferedReader in = new java.io.BufferedReader(new java.io.InputStreamReader(System.in));
        bourse.agent.Visualisation visu = new bourse.agent.Visualisation();
        visu.show();
        ListePdmMemoire l = new ListePdmMemoire(visu);
        PdmMemoire p1 = new PdmMemoire("p1", "HOME", false, false, new ListeProgramme(),1);
        PdmMemoire p2 = new PdmMemoire("p2", "192.168.1.2:80", true, true, new ListeProgramme(),2);
        PdmMemoire p3 = new PdmMemoire("p3", "0.0.0.0:0", false, false, new ListeProgramme(),2);
        
        System.out.println("initialisation : ");
        l.ajouter(p1);
        l.ajouter(p2);
        l.ajouter(p3);
        System.out.println(l.toString(3));
        
        // pause 
        try { in.readLine(); } catch (Exception e) {  }
                
        System.out.println("Aucune active ? " + l.aucuneActive());
        
        if (l.premiereActiveNonVisitee() != null)
            System.out.println("Première active non visitée : " + l.premiereActiveNonVisitee().toString(4));
        if (l.premiereActiveVisitee() != null)
            System.out.println("Première active visitée : " + l.premiereActiveVisitee().toString(4));
        
        System.out.println("mises à jour : ");
        Pdm pdm1 = new Pdm("p1", "192.168.1.1:8080"); // p1 toujours active
        Pdm pdm2 = new Pdm("p2", "192.168.1.3:80"); // p2 a changé d'adresse
        // p3 déconnectée.
        Pdm pdm4 = new Pdm("p4", "192.168.1.5:2726"); // p4 nouvelle arrivée.
        ListePdm p = new ListePdm(); p.ajouter(pdm1); p.ajouter(pdm2); p.ajouter(pdm4);
        l.miseAJour(p);
        System.out.println(l.toString(5));
        
        // pause 
        try { in.readLine(); } catch (Exception e) {  }
        
        System.out.println("mises à jour : ");
        pdm1 = new Pdm("p1", "192.168.1.52:8080"); // p1 changé d'adresse
        // p2 déconnectée.
        pdm4 = new Pdm("p4", "192.168.1.5:2726"); // p4 nouvelle arrivée.
        p = new ListePdm(); p.ajouter(pdm1); p.ajouter(pdm4);
        l.miseAJour(p);        
    }
}
