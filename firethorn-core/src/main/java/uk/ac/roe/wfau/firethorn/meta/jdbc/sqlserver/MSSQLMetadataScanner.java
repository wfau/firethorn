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
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;
import uk.ac.roe.wfau.firethorn.meta.jdbc.JdbcColumn;
import uk.ac.roe.wfau.firethorn.meta.jdbc.JdbcColumn.JdbcType;
import uk.ac.roe.wfau.firethorn.meta.jdbc.JdbcConnector;
import uk.ac.roe.wfau.firethorn.meta.jdbc.JdbcMetadataScanner;

/**
 *
 *
 */
@Slf4j
public class MSSQLMetadataScanner
    implements JdbcMetadataScanner
    {

    public MSSQLMetadataScanner(final JdbcConnector connector)
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
    public Connection connection()
        {
        if (connection == null)
            {
            log.debug("Openning a new connection");
            connection = connector.open();
            }
        return connection;
        }

    protected JdbcMetadataScanner _scanner()
        {
        return this;
        }

    @Override
    public void handle(SQLException ouch)
        {
        log.debug("SQLException [{}][{}][{}]", ouch.getErrorCode(), ouch.getSQLState(), ouch.getMessage());
        if ((ouch.getErrorCode() == 0) || (ouch.getErrorCode() == 21))
            {
            log.debug("Fatal error code, closing existing connection");
            try {
                connector().reset();
                }
            catch (Exception eeek)
                {
                //log.warn("SQLException while closing connection [{}][{}][{}]", eeek.getErrorCode(), eeek.getSQLState(), eeek.getMessage());
                log.warn("Exception while closing connection [{}]", eeek.getMessage());
                }
            finally {
                connection = null ;
                }
            }
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
                /*
                // https://stackoverflow.com/questions/147659/get-list-of-databases-from-sql-server
                final Statement statement = connection.createStatement();
                final ResultSet results = statement.executeQuery(
                    "SELECT name FROM master.dbo.sysdatabases WHERE dbid > 4"
                    );
                 */
                final ResultSet results = connection().getMetaData().getCatalogs();
                final List<Catalog> list = new ArrayList<Catalog>();
                while (results.next())
                    {
                    list.add(
                        catalog(
                            results
                            )
                        );
                    }
                return list ;
                }

            @Override
            public Catalog select(final String name)
            throws SQLException
                {
                /*
                final PreparedStatement statement = connection.prepareStatement(
                    "SELECT name FROM master.dbo.sysdatabases WHERE dbid > 4 AND name = ?"
                    );
                statement.setString(1, name);
                final ResultSet results = statement.executeQuery();
                */
                final ResultSet results = connection().getMetaData().getCatalogs();
                while (results.next())
                    {
                    Catalog catalog = catalog(
                        results
                        );
                    if (catalog.name().toUpperCase().equals(name.trim().toUpperCase()))
                        {
                        return catalog;
                        }
                    }
                return null ;
                }
            };
        }

    protected Catalog catalog(final ResultSet results)
    throws SQLException
        {
        return new Catalog()
            {
            @Override
            public JdbcMetadataScanner scanner()
                {
                //return _scanner();
                return MSSQLMetadataScanner.this; 
                }
            protected Catalog catalog()
                {
                return this ;
                }

            protected String name = results.getString("TABLE_CAT");
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
                        // http://msdn.microsoft.com/en-us/library/aa933205%28v=sql.80%29.aspx
                        final Statement statement = connection().createStatement();
                        final ResultSet results = statement.executeQuery(
                            "SELECT DISTINCT " +
                            "  TABLE_SCHEMA " +
                            "FROM " +
                            "  {catalog}.INFORMATION_SCHEMA.TABLES"
                            .replace(
                                "{catalog}",
                                catalog().name()
                                )
                            );
                        final List<Schema> list = new ArrayList<Schema>();
                        while (results.next())
                            {
                            list.add(
                                schema(
                                    catalog(),
                                    results
                                    )
                                );
                            }
                        return list ;
                        }

                    @Override
                    public Schema select(String name) throws SQLException
                        {
                        // http://msdn.microsoft.com/en-us/library/aa933205%28v=sql.80%29.aspx
                        final PreparedStatement statement = connection().prepareStatement(
                            "SELECT DISTINCT " +
                            "  TABLE_SCHEMA " +
                            "FROM " +
                            "  " + catalog().name() + ".INFORMATION_SCHEMA.TABLES " +
                    		"WHERE " +
                    		"  TABLE_SCHEMA = ?"
                            );
                        statement.setString(1, name);
                        final ResultSet results = statement.executeQuery();
                        if (results.next())
                            {
                            return schema(
                                catalog(),
                                results
                                );
                            }
                        else {
                            return null;
                            }
                        }
                    };
                }

            protected Schema schema(final Catalog catalog, final ResultSet results)
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

                    protected String name = results.getString("TABLE_SCHEMA");
                    @Override
                    public String name()
                        {
                        return name;
                        }
                    @Override
                    public Tables tables()
                        {
                        return new Tables()
                            {
                            @Override
                            public Iterable<Table> select()
                                throws SQLException
                                {
                                // http://msdn.microsoft.com/en-us/library/aa933205%28v=sql.80%29.aspx
                                final PreparedStatement statement = connection().prepareStatement(
                                    "SELECT DISTINCT " +
                                    "  TABLE_NAME " +
                                    "FROM " +
                                    "  " + catalog().name() + ".INFORMATION_SCHEMA.TABLES " +
                                    "WHERE " +
                                    "  TABLE_SCHEMA = ?"
                                    );
                                statement.setString(1, schema().name());
                                final ResultSet results = statement.executeQuery();
                                final List<Table> list = new ArrayList<Table>();
                                while (results.next())
                                    {
                                    list.add(
                                        table(
                                            schema(),
                                            results
                                            )
                                        );
                                    }
                                return list ;
                                }

                            @Override
                            public Table select(String name)
                                throws SQLException
                                {
                                // http://msdn.microsoft.com/en-us/library/aa933205%28v=sql.80%29.aspx
                                final PreparedStatement statement = connection().prepareStatement(
                                    "SELECT DISTINCT " +
                                    "  TABLE_NAME " +
                                    "FROM " +
                                    "  " + catalog().name() + ".INFORMATION_SCHEMA.TABLES " +
                                    "WHERE " +
                                    "  TABLE_SCHEMA = ? " +
                                    "AND " +
                                    " TABLE_NAME = ?"
                                    );
                                statement.setString(1, schema().name());
                                statement.setString(2, name);
                                final ResultSet results = statement.executeQuery();
                                if (results.next())
                                    {
                                    return table(
                                        schema(),
                                        results
                                        );
                                    }
                                else {
                                    return null ;
                                    }
                                }
                            };
                        }

                    protected Table table(final Schema schema, final ResultSet results)
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

                            final String name = results.getString("TABLE_NAME");
                            @Override
                            public String name()
                                {
                                return name ;
                                }
                            @Override
                            public Columns columns()
                                {
                                return new Columns()
                                    {
                                    @Override
                                    public Iterable<Column> select()
                                        throws SQLException
                                        {
                                        // http://msdn.microsoft.com/en-us/library/aa933218%28v=sql.80%29.aspx
                                        final PreparedStatement statement = connection().prepareStatement(
                                            "SELECT DISTINCT " +
                                            "  COLUMN_NAME, " +
                                            "  DATA_TYPE, " +
                                            "  NUMERIC_PRECISION, " +
                                            "  CHARACTER_MAXIMUM_LENGTH " +
                                            "FROM " +
                                            "  " + catalog().name() + ".INFORMATION_SCHEMA.COLUMNS " +
                                            "WHERE " +
                                            "  TABLE_SCHEMA = ? " +
                                            "AND " +
                                            "  TABLE_NAME = ?"
                                            );
                                        statement.setString(
                                            1,
                                            schema().name()
                                            );
                                        statement.setString(
                                            2,
                                            table().name()
                                            );
                                        final ResultSet results = statement.executeQuery();
                                        final List<Column> list = new ArrayList<Column>();
                                        while (results.next())
                                            {
                                            list.add(
                                                column(
                                                    table(),
                                                    results
                                                    )
                                                );
                                            }
                                        return list ;
                                        }

                                    @Override
                                    public Column select(String name)
                                        throws SQLException
                                        {
                                        // http://msdn.microsoft.com/en-us/library/aa933218%28v=sql.80%29.aspx
                                        final PreparedStatement statement = connection().prepareStatement(
                                            "SELECT DISTINCT " +
                                            "  COLUMN_NAME, " +
                                            "  DATA_TYPE, " +
                                            "  NUMERIC_PRECISION, " +
                                            "  CHARACTER_MAXIMUM_LENGTH " +
                                            "FROM " +
                                            "  " + catalog().name() + ".INFORMATION_SCHEMA.COLUMNS " +
                                            "WHERE " +
                                            "  TABLE_SCHEMA = ? " +
                                            "AND " +
                                            "  TABLE_NAME = ?" +
                                            "AND " +
                                            "  COLUMN_NAME = ?"
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
                                final JdbcColumn.JdbcType type = MSSQLMetadataScanner.type(
                                    results.getInt(
                                        "NUMERIC_PRECISION"
                                        ),
                                    results.getString(
                                        "DATA_TYPE"
                                        )
                                    );
                                log.debug("column() [{}][{}][{}]", name, strlen, type);
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
                                    };
                                }
                            };
                        }
                    };
                }
            };
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
        log.debug("type [{}][{}]", numlen, name);
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
