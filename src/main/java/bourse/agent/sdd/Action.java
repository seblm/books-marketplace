package bourse.agent.sdd;

/** Répertorie les actions possibles */
public class Action {
    
    /** Variables d'instance. */
    /** 0 : Aucune action séléctionnée. */
    public static final int aucune = 0;
    /** 1 : Vendre son bouquin. */
    public static final int vendre = 1;
    /** 2 : Migration. */
    public static final int migrer = 2;
    /** 3 : Effectuer son bilan. */
    public static final int bilan = 3;
    /** 4 : Demander le programme. */
    public static final int programme = 4;
    /** 5 : Demander la liste des agents connectés. */
    public static final int adversaires = 5;
    /** 6 : Attendre l'enchère. */
    public static final int attenteEnchere = 6;
    /** 7 : réalise l'objectif. */
    public static final int objectif = 7;
    /** L'action courante. */
    private int action;
    /** Change l'action. */
    public void setAction(int a) { this.action = a; }
    /** Retourne l'action. */
    public int getAction() { return this.action; }
    
    /** Constructeurs. */
    /** Construit une action en fonction de son code. */
    public Action(int a) { this.action = a; }
    /** Construit une action au hasard. */
    public Action() { this.action = new java.util.Random().nextInt(6); }
    
    /** Méthodes. */
    /** Affichage.*/
    public String toString(int decalage) {
        String delta = "";
        for (int i=0; i<decalage; i++) delta += " ";
        String output = delta + String.valueOf(this.action) + " (";
        switch (this.action) {
            case 0: output += "aucune action séléctionnée"; break;
            case 1: output += "vendre son bouquin"; break;
            case 2: output += "migration"; break;
            case 3: output += "effectuer son bilan"; break;
            case 4: output += "demande de programme"; break;
            case 5: output += "demande de la liste des agents présents"; break;
            case 6: output += "attente d'une proposition enchère"; break;
        }
        return output + ")";
    }
    /** Méthode de test */
    public static void main(String argc[]) {
        Action a = new Action(8);
        for (int i=0; i<10; i++) System.out.println(new Action().toString(0));
    }
}
