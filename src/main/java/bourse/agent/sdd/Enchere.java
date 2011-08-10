package bourse.agent.sdd;

import bourse.sdd.Livre;

/**
 * Cette classe stocke les données relatives à une enchère.
 */
public class Enchere {

    /**
     * la durée restante avant la cloture de ce tour
     */
    private int temps;

    /**
     * le pas de l'enchere
     */
    private float pas;

    /**
     * le nom de l'enchere
     */
    private int type;

    /**
     * le nom de l'agent ayant effectué la plus grosse enchere, rien sinon
     */
    private String nomagent;

    /**
     * numéro représentant le tour de l'enchère
     */
    private int numeroEnchere;

    /**
     * Le livre qui concerne l'enchère.
     */
    private Livre livre;

    /**
     * La valeur de la dernière enchere, à défaut le prix de départ.
     */
    private float valeurEnchere;

    /**
     * Le prix maximun évalué par l'agent.
     */
    private float prixMaximum = 0;

    /**
     * Construit une enchère entièrement définie.
     */
    public Enchere(int num, Livre li, float val, int tps, float pas, int type, String nomag) {
        this.numeroEnchere = num;
        this.livre = new Livre(li);
        this.valeurEnchere = val;
        this.type = type;
        this.nomagent = nomag;
        this.pas = pas;
        this.temps = tps;
    }

    /**
     * Constructeur par défaut.
     */
    public Enchere() {
        numeroEnchere = 1;
        valeurEnchere = (float) 0;
        livre = new Livre("", "", null, "", "", 0, 0, "", "", 0, "", 0);
    }

    public Enchere(bourse.agent.sdd.Enchere enchere) {
        this.numeroEnchere = enchere.getNumeroEnchere();
        this.livre = enchere.getLivre();
        this.valeurEnchere = enchere.getValeurEnchere();
        this.type = enchere.getType();
        this.nomagent = enchere.getAgent();
        this.temps = enchere.getTemps();
        this.pas = enchere.getPas();
    }

    public int getTemps() {
        return this.temps;
    }

    public int getType() {
        return this.type;
    }

    public String getAgent() {
        return this.nomagent;
    }

    public int getNumeroEnchere() {
        return this.numeroEnchere;
    }

    public Livre getLivre() {
        return this.livre;
    }

    public float getValeurEnchere() {
        return this.valeurEnchere;
    }

    public float getPas() {
        return this.pas;
    }

    public void setPrixMaximum(float _prixMaximum) {
        prixMaximum = _prixMaximum;
    }

    public float getPrixMaximum() {
        return prixMaximum;
    }

    /**
     * Méthode d'affichage.
     */
    public String toString(int decalage) {
        final StringBuilder out = new StringBuilder();
        for (int i = 0; i < decalage; i++) {
            out.append(" ");
        }
        out.append("tour = ").append(numeroEnchere);
        out.append(", type = ").append(type);
        out.append(", temps = ").append(temps);
        out.append(", pas = ").append(pas);
        out.append(", enchérisseur = ").append(nomagent);
        out.append(", prix = ").append(valeurEnchere);
        out.append(", prix maximum = ").append(prixMaximum);
        out.append(", livre = \n").append(livre.toString(decalage + 1));
        return out.toString();
    }

    public static int enchereToCode(String typeEnchere) {
        if (typeEnchere.equalsIgnoreCase("EnchereUn")) {
            return 1;
        } else if (typeEnchere.equalsIgnoreCase("EnchereDeux")) {
            return 2;
        } else if (typeEnchere.equalsIgnoreCase("EnchereTrois")) {
            return 3;
        } else if (typeEnchere.equalsIgnoreCase("EnchereQuatre")) {
            return 4;
        } else if (typeEnchere.equalsIgnoreCase("EnchereCinq")) {
            return 5;
        } else {
            return 0;
        }
    }

}
