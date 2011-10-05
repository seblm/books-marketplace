package bourse.agent.sdd;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.verifyNew;
import static org.powermock.api.mockito.PowerMockito.whenNew;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
public class ListeAgentTest {

    private ListeAgent listeAgent;

    @Mock
    private Agent agent;

    @Before
    public void create_liste_agent() {
        listeAgent = new ListeAgent();
    }

    @Test
    public void should_create_liste_agent() {
        assertThat(listeAgent.toString(4)).isEqualTo("");
    }

    @Test
    public void should_add_and_retrieve_agent() {
        listeAgent.ajouter(agent);

        assertThat(listeAgent.valeurs()).containsOnly(agent);
    }

    @Test
    public void should_expose_as_simple_string() {
        Agent agent = new Agent("junit agent");

        listeAgent.ajouter(agent);

        assertThat(listeAgent.toString(3)).isEqualTo("   nom = junit agent");
    }

    @Test
    public void should_find_added_agent() {
        when(agent.getNom()).thenReturn("junit agent");
        listeAgent.ajouter(agent);

        assertThat(listeAgent.contient("junit agent")).isTrue();
    }

    @Test
    @PrepareForTest({ ListeAgent.class, Agent.class })
    public void should_execute_main_program() throws Exception {
        ListeAgent listeAgentMocked = mock(ListeAgent.class);
        whenNew(ListeAgent.class).withNoArguments().thenReturn(listeAgentMocked);
        Agent agentMocked = mock(Agent.class);
        whenNew(Agent.class).withArguments(anyString()).thenReturn(agentMocked);

        ListeAgent.main(null);

        verifyNew(ListeAgent.class).withNoArguments();
        verifyNew(Agent.class).withArguments("a1");
        verifyNew(Agent.class).withArguments("a2");
        verifyNew(Agent.class).withArguments("a3");
        verify(listeAgentMocked).toString(5);
    }

}
