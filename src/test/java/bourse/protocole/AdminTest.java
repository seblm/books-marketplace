package bourse.protocole;

import static bourse.protocole.Admin.ADMIN_DOC;
import static bourse.protocole.Admin.ADMIN_TERMINER;
import static bourse.protocole.TypeMessage.TM_ADMIN;
import static org.fest.assertions.Assertions.assertThat;

import org.junit.Test;

public class AdminTest extends SAXTest {

    @Test
    public void should_create_admin_doc() {
        Admin adminDoc = new Admin(Admin.ADMIN_DOC);

        assertThat(adminDoc.getType().getValue()).isEqualTo(TM_ADMIN);
        assertThat(adminDoc.getTypeRequete()).isEqualTo(ADMIN_DOC);
    }

    @Test
    public void should_create_admin_terminer() {
        Admin adminDoc = new Admin(Admin.ADMIN_TERMINER);

        assertThat(adminDoc.getType().getValue()).isEqualTo(TM_ADMIN);
        assertThat(adminDoc.getTypeRequete()).isEqualTo(ADMIN_TERMINER);
    }

    @Test
    public void should_create_admin_doc_by_xml() {
        StringBuilder xmlBuilder = new StringBuilder();
        xmlBuilder.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        xmlBuilder.append("<!DOCTYPE MSG SYSTEM \"src/main/resources/MSG.dtd\">\n");
        xmlBuilder.append("<MSG>\n");
        xmlBuilder.append("    <ADMIN REQUETE=\"index.html\"/>\n");
        xmlBuilder.append("</MSG>\n");
        Protocole newInstance = Admin.newInstance(xmlBuilder.toString());

        assertThat(newInstance).isNotNull();
        assertThat(newInstance).isInstanceOf(Admin.class);
        assertThat(newInstance.getType().getValue()).isEqualTo(TM_ADMIN);
        Admin instance = (Admin) newInstance;
        assertThat(instance.getTypeRequete()).isEqualTo(ADMIN_DOC);
        assertThat(instance.toXML()).isEqualTo(xmlBuilder.toString().replace("src/main/resources/MSG.dtd", "MSG.dtd"));
    }

    @Test
    public void should_create_admin_terminer_by_xml() {
        StringBuilder xmlBuilder = new StringBuilder();
        xmlBuilder.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        xmlBuilder.append("<!DOCTYPE MSG SYSTEM \"src/main/resources/MSG.dtd\">\n");
        xmlBuilder.append("<MSG>\n");
        xmlBuilder.append("    <ADMIN REQUETE=\"terminer\"/>\n");
        xmlBuilder.append("</MSG>\n");
        Protocole newInstance = Admin.newInstance(xmlBuilder.toString());

        assertThat(newInstance).isNotNull();
        assertThat(newInstance).isInstanceOf(Admin.class);
        assertThat(newInstance.getType().getValue()).isEqualTo(TM_ADMIN);
        Admin instance = (Admin) newInstance;
        assertThat(instance.getTypeRequete()).isEqualTo(ADMIN_TERMINER);
        assertThat(instance.toXML()).isEqualTo(xmlBuilder.toString().replace("src/main/resources/MSG.dtd", "MSG.dtd"));
    }
}
