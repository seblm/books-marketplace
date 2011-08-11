package bourse.agent.sdd;

import static org.fest.assertions.Assertions.assertThat;

import org.junit.Test;

public class ActionTest {

    @Test
    public final void testNominal() {
        Action action = new Action(0);
        assertThat(action.getAction()).isEqualTo(0);
        assertThat(action.toString(0)).isEqualTo("0 (aucune action séléctionnée)");
        action.setAction(1);
        assertThat(action.toString(1)).isEqualTo(" 1 (vendre son bouquin)");
        action.setAction(6);
        assertThat(action.toString(1)).isEqualTo(" 6 (attente d'une proposition enchère)");
        action.setAction(8);
        assertThat(action.getAction()).isEqualTo(8);
        assertThat(action.toString(2)).isEqualTo("  8 ()");
    }

}
