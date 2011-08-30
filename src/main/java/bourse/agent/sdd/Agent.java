package bourse.agent.sdd;

/**
 * Stocke les données relatives aux agents concurrents. Par exemple lors de la
 * récupération de la liste des agents.
 */
public class Agent {

    /**
     * Le nom de l'agent adversaire.
     */
    private String nom;

    public String getNom() {
        return this.nom;
    }

    /**
     * Construit un nouvel agent concurrent.
     */
    public Agent(String nom) {
        this.nom = nom;
    }

    /**
     * Affichage.
     */
    public String toString(int decalage) {
        String delta = "";
        for (int i = 0; i < decalage; i++) {
            delta += " ";
        }
        return delta + "nom = " + this.nom;
    }
}
