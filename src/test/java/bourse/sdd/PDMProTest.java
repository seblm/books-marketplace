package bourse.sdd;

import org.fest.assertions.Assertions;
import org.junit.Test;

public class PDMProTest {

    @Test
    public void should_create_a_default_PDMPro() {
        PDMPro pdmPro = new PDMPro();
        Assertions.assertThat(pdmPro.getNom()).isNull();
        Assertions.assertThat(pdmPro.getAdresse()).isNull();
    }

    @Test
    public void should_create_a_PDMPro() {
        PDMPro pdmPro = new PDMPro("myPDMPro", "myAddress");
        Assertions.assertThat(pdmPro.getNom()).isEqualTo("myPDMPro");
        Assertions.assertThat(pdmPro.getAdresse()).isEqualTo("myAddress");
    }
}
