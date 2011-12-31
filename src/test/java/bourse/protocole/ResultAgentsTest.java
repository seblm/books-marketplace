package bourse.protocole;

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Lists.newLinkedList;
import static org.fest.assertions.Assertions.assertThat;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.LinkedList;

import javax.xml.parsers.ParserConfigurationException;

import org.junit.Test;
import org.xml.sax.SAXException;

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
    public final void should_create_instance_by_xml() throws ParserConfigurationException,
            UnsupportedEncodingException, SAXException, IOException {
        StringBuilder xmlBuilder = new StringBuilder();
        xmlBuilder.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        xmlBuilder.append("<!DOCTYPE MSG SYSTEM \"src/main/resources/MSG.dtd\">\n");
        xmlBuilder.append("<MSG>\n");
        xmlBuilder.append("<RESULTAGENTS>\n");
        xmlBuilder.append("<AGENT NOM=\"groupe-E.seb\"/>\n");
        xmlBuilder.append("<AGENT NOM=\"groupe-E.eric\"/>\n");
        xmlBuilder.append("<AGENT NOM=\"groupe-E.arnaud\"/>\n");
        xmlBuilder.append("<AGENT NOM=\"groupe-E.protocoleman\"/>\n");
        xmlBuilder.append("</RESULTAGENTS>\n");
        xmlBuilder.append("</MSG>\n");
        final Protocole resultAgents = ResultAgents.newInstance(xmlBuilder.toString());

        assertThat(resultAgents).isNotNull();
        assertThat(resultAgents).isInstanceOf(ResultAgents.class);
        assertThat(resultAgents.getType().getValue()).isEqualTo(TypeMessage.TM_RESULT_AGENTS);
        ResultAgents instance = (ResultAgents) resultAgents;
        assertThat(instance.getListeAgents()).hasSize(4);
        assertThat(instance.getListeAgents()).containsExactly("groupe-E.seb", "groupe-E.eric", "groupe-E.arnaud",
                "groupe-E.protocoleman");
        assertThat(instance.toXML()).isEqualTo(xmlBuilder.toString().replace("src/main/resources/MSG.dtd", "MSG.dtd"));
    }

}
