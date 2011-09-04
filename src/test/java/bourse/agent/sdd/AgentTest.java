package bourse.agent.sdd;

import static org.fest.assertions.Assertions.assertThat;

import org.junit.Test;

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

}
