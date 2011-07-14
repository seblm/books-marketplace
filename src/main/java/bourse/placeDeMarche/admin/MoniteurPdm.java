/*
 * moniteurPdm.java
 *
 * Created on January 29, 2004, 8:26 AM
 */

package bourse.placeDeMarche.admin;

/**
 *
 * @author  slemerdy
 */
public class MoniteurPdm extends javax.swing.JFrame {
    
    /** Creates new form moniteurPdm */
    public MoniteurPdm() throws java.io.IOException {
        initComponents();
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        panelMenu = new javax.swing.JPanel();
        labelHote = new javax.swing.JLabel();
        champHote = new javax.swing.JTextField();
        labelPort = new javax.swing.JLabel();
        champPort = new javax.swing.JTextField();
        bouttonActualiser = new javax.swing.JButton();
        bouttonTerminer = new javax.swing.JButton();
        panelVisualisation = new javax.swing.JPanel();
        scrollPaneVisualisation = new javax.swing.JScrollPane();
        editorPaneVisualisation = new javax.swing.JEditorPane();
        panelStatut = new javax.swing.JPanel();
        labelStatut = new javax.swing.JLabel();

        setTitle("Administration de la place de march\u00e9");
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                exitForm(evt);
            }
        });

        panelMenu.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        labelHote.setLabelFor(champHote);
        labelHote.setText("h\u00f4te");
        panelMenu.add(labelHote);

        champHote.setText("localhost");
        champHote.setMinimumSize(new java.awt.Dimension(400, 20));
        champHote.setPreferredSize(new java.awt.Dimension(200, 20));
        panelMenu.add(champHote);

        labelPort.setLabelFor(champPort);
        labelPort.setText("port");
        panelMenu.add(labelPort);

        champPort.setText("1981");
        champPort.setMinimumSize(new java.awt.Dimension(100, 20));
        champPort.setPreferredSize(new java.awt.Dimension(50, 20));
        panelMenu.add(champPort);

        bouttonActualiser.setText("actualiser");
        bouttonActualiser.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                bouttonActualiserMouseClicked(evt);
            }
        });

        panelMenu.add(bouttonActualiser);

        bouttonTerminer.setText("terminer la place de march\u00e9");
        bouttonTerminer.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                bouttonTerminerMouseClicked(evt);
            }
        });

        panelMenu.add(bouttonTerminer);

        getContentPane().add(panelMenu, java.awt.BorderLayout.NORTH);

        panelVisualisation.setLayout(new java.awt.BorderLayout());

        panelVisualisation.setPreferredSize(new java.awt.Dimension(600, 400));
        editorPaneVisualisation.setEditable(false);
        editorPaneVisualisation.setMinimumSize(new java.awt.Dimension(600, 400));
        editorPaneVisualisation.setPreferredSize(new java.awt.Dimension(6, 23));
        scrollPaneVisualisation.setViewportView(editorPaneVisualisation);
        editorPaneVisualisation.getAccessibleContext().setAccessibleDescription("text/html");

        panelVisualisation.add(scrollPaneVisualisation, java.awt.BorderLayout.CENTER);

        getContentPane().add(panelVisualisation, java.awt.BorderLayout.CENTER);

        panelStatut.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        labelStatut.setText("Non connect\u00e9");
        panelStatut.add(labelStatut);

        getContentPane().add(panelStatut, java.awt.BorderLayout.SOUTH);

        pack();
    }//GEN-END:initComponents

    private void bouttonTerminerMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_bouttonTerminerMouseClicked
        try {
            java.net.Socket socket = new java.net.Socket(champHote.getText(), Integer.parseInt(champPort.getText()));
            java.io.PrintWriter out = new java.io.PrintWriter(socket.getOutputStream(), true);
            out.println("<MSG><ADMIN REQUETE=\"terminer\"/></MSG>");
        } catch (java.net.UnknownHostException e) {
            labelStatut.setText("L'hôte " + this.champHote.getText() + " est inconnu");
        } catch (java.io.IOException e) {
            labelStatut.setText("Erreur d'entrée/sortie");
            e.printStackTrace(System.err);
        } catch (NumberFormatException e) {
            labelStatut.setText("Le port doit-être un entier");
        }
    }//GEN-LAST:event_bouttonTerminerMouseClicked

    private void bouttonActualiserMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_bouttonActualiserMouseClicked
        try {
            java.net.Socket socket = new java.net.Socket(champHote.getText(), Integer.parseInt(champPort.getText()));
            java.io.PrintWriter out = new java.io.PrintWriter(socket.getOutputStream(), true);
            java.io.BufferedReader in = new java.io.BufferedReader(new java.io.InputStreamReader(socket.getInputStream()));
            out.println("<MSG><ADMIN REQUETE=\"index.html\"/></MSG>");
            String reception = "";
            String buffer = "";
            int nombreTentatives = 10;
            int attenteParTentative = 1000;
            // nombreTentatives * attenteParTentative = nombre de milisecondes avant que n'échoue l'écoute.
            int numeroTentative = 0;
            synchronized (this) {
                while (numeroTentative < 5 && socket.getInputStream().available() == 0) {
                    try { wait(attenteParTentative); } catch (java.lang.InterruptedException e) { }
                    numeroTentative++;
                }
            }
            if (socket.getInputStream().available() != 0) {
                // Il y a quelquechose à lire sur la socket.
                while (buffer.lastIndexOf("</html>") == -1) {
                    buffer = in.readLine();
                    reception += buffer;
                }
                java.io.File fichierCache = java.io.File.createTempFile("pdm", ".html");
                java.io.FileWriter sortieFichier = new java.io.FileWriter(fichierCache);
                sortieFichier.write(reception);
                sortieFichier.close();
                editorPaneVisualisation.setPage("file://" + fichierCache.getCanonicalPath());
                labelStatut.setText("Envoi terminé");
            } else
                labelStatut.setText("Aucune réponse de la part de " + champHote.getText() + " depuis " + (nombreTentatives * attenteParTentative) + "ms.");
            in.close();
            out.close();
            socket.close();
        } catch (java.net.UnknownHostException e) {
            labelStatut.setText("L'hôte " + this.champHote.getText() + " est inconnu");
        } catch (java.io.UnsupportedEncodingException e) {
            labelStatut.setText("UTF-8 n'est pas supporté");
        } catch (java.io.IOException e) {
            labelStatut.setText("Erreur d'entrée/sortie");
            e.printStackTrace(System.err);
        } catch (NumberFormatException e) {
            labelStatut.setText("Le port doit-être un entier");
        }
    }//GEN-LAST:event_bouttonActualiserMouseClicked
    
    /** Exit the Application */
    private void exitForm(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_exitForm
        System.exit(0);
    }//GEN-LAST:event_exitForm
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        try {
            new MoniteurPdm().show();
        } catch (java.io.IOException e) {
            System.err.println(e);
        }
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel panelVisualisation;
    private javax.swing.JLabel labelStatut;
    private javax.swing.JButton bouttonTerminer;
    private javax.swing.JPanel panelMenu;
    private javax.swing.JLabel labelHote;
    private javax.swing.JTextField champHote;
    private javax.swing.JLabel labelPort;
    private javax.swing.JScrollPane scrollPaneVisualisation;
    private javax.swing.JPanel panelStatut;
    private javax.swing.JTextField champPort;
    private javax.swing.JButton bouttonActualiser;
    private javax.swing.JEditorPane editorPaneVisualisation;
    // End of variables declaration//GEN-END:variables
    
}
