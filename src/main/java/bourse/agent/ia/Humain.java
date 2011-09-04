package bourse.agent.ia;

import static bourse.agent.sdd.Action.aucune;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Iterator;

import bourse.agent.Agent;
import bourse.agent.sdd.Action;
import bourse.agent.sdd.ListeLivre;
import bourse.protocole.ProposeVente;
import bourse.sdd.Livre;

public class Humain extends Decision {
    
    private static final long TIMEOUT = 10000;
    
	/**
	 * Constructeur de descision humaine.
	 */
    public Humain(Agent appelant) {
    	super(appelant);
    }
    
    /**
     * Arbitrairement 10 secondes d'attente.
     */
    public long timeout() {
    	return TIMEOUT;
    }
    
    /**
     * On demande à l'utilisateur d'entrer l'action à réaliser.
     */
    public void choixAction() {
        pere.setAction(aucune);
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        System.out.println(" 0 : aucune (pour répondre à une proposition d'enchère)");
        System.out.println(" 1 : vendre son premier bouquin");
        System.out.println(" 2 : migrer");
        System.out.println(" 3 : bilan");
        System.out.println(" 4 : programme");
        System.out.println(" 5 : agents connectés");
        System.out.println(" 6 : attendre une proposition enchère");
        System.out.print("action ? ");
        try {
            pere.setAction(Action.values()[Integer.parseInt(in.readLine())]);
        } catch (IOException e) {
        	e.printStackTrace(System.err);
        	pere.setAction(aucune);
        }
    }
    
    /**
     * On demande à l'utilsateur d'entrer un prix.
     */
    public float choixPrix() {
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        System.out.print("prix ? ");
        float reponse = 0;
        try {
            reponse = Float.parseFloat(in.readLine());
        } catch (IOException e) {
        	e.printStackTrace(System.err);
        	return reponse;
        }
         return reponse;
    }
    
    /**
     * On déclare à l'utilisateur le prix maximaum fixé.
     */
    public float choixPrixMax() {
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        System.out.print("prix maximum ? ");
        float reponse = 0;
        try {
            reponse = Float.parseFloat(in.readLine());
        } catch (IOException e) {
        	e.printStackTrace(System.err);
        	return reponse;
        }
        System.out.println("prix maximum = " + String.valueOf(reponse));
        return reponse;
    }
    
    /**
     * On demande à l'utilisateur si la vente l'intéresse ou pas.
     */
    public boolean livreInteressant(Livre l, int typeEnchere, int miseAPrix) {
        pere.getEnvironnement().getCourante().setPrixMaximum(this.choixPrixMax());
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        System.out.println(" 1 : oui");
        System.out.println(" 2 : non");
        System.out.print("vente intéressante ? ");
        int reponse = 0;
        try {
            reponse = Integer.parseInt(in.readLine());
        } catch (IOException e) {
        	e.printStackTrace(System.err);
        	return false;
        }
        return reponse == 1;
    }
    
    /**
     * Renvoie vrai si la vente est intéressante.
     */
    public boolean venteInteressante() {
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        System.out.println(" 1 : oui");
        System.out.println(" 2 : non");
        System.out.print("vente intéressante ? ");
        int reponse = 0;
        try {
            reponse = Integer.parseInt(in.readLine());
        } catch (IOException e) {
        	e.printStackTrace(System.err);
        	return false;
        }
        return reponse == 1;
    }
    
    /**
     * C'est l'évaluation de l'intérêt porté par le livre en vente.
     */
    public boolean livreInteressant(Livre l, int typeEnchere, float miseAPrix) {
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        System.out.println(" 1 : oui");
        System.out.println(" 2 : non");
        System.out.print("vente intéressante ? ");
        int reponse = 0;
        try {
            reponse = Integer.parseInt(in.readLine());
        } catch (IOException e) {
        	e.printStackTrace(System.err);
        	return false;
        }
        return reponse == 1;
    }
    
    /**
     * On demande à l'utilisateur les informations nécessaires à la vente de
     * son premier livre.
     */
    public ProposeVente choixLivreAVendre(ListeLivre l) {
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        Livre ouvrage;
        int numero = 1;
        String nom = "Enchere";
        int id;
        int prix = 0;
        Iterator<Livre> parcours = l.getValeurs().iterator();
        if (parcours.hasNext()) {
            ouvrage = (Livre)parcours.next();
            System.out.print("Le numéro de l'enchère : ");
            try {
            	numero = Integer.parseInt(in.readLine());
            } catch (IOException e) {
            	e.printStackTrace(System.err);
            }
            switch(numero) {
                case 1: nom += "Un"; break;
                case 2: nom += "Deux"; break;
                case 3: nom += "Trois"; break;
                case 4: nom += "Quatre"; break;
                case 5: nom += "Cinq"; break;
                default: nom += "Un"; break;
            }
            id = ouvrage.getId();
            System.out.print("Le prix proposé : ");
            try {
            	prix = Integer.parseInt(in.readLine());
            } catch (IOException e) {
            	e.printStackTrace(System.err);
            }
            return new ProposeVente(nom, prix, id);
        } else {
        	return null;
        }
    }
} 
