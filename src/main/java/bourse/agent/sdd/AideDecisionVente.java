package bourse.agent.sdd;

import bourse.agent.*;

public class AideDecisionVente {
    private bourse.agent.Agent pere;
    private float sommeQVente;

    public float getSommeQVente() {
        return sommeQVente;
    }

    private float qVenteActuel;

    public float getQVenteActuel() {
        return qVenteActuel;
    }

    private int nbQVenteCalcules;

    public int getNbQVenteCalcules() {
        return nbQVenteCalcules;
    }

    public AideDecisionVente(bourse.agent.Agent a) {
        sommeQVente = 0;
        qVenteActuel = 0;
        nbQVenteCalcules = 0;
        pere = a;
    }

    public void miseAJourQVente() {
        qVenteActuel = ((bourse.agent.ia.Ia) pere.getDecision()).qVente() * pere.getMemoire().getTemps();
        sommeQVente += qVenteActuel;
        nbQVenteCalcules++;
    }
}
