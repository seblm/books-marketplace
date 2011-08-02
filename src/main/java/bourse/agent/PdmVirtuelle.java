package bourse.agent;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.util.LinkedList;

import bourse.protocole.Categorie;
import bourse.protocole.Erreur;
import bourse.protocole.Programme;
import bourse.protocole.PropositionEnchereP;
import bourse.protocole.ResultAgents;
import bourse.protocole.ResultProposeVente;
import bourse.protocole.Resultat;
import bourse.reseau.ManagerConnexion;
import bourse.sdd.Livre;
import bourse.sdd.PDMPro;
import bourse.sdd.ProgrammePro;

/**
 * Emulation d'une pdm.
 */
public class PdmVirtuelle extends ManagerConnexion {
        
    /**
     * Constructeur de pdm virtuelle.
     */
    public PdmVirtuelle(boolean verbose) throws java.lang.Exception {
        super(new ServerSocket(1981).accept(), "</MSG>", verbose);
    }
    
    public void run() {
        super.run();
        try {
            System.out.println("Démarrage du serveur");
            BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
            boolean sortir = false;
            int choix;
            while (!sortir) {
                System.out.println(" 0 : quitter le programme");
                System.out.println("GENERAL");
                System.out.println(" 1 : RESULTWELCOME");
                System.out.println(" 2 : ERREUR DUPLICATION");
                System.out.println(" 3 : RESULTBYE");
                System.out.println(" 4 : ERREUR BLOQUE");
                System.out.println("ERREURS");
                System.out.println(" 5 : ERREUR MALFORMATION");
                System.out.println(" 6 : ERREUR INATTENDU");
                System.out.println("PROGRAMME");
                System.out.println(" 7 : PROGRAMME");
                System.out.println("AGENTS PRESENTS");
                System.out.println(" 8 : RESULTAGENTS");
                System.out.println("ENCHERES");
                System.out.println(" 9 : PROPOSITIONENCHERE");
                System.out.println("10 : PROPOSITIONENCHERE (mise à jour)");
                System.out.println("11 : RESULTAT");
                System.out.println("12 : ERREUR ZEROVENTE");
                System.out.println("13 : RESULTPROPOSEVENTE");
                System.out.print("Le message à envoyer ? ");
                choix = Integer.parseInt(in.readLine());
                Livre b = new Livre("tintin", "RG", new Categorie(Categorie.BD), "broche", "Casterman", (float)30.99, (float)0, "2004-04-21", "222222222", 26, "protocoleman", (float)12.85);
                switch (choix) {
                    case 0 : sortir = true; break;
                    case 1 : this.ecrire(new bourse.protocole.ResultWelcome(100, new Categorie()).toXML()); break;
                    case 2 : this.ecrire(new bourse.protocole.Erreur("Duplication", "désolé, t'es dupliqué man.", "localhost", "1981").toXML()); break;
                    case 3 :
                    	final LinkedList<PDMPro> l = new LinkedList<PDMPro>();
                        l.add(new PDMPro("PdmVirtuelle", "localhost:1981"));
                        this.ecrire(new bourse.protocole.ResultBye(l).toXML());
                        break;
                    case 4 : this.ecrire(new bourse.protocole.Erreur("Bloque", "désolé, je te bloque mon gros.").toXML()); break;
                    case 5 : this.ecrire(new bourse.protocole.Erreur("XMLMalformation", "désolé, t'es malformé man.").toXML()); break;
                    case 6 : this.ecrire(new bourse.protocole.Erreur("Inattendu", "désolé, tu m'envoie de la merde là.").toXML()); break;
                    case 7 :
                    	final LinkedList<ProgrammePro> programmePros = new LinkedList<ProgrammePro>();
                        programmePros.add(new ProgrammePro(56, new Livre("le seigneur des anals", "JRR Tolkien", new Categorie(Categorie.SF), "poche", "TaMereEditions", 30, 0, "2003-01-01", "222222222", 150, "protocoleman", (float)50)));
                        programmePros.add(new ProgrammePro(57, new Livre("tintin", "RG", new Categorie(Categorie.BD), "broche", "Casterman", (float)30.99, 0, "2004-04-21", "222222222", 26, "ban", (float)85)));
                        this.ecrire(new Programme(programmePros).toXML());
                        break;
                    case 8:
                    	final LinkedList<String> listeAgents = new LinkedList<String>();
                        listeAgents.add("Groupe-A-billou");
                        listeAgents.add("GroupeB-georgesOfTheJungle");
                        this.ecrire(new ResultAgents(listeAgents).toXML());
                        break;
                    case 9:
                        this.ecrire(new PropositionEnchereP("EnchereUn", 10, 12, (float)5.5, b, (float)35.0, "").toXML());
                        break; 
                    case 10:
                        this.ecrire(new PropositionEnchereP("EnchereUn", 10, 12, (float)6, b, (float)35.0, "Groupe-E.Bernadette").toXML());
                        break;   
                    case 11:
                        this.ecrire(new Resultat(b, "Groupe-E.Bernadette", (float)35.0).toXML());
                        break;
                    case 12 : this.ecrire(new Erreur("Zerovente", "désolé, je veux pas vendre ton bouquin de merde.").toXML()); break;
                    case 13 : this.ecrire(new ResultProposeVente(26).toXML());
                }
            }
        } catch (Exception e) {
        	e.printStackTrace(System.err);
        }
    }
    
    protected void traiter(String message) {
    }
    
    public static void main(String[] argc) {
        try {
            PdmVirtuelle pdm = new PdmVirtuelle(true);
            pdm.run();
        } catch (Exception e) {
        	e.printStackTrace(System.err);
        }
    }
    
    /**
     * Appelé par ThreadLecture lorsque la connexion a été fermé par le client.
     */
    protected void close() {
    }
    
}
