package bourse.protocole;

import static bourse.protocole.TypeMessage.TM_REQUETE_AGENTS;
import static org.fest.assertions.Assertions.assertThat;

import org.junit.Test;

public class RequeteAgentsTest extends SAXTest {

    @Test
    public void should_create_instance_by_name_and_description() {
        RequeteAgents resultat = new RequeteAgents("comment");

        assertThat(resultat.getType().getValue()).isEqualTo(TM_REQUETE_AGENTS);
    }

    @Test
    public void should_create_instance_by_xml() {
        StringBuilder xmlBuilder = new StringBuilder();
        xmlBuilder.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        xmlBuilder.append("<!DOCTYPE MSG SYSTEM \"src/main/resources/MSG.dtd\">\n");
        xmlBuilder.append("<MSG>\n");
        xmlBuilder.append("    <REQUETEAGENTS/>\n");
        xmlBuilder.append("</MSG>\n");
        Protocole newInstance = RequeteAgents.newInstance(xmlBuilder.toString());

        assertThat(newInstance).isNotNull();
        assertThat(newInstance).isInstanceOf(RequeteAgents.class);
        assertThat(newInstance.getType().getValue()).isEqualTo(TM_REQUETE_AGENTS);
        RequeteAgents instance = (RequeteAgents) newInstance;
        assertThat(instance.toXML()).isEqualTo(xmlBuilder.toString().replace("src/main/resources/MSG.dtd", "MSG.dtd"));
    }

}
