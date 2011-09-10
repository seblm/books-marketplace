package bourse.protocole;

import org.w3c.dom.*;
import bourse.sdd.*;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;

import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import java.io.ByteArrayInputStream;

public class Resultat extends bourse.protocole.Protocole {

    private String nom;
    private float enchere;
    private bourse.sdd.Livre livre;

    /** Obtient le nom de l'acheteur ou du vendeur si la vente n'a pas réussie. */
    public String getAcheteur() {
        return nom;
    }

    public float getEnchere() {
        return enchere;
    }

    public Livre getLivre() {
        return livre;
    }

    /**
     * Modifie le nom de l'acheteur (cela se produit lorsque la place de marché
     * a rencontré une erreur lors de la transaction financière sur la base de
     * données.
     */
    public void setAcheteur(String ancienProprietaire) {
        this.nom = ancienProprietaire;
    }

    public Resultat(bourse.sdd.Livre livre, String nom, float enchere) {
        super(new TypeMessage(TypeMessage.TM_RESULTAT));
        this.nom = nom;
        this.enchere = enchere;
        this.livre = livre;

    }

    public Resultat(Element type) {
        super(new TypeMessage(TypeMessage.TM_RESULTAT));
        this.toClass(type);
    }

    protected void toClass(Element type) {

        NodeList noeuds = type.getChildNodes();
        Element livrend = (Element) noeuds.item(0);
        this.livre = new Livre(livrend);
        Element agent = (Element) noeuds.item(1);
        this.nom = agent.getAttribute("NOM");
        Element enchere = (Element) noeuds.item(2);
        Text noeud = (Text) enchere.getFirstChild();
        this.enchere = Float.valueOf(noeud.getNodeValue()).floatValue();

    }

    public org.w3c.dom.Document toDOM() {
        Document document = null;
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            document = builder.newDocument();
            Element root = document.createElement("MSG");
            document.appendChild(root);
            Element type = document.createElement("RESULTAT");
            root.appendChild(type);
            livre.addElement(type, this.type);
            Element agent = document.createElement("AGENT");
            type.appendChild(agent);
            Attr nomelm = document.createAttribute("NOM");
            nomelm.setValue(nom);
            agent.setAttributeNode(nomelm);
            Element encherelm = document.createElement("ENCHERE");
            type.appendChild(encherelm);
            encherelm.appendChild(document.createTextNode(String.valueOf(enchere)));
        } catch (Exception e) {
            e.printStackTrace(System.err);
        }
        return document;
    }

    public String toXML() {
        return super.toXML(this.toDOM());
    }

    public static void main(String args[]) {
        float solde = 1546;
        float pas = 450;
        int i = 12;
        float pr = 153;
        float et = (float) 0.4;
        Livre livre = new Livre("lupin", "leblanc", new Categorie(), "poch", "belin", pr, et, "15/11/00", "yetet", i,
                "poupoupoup", (float) 0.0);

        String p = new Resultat(livre, "machin", solde).toXML();
        System.out.println(p);
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
                public void fatalError(SAXParseException exception) throws SAXException {
                }

                // treat validation errors as fatal
                public void error(SAXParseException e) throws SAXParseException {
                    throw e;
                }

                // dump warnings too
                public void warning(SAXParseException err) throws SAXParseException {
                    System.out.println("** Warning" + ", line " + err.getLineNumber() + ", uri " + err.getSystemId());
                    System.out.println("   " + err.getMessage());
                }
            });
            Document document = builder.parse(new ByteArrayInputStream(p.getBytes("UTF-8")), Protocole.BASE_DTD);
            Element root = document.getDocumentElement();
            NodeList noeuds = root.getChildNodes();
            Element typeDOM = (Element) noeuds.item(0);
            Element typeDOME = (Element) typeDOM;
            p = new Resultat(typeDOME).toXML();
            System.out.println(p);
        } catch (Exception e) {
            System.err.print("Protocole: ");
            e.printStackTrace(System.err);
        }
    }

}
