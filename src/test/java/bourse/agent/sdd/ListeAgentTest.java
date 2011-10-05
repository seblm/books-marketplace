package bourse.agent.sdd;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.verifyNew;
import static org.powermock.api.mockito.PowerMockito.whenNew;

import java.util.Collection;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest(ListeAgent.class)
public class ListeAgentTest {

    private ListeAgent agentsList;

    @Before
    public final void initNewAgentsList() {
        agentsList = new ListeAgent();
    }

    @Test
    public final void should_add_new_agent() {
        agentsList.ajouter(new Agent("new agent"));

        assertThat(agentsList.contient("new agent")).isTrue();
    }

    @Test
    public final void should_tell_it_has_agent_if_it_really_has_one() {
        agentsList.ajouter(new Agent("new agent"));

        final boolean hasAgentNamedNewAgent = agentsList.contient("new agent");

        assertThat(hasAgentNamedNewAgent).isTrue();
    }

    @Test
    public final void should_tell_it_has_not_agent() {
        final boolean hasAgentNamedNewAgent = agentsList.contient("new agent");

        assertThat(hasAgentNamedNewAgent).isFalse();
    }

    @Test
    public final void should_be_transformed_to_a_human_readable_string() {
        agentsList.ajouter(new Agent("first agent"));
        agentsList.ajouter(new Agent("second agent"));

        final String stringRepresentation = agentsList.toString(3);

        assertThat(stringRepresentation).isEqualTo("   nom = first agent\n   nom = second agent");
    }

    @Test
    public final void empty_instance_should_be_transformed_to_empty_string() {
        final String stringRepresentation = agentsList.toString(0);

        assertThat(stringRepresentation).hasSize(0);
    }

    @Test
    public final void testValeurs() {
        final Agent firstAgent = new Agent("first agent");
        agentsList.ajouter(firstAgent);
        final Agent secondAgent = new Agent("second agent");
        agentsList.ajouter(secondAgent);

        @SuppressWarnings("rawtypes")
        final Collection values = agentsList.valeurs();

        assertThat(values).containsOnly(firstAgent, secondAgent);
    }

    @Test
    public final void testMain() throws Exception {
        final ListeAgent agentsList = mock(ListeAgent.class);
        whenNew(ListeAgent.class).withNoArguments().thenReturn(agentsList);

        ListeAgent.main(new String[0]);

        verifyNew(ListeAgent.class, times(1)).withNoArguments();
        verify(agentsList, times(3)).ajouter(any(Agent.class));
        verify(agentsList, times(1)).toString(5);
    }

}
