package bourse.protocole;

import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.junit.BeforeClass;
import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

public abstract class SAXTest {

    protected static DocumentBuilder documentBuilder;

    @BeforeClass
    public static void initDocumentBuilderFactory() throws ParserConfigurationException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setCoalescing(true);
        factory.setExpandEntityReferences(true);
        factory.setIgnoringComments(true);
        factory.setIgnoringElementContentWhitespace(true);
        documentBuilder = factory.newDocumentBuilder();
        documentBuilder.setEntityResolver(new EntityResolver() {
            @Override
            public InputSource resolveEntity(String publicId, String systemId) throws SAXException, IOException {
                if (systemId.endsWith(Protocole.URI_DTD)) {
                    return new InputSource(ErreurTest.class.getResourceAsStream("/" + Protocole.URI_DTD));
                }
                return null;
            }
        });

        documentBuilder.setErrorHandler(new ErrorHandler() {
            public void fatalError(SAXParseException exception) throws SAXException {
            }

            public void error(SAXParseException e) throws SAXParseException {
                throw e;
            }

            public void warning(SAXParseException err) throws SAXParseException {
            }
        });
    }

}
