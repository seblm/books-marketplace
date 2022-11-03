package bourse.protocole;

import static org.fest.assertions.Assertions.assertThat;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class ErreurTest extends SAXTest {

    @Test
    public final void with_a_simple_error_can_produce_xml_message() throws UnsupportedEncodingException, SAXException,
            IOException {
        Erreur simpleError = new Erreur("Simple Error", "no blabla");

        assertThat(simpleError.getNom()).isEqualTo("Simple Error");
        assertThat(simpleError.getRaison()).isEmpty();
        assertThat(simpleError.getType().getValue()).isEqualTo(TypeMessage.TM_ERREUR);

        String xml = simpleError.toXML();
        String[] xmlLines = xml.split("\n");
        assertThat(xmlLines[0]).isEqualTo("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        assertThat(xmlLines[1]).isEqualTo("<!DOCTYPE MSG SYSTEM \"MSG.dtd\">");
        assertThat(xmlLines[2]).isEqualTo("<MSG>");
        assertThat(xmlLines[3]).isEqualTo("    <ERREUR NOM=\"Simple Error\">no blabla</ERREUR>");
        assertThat(xmlLines[4]).isEqualTo("</MSG>");
        Erreur actualErrorFromXml = getErrorFromXml(xml);
        assertThat(actualErrorFromXml.getNom()).isEqualTo(simpleError.getNom());
        assertThat(actualErrorFromXml.getRaison()).isEqualTo(simpleError.getRaison());
        assertThat(actualErrorFromXml.getType().getValue()).isEqualTo(simpleError.getType().getValue());
    }

    @Test
    public final void with_a_zerovente_can_produce_xml_message() throws UnsupportedEncodingException, SAXException,
            IOException {
        Erreur zeroVenteError = new Erreur("ZEROVENTE", "pas de vente", "aucune raison connue");

        assertThat(zeroVenteError.getNom()).isEqualTo("ZEROVENTE");
        assertThat(zeroVenteError.getRaison()).isEqualTo("aucune raison connue");
        assertThat(zeroVenteError.getType().getValue()).isEqualTo(TypeMessage.TM_ERREUR);

        String xml = zeroVenteError.toXML();
        String[] xmlLines = xml.split("\n");
        assertThat(xmlLines[0]).isEqualTo("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        assertThat(xmlLines[1]).isEqualTo("<!DOCTYPE MSG SYSTEM \"MSG.dtd\">");
        assertThat(xmlLines[2]).isEqualTo("<MSG>");
        assertThat(xmlLines[3]).isEqualTo("    <ERREUR NOM=\"ZEROVENTE\">");
        assertThat(xmlLines[4]).isEqualTo("        pas de vente");
        assertThat(xmlLines[5]).isEqualTo("        <RAISON TYPE=\"aucune raison connue\"/>");
        assertThat(xmlLines[6]).isEqualTo("    </ERREUR>");
        assertThat(xmlLines[7]).isEqualTo("</MSG>");
        Erreur actualErrorFromXml = getErrorFromXml(xml);
        assertThat(actualErrorFromXml.getNom()).isEqualTo(zeroVenteError.getNom());
        assertThat(actualErrorFromXml.getRaison()).isEqualTo(zeroVenteError.getRaison());
        assertThat(actualErrorFromXml.getType().getValue()).isEqualTo(zeroVenteError.getType().getValue());
    }

    @Test
    public final void with_a_duplication_can_produce_message() throws UnsupportedEncodingException, SAXException,
            IOException {
        Erreur duplicationError = new Erreur("DUPLICATION", "en double", "Wall Book", "5° avenue");

        assertThat(duplicationError.getNom()).isEqualTo("DUPLICATION");
        assertThat(duplicationError.getRaison()).isEmpty();
        assertThat(duplicationError.getType().getValue()).isEqualTo(TypeMessage.TM_ERREUR);

        String xml = duplicationError.toXML();
        String[] xmlLines = xml.split("\n");
        assertThat(xmlLines[0]).isEqualTo("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        assertThat(xmlLines[1]).isEqualTo("<!DOCTYPE MSG SYSTEM \"MSG.dtd\">");
        assertThat(xmlLines[2]).isEqualTo("<MSG>");
        assertThat(xmlLines[3]).isEqualTo("    <ERREUR NOM=\"DUPLICATION\">");
        assertThat(xmlLines[4]).isEqualTo("        en double");
        assertThat(xmlLines[5]).isEqualTo("        <PDM ADRESSE=\"5° avenue\" NOM=\"Wall Book\"/>");
        assertThat(xmlLines[6]).isEqualTo("    </ERREUR>");
        assertThat(xmlLines[7]).isEqualTo("</MSG>");
        Erreur actualErrorFromXml = getErrorFromXml(xml);
        assertThat(actualErrorFromXml.getNom()).isEqualTo(duplicationError.getNom());
        assertThat(actualErrorFromXml.getRaison()).isEqualTo(duplicationError.getRaison());
        assertThat(actualErrorFromXml.getType().getValue()).isEqualTo(duplicationError.getType().getValue());
    }

    @Test
    public final void with_a_general_error_toXML_can_not_produce_message() throws UnsupportedEncodingException,
            SAXException, IOException {
        Erreur generalError = new Erreur("General Error", "description", "dte", "1234", "trop de blabla");

        assertThat(generalError.getNom()).isEqualTo("General Error");
        assertThat(generalError.getRaison()).isEqualTo("trop de blabla");
        assertThat(generalError.getType().getValue()).isEqualTo(TypeMessage.TM_ERREUR);

        String xml = generalError.toXML();
        String[] xmlLines = xml.split("\n");
        assertThat(xmlLines[0]).isEqualTo("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        assertThat(xmlLines[1]).isEqualTo("<!DOCTYPE MSG SYSTEM \"MSG.dtd\">");
        assertThat(xmlLines[2]).isEqualTo("<MSG>");
        assertThat(xmlLines[3]).isEqualTo("    <ERREUR NOM=\"General Error\">description</ERREUR>");
        assertThat(xmlLines[4]).isEqualTo("</MSG>");
        Erreur actualErrorFromXml = getErrorFromXml(xml);
        assertThat(actualErrorFromXml.getNom()).isEqualTo(generalError.getNom());
        assertThat(actualErrorFromXml.getRaison()).isEmpty();
        assertThat(actualErrorFromXml.getType().getValue()).isEqualTo(generalError.getType().getValue());
    }

    @Test
    public final void should_launch_main_program() {
        Erreur.main(null);
    }

    private Erreur getErrorFromXml(final String xml) throws SAXException, IOException, UnsupportedEncodingException {
        Document document = documentBuilder.parse(new ByteArrayInputStream(xml.getBytes("UTF-8")));
        Element root = document.getDocumentElement();
        NodeList noeuds = root.getChildNodes();
        Element type = (Element) noeuds.item(0);
        final Erreur erreur = new Erreur(type);
        return erreur;
    }

}
