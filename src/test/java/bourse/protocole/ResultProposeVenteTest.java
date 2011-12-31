package bourse.protocole;

import static bourse.protocole.TypeMessage.TM_RESULT_PROPOSE_VENTE;
import static org.fest.assertions.Assertions.assertThat;

import org.junit.Test;

public class ResultProposeVenteTest extends SAXTest {

    @Test
    public void should_create_instance_by_name_and_description() {
        ResultProposeVente resultProposeVente = new ResultProposeVente(42);

        assertThat(resultProposeVente.getId()).isEqualTo(42);
        assertThat(resultProposeVente.getType().getValue()).isEqualTo(TM_RESULT_PROPOSE_VENTE);
    }

    @Test
    public void should_create_instance_by_xml() {
        StringBuilder xmlBuilder = new StringBuilder();
        xmlBuilder.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        xmlBuilder.append("<!DOCTYPE MSG SYSTEM \"src/main/resources/MSG.dtd\">\n");
        xmlBuilder.append("<MSG>\n");
        xmlBuilder.append("<RESULTPROPOSEVENTE>\n");
        xmlBuilder.append("<LIVRE ID=\"42\"/>\n");
        xmlBuilder.append("</RESULTPROPOSEVENTE>\n");
        xmlBuilder.append("</MSG>\n");
        Protocole newInstance = ResultProposeVente.newInstance(xmlBuilder.toString());

        assertThat(newInstance).isNotNull();
        assertThat(newInstance).isInstanceOf(ResultProposeVente.class);
        assertThat(newInstance.getType().getValue()).isEqualTo(TM_RESULT_PROPOSE_VENTE);
        ResultProposeVente instance = (ResultProposeVente) newInstance;
        assertThat(instance.getId()).isEqualTo(42);
        assertThat(instance.toXML()).isEqualTo(xmlBuilder.toString().replace("src/main/resources/MSG.dtd", "MSG.dtd"));
    }

}
