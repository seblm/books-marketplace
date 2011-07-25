package bourse.agent.sdd;

import bourse.protocole.*;

/** Stocke les données relatives aux agents concurrents.
 *  Par exemple lors de la récupération de la liste des agents.*/
public class AgentMemoire extends Agent {
    
    /** Variables d'instance. */
    /** La catégorie présumée de cette agent. */
    private Categorie categorie;
    /** Si l'agent est present sur la pdm*/
    private boolean present; 
    /** Stocke les statistiques associées à chaque catégories. */
    private float[] statsCategorie = new float[5]; 
    
    public Categorie getCategorie() { return this.categorie;}
    
    public void setCategorie(Categorie cat) {this.categorie = cat;}
    
    public boolean getPresent() { return this.present;}
    
    public void setPresent(boolean b) {this.present = b;}
    
    public float[] getStatsCategorie() { return statsCategorie; }
    
    public void setStatsCategorie(float[] _statsCategorie) {
        for (int i=0; i<5; i++) statsCategorie[i] = _statsCategorie[i];
    }
    
    /** Construit un nouvel agent à stocker. */
    public AgentMemoire(String nom, Categorie cat, boolean pres) {
        super(nom); this.categorie = cat; this.present = pres;
        for (int i=0; i<5; i++) { statsCategorie[i] = 0; }
    }
    /** Affichage. */
    public String toString(int decalage) {
        return super.toString(decalage)
           + ", catégorie = " + this.categorie.toString(0)
           + ", present = " + this.present
           + ", stats = " + statsCategorie[0] + ", " + statsCategorie[1] + ", " + statsCategorie[2] + ", " + statsCategorie[3] + ", " + statsCategorie[4];
    }
    /** Méthode d'affichage dans un javax.swing.JTable */
    public void toRow(javax.swing.JTable tableau, int numeroLigne) {
        tableau.setValueAt(getNom().toString(), numeroLigne, 0);
        tableau.setValueAt(getCategorie().toString(), numeroLigne, 1);
        tableau.setValueAt((getPresent()?"oui":"non"), numeroLigne, 2);
        String output = "";
        for (int i=0; i<5; i++) { output += (float)((float)Math.round((float)statsCategorie[i] * 10000)/(float)100) + "% "; }
        tableau.setValueAt(output, numeroLigne, 3);
    }
    /** Méthode de test de la classe. */
    public static void main(String argc[]) {
        bourse.agent.Visualisation visu = new bourse.agent.Visualisation();
        visu.setVisible(true);
        javax.swing.table.DefaultTableModel tm = new javax.swing.table.DefaultTableModel(
            new String [] {"Nom", "Categorie", "Présent", "Fréquence des Catégories"},
            4
        );
        javax.swing.JTable tableau = new javax.swing.JTable(tm);
        AgentMemoire a1 = new AgentMemoire("Arnaud", new Categorie(Categorie.SF), true);
        AgentMemoire a2 = new AgentMemoire("Sébastien", new Categorie(Categorie.BD), false);
        AgentMemoire a3 = new AgentMemoire("Protocoleman", new Categorie(Categorie.ROMAN), true);
        AgentMemoire a4 = new AgentMemoire("Eric", new Categorie(Categorie.SCIENCE), false);
        System.out.println(a1.toString(5)); a1.toRow(tableau, 0);
        System.out.println(a2.toString(5)); a2.toRow(tableau, 1);
        System.out.println(a3.toString(5)); a3.toRow(tableau, 2);
        System.out.println(a4.toString(5)); a4.toRow(tableau, 3);
        System.out.println("test = " + (float)((float)Math.round(0.66665 * 10000)/(float)100));
        visu.getJScrollPaneAgentMemoire().remove(visu.getTableauAgentMemoire());
        visu.getJScrollPaneAgentMemoire().setViewportView(tableau);
    }
}
     

