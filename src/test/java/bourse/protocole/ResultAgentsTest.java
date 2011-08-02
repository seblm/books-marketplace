package bourse.protocole;

import static org.fest.assertions.Assertions.assertThat;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.LinkedList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

public class ResultAgentsTest {

	@Test
	public final void testResultAgentsLinkedListOfString() {
        LinkedList<String> listeAgents=new LinkedList<String>();
        listeAgents.add(0,"groupe-E.seb");
        listeAgents.add(1,"groupe-E.eric");
        listeAgents.add(2,"groupe-E.arnaud");
        listeAgents.add(3,"groupe-E.protocoleman");
        ResultAgents resultAgents = new ResultAgents(listeAgents);
        
        assertThat(resultAgents.type).isEqualTo(new TypeMessage(TypeMessage.TM_RESULT_AGENTS));
        assertThat(resultAgents.getListeAgents()).containsExactly(listeAgents.toArray());
	}

	@Test
	public final void testResultAgentsElement() throws ParserConfigurationException, UnsupportedEncodingException, SAXException, IOException {
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
        String p = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
				+ "<MSG>"
				+ " <RESULTAGENTS>"
				+ "  <AGENT NOM=\"groupe-E.seb\"/>"
				+ "  <AGENT NOM=\"groupe-E.eric\"/>"
				+ "  <AGENT NOM=\"groupe-E.arnaud\"/>"
				+ "  <AGENT NOM=\"groupe-E.protocoleman\"/>"
				+ " </RESULTAGENTS>"
				+ "</MSG>";
        Document document = builder.parse(new ByteArrayInputStream(p.getBytes("UTF-8")), Protocole.BASE_DTD);
        Element msg = document.getDocumentElement();
        NodeList noeuds = msg.getChildNodes();
        Node typeDOM = noeuds.item(1);
        assertThat(typeDOM.getNodeName()).isEqualTo("RESULTAGENTS");
        Element typeDOME = (Element)typeDOM;
        final ResultAgents resultAgents = new ResultAgents(typeDOME);
        assertThat(resultAgents.getListeAgents().size()).isEqualTo(4);
	}

	@Test
	public final void testToXML() {
        LinkedList<String> listeAgents=new LinkedList<String>();
        listeAgents.add(0,"groupe-E.seb");
        listeAgents.add(1,"groupe-E.eric");
        listeAgents.add(2,"groupe-E.arnaud");
        listeAgents.add(3,"groupe-E.protocoleman");
        String p = new ResultAgents(listeAgents).toXML();
        System.out.println(p);
	}

}
