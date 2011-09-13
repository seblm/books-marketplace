package bourse.agent.sdd;

import static org.fest.assertions.Assertions.assertThat;

import org.junit.Test;

public class PdmTest {

    @Test
    public void should_create_Pdm() {
        Pdm pdm = new Pdm("name", "192.168.1.1:8080");

        assertThat(pdm.getNom()).isEqualTo("name");
        assertThat(pdm.getAdresse().toString()).isEqualTo("192.168.1.1:8080");
    }

}
