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
package uk.ac.roe.wfau.firethorn.meta.jdbc.hsqldb;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;
import uk.ac.roe.wfau.firethorn.meta.jdbc.JdbcColumn;
import uk.ac.roe.wfau.firethorn.meta.jdbc.JdbcConnector;
import uk.ac.roe.wfau.firethorn.meta.jdbc.JdbcConnectionEntity.MetadataException;
import uk.ac.roe.wfau.firethorn.meta.jdbc.JdbcMetadataScanner;

/**
 * Metadata scanner for a HSQLDB database.
 *
 */
@Slf4j
public class HsqldbScanner
implements JdbcMetadataScanner
    {
    public HsqldbScanner(final JdbcConnector connector)
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
            throws SQLException, MetadataException
                {
                final String match = connector().catalog();
                final List<Catalog> list = new ArrayList<Catalog>();
                list.add(
                    catalog(
                        match
                        )
                    );
                return list ;
                }

            @Override
            public Catalog select(final String name)
            throws SQLException, MetadataException
                {
                final String match = connector().catalog();
                if (name.trim().equals(match))
                    {
                    return catalog(
                        match
                        );
                    }
                else {
                    return null ;
                    }
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
                return HsqldbScanner.this; 
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
                        log.debug("Selecting schema for [{}]", catalog().name());
                        final PreparedStatement statement = connection().prepareStatement(
                            " SELECT" +
                            "    CATALOG_NAME," +
                            "    SCHEMA_NAME" +
                            " FROM" +
                            "    INFORMATION_SCHEMA.SCHEMATA" +
                            " WHERE" +
                            "    CATALOG_NAME = ?"
                            );
                        statement.setString(
                            1,
                            catalog().name()
                            );
                        final ResultSet results = statement.executeQuery();
                        final List<Schema> list = new ArrayList<Schema>();
                        while (results.next())
                            {
                            list.add(
                                schema(
                                    catalog(),
                                    results.getString(
                                        "SCHEMA_NAME"
                                        )
                                    )
                                );
                            }
                        return list ;
                        }

                    @Override
                    public Schema select(String name) throws SQLException
                        {
                        log.debug("Selecting schema [{}] for [{}]", name, catalog().name());
                        final PreparedStatement statement = connection().prepareStatement(
                            " SELECT" +
                            "    CATALOG_NAME," +
                            "    SCHEMA_NAME" +
                            " FROM" +
                            "    INFORMATION_SCHEMA.SCHEMATA" +
                            " WHERE" +
                            "    CATALOG_NAME = ?" +
                            " AND" +
                            "    SCHEMA_NAME = ?"
                            );
                        statement.setString(
                            1,
                            catalog().name()
                            );
                        statement.setString(
                            2,
                            name
                            );
                        final ResultSet results = statement.executeQuery();
                        if (results.next())
                            {
                            return schema(
                                catalog(),
                                results.getString(
                                    "SCHEMA_NAME"
                                    )
                                );
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
                log.debug("schema() [{}][{}][{}]", catalog, name);
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
                        return new Tables()
                            {
                            @Override
                            public Iterable<Table> select()
                                throws SQLException
                                {
                                final PreparedStatement statement = connection().prepareStatement(
                                    " SELECT" +
                                    "    TABLE_CATALOG," +
                                    "    TABLE_SCHEMA," +
                                    "    TABLE_NAME," +
                                    "    TABLE_TYPE" +
                                    " FROM" +
                                    "    INFORMATION_SCHEMA.TABLES" +
                                    " WHERE" +
                                    "    TABLE_CATALOG = ?" +
                                    " AND" +
                                    "    TABLE_SCHEMA = ?"
                                    );
                                statement.setString(
                                    1,
                                    catalog().name()
                                    );
                                statement.setString(
                                    2,
                                    schema().name()
                                    );
                                final ResultSet results = statement.executeQuery();
                                final List<Table> list = new ArrayList<Table>();
                                while (results.next())
                                    {
                                    list.add(
                                        table(
                                            schema(),
                                            results.getString(
                                                "TABLE_NAME"
                                                )
                                            )
                                        );
                                    }
                                return list ;
                                }

                            @Override
                            public Table select(String name)
                                throws SQLException
                                {
                                final PreparedStatement statement = connection().prepareStatement(
                                    " SELECT" +
                                    "    TABLE_CATALOG," +
                                    "    TABLE_SCHEMA," +
                                    "    TABLE_NAME," +
                                    "    TABLE_TYPE" +
                                    " FROM" +
                                    "    INFORMATION_SCHEMA.TABLES" +
                                    " WHERE" +
                                    "    TABLE_CATALOG = ?" +
                                    " AND" +
                                    "    TABLE_SCHEMA = ?" +
                                    " AND" +
                                    "    TABLE_NAME = ?"
                                    );
                                statement.setString(
                                    1,
                                    catalog().name()
                                    );
                                statement.setString(
                                    2,
                                    schema().name()
                                    );
                                statement.setString(
                                    3,
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
                        log.debug("table() [{}][{}][{}]", schema.name(), name);
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
                                return new Columns()
                                    {
                                    @Override
                                    public Iterable<Column> select()
                                        throws SQLException
                                        {
                                        final PreparedStatement statement = connection().prepareStatement(
                                            " SELECT" +
                                            "    TABLE_CATALOG," +
                                            "    TABLE_SCHEMA," +
                                            "    TABLE_NAME," +
                                            "    DATA_TYPE," +
                                            "    COLUMN_NAME," +
                                            "    CHARACTER_MAXIMUM_LENGTH" +
                                            " FROM" +
                                            "    INFORMATION_SCHEMA.COLUMNS" +
                                            " WHERE" +
                                            "    TABLE_CATALOG = ?" +
                                            " AND" +
                                            "    TABLE_SCHEMA = ?" +
                                            " AND" +
                                            "    TABLE_NAME = ?"   
                                            );
                                        statement.setString(
                                            1,
                                            catalog.name()
                                            );
                                        statement.setString(
                                            2,
                                            schema().name()
                                            );
                                        statement.setString(
                                            3,
                                            table().name()
                                            );
                                        final ResultSet results = statement.executeQuery();
                                        final List<Column> list = new ArrayList<Column>();
                                        while (results.next())
                                            {
                                            list.add(
                                                column(
                                                    table(),
                                                    hstype(
                                                        results.getString(
                                                            "DATA_TYPE"
                                                            )
                                                        ),
                                                    results.getString(
                                                        "COLUMN_NAME"
                                                        ),
                                                    results.getInt(
                                                        "CHARACTER_MAXIMUM_LENGTH"
                                                        )
                                                    )
                                                );
                                            }
                                        return list ;
                                        }
                                    @Override
                                    public Column select(String name)
                                        throws SQLException
                                        {
                                        final PreparedStatement statement = connection().prepareStatement(
                                            " SELECT" +
                                            "    TABLE_CATALOG," +
                                            "    TABLE_SCHEMA," +
                                            "    TABLE_NAME," +
                                            "    DATA_TYPE," +
                                            "    COLUMN_NAME," +
                                            "    CHARACTER_MAXIMUM_LENGTH" +
                                            " FROM" +
                                            "    INFORMATION_SCHEMA.COLUMNS" +
                                            " WHERE" +
                                            "    TABLE_CATALOG = ?" +
                                            " AND" +
                                            "    TABLE_SCHEMA = ?" +
                                            " AND" +
                                            "    TABLE_NAME = ?" +   
                                            " AND" +
                                            "    COLUMN_NAME = ?"   
                                            );
                                        statement.setString(
                                            1,
                                            catalog.name()
                                            );
                                        statement.setString(
                                            2,
                                            schema().name()
                                            );
                                        statement.setString(
                                            3,
                                            table().name()
                                            );
                                        statement.setString(
                                            4,
                                            table().name()
                                            );
                                        final ResultSet results = statement.executeQuery();
                                        if (results.next())
                                            {
                                            return column(
                                                table(),
                                                hstype(
                                                    results.getString(
                                                        "DATA_TYPE"
                                                        )
                                                    ),
                                                results.getString(
                                                    "COLUMN_NAME"
                                                    ),
                                                results.getInt(
                                                    "CHARACTER_MAXIMUM_LENGTH"
                                                    )
                                                );
                                            }
                                        else {
                                            return null;
                                            }
                                        }
                                    };
                                }

                            protected Column column(final Table table, final JdbcColumn.JdbcType type, final String name, final Integer len)
                            throws SQLException
                                {
                                log.debug("column() [{}][{}][{}]", name, type, len);
                                return new Column()
                                    {
                                    @Override
                                    public Table table()
                                        {
                                        return table ;
                                        }
                                    @Override
                                    public String name()
                                        {
                                        return name;
                                        }
                                    @Override
                                    public Integer strlen()
                                        {
                                        return len;
                                        }
                                    @Override
                                    public JdbcColumn.JdbcType type()
                                        {
                                        return type;
                                        }
                                    @Override
                                    public void handle(SQLException ouch)
                                        {
                                        HsqldbScanner.this.handle(ouch);
                                        }
                                    };
                                }
                            @Override
                            public void handle(SQLException ouch)
                                {
                                HsqldbScanner.this.handle(ouch);
                                }
                            };
                        }
                    @Override
                    public void handle(SQLException ouch)
                        {
                        HsqldbScanner.this.handle(ouch);
                        }
                    };
                }
            @Override
            public void handle(SQLException ouch)
                {
                HsqldbScanner.this.handle(ouch);
                }
            };
        }

    @Override
    public void handle(SQLException ouch)
        {
        log.debug("SQLException [{}][{}][{}]", ouch.getErrorCode(), ouch.getSQLState(), ouch.getMessage());
        }

    /**
     * Internal map of database types to {@link JdbcColumn.JdbcType}.
     * 
     */
    protected static Map<String, JdbcColumn.JdbcType> typemap = new HashMap<String, JdbcColumn.JdbcType>();
    static {

        typemap.put("BOOLEAN",                  JdbcColumn.JdbcType.BOOLEAN); 

        typemap.put("BIT",                      JdbcColumn.JdbcType.BIT); 
        typemap.put("BIT VARYING",              JdbcColumn.JdbcType.BIT); 

        typemap.put("BIGINT",                   JdbcColumn.JdbcType.BIGINT); 
        typemap.put("SMALLINT",                 JdbcColumn.JdbcType.SMALLINT); 
        typemap.put("TINYINT",                  JdbcColumn.JdbcType.TINYINT); 
        typemap.put("INTEGER",                  JdbcColumn.JdbcType.INTEGER); 
        typemap.put("REAL",                     JdbcColumn.JdbcType.REAL); 
        typemap.put("FLOAT",                    JdbcColumn.JdbcType.DOUBLE); 
        typemap.put("DOUBLE",                   JdbcColumn.JdbcType.DOUBLE); 
        typemap.put("DOUBLE PRECISION",         JdbcColumn.JdbcType.DOUBLE); 

        typemap.put("NUMERIC",                  JdbcColumn.JdbcType.NUMERIC); 
        typemap.put("DECIMAL",                  JdbcColumn.JdbcType.DECIMAL); 

        typemap.put("DATE",                     JdbcColumn.JdbcType.DATE); 
        typemap.put("TIME",                     JdbcColumn.JdbcType.TIME); 
        typemap.put("DATETIME",                 JdbcColumn.JdbcType.DATETIME); 
        typemap.put("TIMESTAMP",                JdbcColumn.JdbcType.TIMESTAMP); 
        
        typemap.put("CHAR",                     JdbcColumn.JdbcType.CHAR); 
        typemap.put("CHARACTER",                JdbcColumn.JdbcType.CHAR); 
        typemap.put("VARCHAR",                  JdbcColumn.JdbcType.VARCHAR); 
        typemap.put("LONGVARCHAR",              JdbcColumn.JdbcType.VARCHAR); 
        typemap.put("CHARACTER VARYING",        JdbcColumn.JdbcType.VARCHAR); 
                
        typemap.put("CLOB",                     JdbcColumn.JdbcType.CLOB); 
        typemap.put("CHARACTER LARGE OBJECT",   JdbcColumn.JdbcType.CLOB); 

        typemap.put("BLOB",                     JdbcColumn.JdbcType.BLOB); 
        typemap.put("BINARY",                   JdbcColumn.JdbcType.BLOB); 
        typemap.put("VARBINARY",                JdbcColumn.JdbcType.BLOB); 
        typemap.put("BINARY VARYING",           JdbcColumn.JdbcType.BLOB); 

        };

    protected static JdbcColumn.JdbcType hstype(final String name)
        {
        log.debug("type [{}][{}]", name);
        JdbcColumn.JdbcType type = typemap.get(
            name
            );
        if (type != null)
            {
            return type ;
            }
        else {
            log.warn("Type not found [{}]", name);
            return JdbcColumn.JdbcType.UNKNOWN;
            }
        }
    }
