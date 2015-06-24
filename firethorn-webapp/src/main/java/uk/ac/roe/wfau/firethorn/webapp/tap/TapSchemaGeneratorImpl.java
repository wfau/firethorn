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

	private JDBCParams params;

	private ServletContext servletContext;
	
	private static String structureSqlFile = "WEB-INF/data/mysql_tapschema.sql";

	public TapSchemaGeneratorImpl() {
		super();
	}
	
	public TapSchemaGeneratorImpl(JDBCParams params,ServletContext servletContext) {
		super();
		this.params = params;
		this.servletContext = servletContext;
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

	/**
	 * Return the sql file to initialise TAP_SCHEMA structure
	 * 
	 * @return structureSqlFile
	 */
	public String getStructureSqlFile() {
		return structureSqlFile;
	}

	
	/**
	 * Create the initial TAP_SCHEMA structure & data 
	 * 
	 */
	public void createStructure() {
		java.sql.Connection con = null;

		try {
			Class.forName(this.params.getDriver());

			con = DriverManager.getConnection(this.params.getConnectionURL(),
					this.params.getUsername(), this.params.getPassword());

			ScriptRunner runner = new ScriptRunner(con, false, false);

			runner.runScript(new BufferedReader(new FileReader(this.servletContext
					.getRealPath("WEB-INF/data/mysql_tapschema.sql"))));

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
	 * @param resource
	 */
	public void insertMetadata(AdqlResource resource) {
		java.sql.Statement stmt = null;
		java.sql.Connection con = null;
		
		try {
			stmt = null;
			Class.forName(this.params.getDriver());
			con = DriverManager.getConnection(this.params.getConnectionURL(),
					this.params.getUsername(), this.params.getPassword());

			System.out.println("Inserting resource records into the table...");
			stmt = con.createStatement();

			for (AdqlSchema schema : resource.schemas().select()) {

				String schemaName = schema.name();
				String schemaDescription = schema.text();

				String sql = "INSERT INTO `TAP_SCHEMA`.`schemas` VALUES ('"
						+ schemaName + "', '" + schemaDescription
						+ "', NULL, NULL);";
				stmt.executeUpdate(sql);

				for (AdqlTable table : schema.tables().select()) {

					String tableName = table.name();
					String tableDescription = table.text();

					sql = "INSERT INTO `TAP_SCHEMA`.`tables` VALUES ('"
							+ schemaName + "', '" + tableName + "', 'table', '"
							+ tableDescription + "', NULL, NULL);";
					stmt.executeUpdate(sql);

					for (AdqlColumn column : table.columns().select()) {
						sql = "INSERT INTO `TAP_SCHEMA`.`columns` VALUES (";
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
								sql += "NULL,";
							}

							if (meta.adql().ucd() != null) {
								sql += "'" + meta.adql().ucd() + "',";
							} else {
								sql += "NULL,";
							}

							if (meta.adql().utype() != null) {
								sql += "'" + meta.adql().utype() + "',";
							} else {
								sql += "NULL,";
							}

							if (meta.adql().type() != null) {
								if (meta.adql().type() != null) {
									sql += "'" + meta.adql().type() + "',";
								} else {
									sql += "NULL,";
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

						sql += "0, 0, NULL,NULL";
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

}