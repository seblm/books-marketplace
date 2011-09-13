package bourse.agent.sdd;

import static org.fest.assertions.Assertions.assertThat;
import static org.junit.Assert.fail;

import org.junit.Test;

import bourse.protocole.Categorie;
import bourse.sdd.Livre;

public class EncheresTest {

    @Test
    public void with_an_empty_enchere_should_print_nothing() {
        Encheres encheres = new Encheres();
        assertThat(encheres.toString(4)).isEqualTo("");
    }

    @Test
    public void with_encheres_should_print_the_enchere() {
        Encheres encheres = new Encheres();
        Enchere enchere = new Enchere(42, new Livre("titre", "auteur", new Categorie(Categorie.ROMAN), "format",
                "editeur", 0f, 0f, "24/12/1981", "3A61EF7", 42, "propriétaire", 0.1f), 4f, 3, 3f, 3, "34");
        encheres.ajouter(enchere);
        assertThat(encheres.toString(4))
                .isEqualTo(
                        "     tour = 42, type = 3, temps = 3, pas = 3.0, enchérisseur = 34, prix = 4.0, prix maximum = 0.0, livre = \n"
                                + "      Auteur = auteur, Catégorie = Romans Policiers, Date Parution = 24/12/1981, Editeur = editeur, Format = format, Id = 42, Isbn = 3A61EF7, Titre = titre, Proprietaire = propriétaire, PrixAchat = 0.1");
    }

    @Test
    public void should_get_any_enchere() {
        final Encheres encheres = new Encheres();
        final Enchere enchere = new Enchere(42, new Livre("titre", "auteur", new Categorie(Categorie.ROMAN), "format",
                "editeur", 0f, 0f, "24/12/1981", "3A61EF7", 42, "propriétaire", 0.1f), 4f, 3, 3f, 3, "34");
        encheres.ajouter(enchere);

        final Enchere firstEnchere = encheres.get(0);

        assertThat(firstEnchere.getAgent()).isEqualTo(enchere.getAgent());
        assertThat(firstEnchere.getLivre()).isSameAs(enchere.getLivre());
        assertThat(firstEnchere.getNumeroEnchere()).isEqualTo(enchere.getNumeroEnchere());
        assertThat(firstEnchere.getPas()).isEqualTo(enchere.getPas());
        assertThat(firstEnchere.getPrixMaximum()).isEqualTo(enchere.getPrixMaximum());
        assertThat(firstEnchere.getTemps()).isEqualTo(enchere.getTemps());
        assertThat(firstEnchere.getType()).isEqualTo(enchere.getType());
        assertThat(firstEnchere.getValeurEnchere()).isEqualTo(enchere.getValeurEnchere());
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void should_not_get_non_existent_enchere() {
        new Encheres().get(0);

        fail("should throws an IndexOutOfBoundsException");
    }
    
    @Test
    public void when_encheres_is_empty_should_call_nettoyer() {
        try {
            new Encheres().nettoyer();
        } catch (IllegalStateException e) {
            fail(e.getMessage());
        }
    }
    
    @Test(expected = IllegalStateException.class)
    public void should_not_call_method_nettoyer() {
        final Encheres encheres = new Encheres();
        final Enchere enchere = new Enchere(42, new Livre("titre", "auteur", new Categorie(Categorie.ROMAN), "format",
                "editeur", 0f, 0f, "24/12/1981", "3A61EF7", 42, "propriétaire", 0.1f), 4f, 3, 3f, 3, "34");
        encheres.ajouter(enchere);

        assertThat(encheres.get(0)).isNotNull();
        
        encheres.nettoyer();
        
        fail("should throws an IllegalStateException");
    }
}
