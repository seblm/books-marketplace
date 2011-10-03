package bourse.agent.sdd;

import static com.google.common.collect.Lists.newArrayList;
import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.verifyNew;
import static org.powermock.api.mockito.PowerMockito.whenNew;

import java.util.LinkedList;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import bourse.protocole.Categorie;
import bourse.sdd.Livre;
import bourse.sdd.ProgrammePro;

import com.google.common.collect.Lists;

@RunWith(PowerMockRunner.class)
@PrepareForTest(ListeProgramme.class)
public class ListeProgrammeTest {

    private ProgrammePro program;

    @Before
    public final void createProgramPro() {
        program = new ProgrammePro(1, new Livre("title", "author", new Categorie(Categorie.AUCUNE), "format", "editor",
                1f, 1f, "1981-12-24", "ISBN-NUM", 2, "owner", 2f));
    }

    @Test
    public final void new_ListeProgramme_should_be_constructed_by_LinkedList() {
        final LinkedList<ProgrammePro> programsLinkedList = Lists.<ProgrammePro> newLinkedList(newArrayList(program));

        ListeProgramme programsList = new ListeProgramme(programsLinkedList);

        assertThat(programsList.getIeme(1)).isSameAs(program);
    }

    @Test
    public final void programs_should_be_added() {
        final ListeProgramme programsList = new ListeProgramme();

        programsList.ajouter(program);

        assertThat(programsList.getIeme(1)).isSameAs(program);
    }

    @Test
    public final void should_display_human_readable_string() {
        final ListeProgramme programsList = new ListeProgramme();
        programsList.ajouter(program);

        final String[] humanReadableString = programsList.toString(1).split("\n");

        assertThat(humanReadableString[0]).startsWith(" date = ").endsWith(", liste = ");
        assertThat(humanReadableString[1]).startsWith("  numéro = 1, livre = [").endsWith("]");
        assertThat(humanReadableString).hasSize(2);
    }

    @Test
    public void should_output_some_data_to_console_when_executed() throws Exception {
        ListeProgramme listeProgramme = mock(ListeProgramme.class);
        whenNew(ListeProgramme.class).withNoArguments().thenReturn(listeProgramme);

        ListeProgramme.main(null);

        verifyNew(ListeProgramme.class, times(2)).withNoArguments();
        verify(listeProgramme, times(3)).toString(0);
    }

}
