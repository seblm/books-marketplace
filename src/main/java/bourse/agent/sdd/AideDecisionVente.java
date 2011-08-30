package bourse.agent.sdd;

import bourse.agent.Agent;

public class AideDecisionVente {

    private float sommeQVente;

    private float qVenteActuel;

    private int nbQVenteCalcules;

    private Agent pere;

    public AideDecisionVente(Agent a) {
        sommeQVente = 0;
        qVenteActuel = 0;
        nbQVenteCalcules = 0;
        pere = a;
    }

    public float getSommeQVente() {
        return sommeQVente;
    }

    public float getQVenteActuel() {
        return qVenteActuel;
    }

    public int getNbQVenteCalcules() {
        return nbQVenteCalcules;
    }

    public void miseAJourQVente() {
        qVenteActuel = ((bourse.agent.ia.Ia) pere.getDecision()).qVente()
                * pere.getMemoire().getTemps();
        sommeQVente += qVenteActuel;
        nbQVenteCalcules++;
    }
}
