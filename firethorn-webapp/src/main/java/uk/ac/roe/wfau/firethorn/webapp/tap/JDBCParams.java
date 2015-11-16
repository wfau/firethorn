package uk.ac.roe.wfau.firethorn.webapp.tap;

public class JDBCParams {

	private String username;
	private String password;
	private String connectionURL;
	private String driver;
	private String catalogue;

	public JDBCParams(String connectionURL, String username, String password, String driver) {
		super();
		this.username = username;
		this.password = password;
		this.connectionURL = connectionURL;
		this.driver = driver;
	}

	public JDBCParams(String connectionURL, String username, String password, String driver, String catalogue) {
		super();
		this.username = username;
		this.password = password;
		this.connectionURL = connectionURL;
		this.driver = driver;
		this.catalogue = catalogue;
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
	
	public String getCatalogue() {
		return catalogue;
	}

	public void setCatalogue(String catalogue) {
		this.catalogue = catalogue;
	}

}
