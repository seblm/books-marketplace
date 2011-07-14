package bourse.agent.sdd;

/** Stocke les données relatives aux agents concurrents.
 *  Par exemple lors de la récupération de la liste des agents.*/
public class Agent {
    
    /** Variables d'instance. */
    /** Le nom de l'agent adversaire. */
    private String nom;
 
    public String getNom() { return this.nom;}
    
    /** Construit un nouvel agent concurrent. */
    public Agent(String nom) {
        this.nom = nom; 
    }
    /** Affichage. */
    public String toString(int decalage) {
        String delta = "";
        for (int i=0; i<decalage; i++) delta += " ";
        return delta + "nom = " + this.nom;
    }
    /** Méthode de test de la classe. */
    public static void main(String argc[]) {
        Agent a = new Agent("Arnaud");
        System.out.println(a.toString(5));
        a = new Agent("Eric");
        System.out.println(a.toString(5));
    }
}
     

