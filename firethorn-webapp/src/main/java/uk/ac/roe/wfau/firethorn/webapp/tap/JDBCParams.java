package uk.ac.roe.wfau.firethorn.webapp.tap;

public class JDBCParams {

	private String username;
	private String password;
	private String connectionURL;
	private String driver;


	public JDBCParams(String connectionURL, String username, String password, String driver) {
		super();
		this.username = username;
		this.password = password;
		this.connectionURL = connectionURL;
		this.driver = driver;
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
}
