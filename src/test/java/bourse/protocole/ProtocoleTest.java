package bourse.protocole;

import static org.fest.assertions.Assertions.assertThat;

import org.junit.Test;

public class ProtocoleTest extends SAXTest {

    @Test
    public final void should_create_error_with_valid_xml() {
        Protocole protocole = Protocole.newInstance("<protocole></protocole>");

        assertThat(protocole).isNotNull();
        assertThat(protocole.getType().getValue()).isEqualTo(TypeMessage.TM_ERREUR);
    }

}
