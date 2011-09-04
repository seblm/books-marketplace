package bourse.sdd;

import static org.fest.assertions.Assertions.assertThat;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import bourse.protocole.Categorie;
import bourse.protocole.SAXTest;
import bourse.protocole.TypeMessage;

public class LivreTest extends SAXTest {

    private Livre livre;

    @Before
    public void createBook() {
        livre = TestFactory.createLivre();
    }

    @Test
    public void should_create_a_book() {
        assertBook(livre);
        assertThat(livre.toString(3))
                .isEqualTo(
                        "   Auteur = leblanc, Catégorie = Science, Date Parution = 15/11/00, Editeur = belin, Etat = 0.4, Format = poch, Id = 12, Isbn = yetet, Prix = 153.0, Titre = lupin, Proprietaire = protocoleman, PrixAchat = 50.95");
    }

    @Test
    public void should_export_as_html() {
        assertThat(livre.toHtml())
                .isEqualTo(
                        "Livre n°12 : <i>lupin</i> d'une valeur de <b>61 Euros</b>, d&eacute;tenu par <b>protocoleman</b>, achet&eacute; <b>50.95 Euros</b>.");
    }

    @Test
    public void should_create_book_by_copying_existing_one() {
        Livre book = new Livre(livre);
        assertBook(book);
        assertThat(book).isEqualTo(livre);
    }

    private void assertBook(Livre book) {
        assertThat(book.getTitre()).isEqualTo("lupin");
        assertThat(book.getAuteur()).isEqualTo("leblanc");
        assertThat(book.getCategorie().getCategorie()).isEqualTo(Categorie.SCIENCE);
        assertThat(book.getFormat()).isEqualTo("poch");
        assertThat(book.getEditeur()).isEqualTo("belin");
        assertThat(book.getPrix()).isEqualTo(153f);
        assertThat(book.getEtat()).isEqualTo(0.4f);
        assertThat(book.getDateParu()).isEqualTo("15/11/00");
        assertThat(book.getIsbn()).isEqualTo("yetet");
        assertThat(book.getId()).isEqualTo(12);
        assertThat(book.getProprietaire()).isEqualTo("protocoleman");
        assertThat(book.getPrixAchat()).isEqualTo(50.95f);
    }

    @Test
    public void should_create_book_from_DOM() {
        Document document = documentBuilder.newDocument();
        Element root = document.createElement("root");
        root.setAttribute("TITRE", "lupin");
        root.setAttribute("AUTEUR", "leblanc");
        root.setAttribute("CATEGORIE", "Sciences");
        root.setAttribute("FORMAT", "poch");
        root.setAttribute("EDITEUR", "belin");
        root.setAttribute("PRIX", "153");
        root.setAttribute("ETAT", "0.4");
        root.setAttribute("DATEPAR", "15/11/00");
        root.setAttribute("ISBN", "yetet");
        root.setAttribute("ID", "12");
        root.setAttribute("PROPRIETAIRE", "protocoleman");
        root.setAttribute("PRIXACHAT", "50.95");

        Livre book = new Livre(root);

        assertBook(book);
    }

    @Test
    public void should_create_empty_book_from_empty_DOM() {
        Document document = documentBuilder.newDocument();
        Element root = document.createElement("root");

        Livre book = new Livre(root);

        assertThat(book.getAuteur()).isEqualTo("");
        assertThat(book.getCategorie()).isEqualTo(new Categorie(Categorie.AUCUNE));
        assertThat(book.getDateParu()).isEqualTo("");
        assertThat(book.getEditeur()).isEqualTo("");
        assertThat(book.getEtat()).isEqualTo(0.0f);
        assertThat(book.getFormat()).isEqualTo("");
        assertThat(book.getId()).isEqualTo(0);
        assertThat(book.getIsbn()).isEqualTo("");
        assertThat(book.getPrix()).isEqualTo(0.0f);
        assertThat(book.getPrixAchat()).isEqualTo(0.0f);
        assertThat(book.getProprietaire()).isEqualTo("");
        assertThat(book.getTitre()).isEqualTo("");
    }

    @Test
    public void with_should_generate_dom() throws UnsupportedEncodingException, SAXException, IOException {
        Document document = documentBuilder.newDocument();
        Element root = document.createElement("root");

        livre.addElement(root, TypeMessage.PROPOSEVENTE);

        assertThat(root.hasChildNodes()).isTrue();
        Node bookNode = root.getChildNodes().item(0);
        assertThat(bookNode.getNodeName()).isEqualTo("LIVRE");
        assertThat(bookNode.getAttributes().getNamedItem("ID").getNodeValue()).isEqualTo("12");
        assertThat(bookNode.hasChildNodes()).isFalse();
    }
}
