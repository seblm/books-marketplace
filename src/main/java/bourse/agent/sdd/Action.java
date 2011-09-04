package bourse.agent.sdd;

import java.util.Random;

/**
 * Répertorie les actions possibles
 */
public enum Action {

    /**
     * Aucune action séléctionnée.
     */
    aucune,

    /**
     * Vendre son bouquin.
     */
    vendre,

    /**
     * Migration.
     */
    migrer,

    /**
     * Effectuer son bilan.
     */
    bilan,

    /**
     * Demander le programme.
     */
    programme,

    /**
     * Demander la liste des agents connectés.
     */
    adversaires,

    /**
     * Attendre l'enchère.
     */
    attenteEnchere,

    /**
     * Réalise l'objectif.
     */
    objectif;

    /**
     * Construit une action au hasard.
     */
    public static Action randomAction() {
        return Action.values()[new Random().nextInt(6)];
    }

    /**
     * Affichage.
     */
    public String toString() {
        switch (this) {
        case aucune:
            return "aucune action séléctionnée";
        case vendre:
            return "vendre son bouquin";
        case migrer:
            return "migration";
        case bilan:
            return "effectuer son bilan";
        case programme:
            return "demande de programme";
        case adversaires:
            return "demande de la liste des agents présents";
        case attenteEnchere:
            return "attente d'une proposition enchère";
        case objectif:
            return "réaliser l'objectif";
        default:
            return "";
        }
    }

}
