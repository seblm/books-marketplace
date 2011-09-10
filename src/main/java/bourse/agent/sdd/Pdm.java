package bourse.agent.sdd;

import bourse.reseau.*;

/** Stocke les données relatives à une pdm.
 *  Par exemple lors de la récupération de la liste des pdms actives.*/
public class Pdm {
    
    /** Variables d'instance. */
    /** Le nom de la pdm (nom du groupe). */
    private String nom;
    /** L'adresse. */
    private Ip adresse;
    
    /** Constructeurs. */
    /** Constructeur de pdm. */
    public Pdm(String nom, String adresse) {
        this.nom = nom; this.adresse = new Ip(adresse);
    }
    
    /** Méthodes. */ 
    /** Renvoie le nom de la pdm (nom du groupe). */
    public String getNom() { return this.nom; }
    /** Modifie le nom. */
    public void setNom(String nom) { this.nom = nom; }
    /** Renvoie l'adresse. */
    public Ip getAdresse() { return this.adresse; }
    /** Modifie l'état d'activité. */
    public void setAdresse(Ip adresse) { this.adresse = adresse; }
    /** Méthode d'affichage. */
    public String toString(int decalage) {
        String delta = "";
        for (int i=0; i<decalage; i++) delta += " ";
        return delta + "nom = " + this.nom + ", adresse = [" + this.adresse.toString(0) + "]";
    }
    /** Méthode de test. */
    public static void main(String argc[]) {
        Pdm p = new Pdm("Groupe-E", "192.168.1.2:8080");
        System.out.println(p.toString(5));
    }
}
