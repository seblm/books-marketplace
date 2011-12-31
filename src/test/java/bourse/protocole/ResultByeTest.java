package bourse.protocole;

import static bourse.protocole.TypeMessage.TM_RESULT_BYE;
import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Lists.newLinkedList;
import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Mockito.mock;

import org.junit.Test;

import bourse.sdd.PDMPro;

public class ResultByeTest extends SAXTest {

    @Test
    public void should_create_instance_by_name_and_description() {
        PDMPro pdm1 = mock(PDMPro.class);
        PDMPro pdm2 = mock(PDMPro.class);
        ResultBye resultBye = new ResultBye(newLinkedList(newArrayList(pdm1, pdm2)));

        assertThat(resultBye.getListepdm()).containsExactly(pdm1, pdm2);
        assertThat(resultBye.getType().getValue()).isEqualTo(TM_RESULT_BYE);
    }

    @Test
    public void should_create_instance_by_xml() {
        StringBuilder xmlBuilder = new StringBuilder();
        xmlBuilder.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        xmlBuilder.append("<!DOCTYPE MSG SYSTEM \"src/main/resources/MSG.dtd\">\n");
        xmlBuilder.append("<MSG>\n");
        xmlBuilder.append("<RESULTBYE>\n");
        xmlBuilder.append("<PDM ADRESSE=\"\" NOM=\"\"/>\n");
        xmlBuilder.append("<PDM ADRESSE=\"foo\" NOM=\"bar\"/>\n");
        xmlBuilder.append("</RESULTBYE>\n");
        xmlBuilder.append("</MSG>\n");
        Protocole newInstance = ResultBye.newInstance(xmlBuilder.toString());

        assertThat(newInstance).isNotNull();
        assertThat(newInstance).isInstanceOf(ResultBye.class);
        assertThat(newInstance.getType().getValue()).isEqualTo(TM_RESULT_BYE);
        ResultBye instance = (ResultBye) newInstance;
        assertThat(instance.getListepdm()).hasSize(2);
        assertThat(instance.getListepdm().get(0)).isInstanceOf(PDMPro.class);
        PDMPro pdm1 = (PDMPro) instance.getListepdm().get(0);
        assertThat(pdm1.getAdresse()).isEmpty();
        assertThat(pdm1.getNom()).isEmpty();
        assertThat(instance.getListepdm().get(1)).isInstanceOf(PDMPro.class);
        PDMPro pdm2 = (PDMPro) instance.getListepdm().get(1);
        assertThat(pdm2.getAdresse()).isEqualTo("foo");
        assertThat(pdm2.getNom()).isEqualTo("bar");
        assertThat(instance.toXML()).isEqualTo(xmlBuilder.toString().replace("src/main/resources/MSG.dtd", "MSG.dtd"));
    }

}
