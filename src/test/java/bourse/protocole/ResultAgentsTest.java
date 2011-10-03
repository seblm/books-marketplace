package bourse.protocole;

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Lists.newLinkedList;
import static org.fest.assertions.Assertions.assertThat;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Iterator;
import java.util.LinkedList;

import javax.xml.parsers.ParserConfigurationException;

import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.google.common.collect.Iterators;

public class ResultAgentsTest extends SAXTest {

    @Test
    public final void should_create_ResultAgents_with_a_LinkedList_of_String() {
        LinkedList<String> listeAgents = newLinkedList(newArrayList("groupe-E.seb", "groupe-E.eric", "groupe-E.arnaud",
                "groupe-E.protocoleman"));

        ResultAgents resultAgents = new ResultAgents(listeAgents);

        assertThat(resultAgents.type.getValue()).isEqualTo(TypeMessage.TM_RESULT_AGENTS);
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
        String p = new ResultAgents(newLinkedList(newArrayList("groupe-E.seb", "groupe-E.eric", "groupe-E.arnaud",
                "groupe-E.protocoleman"))).toXML();

        Iterator<String> xml = Iterators.forArray(p.split("\n"));
        assertThat(xml.next()).isEqualTo("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        assertThat(xml.next()).isEqualTo("<!DOCTYPE MSG SYSTEM \"MSG.dtd\">");
        assertThat(xml.next()).isEqualTo("<MSG>");
        assertThat(xml.next()).isEqualTo("<RESULTAGENTS>");
        assertThat(xml.next()).isEqualTo("<AGENT NOM=\"groupe-E.seb\"/>");
        assertThat(xml.next()).isEqualTo("<AGENT NOM=\"groupe-E.eric\"/>");
        assertThat(xml.next()).isEqualTo("<AGENT NOM=\"groupe-E.arnaud\"/>");
        assertThat(xml.next()).isEqualTo("<AGENT NOM=\"groupe-E.protocoleman\"/>");
        assertThat(xml.next()).isEqualTo("</RESULTAGENTS>");
        assertThat(xml.next()).isEqualTo("</MSG>");
        assertThat(xml.hasNext()).isFalse();
    }

}
