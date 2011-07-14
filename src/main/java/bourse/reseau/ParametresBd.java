package bourse.reseau;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.IOException;

public final class ParametresBd implements java.io.Serializable {
    
    private static final String nomFichier = "parametresBd.obj";
    
    private String hote = "sqlhost";
    private int port = 3306;
    private String utilisateur = "guillon";
    private String motDePasse = "lmi2";
    private String baseDeDonnees = "bd_guillon";
    
    public String getHote() { return hote; }
    public int getPort() { return port; }
    public String getUtilisateur() { return utilisateur; }
    public String getMotDePasse() { return motDePasse; }
    public String getBaseDeDonnees() { return baseDeDonnees; }
    
    /** Crée une nouvelle instance de ParametresBd. */
    public ParametresBd() {
        charge();
    }
    
    /** Fixe tous les paramètres. */
    public void setParametres(String hote, int port, String utilisateur, String motDePasse, String baseDeDonnees) {
        this.hote = hote;
        this.port = port;
        this.utilisateur = utilisateur;
        this.motDePasse = motDePasse;
        this.baseDeDonnees = baseDeDonnees;
    }
    
    /** Charge l'instance de l'objet dans le fichier. */
    public void charge() {
        try {
            ObjectInputStream s = new ObjectInputStream(new FileInputStream(nomFichier));
            ParametresBd temp = (ParametresBd)s.readObject();
            s.close();
            this.setParametres(temp.getHote(), temp.getPort(), temp.getUtilisateur(), temp.getMotDePasse(), temp.getBaseDeDonnees());
        } catch (Exception e) {
            // Il y a eu un problème lors de la lecture du fichier. Il faut le réenregistrer.
            System.err.println(e);
            sauvegarde();
        }
    }
    
    /** Sauvegarde l'instance dans le fichier. */
    public void sauvegarde() {
        try {
            ObjectOutputStream s = new ObjectOutputStream(new FileOutputStream(nomFichier));
            s.writeObject(this);
            s.flush();
            s.close();
        } catch (Exception e) {
            // Impossible d'enregistrer l'instance.
            System.err.println(e);
        }
    }
    
    /** @return les données de l'instance. */
    public String toString() {
        return "ParametresBd : utilisateur " + utilisateur + " sur le serveur " + hote + ":" + port + " et sur la base " + baseDeDonnees;
    }
    
    /** Teste la classe ParametresBd. */
    public static void main(String args[]) {
        // Instanciation
        ParametresBd parametresBd = new ParametresBd();
        System.out.println(parametresBd);
        // Modification
        parametresBd.setParametres("harukiya", 3306, "elemerdy", "devede", "bal");
        System.out.println(parametresBd);
        // Sauvegarde
        parametresBd.sauvegarde();
        // Instanciation
        parametresBd = new ParametresBd();
        System.out.println(parametresBd);
        // Chargement
        parametresBd.charge();
        System.out.println(parametresBd);
    }
    
}
