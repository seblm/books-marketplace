package bourse.placeDeMarche;

import bourse.protocole.*;

public class Agent {
    private String nomPDM;
    private String nomAgent;
    private int port;
    private float argent;
    private Categorie categorie;
    private boolean bloque;
    /** Instancie un agent avec les informations minimum. Il est conseillé d'actualiser
     * ces données par une requête à la base de données. */
    public Agent(int port) {
        this.nomPDM = "";
        this.nomAgent = "";
        this.port = port;
        this.argent = 0;
        this.categorie = new Categorie();
        this.bloque = false;
    }
    /** Instancie un agent avec toutes les informations nécessaires pour être
     * enregistré dans la table agents de la base de données. */
    public Agent(String nomPDM, String nomAgent, int port, float argent, Categorie categorie) {
        this.port = port;
        this.setAgent(nomPDM, nomAgent, argent, categorie);
        this.setBloque(false);
    }
    /** Constructeur par recopie */
    public Agent(Agent ancienAgent) {
        this.nomPDM = ancienAgent.getNomPDM();
        this.nomAgent = ancienAgent.getNomAgent();
        this.port = ancienAgent.getPort();
        this.argent = ancienAgent.getArgent();
        this.categorie = ancienAgent.getCategorie();
        this.bloque = ancienAgent.getBloque();
    }
    public void setAgent(String nomPDM, String nomAgent, float argent, Categorie categorie) {
        this.nomPDM = nomPDM;
        this.nomAgent = nomAgent;
        this.argent = argent;
        this.categorie = categorie;
    }
    public String getNomPDM() { return this.nomPDM; }
    public String getNomAgent() { return this.nomAgent; }
    public int getPort() { return this.port; }
    public float getArgent() { return this.argent; }
    public Categorie getCategorie() { return this.categorie; }
    public boolean getBloque() { return this.bloque; }
    public void setNom(String nom) { this.nomAgent = nom; }
    public void setPort(int port) { this.port = port; }
    public void setArgent(float argent) { this.argent = argent; }
    public void setNomPDM(String nomPDM) { this.nomPDM = nomPDM; }
    public void setCategorie(Categorie categorie) { this.categorie = categorie; }
    public void setBloque(boolean bloque) { this.bloque = bloque; }
    public String toString() { return this.nomAgent; }

    public String toHtml() {
        return "<i>" + nomAgent + "</i> connect&eacute; sur " + nomPDM + ":" + port + ", disposant de <b>" + argent + " Euros</b>, int&eacute;ress&eacute; par " + categorie + " et actuellement " + (bloque?"":"non ") + "bloqu&eacute;.";
    }
    
    /** Surcharge la méthode de comparaison standard de Agent en le comparant à 
     * un autre agent et en testant l'égalité sur le nom et le port de l'agent.
     */
    public boolean equals(Object o) {
        if (o instanceof Agent) {
            Agent a = (Agent)o;
            return a.getNomAgent().equalsIgnoreCase(this.getNomAgent()) && a.getPort() == port;
        } else
            return false;
    }
}
