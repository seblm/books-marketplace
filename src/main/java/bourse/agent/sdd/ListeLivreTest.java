package bourse.agent.sdd;

import static org.fest.assertions.Assertions.assertThat;
import static org.fest.assertions.MapAssert.entry;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.whenNew;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import bourse.sdd.Livre;

@RunWith(PowerMockRunner.class)
public class ListeLivreTest {

    @Test
    public void should_create_liste_livre() {
        ListeLivre listeLivre = new ListeLivre();

        assertThat(listeLivre.toString(3)).isEqualTo("");
    }

    @Test
    public void should_add_and_retrieve_livre() {
        ListeLivre listeLivre = new ListeLivre();
        Livre livre = mock(Livre.class);

        listeLivre.ajouter(livre);

        assertThat(listeLivre.getValeurs()).containsOnly(livre);
        assertThat(listeLivre.getListe()).includes(entry(0, livre));
    }

    @Test
    public void with_non_empty_list_should_remove_livre_by_oder() {
        ListeLivre listeLivre = new ListeLivre();
        listeLivre.ajouter(mock(Livre.class));

        listeLivre.supprimer(0);

        assertThat(listeLivre.getValeurs()).isEmpty();
    }

    @Test
    public void with_empty_list_should_not_remove_livre_by_oder() {
        ListeLivre listeLivre = new ListeLivre();

        listeLivre.supprimer(0);

        assertThat(listeLivre.getValeurs()).isEmpty();
    }

    @Test
    public void with_non_empty_list_should_remove_livre() {
        ListeLivre listeLivre = new ListeLivre();
        Livre livre = mock(Livre.class);
        listeLivre.ajouter(livre);

        listeLivre.supprimer(livre);

        assertThat(listeLivre.getValeurs()).isEmpty();
    }

    @Test
    public void with_empty_list_should_not_remove_livre() {
        ListeLivre listeLivre = new ListeLivre();
        Livre livre = mock(Livre.class);

        listeLivre.supprimer(livre);

        assertThat(listeLivre.getValeurs()).isEmpty();
    }

    @Test
    @PrepareForTest(ListeLivre.class)
    public void should_launch_main_program() throws Exception {
        ListeLivre listeLivre = spy(new ListeLivre());
        whenNew(ListeLivre.class).withNoArguments().thenReturn(listeLivre);

        ListeLivre.main(null);

        verify(listeLivre, times(3)).ajouter(any(Livre.class));
        verify(listeLivre).supprimer(any(Integer.class));
        verify(listeLivre).supprimer(any(Livre.class));
    }
}
