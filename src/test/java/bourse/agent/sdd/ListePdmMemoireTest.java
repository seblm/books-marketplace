package bourse.agent.sdd;

import static org.fest.assertions.Assertions.assertThat;

import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import bourse.agent.Visualisation;

public class ListePdmMemoireTest {

    private ListePdmMemoire listePdmMemoire;

    @Before
    public void initialisation() {
        Visualisation visu = new Visualisation();
        listePdmMemoire = new ListePdmMemoire(visu);
        listePdmMemoire.ajouter(new PdmMemoire("p1", "HOME", false, false, new ListeProgramme(), 1));
        listePdmMemoire.ajouter(new PdmMemoire("p2", "192.168.1.2:80", true, true, new ListeProgramme(), 2));
        listePdmMemoire.ajouter(new PdmMemoire("p3", "0.0.0.0:0", false, true, new ListeProgramme(), 3));
    }

    @Test
    public void testListePdmMemoire() {
        assertThat(listePdmMemoire.acceder("p1").getActive()).isFalse();
        assertThat(listePdmMemoire.acceder("p1").getVisitee()).isFalse();
        assertThat(listePdmMemoire.acceder("p2").getActive()).isTrue();
        assertThat(listePdmMemoire.acceder("p2").getVisitee()).isTrue();
        assertThat(listePdmMemoire.acceder("p3").getActive()).isTrue();
        assertThat(listePdmMemoire.acceder("p3").getVisitee()).isFalse();
        assertThat(listePdmMemoire.aucuneActive()).isFalse();
        assertThat(listePdmMemoire.premiereActiveNonVisitee().getNom()).isEqualTo("p3");
        assertThat(listePdmMemoire.premiereActiveVisitee().getNom()).isEqualTo("p2");

        Pdm pdm1 = new Pdm("p1", "192.168.1.1:8080"); // p1 toujours active
        Pdm pdm2 = new Pdm("p2", "192.168.1.3:80"); // p2 a changé d'adresse
        // p3 déconnectée.
        Pdm pdm4 = new Pdm("p4", "192.168.1.5:2726"); // p4 nouvelle arrivée.
        ListePdm p = new ListePdm();
        p.ajouter(pdm1);
        p.ajouter(pdm2);
        p.ajouter(pdm4);
        listePdmMemoire.miseAJour(p);
        assertThat(listePdmMemoire.acceder("p1").getActive()).isTrue();
        assertThat(listePdmMemoire.acceder("p1").getVisitee()).isFalse();
        assertThat(listePdmMemoire.acceder("p2").getActive()).isTrue();
        assertThat(listePdmMemoire.acceder("p2").getVisitee()).isTrue();
        assertThat(listePdmMemoire.acceder("p3").getActive()).isFalse();
        assertThat(listePdmMemoire.acceder("p3").getVisitee()).isFalse();
        assertThat(listePdmMemoire.acceder("p4").getActive()).isTrue();
        assertThat(listePdmMemoire.acceder("p4").getVisitee()).isFalse();

        pdm1 = new Pdm("p1", "192.168.1.52:8080"); // p1 changé d'adresse
        // p2 déconnectée.
        pdm4 = new Pdm("p4", "192.168.1.5:2726"); // p4 nouvelle arrivée.
        p = new ListePdm();
        p.ajouter(pdm1);
        p.ajouter(pdm4);
        listePdmMemoire.miseAJour(p);
        assertThat(listePdmMemoire.acceder("p1").getActive()).isTrue();
        assertThat(listePdmMemoire.acceder("p1").getVisitee()).isFalse();
        assertThat(listePdmMemoire.acceder("p2").getActive()).isFalse();
        assertThat(listePdmMemoire.acceder("p2").getVisitee()).isTrue();
        assertThat(listePdmMemoire.acceder("p3").getActive()).isFalse();
        assertThat(listePdmMemoire.acceder("p3").getVisitee()).isFalse();
        assertThat(listePdmMemoire.acceder("p4").getActive()).isTrue();
        assertThat(listePdmMemoire.acceder("p4").getVisitee()).isFalse();
    }

    @Test
    public void when_removed_by_name_a_pdm_must_not_be_present_anymore() {
        listePdmMemoire.ajouter(new PdmMemoire("pdmName", "127.0.0.1:1981", false, false, new ListeProgramme(), 3));

        listePdmMemoire.supprimer(new PdmMemoire("pdmName", "192.168.0.1:8080", true, true, new ListeProgramme(), 76));

        assertThat(listePdmMemoire.acceder("pdmName")).isNull();
    }

    @Test
    public void new_pdm_list_must_be_empty() {
        ListePdmMemoire pdmMemoryList = new ListePdmMemoire(null);
        @SuppressWarnings("unchecked")
        final Map<String, Pdm> internalList = pdmMemoryList.getListe();

        assertThat(pdmMemoryList.estVide()).isTrue();
        assertThat(internalList).isNotNull();
        assertThat(internalList.isEmpty()).isTrue();
    }

    @Test
    public void pdm_list_must_be_transcoded_to_understandable_string() {
        final ListePdmMemoire pdmList = new ListePdmMemoire(null);
        final PdmMemoire newPdm = new PdmMemoire("pdmName", "127.0.0.1:1981", false, false, new ListeProgramme(), 1);
        pdmList.ajouter(newPdm);

        final String[] humanReadableString = pdmList.toString(1).split("\n");

        assertThat(humanReadableString[0])
                .isEqualTo(
                        " nom = pdmName, adresse = [ip = 127.0.0.1, port = 1981], visitée = false, active = false, programme = ");
        assertThat(humanReadableString[1]).startsWith("  date = ").endsWith(", liste = ");
    }
    
    @Test
    public void pdm_list_with_some_active_one_must_be_active() {
        assertThat(listePdmMemoire.aucuneActive()).isFalse();
    }
    
    @Test
    public void pdm_list_with_no_active_ones_must_not_be_active() {
        listePdmMemoire.supprimer(new PdmMemoire("p2", "127.0.0.1:1981", false, false, null, 0));
        listePdmMemoire.supprimer(new PdmMemoire("p3", "127.0.0.1:1981", false, false, null, 0));
        
        assertThat(listePdmMemoire.aucuneActive()).isTrue();
    }
    
    @Test
    public void pdm_list_must_return_first_active_not_already_visited() {
        final PdmMemoire firstActiveNotAlreadyVisited = listePdmMemoire.premiereActiveNonVisitee();
        assertThat(firstActiveNotAlreadyVisited).isNotNull();
        assertThat(firstActiveNotAlreadyVisited.getNom()).isEqualTo("p3");
    }
}
