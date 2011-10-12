package bourse.placeDeMarche.enchere;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.whenNew;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import bourse.protocole.Categorie;
import bourse.sdd.Livre;

@RunWith(PowerMockRunner.class)
@PrepareForTest(Enchere.class)
public class EnchereTest {

    private Livre book;

    @Before
    public final void initMocksAndBook() throws Exception {
        book = new Livre("title", "author", new Categorie(Categorie.BD), "format", "editor", 34.6f, 0.9f, "2000-01-01",
                "349E99490", 42, "owner", 36f);
        final Requetes requests = mock(Requetes.class);
        whenNew(Requetes.class).withArguments(anyBoolean()).thenReturn(requests);
        when(requests.getLivre(42)).thenReturn(book);
    }

    @Test
    public final void should_transcode_bid_to_understandable_string() {
        final Enchere bid = Enchere.newInstance(1, 10, 42, Enchere.ENCHERE_UN);

        assertThat(bid.typeEnchereToString(Enchere.ENCHERE_UN)).isEqualTo("à prendre ou à laisser");
        assertThat(bid.typeEnchereToString(Enchere.ENCHERE_DEUX)).isEqualTo("à prix scellé");
        assertThat(bid.typeEnchereToString(Enchere.ENCHERE_TROIS)).isEqualTo("ascendante");
        assertThat(bid.typeEnchereToString(Enchere.ENCHERE_QUATRE)).isEqualTo("descendante");
        assertThat(bid.typeEnchereToString(Enchere.ENCHERE_CINQ)).isEqualTo("de Vickrey");
        assertThat(bid.typeEnchereToString(Enchere.ENCHERE_INCONNUE)).isEqualTo("inconnue");
        assertThat(bid.typeEnchereToString(42)).isEqualTo("inconnue");
    }

    @Test
    public final void should_tell_if_salesman_identified_by_his_name_is_present_or_not() {
        final Enchere bid = Enchere.newInstance(1, 10, 42, Enchere.ENCHERE_DEUX);

        assertThat(bid.vendeurPresent("unknown owner")).isEqualTo(1);
        assertThat(bid.vendeurPresent("owner")).isEqualTo(0);
    }

    @Test
    public final void should_create_new_bid_with_a_book() throws Exception {
        final Enchere bid = Enchere.newInstance(1, book);

        assertThat(bid.getLivre()).isSameAs(book);
        assertThat(bid.getNumEnchere()).isEqualTo(1);
        assertThat(bid.getPrixCourant()).isEqualTo(34.6f * .9f);
        assertThat(bid.getPas()).isEqualTo(34.6f * .9f * .1f);
    }

    @Test
    public final void bid_with_one_response_should_be_one_two_and_five() {
        final Enchere bid1 = Enchere.newInstance(1, 10, 42, Enchere.ENCHERE_UN);
        final Enchere bid2 = Enchere.newInstance(1, 10, 42, Enchere.ENCHERE_DEUX);
        final Enchere bid3 = Enchere.newInstance(1, 10, 42, Enchere.ENCHERE_TROIS);
        final Enchere bid4 = Enchere.newInstance(1, 10, 42, Enchere.ENCHERE_QUATRE);
        final Enchere bid5 = Enchere.newInstance(1, 10, 42, Enchere.ENCHERE_CINQ);

        assertThat(bid1.estEnchereReponseUnique()).isTrue();
        assertThat(bid2.estEnchereReponseUnique()).isTrue();
        assertThat(bid3.estEnchereReponseUnique()).isFalse();
        assertThat(bid4.estEnchereReponseUnique()).isFalse();
        assertThat(bid5.estEnchereReponseUnique()).isTrue();
    }

    @Test
    public final void bid_with_loop_responses_should_be_four() {
        final Enchere bid1 = Enchere.newInstance(1, 10, 42, Enchere.ENCHERE_UN);
        final Enchere bid2 = Enchere.newInstance(1, 10, 42, Enchere.ENCHERE_DEUX);
        final Enchere bid3 = Enchere.newInstance(1, 10, 42, Enchere.ENCHERE_TROIS);
        final Enchere bid4 = Enchere.newInstance(1, 10, 42, Enchere.ENCHERE_QUATRE);
        final Enchere bid5 = Enchere.newInstance(1, 10, 42, Enchere.ENCHERE_CINQ);

        assertThat(bid1.estEnchereReponseBoucle()).isFalse();
        assertThat(bid2.estEnchereReponseBoucle()).isFalse();
        assertThat(bid3.estEnchereReponseBoucle()).isFalse();
        assertThat(bid4.estEnchereReponseBoucle()).isTrue();
        assertThat(bid5.estEnchereReponseBoucle()).isFalse();
    }

    @Test
    public final void bid_with_multiple_responses_should_be_three() {
        final Enchere bid1 = Enchere.newInstance(1, 10, 42, Enchere.ENCHERE_UN);
        final Enchere bid2 = Enchere.newInstance(1, 10, 42, Enchere.ENCHERE_DEUX);
        final Enchere bid3 = Enchere.newInstance(1, 10, 42, Enchere.ENCHERE_TROIS);
        final Enchere bid4 = Enchere.newInstance(1, 10, 42, Enchere.ENCHERE_QUATRE);
        final Enchere bid5 = Enchere.newInstance(1, 10, 42, Enchere.ENCHERE_CINQ);

        assertThat(bid1.estEnchereReponseMultiple()).isFalse();
        assertThat(bid2.estEnchereReponseMultiple()).isFalse();
        assertThat(bid3.estEnchereReponseMultiple()).isTrue();
        assertThat(bid4.estEnchereReponseMultiple()).isFalse();
        assertThat(bid5.estEnchereReponseMultiple()).isFalse();
    }

    @Test
    public final void start_price_should_have_given_value() throws Exception {
        final Enchere bid = Enchere.newInstance(1, book);

        assertThat(bid.getPrixDepart()).isEqualTo(34.6f * 0.9f * 1.1f);
    }

    @Test
    public final void should_decrement_bid_number() throws Exception {
        final Enchere bid = Enchere.newInstance(1, book);

        bid.decrementerNumEnchere();

        assertThat(bid.getNumEnchere()).isEqualTo(0);
    }

    @Test
    public final void should_increment_bid_number() throws Exception {
        final Enchere bid = Enchere.newInstance(1, book);

        bid.incrementerNumEnchere();

        assertThat(bid.getNumEnchere()).isEqualTo(2);
    }

    @Test
    public final void should_be_display_in_html_format() throws Exception {
        final Enchere bid = Enchere.newInstance(1, book);

        final String htmledBid = bid.toHtml();

        assertThat(htmledBid).startsWith("N° 1 : Enchère ").endsWith(
                " sur Livre n°42 : <i>title</i> d'une valeur de <b>31 Euros</b>, "
                        + "d&eacute;tenu par <b>owner</b>, achet&eacute; <b>36.0 Euros</b>.");
    }

}
