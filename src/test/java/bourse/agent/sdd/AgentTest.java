package bourse.agent.sdd;

import static bourse.agent.sdd.Agent.main;
import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.whenNew;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
public class AgentTest {

    @Test
    public void an_agent_has_a_name() throws Exception {
        Agent agent = new Agent("junit");
        assertThat(agent.getNom()).isEqualTo("junit");
    }

    @Test
    public void the_agent_name_can_be_printed() {
        Agent agent = new Agent("Arnaud");
        assertThat(agent.toString(5)).isEqualTo("     nom = Arnaud");

        Agent eric = new Agent("Eric");
        assertThat(eric.toString(3)).isEqualTo("   nom = Eric");
    }

    @Test
    @PrepareForTest(Agent.class)
    public void the_main_program_should_create_agent_and_print() throws Exception {
        Agent agent = mock(Agent.class);
        whenNew(Agent.class).withArguments(anyString()).thenReturn(agent);

        main(null);

        verify(agent, times(2)).toString(5);
    }

}
