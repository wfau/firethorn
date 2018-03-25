package uk.ac.roe.wfau.firethorn.webapp.tap;

import uk.ac.roe.wfau.firethorn.meta.jdbc.JdbcProductType;

public class JDBCParams {

	private String username;
	private String password;
	private String catalog;
	private String database;
	private String host;
	private String type;
	private String connectionURL;
	private String driver;
	private Integer port;
	
	public JDBCParams(String username, String password, String catalog, String database, String host, String type, String driver, Integer port) {
		super();
		this.username = username;
		this.password = password;
		this.catalog = catalog;
		this.database = database;
		this.host = host;
		this.type = type;
        this.connectionURL = "jdbc:postgresql://" + host + "/" + database;
		this.driver = driver;
		this.port = port;
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
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	public String getConnectionURL() {
		return connectionURL;
	}

	public void setConnectionURL(String connectionURL) {
		this.connectionURL = connectionURL;
	}

	public String getDriver() {
		return driver;
	}

	public void setDriver(String driver) {
		this.driver = driver;
	}
	
	public Integer getPort() {
		return port;
	}

	public void setPort(Integer port) {
		this.port = port;
	}	
}
