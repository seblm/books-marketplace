package bourse.protocole;

// inclusion de l'API JAXP
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

// utilisé pour exporter du xml
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.dom.DOMSource;

// nécessaire pour la méthode Transformer.transform()...
import java.io.StringWriter;
import java.io.ByteArrayInputStream;

// la définition d'un DOM par le w3c
import org.w3c.dom.*;

// les exceptions levées lors de la validation
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/**
 * Abstrait le protocole en permettant :
 *  - de stocker des constantes relatives au protocole
 *  - d'instancier une classe de message à partir d'un document XML 
 *  - de générer un document XML à partir de sa représentation DOM
 */
public abstract class Protocole {

    // Constantes globales
    public static final int portAgent = 1982;
    public static int LIVRES_NEUFS_PAR_AGENTS = 15;
    //public static final String[] categorie = {"Science+fiction", "Bandes+dessin%C3%A9es", "Science", "Romans+Policiers", "Informatique", "Aucune"};
    public static final int SCIENCE_FICTION=0;
    public static final int BANDE_DESSINEE=1;
    public static final int SCIENCE=2;
    public static final int ROMANS_POLICIERS=3;
    public static final int INFORMATIQUE=4;
    public static final int AUCUNE=5;
    public static final String[] typeRaison = {"neutre", "nomImplementee", "nonValide"};
    public static final int NEUTRE=0;
    public static final int NONIMPLEMENTE=1;
    public static final int NONVALIDE=2;
    public static final int transactionsParPlaceDeMarche = 100;
    public static final String MOTIF_FIN_FICHIER_XML = "</MSG";
    public static final String BASE_DTD = "file://" + new java.io.File("").getAbsolutePath() + "/";
    public static final String URI_DTD = "MSG.dtd";
    /** Classe sérialisée et commune pour tout les utilisateurs de Bd. */
    public static final bourse.reseau.ParametresBd parametresBd = new bourse.reseau.ParametresBd();

    // Constructeurs (inutiles car la classe est abstract mais appellées par les classes filles - super(...); )
    protected Protocole(TypeMessage type) { this.type = type; }
    protected Protocole(Element type) { this.toClass(type); };

    protected TypeMessage type;
    
    public TypeMessage getType() { return this.type; }
    
    // Méthodes à hériter
    protected abstract void toClass(Element type);
    public abstract Document toDOM();
/*    
    public int TransfCategorie(String cat){
        if(cat.equalsIgnoreCase(this.categorie[this.AUCUNE])) return this.AUCUNE;
        else if(cat.equalsIgnoreCase(this.categorie[this.BANDE_DESSINEE])) return this.BANDE_DESSINEE;
        else if(cat.equalsIgnoreCase(this.categorie[this.INFORMATIQUE])) return this.INFORMATIQUE;
        else if(cat.equalsIgnoreCase(this.categorie[this.ROMANS_POLICIERS])) return this.ROMANS_POLICIERS;
        else if(cat.equalsIgnoreCase(this.categorie[this.SCIENCE])) return this.SCIENCE;
        else if(cat.equalsIgnoreCase(this.categorie[this.SCIENCE_FICTION])) return this.SCIENCE_FICTION;
        else return (-1);
    }
*/
    public static final Protocole newInstance(String fichierXML) {
        Protocole message = null;
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        // D'après le tutorial JAXP, ces variables fixées à true permettent à
        // l'application de se concentrer sur l'analyse sémantique.
        factory.setCoalescing(true);
        factory.setExpandEntityReferences(true);
        factory.setIgnoringComments(true);
        factory.setIgnoringElementContentWhitespace(true);
        try {
            // factory.setValidating(true);
            DocumentBuilder builder = factory.newDocumentBuilder();
            
            // La définition de ErrorHandler est inspirée de
            // http://java.sun.com/j2ee/1.4/docs/tutorial/doc/JAXPDOM3.html#wp64106
            builder.setErrorHandler(new org.xml.sax.ErrorHandler() {
                // ignore fatal errors (an exception is guaranteed)
                public void fatalError(SAXParseException exception) throws SAXException { }
                // treat validation errors as fatal
                public void error(SAXParseException e) throws SAXParseException { throw e; }
                // dump warnings too
                public void warning(SAXParseException err) throws SAXParseException {
                    System.out.println("** Warning"
                    + ", line " + err.getLineNumber()
                    + ", uri " + err.getSystemId());
                    System.out.println("   " + err.getMessage());
                }
            }
            );
            Document document = builder.parse(new ByteArrayInputStream(fichierXML.getBytes("UTF-8")), Protocole.BASE_DTD);
            Element root = document.getDocumentElement();
            NodeList noeuds = root.getChildNodes();
            Element typeDOM = (Element)noeuds.item(0);
            Element typeDOME = (Element)typeDOM;
            TypeMessage type = new TypeMessage(typeDOM.getTagName());
            switch (type.getValue()) {
                case TypeMessage.TM_INCONNU : message = new Inconnu(); break;
                case TypeMessage.TM_WELCOME : message = new Welcome(typeDOME); break;
                case TypeMessage.TM_BYE     : message = new Bye(typeDOME); break;
                case TypeMessage.TM_REQUETE_PROGRAMME: message = new RequeteProgramme (); break;
                case TypeMessage.TM_REQUETE_AGENTS: message = new RequeteAgents (); break;
                case TypeMessage.TM_RESULT_WELCOME : message = new ResultWelcome(typeDOME); break;
                case TypeMessage.TM_ERREUR     : message = new Erreur(typeDOME); break;
                case TypeMessage.TM_RESULT_PROPOSE_VENTE     : message = new ResultProposeVente(typeDOME); break;
                case TypeMessage.TM_RESULT_AGENTS     : message = new ResultAgents(typeDOME); break;
                case TypeMessage.TM_PROPOSE_VENTE     : message = new ProposeVente(typeDOME); break;
                case TypeMessage.TM_PROPOSITION_ENCHERE     : if (typeDOM.hasAttribute("NOM"))
                    message = new PropositionEnchereP(typeDOME);
                else
                    message = new PropositionEnchereA(typeDOME);
                break;
                case TypeMessage.TM_PROGRAMME: message = new Programme(typeDOME); break;
                
                case TypeMessage.TM_RESULTAT          : message = new Resultat(typeDOME); break;
                case TypeMessage.TM_RESULT_BYE        : message = new ResultBye(typeDOME); break;
                case TypeMessage.TM_ADMIN             : message = new Admin(typeDOME); break;
                default : message = new Erreur("Malformation", "Le type du message n'a pas pu être reconnu."); break;
                
            }
        } catch (Exception e) {
            System.err.print("Protocole: ");
            e.printStackTrace(System.err);
            message = new Erreur("Malformation", "Le type du message n'a pas pu être reconnu.");
        }
        return message;
    }
    
    public String toXML(Document document) {
        java.io.StringWriter stringWriter = new java.io.StringWriter();
        try {            
            DOMSource domSource = new DOMSource(document.getDocumentElement());

            StreamResult streamResult = new StreamResult(stringWriter);
            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer serializer = tf.newTransformer();

            serializer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            serializer.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM, Protocole.URI_DTD);
            serializer.setOutputProperty(OutputKeys.INDENT,"yes");
            serializer.transform(domSource, streamResult);
        } catch (Exception e) {
            System.err.print("Protocole: ");
            e.printStackTrace(System.err);
            stringWriter = new StringWriter();
        }
        
        stringWriter.flush();
        return stringWriter.toString();
    }
    
}