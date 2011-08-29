package bourse.protocole;

import static org.fest.assertions.Assertions.assertThat;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import javax.xml.parsers.ParserConfigurationException;

import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class ErreurTest extends SAXTest {

	@Test
	public final void testNewErreurWithDomElement()
			throws UnsupportedEncodingException, SAXException, IOException,
			ParserConfigurationException {
		final String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<!DOCTYPE MSG SYSTEM \"MSG.dtd\">\n<MSG>\n<ERREUR NOM=\"nom\"></ERREUR>\n</MSG>\n";
		Document document = documentBuilder.parse(
				new ByteArrayInputStream(
						xml.getBytes("UTF-8")));
		Element root = document.getDocumentElement();
		NodeList noeuds = root.getChildNodes();
		Element typeDOM = (Element) noeuds.item(0);
		Element typeDOME = (Element) typeDOM;
		final Erreur erreur = new Erreur(typeDOME);
		assertThat(erreur.getNom()).isEqualTo("nom");
		assertThat(erreur.getType()).isEqualTo(TypeMessage.ERREUR);
	}

	@Test
	public final void testInstanciation() {
		Erreur e = new Erreur("Zerovente", "description", "dte", "1234",
				"trop de blabla");
		assertThat(e.getNom()).isEqualTo("Zerovente");
		assertThat(e.getRaison()).isEqualTo("trop de blabla");
		assertThat(e.getType()).isEqualTo(TypeMessage.ERREUR);
	}

	@Test
	public final void testToXML() {
		final Erreur erreur = new Erreur("nom", "message", "pdmnom", "adresse",
				"raison");
		assertThat(erreur.toXML()).isEqualTo(
				"<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
						+ "<!DOCTYPE MSG SYSTEM \"MSG.dtd\">\n" + "<MSG>\n"
						+ "<ERREUR NOM=\"nom\">message</ERREUR>\n" + "</MSG>\n");
	}

}
