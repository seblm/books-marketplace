package bourse.placeDeMarche.enchere;

import bourse.sdd.*;
import bourse.protocole.*;
import bourse.placeDeMarche.*;
import java.util.Random;

public abstract class Enchere {

    protected static final float POURCENTAGE_PAS = (float) .1;

    /** Enchère inconnue */
    public static final int ENCHERE_INCONNUE = 0;
    /**
     * Enchère à prendre ou à laisser : le prix est déterminé et chaque acheteur
     * réponds par oui ou non. L'objet peut ne pas être vendu.
     */
    public static final int ENCHERE_UN = 1;
    /**
     * Enchère à prix scellé : chaque acheteur fait une seule proposition et
     * l'objet est vendu au plus offrant.
     */
    public static final int ENCHERE_DEUX = 2;
    /**
     * Enchère ascendante : c'est l'enchère classique où les agents surenchéris-
     * sent de façon concurrente. L'objet est vendu au plus offrant.
     */
    public static final int ENCHERE_TROIS = 3;
    /**
     * Enchère descendante : la place de marché commence par un prix élevé qui
     * est baissé progressivement jusqu'à ce qu'un agent l'accepte.
     */
    public static final int ENCHERE_QUATRE = 4;
    /**
     * Enchère de Vickrey : chaque acheteur fait une seule proposition et
     * l'objet est vendu au prix de la seconde meilleure offre.
     */
    public static final int ENCHERE_CINQ = 5;

    /** @return le type de l'enchère (dans le protocole). */
    public static final String[] NOM = { "EnchereInconnue", "EnchereUn", "EnchereDeux", "EnchereTrois",
            "EnchereQuatre", "EnchereCinq" };

    public String typeEnchereToString(int type) {
        switch (type) {
        case 1:
            return "à prendre ou à laisser";
        case 2:
            return "à prix scellé";
        case 3:
            return "ascendante";
        case 4:
            return "descendante";
        case 5:
            return "de Vickrey";
        default:
            return "inconnue";
        }
    }

    protected static Random generateurAleatoire = new Random();
    private Requetes requetes = null;
    String vendeur;

    public int vendeurPresent(String nomPdm) {
        return (!vendeur.equalsIgnoreCase(nomPdm)) ? 1 : 0;
    }

    private static int choisirTypeEnchere() {
        return Enchere.generateurAleatoire.nextInt(5) + 1;
    }

    /**
     * Cette méthode statique est appelée par la place de marché lorsqu'elle
     * veut vendre un nouveau livre.
     */
    public static final Enchere newInstance(int numEnchere, Livre livre) {
        int typeAleatoire = Enchere.choisirTypeEnchere();
        switch (typeAleatoire) {
        case Enchere.ENCHERE_UN:
            return new EnchereUn(numEnchere, livre);
        case Enchere.ENCHERE_DEUX:
            return new EnchereDeux(numEnchere, livre);
        case Enchere.ENCHERE_TROIS:
            return new EnchereTrois(numEnchere, livre);
        case Enchere.ENCHERE_QUATRE:
            return new EnchereQuatre(numEnchere, livre);
        case Enchere.ENCHERE_CINQ:
            return new EnchereCinq(numEnchere, livre);
        default:
            return null;
        }
    }

    public static final Enchere newInstance(int numEnchere, float prixVente, int idLivre, int typeEnchere) {
        switch (typeEnchere) {
        case Enchere.ENCHERE_UN:
            return new EnchereUn(numEnchere, prixVente, idLivre);
        case Enchere.ENCHERE_DEUX:
            return new EnchereDeux(numEnchere, prixVente, idLivre);
        case Enchere.ENCHERE_TROIS:
            return new EnchereTrois(numEnchere, prixVente, idLivre);
        case Enchere.ENCHERE_QUATRE:
            return new EnchereQuatre(numEnchere, prixVente, idLivre);
        case Enchere.ENCHERE_CINQ:
            return new EnchereCinq(numEnchere, prixVente, idLivre);
        default:
            return null;
        }
    }

    protected int numEnchere;
    protected Livre livre;
    /**
     * Le type de l'enchère courante. Doit-être une valeur comprise dans l'en-
     * semble Enchere.ENCHERE_{UN, ... CINQ}
     */
    protected int typeEnchere;
    protected float prixCourant;
    /**
     * Nom de l'agent ayant effectué la plus grosse enchère jusqu'ici, rien si
     * c'est le premier tour (uniquement en cas d'enchère montante).
     */
    protected String nomAgent;

