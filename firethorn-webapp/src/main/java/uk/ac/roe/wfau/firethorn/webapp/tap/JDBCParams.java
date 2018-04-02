package uk.ac.roe.wfau.firethorn.webapp.tap;

import lombok.extern.slf4j.Slf4j;
import uk.ac.roe.wfau.firethorn.meta.jdbc.JdbcProductType;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

@Slf4j
@Component
public class JDBCParams {

    @Value("${firethorn.tapschema.resource.name}")
    private String jdbcname;

    @Value("${firethorn.tapschema.database.user}")
    private String username;

    @Value("${firethorn.tapschema.database.pass}")
	private String password;
    
    @Value("${firethorn.tapschema.database.name}")
	private String catalog;
    
    @Value("${firethorn.tapschema.database.name}")
	private String database;
    
    @Value("${firethorn.tapschema.database.host}")
	private String host;
    
    @Value("${firethorn.tapschema.database.type}")
	private String type;
	
    @Value("${firethorn.tapschema.database.driver}")
	private String driver;
    
    @Value("${firethorn.tapschema.database.port}")
	private Integer port;

	public JDBCParams() {
		super();
	}

	public JDBCParams(String username, String password, String catalog, String database, String host, String type, String driver, Integer port) {
		super();
		this.username = username;
		this.password = password;
		this.catalog = catalog;
		this.database = database;
		this.host = host;
		this.type = type;
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

	
	public JdbcProductType getTypeAsJdbcProductType() {
		if (type.toLowerCase().equals("pgsql")){
            return JdbcProductType.pgsql;
		} else if (type.toLowerCase().equals("mssql")){
			return JdbcProductType.mssql;
		} else {
			return JdbcProductType.pgsql;
		}
	}

	public String getType() {
		return type;
	}
	
	public void setType(String type) {
		this.type = type;
	}
	
	public String getConnectionURL() {
		if (type.toLowerCase().equals("pgsql")){
            return "jdbc:postgresql://" + this.getHost() + "/" + this.getDatabase();
		} else if (type.toLowerCase().equals("mssql")){
			return  "jdbc:jtds:sqlserver://" + this.getHost() + "/" + this.getDatabase();
		} else {
			return "jdbc:postgresql://" + this.getHost() + "/" + this.getDatabase();
		}
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
