package bourse.agent;

import static org.fest.assertions.Assertions.assertThat;

import org.junit.Test;

import bourse.agent.sdd.Enchere;
import bourse.protocole.Categorie;
import bourse.sdd.Livre;

public class EnvironnementTest {

    @Test
    public void test() {
        final Categorie categorie = new Categorie(Categorie.SF);
        Livre l1 = new bourse.sdd.Livre("l1", "a2", categorie, "poche", "O'reilly", (float) 50.65, (float) 0.45,
                "2004-01-01", "222222222", 1, "arno", (float) 12);
        Environnement e = new Environnement(new Enchere(12, l1, (float) 9.67, 25, (float) 5, 1, "Protocol'man"), 1, 0);
        final String environment = e.toString(0);
        assertThat(environment)
                .isEqualTo(
                        "enchère =\n"
                                + " tour = 12, type = 1, temps = 25, pas = 5.0, enchérisseur = Protocol'man, prix = 9.67, prix maximum = 0.0, livre = \n"
                                + "  Auteur = a2, Catégorie = Science fiction, Date Parution = 2004-01-01, Editeur = O'reilly, Etat = 0.45, Format = poche, Id = 1, Isbn = 222222222, Prix = 50.65, Titre = l1, Proprietaire = arno, PrixAchat = 12.0\n"
                                + "typeEnchèreDemandée = 1 nombreActions = 0 dateListeAgent = 0 dateListeProgramme = 0 enchèreInteressante = false");
    }

}
