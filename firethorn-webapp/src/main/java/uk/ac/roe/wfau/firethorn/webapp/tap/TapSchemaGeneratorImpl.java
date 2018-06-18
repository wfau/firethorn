package uk.ac.roe.wfau.firethorn.webapp.tap;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import javax.servlet.ServletContext;

import lombok.extern.slf4j.Slf4j;
import uk.ac.roe.wfau.firethorn.access.ProtectionException;
import uk.ac.roe.wfau.firethorn.entity.exception.NameNotFoundException;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlColumn;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlResource;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlSchema;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlTable;
import uk.ac.roe.wfau.firethorn.meta.jdbc.JdbcColumn;
import uk.ac.roe.wfau.firethorn.meta.jdbc.JdbcResource;
import uk.ac.roe.wfau.firethorn.meta.jdbc.JdbcSchema;
import uk.ac.roe.wfau.firethorn.meta.jdbc.JdbcTable;
import uk.ac.roe.wfau.firethorn.spring.ComponentFactories;

/**
 * Generate TAP_SCHEMA of a resource
 * 
 * @author stelios
 * 
 */
@Slf4j
public class TapSchemaGeneratorImpl implements TapSchemaGenerator {

	/**
	 * AdqlResource for which to create the TAP_SCHEMA.
	 * 
	 */
	private AdqlResource adqlresource;

	/**
	 * AdqlSchema for the TAP_SCHEMA.
	 * 
	 */
	private AdqlSchema adqlschema;

	/**
	 * JdbcResource for the TAP_SCHEMA.
	 * 
	 */
	private JdbcResource jdbcresource;

	/**
	 * JdbcSchema for the TAP_SCHEMA.
	 * 
	 */
	private JdbcSchema jdbcschema;

	/**
	 * Tap Schema Properties
	 */
	protected TapSchemaProperties properties;

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
	 * ComponentFactories instance.
	 *
	 */

	private ComponentFactories factories;

