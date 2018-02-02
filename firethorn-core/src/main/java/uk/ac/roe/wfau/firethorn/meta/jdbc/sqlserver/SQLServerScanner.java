/*
 *  Copyright (C) 2014 Royal Observatory, University of Edinburgh, UK
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package uk.ac.roe.wfau.firethorn.meta.jdbc.sqlserver;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;
import uk.ac.roe.wfau.firethorn.meta.jdbc.JdbcColumn;
import uk.ac.roe.wfau.firethorn.meta.jdbc.JdbcColumn.JdbcType;
import uk.ac.roe.wfau.firethorn.meta.jdbc.JdbcConnector;
import uk.ac.roe.wfau.firethorn.meta.jdbc.JdbcMetadataScanner;
import uk.ac.roe.wfau.firethorn.util.ResultSetFilterator;
import uk.ac.roe.wfau.firethorn.util.ResultSetIterator;

/**
 * Database metadata scanner for SQLServer.
 *
 */
@Slf4j
public class SQLServerScanner
    implements JdbcMetadataScanner
    {
    /**
     * Public constructor.
     * 
     */
    public SQLServerScanner(final JdbcConnector connector)
        {
        this.connector = connector ;
        }

    protected JdbcConnector connector ;
    @Override
    public JdbcConnector connector()
        {
        return this.connector;
        }

    protected Connection connection ;
    protected Connection connection()
        {
        if (connection == null)
            {
            log.debug("Openning a new connection");
            connection = connector.open();
            }
        return connection;
        }

    @Override
    public Catalogs catalogs()
        {
        return new Catalogs()
            {
            @Override
            public Iterable<Catalog> select()
            throws SQLException
                {
                // Bug fix.
                // A fatal error accessing a broken catalog can cause the database driver to close the connection, breaking the ResultSet in the process.
                // To mitigate the side effects we transfer the list of catalogs to a local List.
                final List<Catalog> list = new ArrayList<Catalog>();
                final ResultSet rset = connection().getMetaData().getCatalogs();
                final Iterable<Catalog> iter = new ResultSetIterator.Factory<Catalog>(rset)
                    {
                    @Override
                    protected Catalog getNext() throws SQLException
                        {
                        return catalog(
                            rset.getString(
                                "TABLE_CAT"
                                )
                            );
                        }
                    };
                for (Catalog catalog : iter)
                    {
                    list.add(
                        catalog
                        );
                    }
                return list ;
                }

            @Override
            public Catalog select(final String name)
            throws SQLException
                {
                Iterator<Catalog> catalogs = select().iterator();
                while (catalogs.hasNext())
                    {
                    Catalog catalog = catalogs.next(); 
                    if (catalog.name().toUpperCase().equals(name.trim().toUpperCase()))
                        {
                        return catalog;
                        }
                    }
                return null ;
                }
            };
        }

    protected Catalog catalog(final String name)
    throws SQLException
        {
        return new Catalog()
            {
            @Override
            public JdbcMetadataScanner scanner()
                {
                return SQLServerScanner.this; 
                }
            protected Catalog catalog()
                {
                return this ;
                }

            @Override
            public String name()
                {
                return name;
                }
            @Override
            public Schemas schemas()
                {
                log.debug("schemas() for [{}]", catalog().name());
                return new Schemas()
                    {
                    @Override
                    public Iterable<Schema> select() throws SQLException
                        {
                        log.debug("schemas().select() for [{}]", catalog().name());
                        // http://msdn.microsoft.com/en-us/library/aa933205%28v=sql.80%29.aspx
                    	// http://msdn.microsoft.com/en-GB/library/ms182642.aspx
                        final PreparedStatement statement = connection().prepareStatement(
                            (
                    		"SELECT DISTINCT" +
                            "  [SCHEMA_NAME]" +
                            " FROM" +
                            "  [{catalog}].[INFORMATION_SCHEMA].[SCHEMATA]"
                            ).replace(
                                "{catalog}",
                                catalog().name()
                                )
                            );
                        log.debug("Statement [{}]", statement.toString());
                        return new ResultSetFilterator.Factory<Schema>(
                            statement.executeQuery()
                            ){
                            /**
                             * Get the next {@link Schema}, skipping reserved names.
                             *  
                             */
                            @Override
                            protected Schema getNext() throws SQLException
                                {
                                //
                                // Check for reserved names.
                                final String schemaname = results().getString(
                                    "SCHEMA_NAME"
                                    );
                                log.debug("Found schema [{}]", schemaname);
                                if (connector().type().ignore().contains(schemaname))
                                    {
                                    log.debug(" Ignoring schema [{}]", schemaname);
                                    return null ;
                                    }
                                else {
                                    log.debug(" Returning schema [{}]", schemaname);
                                    return schema(
                                        catalog(),
                                        schemaname
                                        );
                                    }
                                }
                            };
                        }

                    @Override
                    public Schema select(String name) throws SQLException
                        {
                        log.debug("schemas().select(String) for [{}][{}]", catalog().name(), name);
                        // http://msdn.microsoft.com/en-us/library/aa933205%28v=sql.80%29.aspx
                    	// http://msdn.microsoft.com/en-GB/library/ms182642.aspx
                        final PreparedStatement statement = connection().prepareStatement(
                    		(
                    		"SELECT DISTINCT" +
                    		"  [SCHEMA_NAME]" +
                    		" FROM" +
                    		"  [{catalog}].[INFORMATION_SCHEMA].[SCHEMATA]" +
                    		" WHERE " +
                        	"  [SCHEMA_NAME] = ?"
                    		).replace(
                                "{catalog}",
                                catalog().name()
                                )
                            );
                        statement.setString(
                			1,
                			name
                			);
                        
                        final ResultSet results = statement.executeQuery();
                        if (results.next())
                            {
							//
							// Check for reserved names.
							final String schemaname = results.getString(
								"SCHEMA_NAME"
								);
							log.debug("Found schema [{}]", schemaname);
							if (connector().type().ignore().contains(schemaname))
								{
								log.debug(" Ignoring schema [{}]", schemaname);
								return null ;
								}
							else {
	                        	return schema(
	                                catalog(),
	                                schemaname
	                                );
                            	}
                            }
                        else {
                            return null;
                            }
                        }
                    };
                }

            protected Schema schema(final Catalog catalog, final String name)
            throws SQLException
                {
                return new Schema()
                    {
                    @Override
                    public Catalog catalog()
                        {
                        return catalog ;
                        }
                    protected Schema schema()
                        {
                        return this ;
                        }

                    @Override
                    public String name()
                        {
                        return name;
                        }
                    @Override
                    public Tables tables()
                        {
                        log.debug("tables() for [{}][{}]", catalog().name(), schema().name());
                        return new Tables()
                            {
                            @Override
                            public Iterable<Table> select()
                                throws SQLException
                                {
                                log.debug("tables().select() for [{}][{}]", catalog().name(), schema().name());
                                // http://msdn.microsoft.com/en-us/library/aa933205%28v=sql.80%29.aspx
                                final PreparedStatement statement = connection().prepareStatement(
                                    (
                            		"SELECT DISTINCT" +
                                    "  [TABLE_NAME]" +
                                    " FROM " +
                                    "  [{catalog}].[INFORMATION_SCHEMA].[TABLES]" +
                                    " WHERE" +
                                    "  [TABLE_SCHEMA] = ?"
                                    ).replace(
                                        "{catalog}",
                                        catalog().name()
                                        )
                                    );
                                statement.setString(1, schema().name());
                                return new ResultSetIterator.Factory<Table>(statement.executeQuery())
                                    {
                                    @Override
                                    protected Table getNext()
                                        throws SQLException
                                        {
                                        return table(
                                            schema(),
                                            results().getString(
                                                "TABLE_NAME"
                                                )
                                            );
                                        }
                                    };
                                }

                            @Override
                            public Table select(String name)
                                throws SQLException
                                {
                                log.debug("tables().select(String) for [{}][{}][{}]", catalog().name(), schema().name(), name);
                                // http://msdn.microsoft.com/en-us/library/aa933205%28v=sql.80%29.aspx
                                final PreparedStatement statement = connection().prepareStatement(
                                    (
                            		"SELECT DISTINCT" +
                                    "  [TABLE_NAME] " +
                                    " FROM" +
                                    "  [{catalog}].[INFORMATION_SCHEMA].[TABLES]" +
                                    " WHERE" +
                                    "  [TABLE_SCHEMA] = ?" +
                                    " AND" +
                                    "  [TABLE_NAME] = ?"
                                    ).replace(
                                        "{catalog}",
                                        catalog().name()
                                        )
                                    );
                                statement.setString(
                            		1,
                            		schema().name()
                            		);
                                statement.setString(
                            		2,
                            		name
                            		);

                                final ResultSet results = statement.executeQuery();
                                if (results.next())
                                    {
                                    return table(
                                        schema(),
                                        results.getString(
                                    		"TABLE_NAME"
                                    		)
                                        );
                                    }
                                else {
                                    return null ;
                                    }
                                }
                            };
                        }

                    protected Table table(final Schema schema, final String name)
                    throws SQLException
                        {
                        return new Table()
                            {
                            @Override
                            public Schema schema()
                                {
                                return schema;
                                }
                            protected Table table()
                                {
                                return this ;
                                }

                            @Override
                            public String name()
                                {
                                return name ;
                                }
                            @Override
                            public Columns columns()
                                {
                                log.debug("columns() for [{}][{}][{}]", catalog().name(), schema().name(), table().name());
                                return new Columns()
                                    {
                                    @Override
                                    public Iterable<Column> select()
                                        throws SQLException
                                        {
                                        log.debug("columns().select() for [{}][{}][{}]", catalog().name(), schema().name(), table().name());
                                        // http://msdn.microsoft.com/en-us/library/aa933218%28v=sql.80%29.aspx
                                        final PreparedStatement statement = connection().prepareStatement(
                                            (
                                    		"SELECT DISTINCT" +
                                            "  [COLUMN_NAME]," +
                                            "  [DATA_TYPE]," +
                                            "  [NUMERIC_PRECISION]," +
                                            "  [CHARACTER_MAXIMUM_LENGTH]" +
                                            " FROM" +
                                            "  [{catalog}].[INFORMATION_SCHEMA].[COLUMNS]" +
                                            " WHERE" +
                                            "  [TABLE_SCHEMA] = ?" +
                                            " AND" +
                                            "  [TABLE_NAME] = ?"
                                            ).replace(
                                                "{catalog}",
                                                catalog().name()
                                                )
                                            );
                                        statement.setString(
                                            1,
                                            schema().name()
                                            );
                                        statement.setString(
                                            2,
                                            table().name()
                                            );

                                        return new ResultSetIterator.Factory<Column>(
                                            statement.executeQuery()
                                            ){
                                            @Override
                                            protected Column getNext()
                                                throws SQLException
                                                {
                                                return column(
                                                    table(),
                                                    results()
                                                    );
                                                }
                                            };
                                        }

                                    @Override
                                    public Column select(String name)
                                        throws SQLException
                                        {
                                        log.debug("columns().select(String) for [{}][{}][{}][{}]", catalog().name(), schema().name(), table().name(), name);
                                        // http://msdn.microsoft.com/en-us/library/aa933218%28v=sql.80%29.aspx
                                        final PreparedStatement statement = connection().prepareStatement(
                                    		(
                                            "SELECT DISTINCT" +
                                            "  [COLUMN_NAME]," +
                                            "  [DATA_TYPE]," +
                                            "  [NUMERIC_PRECISION]," +
                                            "  [CHARACTER_MAXIMUM_LENGTH]" +
                                            " FROM" +
                                            "  [{catalog}].[INFORMATION_SCHEMA].[COLUMNS]" +
                                            " WHERE " +
                                            "  [TABLE_SCHEMA] = ?" +
                                            " AND" +
                                            "  [TABLE_NAME] = ?" +
                                            " AND" +
                                            "  [COLUMN_NAME] = ?"
                                            ).replace(
                                                "{catalog}",
                                                catalog().name()
                                                )
                                            );
                                        statement.setString(
                                            1,
                                            schema().name()
                                            );
                                        statement.setString(
                                            2,
                                            table().name()
                                            );
                                        statement.setString(
                                            3,
                                            name
                                            );

                                        final ResultSet results = statement.executeQuery();
                                        if (results.next())
                                            {
                                            return column(
                                                table(),
                                                results
                                                );
                                            }
                                        else {
                                            return null;
                                            }
                                        }
                                    };
                                }
                            protected Column column(final Table table, final ResultSet results)
                            throws SQLException
                                {
                                final String  name = results.getString("COLUMN_NAME");
                                final Integer strlen = results.getInt("CHARACTER_MAXIMUM_LENGTH");
                                final JdbcColumn.JdbcType type = SQLServerScanner.type(
                                    results.getInt(
                                        "NUMERIC_PRECISION"
                                        ),
                                    results.getString(
                                        "DATA_TYPE"
                                        )
                                    );
                                log.trace("column() [{}][{}][{}]", name, strlen, type);
                                return new Column()
                                    {
                                    @Override
                                    public Table table()
                                        {
                                        return table ;
                                        }
/*
 * 
                                    protected String  name = results.getString("COLUMN_NAME");
                                    protected Integer size = results.getInt("CHARACTER_MAXIMUM_LENGTH");
                                    protected JdbcColumn.JdbcType type = MSSQLMetadataScanner.type(
                                        results.getString(
                                            "DATA_TYPE"
                                            )
                                        );
 * 
 */
                                    @Override
                                    public String name()
                                        {
                                        return name;
                                        }
                                    @Override
                                    public Integer strlen()
                                        {
                                        return strlen;
                                        }
                                    @Override
                                    public JdbcColumn.JdbcType type()
                                        {
                                        return type;
                                        }
                                    @Override
                                    public void handle(SQLException ouch)
                                        {
                                        SQLServerScanner.this.handle(ouch);
                                        }
                                    };
                                }
                            @Override
                            public void handle(SQLException ouch)
                                {
                                SQLServerScanner.this.handle(ouch);
                                }
                            };
                        }
                    @Override
                    public void handle(SQLException ouch)
                        {
                        SQLServerScanner.this.handle(ouch);
                        }
                    };
                }
            @Override
            public void handle(SQLException ouch)
                {
                SQLServerScanner.this.handle(ouch);
                }
            };
        }

    @Override
    public void handle(SQLException ouch)
        {
        log.debug("SQLException [{}][{}][{}]", ouch.getErrorCode(), ouch.getSQLState(), ouch.getMessage());
        if ((ouch.getErrorCode() == 0) || (ouch.getErrorCode() == 21))
            {
            log.warn("Fatal error code, resetting connection");
            try {
                connector().reset();
                }
            catch (Exception eeek)
                {
                log.warn("Exception while resetting connection following SQLException [{}]", eeek.getMessage());
                }
            finally {
                connection = null ;
                }
            }
        }
    
    protected static Map<String, JdbcColumn.JdbcType> typemap = new HashMap<String, JdbcColumn.JdbcType>();
    static {
    
        typemap.put("bit",       JdbcColumn.JdbcType.BIT); 
        typemap.put("int",       JdbcColumn.JdbcType.INTEGER); 
        typemap.put("bigint",    JdbcColumn.JdbcType.BIGINT); 
        typemap.put("smallint",  JdbcColumn.JdbcType.SMALLINT); 
        typemap.put("tinyint",   JdbcColumn.JdbcType.TINYINT); 
        typemap.put("real",      JdbcColumn.JdbcType.REAL); 
        typemap.put("float",     JdbcColumn.JdbcType.FLOAT); 
        typemap.put("datetime",  JdbcColumn.JdbcType.DATETIME); 

        typemap.put("char",      JdbcColumn.JdbcType.CHAR); 
        typemap.put("nchar",     JdbcColumn.JdbcType.NCHAR); 
        typemap.put("varchar",   JdbcColumn.JdbcType.VARCHAR); 
        typemap.put("nvarchar",  JdbcColumn.JdbcType.NVARCHAR); 

        typemap.put("binary",    JdbcColumn.JdbcType.BINARY); 
        typemap.put("varbinary", JdbcColumn.JdbcType.VARBINARY); 

        //http://msdn.microsoft.com/en-GB/library/ms187993.aspx
        typemap.put("image",     JdbcColumn.JdbcType.VARBINARY); 
        typemap.put("text",      JdbcColumn.JdbcType.VARCHAR);
        typemap.put("ntext",     JdbcColumn.JdbcType.NVARCHAR); 

        //http://msdn.microsoft.com/en-us/library/ms187746.aspx
        typemap.put("numeric",          JdbcColumn.JdbcType.UNKNOWN); 
        //http://msdn.microsoft.com/en-us/library/ms173829.aspx
        typemap.put("sql_variant",      JdbcColumn.JdbcType.UNKNOWN); 
        //http://msdn.microsoft.com/en-us/library/ms187942.aspx
        typemap.put("uniqueidentifier", JdbcColumn.JdbcType.UNKNOWN); 

        };

    protected static JdbcColumn.JdbcType type(final Integer numlen, final String name)
        {
        log.trace("type [{}][{}]", numlen, name);
        JdbcColumn.JdbcType type = typemap.get(
            name
            );
        if (type != null)
            {
            //
            // Floating point numeric depends on precision.
            // http://msdn.microsoft.com/en-gb/library/ms173773.aspx
            if ((type == JdbcType.REAL) || (type == JdbcType.FLOAT))
                {
                if (numlen > 24)
                    {
                    type = JdbcType.DOUBLE ;
                    }
                else {
                    type = JdbcType.FLOAT ;
                    }
                }
            return type ;
            }
        else {
            log.warn("Type not found [{}]", name);
            return JdbcColumn.JdbcType.UNKNOWN;
            }
        }
    }
