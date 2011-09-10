package bourse.agent.sdd;

import bourse.sdd.*;

/** Cette classe stocke les données relatives à une enchère. */
public class Enchere {
    /** le duree restante avant la cloture de ce tour */
    private int temps;
    /** le pas de l'enchere */
    private float pas;
    /** le nom de l'enchere */
    private int type;
    /** le nom de l'agent ayant effectué la plus grosse enchere, rien sinon */
    private String nomagent;
    /** numero representant le tour de l'enchere */
    private int numeroEnchere;
    /** Le livre qui concerne l'enchere. */
    private Livre livre;
    /** La valeur de la derniere enchere, a deffaut le prix de depart. */
    private float valeurEnchere;
    /** Le prix maximun évalué par l'agent. */
    private float prixMaximum = 0;

    /** Constructeurs. */
    /** Construit une enchère entièrement définie. */
    public Enchere(int num, Livre li, float val, int tps, float pas, int type, String nomag) {
        this.numeroEnchere = num;
        this.livre = new Livre(li);
        this.valeurEnchere = val;
        this.type = type;
        this.nomagent = nomag;
        this.pas = pas;
        this.temps = tps;
    }

    /** Constructeur par défaut. */
    public Enchere() {
        numeroEnchere = 1;
        valeurEnchere = (float) 0;
        livre = new Livre();
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

    /** Méthodes. */
    /** Méthode d'affichage. */
    public String toString(int decalage) {
        String delta = "";
        for (int i = 0; i < decalage; i++)
            delta += " ";
        return delta + "tour = " + this.numeroEnchere + ", type = " + this.type + ", temps = " + this.temps
                + ", pas = " + this.pas + ", enchérisseur = " + this.nomagent + ", prix = " + this.valeurEnchere
                + ", prix maximum = " + this.prixMaximum + ", livre = " + "\n" + this.livre.toString(decalage + 1);
    }

    public static int enchereToCode(String typeEnchere) {
        if (typeEnchere.equalsIgnoreCase("EnchereUn"))
            return 1;
        else if (typeEnchere.equalsIgnoreCase("EnchereDeux"))
            return 2;
        else if (typeEnchere.equalsIgnoreCase("EnchereTrois"))
            return 3;
        else if (typeEnchere.equalsIgnoreCase("EnchereQuatre"))
            return 4;
        else if (typeEnchere.equalsIgnoreCase("EnchereCinq"))
            return 5;
        else
            return 0;
    }

    /** La méthode main de test. */
    public static void main(String[] argc) {
        bourse.sdd.Livre l1 = new bourse.sdd.Livre("l1", "a2", new bourse.protocole.Categorie(), "poche", "O'reilly",
                (float) 50.65, (float) 0.45, "2004-01-01", "222222222", 1, "Seb", (float) 65);
        bourse.sdd.Livre l2 = new bourse.sdd.Livre("l2", "a1", new bourse.protocole.Categorie(), "poche", "Casterman",
                (float) 40.75, (float) 0.85, "1954-04-12", "222XX2254", 2, "protocoleman", (float) 50);
        bourse.sdd.Livre l3 = new bourse.sdd.Livre("l2", "a1", new bourse.protocole.Categorie(), "poche", "Casterman",
                (float) 40.75, (float) 0.27, "1954-04-12", "222XX2254", 3, "arno", (float) 12);
        Enchere e1 = new Enchere(1, l1, (float) 19.81, 12, (float) 1.2, 3, "a1");
        Enchere e2 = new Enchere(2, l2, (float) 20.00, 13, (float) 1.2, 3, "a1");
        Enchere e3 = new Enchere(3, l3, (float) 10.01, 14, (float) 1.2, 3, "a1");
        System.out.println(e1.toString(4));
        System.out.println(e2.toString(4));
        System.out.println(e3.toString(4));
    }
}
