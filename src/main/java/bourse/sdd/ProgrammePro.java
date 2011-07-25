/*
 * ProgrammePro.java
 *
 * Created on 20 janvier 2004, 10:07
 */

package bourse.sdd;

import bourse.placeDeMarche.enchere.Enchere;

/**
 *
 * @author  pechot
 */
public class ProgrammePro {
    
    private int numEnchere;
    public Livre livre;
    private int typeEnchere;
    private float prixVente;
    /** Creates a new instance of ProgrammePro */
    
    /** Constructeur appelé lorsque le type de l'enchère n'est pas connue. */
    public ProgrammePro(int num, Livre livre) {
        this.numEnchere=num;
        this.livre=livre;
        this.typeEnchere = Enchere.ENCHERE_INCONNUE;
        this.prixVente = (float)0;
    }
    
    /** Constructeur appelé lorsque l'agent propose une vente. */
    public ProgrammePro(int num, Livre livre, int typeEnchere, float prixVente) {
        this.numEnchere=num;
        this.livre=livre;
        this.typeEnchere = typeEnchere;
        this.prixVente = prixVente;
    }
    
    public int getNum(){
        return this.numEnchere;
    }
    
    public float getPrixVente(){
        return this.prixVente;
    }
    
    public int getTypeEnchere(){
        return this.typeEnchere;
    }
    
    public Livre getLivre(){
        return this.livre;
    }
    
    public void setLivre(Livre livre){
        this.livre=livre;
    }
    
    public void setNumEnchere(int num){
        this.numEnchere=num;
    }
    public void decrementerNumEnchere() { numEnchere--; }
    public void incrementerNumEnchere() { numEnchere++; }
    /** Méthode d'affichage qui décale l'affichage de décalage. */
    public String toString(int decalage) {
        String delta = "";
        for (int i=0; i<decalage; i++) delta += " ";
        return delta + "numéro = " + this.numEnchere + ", livre = [ " + this.livre.toString(0) + " ]";
    }
    /** Programme principale. */
    public static void main(String argc[]) {
        Livre l1 = new Livre("l1", "a2", new bourse.protocole.Categorie(), "poche", "O'reilly", (float)50.65, (float)0.45, "2004-01-01", "222222222", 1, "", (float)0.0);
        ProgrammePro p = new ProgrammePro(34, l1);
        System.out.println(p.toString(5));
    }
}
