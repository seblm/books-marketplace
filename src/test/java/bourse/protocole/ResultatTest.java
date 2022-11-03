package bourse.protocole;

import static bourse.protocole.TypeMessage.TM_RESULTAT;
import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Mockito.mock;

import org.junit.Test;

import bourse.sdd.Livre;

public class ResultatTest extends SAXTest {

    @Test
    public void should_create_instance_by_name_and_description() {
        Livre book = mock(Livre.class);
        Resultat resultat = new Resultat(book, "buyer", 42f);

        assertThat(resultat.getAcheteur()).isEqualTo("buyer");
        assertThat(resultat.getEnchere()).isEqualTo(42f);
        assertThat(resultat.getLivre()).isEqualTo(book);
        assertThat(resultat.getType().getValue()).isEqualTo(TM_RESULTAT);
    }

    @Test
    public void should_create_instance_by_xml() {
        StringBuilder xmlBuilder = new StringBuilder();
        xmlBuilder.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        xmlBuilder.append("<!DOCTYPE MSG SYSTEM \"src/main/resources/MSG.dtd\">\n");
        xmlBuilder.append("<MSG>\n");
        xmlBuilder.append("    <RESULTAT>\n");
        xmlBuilder
                .append("        <LIVRE AUTEUR=\"author\" CATEGORIE=\"Aucune\" DATEPAR=\"publicationDate\" EDITEUR=\"editor\" ETAT=\"42.0\" FORMAT=\"format\" ID=\"42\" ISBN=\"isbn\" PRIX=\"42.0\" PROPRIETAIRE=\"owner\" TITRE=\"title\"/>\n");
        xmlBuilder.append("        <AGENT NOM=\"foo\"/>\n");
        xmlBuilder.append("        <ENCHERE>42.0</ENCHERE>\n");
        xmlBuilder.append("    </RESULTAT>\n");
        xmlBuilder.append("</MSG>\n");
        Protocole newInstance = Resultat.newInstance(xmlBuilder.toString());

        assertThat(newInstance).isNotNull();
        assertThat(newInstance).isInstanceOf(Resultat.class);
        assertThat(newInstance.getType().getValue()).isEqualTo(TM_RESULTAT);
        Resultat instance = (Resultat) newInstance;
        assertThat(instance.getLivre().getAuteur()).isEqualTo("author");
        assertThat(instance.getLivre().getCategorie().getCategorie()).isEqualTo(Categorie.AUCUNE);
        assertThat(instance.getLivre().getDateParu()).isEqualTo("publicationDate");
        assertThat(instance.getLivre().getEditeur()).isEqualTo("editor");
        assertThat(instance.getLivre().getEtat()).isEqualTo(42f);
        assertThat(instance.getLivre().getFormat()).isEqualTo("format");
        assertThat(instance.getLivre().getId()).isEqualTo(42);
        assertThat(instance.getLivre().getIsbn()).isEqualTo("isbn");
        assertThat(instance.getLivre().getPrix()).isEqualTo(42f);
        assertThat(instance.getLivre().getPrixAchat()).isEqualTo(0f);
        assertThat(instance.getLivre().getProprietaire()).isEqualTo("owner");
        assertThat(instance.getLivre().getTitre()).isEqualTo("title");
        assertThat(instance.getAcheteur()).isEqualTo("foo");
        assertThat(instance.getEnchere()).isEqualTo(42f);
        assertThat(instance.toXML()).isEqualTo(xmlBuilder.toString().replace("src/main/resources/MSG.dtd", "MSG.dtd"));
    }

}
