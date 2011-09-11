package bourse.agent.sdd;

import static bourse.agent.sdd.Action.adversaires;
import static bourse.agent.sdd.Action.attenteEnchere;
import static bourse.agent.sdd.Action.aucune;
import static bourse.agent.sdd.Action.bilan;
import static bourse.agent.sdd.Action.migrer;
import static bourse.agent.sdd.Action.objectif;
import static bourse.agent.sdd.Action.programme;
import static bourse.agent.sdd.Action.vendre;
import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.verifyNew;
import static org.powermock.api.mockito.PowerMockito.whenNew;

import java.util.Random;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
public class ActionTest {

    @Test
    @PrepareForTest(Action.class)
    public void should_create_a_random_action() throws Exception {
        Random random = mock(Random.class);
        when(random.nextInt(6)).thenReturn(42);
        whenNew(Random.class).withNoArguments().thenReturn(random);

        Action action = new Action();

        assertThat(action.getAction()).isEqualTo(42);
    }

    @Test
    public void should_print_action_name() {
        assertThat(new Action(aucune).toString(8)).isEqualTo("        0 (aucune action séléctionnée)");
        assertThat(new Action(vendre).toString(8)).isEqualTo("        1 (vendre son bouquin)");
        assertThat(new Action(migrer).toString(8)).isEqualTo("        2 (migration)");
        assertThat(new Action(bilan).toString(9)).isEqualTo("         3 (effectuer son bilan)");
        assertThat(new Action(programme).toString(5)).isEqualTo("     4 (demande de programme)");
        assertThat(new Action(adversaires).toString(3)).isEqualTo("   5 (demande de la liste des agents présents)");
        assertThat(new Action(attenteEnchere).toString(0)).isEqualTo("6 (attente d'une proposition enchère)");
        assertThat(new Action(objectif).toString(6)).isEqualTo("      7 ()");
    }

    @Test
    public void should_set_action() {
        Action action = new Action();

        action.setAction(42);

        assertThat(action.getAction()).isEqualTo(42);
    }

    @Test
    @PrepareForTest(Action.class)
    public void main_should_create_and_print_a_state() throws Exception {
        Action action = mock(Action.class);
        whenNew(Action.class).withNoArguments().thenReturn(action);
        whenNew(Action.class).withArguments(anyInt()).thenReturn(action);

        Action.main(null);

        verifyNew(Action.class).withArguments(8);
        verifyNew(Action.class, times(10)).withNoArguments();
        verify(action, times(10)).toString(0);
    }
}
