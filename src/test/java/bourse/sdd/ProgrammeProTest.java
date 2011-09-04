package bourse.sdd;

import static org.fest.assertions.Assertions.assertThat;

import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import bourse.placeDeMarche.enchere.Enchere;

import com.google.common.collect.Sets;

public class ProgrammeProTest {

    private Livre book;
    private ProgrammePro programmePro;

    @Before
    public void createProgrammePro() {
        book = TestFactory.createLivre();
        programmePro = new ProgrammePro(2, book, Enchere.ENCHERE_UN, 15.0f);
    }

    @Test
    public void should_create_a_programmePro_when_bid_type_is_not_known() {
        ProgrammePro programmePro = new ProgrammePro(3, book);
        assertThat(programmePro.getLivre()).isEqualTo(book);
        assertThat(programmePro.getNum()).isEqualTo(3);
        assertThat(programmePro.getPrixVente()).isEqualTo(0.0f);
        assertThat(programmePro.getTypeEnchere()).isEqualTo(Enchere.ENCHERE_INCONNUE);
    }

    @Test
    public void should_create_a_programmePro_when_agent_submit_a_bid() {
        assertThat(programmePro.getLivre()).isEqualTo(book);
        assertThat(programmePro.getNum()).isEqualTo(2);
        assertThat(programmePro.getPrixVente()).isEqualTo(15.0f);
        assertThat(programmePro.getTypeEnchere()).isEqualTo(Enchere.ENCHERE_UN);
    }

    @Test
    public void should_increase_bid_to_a_programmePro() {
        assertThat(programmePro.getNum()).isEqualTo(2);
        programmePro.incrementerNumEnchere();
        assertThat(programmePro.getNum()).isEqualTo(3);
    }

    @Test
    public void should_decrease_bid_to_a_programmePro() {
        assertThat(programmePro.getNum()).isEqualTo(2);
        programmePro.decrementerNumEnchere();
        assertThat(programmePro.getNum()).isEqualTo(1);
    }

    @Test
    public void a_programmePro_should_print_gracefully() {
        String programmeProAsAString = programmePro.toString(3);
        assertThat(programmeProAsAString)
                .isEqualTo(
                        "   numéro = 2, livre = [ Auteur = leblanc, Catégorie = Science, Date Parution = 15/11/00, Editeur = belin, Etat = 0.4, Format = poch, Id = 12, Isbn = yetet, Prix = 153.0, Titre = lupin, Proprietaire = protocoleman, PrixAchat = 50.95 ]");
    }

    @Test
    public void with_two_equals_programmePro() {
        ProgrammePro programmePro2 = new ProgrammePro(2, book, Enchere.ENCHERE_UN, 15.0f);
        assertThat(programmePro.equals(programmePro2));
    }

    @Test
    public void with_two_equals_programmePro_should_set_only_one_instance_in_a_set() {
        Set<ProgrammePro> programmePros = Sets.newHashSet();
        assertThat(programmePros.add(programmePro)).isTrue();
        ProgrammePro programmePro2 = new ProgrammePro(2, book, Enchere.ENCHERE_UN, 15.0f);
        assertThat(programmePros.add(programmePro2)).isFalse();
    }

}
