package bourse.agent;

import static org.fest.assertions.Assertions.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.whenNew;

import java.sql.ResultSet;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import bourse.agent.ia.Aleatoire;
import bourse.agent.sdd.Action;
import bourse.agent.sdd.Etat;
import bourse.agent.sdd.ListePdm;
import bourse.agent.sdd.ListePdmMemoire;
import bourse.agent.sdd.Pdm;
import bourse.protocole.Categorie;

@RunWith(PowerMockRunner.class)
public class AgentTest {

    private Visualisation window;
    private RequetesAgent requetesAgent;

    @Before
    public void initMocks() throws Exception {
        window = mock(Visualisation.class);
        whenNew(Visualisation.class).withNoArguments().thenReturn(window);
        requetesAgent = mock(RequetesAgent.class);
        whenNew(RequetesAgent.class).withArguments(Boolean.TRUE).thenReturn(requetesAgent);
        whenNew(RequetesAgent.class).withArguments(Boolean.FALSE).thenReturn(requetesAgent);
    }

    @Test
    @PrepareForTest(Agent.class)
    public final void creates_new_agent_should_set_an_initial_state() throws Exception {
        Agent agent = new Agent("test");

        assertThat(agent.getEtat()).isEqualTo(Etat.initial);
        try {
            assertThat(agent.getEtatSuivant()).isNull();
            fail("getEtatSuivant() must throws a NullPointerException");
        } catch (final NullPointerException e) {
        }
        assertThat(agent.getCurrentPdm()).isNull();
        assertThat(agent.getWallet()).isEqualTo(0);
        assertThat(agent.getCategorie().getCode()).isEqualTo(new Categorie(Categorie.AUCUNE).getCode());
        assertThat(agent.getAction()).isEqualTo(Action.aucune);
        assertThat(agent.getEnvironnement()).isNotNull();
        assertThat(agent.getMemoire()).isNotNull();
        assertThat(agent.getNom()).isEqualTo("Groupe-E-test");
        assertThat(agent.getFenetre()).isSameAs(window);
        assertThat(agent.getDecision()).isNull();
    }

    @Test
    @PrepareForTest(Agent.class)
    public void agent_should_display_comprehensive_toString() throws Exception {
        Agent agent = new Agent("test");

        agent.setEtatSuivant(Etat.actionChoisie);
        agent.setHote("localhost");
        agent.setVerbose(false);
        agent.setDecision(new Aleatoire(agent));

        final String[] agentString = agent.toString(1).split("\n");
        assertThat(agentString[0]).isEqualTo(" Nom = Groupe-E-test");
        assertThat(agentString[1]).isEqualTo(" Solde = 0.0");
        assertThat(agentString[2]).startsWith(" Decision = bourse.agent.ia.Aleatoire@");
        assertThat(agentString[3]).isEqualTo(" Catégorie = 5 (Aucune)");
        assertThat(agentString[4]).isEqualTo(" Etat = 1 (initialisé)");
        assertThat(agentString[5]).isEqualTo(" Action = 0 (aucune action séléctionnée)");
        assertThat(agentString[6]).isEqualTo(" Memoire =");
        assertThat(agentString[7]).isEqualTo("  pdms :");
        assertThat(agentString[8]).isEqualTo("");
        assertThat(agentString[9]).isEqualTo("  agents :");
        assertThat(agentString[10]).isEqualTo("");
        assertThat(agentString[11]).isEqualTo("  possessions :");
        assertThat(agentString[12]).isEqualTo("");
        assertThat(agentString[13]).isEqualTo("  temps :");
        assertThat(agentString[14]).isEqualTo("0");
        assertThat(agentString[15]).isEqualTo(" Environnement =");
        assertThat(agentString[16]).isEqualTo("  enchère =");
        assertThat(agentString[17])
                .isEqualTo(
                        "   tour = 1, type = 0, temps = 0, pas = 0.0, enchérisseur = null, prix = 0.0, prix maximum = 0.0, livre = ");
        assertThat(agentString[18]).startsWith("    , Catégorie = ");
        assertThat(agentString[19])
                .isEqualTo(
                        "  typeEnchèreDemandée = 0   nombreActions = 0   dateListeAgent = 9223372036854775807   dateListeProgramme = 9223372036854775807   enchèreInteressante = false");
        assertThat(agentString[20]).isEqualTo(" Hote = localhost");
    }

    @Test
    @PrepareForTest({ Agent.class, Memoire.class })
    public final void should_get_pdms_from_bd() throws Exception {
        final ResultSet pdmsFromBd = mock(ResultSet.class);
        when(pdmsFromBd.next()).thenReturn(true, true, false);
        when(pdmsFromBd.getString("nom")).thenReturn("pdm1", "pdm2");
        when(pdmsFromBd.getString("adresse")).thenReturn("192.168.10.1:80", "localhost:8080");
        when(requetesAgent.getPdMs()).thenReturn(pdmsFromBd);
        final ListePdmMemoire pdmListMock = mock(ListePdmMemoire.class);
        whenNew(ListePdmMemoire.class).withArguments(window).thenReturn(pdmListMock);
        final Agent agent = new Agent("nom");

        agent.getPdmsFromBd();

        ArgumentCaptor<ListePdm> pdmListArgument = ArgumentCaptor.forClass(ListePdm.class);
        verify(pdmListMock).miseAJour(pdmListArgument.capture());
        final ListePdm pdmList = pdmListArgument.getValue();
        assertThat(pdmList.estVide()).isFalse();
        final Pdm pdm = pdmList.acceder("pdm1");
        assertThat(pdm).isNotNull();
        assertThat(pdm.getAdresse().ipToString()).isEqualTo("192.168.10.1");
        assertThat(pdmList.acceder("pdm2")).isNotNull();
        assertThat(agent.getEtat()).isEqualTo(Etat.connaitPdms);
    }

    @Test
    @PrepareForTest(Agent.class)
    public void should_show_results() throws Exception {
        final ResultSet resultPerBook = mock(ResultSet.class);
        when(resultPerBook.next()).thenReturn(true, true, false);
        when(resultPerBook.getString("argent")).thenReturn("34.9");
        when(resultPerBook.getString("titre")).thenReturn("titre1", "titre2");
        when(resultPerBook.getString("id")).thenReturn("id1", "id2");
        when(resultPerBook.getFloat("points")).thenReturn(8.4f, 8.4f, 4f, 4f);
        when(resultPerBook.getString("categorie")).thenReturn("A", "B");
        when(requetesAgent.getResultPerBook("Groupe-E-nom")).thenReturn(resultPerBook);

        Agent agent = new Agent("nom");
        agent.setEtat(Etat.pret);

        agent.showResults();

        final StringBuilder output = new StringBuilder();
        output.append("Argent = 34.9\n");
        output.append("Titre = titre1 Id = id1 Points = 8.4 Catégorie = A\n");
        output.append("Titre = titre2 Id = id2 Points = 4.0 Catégorie = B\n");
        output.append("Total des points = 12.4");

        verify(window).setResultat(output.toString());
    }

}
