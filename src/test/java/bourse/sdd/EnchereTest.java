package bourse.sdd;

import static bourse.sdd.TestFactory.createLivre;
import static org.fest.assertions.Assertions.assertThat;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class EnchereTest {

    @Test
    public void with_a_document_node_should_add_bid() throws ParserConfigurationException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.newDocument();
        Element root = document.createElement("root");
        Enchere bid = new Enchere(1, createLivre());

        bid.addElement(root);

        assertThat(root.hasChildNodes()).isEqualTo(true);
        Node bidNode = root.getFirstChild();
        assertThat(bidNode.getNodeName()).isEqualTo("ENCHERE");
        Node bidNumberAttribute = bidNode.getAttributes().item(0);
        assertThat(bidNumberAttribute.getNodeName()).isEqualTo("NUMERO");
        assertThat(bidNumberAttribute.getNodeValue()).isEqualTo("1");
        assertThat(bidNode.hasChildNodes()).isEqualTo(true);
        Node bidBookNode = bidNode.getFirstChild();
        assertThat(bidBookNode.getNodeName()).isEqualTo("LIVRE");
        assertThat(bidBookNode.getAttributes().getLength()).isEqualTo(4);
        Node bidBookCategoryAttribute = bidBookNode.getAttributes().item(0);
        assertThat(bidBookCategoryAttribute.getNodeName()).isEqualTo("CATEGORIE");
        assertThat(bidBookCategoryAttribute.getNodeValue()).isEqualTo("Science");
        Node bidBookStateCategoryAttribute = bidBookNode.getAttributes().item(1);
        assertThat(bidBookStateCategoryAttribute.getNodeName()).isEqualTo("ETAT");
        assertThat(bidBookStateCategoryAttribute.getNodeValue()).isEqualTo("0.4");
        Node bidBookStatePriceAttribute = bidBookNode.getAttributes().item(2);
        assertThat(bidBookStatePriceAttribute.getNodeName()).isEqualTo("PRIX");
        assertThat(bidBookStatePriceAttribute.getNodeValue()).isEqualTo("153.0");
        Node bidBookStateTitleAttribute = bidBookNode.getAttributes().item(3);
        assertThat(bidBookStateTitleAttribute.getNodeName()).isEqualTo("TITRE");
        assertThat(bidBookStateTitleAttribute.getNodeValue()).isEqualTo("lupin");
    }
}
