package bourse.placeDeMarche.enchere;

import bourse.sdd.*;
import bourse.protocole.*;
import bourse.placeDeMarche.*;
import java.util.Random;

public abstract class Enchere {
    
    protected static final float POURCENTAGE_PAS = (float).1;
    
    /** Ench�re inconnue */
    public static final int ENCHERE_INCONNUE = 0;
    /** Ench�re � prendre ou � laisser : le prix est d�termin� et chaque acheteur
     * r�ponds par oui ou non. L'objet peut ne pas �tre vendu. */
    public static final int ENCHERE_UN = 1;
    /** Ench�re � prix scell� : chaque acheteur fait une seule proposition et
     * l'objet est vendu au plus offrant. */
    public static final int ENCHERE_DEUX = 2;
    /** Ench�re ascendante : c'est l'ench�re classique o� les agents surench�ris-
     * sent de fa�on concurrente. L'objet est vendu au plus offrant. */
    public static final int ENCHERE_TROIS = 3;
    /** Ench�re descendante : la place de march� commence par un prix �lev� qui
     * est baiss� progressivement jusqu'� ce qu'un agent l'accepte. */
    public static final int ENCHERE_QUATRE = 4;
    /** Ench�re de Vickrey : chaque acheteur fait une seule proposition et l'objet
     * est vendu au prix de la seconde meilleure offre. */
    public static final int ENCHERE_CINQ = 5;
    
    /** @return le type de l'ench�re (dans le protocole). */
    public static final String[] NOM = {"EnchereInconnue", "EnchereUn", "EnchereDeux", "EnchereTrois", "EnchereQuatre", "EnchereCinq"};
    
    public String typeEnchereToString(int type) {
        switch (type) {
            case 1 : return "� prendre ou � laisser";
            case 2 : return "� prix scell�";
            case 3 : return "ascendante";
            case 4 : return "descendante";
            case 5 : return "de Vickrey";
            default : return "inconnue";
        }
    }
    
    protected static Random generateurAleatoire = new Random();
    private Requetes requetes = null;
    String vendeur;
    
    public int vendeurPresent(String nomPdm) {
        return (!vendeur.equalsIgnoreCase(nomPdm))?1:0;
    }
    
    private static int choisirTypeEnchere() { return Enchere.generateurAleatoire.nextInt(5) + 1; }
    /** Cette m�thode statique est appel�e par la place de march� lorsqu'elle veut
     * vendre un nouveau livre. */
    public static final Enchere newInstance(int numEnchere, Livre livre) {
        int typeAleatoire = Enchere.choisirTypeEnchere();
        switch (typeAleatoire) {
            case Enchere.ENCHERE_UN : return new EnchereUn(numEnchere, livre);
            case Enchere.ENCHERE_DEUX : return new EnchereDeux(numEnchere, livre);
            case Enchere.ENCHERE_TROIS : return new EnchereTrois(numEnchere, livre);
            case Enchere.ENCHERE_QUATRE : return new EnchereQuatre(numEnchere, livre);
            case Enchere.ENCHERE_CINQ : return new EnchereCinq(numEnchere, livre);
            default : return null;
        }
    }
    
    public static final Enchere newInstance(int numEnchere, float prixVente, int idLivre, int typeEnchere) {
        switch (typeEnchere) {
            case Enchere.ENCHERE_UN : return new EnchereUn(numEnchere, prixVente, idLivre);
            case Enchere.ENCHERE_DEUX : return new EnchereDeux(numEnchere, prixVente, idLivre);
            case Enchere.ENCHERE_TROIS : return new EnchereTrois(numEnchere, prixVente, idLivre);
            case Enchere.ENCHERE_QUATRE : return new EnchereQuatre(numEnchere, prixVente, idLivre);
            case Enchere.ENCHERE_CINQ : return new EnchereCinq(numEnchere, prixVente, idLivre);
            default : return null;
        }
    }
    
    protected int numEnchere;
    protected Livre livre;
    /** Le type de l'ench�re courante. Doit-�tre une valeur comprise dans l'en-
     * semble Enchere.ENCHERE_{UN, ... CINQ} */
    protected int typeEnchere;
    protected float prixCourant;
    /** Nom de l'agent ayant effectu� la plus grosse ench�re jusqu'ici, rien si
     * c'est le premier tour (uniquement en cas d'ench�re montante). */
    protected String nomAgent;

