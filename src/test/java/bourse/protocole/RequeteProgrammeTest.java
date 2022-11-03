package bourse.protocole;

import static bourse.protocole.TypeMessage.TM_REQUETE_PROGRAMME;
import static org.fest.assertions.Assertions.assertThat;

import org.junit.Test;

public class RequeteProgrammeTest extends SAXTest {

    @Test
    public void should_create_instance_by_name_and_description() {
        RequeteProgramme resultat = new RequeteProgramme();

        assertThat(resultat.getType().getValue()).isEqualTo(TM_REQUETE_PROGRAMME);
    }

    @Test
    public void should_create_instance_by_xml() {
        StringBuilder xmlBuilder = new StringBuilder();
        xmlBuilder.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        xmlBuilder.append("<!DOCTYPE MSG SYSTEM \"src/main/resources/MSG.dtd\">\n");
        xmlBuilder.append("<MSG>\n");
        xmlBuilder.append("    <REQUETEPROGRAMME/>\n");
        xmlBuilder.append("</MSG>\n");
        Protocole newInstance = RequeteProgramme.newInstance(xmlBuilder.toString());

        assertThat(newInstance).isNotNull();
        assertThat(newInstance).isInstanceOf(RequeteProgramme.class);
        assertThat(newInstance.getType().getValue()).isEqualTo(TM_REQUETE_PROGRAMME);
        RequeteProgramme instance = (RequeteProgramme) newInstance;
        assertThat(instance.toXML()).isEqualTo(xmlBuilder.toString().replace("src/main/resources/MSG.dtd", "MSG.dtd"));
    }

}