	/**
	 * Our system services.
	 *
	 */
	public ComponentFactories factories() {
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
	public TapSchemaGeneratorImpl(ServletContext servletContext, ComponentFactories factories, AdqlResource resource,
			TapSchemaProperties properties) {
		super();
		this.servletContext = servletContext;
		this.adqlresource = resource;
		this.tapSchemaScript = "/WEB-INF/data/" + "pgsql_tap_schema";
		this.factories = factories;
		this.tapSchemaJDBCName = "TAP_SCHEMA_" + this.adqlresource.ident().toString();
		this.tapSchemaResourceJDBCName = "TAP_RESOURCE_" + this.adqlresource.ident().toString();
		this.properties = properties;

		if (this.properties.getType().toLowerCase().equals("pgsql")) {
			this.tapSchemaScript = "/WEB-INF/data/" + "pgsql_tap_schema.sql";
		} else if (this.properties.getType().toLowerCase().equals("mssql")) {
			this.tapSchemaScript = "/WEB-INF/data/" + "sqlserver_tap_schema.sql";
		}

	}

	public TapSchemaProperties getProperties() {
		return properties;
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
	 * 
	 * @param sql
	 */
	private void updateSQL(String sql) {
		java.sql.Statement stmt = null;
		java.sql.Connection con = null;

		try {
			stmt = null;
			Class.forName(this.properties.getDriver());
			con = DriverManager.getConnection(this.properties.getConnectionURL(), this.properties.getUsername(),
					this.properties.getPassword());

			log.debug("Inserting resource records into the table...");
			stmt = con.createStatement();
			stmt.executeUpdate(sql);
		} catch (SQLException e) {
			log.debug("Exception: ", e);
		} catch (Exception e) {
			log.debug("Exception: ", e);
		} finally {

			if (con != null)
				try {
					con.close();
				} catch (SQLException e) {
					log.debug("Exception: ", e);
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
			Class.forName(this.properties.getDriver());

			con = DriverManager.getConnection(this.properties.getConnectionURL(), this.properties.getUsername(),
					this.properties.getPassword());

			// Create Schema
			this.updateSQL("DROP SCHEMA IF EXISTS \"" + getTapSchemaJDBCName() + "\"");
			this.updateSQL("CREATE SCHEMA \"" + getTapSchemaJDBCName() + "\"");

			ScriptRunner runner = new ScriptRunner(con, false, false);

			ServletContext context = this.servletContext;
			InputStream is = context.getResourceAsStream(this.tapSchemaScript);
			InputStreamReader r = new InputStreamReader(is);

			runner.runScript(r, getTapSchemaJDBCName());

		} catch (IOException | SQLException e) {
			log.debug("Exception: ", e);
		} catch (Exception e) {
			log.debug("Exception: ", e);
		} finally {

			if (con != null)
				try {
					con.close();
				} catch (SQLException e) {
					log.error("Exception: ", e);
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
			Class.forName(this.properties.getDriver());
			con = DriverManager.getConnection(this.properties.getConnectionURL(), this.properties.getUsername(),
					this.properties.getPassword());

			System.out.println("Inserting resource records into the table...");
			stmt = con.createStatement();

			for (AdqlSchema schema : this.adqlresource.schemas().select()) {

				String schemaName = schema.name().replace("'", "''");

				String schemaDescription = schema.text();
				String sql;
				if (schemaDescription == null) {
					sql = "INSERT INTO \"" + this.tapSchemaJDBCName
							+ "\".\"schemas\" (\"schema_name\",\"description\",\"utype\",\"ft_schema_id\") VALUES ('"
							+ schemaName + "', NULL, NULL," + schema.ident().toString() + ");";
				} else {
					sql = "INSERT INTO \"" + this.tapSchemaJDBCName
							+ "\".\"schemas\" (\"schema_name\",\"description\",\"utype\",\"ft_schema_id\") VALUES ('"
							+ schemaName + "', '" + schemaDescription.replace("'", "''") + "', NULL,"
							+ schema.ident().toString() + ");";
				}

				stmt.executeUpdate(sql);

				for (AdqlTable table : schema.tables().select()) {
					String tableName = table.name().replace("'", "''");
					String tableDescription = table.text();
					if (tableDescription == null) {
						sql = "INSERT INTO \"" + this.tapSchemaJDBCName
								+ "\".\"tables\"  (\"schema_name\", \"table_name\", \"table_type\", \"description\", \"utype\", \"ft_table_id\") VALUES ('"
								+ schemaName + "', '" + schemaName + "." + tableName + "', 'table', NULL, '',"
								+ table.ident().toString() + ");";
					} else {
						sql = "INSERT INTO \"" + this.tapSchemaJDBCName
								+ "\".\"tables\"  (\"schema_name\", \"table_name\", \"table_type\", \"description\", \"utype\", \"ft_table_id\") VALUES ('"
								+ schemaName + "', '" + schemaName + "." + tableName + "', 'table', '"
								+ tableDescription.replace("'", "''") + "', ''," + table.ident().toString() + ");";
					}

					stmt.executeUpdate(sql);

					for (AdqlColumn column : table.columns().select()) {
						sql = "INSERT INTO \"" + this.tapSchemaJDBCName
								+ "\".\"columns\" (\"table_name\", \"column_name\", \"description\", \"unit\", \"ucd\", \"utype\", \"datatype\", \"size\", \"arraysize\", \"principal\", \"indexed\", \"std\", \"ft_column_id\") VALUES (";
						String columnName = column.name().replace("'", "''");
						if (columnName.toLowerCase().equals("timestamp") 
                                                                || columnName.toLowerCase().equals("coord2")
								|| columnName.toLowerCase().equals("coord1") 
								|| columnName.toLowerCase().equals("size") 
                                                                || columnName.toLowerCase().equals("min")
								|| columnName.toLowerCase().equals("min")
                                                                || columnName.toLowerCase().equals("max")
								|| columnName.toLowerCase().equals("match")
								|| columnName.toLowerCase().equals("zone")
								|| columnName.toLowerCase().equals("time")
								|| columnName.toLowerCase().equals("distance")
								|| columnName.toLowerCase().equals("value")
								|| columnName.toLowerCase().equals("sql")
								|| columnName.toLowerCase().equals("first")
								|| columnName.toLowerCase().equals("MATCH")
								|| columnName.toLowerCase().equals("DATE")
								|| columnName.toLowerCase().equals("area")
								|| columnName.toLowerCase().equals("key")
								|| columnName.toLowerCase().equals("count")
								|| columnName.toLowerCase().equals("when")
                                                                || columnName.toLowerCase().equals("date") ) {
							columnName = '"' + column.name() + '"';
						}
						String columnDescription = column.text();
						sql += "'" + schemaName + "." + tableName + "',";
						sql += "'" + columnName + "',";
						if (columnDescription == null) {
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

								String votableType = meta.adql().type().name().toString().replace("'", "''");
								String arraysize = "*";

								if (column.meta().adql().type() == AdqlColumn.AdqlType.DATE) {
									votableType = "char";
									arraysize = "*";
								}
								if (column.meta().adql().type() == AdqlColumn.AdqlType.TIME) {
									votableType = "char";
									arraysize = "*";
								}
								if (column.meta().adql().type() == AdqlColumn.AdqlType.DATETIME) {
									votableType = "char";
									arraysize = "*";
								} else if (column.meta().adql().type() == AdqlColumn.AdqlType.INTEGER) {
									votableType = "int";
									arraysize = "1";
								}

								if (votableType != null) {
									sql += "'" + votableType.toLowerCase() + "',";
								} else {
									sql += "'',";
								}

								if ((meta.adql().arraysize() != null) && (meta.adql().arraysize() != 0)) {
									if (meta.adql().arraysize() == -1) {
										sql += "null, null,";
									} else {
										sql += "'" + meta.adql().arraysize().toString().replace("'", "''") + "',";

										sql += "'" + meta.adql().arraysize().toString().replace("'", "''") + "',";
									}
								} else {
									sql += "null, null,";
								}
							}
						}

						sql += " 0, 0,''";
						sql += ")";
						stmt.executeUpdate(sql);

					}
				}
			}

			// End insert
			System.out.println("Inserted records into the table...");

		} catch (SQLException e) {
			// Handle errors for JDBC
			log.error("Exception: ", e);
		} catch (Exception e) {
			log.error("Exception: ", e);
		} finally {

			try {
				if (stmt != null)
					con.close();
			} catch (SQLException e) {
				log.error("Exception: ", e);
			}

			try {
				if (con != null)
					con.close();
			} catch (SQLException e) {
				log.error("Exception: ", e);
			} // end finally
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
			Class.forName(this.properties.getDriver());
			con = DriverManager.getConnection(this.properties.getConnectionURL(), this.properties.getUsername(),
					this.properties.getPassword());

			System.out.println("Inserting resource records into the table...");
			stmt = con.createStatement();

			for (AdqlSchema schema : this.adqlresource.schemas().select()) {

				String schemaName = schema.name().replace("'", "''");

				String schemaDescription = schema.text();
				String sql;
				if (schemaDescription == null) {
					sql = "INSERT INTO \"" + this.tapSchemaJDBCName
							+ "\".schemas (\"schema_name\",\"description\",\"utype\",\"ft_schema_id\") VALUES ('"
							+ schemaName + "', NULL, NULL, 0);";
				} else {
					sql = "INSERT INTO \"" + this.tapSchemaJDBCName
							+ "\".schemas (\"schema_name\",\"description\",\"utype\",\"ft_schema_id\") 	VALUES ('"
							+ schemaName + "', '" + schemaDescription.replace("'", "''") + "', NULL, 0);";
				}
				
				stmt.executeUpdate(sql);

				for (AdqlTable table : schema.tables().select()) {
					String tableName = table.name().replace("'", "''");
					String tableDescription = table.text();
					if (tableDescription == null) {
						sql = "INSERT INTO \"" + this.tapSchemaJDBCName
								+ "\".tables (\"schema_name\", \"table_name\", \"table_type\", \"description\", \"utype\", \"ft_table_id\") VALUES ('"
								+ schemaName + "', '" + schemaName + "." + tableName + "', 'table', NULL, '', 0);";
					} else {
						sql = "INSERT INTO \"" + this.tapSchemaJDBCName
								+ "\".tables (\"schema_name\", \"table_name\", \"table_type\", \"description\", \"utype\", \"ft_table_id\") VALUES ('"
								+ schemaName + "', '" + schemaName + "." + tableName + "', 'table', '"
								+ tableDescription.replace("'", "''") + "', '', 0);";
					}

					stmt.executeUpdate(sql);

					for (AdqlColumn column : table.columns().select()) {
						sql = "INSERT INTO \"" + this.tapSchemaJDBCName
								+ "\".columns (\"table_name\", \"column_name\", \"description\", \"unit\", \"ucd\", \"utype\", \"datatype\", \"size\", \"arraysize\", \"principal\", \"indexed\", \"std\", \"ft_column_id\") VALUES (";
						String columnName = column.name().replace("'", "''");
						if (columnName.toLowerCase().equals("timestamp") 
                                                                || columnName.toLowerCase().equals("coord2")
								|| columnName.toLowerCase().equals("coord1") 
								|| columnName.toLowerCase().equals("size") 
                                                                || columnName.toLowerCase().equals("min")
								|| columnName.toLowerCase().equals("min")
                                                                || columnName.toLowerCase().equals("max")
								|| columnName.toLowerCase().equals("match")
								|| columnName.toLowerCase().equals("zone")
								|| columnName.toLowerCase().equals("time")
								|| columnName.toLowerCase().equals("distance")
								|| columnName.toLowerCase().equals("value")
								|| columnName.toLowerCase().equals("sql")
								|| columnName.toLowerCase().equals("first")
								|| columnName.toLowerCase().equals("MATCH")
								|| columnName.toLowerCase().equals("DATE")
								|| columnName.toLowerCase().equals("area")
								|| columnName.toLowerCase().equals("key")
								|| columnName.toLowerCase().equals("count")
								|| columnName.toLowerCase().equals("when")
                                                                || columnName.toLowerCase().equals("date") ) {
							columnName = '"' + column.name() + '"';
						}
						String columnDescription = column.text();
						sql += "'" + schemaName + "." + tableName + "',";
						sql += "'" + columnName + "',";
						if (columnDescription == null) {
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

								String votableType = meta.adql().type().name().toString().replace("'", "''").toLowerCase();
								String arraysize = "*";

								if (column.meta().adql().type() == AdqlColumn.AdqlType.DATE) {
									votableType = "char";
									arraysize = "*";
								}
								if (column.meta().adql().type() == AdqlColumn.AdqlType.TIME) {
									votableType = "char";
									arraysize = "*";
								}
								if (column.meta().adql().type() == AdqlColumn.AdqlType.DATETIME) {
									votableType = "char";
									arraysize = "*";
								} 
								if (column.meta().adql().type() == AdqlColumn.AdqlType.INTEGER) {
									votableType = "int";
									arraysize = "1";
								}
								if (column.meta().adql().type() == AdqlColumn.AdqlType.BYTE) {
									votableType = "unsignedByte";
									arraysize = "*";
								}

								if (votableType != null) {
									sql += "'" + votableType + "',";
								} else {
									sql += "'',";
								}

								if ((meta.adql().arraysize() != null) && (meta.adql().arraysize() != 0)) {
									if (meta.adql().arraysize() == -1) {
										sql += "null, null,";
									} else {
										sql += "'" + meta.adql().arraysize().toString().replace("'", "''") + "',";

										sql += "'" + meta.adql().arraysize().toString().replace("'", "''") + "',";
									}
								} else {
									sql += "null, null,";
								}
							}
						}

						sql += " 0, 0, 0, ";
						sql += column.ident().toString();
						sql += ")";
						stmt.executeUpdate(sql);

					}
				}
			}

			// End insert
			System.out.println("Inserted records into the table...");

		} catch (SQLException e) {
			// Handle errors for JDBC
			log.error("Exception: ", e);
		} catch (Exception e) {
			log.error("Exception: ", e);
		} finally {

			try {
				if (stmt != null)
					con.close();
			} catch (SQLException e) {
				log.error("Exception: ", e);
			}

			try {
				if (con != null)
					con.close();
			} catch (SQLException e) {
				log.error("Exception: ", e);
			} // end finally
		}

	}

	/**
	 * Create the JDBC TAP_SCHEMA.
	 * 
	 * @throws ProtectionException
	 * @throws NameNotFoundException
	 * 
	 */
	public JdbcSchema createTapSchemaJdbc() throws ProtectionException, NameNotFoundException {
		this.createStructure();
		this.insertMetadata();

		jdbcresource = this.factories.jdbc().resources().entities().create(this.tapSchemaResourceJDBCName,
				this.properties.getTypeAsJdbcProductType(), this.properties.getDatabase(), this.properties.getCatalog(),
				this.properties.getHost(), Integer.parseInt(this.properties.getPort()), this.properties.getUsername(),
				this.properties.getPassword());

		jdbcschema = jdbcresource.schemas().select(this.properties.getCatalog() + "." + this.tapSchemaJDBCName);
		return jdbcschema;
	}

	/**
	 * List of system tables to ignore.
	 * 
	 */
	protected static final List<String> systables = Arrays.asList("tableoid", "xmin", "xmax", "cmin", "cmax", "ctid");

	/**
	 * Create the ADQL TAP_SCHEMA.
	 * 
	 * @throws ProtectionException
	 * 
	 */
	public AdqlSchema createTapSchemaAdql() throws ProtectionException {
		adqlschema = adqlresource.schemas().create(AdqlSchema.CopyDepth.PARTIAL, "TAP_SCHEMA", jdbcschema);

		for (JdbcTable jdbctable : jdbcschema.tables().select()) {
			log.debug("Processing JdbcTable [{}][{}]", jdbctable.ident(), jdbctable.name());

			AdqlTable adqltable = adqlschema.tables().create(AdqlSchema.CopyDepth.PARTIAL, jdbctable.base(),
					jdbctable.name());
			log.debug("Created AdqlTable [{}][{}]", adqltable.ident(), adqltable.name());

			for (JdbcColumn jdbccolumn : jdbctable.columns().select()) {
				log.debug("Processing JdbcColumn [{}][{}]", jdbccolumn.ident(), jdbccolumn.name());
				if (systables.contains(jdbccolumn.name().toLowerCase())) {
					log.debug("Skipping system column [{}][{}]", jdbccolumn.ident(), jdbccolumn.name());
				} else {
					AdqlColumn adqlcolumn = adqltable.columns().create(jdbccolumn.base(), jdbccolumn.name());
					log.debug("Created AdqlColumn [{}][{}]", adqlcolumn.ident(), adqlcolumn.name());
				}
			}
		}

		return adqlschema;
	}

}
