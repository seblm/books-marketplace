package bourse.agent.sdd;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.verifyNew;
import static org.powermock.api.mockito.PowerMockito.whenNew;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import bourse.reseau.Ip;

@RunWith(PowerMockRunner.class)
@PrepareForTest(Pdm.class)
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

    @Test
    public void should_launch_main_program() throws Exception {
        Pdm pdm = mock(Pdm.class);
        whenNew(Pdm.class).withArguments(anyString(), anyString()).thenReturn(pdm);

        Pdm.main(null);

        verifyNew(Pdm.class).withArguments("Groupe-E", "192.168.1.2:8080");
        verify(pdm, times(1)).toString(5);
    }

}
