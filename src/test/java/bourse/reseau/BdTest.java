package bourse.reseau;

import static org.fest.assertions.Assertions.assertThat;
import static org.junit.Assert.fail;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.operation.DatabaseOperation;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class BdTest {

    private static Connection connection;

    private Bd database;

    @BeforeClass
    public static void initDatabase() throws Exception {
        connection = DriverManager.getConnection("jdbc:h2:mem:test", "SA", "");
        final Statement statement = connection.createStatement();
        BufferedReader in = new BufferedReader(new InputStreamReader(BdTest.class.getResourceAsStream("/create.sql")));
        String currentSQLOrder = null;
        while ((currentSQLOrder = in.readLine()) != null) {
            statement.execute(currentSQLOrder);
        }
        statement.close();
    }

    @Before
    public void populate() throws Exception {
        final IDatabaseConnection dbUnitConnection = new DatabaseConnection(connection);
        final FlatXmlDataSetBuilder dataSetBuilder = new FlatXmlDataSetBuilder();
        dataSetBuilder.setMetaDataSetFromDtd(BdTest.class.getResourceAsStream("/dataset.dtd"));
        dataSetBuilder.setDtdMetadata(true);
        dataSetBuilder.setColumnSensing(true);
        IDataSet dataSet = dataSetBuilder.build(BdTest.class.getResourceAsStream("/dataset.xml"));
        DatabaseOperation.CLEAN_INSERT.execute(dbUnitConnection, dataSet);
        database = new Bd(connection);
    }

    @Test
    public void testResultat() throws Exception {
        try {
            ResultSet r = database.resultat("SELECT * FROM livres;");
            assertThat(r.next()).isTrue();
        } catch (SQLException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testRequete() throws Exception {
        try {
            database.requete("UPDATE livres SET Categorie = 'Science' WHERE Categorie = 'Science fiction'");
        } catch (SQLException e) {
            fail(e.getMessage());
        }
    }

}
