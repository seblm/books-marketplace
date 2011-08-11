package bourse.agent.sdd;

import java.util.Random;

/**
 * Répertorie les actions possibles
 * 
 * TODO transformer cette classe en enum
 */
public class Action {
    
    /**
     * 0 : Aucune action séléctionnée.
     */
    public static final int aucune = 0;
    
    /**
     * 1 : Vendre son bouquin.
     */
    public static final int vendre = 1;
    
    /**
     * 2 : Migration.
     */
    public static final int migrer = 2;
    
    /**
     * 3 : Effectuer son bilan.
     */
    public static final int bilan = 3;
    
    /**
     * 4 : Demander le programme.
     */
    public static final int programme = 4;
    
    /**
     * 5 : Demander la liste des agents connectés.
     */
    public static final int adversaires = 5;
    
    /**
     * 6 : Attendre l'enchère.
     */
    public static final int attenteEnchere = 6;
    
    /**
     * 7 : réalise l'objectif.
     */
    public static final int objectif = 7;
    
    /**
     * L'action courante.
     */
    private int action;
    
    /**
     * Change l'action.
     */
    public void setAction(int a) {
        action = a;
    }
    
    /**
     * Retourne l'action.
     */
    public int getAction() {
        return action;
    }
    
    /**
     * Construit une action en fonction de son code.
     */
    public Action(int a) {
        action = a;
    }
    
    /**
     * Construit une action au hasard.
     */
    public Action() {
        action = new Random().nextInt(6);
    }
    
    /**
     * Affichage.
     */
    public String toString(int decalage) {
        final StringBuilder output = new StringBuilder();
        for (int i = 0; i < decalage; i++) {
            output.append(' ');
        }
        output.append(action).append(" (");
        switch (this.action) {
            case 0: output.append("aucune action séléctionnée"); break;
            case 1: output.append("vendre son bouquin"); break;
            case 2: output.append("migration"); break;
            case 3: output.append("effectuer son bilan"); break;
            case 4: output.append("demande de programme"); break;
            case 5: output.append("demande de la liste des agents présents"); break;
            case 6: output.append("attente d'une proposition enchère"); break;
        }
        return output.append(')').toString();
    }
    
}
