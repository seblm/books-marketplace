package bourse.protocole;

import static org.fest.assertions.Assertions.assertThat;

import org.junit.Test;

public class WelcomeTest extends SAXTest {

    @Test
    public void should_create_instance_by_name_and_description() {
        Welcome welcome = new Welcome("name", "desc");

        assertThat(welcome.getNom()).isEqualTo("name");
        assertThat(welcome.getType().getValue()).isEqualTo(TypeMessage.TM_WELCOME);
    }

    @Test
    public void should_create_instance_by_xml() {
        StringBuilder xmlBuilder = new StringBuilder();
        xmlBuilder.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        xmlBuilder.append("<!DOCTYPE MSG SYSTEM \"src/main/resources/MSG.dtd\">\n");
        xmlBuilder.append("<MSG>\n");
        xmlBuilder.append("<WELCOME>\n");
        xmlBuilder.append("<AGENT NOM=\"name\">desc</AGENT>\n");
        xmlBuilder.append("</WELCOME>\n");
        xmlBuilder.append("</MSG>\n");
        Protocole newInstance = Welcome.newInstance(xmlBuilder.toString());

        assertThat(newInstance).isNotNull();
        assertThat(newInstance).isInstanceOf(Welcome.class);
        assertThat(newInstance.getType().getValue()).isEqualTo(TypeMessage.TM_WELCOME);
        Welcome instance = (Welcome) newInstance;
        assertThat(instance.getNom()).isEqualTo("name");
        assertThat(instance.toXML()).isEqualTo(xmlBuilder.toString().replace("src/main/resources/MSG.dtd", "MSG.dtd"));
    }

}
