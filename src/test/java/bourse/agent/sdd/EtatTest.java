package bourse.agent.sdd;

import static org.fest.assertions.Assertions.assertThat;

import org.junit.Test;

public class EtatTest {

    @Test
    public void with_all_states_should_accept_synchronous_and_is_waiting() {
        assertState(Etat.actionChoisie, true, true);
        assertState(Etat.enchereDeuxOuCinq, true, true);
        assertState(Etat.enchereInteressante, true, true);
        assertState(Etat.enchereTrois, true, true);
        assertState(Etat.enchereUnOuQuatre, true, true);
        assertState(Etat.modeEnchere, true, true);
        assertState(Etat.attenteDeclenchementEnchere, true, false);
        assertState(Etat.attentePropositionEnchere, true, false);
        assertState(Etat.attenteRESULTATdeSaVente, true, false);
        assertState(Etat.attenteRESULTBYE, true, false);
        assertState(Etat.pret, true, false);
        assertState(Etat.attenteRESULTWELCOME, false, false);
        assertState(Etat.connaitPdms, false, false);
        assertState(Etat.nonConnecte, false, false);
        assertState(Etat.pdmChoisie, false, false);
        assertState(Etat.quitter, false, false);
        assertState(Etat.connectePhysiquement, false, false);
        assertState(Etat.initial, false, false);
    }

    private static void assertState(int expectedState, boolean expectedAcceptSynchronous, boolean expectedIsWaiting) {
        Etat actual = new Etat(expectedState);
        assertThat(actual.getEtat()).isEqualTo(expectedState);
        assertThat(actual.acceptAsynchronus()).isEqualTo(expectedAcceptSynchronous);
        assertThat(actual.isWaiting()).isEqualTo(expectedIsWaiting);
    }

    @Test
    public void with_etat_2_should_print_status() {
        Etat etat = new Etat(Etat.connaitPdms);
        assertThat(etat.toString(3)).isEqualTo("   2 (connait les pdms actives)");
    }
}
