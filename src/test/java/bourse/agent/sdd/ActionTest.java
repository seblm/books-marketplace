package bourse.agent.sdd;

import static bourse.agent.sdd.Action.attenteEnchere;
import static bourse.agent.sdd.Action.aucune;
import static bourse.agent.sdd.Action.vendre;
import static org.fest.assertions.Assertions.assertThat;

import org.junit.Test;

public class ActionTest {

    @Test
    public final void testNominal() {
        Action action = aucune;
        assertThat(action).isEqualTo(aucune);
        assertThat(action.toString()).isEqualTo("aucune action séléctionnée");
        action = vendre;
        assertThat(action.toString()).isEqualTo("vendre son bouquin");
        action = attenteEnchere;
        assertThat(action.toString()).isEqualTo("attente d'une proposition enchère");
    }

}
