package bourse.reseau;

import java.io.BufferedReader;
import java.io.IOException;

public class ThreadLecture extends Thread {

    /**
     * TAILLE_MAX_RECEPTION empêche un client malin d'envoyer trop de messages
     * sans jamais contenir le motifFin.
     */
    private static int TAILLE_MAX_RECEPTION = 1024 * 100;
    private ManagerConnexion managerConnexion;
    private BufferedReader in;

    protected ThreadLecture(ManagerConnexion managerConnexion, BufferedReader in) {
        this.managerConnexion = managerConnexion;
        this.in = in;
    }

    public void run() {
        String message = "";
        String buffer = "";
        try {
            while (managerConnexion.getEcouter()) {
                buffer = in.readLine();
                if (buffer == null) {
                    // La connexion a été coupée par le correspondant
                    managerConnexion.setConnexionInterrompue(true);
                    managerConnexion.traiter(null);
                } else {
                    message += buffer;
                    if (message.lastIndexOf(managerConnexion.getMotifFin()) != -1) {
                        // Le message contient le motif de fin donc on peux
                        // demander son traitement.
                        if (managerConnexion.getVerbose())
                            System.out.println("Réception de : " + message);
                        managerConnexion.traiter(message);
                        message = "";
                    } else if (message.length() > TAILLE_MAX_RECEPTION)
                        // Le message est trop gros : on l'oublie.
                        message = "";
                }
            }
        } catch (IOException e) {
            // La connexion a été coupée par le correspondant
            if (managerConnexion.getVerbose())
                System.err.println("ThreadLecture : La connexion a été interrompue !!!");
            managerConnexion.setConnexionInterrompue(true);
            managerConnexion.traiter(null);
        }
        try {
            in.close();
        } catch (IOException e) {
            if (managerConnexion.getVerbose())
                System.err.println(e);
        }
    }
}
