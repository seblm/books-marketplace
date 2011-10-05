package bourse.agent.sdd;

import static org.fest.assertions.Assertions.assertThat;
import static org.fest.assertions.MapAssert.entry;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.anyVararg;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.whenNew;

import javax.swing.JScrollPane;
import javax.swing.JTable;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import bourse.agent.Agent;
import bourse.agent.Memoire;
import bourse.agent.RequetesAgent;
import bourse.agent.Visualisation;
import bourse.protocole.Categorie;
import bourse.sdd.Livre;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ Agent.class, Possessions.class, ListeAgentMemoire.class })
public class PossessionsTest {

    private Possessions ownerships;
    private Livre book;
    private Possession ownership;
    private Visualisation window;
    private RequetesAgent agentRequest;
    private JTable table;

    @Before
    public void initOwnerships() throws Exception {
        window = mock(Visualisation.class);
        JScrollPane jScrollPane = mock(JScrollPane.class);
        when(window.getJScrollPanePossessionMemoire()).thenReturn(jScrollPane);
        when(window.getJScrollPaneAgentMemoire()).thenReturn(jScrollPane);
        whenNew(Visualisation.class).withNoArguments().thenReturn(window);
        agentRequest = mock(RequetesAgent.class);
        whenNew(RequetesAgent.class).withArguments(Boolean.TRUE).thenReturn(agentRequest);
        whenNew(RequetesAgent.class).withArguments(Boolean.FALSE).thenReturn(agentRequest);
        table = mock(JTable.class);
        whenNew(JTable.class).withArguments(anyVararg()).thenReturn(table);
        ownerships = new Possessions(window, new Memoire(new Agent("name"), window));
        book = new Livre("title", "author", new Categorie(Categorie.AUCUNE), "format", "editor", 1f, 1f, "1981-12-24",
                "ISBN-NUM", 42, "owner", 2f);
        ownership = new Possession(book);
    }

    @Test
    public void ownership_should_be_retrieved_by_book_number() throws Exception {
        ownerships.ajouter(ownership);

        final Possession ownershipFromOwnerships = ownerships.get(42);

        assertThat(ownershipFromOwnerships).isNotNull().isSameAs(ownership);
    }

    @Test
    public final void ownership_should_be_retrieved_by_book() throws Exception {
        ownerships.ajouter(ownership);

        final Possession ownershipFromOwnerships = ownerships.get(book);

        assertThat(ownershipFromOwnerships).isNotNull().isSameAs(ownership);
    }

    @Test
    public final void book_ownership_should_be_added() {
        ownerships.ajouter(book);

        assertThat(ownerships.get(42).getLivre()).isSameAs(book);
    }

    @Test
    public final void more_recent_ownership_should_replace_older_one_on_same_book() {
        final Possession ownershipAtFirstJanuary1970 = new Possession(book, 0l);
        ownerships.ajouter(ownershipAtFirstJanuary1970);
        ownerships.ajouter(ownership);

        assertThat(ownerships.get(42)).isSameAs(ownership);
    }

    @Test
    public final void remove_an_ownership_by_his_book_should_really_remove_it() {
        ownerships.ajouter(ownership);

        ownerships.supprimer(book);

        assertThat(ownerships.get(book)).isNull();
    }

    @Test
    public final void remove_an_ownership_by_his_book_id_should_really_remove_it() {
        ownerships.ajouter(ownership);

        ownerships.supprimer(42);

        assertThat(ownerships.get(42)).isNull();
    }

    @Test
    public final void books_should_be_retrieved_by_owner_name() {
        ownerships.ajouter(ownership);

        final ListeLivre booksList = ownerships.possede("owner");

        assertThat(booksList.getListe()).isNotEmpty().hasSize(1).includes(entry(42, book));
    }

    @Test
    public final void all_ownerships_should_be_retrieved() {
        ownerships.ajouter(ownership);

        assertThat(ownerships.getValues()).isNotEmpty().hasSize(1).contains(ownership);
    }

    @Test
    public final void ownerships_should_be_displayed_to_human_readable_form() {
        ownerships.ajouter(ownership);

        final String[] humanReadableString = ownerships.toString(1).split("\n");

        assertThat(humanReadableString).hasSize(5);
        assertThat(humanReadableString[0]).isEqualTo(" possession =");
        assertThat(humanReadableString[1]).isEqualTo("  livre = ");
        assertThat(humanReadableString[2])
                .isEqualTo(
                        "   Auteur = authorAucune, Date Parution = 1981-12-24, Editeur = editor, Etat = 1.0, Format = format, Id = 42, Isbn = ISBN-NUM, Prix = 1.0, Titre = title, Proprietaire = owner, PrixAchat = 2.0");
        assertThat(humanReadableString[3]).isEqualTo("  en instance de vente = false");
        assertThat(humanReadableString[4]).startsWith("  date = ");
    }

    @Test
    public final void empty_ownerships_should_be_displayed_with_empty_string() {
        assertThat(ownerships.toString(0)).hasSize(0);
    }

    @Test
    public final void owneships_should_be_executed() throws Exception {
        ownerships = mock(Possessions.class);
        whenNew(Possessions.class).withArguments(any(Visualisation.class), any(Memoire.class)).thenReturn(ownerships);
        when(ownerships.possede(anyString())).thenReturn(new ListeLivre());

        Possessions.main(new String[0]);

        verify(ownerships, times(4)).ajouter(any(Livre.class));
        verify(ownerships, times(1)).ajouter(any(Possession.class));
    }

}