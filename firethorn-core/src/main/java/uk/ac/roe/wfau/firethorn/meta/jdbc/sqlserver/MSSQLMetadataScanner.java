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
import java.util.Iterator;

import lombok.extern.slf4j.Slf4j;

import uk.ac.roe.wfau.firethorn.meta.jdbc.JdbcMetadataScanner;
import uk.ac.roe.wfau.firethorn.util.ResultSetIterator;

/**
 *
 *
 */
@Slf4j
public class MSSQLMetadataScanner
    implements JdbcMetadataScanner
    {

    public MSSQLMetadataScanner(final Connection connection)
        {
        this.connection = connection ;
        }

    protected Connection connection ;

    @Override
    public Catalogs catalogs()
        {
        return new Catalogs()
            {
            @Override
            public Iterable<Catalog> select()
            throws SQLException
                {
                // https://stackoverflow.com/questions/147659/get-list-of-databases-from-sql-server
                final Statement statement = connection.createStatement();
                final ResultSet results = statement.executeQuery(
                    "SELECT name FROM master.dbo.sysdatabases WHERE dbid > 4"
                    );
                return new Iterable<Catalog>()
                    {
                    @Override
                    public Iterator<Catalog> iterator()
                        {
                        return new ResultSetIterator<Catalog>(results)
                            {
                            @Override
                            protected Catalog getNext(final ResultSet results)
                            throws SQLException
                                {
                                return catalog(
                                    results
                                    );
                                }
                            };
                        }
                    };
                }

            @Override
            public Catalog select(final String name)
            throws SQLException
                {
                final PreparedStatement statement = connection.prepareStatement(
                    "SELECT name FROM master.dbo.sysdatabases WHERE dbid > 4 AND name = ?"
                    );
                statement.setString(1, name);
                final ResultSet results = statement.executeQuery();
                if (results.next())
                    {
                    return catalog(
                        results
                        );
                    }
                else {
                    return null ;
                    }
                }
            };
        }

    protected Catalog catalog(final ResultSet results)
    throws SQLException
        {
        return new Catalog()
            {
            protected String name = results.getString("name");
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
            public Schemas schema()
                {
                return new Schemas()
                    {
                    @Override
                    public Iterable<Schema> select() throws SQLException
                        {
                        final Statement statement = connection.createStatement();
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
                        return new Iterable<Schema>()
                            {
                            @Override
                            public Iterator<Schema> iterator()
                                {
                                return new ResultSetIterator<Schema>(results)
                                    {
                                    @Override
                                    protected Schema getNext(final ResultSet results)
                                    throws SQLException
                                        {
                                        return schema(
                                            results
                                            );
                                        }
                                    };
                                }
                            };
                        }

                    @Override
                    public Schema select(String name) throws SQLException
                        {
                        // TODO Auto-generated method stub
                        return null;
                        }
                    };
                }

            protected Schema schema(final ResultSet results)
            throws SQLException
                {
                return new Schema()
                    {
                    protected String name = results.getString("TABLE_SCHEMA");
                    protected Schema schema()
                        {
                        return this ;
                        }
                    @Override
                    public Catalog parent()
                        {
                        return catalog();
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
                                final PreparedStatement statement = connection.prepareStatement(
                                    "SELECT DISTINCT " +
                                    "  TABLE_NAME " +
                                    "FROM " +
                                    "  " + catalog().name() + ".INFORMATION_SCHEMA.TABLES " +
                                    "WHERE " +
                                    "  TABLE_SCHEMA = ?"
                                    );
                                statement.setString(1, schema().name());
                                final ResultSet results = statement.executeQuery();
                                return new Iterable<Table>()
                                    {
                                    @Override
                                    public Iterator<Table> iterator()
                                        {
                                        return new ResultSetIterator<Table>(results)
                                            {
                                            @Override
                                            protected Table getNext(final ResultSet results)
                                            throws SQLException
                                                {
                                                return table(
                                                    results
                                                    );
                                                }
                                            };
                                        }
                                    };
                                }

                            @Override
                            public Table select(String name)
                                throws SQLException
                                {
                                // TODO Auto-generated method stub
                                return null;
                                }
                            };
                        }

                    protected Table table(final ResultSet results)
                    throws SQLException
                        {
                        return new Table()
                            {
                            final String name = results.getString("TABLE_NAME");
                            protected Table table()
                                {
                                return this ;
                                }
                            @Override
                            public Schema parent()
                                {
                                return schema();
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
                                        final PreparedStatement statement = connection.prepareStatement(
                                            "SELECT DISTINCT " +
                                            "  COLUMN_NAME " +
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
                                        return new Iterable<Column>()
                                            {
                                            @Override
                                            public Iterator<Column> iterator()
                                                {
                                                return new ResultSetIterator<Column>(results)
                                                    {
                                                    @Override
                                                    protected Column getNext(final ResultSet results)
                                                    throws SQLException
                                                        {
                                                        return column(
                                                            results
                                                            );
                                                        }
                                                    };
                                                }
                                            };
                                        }

                                    @Override
                                    public Column select(String name)
                                        throws SQLException
                                        {
                                        // TODO Auto-generated method stub
                                        return null;
                                        }
                                    };
                                }
                            protected Column column(final ResultSet results)
                            throws SQLException
                                {
                                return new Column()
                                    {
                                    protected String name = results.getString("COLUMN_NAME");
                                    @Override
                                    public Table parent()
                                        {
                                        return table();
                                        }
                                    @Override
                                    public String name()
                                        {
                                        return name;
                                        }
                                    };
                                }
                            };
                        }
                    };
                }
            };
        }
    }
