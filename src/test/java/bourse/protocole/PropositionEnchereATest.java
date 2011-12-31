package bourse.protocole;

import static bourse.protocole.TypeMessage.TM_PROPOSITION_ENCHERE_A;
import static org.fest.assertions.Assertions.assertThat;

import org.junit.Test;

public class PropositionEnchereATest extends SAXTest {

    @Test
    public void should_create_instance_by_name_and_description() {
        PropositionEnchereA enchere = new PropositionEnchereA(42, 24f);

        assertThat(enchere.getType().getValue()).isEqualTo(TM_PROPOSITION_ENCHERE_A);
        assertThat(enchere.getEnchere()).isEqualTo(24f);
        assertThat(enchere.getNumero()).isEqualTo(42);
    }

    @Test
    public void should_create_instance_by_xml() {
        StringBuilder xmlBuilder = new StringBuilder();
        xmlBuilder.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        xmlBuilder.append("<!DOCTYPE MSG SYSTEM \"src/main/resources/MSG.dtd\">\n");
        xmlBuilder.append("<MSG>\n");
        xmlBuilder.append("<PROPOSITIONENCHERE NUMERO=\"42\">\n");
        xmlBuilder.append("<ENCHERE>24.0</ENCHERE>\n");
        xmlBuilder.append("</PROPOSITIONENCHERE>\n");
        xmlBuilder.append("</MSG>\n");
        Protocole newInstance = PropositionEnchereA.newInstance(xmlBuilder.toString());

        assertThat(newInstance).isNotNull();
        assertThat(newInstance).isInstanceOf(PropositionEnchereA.class);
        assertThat(newInstance.getType().getValue()).isEqualTo(TM_PROPOSITION_ENCHERE_A);
        PropositionEnchereA instance = (PropositionEnchereA) newInstance;
        assertThat(instance.getEnchere()).isEqualTo(24f);
        assertThat(instance.getNumero()).isEqualTo(42);
        assertThat(instance.toXML()).isEqualTo(xmlBuilder.toString().replace("src/main/resources/MSG.dtd", "MSG.dtd"));
    }

}
