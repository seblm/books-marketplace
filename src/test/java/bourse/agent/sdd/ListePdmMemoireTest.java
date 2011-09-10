package bourse.agent.sdd;

import static org.fest.assertions.Assertions.assertThat;

import org.junit.Before;
import org.junit.Test;

import bourse.agent.Visualisation;

public class ListePdmMemoireTest {

    private ListePdmMemoire listePdmMemoire;

    @Before
    public void initialisation() {
        Visualisation visu = new Visualisation();
        visu.setVisible(true);
        listePdmMemoire = new ListePdmMemoire(visu);
        listePdmMemoire.ajouter(new PdmMemoire("p1", "HOME", false, false, new ListeProgramme(), 1));
        listePdmMemoire.ajouter(new PdmMemoire("p2", "192.168.1.2:80", true, true, new ListeProgramme(), 2));
        listePdmMemoire.ajouter(new PdmMemoire("p3", "0.0.0.0:0", false, false, new ListeProgramme(), 2));
    }

    @Test
    public void testListePdmMemoire() {
        assertThat(listePdmMemoire.acceder("p1").getActive()).isFalse();
        assertThat(listePdmMemoire.acceder("p1").getVisitee()).isFalse();
        assertThat(listePdmMemoire.acceder("p2").getActive()).isTrue();
        assertThat(listePdmMemoire.acceder("p2").getVisitee()).isTrue();
        assertThat(listePdmMemoire.acceder("p3").getActive()).isFalse();
        assertThat(listePdmMemoire.acceder("p3").getVisitee()).isFalse();
        assertThat(listePdmMemoire.aucuneActive()).isFalse();
        assertThat(listePdmMemoire.premiereActiveNonVisitee()).isNull();
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
}
