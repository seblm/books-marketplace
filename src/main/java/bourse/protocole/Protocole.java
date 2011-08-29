package bourse.protocole;

// inclusion de l'API JAXP
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringWriter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
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

    // Constructeurs (inutiles car la classe est abstract mais appellées par les classes filles - super(...); )
    protected Protocole(TypeMessage type) { this.type = type; }
    protected Protocole(Element type) { this.toClass(type); };

    protected TypeMessage type;
    
    public TypeMessage getType() { return this.type; }
    
    // Méthodes à hériter
    protected abstract void toClass(Element type);
    public abstract Document toDOM();
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
            
            builder.setEntityResolver(new EntityResolver() {
                @Override
                public InputSource resolveEntity(String publicId, String systemId) throws SAXException, IOException {
                    return new InputSource(Protocole.class.getResourceAsStream("/MSG.dtd"));
                }
            });
            // La définition de ErrorHandler est inspirée de
            // http://java.sun.com/j2ee/1.4/docs/tutorial/doc/JAXPDOM3.html#wp64106
            builder.setErrorHandler(new ErrorHandler() {
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
            Document document = builder.parse(new ByteArrayInputStream(fichierXML.getBytes("UTF-8")));
            Element root = document.getDocumentElement();
            NodeList noeuds = root.getChildNodes();
            Element typeDOM = (Element)noeuds.item(0);
            Element typeDOME = (Element)typeDOM;
            System.out.println(typeDOM.getTagName());
            TypeMessage type = TypeMessage.valueOf(typeDOM.getTagName());
            switch (type) {
                case INCONNU              : message = new Inconnu(); break;
                case WELCOME              : message = new Welcome(typeDOME); break;
                case BYE                  : message = new Bye(typeDOME); break;
                case REQUETEPROGRAMME    : message = new RequeteProgramme (); break;
                case REQUETEAGENTS       : message = new RequeteAgents (); break;
                case RESULTWELCOME       : message = new ResultWelcome(typeDOME); break;
                case ERREUR               : message = new Erreur(typeDOME); break;
                case RESULTPROPOSEVENTE : message = new ResultProposeVente(typeDOME); break;
                case RESULTAGENTS        : message = new ResultAgents(typeDOME); break;
                case PROPOSEVENTE        : message = new ProposeVente(typeDOME); break;
                case PROPOSITIONENCHERE  : if (typeDOM.hasAttribute("NOM"))
                    message = new PropositionEnchereP(typeDOME);
                else
                    message = new PropositionEnchereA(typeDOME);
                break;
                case PROGRAMME            : message = new Programme(typeDOME); break;
                case RESULTAT             : message = new Resultat(typeDOME); break;
                case RESULTBYE           : message = new ResultBye(typeDOME); break;
                case ADMIN                : message = new Admin(typeDOME); break;
                default                   : message = new Erreur("Malformation", "Le type du message n'a pas pu être reconnu."); break;
                
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