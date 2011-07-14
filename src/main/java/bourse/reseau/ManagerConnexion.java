package bourse.reseau;

import java.io.*;
import java.net.Socket;

/** Serveur gérant les entrées/sorties d'une connexion. */
public abstract class ManagerConnexion extends Thread {

    private Socket socket;
    public PrintWriter out;
    private ThreadLecture threadLecture;
    /** Le message n'est transmis que lorsque la dernière chaîne reçue contient
     * la chaîne motifFin. */
    private String motifFin;
    /** Tant que écouter est vrai, le thread de lecture continue à attendre des
     * messages. */
    private boolean ecouter;
    private boolean connexionInterrompue;
    private boolean verbose;

    public ManagerConnexion(Socket socket, String motifFin, boolean verbose) throws IOException {
        this.socket = socket;
        this.out = new PrintWriter(this.socket.getOutputStream(), true);
        this.ecouter = true;
        this.connexionInterrompue = false;
        this.motifFin = motifFin;
        this.threadLecture = new ThreadLecture(this, new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8")));
        this.verbose = verbose;
    }

    public void run() {
        this.threadLecture.start();
    }

    protected abstract void traiter(String message);
    
    /** Détermine si la connexion doit encore écouter. */
    protected synchronized boolean getEcouter() { return ecouter && !connexionInterrompue; }
    /** Fixé à false, le booléen écouter stope toute lecture et donc termine la connexion. */
    protected synchronized void setEcouter(boolean ecouter) { this.ecouter = ecouter; }
    protected synchronized boolean getConnexionInterrompue() { return connexionInterrompue; }
    protected synchronized void setConnexionInterrompue(boolean connexionInterrompue) { this.connexionInterrompue = connexionInterrompue; }
    protected synchronized boolean getVerbose() { return verbose; }
    protected synchronized void setVerbose(boolean verbose) { this.verbose = verbose; }
    protected String getMotifFin() { return this.motifFin; }
    protected synchronized String getHostAddress() { return this.socket.getInetAddress().getHostAddress(); }
    /** Écrire une chaîne vers l'hôte distant. */
    public synchronized void ecrire(String chaine) throws IOException {
        if (ecouter) {
            if (verbose) System.out.println("Envoi de \"" + chaine + "\"");
            this.out.println(chaine);
        } else
            throw new IOException("La connexion est inactive.");
    }
    public synchronized void deconnecter() {
        this.ecouter = false;
        this.out.close();
        try { this.socket.close(); } catch (IOException e) { System.err.println(e); }
    }
}
