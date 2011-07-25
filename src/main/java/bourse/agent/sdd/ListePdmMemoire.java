package bourse.agent.sdd;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import bourse.agent.Visualisation;

/**
 * Donne toutes les pdms que l'agent connait
 */
public class ListePdmMemoire {
    
    /**
     * Liste des pdms.
     */
    private Map<String, PdmMemoire> liste;
    
    /**
     * Lien vers la fenetre centrale.
     */
    private Visualisation fenetre;
    
    /**
     * Construit une liste de pdms vide.
     */
    public ListePdmMemoire(Visualisation fenetre) {
        liste = new HashMap<String, PdmMemoire>();
        this.fenetre = fenetre;
    }
    
    /**
     * Retourne la liste.
     */
    public Map<String, PdmMemoire> getListe() {
    	return liste;
    }
    
    /** Rafraichir l'affichage de la liste. */
    public void refresh() {
        int numeroLigne = 0;
        // Réinstanciation du JTable pour que sa taille soit celle de la mémoire.
        DefaultTableModel tm = new DefaultTableModel(
            new String [] {"Nom", "Adresse", "Active", "Visitée", "Enchères gérées", "Numero du tour"},
            liste.size()
        );
        JTable tableau = new JTable(tm);
        for (PdmMemoire pdmMemoire : liste.values()) {
            pdmMemoire.toRow(tableau, numeroLigne);
            numeroLigne++;
        }
        fenetre.getJScrollPanePdmMemoire().remove(fenetre.getTableauPdmMemoire());
        fenetre.getJScrollPanePdmMemoire().setViewportView(tableau);
    }
    
    /**
     * Ajouter une pdm.
     */
    public void ajouter(PdmMemoire pdm) {
    	liste.put(pdm.getNom(), pdm);
    }
    
    /**
     * Supprimer une pdm.
     */
    public void supprimer(PdmMemoire pdm) {
    	liste.remove(pdm.getNom());
    }
    
    /**
     * Accès à une pdm de la liste à partir de son nom.
     */
    public PdmMemoire acceder(String nom) {
    	return liste.get(nom);
    }
    
    /**
     * Renvoie vrai si la pdm existe dans la liste.
     */
    protected boolean existe(String nom) {
    	return liste.containsKey(nom);
    }
    
    /**
     * Renvoie vrai si aucune pdm n'est active dans la liste.
     */
    public boolean aucuneActive() {
        Iterator<PdmMemoire> parcours = liste.values().iterator();
        while (parcours.hasNext()) {
            if (parcours.next().getActive()) {
            	return false;
            }
        }
        return true;
    }
    
    /**
     * Renvoie la première pdm active non visitée, null sinon.
     */
    public PdmMemoire premiereActiveNonVisitee() {
        Iterator<PdmMemoire> parcours = liste.values().iterator();
        PdmMemoire current;
        while (parcours.hasNext()) {
            current = parcours.next();
            if (current.getActive() && !current.getVisitee()) {
            	return current;
            }
        }
        return null;
    }
    
    /**
     * Renvoie une autre pdm active visitée.
     */
    public PdmMemoire premiereActiveVisitee() {
        // copie des pdms actives et visitées
        List<PdmMemoire> copiePdmActives = new LinkedList<PdmMemoire>();
        for (PdmMemoire current : liste.values()) {
            if (current.getActive() && current.getVisitee()) {
            	copiePdmActives.add(current);
            }
        }
        // Sélection au hasard d'une pdm active et visitée
        int index = new Random().nextInt(copiePdmActives.size());
        return copiePdmActives.get(index);
    }
    
    /**
     * Change tous les états d'activation de la liste. Méthode dangeureuse.
     */
    private void setActivee(boolean activee) {
        for (final PdmMemoire pdmMemoire : liste.values()) {
            pdmMemoire.setActive(activee);
        }
    }
    
    /**
     * Renvoie vrai si la liste est vide.
     */
    public boolean estVide() {
    	return liste.isEmpty();
    }
    
    /**
     * Méthode de mise à jour globale à partir d'une liste de pdm de l'environnement.
     * Typiquement : après avoir téléchargé la nouvelle liste.
     */
    public void miseAJour(ListePdm l) {
        setActivee(false); // par défaut, toutes les pdms sont désactivées.
        PdmMemoire ancienne;
        PdmMemoire aInserer;
        for (final Pdm nouvelle : l.getListe().values()) {
            // on a la pdm à sauvegarder.
            if (existe(nouvelle.getNom())) {
            	// on doit mettre à jour la pdm sans l'écraser.
            	ancienne = acceder(nouvelle.getNom());
            	// on a l'ancienne pdm.
                aInserer = new PdmMemoire(nouvelle.getNom(), nouvelle.getAdresse().toString(), ancienne.getVisitee(), true, ancienne.getProgramme(), ancienne.getNumeroDernierTour());
            } else {
            	// on peut ajouter la pdm.
                aInserer = new PdmMemoire(nouvelle);
            }
            ajouter(aInserer);
        }
        refresh();
    }
    
    /**
     * Méthode d'affichage.
     */
    public String toString(int decalage) {
        String output = "";
        Iterator<PdmMemoire> parcours = this.liste.values().iterator();
        while (parcours.hasNext()) {
        	output += ((PdmMemoire)parcours.next()).toString(decalage) + "\n";
        }
        if (output.length() == 0) {
        	return output;
        } else {
        	return output.substring(0, output.length() - 1);
        }
    }
    
}
