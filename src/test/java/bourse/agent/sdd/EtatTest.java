package bourse.agent.sdd;

import static bourse.agent.sdd.Etat.actionChoisie;
import static bourse.agent.sdd.Etat.attenteDeclenchementEnchere;
import static bourse.agent.sdd.Etat.attentePropositionEnchere;
import static bourse.agent.sdd.Etat.attenteRESULTATdeSaVente;
import static bourse.agent.sdd.Etat.attenteRESULTBYE;
import static bourse.agent.sdd.Etat.attenteRESULTWELCOME;
import static bourse.agent.sdd.Etat.connaitPdms;
import static bourse.agent.sdd.Etat.connectePhysiquement;
import static bourse.agent.sdd.Etat.enchereDeuxOuCinq;
import static bourse.agent.sdd.Etat.enchereInteressante;
import static bourse.agent.sdd.Etat.enchereTrois;
import static bourse.agent.sdd.Etat.enchereUnOuQuatre;
import static bourse.agent.sdd.Etat.initial;
import static bourse.agent.sdd.Etat.modeEnchere;
import static bourse.agent.sdd.Etat.nonConnecte;
import static bourse.agent.sdd.Etat.pdmChoisie;
import static bourse.agent.sdd.Etat.pret;
import static bourse.agent.sdd.Etat.quitter;
import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.whenNew;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
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
    public void with_all_etat_should_print_status() {
        assertThat(new Etat(quitter).toString(6)).isEqualTo("      0 (quitter)");
        assertThat(new Etat(initial).toString(6)).isEqualTo("      1 (initialisé)");
        assertThat(new Etat(connaitPdms).toString(2)).isEqualTo("  2 (connait les pdms actives)");
        assertThat(new Etat(pdmChoisie).toString(3)).isEqualTo("   3 (pdm choisie dans la liste des actives)");
        assertThat(new Etat(connectePhysiquement).toString(0)).isEqualTo("4 (connexion physique effectuée)");
        assertThat(new Etat(attenteRESULTWELCOME).toString(0)).isEqualTo("5 (attente d'une réponse au WELCOME)");
        assertThat(new Etat(pret).toString(9)).isEqualTo("         6 (pret pour travailler)");
        assertThat(new Etat(attenteRESULTBYE).toString(4)).isEqualTo("    7 (attente d'une réponse au BYE)");
        assertThat(new Etat(nonConnecte).toString(2)).isEqualTo("  8 (non connecté physiquement)");
        assertThat(new Etat(actionChoisie).toString(0)).isEqualTo("9 (action choisie)");
        assertThat(new Etat(attentePropositionEnchere).toString(0)).isEqualTo(
                "10 (attente de réponse à la demande de vente)");
        assertThat(new Etat(attenteDeclenchementEnchere).toString(0)).isEqualTo(
                "11 (attente du déclenchement de la vente)");
        assertThat(new Etat(modeEnchere).toString(1)).isEqualTo(" 12 (debut du mode enchère)");
        assertThat(new Etat(attenteRESULTATdeSaVente).toString(0)).isEqualTo("13 (attente du résultat de sa vente)");
        assertThat(new Etat(enchereInteressante).toString(0)).isEqualTo("14 (enchère interessante)");
        assertThat(new Etat(enchereUnOuQuatre).toString(0)).isEqualTo(
                "15 (enchère à prendre ou à laisser ou enchère descendante)");
        assertThat(new Etat(enchereDeuxOuCinq).toString(0))
                .isEqualTo("16 (enchère à pli scellé ou enchère de Vickrey)");
        assertThat(new Etat(enchereTrois).toString(1)).isEqualTo(" 17 (enchère ascendante)");
        assertThat(new Etat(42).toString(10)).isEqualTo("          42 (enchère non gérée)");
    }

    @Test
    @PrepareForTest(Etat.class)
    public void main_should_create_and_print_a_state() throws Exception {
        Etat etat = mock(Etat.class);
        whenNew(Etat.class).withArguments(2).thenReturn(etat);

        Etat.main(null);

        verify(etat).toString(0);
    }

    @Test
    public void the_state_can_be_set() throws Exception {
        Etat etat = new Etat(42);

        etat.setEtat(24);

        assertThat(etat.getEtat()).isEqualTo(24);
    }
}
