package uk.ac.roe.wfau.firethorn.webapp.tap;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.servlet.ServletContext;

import org.springframework.beans.factory.annotation.Value;

import lombok.extern.slf4j.Slf4j;
import uk.ac.roe.wfau.firethorn.access.ProtectionException;
import uk.ac.roe.wfau.firethorn.entity.exception.NameNotFoundException;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlColumn;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlResource;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlSchema;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlTable;
import uk.ac.roe.wfau.firethorn.meta.jdbc.JdbcProductType;
import uk.ac.roe.wfau.firethorn.meta.jdbc.JdbcResource;
import uk.ac.roe.wfau.firethorn.meta.jdbc.JdbcSchema;
import uk.ac.roe.wfau.firethorn.spring.ComponentFactories;

/**
 * Generate TAP_SCHEMA of a resource
 * 
 * @author stelios
 * 
 */
@Slf4j
public class TapSchemaGeneratorImpl implements TapSchemaGenerator{

	@Value("${firethorn.webapp.baseurl:null}")
	private String baseurl;
	
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
    
    @Value("${firethorn.tapschema.database.type:pgsql}")
	private String type;
	
    @Value("${firethorn.tapschema.database.driver}")
	private String driver;
    
    @Value("${firethorn.tapschema.database.port}")
	private String port;

	/**
	 * Adql resource for which to create the TAP_SCHEMA
	 */
	private AdqlResource resource;
	
	/**
	 * Servlet Context
	 */
	private ServletContext servletContext;

	/**
	 * Script to create Tap Schema
	 */
	private String tapSchemaScript;
	
	/**
	 * tap schema JDBC name
	 */
	private String tapSchemaJDBCName;
	
	/**
	 * TAP_SCHEMA resource JDBC name
	 */
	private String tapSchemaResourceJDBCName;

    /**
     *  ComponentFactories instance.
     *
     */
  
    private ComponentFactories factories;

