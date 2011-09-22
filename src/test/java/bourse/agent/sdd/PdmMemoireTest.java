package bourse.agent.sdd;

import static org.fest.assertions.Assertions.assertThat;

import org.junit.Before;
import org.junit.Test;

public class PdmMemoireTest {

    private PdmMemoire pdmMemoire;

    @Before
    public final void init_new_pdmMemoire() {
        pdmMemoire = new PdmMemoire("name", "127.0.0.1:8080", false, false, new ListeProgramme(), 1);
    }

    @Test
    public final void should_update_unhandled_bids() {
        pdmMemoire.setNonEnchereGeree(2);
        pdmMemoire.setNonEnchereGeree(4);
        final boolean[] unhandledBids = pdmMemoire.getEnchereGeree();

        assertThat(unhandledBids).isEqualTo(new boolean[] { true, false, true, false, true });
    }

    @Test
    public final void testSetVisitee() {
        pdmMemoire.setVisitee(true);

        assertThat(pdmMemoire.getVisitee()).isTrue();
    }

    @Test
    public final void testSetProgramme() {
        final ListeProgramme programList = new ListeProgramme();

        pdmMemoire.setProgramme(programList);

        assertThat(pdmMemoire.getProgramme()).isSameAs(programList);
    }

    @Test
    public final void testSetNumeroDernierTour() {
        pdmMemoire.setNumeroDernierTour(42);

        assertThat(pdmMemoire.getNumeroDernierTour()).isEqualTo(42);
    }

}
