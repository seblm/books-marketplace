package bourse.sdd;

import static org.fest.assertions.Assertions.assertThat;

import org.junit.Test;

public class PDMProTest {

    @Test
    public void should_create_a_default_PDMPro() {
        PDMPro pdmPro = new PDMPro();
        assertThat(pdmPro.getNom()).isNull();
        assertThat(pdmPro.getAdresse()).isNull();
    }

    @Test
    public void should_create_a_PDMPro() {
        PDMPro pdmPro = new PDMPro("myPDMPro", "myAddress");
        assertThat(pdmPro.getNom()).isEqualTo("myPDMPro");
        assertThat(pdmPro.getAdresse()).isEqualTo("myAddress");
    }

    @Test
    public void should_set_values_of_a_PDMPro() {
        PDMPro pdmPro = new PDMPro();
        pdmPro.setNom("myPDMPro");
        assertThat(pdmPro.getNom()).isEqualTo("myPDMPro");
        pdmPro.setAdresse("myAddress");
        assertThat(pdmPro.getAdresse()).isEqualTo("myAddress");
    }
}
