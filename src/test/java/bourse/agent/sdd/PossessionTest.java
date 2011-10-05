package bourse.agent.sdd;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.verifyNew;
import static org.powermock.api.mockito.PowerMockito.whenNew;

import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import bourse.agent.Visualisation;
import bourse.protocole.Categorie;
import bourse.sdd.Livre;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ Visualisation.class, Possession.class, JTable.class })
public class PossessionTest {

    private Possession ownership;

    @Before
    public final void initOwnership() {
        ownership = new Possession(new Livre("title", "author", Categorie.newCategorieFromBd(Categorie.BD), "format",
                "editor", 10, 1, "2010-01-01", "ISBN-NUM", 42, "owner", 9.3f));
    }

    @Test
    public final void should_not_be_a_sold_instance_when_created() {
        final boolean soldInstance = ownership.getInstanceDeVente();

        assertThat(soldInstance).isFalse();
    }

    @Test
    public final void should_be_a_sold_instance() {
        ownership.setInstanceDeVente(true);

        final boolean soldInstance = ownership.getInstanceDeVente();

        assertThat(soldInstance).isTrue();
    }

    @Test
    public final void should_be_executable() throws Exception {
        final Visualisation window = mock(Visualisation.class);
        whenNew(Visualisation.class).withNoArguments().thenReturn(window);
        final Possession ownership = mock(Possession.class);
        whenNew(Possession.class).withArguments(any(Livre.class)).thenReturn(ownership);
        final JTable table = mock(JTable.class);
        whenNew(JTable.class).withArguments(any(DefaultTableModel.class)).thenReturn(table);
        final JScrollPane pane = mock(JScrollPane.class);
        when(window.getJScrollPanePossessionMemoire()).thenReturn(pane);

        Possession.main(new String[0]);

        verifyNew(Possession.class, times(3)).withArguments(any(Livre.class));
        verify(ownership, times(3)).toString(anyInt());
    }

}
