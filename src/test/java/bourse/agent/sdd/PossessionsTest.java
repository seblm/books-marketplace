package bourse.agent.sdd;

import static org.fest.assertions.Assertions.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.anyVararg;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.whenNew;

import javax.swing.JScrollPane;
import javax.swing.JTable;

import org.junit.Before;
import org.junit.Ignore;
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
        book = new Livre("title", "author", new Categorie(Categorie.AUCUNE), "format", "editor", 1f, 1f,
                "1981-12-24", "ISBN-NUM", 42, "owner", 2f);
        ownership = new Possession(book);
    }
    
    @Test
    public void ownership_should_be_retrieved_by_book_number() throws Exception {
        ownerships.ajouter(ownership);

        final Possession ownershipFromOwnerships = ownerships.get(42);

        assertThat(ownershipFromOwnerships).isNotNull().isSameAs(ownership);
    }

    @Test
    public final void testGetLivre() throws Exception {
        ownerships.ajouter(ownership);
        
        final Possession ownershipFromOwnerships = ownerships.get(book);
        
        assertThat(ownershipFromOwnerships).isNotNull().isSameAs(ownership);
    }

    @Test
    public final void testAjouterLivre() {
        ownerships.ajouter(book);
        
        assertThat(ownerships.get(42).getLivre()).isSameAs(book);
    }

    @Test
    @Ignore
    public final void testAjouterPossession() {
        fail("Not yet implemented");
    }

    @Test
    @Ignore
    public final void testSupprimerLivre() {
        fail("Not yet implemented");
    }

    @Test
    @Ignore
    public final void testSupprimerInt() {
        fail("Not yet implemented");
    }

    @Test
    @Ignore
    public final void testPossede() {
        fail("Not yet implemented");
    }

    @Test
    @Ignore
    public final void testGetValues() {
        fail("Not yet implemented");
    }

    @Test
    @Ignore
    public final void testToStringInt() {
        fail("Not yet implemented");
    }

}
