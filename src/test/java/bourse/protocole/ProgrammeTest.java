package bourse.protocole;

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Lists.newLinkedList;
import static org.fest.assertions.Assertions.assertThat;
import static org.fest.assertions.Fail.fail;

import java.util.LinkedList;

import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import bourse.placeDeMarche.enchere.Enchere;
import bourse.sdd.Livre;
import bourse.sdd.ProgrammePro;

import com.google.common.collect.Lists;

public class ProgrammeTest {

    private LinkedList<ProgrammePro> programmes;

    private Livre livre;

    @Before
    public void initialize() {
        programmes = Lists.<ProgrammePro> newLinkedList();
        float prix = 0;
        float etat = 0.4f;
        this.livre = new Livre("lupin", "leblanc", new Categorie(Categorie.SF), "poch", "belin", prix, etat,
                "15/11/00", "yetet", 12, "agent1", 0f);
        programmes.add(new ProgrammePro(1, livre));
        Livre newLivre = new Livre("peterpan", "disney", new Categorie(Categorie.ROMAN), "poch", "belin", prix, etat,
                "15/11/00", "yetet", 34, "pdm", 0f);
        programmes.add(new ProgrammePro(2, newLivre));
        newLivre = new Livre("roi lion", "disney", new Categorie(Categorie.BD), "poch", "belin", prix, etat,
                "15/11/00", "yetet", 19, "agent2", 0f);
        programmes.add(new ProgrammePro(3, newLivre));
    }

    @Test
    public void testToClass() {
        Programme p = new Programme(programmes);
        Element programmeDocumentElement = (Element) p.toDOM().getDocumentElement().getChildNodes().item(0);
        Programme pdest = new Programme(newLinkedList());
        pdest.toClass(programmeDocumentElement);
        ProgrammePro firstProgrammePro = (bourse.sdd.ProgrammePro) pdest.getListeProgramme().get(0);
        assertThat(firstProgrammePro.getLivre().getTitre()).isEqualTo("lupin");
        assertThat(firstProgrammePro.getLivre().getCategorie()).isEqualTo(new Categorie(Categorie.SF));
        assertThat(firstProgrammePro.getLivre().getEtat()).isEqualTo(0.4f);
        assertThat(firstProgrammePro.getLivre().getTitre()).isEqualTo("lupin");
    }

    @Test
    public void testToDOM() {
        Programme p = new Programme(programmes);
        Document d = p.toDOM();
        Element root = d.getDocumentElement();
        assertThat(root.getTagName()).isEqualTo("MSG");
        Node programmeElement = root.getFirstChild();
        assertThat(programmeElement.getNodeName()).isEqualTo("PROGRAMME");
        assertThat(programmeElement.getChildNodes().getLength()).isEqualTo(3);
        final Node enchereElement = programmeElement.getFirstChild();
        assertThat(enchereElement.getNodeName()).isEqualTo("ENCHERE");
        final Node numeroAttribute = enchereElement.getAttributes().item(0);
        assertThat(numeroAttribute.getNodeName()).isEqualTo("NUMERO");
        assertThat(numeroAttribute.getNodeValue()).isEqualTo("1");
        final Node livreElement = enchereElement.getFirstChild();
        assertThat(livreElement).isNotNull();
        assertThat(livreElement.getNodeName()).isEqualTo("LIVRE");
        assertThat(livreElement.getAttributes().item(0).getNodeName()).isEqualTo("CATEGORIE");
        assertThat(livreElement.getAttributes().item(0).getNodeValue()).isEqualTo("Science fiction");
        assertThat(livreElement.getAttributes().item(1).getNodeName()).isEqualTo("ETAT");
        assertThat(livreElement.getAttributes().item(1).getNodeValue()).isEqualTo("0.4");
        assertThat(livreElement.getAttributes().item(2).getNodeName()).isEqualTo("PRIX");
        assertThat(livreElement.getAttributes().item(2).getNodeValue()).isEqualTo("0.0");
        assertThat(livreElement.getAttributes().item(3).getNodeName()).isEqualTo("TITRE");
        assertThat(livreElement.getAttributes().item(3).getNodeValue()).isEqualTo("lupin");
    }

    @Test
    public void testInsertionPossible() {
        Programme p = new Programme(programmes);
        assertThat(p.insertionPossible("no-pdm", 23)).isFalse();
        assertThat(p.insertionPossible("pdm", 23)).isTrue();
        assertThat(p.insertionPossible("agent2", 19)).isFalse();
    }

    @Test
    public void testAjouterVente() {
        Programme p = new Programme(programmes);
        boolean result = p.ajouterVente(livre, "", Enchere.ENCHERE_UN, 10.4f);
        assertThat(result).isFalse();
    }

    @Test
    public void testToHtml() {
        assertThat(new Programme(programmes).toHtml())
                .isEqualTo(
                        "<ol><li>Livre n°12 : <i>lupin</i> d'une valeur de <b>0 Euros</b>, d&eacute;tenu par <b>agent1</b>.</li>"
                                + "<li>Livre n°34 : <i>peterpan</i> d'une valeur de <b>0 Euros</b>, d&eacute;tenu par <b>pdm</b>.</li>"
                                + "<li>Livre n°19 : <i>roi lion</i> d'une valeur de <b>0 Euros</b>, d&eacute;tenu par <b>agent2</b>.</li></ol>");
    }

    @Test
    public void testToHtmlWithoutBooks() {
        final Programme programme = new Programme(newLinkedList(newArrayList(new ProgrammePro(1, null))));
        try {
            programme.toHtml();
            fail("Implementation does not support progamme with no books. Expecting a NPE.");
        } catch (NullPointerException e) {
            assertThat(e).isNotNull();
        }
    }
}
