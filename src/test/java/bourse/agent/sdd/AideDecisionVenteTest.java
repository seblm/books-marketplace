package bourse.agent.sdd;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import bourse.agent.Agent;
import bourse.agent.Memoire;
import bourse.agent.ia.Ia;

@RunWith(MockitoJUnitRunner.class)
public class AideDecisionVenteTest {

    @Mock
    private Agent agent;

    @Mock
    private Ia decision;

    @Mock
    private Memoire memoire;

    private AideDecisionVente aideDecisionVente;

    @Before
    public void createAideDecisionVente() {
        aideDecisionVente = new AideDecisionVente(agent);
    }

    @Test
    public void should_initialize_aide_decision_vente() {
        assertThat(aideDecisionVente.getSommeQVente()).isEqualTo(0);
        assertThat(aideDecisionVente.getQVenteActuel()).isEqualTo(0);
        assertThat(aideDecisionVente.getNbQVenteCalcules()).isEqualTo(0);
    }

    @Test
    public void should_update_q_vente() {
        when(agent.getDecision()).thenReturn(decision);
        when(decision.qVente()).thenReturn(42f);
        when(agent.getMemoire()).thenReturn(memoire);
        when(memoire.getTemps()).thenReturn(25);

        aideDecisionVente.miseAJourQVente();

        float expectedQVente = 42f * 25f;
        assertThat(aideDecisionVente.getSommeQVente())
                .isEqualTo(expectedQVente);
        assertThat(aideDecisionVente.getQVenteActuel()).isEqualTo(
                expectedQVente);
        assertThat(aideDecisionVente.getNbQVenteCalcules()).isEqualTo(1);
    }

}