    public JdbcProductType getTypeAsJdbcProductType() {
		if (type.toLowerCase().equals("pgsql")){
            return JdbcProductType.pgsql;
		} else if (type.toLowerCase().equals("mssql")){
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
		return type;
	}
	
	public void setType(String type) {
		this.type = type;
	}
	

	public String getDriver() {
		return driver;
	}

	public void setDriver(String driver) {
		this.driver = driver;
	}
	
	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
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

    /**
     * Our system services.
     *
     */
    public ComponentFactories factories()
        {
        return this.factories;
        }
    
	/**
	 * Empty Constructor
	 */
	public TapSchemaGeneratorImpl() {
		super();
	}
	
	/**
	 * Constructor
	 */
	public TapSchemaGeneratorImpl(ServletContext servletContext,ComponentFactories factories, AdqlResource resource) {
		super();
		this.servletContext = servletContext;
		this.resource = resource;
		this.tapSchemaScript = "/WEB-INF/data/" + "pgsql_tap_schema";
		this.factories = factories;
		this.tapSchemaJDBCName = "TAP_SCHEMA_" + this.resource.ident().toString();
		this.tapSchemaResourceJDBCName = "TAP_RESOURCE_" + this.resource.ident().toString();
		
		if (this.getType().toLowerCase().equals("pgsql")){
			this.tapSchemaScript = "/WEB-INF/data/" + "pgsql_tap_schema.sql";
		} else if (this.getType().toLowerCase().equals("mssql")){
			this.tapSchemaScript = "/WEB-INF/data/" + "sqlserver_tap_schema.sql";
		}

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
	
	public String getTapSchemaScript() {
		return tapSchemaScript;
	}

	public void setTapSchemaScript(String tapSchemaScript) {
		this.tapSchemaScript = tapSchemaScript;
	}

	public String getTapSchemaJDBCName() {
		return tapSchemaJDBCName;
	}

	public void setTapSchemaJDBCName(String tapSchemaJDBCName) {
		this.tapSchemaJDBCName = tapSchemaJDBCName;
	}
	
	public String getTapSchemaResourceJDBCName() {
		return tapSchemaJDBCName;
	}

	public void setTapSchemaResourceJDBCName(String tapSchemaResourceJDBCName) {
		this.tapSchemaResourceJDBCName = tapSchemaResourceJDBCName;
	}
	
	
	/**
	 * Run the 'sql' update SQL command
	 * @param sql
	 */
	private void updateSQL(String sql) {
		java.sql.Statement stmt = null;
		java.sql.Connection con = null;
		
		try {
			stmt = null;
			Class.forName(this.driver);
			con = DriverManager.getConnection(this.getConnectionURL(),
					this.getUsername(), this.getPassword());

			log.debug("Inserting resource records into the table...");
			stmt = con.createStatement();			
			stmt.executeUpdate(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} finally {

			if (con != null)
				try {
					con.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}

		}
		
	}
	
	/**
	 * Create the initial TAP_SCHEMA structure & data 
	 * 
	 */
	private void createStructure() {
		java.sql.Connection con = null;
		java.sql.Statement stmt = null;
		
		try {
			Class.forName(this.getDriver());

			con = DriverManager.getConnection(this.getConnectionURL(),
					this.getUsername(), this.getPassword());
			
			// Create Schema
			this.updateSQL("DROP SCHEMA IF EXISTS \"" + getTapSchemaJDBCName() + "\"");
			this.updateSQL("CREATE SCHEMA \"" + getTapSchemaJDBCName() + "\"");

			ScriptRunner runner = new ScriptRunner(con, false, false);

			ServletContext context = this.servletContext;
			InputStream is = context.getResourceAsStream(this.tapSchemaScript);
			InputStreamReader r = new InputStreamReader(is);

			runner.runScript(r , getTapSchemaJDBCName());

		} catch (IOException | SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} finally {

			if (con != null)
				try {
					con.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}

		}
	}

	/**
	 * Insert resource metadata into the TAP_SCHEMA schema
	 * 
	 */
	public void insertMetadataMSSQL() {
		java.sql.Statement stmt = null;
		java.sql.Connection con = null;
		
		try {
			stmt = null;
			Class.forName(this.getDriver());
			con = DriverManager.getConnection(this.getConnectionURL(),
					this.getUsername(), this.getPassword());

			System.out.println("Inserting resource records into the table...");
			stmt = con.createStatement();

			for (AdqlSchema schema : this.resource.schemas().select()) {

				String schemaName = schema.name().replace("'", "''");

				String schemaDescription = schema.text();
				String sql;
				if (schemaDescription==null){
					sql = "INSERT INTO \"" + this.tapSchemaJDBCName +  "\".\"schemas\" VALUES ('"
							+ schemaName + "', NULL, NULL);";
				} else {
					sql = "INSERT INTO \"" + this.tapSchemaJDBCName +  "\".\"schemas\" VALUES ('"
							+ schemaName + "', '" + schemaDescription.replace("'", "''")
							+ "', NULL);";
				}
				
				stmt.executeUpdate(sql);
				
				for (AdqlTable table : schema.tables().select()) {
					String tableName = table.name().replace("'", "''");
					String tableDescription = table.text();
					if (tableDescription==null){
						sql = "INSERT INTO \"" + this.tapSchemaJDBCName +  "\".\"tables\" VALUES ('"
								+ schemaName + "', '" + schemaName + "." + tableName + "', 'table', NULL, '');";
					} else {
						sql = "INSERT INTO \"" + this.tapSchemaJDBCName +  "\".\"tables\" VALUES ('"
								+ schemaName + "', '" + schemaName + "." + tableName + "', 'table', '"
								+ tableDescription.replace("'", "''") + "', '');";
					}

					stmt.executeUpdate(sql);

					for (AdqlColumn column : table.columns().select()) {
						sql = "INSERT INTO \"" + this.tapSchemaJDBCName +  "\".\"columns\" VALUES (";
						String columnName = column.name().replace("'", "''");
                        if (columnName.toLowerCase().equals("timestamp") || columnName.toLowerCase().equals("coord2") || columnName.toLowerCase().equals("coord1")){  
                        	columnName = '"' + column.name() + '"';
                        }
						String columnDescription = column.text();
						sql += "'" +  schemaName + "." + tableName + "',";
						sql += "'" + columnName + "',";
						if (columnDescription==null){
							sql += "NULL, ";
						} else {
							sql += "'" + columnDescription.replace("'", "''") + "',";
						}

						AdqlColumn.Metadata meta = column.meta();

						if ((meta != null) && (meta.adql() != null)) {

							if (meta.adql().units() != null) {
								sql += "'" + meta.adql().units().replace("'", "''") + "',";
							} else {
								sql += "'',";
							}

							if (meta.adql().ucd() != null) {
								sql += "'" + meta.adql().ucd().replace("'", "''") + "',";
							} else {
								sql += "'',";
							}

							if (meta.adql().utype() != null) {
								sql += "'" + meta.adql().utype().replace("'", "''") + "',";
							} else {
								sql += "'',";
							}

							if (meta.adql().type() != null) {
								
								String votableType = meta.adql().type().votype().toString().replace("'", "''");
								String arraysize = "*";
								
								if (column.meta().adql().type() == AdqlColumn.AdqlType.DATE)
								    {
								    votableType = "char";
								    arraysize = "*";
								    }
								if (column.meta().adql().type() == AdqlColumn.AdqlType.TIME)
								    {
								    votableType = "char";
								    arraysize = "*";
								    }
								if (column.meta().adql().type() == AdqlColumn.AdqlType.DATETIME)
								    {
								    votableType = "char";
								    arraysize = "*";
								    }
								else if (column.meta().adql().type() == AdqlColumn.AdqlType.INTEGER) 
								    {	
									votableType = "int";
								    arraysize = "1";
								    }
						       
								if (votableType != null) {
									sql += "'" + votableType + "',";
								} else {
									sql += "'',";
								}

								if ((meta.adql().arraysize() != null)
										&& (meta.adql().arraysize() != 0)) {
									if (meta.adql().arraysize() == -1) {
										sql += "null,";
									} else {
										sql += "'"+meta.adql().arraysize()
												.toString().replace("'", "''")
												+ "',";
									}
								} else {
									sql += "null,";
								}
							}
						}

						sql += " 0, '',''";
						sql += ")";
						stmt.executeUpdate(sql);

					}
				}
			}

			// End insert
			System.out.println("Inserted records into the table...");

		} catch (SQLException se) {
			// Handle errors for JDBC
			log.debug("SQLException: ", se);
		} catch (Exception e) {
			log.debug("Exception: ", e);

		} finally {

			try {
				if (stmt != null)
					con.close();
			} catch (SQLException se) {
			}

			try {
				if (con != null)
					con.close();
			} catch (SQLException se) {
				se.printStackTrace();
			}// end finally
		}

	}
	

	/**
	 * Insert resource metadata into the TAP_SCHEMA schema
	 * 
	 */
	public void insertMetadata() {
		java.sql.Statement stmt = null;
		java.sql.Connection con = null;
		
		try {
			stmt = null;
			Class.forName(this.getDriver());
			con = DriverManager.getConnection(this.getConnectionURL(),
					this.getUsername(), this.getPassword());

			System.out.println("Inserting resource records into the table...");
			stmt = con.createStatement();

			for (AdqlSchema schema : this.resource.schemas().select()) {

				String schemaName = schema.name().replace("'", "''");

				String schemaDescription = schema.text();
				String sql;
				if (schemaDescription==null){
					sql = "INSERT INTO \"" + this.tapSchemaJDBCName +  "\".schemas VALUES ('"
							+ schemaName + "', NULL, NULL);";
				} else {
					sql = "INSERT INTO \"" + this.tapSchemaJDBCName +  "\".schemas VALUES ('"
							+ schemaName + "', '" + schemaDescription.replace("'", "''")
							+ "', NULL);";
				}
				
				stmt.executeUpdate(sql);
				
				for (AdqlTable table : schema.tables().select()) {
					String tableName = table.name().replace("'", "''");
					String tableDescription = table.text();
					if (tableDescription==null){
						sql = "INSERT INTO \"" + this.tapSchemaJDBCName +  "\".tables VALUES ('"
								+ schemaName + "', '" + schemaName + "." + tableName + "', 'table', NULL, '');";
					} else {
						sql = "INSERT INTO \"" + this.tapSchemaJDBCName  +  "\".tables VALUES ('"
								+ schemaName + "', '" + schemaName + "." + tableName + "', 'table', '"
								+ tableDescription.replace("'", "''") + "', '');";
					}

					stmt.executeUpdate(sql);

					for (AdqlColumn column : table.columns().select()) {
						sql = "INSERT INTO \"" + this.tapSchemaJDBCName +  "\".columns VALUES (";
						String columnName = column.name().replace("'", "''");
                        if (columnName.toLowerCase().equals("timestamp") || columnName.toLowerCase().equals("coord2") || columnName.toLowerCase().equals("coord1")){  
                        	columnName = '"' + column.name() + '"';
                        }
						String columnDescription = column.text();
						sql += "'" +  schemaName + "." + tableName + "',";
						sql += "'" + columnName + "',";
						if (columnDescription==null){
							sql += "NULL, ";
						} else {
							sql += "'" + columnDescription.replace("'", "''") + "',";
						}

						AdqlColumn.Metadata meta = column.meta();

						if ((meta != null) && (meta.adql() != null)) {

							if (meta.adql().units() != null) {
								sql += "'" + meta.adql().units().replace("'", "''") + "',";
							} else {
								sql += "'',";
							}

							if (meta.adql().ucd() != null) {
								sql += "'" + meta.adql().ucd().replace("'", "''") + "',";
							} else {
								sql += "'',";
							}

							if (meta.adql().utype() != null) {
								sql += "'" + meta.adql().utype().replace("'", "''") + "',";
							} else {
								sql += "'',";
							}

							if (meta.adql().type() != null) {
								
								String votableType = meta.adql().type().votype().toString().replace("'", "''");
								String arraysize = "*";
								
								if (column.meta().adql().type() == AdqlColumn.AdqlType.DATE)
								    {
								    votableType = "char";
								    arraysize = "*";
								    }
								if (column.meta().adql().type() == AdqlColumn.AdqlType.TIME)
								    {
								    votableType = "char";
								    arraysize = "*";
								    }
								if (column.meta().adql().type() == AdqlColumn.AdqlType.DATETIME)
								    {
								    votableType = "char";
								    arraysize = "*";
								    }
								else if (column.meta().adql().type() == AdqlColumn.AdqlType.INTEGER) 
								    {	
									votableType = "int";
								    arraysize = "1";
								    }
						       
								if (votableType != null) {
									sql += "'" + votableType + "',";
								} else {
									sql += "'',";
								}

								if ((meta.adql().arraysize() != null)
										&& (meta.adql().arraysize() != 0)) {
									if (meta.adql().arraysize() == -1) {
										sql += "null,";
									} else {
										sql += "'"+meta.adql().arraysize()
												.toString().replace("'", "''")
												+ "',";
									}
								} else {
									sql += "null,";
								}
							}
						}

						sql += " 0, null, null";
						sql += ")";
						stmt.executeUpdate(sql);

					}
				}
			}

			// End insert
			System.out.println("Inserted records into the table...");

		} catch (SQLException se) {
			// Handle errors for JDBC
			log.debug("SQLException: ", se);
		} catch (Exception e) {
			log.debug("Exception: ", e);

		} finally {

			try {
				if (stmt != null)
					con.close();
			} catch (SQLException se) {
			}

			try {
				if (con != null)
					con.close();
			} catch (SQLException se) {
				se.printStackTrace();
			}// end finally
		}

	}
	
	/**
	 * Create the TAP_SCHEMA 
	 * @throws ProtectionException 
	 * 
	 */
	public void createTapSchema()
    throws ProtectionException
	    {
		this.createStructure();
		this.insertMetadata();
		AdqlResource resource = this.resource;
		
		JdbcResource tap_schema_resource = this.factories.jdbc().resources().entities().create(
            this.tapSchemaResourceJDBCName,
            this.getTypeAsJdbcProductType(),
            this.getDatabase(),
            this.getCatalog(),
            this.getHost(),
            Integer.parseInt(this.getPort()),
            this.getUsername(),
            this.getPassword()
	        );

		JdbcSchema tap_schema;
		try {
			tap_schema = tap_schema_resource.schemas().select(this.getCatalog()  + "." + this.tapSchemaJDBCName);
			resource.schemas().create("TAP_SCHEMA", tap_schema);
		} catch (NameNotFoundException e) {
			System.out.println(e);
		}
	}



}