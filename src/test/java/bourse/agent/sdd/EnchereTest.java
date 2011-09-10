package bourse.agent.sdd;

import static org.fest.assertions.Assertions.assertThat;

import org.junit.Test;

import bourse.protocole.Categorie;
import bourse.sdd.Livre;

public class EnchereTest {

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

}
