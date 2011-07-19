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

	@BeforeClass
	public static void initDatabase() throws Exception {
		Class.forName("org.h2.Driver");
		connection = DriverManager.getConnection("jdbc:h2:mem:test", "", "");
		final Statement statement = connection.createStatement();
		BufferedReader in = new BufferedReader(new InputStreamReader(BdTest.class.getResourceAsStream("/create.sql")));
		String currentSQLOrder = null;
		while ((currentSQLOrder = in.readLine()) != null) {
			statement.execute(currentSQLOrder);
		}
		statement.close();
		// System.setProperty("jdbc.drivers", "org.gjt.mm.mysql.Driver");
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
	}

	@Test
	public void testBd() {
		try {
			new Bd(true);
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}

	@Test
	public void testConnexion() throws Exception {
		Bd bd = new Bd(true);
		try {
			bd.connexion();
		} catch (SQLException e) {
			fail(e.getMessage());
		} finally {
			bd.deconnexion();
		}
	}

	@Test
	public void testDeconnexion() throws Exception {
		Bd bd = new Bd(true);
		bd.connexion();
		bd.deconnexion();
	}

	@Test
	public void testResultat() throws Exception {
		Bd b = new Bd(true);
		try {
			ResultSet r = b.resultat("SELECT * FROM livres;");
			assertThat(r.next()).isTrue();
		} catch (SQLException e) {
			fail(e.getMessage());
		}
	}

	@Test
	public void testRequete() throws Exception {
		Bd b = new Bd(true);
		try {
			b.requete("UPDATE livres SET Categorie = 'Science' WHERE Categorie = 'Science fiction'");
		} catch (SQLException e) {
			fail(e.getMessage());
		}
	}

}
