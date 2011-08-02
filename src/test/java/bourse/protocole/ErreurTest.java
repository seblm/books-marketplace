package bourse.protocole;

import static org.fest.assertions.Assertions.assertThat;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

public class ErreurTest {

	@Test
	public final void testNewErreurWithDomElement()
			throws UnsupportedEncodingException, SAXException, IOException,
			ParserConfigurationException {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		// D'après le tutorial JAXP, ces variables fixées à true permettent à
		// l'application de se concentrer sur l'analyse sémantique.
		factory.setCoalescing(true);
		factory.setExpandEntityReferences(true);
		factory.setIgnoringComments(true);
		factory.setIgnoringElementContentWhitespace(true);
		// factory.setValidating(true);
		DocumentBuilder builder = factory.newDocumentBuilder();

		// La définition de ErrorHandler est inspirée de
		// http://java.sun.com/j2ee/1.4/docs/tutorial/doc/JAXPDOM3.html#wp64106
		builder.setErrorHandler(new org.xml.sax.ErrorHandler() {
			// ignore fatal errors (an exception is guaranteed)
			public void fatalError(SAXParseException exception)
					throws SAXException {
			}

			// treat validation errors as fatal
			public void error(SAXParseException e) throws SAXParseException {
				throw e;
			}

			// dump warnings too
			public void warning(SAXParseException err) throws SAXParseException {
				System.out.println("** Warning" + ", line "
						+ err.getLineNumber() + ", uri " + err.getSystemId());
				System.out.println("   " + err.getMessage());
			}
		});
		final String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<MSG>\n<ERREUR NOM=\"nom\">message</ERREUR>\n</MSG>\n";
		Document document = builder.parse(
				new ByteArrayInputStream(
						xml.getBytes("UTF-8")), Protocole.BASE_DTD);
		Element root = document.getDocumentElement();
		NodeList noeuds = root.getChildNodes();
		Element typeDOM = (Element) noeuds.item(1);
		Element typeDOME = (Element) typeDOM;
		final Erreur erreur = new Erreur(typeDOME);
		assertThat(erreur.getNom()).isEqualTo("nom");
		assertThat(erreur.getRaison()).isEqualTo("raison");
		assertThat(erreur.getType()).isEqualTo(
				new TypeMessage(TypeMessage.TM_ERREUR));
	}

	@Test
	public final void testInstanciation() {
		Erreur e = new Erreur("Zerovente", "description", "dte", "1234",
				"trop de blabla");
		assertThat(e.getNom()).isEqualTo("Zerovente");
		assertThat(e.getRaison()).isEqualTo("trop de blabla");
		assertThat(e.getType()).isEqualTo(
				new TypeMessage(TypeMessage.TM_ERREUR));
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
