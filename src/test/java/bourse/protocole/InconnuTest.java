package bourse.protocole;

import static org.fest.assertions.Assertions.assertThat;

import org.junit.Test;

public class InconnuTest extends SAXTest {

    @Test
    public void should_create_instance_by_name_and_description() {
        Inconnu inconnu = new Inconnu();

        assertThat(inconnu.getType().getValue()).isEqualTo(TypeMessage.TM_INCONNU);
    }

    @Test
    public void should_not_export_as_xml() {
        Inconnu inconnu = new Inconnu();
        assertThat(inconnu.toXML()).isNull();
    }
}
