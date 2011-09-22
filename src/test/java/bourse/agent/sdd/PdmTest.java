package bourse.agent.sdd;

import static org.fest.assertions.Assertions.assertThat;

import org.junit.Test;

import bourse.reseau.Ip;

public class PdmTest {

    @Test
    public void should_create_Pdm() {
        Pdm pdm = new Pdm("name", "192.168.1.1:8080");

        assertThat(pdm.getNom()).isEqualTo("name");
        assertThat(pdm.getAdresse().toString()).isEqualTo("192.168.1.1:8080");
    }

    @Test
    public void should_change_Pdm_name() {
        Pdm pdm = new Pdm("oldName", "192.168.1.1:8080");

        pdm.setNom("newName");

        assertThat(pdm.getNom()).isEqualTo("newName");
    }

    @Test
    public void should_change_Pdm_address() {
        Pdm pdm = new Pdm("oldName", "192.168.1.1:8080");

        pdm.setAdresse(new Ip("127.0.0.1:21"));

        assertThat(pdm.getAdresse().toString()).isEqualTo("127.0.0.1:21");
    }

}
