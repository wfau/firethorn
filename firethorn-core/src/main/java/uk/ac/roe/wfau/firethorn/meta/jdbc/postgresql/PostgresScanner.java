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
package uk.ac.roe.wfau.firethorn.meta.jdbc.postgresql;

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
import uk.ac.roe.wfau.firethorn.meta.jdbc.JdbcConnectionEntity.MetadataException;
import uk.ac.roe.wfau.firethorn.meta.jdbc.JdbcConnection;
import uk.ac.roe.wfau.firethorn.meta.jdbc.JdbcMetadataScanner;

/**
 * Metadata scanner for a Postgres database.
 *
 */
@Slf4j
public class PostgresScanner
implements JdbcMetadataScanner
    {
    public interface PostgresObject
        {
        public Long oid();
        }

    interface PostgresSchema
    extends JdbcMetadataScanner.Schema, PostgresObject 
        {
        }
    
    interface PostgresTable
    extends JdbcMetadataScanner.Table, PostgresObject
        {
        }
    
    interface PostgresColumn
    extends JdbcMetadataScanner.Column, PostgresObject
        {
        }
    
    interface PostgresType
    extends PostgresObject
        {
        public String name();
        }

    public PostgresScanner(final JdbcConnection connector)
        {
        this.connector = connector ;
        }

    
    protected JdbcConnection connector ;
    @Override
    public JdbcConnection connector()
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
                final String match = connector().catalog().toLowerCase();
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
                final String match = connector().catalog().toLowerCase();
                if (name.toLowerCase().trim().equals(match))
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
                return PostgresScanner.this; 
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
                        final Statement statement = connection().createStatement();
                        final ResultSet results = statement.executeQuery(
                            " SELECT" +
                            "    oid," +
                            "    nspname" +
                            " FROM" +
                            "    pg_namespace" +
                            " WHERE" +
                            "    nspname NOT LIKE 'pg_%'" +
                            " AND" +
                            "    nspname <> 'information_schema'"
                            );
                        final List<Schema> list = new ArrayList<Schema>();
                        while (results.next())
                            {
                            list.add(
                                schema(
                                    catalog(),
                                    results.getLong(
                                        "oid"
                                        ),
                                    results.getString(
                                        "nspname"
                                        )
                                    )
                                );
                            }
                        return list ;
                        }

                    @Override
                    public PostgresSchema select(String name) throws SQLException
                        {
                        log.debug("Selecting schema [{}] for [{}]", name, catalog().name());
                        final PreparedStatement statement = connection().prepareStatement(
                            " SELECT" +
                            "    oid," +
                            "    nspname" +
                            " FROM" +
                            "    pg_namespace" +
                            " WHERE" +
                            "    nspname = ?"
                            );
                        statement.setString(1, name);
                        final ResultSet results = statement.executeQuery();
                        if (results.next())
                            {
                            return schema(
                                catalog(),
                                results.getLong(
                                    "oid"
                                    ),
                                results.getString(
                                    "nspname"
                                    )
                                );
                            }
                        else {
                            return null;
                            }
                        }
                    };
                }

            protected PostgresSchema schema(final Catalog catalog, final Long oid, final String name)
            throws SQLException
                {
                log.debug("schema() [{}][{}][{}]", catalog, oid, name);
                return new PostgresSchema()
                    {
                    @Override
                    public Long oid()
                        {
                        return oid;
                        }
                    @Override
                    public Catalog catalog()
                        {
                        return catalog ;
                        }
                    protected PostgresSchema schema()
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
                                    "    oid," +
                                    "    relname" +
                                    " FROM" +
                                    "    pg_class" +
                                    " WHERE" +
                                    "    relkind IN ('r','v')" +
                                    "AND" +
                                    "    relnamespace = ?"
                                    );
                                statement.setLong(
                                    1,
                                    schema().oid()
                                    );
                                final ResultSet results = statement.executeQuery();
                                final List<Table> list = new ArrayList<Table>();
                                while (results.next())
                                    {
                                    list.add(
                                        table(
                                            schema(),
                                            results.getLong(
                                                "oid"
                                                ),
                                            results.getString(
                                                "relname"
                                                )
                                            )
                                        );
                                    }
                                return list ;
                                }

                            @Override
                            public PostgresTable select(String name)
                                throws SQLException
                                {
                                final PreparedStatement statement = connection().prepareStatement(
                                    " SELECT" +
                                    "    oid," +
                                    "    relname" +
                                    " FROM" +
                                    "    pg_class" +
                                    " WHERE" +
                                    "    relkind IN ('r','v')" +
                                    "AND" +
                                    "    relnamespace = ?" +
                                    "AND" +
                                    "    relname = ?"
                                    );
                                statement.setLong(
                                    1,
                                    schema().oid()
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
                                        results.getLong(
                                            "oid"
                                            ),
                                        results.getString(
                                            "relname"
                                            )
                                        );
                                    }
                                else {
                                    return null ;
                                    }
                                }
                            };
                        }

                    protected PostgresTable table(final Schema schema, final Long oid, final String name)
                    throws SQLException
                        {
                        log.debug("table() [{}][{}][{}]", schema.name(), oid, name);
                        return new PostgresTable()
                            {
                            @Override
                            public Long oid()
                                {
                                return oid;
                                }
                            @Override
                            public Schema schema()
                                {
                                return schema;
                                }
                            protected PostgresTable table()
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
                                            "    attname," + 
                                            "    atttypmod," + 
                                            "    atttypid," + 
                                            "    typname" +
                                            " FROM" + 
                                            "    pg_attribute" + 
                                            " JOIN" + 
                                            "    pg_type" + 
                                            " ON" + 
                                            "    pg_attribute.atttypid = pg_type.oid" + 
                                            " WHERE" + 
                                            "    atttypid != 0" + 
                                            " AND" + 
                                            "    attrelid = ?" + 
                                            ""
                                            );
                                        statement.setLong(
                                            1,
                                            table().oid()
                                            );
                                        final ResultSet results = statement.executeQuery();
                                        final List<Column> list = new ArrayList<Column>();
                                        while (results.next())
                                            {
                                            list.add(
                                                column(
                                                    table(),
                                                    pgtype(
                                                        results.getLong(
                                                            "atttypid"
                                                            ),
                                                        results.getString(
                                                            "typname"
                                                            )
                                                        ),
                                                    results.getString(
                                                        "attname"
                                                        ),
                                                    results.getInt(
                                                        "atttypmod"
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
                                            "    attname," + 
                                            "    atttypmod," + 
                                            "    atttypid," + 
                                            "    typname" + 
                                            " FROM" + 
                                            "    pg_attribute" + 
                                            " JOIN" + 
                                            "    pg_type" + 
                                            " ON" + 
                                            "    pg_attribute.atttypid = pg_type.oid" + 
                                            " WHERE" + 
                                            "    atttypid != 0" + 
                                            " AND" + 
                                            "    attrelid = ?" + 
                                            " AND" + 
                                            "    attname = ?" + 
                                            ""
                                            );
                                        statement.setLong(
                                            1,
                                            table().oid()
                                            );
                                        statement.setString(
                                            2,
                                            name
                                            );
                                        final ResultSet results = statement.executeQuery();
                                        if (results.next())
                                            {
                                            return column(
                                                table(),
                                                pgtype(
                                                    results.getLong(
                                                        "atttypid"
                                                        ),
                                                    results.getString(
                                                        "typname"
                                                        )
                                                    ),
                                                results.getString(
                                                    "attname"
                                                    ),
                                                results.getInt(
                                                    "atttypmod"
                                                    )
                                                );
                                            }
                                        else {
                                            return null;
                                            }
                                        }
                                    };
                                }

                            protected PostgresColumn column(final PostgresTable table, final JdbcColumn.JdbcType type, final String name, final Integer len)
                            throws SQLException
                                {
                                log.debug("column() [{}][{}][{}]", name, type, len);
                                return new PostgresColumn()
                                    {
                                    @Override
                                    public PostgresTable table()
                                        {
                                        return table ;
                                        }
                                    @Override
                                    public Long oid()
                                        {
                                        return null;
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
                                        PostgresScanner.this.handle(ouch);
                                        }
                                    };
                                }
                            @Override
                            public void handle(SQLException ouch)
                                {
                                PostgresScanner.this.handle(ouch);
                                }
                            };
                        }
                    @Override
                    public void handle(SQLException ouch)
                        {
                        PostgresScanner.this.handle(ouch);
                        }
                    };
                }
            @Override
            public void handle(SQLException ouch)
                {
                PostgresScanner.this.handle(ouch);
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

        typemap.put("oid",       JdbcColumn.JdbcType.INTEGER); 
        typemap.put("tid",       JdbcColumn.JdbcType.INTEGER); 
        typemap.put("xid",       JdbcColumn.JdbcType.INTEGER); 
        typemap.put("cid",       JdbcColumn.JdbcType.INTEGER); 
    
        typemap.put("smallint",  JdbcColumn.JdbcType.SMALLINT); 
        typemap.put("int",       JdbcColumn.JdbcType.INTEGER); 
        typemap.put("int4",      JdbcColumn.JdbcType.INTEGER); 
        typemap.put("integer",   JdbcColumn.JdbcType.INTEGER); 
        typemap.put("bigint",    JdbcColumn.JdbcType.BIGINT); 

        typemap.put("serial",    JdbcColumn.JdbcType.INTEGER); 
        typemap.put("bigserial", JdbcColumn.JdbcType.BIGINT); 

        typemap.put("real",      JdbcColumn.JdbcType.REAL); 
        typemap.put("float",     JdbcColumn.JdbcType.FLOAT); 
        typemap.put("double",    JdbcColumn.JdbcType.DOUBLE); 
        typemap.put("double precision", JdbcColumn.JdbcType.DOUBLE); 
        
        typemap.put("datetime",  JdbcColumn.JdbcType.DATETIME); 

        typemap.put("char",      JdbcColumn.JdbcType.CHAR); 
        typemap.put("nchar",     JdbcColumn.JdbcType.NCHAR); 
        typemap.put("varchar",   JdbcColumn.JdbcType.VARCHAR); 
        typemap.put("nvarchar",  JdbcColumn.JdbcType.NVARCHAR); 

        typemap.put("binary",    JdbcColumn.JdbcType.BINARY); 
        typemap.put("varbinary", JdbcColumn.JdbcType.VARBINARY); 

        typemap.put("image",     JdbcColumn.JdbcType.VARBINARY); 
        typemap.put("text",      JdbcColumn.JdbcType.VARCHAR);
        typemap.put("ntext",     JdbcColumn.JdbcType.NVARCHAR); 

        };

    protected static JdbcColumn.JdbcType pgtype(final Long oid, final String name)
        {
        log.debug("type [{}][{}]", oid, name);
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
