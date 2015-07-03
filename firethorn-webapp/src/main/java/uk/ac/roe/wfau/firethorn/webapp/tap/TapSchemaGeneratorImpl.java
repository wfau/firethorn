package uk.ac.roe.wfau.firethorn.webapp.tap;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.servlet.ServletContext;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import uk.ac.roe.wfau.firethorn.meta.adql.AdqlColumn;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlResource;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlSchema;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlTable;

/**
 * Generate TAP_SCHEMA of a resource
 * 
 * @author stelios
 * 
 */

public class TapSchemaGeneratorImpl implements TapSchemaGenerator{

	@Value("${firethorn.webapp.endpoint:null}")
	private String baseurl;
	
	/**
	 * Adql resource for which to create the TAP_SCHEMA
	 */
	
	private AdqlResource resource;
	
	/**
	 * JDBC parameters (user, pass, url, driver)
	 */
	private JDBCParams params;

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
	 * Empty Constructor
	 */
	public TapSchemaGeneratorImpl() {
		super();
	}
	
	/**
	 * Constructor
	 */
	public TapSchemaGeneratorImpl(JDBCParams params,ServletContext servletContext, AdqlResource resource, String tapSchemaScript) {
		super();
		this.params = params;
		this.servletContext = servletContext;
		this.resource = resource;
		this.tapSchemaScript = tapSchemaScript;
		setTapSchemaJDBCName("TAP_SCHEMA_" + this.resource.ident().toString());
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
	
	/**
	 * Run the 'sql' update SQL command
	 * @param sql
	 */
	private void updateSQL(String sql) {
		java.sql.Statement stmt = null;
		java.sql.Connection con = null;
		
		try {
			stmt = null;
			Class.forName(this.params.getDriver());
			con = DriverManager.getConnection(this.params.getConnectionURL(),
					this.params.getUsername(), this.params.getPassword());

			System.out.println("Inserting resource records into the table...");
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
			Class.forName(this.params.getDriver());

			con = DriverManager.getConnection(this.params.getConnectionURL(),
					this.params.getUsername(), this.params.getPassword());
			
			// Create Schema
			this.updateSQL("CREATE SCHEMA \"" + getTapSchemaJDBCName() +  "\"");

			ScriptRunner runner = new ScriptRunner(con, false, false);

			runner.runScript(new BufferedReader(new FileReader(this.servletContext
					.getRealPath(this.tapSchemaScript))), getTapSchemaJDBCName());

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
	public void insertMetadata() {
		java.sql.Statement stmt = null;
		java.sql.Connection con = null;
		
		try {
			stmt = null;
			Class.forName(this.params.getDriver());
			con = DriverManager.getConnection(this.params.getConnectionURL(),
					this.params.getUsername(), this.params.getPassword());

			System.out.println("Inserting resource records into the table...");
			stmt = con.createStatement();

			for (AdqlSchema schema : this.resource.schemas().select()) {

				String schemaName = schema.name();
				String schemaDescription = schema.text();

				String sql = "INSERT INTO \"" + this.tapSchemaJDBCName +  "\".\"schemas\" VALUES ('"
						+ schemaName + "', '" + schemaDescription
						+ "', '', '');";
				stmt.executeUpdate(sql);

				for (AdqlTable table : schema.tables().select()) {

					String tableName = table.name();
					String tableDescription = table.text();

					sql = "INSERT INTO \"" + this.tapSchemaJDBCName +  "\".\"tables\" VALUES ('"
							+ schemaName + "', '" + tableName + "', 'table', '"
							+ tableDescription + "', NULL, NULL);";
					stmt.executeUpdate(sql);

					for (AdqlColumn column : table.columns().select()) {
						sql = "INSERT INTO \"" + this.tapSchemaJDBCName +  "\".\"columns\" VALUES (";
						String columnName = column.name();
						String columnDescription = column.text();
						sql += "'" + tableName + "',";
						sql += "'" + columnName + "',";
						sql += "'" + columnDescription + "',";

						AdqlColumn.Metadata meta = column.meta();

						if ((meta != null) && (meta.adql() != null)) {

							if (meta.adql().units() != null) {
								sql += "'" + meta.adql().units() + "',";
							} else {
								sql += "'',";
							}

							if (meta.adql().ucd() != null) {
								sql += "'" + meta.adql().ucd() + "',";
							} else {
								sql += "'',";
							}

							if (meta.adql().utype() != null) {
								sql += "'" + meta.adql().utype() + "',";
							} else {
								sql += "'',";
							}

							if (meta.adql().type() != null) {
								if (meta.adql().type() != null) {
									sql += "'" + meta.adql().type() + "',";
								} else {
									sql += "'',";
								}

								if ((meta.adql().arraysize() != null)
										&& (meta.adql().arraysize() != 0)) {
									if (meta.adql().arraysize() == -1) {
										sql += "-1,";
									} else {
										sql += meta.adql().arraysize()
												.toString()
												+ ",";
									}
								} else {
									sql += "-1,";
								}
							}
						}

						sql += "0, 0, '',''";
						sql += ")";
						stmt.executeUpdate(sql);

					}
				}
			}

			// End insert
			System.out.println("Inserted records into the table...");

		} catch (SQLException se) {
			// Handle errors for JDBC
			se.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
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
	 */
	public void createTapSchema() {
		this.createStructure();
		this.insertMetadata();
		
	}



}