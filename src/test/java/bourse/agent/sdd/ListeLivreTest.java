package bourse.agent.sdd;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.verifyNew;
import static org.powermock.api.mockito.PowerMockito.whenNew;

import java.util.Collection;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import bourse.protocole.Categorie;
import bourse.sdd.Livre;

@RunWith(PowerMockRunner.class)
@PrepareForTest(ListeLivre.class)
public class ListeLivreTest {

    private ListeLivre booksList;
    private Livre book;

    @Before
    public final void initNewAgentsList() {
        booksList = new ListeLivre();
        book = new Livre("title", "author", Categorie.newCategorieFromBd(Categorie.BD), "format", "editor", 10, 1,
                "2010-01-01", "ISBN-NUM", 42, "owner", 9.3f);
    }
    
    @Test
    public final void should_not_keep_same_book_identified_by_id() {
        booksList.ajouter(book);
        final Livre bookWithId42 = new Livre("title", "author", Categorie.newCategorieFromBd(Categorie.BD), "format", "editor", 10, 1,
                "2010-01-01", "ISBN-NUM", 42, "owner", 9.3f);
        
        booksList.ajouter(bookWithId42);
        
        assertThat(booksList.getValeurs()).hasSize(1).contains(bookWithId42).excludes(book);
    }
    
    @Test
    public final void should_removes_a_book_by_id() {
        booksList.ajouter(book);
        
        booksList.supprimer(42);
        
        assertThat(booksList.getValeurs()).isEmpty();
    }

    @Test
    public final void should_removes_a_book_by_instance() {
        booksList.ajouter(book);
        
        booksList.supprimer(book);
        
        assertThat(booksList.getValeurs()).isEmpty();
    }

    @Test
    public final void should_be_transformed_to_a_human_readable_string() {
        booksList.ajouter(book);

        final String stringRepresentation = booksList.toString(3);

        assertThat(stringRepresentation).isEqualTo("       Auteur = author, Catégorie = Bandes Dessinées, Date Parution = 2010-01-01, Editeur = editor, Etat = 1.0, Format = format, Id = 42, Isbn = ISBN-NUM, Prix = 10.0, Titre = title, Proprietaire = owner, PrixAchat = 9.3");
    }

    @Test
    public final void empty_instance_should_be_transformed_to_empty_string() {
        final String stringRepresentation = booksList.toString(0);

        assertThat(stringRepresentation).hasSize(0);
    }

    @Test
    public final void should_return_books_that_it_contains() {
        booksList.ajouter(book);
        final Livre secondBook = new Livre("title", "author", Categorie.newCategorieFromBd(Categorie.BD), "format", "editor", 10, 1,
                "2010-01-01", "ISBN-NUM", 1, "owner", 9.3f);
        booksList.ajouter(secondBook);

        @SuppressWarnings("rawtypes")
        final Collection values = booksList.getValeurs();

        assertThat(values).containsOnly(book, secondBook);
    }

    @Test
    public final void should_be_executable() throws Exception {
        final ListeLivre booksList = mock(ListeLivre.class);
        whenNew(ListeLivre.class).withNoArguments().thenReturn(booksList);

        ListeLivre.main(new String[0]);

        verifyNew(ListeLivre.class, times(1)).withNoArguments();
        verify(booksList, times(3)).ajouter(any(Livre.class));
        verify(booksList, times(1)).toString(5);
    }

}
