package bourse.sdd;

import static org.fest.assertions.Assertions.assertThat;

import org.junit.Test;

import bourse.protocole.Categorie;

public class LivreTest {

    @Test
    public void intiialize_the_bean() {
        Livre livre = new Livre("lupin", "leblanc", new Categorie(
                Categorie.SCIENCE), "poch", "belin", 153f, 0.4f, "15/11/00",
                "yetet", 12, "protocoleman", 50.95f);

        assertThat(livre.getTitre()).isEqualTo("lupin");
        assertThat(livre.getAuteur()).isEqualTo("leblanc");
        assertThat(livre.getCategorie().getCategorie()).isEqualTo(
                Categorie.SCIENCE);
        assertThat(livre.getFormat()).isEqualTo("poch");
        assertThat(livre.getEditeur()).isEqualTo("belin");
        assertThat(livre.getPrix()).isEqualTo(153f);
        assertThat(livre.getEtat()).isEqualTo(0.4f);
        assertThat(livre.getDateParu()).isEqualTo("15/11/00");
        assertThat(livre.getIsbn()).isEqualTo("yetet");
        assertThat(livre.getId()).isEqualTo(12);
        assertThat(livre.getProprietaire()).isEqualTo("protocoleman");
        assertThat(livre.getPrixAchat()).isEqualTo(50.95f);

        assertThat(livre.toString(3))
                .isEqualTo(
                        "   Auteur = leblanc, Catégorie = Science, Date Parution = 15/11/00, Editeur = belin, Etat = 0.4, Format = poch, Id = 12, Isbn = yetet, Prix = 153.0, Titre = lupin, Proprietaire = protocoleman, PrixAchat = 50.95");

        assertThat(livre.toHtml())
                .isEqualTo(
                        "Livre n°12 : <i>lupin</i> d'une valeur de <b>61 Euros</b>, d&eacute;tenu par <b>protocoleman</b>, achet&eacute; <b>50.95 Euros</b>.");
    }

}
