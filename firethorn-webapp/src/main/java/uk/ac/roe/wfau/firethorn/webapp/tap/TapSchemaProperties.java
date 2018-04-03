package uk.ac.roe.wfau.firethorn.webapp.tap;

import lombok.extern.slf4j.Slf4j;
import uk.ac.roe.wfau.firethorn.meta.jdbc.JdbcProductType;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

@Slf4j
public class TapSchemaProperties {
	
	private String baseurl;
	private String jdbcname;
	private String username;
	private String password;
	private String catalog;
	private String database;
	private String host;
	private String type;
	private String driver;
	private String port;

	public TapSchemaProperties() {
		super();
	}

	public TapSchemaProperties(String username, String password, String catalog, String database, String host, String type, String driver, String port, String jdbcname) {
		super();
		this.username = username;
		this.password = password;
		this.catalog = catalog;
		this.database = database;
		this.host = host;
		this.type = type;
		this.driver = driver;
		this.port = port;
		this.jdbcname = jdbcname;
	}

	public JdbcProductType getTypeAsJdbcProductType() {
		if (this.getType().toLowerCase().equals("pgsql")) {
			return JdbcProductType.pgsql;
		} else if (type.toLowerCase().equals("mssql")) {
			return JdbcProductType.mssql;
		} else {
			return JdbcProductType.pgsql;
		}
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getDatabase() {
		return database;
	}

	public void setDatabase(String database) {
		this.database = database;
	}

	public String getCatalog() {
		return catalog;
	}

	public void setCatalog(String catalogue) {
		this.catalog = catalogue;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getType() {
		if (type == null) {
			return "pgsql";
		} else {
			return null;
		}
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getDriver() {
		if (driver == null) {
			return "org.postgresql.Driver";
		} else {
			return driver;
		}

	}

	public void setDriver(String driver) {
		this.driver = driver;
	}

	public String getPort() {
		if (port == null) {
			return "5432";
		} else {
			return port;
		}

	}

	public void setPort(String port) {
		this.port = port;
	}

	public String getConnectionURL() {
		if (this.getType().toLowerCase().equals("pgsql")) {
			return "jdbc:postgresql://" + this.getHost() + "/" + this.getDatabase();
		} else if (this.getType().toLowerCase().equals("mssql")) {
			return "jdbc:jtds:sqlserver://" + this.getHost() + "/" + this.getDatabase();
		} else {
			return "jdbc:postgresql://" + this.getHost() + "/" + this.getDatabase();
		}
	}

	public void setJdbcname(String jdbcname) {
		this.jdbcname = jdbcname;
	}

	public String getJdbcname() {
		return jdbcname;
	}

	/**
	 * Get baseurl
	 * 
	 * @return baseurl
	 */
	public String getBaseurl() {
		return baseurl;
	}

	/**
	 * Set baseurl
	 * 
	 * @return
	 */
	public void setBaseurl(String baseurl) {
		this.baseurl = baseurl;
	}
	
}
