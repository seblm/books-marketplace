package bourse.protocole;

import static org.fest.assertions.Assertions.assertThat;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.junit.BeforeClass;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

public class ProposeVenteTest {

	private static DocumentBuilderFactory factory;

	private static DocumentBuilder documentBuilder;

	@BeforeClass
	public static void initDocumentBuilderFactory() throws ParserConfigurationException {
		factory = DocumentBuilderFactory.newInstance();
		factory.setCoalescing(true);
		factory.setExpandEntityReferences(true);
		factory.setIgnoringComments(true);
		factory.setIgnoringElementContentWhitespace(true);
		documentBuilder = factory.newDocumentBuilder();
		documentBuilder.setErrorHandler(new ErrorHandler() {
			public void fatalError(SAXParseException exception) throws SAXException {
			}

			public void error(SAXParseException e) throws SAXParseException {
				throw e;
			}

			public void warning(SAXParseException err) throws SAXParseException {
			}
		});
	}

	@Test
	public void testProposeVenteElement() throws ParserConfigurationException, UnsupportedEncodingException,
			SAXException, IOException {
		Document document = documentBuilder
				.parse(new ByteArrayInputStream(
						"<?xml version=\"1.0\" encoding=\"UTF-8\"?><MSG><PROPOSEVENTE NOM=\"EnchereUn\"><LIVRE ID=\"12\"/><ENCHERE>1546.0</ENCHERE></PROPOSEVENTE></MSG>"
								.getBytes("UTF-8")), Protocole.BASE_DTD);
		Element root = document.getDocumentElement();
		NodeList noeuds = root.getChildNodes();
		Element typeDOM = (Element) noeuds.item(0);
		Element typeDOME = (Element) typeDOM;
		final ProposeVente proposeVente = new ProposeVente(typeDOME);
		assertThat(proposeVente.getId()).isEqualTo(12);
		assertThat(proposeVente.getNom()).isEqualTo(1);
		assertThat(proposeVente.getPrix()).isEqualTo(1546);
		assertThat(proposeVente.getType()).isEqualTo(TypeMessage.TM_PROPOSE_VENTE);
	}

	@Test
	public void testToXML() {
		ProposeVente proposeVente = new ProposeVente("Montante", 1546, 12);
		System.out.println("PROPOSEVENTE");
		System.out.println(proposeVente.toXML());
		assertThat(proposeVente.toXML()).isEqualTo(
				"<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<!DOCTYPE MSG SYSTEM \"MSG.dtd\">\n<MSG>\n"
						+ "<PROPOSEVENTE NOM=\"Montante\">\n<LIVRE ID=\"12\"/>\n<ENCHERE>1546.0</ENCHERE>\n"
						+ "</PROPOSEVENTE>\n</MSG>\n");
	}

}