    /** Constructeur utilis� lorsque la place de march� est vendeuse. */
    protected Enchere(int numEnchere, Livre livre) {
        try { this.requetes = new Requetes(true); } catch (Exception e) {
            // Il s'agit d'une erreur fatale.
            System.err.println("Enchere : erreur lors de l'instantiation de Requetes.\n" + e);
            System.exit(1);
        }
        this.vendeur = livre.getProprietaire();
        this.numEnchere = numEnchere;
        this.livre = livre;
        this.prixCourant = livre.getPrix() * livre.getEtat();
    }
    
    /** Constructeur utilis� lorsque la place de march� re�oit une proposition de
     * vente. */
    protected Enchere(int numEnchere, float prixVente, int idLivre) {
        try { this.requetes = new Requetes(true); } catch (Exception e) {
            // Il s'agit d'une erreur fatale.
            System.err.println("Enchere : erreur lors de l'instantiation de Requetes.\n" + e);
            System.exit(1);
        }
        this.livre = new Livre(requetes.getLivre(idLivre));
        this.vendeur = livre.getProprietaire();
        this.numEnchere = numEnchere;
        this.prixCourant = prixVente;
    }
    
    /** Une ench�re en un tour se d�roule de la mani�re suivante :
     * La place march� envoie � tous les agents une ench�re et s'endort jusqu'�
     * la fin de son timeout. De leur c�t�, les agents r�pondent ou pas.
     * Lors du r�veil de la place de march�, celle-ci d�termine le vainqueur de
     * l'ench�re s'il y en a un en suivant les r�gles propres au type de l'en-
     * ch�re annonc�. */
    public boolean estEnchereReponseUnique() { return this.typeEnchere == Enchere.ENCHERE_UN || this.typeEnchere == Enchere.ENCHERE_DEUX || this.typeEnchere == Enchere.ENCHERE_CINQ; }

    /** Une ench�re de type boucle it�re ses annonces d'ench�res jusqu'� ce qu'un 
     * agent signale qu'il d�sire l'objet. */
    public boolean estEnchereReponseBoucle() { return this.typeEnchere == Enchere.ENCHERE_QUATRE; }

    /** Une place de march� qui met en place une ench�re en plusieurs tours com-
     * mence par annoncer l'ench�re aux puis s'endort jusqu'� son r�veil provo-
     * qu� par :
     * - un message de la part d'un agent d�sirant ench�rir ;
     * - le timeout.
     * Dans le premier cas, l'ench�re actualise ses donn�es, renvoie un message
     * � tous les autres agents et se rendort ; dans le second, elle se termine
     * et envoie le r�sultat de la transaction. */
    public boolean estEnchereReponseMultiple() { return this.typeEnchere == Enchere.ENCHERE_TROIS; }

    public float getPas() { return prixCourant * Enchere.POURCENTAGE_PAS; }
    public float getPrixDepart() { return livre.getPrix() * livre.getEtat() * (1 + Enchere.POURCENTAGE_PAS); }
    public int getNumEnchere() { return this.numEnchere; }
    public float getPrixCourant() { return this.prixCourant; }
    public void setPrixCourant(float prixCourant) { this.prixCourant = prixCourant; }
    public int getTypeEnchere() { return this.typeEnchere; }
    public String getVendeur() { return this.vendeur; }
    public void setVendeur(String vendeur) { this.vendeur = vendeur; }
    public Livre getLivre() { return this.livre; }
    public void setLivre(Livre livre) { this.livre = livre; }

    public void decrementerNumEnchere() { numEnchere--; }
    protected void incrementerNumEnchere() { numEnchere++; }
        
    public abstract PropositionEnchereP annonce();
    /** Utilis� lorsqu'un agent se connecte "au milieu" d'une ench�re. */
    public abstract PropositionEnchereP reAnnonce();
    public abstract Resultat resolution();
    
    public String toHtml() {
        return "N� " + this.numEnchere + " : Ench�re " + this.typeEnchereToString(this.typeEnchere) + " sur " + livre.toHtml();
    }
   
}