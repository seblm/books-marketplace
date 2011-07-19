package bourse.agent;

import static org.fest.assertions.Assertions.assertThat;
import static org.junit.Assert.fail;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import bourse.agent.sdd.PdmMemoire;

public class MemoireTest {

	private Memoire memoire;

	@Before
	public void initMemoire() {
		Visualisation visu = new Visualisation();
		memoire = new Memoire(new Agent("test"), visu);
	}

	@Test
	public void testMemoire() {
		assertThat(memoire.getAgents()).isNotNull();
		assertThat(memoire.getAideDecisionVente()).isNotNull();
		assertThat(memoire.getLivreAVendre()).isEqualTo(0);
		assertThat(memoire.getPdms()).isNotNull();
		assertThat(memoire.getPossessions()).isNotNull();
		assertThat(memoire.getTemps()).isEqualTo(0);
		memoire.setLivreAVendre(3);
		assertThat(memoire.getLivreAVendre()).isEqualTo(3);
	}

	public void testCalculTemps() {
		memoire.getPdms().ajouter(new PdmMemoire(null, null, true, true, null, 1));
		memoire.getPdms().ajouter(new PdmMemoire(null, null, false, true, null, 10));
		memoire.getPdms().ajouter(new PdmMemoire(null, null, true, false, null, 100));
		memoire.getPdms().ajouter(new PdmMemoire(null, null, false, false, null, 1000));
		assertThat(memoire.getTemps()).isEqualTo(0);
		memoire.refreshTemps();
		assertThat(memoire.getTemps()).isEqualTo(1111);
	}

	@Test
	@Ignore("must not divide by zero")
	public void testRefreshTempsWithoutPdmMemoire() {
		try {
			memoire.refreshTemps();
		} catch (ArithmeticException e) {
			fail(e.getMessage());
		}
	}

	@After
	public void removeMemoire() {
		memoire = null;
	}

}
