package bourse.protocole;

import static bourse.protocole.Categorie.AUCUNE;
import static bourse.protocole.TypeMessage.TM_PROPOSITION_ENCHERE_P;
import static bourse.sdd.LivreTest.assertBook;
import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Mockito.mock;

import org.junit.Test;

import bourse.sdd.Livre;

public class PropositionEncherePTest extends SAXTest {

    private static final String LIVRE_XML = "<LIVRE AUTEUR=\"author\" CATEGORIE=\"Aucune\" "
            + "DATEPAR=\"publicationDate\" EDITEUR=\"editor\" ETAT=\"1.0\" FORMAT=\"format\" "
            + "ID=\"22\" ISBN=\"isbn\" PRIX=\"12.3\" PROPRIETAIRE=\"owner\" TITRE=\"title\"/>\n";

    @Test
    public void should_create_instance_by_name_number_time_and_book() {
        Livre book = mock(Livre.class);
        PropositionEnchereP enchere = new PropositionEnchereP("name", 42, 24, book);

        assertEnchere(enchere, "", "name", 42, 0f, 24, 0f);
    }

    @Test
    public void should_create_instance_by_name_number_time_step_and_book() {
        Livre book = mock(Livre.class);
        PropositionEnchereP enchere = new PropositionEnchereP("name", 42, 24, 2f, book);

        assertEnchere(enchere, "", "name", 42, 2f, 24, 0f);
    }

    @Test
    public void should_create_instance_by_name_number_time_book_and_bid() {
        Livre book = mock(Livre.class);
        PropositionEnchereP enchere = new PropositionEnchereP("name", 42, 24, book, 24f);

        assertEnchere(enchere, "", "name", 42, 0f, 24, 24f);
    }

    @Test
    public void should_create_instance_by_name_number_time_step_book_bid_and_agent() {
        Livre book = mock(Livre.class);
        PropositionEnchereP enchere = new PropositionEnchereP("name", 42, 24, 2f, book, 24f, "007");

        assertEnchere(enchere, "007", "name", 42, 2f, 24, 24f);
    }

    private void assertEnchere(PropositionEnchereP actual, String expectedAgent, String expectedName,
            int expectedBetNumber, float expectedStep, int expectedTime, float expectedBid) {
        assertThat(actual.getAgent()).isEqualTo(expectedAgent);
        assertThat(actual.getNom()).isEqualTo(expectedName);
        assertThat(actual.getNumeroEnchere()).isEqualTo(expectedBetNumber);
        assertThat(actual.getPas()).isEqualTo(expectedStep);
        assertThat(actual.getTemps()).isEqualTo(expectedTime);
        assertThat(actual.getType().getValue()).isEqualTo(TM_PROPOSITION_ENCHERE_P);
        assertThat(actual.getValeurEnchere()).isEqualTo(expectedBid);
    }

    @Test
    public void should_create_instance_by_name_number_time_and_book_with_xml() {
        StringBuilder xmlBuilder = new StringBuilder();
        xmlBuilder.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        xmlBuilder.append("<!DOCTYPE MSG SYSTEM \"src/main/resources/MSG.dtd\">\n");
        xmlBuilder.append("<MSG>\n");
        xmlBuilder.append("    <PROPOSITIONENCHERE NOM=\"name\" NUMERO=\"42\" TEMPS=\"24\">\n");
        xmlBuilder.append("        " + LIVRE_XML);
        xmlBuilder.append("    </PROPOSITIONENCHERE>\n");
        xmlBuilder.append("</MSG>\n");
        Protocole newInstance = PropositionEnchereP.newInstance(xmlBuilder.toString());

        assertThat(newInstance).isInstanceOf(PropositionEnchereP.class);
        PropositionEnchereP enchere = (PropositionEnchereP) newInstance;
        assertEnchere(enchere, "", "name", 42, 0f, 24, 0f);
        assertBook(enchere.getLivre(), "author", AUCUNE, "publicationDate", "editor", 1f, "format", 22, "isbn", 12.3f,
                0f, "owner", "title");
        assertThat(enchere.toXML()).isEqualTo(xmlBuilder.toString().replace("src/main/resources/MSG.dtd", "MSG.dtd"));
    }

    @Test
    public void should_create_instance_by_name_number_time_step_book_and_bid_with_xml() {
        StringBuilder xmlBuilder = new StringBuilder();
        xmlBuilder.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        xmlBuilder.append("<!DOCTYPE MSG SYSTEM \"src/main/resources/MSG.dtd\">\n");
        xmlBuilder.append("<MSG>\n");
        xmlBuilder.append("    <PROPOSITIONENCHERE NOM=\"ENCHERETROIS\" NUMERO=\"42\" PAS=\"2.0\" TEMPS=\"24\">\n");
        xmlBuilder.append("        " + LIVRE_XML);
        xmlBuilder.append("        <ENCHERE>24.0</ENCHERE>\n");
        xmlBuilder.append("    </PROPOSITIONENCHERE>\n");
        xmlBuilder.append("</MSG>\n");
        Protocole newInstance = PropositionEnchereP.newInstance(xmlBuilder.toString());

        assertThat(newInstance).isInstanceOf(PropositionEnchereP.class);
        PropositionEnchereP enchere = (PropositionEnchereP) newInstance;
        assertEnchere(enchere, "", "ENCHERETROIS", 42, 2f, 24, 24f);
        assertBook(enchere.getLivre(), "author", AUCUNE, "publicationDate", "editor", 1f, "format", 22, "isbn", 12.3f,
                0f, "owner", "title");
        assertThat(enchere.toXML()).isEqualTo(xmlBuilder.toString().replace("src/main/resources/MSG.dtd", "MSG.dtd"));
    }

    @Test
    public void should_create_instance_by_name_number_time_step_book_bid_and_agent_with_xml() {
        StringBuilder xmlBuilder = new StringBuilder();
        xmlBuilder.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        xmlBuilder.append("<!DOCTYPE MSG SYSTEM \"src/main/resources/MSG.dtd\">\n");
        xmlBuilder.append("<MSG>\n");
        xmlBuilder.append("    <PROPOSITIONENCHERE NOM=\"ENCHERETROIS\" NUMERO=\"42\" PAS=\"2.0\" TEMPS=\"24\">\n");
        xmlBuilder.append("        " + LIVRE_XML);
        xmlBuilder.append("        <ENCHERE>24.0</ENCHERE>\n");
        xmlBuilder.append("        <AGENT NOM=\"007\"/>\n");
        xmlBuilder.append("    </PROPOSITIONENCHERE>\n");
        xmlBuilder.append("</MSG>\n");
        Protocole newInstance = PropositionEnchereP.newInstance(xmlBuilder.toString());

        assertThat(newInstance).isInstanceOf(PropositionEnchereP.class);
        PropositionEnchereP enchere = (PropositionEnchereP) newInstance;
        assertEnchere(enchere, "007", "ENCHERETROIS", 42, 2f, 24, 24f);
        assertBook(enchere.getLivre(), "author", AUCUNE, "publicationDate", "editor", 1f, "format", 22, "isbn", 12.3f,
                0f, "owner", "title");
        assertThat(enchere.toXML()).isEqualTo(xmlBuilder.toString().replace("src/main/resources/MSG.dtd", "MSG.dtd"));
    }

}
