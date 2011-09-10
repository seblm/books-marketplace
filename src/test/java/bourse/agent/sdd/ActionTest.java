package bourse.agent.sdd;

import static bourse.agent.sdd.Action.attenteEnchere;
import static bourse.agent.sdd.Action.aucune;
import static bourse.agent.sdd.Action.vendre;
import static org.fest.assertions.Assertions.assertThat;

import org.junit.Test;

public class ActionTest {

    @Test
    public final void testNominal() {
        Action action = new Action(aucune);
        assertThat(action.getAction()).isEqualTo(aucune);
        assertThat(action.toString(0)).isEqualTo("0 (aucune action séléctionnée)");
        action = new Action(vendre);
        assertThat(action.toString(0)).isEqualTo("1 (vendre son bouquin)");
        action = new Action(attenteEnchere);
        assertThat(action.toString(0)).isEqualTo("6 (attente d'une proposition enchère)");
    }

}
