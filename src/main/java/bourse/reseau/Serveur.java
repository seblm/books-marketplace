package bourse.reseau;

import java.net.*;
import java.io.IOException;

/**
 * Écoute les demandes de connexion physique au réseau
 */
public abstract class Serveur extends Thread {
    /** Socket d'écoute du serveur. */
    private ServerSocket socketServeur;

    /** Accès au socketServeur. */
    public ServerSocket getSocketServeur() {
        return this.socketServeur;
    }

    /** Constructeur de serveur. */
    public Serveur(int port) throws java.net.PortUnreachableException {
        try {
            socketServeur = new ServerSocket(port);
        } catch (IOException e) {
            throw new PortUnreachableException("Ne peut écouter le port " + port);
        }
    }
}