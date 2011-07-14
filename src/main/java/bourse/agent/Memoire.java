package bourse.agent;

import bourse.agent.sdd.*;
import bourse.protocole.*;

/** Répertorie toutes les informations utiles pour un agent de conserver. */
public class Memoire {
    private Agent pere;
    /** Pdms. */
    private ListePdmMemoire pdms;
    /** L'ensemble des agents qu'il connait. */
    private ListeAgentMemoire agents;
    /** L'ensemble des transactions. */
    private Possessions possessions;
    /** Stocke les statisiques intéressantes pour aider à la decision d'une vente. */
    private AideDecisionVente stats;
    /** Stocke le temps comme la moyenne actuelle des tours de chaque pdm. entre
     *  1 et 100. */
    private int temps = 0;
    /** Stocke l'id du livre qu'on a décidé de vendre. */
    private int livreAVendre;
    
    /** Constructeurs. */
    /** Par défaut. */
    public Memoire(bourse.agent.Agent _a, Visualisation fenetre) {
        this.pere = _a;
        this.pdms = new ListePdmMemoire(fenetre); 
        this.agents = new ListeAgentMemoire(fenetre, this);
        this.possessions = new Possessions(fenetre, this);
        this.stats = new AideDecisionVente(_a);
        this.temps = 0;
    }
    
    /** Méthodes. */
    /** Renvoie la liste des pdm. */
    public ListePdmMemoire getPdms() { return this.pdms; }
    /** Renvoie la liste des livres. */
    public ListeAgentMemoire getAgents() { return this.agents; }
    /** Renvoie la liste des transactions. */
    public Possessions getPossessions() { return this.possessions; }
    /** Modifie le temps. */
    public void setTemps(int temps) { this.temps = temps; }
    /** Renvoie le temps */
    public int getTemps() { return this.temps; }
    
    public int getLivreAVendre() { return livreAVendre; }
    
    public void setLivreAVendre(int _id) { livreAVendre = _id; }
    
    public AideDecisionVente getAideDecisionVente() { return stats; }
    /** Méthode d'affichage. */
    public String toString(int decalage) {
        String delta = "";
        for (int i=0; i<decalage; i++) delta += " ";
        return delta + "pdms :\n" + this.pdms.toString(decalage+1) + "\n"
             + delta + "agents :\n" + this.agents.toString(decalage+1) + "\n"
             + delta + "possessions :\n" + this.possessions.toString(decalage+1) + "\n"
             + delta + "temps :\n" + this.temps;
    }
    public void refreshTemps() {
        ListePdmMemoire liste = this.getPdms();
        java.util.Iterator parcours = liste.getListe().values().iterator();
        int total = 0;
        int cpt = 0;
        while (parcours.hasNext()) {
            total += ((PdmMemoire)parcours.next()).getNumeroDernierTour();
            cpt++;
        }
        temps = total / cpt; 
    }
    /** Méthode de test. */
    public static void main(String argc[]) {
        bourse.agent.Visualisation visu = new bourse.agent.Visualisation();
        visu.show();
        Memoire m = new Memoire(new bourse.agent.Agent("test"), visu);
        PdmMemoire p1 = new PdmMemoire("Groupe-A", "HOME", false, true, new ListeProgramme(),1);
        PdmMemoire p2 = new PdmMemoire("Groupe-B", "192.168.1.2:80", true, false, new ListeProgramme(),2);
        PdmMemoire p3 = new PdmMemoire("Groupe-A", "0.0.0.0:0", false, false, new ListeProgramme(),2);
        PdmMemoire p4 = new PdmMemoire(new Pdm("Groupe-X", "169.254.1.5:2525"));
        m.getPdms().ajouter(p1);
        m.getPdms().ajouter(p2);
        m.getPdms().ajouter(p3);
        m.getPdms().ajouter(p4);
        
        bourse.sdd.Livre l1 = new bourse.sdd.Livre("l1", "a2", new bourse.protocole.Categorie(), "poche", "O'reilly", (float)50.65, (float)0.45, "2004-01-01", "222222222", 1, "Seb", (float)65);
        bourse.sdd.Livre l2 = new bourse.sdd.Livre("l2", "a1", new bourse.protocole.Categorie(), "poche", "Casterman", (float)40.75, (float)0.85, "1954-04-12", "222XX2254", 2, "protocoleman", (float)50);
        bourse.sdd.Livre l3 = new bourse.sdd.Livre("l2", "a1", new bourse.protocole.Categorie(), "poche", "Casterman", (float)40.75, (float)0.27, "1954-04-12", "222XX2254", 3, "arno", (float)12);
        
        AgentMemoire a1 = new AgentMemoire("Arnaud", new Categorie(Categorie.INFO), true);
        AgentMemoire a2 = new AgentMemoire("Jerôme", new Categorie(Categorie.INFO), false);
        AgentMemoire a3 = new AgentMemoire("Eric", new Categorie(Categorie.BD), true);
        m.getAgents().ajouter(a1);
        m.getAgents().ajouter(a2);
        m.getAgents().ajouter(a3);
        
        Possession t1 = new Possession(l1);
        Possession t2 = new Possession(l2);
        m.getPossessions().ajouter(t1);
        m.getPossessions().ajouter(t2);

        System.out.println(m.toString(6));
    }
    
}
