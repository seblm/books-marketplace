package bourse.agent.sdd;

/** Répertorie les états possibles. */
public class Etat {

    /** Variables d'instance. */
    /** 0 : Quitter. */
    public static int quitter = 0;
    /** 1 : Etat initial. */
    public static int initial = 1;
    /** 2 : Connait les pdms actives. */
    public static int connaitPdms = 2;
    /** 3 : Pdm choisie dans la liste des actives. */
    public static int pdmChoisie = 3;
    /** 4 : Connexion physique effectuée. */
    public static int connectePhysiquement = 4;
    /** 5 : Attente d'une réponse au WELCOME. */
    public static int attenteRESULTWELCOME = 5;
    /** 6 : Pret pour travailler. */
    public static int pret = 6;
    /** 7 : Attente d'une réponse au BYE. */
    public static int attenteRESULTBYE = 7;
    /** 8 : Non connecté physiquement. */
    public static int nonConnecte = 8;
    /** 9 : Action choisie. */
    public static int actionChoisie = 9;
    /** 10 : Attente de réponse à la demande de vente. */
    public static int attentePropositionEnchere = 10;
    /** 11 : Attente du déclenchement de la vente. */
    public static int attenteDeclenchementEnchere = 11;
    /** 12 : Debut du mode enchère. */
    public static int modeEnchere = 12;
    /** 13 : Attente du résultat de sa vente. */
    public static int attenteRESULTATdeSaVente = 13;
    /** 14 : Enchère interessante. */
    public static int enchereInteressante = 14;
    /** 15 : L'enchere est une EnchèreUn. */
    public static int enchereUnOuQuatre = 15;
    /**
     * 16 : L'enchere est une EnchèreDeux ou une enchère cinq car on gère de la
     * meme facon.
     */
    public static int enchereDeuxOuCinq = 16;
    /** 17 : L'enchere est une EnchèreTrois. */
    public static int enchereTrois = 17;
    /** L'état courant. */
    private int etat;

    /** Constructeur. */
    public Etat(int etat) {
        this.etat = etat;
    }

    /** Méthodes. */
    /** Renvoie l'état codé sous forme d'entier. */
    public int getEtat() {
        return this.etat;
    }

    /** Modifie la valeur de l'état. */
    public void setEtat(int etat) {
        this.etat = etat;
    }

    /** Méthode d'affichage qui présente le code et la signification de l'état. */
    public String toString(int decalage) {
        String delta = "";
        for (int i = 0; i < decalage; i++)
            delta += " ";
        String output = delta + String.valueOf(this.etat) + " (";
        switch (this.etat) {
        case 0:
            output += "quitter";
            break;
        case 1:
            output += "initialisé";
            break;
        case 2:
            output += "connait les pdms actives";
            break;
        case 3:
            output += "pdm choisie dans la liste des actives";
            break;
        case 4:
            output += "connexion physique effectuée";
            break;
        case 5:
            output += "attente d'une réponse au WELCOME";
            break;
        case 6:
            output += "pret pour travailler";
            break;
        case 7:
            output += "attente d'une réponse au BYE";
            break;
        case 8:
            output += "non connecté physiquement";
            break;
        case 9:
            output += "action choisie";
            break;
        case 10:
            output += "attente de réponse à la demande de vente";
            break;
        case 11:
            output += "attente du déclenchement de la vente";
            break;
        case 12:
            output += "debut du mode enchère";
            break;
        case 13:
            output += "attente du résultat de sa vente";
            break;
        case 14:
            output += "enchère interessante";
            break;
        case 15:
            output += "enchère à prendre ou à laisser ou enchère descendante";
            break;
        case 16:
            output += "enchère à pli scellé ou enchère de Vickrey";
            break;
        case 17:
            output += "enchère ascendante";
            break;
        // case 18: output += "enchère descendante"; break;
        default:
            output += "enchère non gérée";
            break;
        }
        return output + ")";
    }

    /** Vrai si l'état nécessite une synchronisation. */
    public boolean isWaiting() {
        return (/*
                 * etat == pret ||
                 */etat == modeEnchere || etat == enchereInteressante || etat == enchereUnOuQuatre
                || etat == enchereDeuxOuCinq || etat == enchereTrois || etat == actionChoisie);
    }

    /**
     * Renvoie vrai si l'état accepte les messages asynchrones c'est à dire les
     * messages resultprogramme, resultagent et resultat. 6 7 10 11 12 13 14 15
     * 16 17 18
     */
    public boolean acceptAsynchronus() {
        return (etat == 6 || etat == 7 || etat == 10 || etat == 11 || etat == 12 || etat == 13 || etat == 14
                || etat == 15 || etat == 16 || etat == 17 || etat == 9);
    }
}
