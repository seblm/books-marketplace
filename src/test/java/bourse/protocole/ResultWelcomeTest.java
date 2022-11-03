package bourse.protocole;

import static bourse.protocole.Categorie.AUCUNE;
import static bourse.protocole.Categorie.newCategorieFromBd;
import static bourse.protocole.TypeMessage.TM_RESULT_WELCOME;
import static org.fest.assertions.Assertions.assertThat;

import org.junit.Test;

public class ResultWelcomeTest extends SAXTest {

    @Test
    public void should_create_instance_by_name_and_description() {
        ResultWelcome resultWelcome = new ResultWelcome(42f, newCategorieFromBd(AUCUNE));

        assertThat(resultWelcome.getCategorie().getCategorie()).isEqualTo(AUCUNE);
        assertThat(resultWelcome.getSolde()).isEqualTo(42f);
        assertThat(resultWelcome.getType().getValue()).isEqualTo(TM_RESULT_WELCOME);
    }

    @Test
    public void should_create_instance_by_xml() {
        StringBuilder xmlBuilder = new StringBuilder();
        xmlBuilder.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        xmlBuilder.append("<!DOCTYPE MSG SYSTEM \"src/main/resources/MSG.dtd\">\n");
        xmlBuilder.append("<MSG>\n");
        xmlBuilder.append("    <RESULTWELCOME>\n");
        xmlBuilder.append("        <SOLDE>42.0</SOLDE>\n");
        xmlBuilder.append("        <CATEGORIE>Aucune</CATEGORIE>\n");
        xmlBuilder.append("    </RESULTWELCOME>\n");
        xmlBuilder.append("</MSG>\n");
        Protocole newInstance = ResultWelcome.newInstance(xmlBuilder.toString());

        assertThat(newInstance).isNotNull();
        assertThat(newInstance).isInstanceOf(ResultWelcome.class);
        assertThat(newInstance.getType().getValue()).isEqualTo(TM_RESULT_WELCOME);
        ResultWelcome instance = (ResultWelcome) newInstance;
        assertThat(instance.getCategorie().getCategorie()).isEqualTo(AUCUNE);
        assertThat(instance.getSolde()).isEqualTo(42f);
        assertThat(instance.toXML()).isEqualTo(xmlBuilder.toString().replace("src/main/resources/MSG.dtd", "MSG.dtd"));
    }

}
