/*
 * PDMPro.java
 *
 * Created on January 20, 2004, 7:56 AM
 */

package bourse.sdd;

/**
 * Cette classe est spécialement étudiée pour stoquer la liste des places de marché
 * et de leur adresse respective, lorsqu'un agent se déconnecte d'une place de
 * marché.
 * @author  pechot
 */
public class PDMPro {
    
    
        private String nom;
        private String adresse;
        /** Creates a new instance of PDMPro */
        public PDMPro(){}
        public PDMPro(String nom,String adresse){
            this.nom=nom;
            this.adresse=adresse;
        }
        
        public void setNom(String nom){
            this.nom=nom;
        }
        
        public void setAdresse(String adresse){
            this.adresse=adresse;
        }
        
        public String getNom(){
            return this.nom;
        }
        
        public String getAdresse(){
            return this.adresse;
        }
        
    
}
