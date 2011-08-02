package bourse.agent.sdd;

import static org.fest.assertions.Assertions.assertThat;

import org.junit.Test;

import bourse.protocole.Protocole;
import bourse.protocole.ResultBye;

public class ListePdmTest {

	@Test
	public void testListePdmResultBye() {
		final ListePdm marketPlaces = new ListePdm((ResultBye)Protocole.newInstance("<MSG><RESULTBYE><PDM NOM=\"jhlfdhsl\" ADRESSE=\"192.168.1.1:24\"/></RESULTBYE></MSG>"));
        assertThat(marketPlaces.estVide()).isFalse();
        final Pdm marketPlace = marketPlaces.acceder("jhlfdhsl");
		assertThat(marketPlace).isNotNull();
		assertThat(marketPlace.getAdresse().ipToString()).isEqualTo("192.168.1.1");
		assertThat(marketPlace.getAdresse().getPort()).isEqualTo(24);
	}

}
