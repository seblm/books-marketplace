package bourse.protocole;

import static org.fest.assertions.Assertions.assertThat;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.LinkedList;

import javax.xml.parsers.ParserConfigurationException;

import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class ResultAgentsTest extends SAXTest {

    @Test
    public final void testResultAgentsLinkedListOfString() {
        LinkedList<String> listeAgents = new LinkedList<String>();
        listeAgents.add(0, "groupe-E.seb");
        listeAgents.add(1, "groupe-E.eric");
        listeAgents.add(2, "groupe-E.arnaud");
        listeAgents.add(3, "groupe-E.protocoleman");
        ResultAgents resultAgents = new ResultAgents(listeAgents);

        assertThat(resultAgents.type).isEqualTo(TypeMessage.RESULTAGENTS);
        assertThat(resultAgents.getListeAgents()).containsExactly(listeAgents.toArray());
    }

    @Test
    public final void testResultAgentsElement() throws ParserConfigurationException, UnsupportedEncodingException,
            SAXException, IOException {
        String p = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + "<!DOCTYPE MSG SYSTEM \"MSG.dtd\">" + "<MSG>"
                + " <RESULTAGENTS>" + "  <AGENT NOM=\"groupe-E.seb\"/>" + "  <AGENT NOM=\"groupe-E.eric\"/>"
                + "  <AGENT NOM=\"groupe-E.arnaud\"/>" + "  <AGENT NOM=\"groupe-E.protocoleman\"/>"
                + " </RESULTAGENTS>" + "</MSG>";
        Document document = documentBuilder.parse(new ByteArrayInputStream(p.getBytes("UTF-8")));
        Element msg = document.getDocumentElement();
        NodeList noeuds = msg.getChildNodes();
        Node typeDOM = noeuds.item(0);
        assertThat(typeDOM.getNodeName()).isEqualTo("RESULTAGENTS");
        Element typeDOME = (Element) typeDOM;
        final ResultAgents resultAgents = new ResultAgents(typeDOME);
        assertThat(resultAgents.getListeAgents().size()).isEqualTo(4);
    }

    @Test
    public final void testToXML() {
        LinkedList<String> listeAgents = new LinkedList<String>();
        listeAgents.add(0, "groupe-E.seb");
        listeAgents.add(1, "groupe-E.eric");
        listeAgents.add(2, "groupe-E.arnaud");
        listeAgents.add(3, "groupe-E.protocoleman");
        String p = new ResultAgents(listeAgents).toXML();
        System.out.println(p);
    }

}
