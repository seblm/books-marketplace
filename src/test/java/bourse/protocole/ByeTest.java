package bourse.protocole;

import static bourse.protocole.TypeMessage.TM_BYE;
import static org.fest.assertions.Assertions.assertThat;

import org.junit.Test;

public class ByeTest extends SAXTest {

    @Test
    public void should_create_instance_by_name_and_description() {
        Bye bye = new Bye("comment");

        assertThat(bye.getType().getValue()).isEqualTo(TM_BYE);
    }

    @Test
    public void should_create_instance_by_xml() {
        StringBuilder xmlBuilder = new StringBuilder();
        xmlBuilder.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        xmlBuilder.append("<!DOCTYPE MSG SYSTEM \"src/main/resources/MSG.dtd\">\n");
        xmlBuilder.append("<MSG>\n");
        xmlBuilder.append("<BYE/>\n");
        xmlBuilder.append("</MSG>\n");
        Protocole newInstance = Bye.newInstance(xmlBuilder.toString());

        assertThat(newInstance).isNotNull();
        assertThat(newInstance).isInstanceOf(Bye.class);
        assertThat(newInstance.getType().getValue()).isEqualTo(TM_BYE);
        Bye instance = (Bye) newInstance;
        assertThat(instance.toXML()).isEqualTo(xmlBuilder.toString().replace("src/main/resources/MSG.dtd", "MSG.dtd"));
    }
}
