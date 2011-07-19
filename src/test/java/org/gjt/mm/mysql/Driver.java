package org.gjt.mm.mysql;

import java.sql.DriverManager;
import java.sql.DriverPropertyInfo;
import java.sql.SQLException;
import java.util.Properties;

public class Driver implements java.sql.Driver {

	static {
		try {
			DriverManager.registerDriver(new Driver());
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private final Connection connection = new Connection();

	@Override
	public boolean acceptsURL(String url) throws SQLException {
		return url != null && url.startsWith("jdbc:mysql");
	}

	@Override
	public Connection connect(String url, Properties info) throws SQLException {
		if (url != null && url.startsWith("jdbc:mysql")) {
			return connection;
		}
		return null;
	}

	@Override
	public int getMajorVersion() {
		return 0;
	}

	@Override
	public int getMinorVersion() {
		return 0;
	}

	@Override
	public DriverPropertyInfo[] getPropertyInfo(String url, Properties info) throws SQLException {
		return null;
	}

	@Override
	public boolean jdbcCompliant() {
		return true;
	}

}
