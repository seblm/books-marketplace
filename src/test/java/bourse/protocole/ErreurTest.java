package bourse.protocole;

import static com.google.common.collect.Iterators.forArray;
import static org.fest.assertions.Assertions.assertThat;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Iterator;

import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class ErreurTest extends SAXTest {

    @Test
    public final void with_a_simple_error_can_produce_xml_message()
            throws UnsupportedEncodingException, SAXException, IOException {
        Erreur simpleError = new Erreur("Simple Error", "no blabla");

        assertThat(simpleError.getNom()).isEqualTo("Simple Error");
        assertThat(simpleError.getRaison()).isEmpty();
        assertThat(simpleError.getType()).isEqualTo(TypeMessage.ERREUR);

        String xml = simpleError.toXML();
        Iterator<String> xmlLines = forArray(xml.split("\n"));
        assertThat(xmlLines.next()).isEqualTo(
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        assertThat(xmlLines.next()).isEqualTo(
                "<!DOCTYPE MSG SYSTEM \"MSG.dtd\">");
        assertThat(xmlLines.next()).isEqualTo("<MSG>");
        assertThat(xmlLines.next()).isEqualTo(
                "<ERREUR NOM=\"Simple Error\">no blabla</ERREUR>");
        assertThat(xmlLines.next()).isEqualTo("</MSG>");
        assertThat(getErrorFromXml(xml)).isEqualTo(simpleError);
    }

    @Test
    public final void with_a_zerovente_can_not_produce_xml_message()
            throws UnsupportedEncodingException, SAXException, IOException {
        Erreur zeroVenteError = new Erreur("ZEROVENTE", "pas de vente",
                "aucune raison connue");

        assertThat(zeroVenteError.getNom()).isEqualTo("ZEROVENTE");
        assertThat(zeroVenteError.getRaison())
                .isEqualTo("aucune raison connue");
        assertThat(zeroVenteError.getType()).isEqualTo(TypeMessage.ERREUR);

        String xml = zeroVenteError.toXML();
        Iterator<String> xmlLines = forArray(xml.split("\n"));
        assertThat(xmlLines.next()).isEqualTo(
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        assertThat(xmlLines.next()).isEqualTo(
                "<!DOCTYPE MSG SYSTEM \"MSG.dtd\">");
        assertThat(xmlLines.next()).isEqualTo("<MSG>");
        assertThat(xmlLines.next())
                .isEqualTo(
                        "<ERREUR NOM=\"ZEROVENTE\">pas de vente<RAISON TYPE=\"aucune raison connue\"/>");
        assertThat(xmlLines.next()).isEqualTo("</ERREUR>");
        assertThat(xmlLines.next()).isEqualTo("</MSG>");
        assertThat(getErrorFromXml(xml).toString())
                .isEqualTo(
                        "Erreur{nom=ZEROVENTE, message=pas de vente, pdmnom=, adresse=, raison=aucune raison connue}");
    }

    @Test
    public final void with_a_duplication_can_produce_message()
            throws UnsupportedEncodingException, SAXException, IOException {
        Erreur duplicationError = new Erreur("DUPLICATION", "en double",
                "Wall Book", "5° avenue");

        assertThat(duplicationError.getNom()).isEqualTo("DUPLICATION");
        assertThat(duplicationError.getRaison()).isEmpty();
        assertThat(duplicationError.getType()).isEqualTo(TypeMessage.ERREUR);

        String xml = duplicationError.toXML();
        Iterator<String> xmlLines = forArray(xml.split("\n"));
        assertThat(xmlLines.next()).isEqualTo(
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        assertThat(xmlLines.next()).isEqualTo(
                "<!DOCTYPE MSG SYSTEM \"MSG.dtd\">");
        assertThat(xmlLines.next()).isEqualTo("<MSG>");
        assertThat(xmlLines.next())
                .isEqualTo(
                        "<ERREUR NOM=\"DUPLICATION\">en double<PDM ADRESSE=\"5° avenue\" NOM=\"Wall Book\"/>");
        assertThat(xmlLines.next()).isEqualTo("</ERREUR>");
        assertThat(xmlLines.next()).isEqualTo("</MSG>");
        assertThat(getErrorFromXml(xml)).isEqualTo(duplicationError);
    }

    @Test
    public final void with_a_general_error_toXML_can_not_produce_message()
            throws UnsupportedEncodingException, SAXException, IOException {
        Erreur generalError = new Erreur("General Error", "description", "dte",
                "1234", "trop de blabla");

        assertThat(generalError.getNom()).isEqualTo("General Error");
        assertThat(generalError.getRaison()).isEqualTo("trop de blabla");
        assertThat(generalError.getType()).isEqualTo(TypeMessage.ERREUR);

        String xml = generalError.toXML();
        Iterator<String> xmlLines = forArray(xml.split("\n"));
        assertThat(xmlLines.next()).isEqualTo(
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        assertThat(xmlLines.next()).isEqualTo(
                "<!DOCTYPE MSG SYSTEM \"MSG.dtd\">");
        assertThat(xmlLines.next()).isEqualTo("<MSG>");
        assertThat(xmlLines.next()).isEqualTo(
                "<ERREUR NOM=\"General Error\">description</ERREUR>");
        assertThat(xmlLines.next()).isEqualTo("</MSG>");
        assertThat(getErrorFromXml(xml)).isEqualTo(
                new Erreur("General Error", "description"));
    }

    private Erreur getErrorFromXml(final String xml) throws SAXException,
            IOException, UnsupportedEncodingException {
        Document document = documentBuilder.parse(new ByteArrayInputStream(xml
                .getBytes("UTF-8")));
        Element root = document.getDocumentElement();
        NodeList noeuds = root.getChildNodes();
        Element type = (Element) noeuds.item(0);
        final Erreur erreur = new Erreur(type);
        return erreur;
    }

}
