package bourse.agent.sdd;

import static bourse.agent.sdd.Enchere.enchereToCode;
import static org.fest.assertions.Assertions.assertThat;
import static org.powermock.api.mockito.PowerMockito.whenNew;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import bourse.protocole.Categorie;
import bourse.sdd.Livre;

@RunWith(PowerMockRunner.class)
public class EnchereTest {

    @Mock
    private Livre livre;

    @Test
    public final void instantiationAndToString() {
        Livre l1 = new Livre("l1", "a2", new Categorie(), "poche", "O'reilly", 50.65f, 0.45f, "2004-01-01",
                "222222222", 1, "Seb", 65f);
        Livre l2 = new Livre("l2", "a1", new Categorie(), "poche", "Casterman", 40.75f, 0.85f, "1954-04-12",
                "222XX2254", 2, "protocoleman", 50f);
        Livre l3 = new Livre("l2", "a1", new Categorie(), "poche", "Casterman", 40.75f, 0.27f, "1954-04-12",
                "222XX2254", 3, "arno", 12f);
        Enchere e2 = new Enchere(2, l2, 20f, 13, 1.2f, 3, "a1");
        Enchere e1 = new Enchere(1, l1, 19.81f, 12, 1.2f, 3, "a1");
        Enchere e3 = new Enchere(3, l3, 10.01f, 14, 1.2f, 3, "a1");

        assertThat(e1.toString(1))
                .startsWith(
                        " tour = 1, type = 3, temps = 12, pas = 1.2, enchérisseur = a1, prix = 19.81, prix maximum = 0.0, livre = \n  Auteur = a2");
        assertThat(e2.toString(2))
                .startsWith(
                        "  tour = 2, type = 3, temps = 13, pas = 1.2, enchérisseur = a1, prix = 20.0, prix maximum = 0.0, livre = \n   Auteur = a1");
        assertThat(e3.toString(4))
                .startsWith(
                        "    tour = 3, type = 3, temps = 14, pas = 1.2, enchérisseur = a1, prix = 10.01, prix maximum = 0.0, livre = \n     Auteur = a1");
    }

    @Test
    public final void should_convert_enchere_string_to_integer() {
        assertThat(enchereToCode("EnchereUn")).isEqualTo(1);
        assertThat(enchereToCode("EnchereDeux")).isEqualTo(2);
        assertThat(enchereToCode("EnchereTrois")).isEqualTo(3);
        assertThat(enchereToCode("EnchereQuatre")).isEqualTo(4);
        assertThat(enchereToCode("EnchereCinq")).isEqualTo(5);
        assertThat(enchereToCode("foo")).isEqualTo(0);
    }

    @Test
    @PrepareForTest(Enchere.class)
    public final void should_creates_a_new_enchere_with_default_values() throws Exception {
        whenNew(Livre.class).withNoArguments().thenReturn(livre);
        final Enchere enchere = new Enchere();
        assertThat(enchere.getTemps()).isEqualTo(0);
        assertThat(enchere.getPas()).isEqualTo(0);
        assertThat(enchere.getType()).isEqualTo(0);
        assertThat(enchere.getAgent()).isNull();
        assertThat(enchere.getNumeroEnchere()).isEqualTo(1);
        assertThat(enchere.getLivre()).isEqualTo(livre);
        assertThat(enchere.getValeurEnchere()).isEqualTo(0f);
        assertThat(enchere.getPrixMaximum()).isEqualTo(0f);
    }

    @Test
    public final void should_copy_enchere() {
        final Enchere sourceEnchere = new Enchere(34, livre, 5.3f, 4, -3, 3, "nomag");
        Livre sourceLivre = sourceEnchere.getLivre();
        final Enchere destinationEnchere = new Enchere(sourceEnchere);

        assertThat(destinationEnchere.getTemps()).isEqualTo(4);
        assertThat(destinationEnchere.getPas()).isEqualTo(-3);
        assertThat(destinationEnchere.getType()).isEqualTo(3);
        assertThat(destinationEnchere.getAgent()).isEqualTo("nomag");
        assertThat(destinationEnchere.getNumeroEnchere()).isEqualTo(34);
        assertThat(destinationEnchere.getLivre()).isSameAs(sourceLivre);
        assertThat(destinationEnchere.getValeurEnchere()).isEqualTo(5.3f);
        assertThat(destinationEnchere.getPrixMaximum()).isEqualTo(0f);
    }

}