    /** Constructeur utilisé lorsque la place de marché est vendeuse. */
    protected Enchere(int numEnchere, Livre livre) {
        try {
            this.requetes = new Requetes(true);
        } catch (Exception e) {
            // Il s'agit d'une erreur fatale.
            System.err.println("Enchere : erreur lors de l'instantiation de Requetes.\n" + e);
            System.exit(1);
        }
        this.vendeur = livre.getProprietaire();
        this.numEnchere = numEnchere;
        this.livre = livre;
        this.prixCourant = livre.getPrix() * livre.getEtat();
    }

    /**
     * Constructeur utilisé lorsque la place de marché reçoit une proposition de
     * vente.
     */
    protected Enchere(int numEnchere, float prixVente, int idLivre) {
        try {
            this.requetes = new Requetes(true);
        } catch (Exception e) {
            // Il s'agit d'une erreur fatale.
            System.err.println("Enchere : erreur lors de l'instantiation de Requetes.\n" + e);
            System.exit(1);
        }
        this.livre = new Livre(requetes.getLivre(idLivre));
        this.vendeur = livre.getProprietaire();
        this.numEnchere = numEnchere;
        this.prixCourant = prixVente;
    }

    /**
     * Une enchère en un tour se déroule de la manière suivante : La place
     * marché envoie à tous les agents une enchère et s'endort jusqu'à la fin de
     * son timeout. De leur côté, les agents répondent ou pas. Lors du réveil de
     * la place de marché, celle-ci détermine le vainqueur de l'enchère s'il y
     * en a un en suivant les règles propres au type de l'en- chère annoncé.
     */
    public boolean estEnchereReponseUnique() {
        return this.typeEnchere == Enchere.ENCHERE_UN || this.typeEnchere == Enchere.ENCHERE_DEUX
                || this.typeEnchere == Enchere.ENCHERE_CINQ;
    }

    /**
     * Une enchère de type boucle itère ses annonces d'enchères jusqu'à ce qu'un
     * agent signale qu'il désire l'objet.
     */
    public boolean estEnchereReponseBoucle() {
        return this.typeEnchere == Enchere.ENCHERE_QUATRE;
    }

    /**
     * Une place de marché qui met en place une enchère en plusieurs tours com-
     * mence par annoncer l'enchère aux puis s'endort jusqu'à son réveil provo-
     * qué par : - un message de la part d'un agent désirant enchérir ; - le
     * timeout. Dans le premier cas, l'enchère actualise ses données, renvoie un
     * message à tous les autres agents et se rendort ; dans le second, elle se
     * termine et envoie le résultat de la transaction.
     */
    public boolean estEnchereReponseMultiple() {
        return this.typeEnchere == Enchere.ENCHERE_TROIS;
    }

    public float getPas() {
        return prixCourant * Enchere.POURCENTAGE_PAS;
    }

    public float getPrixDepart() {
        return livre.getPrix() * livre.getEtat() * (1 + Enchere.POURCENTAGE_PAS);
    }

    public int getNumEnchere() {
        return this.numEnchere;
    }

    public float getPrixCourant() {
        return this.prixCourant;
    }

    public void setPrixCourant(float prixCourant) {
        this.prixCourant = prixCourant;
    }

    public int getTypeEnchere() {
        return this.typeEnchere;
    }

    public String getVendeur() {
        return this.vendeur;
    }

    public void setVendeur(String vendeur) {
        this.vendeur = vendeur;
    }

    public Livre getLivre() {
        return this.livre;
    }

    public void setLivre(Livre livre) {
        this.livre = livre;
    }

    public void decrementerNumEnchere() {
        numEnchere--;
    }

    protected void incrementerNumEnchere() {
        numEnchere++;
    }

    public abstract PropositionEnchereP annonce();

    /** Utilisé lorsqu'un agent se connecte "au milieu" d'une enchère. */
    public abstract PropositionEnchereP reAnnonce();

    public abstract Resultat resolution();

    public String toHtml() {
        return "N° " + this.numEnchere + " : Enchère " + this.typeEnchereToString(this.typeEnchere) + " sur "
                + livre.toHtml();
    }

}