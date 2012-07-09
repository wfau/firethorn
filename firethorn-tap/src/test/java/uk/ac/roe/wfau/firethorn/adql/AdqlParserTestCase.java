/*
 *  Copyright (C) 2012 Royal Observatory, University of Edinburgh, UK
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
package uk.ac.roe.wfau.firethorn.adql ;

import lombok.extern.slf4j.Slf4j;

import java.net.URI;

import java.util.Iterator;
import java.util.ArrayList;

import java.io.StringBufferInputStream;

import org.junit.Test;
import static org.junit.Assert.*;

import adql.query.ADQLQuery;
import adql.query.from.ADQLTable;
import adql.query.from.FromContent;

import adql.parser.ADQLParser;
import adql.parser.QueryChecker;
import adql.parser.ParseException;

import adql.db.DBTable;
import adql.db.DBColumn;
import adql.db.SearchColumnList;

import adql.db.DBChecker;
import adql.db.DefaultDBTable;
import adql.db.DefaultDBColumn;

import uk.ac.roe.wfau.firethorn.test.TestBase;
import uk.ac.roe.wfau.firethorn.widgeon.WidgeonView ;
import uk.ac.roe.wfau.firethorn.widgeon.WidgeonViewTestBase ;

/**
 *
 */
@Slf4j
public class AdqlParserTestCase
extends WidgeonViewTestBase
    {

    @Test
    public void test000()
    throws Exception
        {

        // Create a parser:
        ADQLParser parser = new ADQLParser();

        //
        // Create our table description.
        DefaultDBTable dbtable = new DefaultDBTable(
            "jdbc_catalog",
            "adql_catalog",
            "jdbc_schema",
            "adql_schema",
            "jdbc_table",
            "adql_table"
            );

        dbtable.addColumn(
            new DefaultDBColumn(
                "jdbc_ra",
                "adql_ra",
                dbtable)
            );
        dbtable.addColumn(
            new DefaultDBColumn(
                "jdbc_dec",
                "adql_dec",
                dbtable
                )
            );
        dbtable.addColumn(
            new DefaultDBColumn(
                "jdbc_pts",
                "adql_pts",
                dbtable
                )
            );

        //
        // Create a list of tables.
        ArrayList tables = new ArrayList();
        tables.add(dbtable);

        //
        // Create our QueryChecker:
        QueryChecker checker = new DBChecker(tables);

        //
        // Add our checker to our parser.
        parser.setQueryChecker(checker);

        // Parse some ADQL.
        ADQLQuery query = parser.parseQuery(
            new StringBufferInputStream(
                  "SELECT"
                + "    adql_ra  as ra,"
                + "    adql_dec as dec,"
                + "    adql_pts as pts"
                + " FROM"
                + "    adql_table as psc"
                + " WHERE"
                + "    adql_ra  Between '56.0' AND '57.9'"
                + " AND"
                + "    adql_dec Between '24.0' AND '24.2'"
                )
            );

        log.debug("Got query [{}]", query.getName());

        log.debug("From tables --");
        FromContent from = query.getFrom();
        for (ADQLTable table : from.getTables())
            {
            log.debug(" Table [{}][{}]", table.getTableName(), table.getFullTableName());
            }

        log.debug("From columns --");
        for (DBColumn column : from.getDBColumns())
            {

            log.debug(" ADQL [{}][{}][{}][{}]", new String[]{
                column.getADQLName(),
                column.getTable().getADQLName(),
                column.getTable().getADQLSchemaName(),
                column.getTable().getADQLCatalogName()
                });

            log.debug(" JDBC [{}][{}][{}][{}]", new String[]{
                column.getDBName(),
                column.getTable().getDBName(),
                column.getTable().getDBSchemaName(),
                column.getTable().getDBCatalogName()
                });

            }
        }

    public static class TestDBTable
    implements DBTable
        {

        private WidgeonView.Table table ;

        public TestDBTable(WidgeonView.Table table)
            {
            this(
                table,
                null,
                null
                );
            }

        public TestDBTable(WidgeonView.Table table, java.lang.String jdbcName, java.lang.String adqlName)
            {
log.debug("TestDBTable({}, {}, {})", new String[]{ table.name(), jdbcName, adqlName});
            this.table = table ;
            this.jdbcName = jdbcName;
            this.adqlName = adqlName;
            }

        private String adqlName ;
        private String jdbcName ;

        @Override
        public DBTable copy(java.lang.String jdbcName, java.lang.String adqlName)
            {
            return new TestDBTable(
                table,
                jdbcName, 
                adqlName
                );
            }

        @Override
        public String getADQLName()
            {
//log.debug("TestDBTable.getADQLName()");
            if (this.adqlName != null)
                {
//log.debug(" Name [{}]", this.adqlName);
                return this.adqlName ;
                } 
            else {            
//log.debug(" Name [{}]", table.name());
                return table.name();
                }
            }

        @Override
        public String getADQLSchemaName()
            {
//log.debug("TestDBTable.getADQLSchemaName()");
//log.debug(" Name [{}]", table.schema().name());
            return table.parent().name();
            }

        @Override
        public String getADQLCatalogName()
            {
//log.debug("TestDBTable.getADQLCatalogName()");
//log.debug(" Name [{}]", table.catalog().name());
            return table.parent().parent().name();
            }

        @Override
        public String getDBName()
            {
//log.debug("TestDBTable.getDBName()");
            if (this.jdbcName != null)
                {
//log.debug(" Name [{}]", this.jdbcName);
                return this.jdbcName ;
                } 
            else {            
//log.debug(" Name [{}]", table.base().name());
                return table.base().name();
                }
            }

        @Override
        public String getDBSchemaName()
            {
//log.debug("TestDBTable.getDBSchemaName()");
//log.debug(" Name [{}]", table.base().schema().name());
            return table.base().parent().name();
            }

        @Override
        public String getDBCatalogName()
            {
//log.debug("TestDBTable.getDBCatalogName()");
//log.debug(" Name [{}]", table.base().catalog().name());
            return table.base().parent().parent().name();
            }
        
        @Override
        public DBColumn getColumn(java.lang.String name, boolean adql)
            {
//log.debug("TestDBTable.getColumn({},{})", name, adql);
            WidgeonView.Column column ;
            if (adql)
                {
                column = table.columns().search(
                    name
                    );
                }
            else {
                column = table.columns().search(
                    table.base().columns().search(
                        name
                        )
                    );
                }

            if (column != null)
                {
                return new TestDBColumn(
                    this,
                    column
                    );
                }
            else {
                return null ;
                }
            }

        public Iterator<DBColumn> iterator()
            {
//log.debug("TestDBTable.iterator()");
            return new Iterator<DBColumn>()
                {

                private Iterator<WidgeonView.Column> iter = table.columns().select().iterator();

                @Override
                public DBColumn next()
                    {
//log.debug("TestDBTable.iterator().next()");
                    return new TestDBColumn(
                        TestDBTable.this,
                        iter.next()
                        );
                    }

                @Override
                public boolean hasNext()
                    {
                    return iter.hasNext();
                    }

                @Override
                public void remove()
                    {
                    throw new UnsupportedOperationException(
                        "Iterator.remove() is not supported"
                        );
                    }
                };
            }
        }

    public static class TestDBColumn
    implements DBColumn
        {

        private TestDBTable parent ;
        private WidgeonView.Column column ;

        public TestDBColumn(TestDBTable parent, WidgeonView.Column column)
            {
//log.debug("TestDBColumn()");
            this.parent = parent ;
            this.column = column ;
            }

        @Override
        public DBColumn copy(java.lang.String dbName, java.lang.String adqlName, DBTable dbTable)
            {
            throw new UnsupportedOperationException(
                "DBColumn.copy() is not supported"
                );
            }

        @Override
        public String getADQLName()
            {
//log.debug("TestDBColumn.getADQLName()");
            return column.name();
            }

        @Override
        public String getDBName()
            {
//log.debug("TestDBColumn.getDBName()");
            return column.base().name();
            }
                
        @Override
        public DBTable getTable()
            {
//log.debug("TestDBColumn.getTable()");
            return parent;
            }
        }

    @Test
    public void test001()
    throws Exception
        {
        //
        // Create base catalog, schema and table.
        assertNotNull(
            base().catalogs().create(
                "jdbc_catalog"
                ).schemas().create(
                    "jdbc_schema"
                    ).tables().create(
                        "jdbc_table"
                        )
            );
        //
        // Create the columns.
        assertNotNull(
            base().catalogs().select(
                "jdbc_catalog"
                ).schemas().select(
                    "jdbc_schema"
                    ).tables().select(
                        "jdbc_table"
                        ).columns().create(
                            "jdbc_ra"
                            )
            );
        assertNotNull(
            base().catalogs().select(
                "jdbc_catalog"
                ).schemas().select(
                    "jdbc_schema"
                    ).tables().select(
                        "jdbc_table"
                        ).columns().create(
                            "jdbc_dec"
                            )
            );
        assertNotNull(
            base().catalogs().select(
                "jdbc_catalog"
                ).schemas().select(
                    "jdbc_schema"
                    ).tables().select(
                        "jdbc_table"
                        ).columns().create(
                            "jdbc_pts"
                            )
            );
        //
        // Create our view.
        assertNotNull(
            base().views().create(
                "adql_view"
                )
            );

        //
        // Change the view names.
        base().views().select(
            "adql_view"
            ).catalogs().select(
                "jdbc_catalog"
                ).name(
                    "adql_catalog"
                    );

        base().views().select(
            "adql_view"
            ).catalogs().select(
                "adql_catalog"
                ).schemas().select(
                    "jdbc_schema"
                    ).name(
                        "adql_schema"
                        );

        base().views().select(
            "adql_view"
            ).catalogs().select(
                "adql_catalog"
                ).schemas().select(
                    "adql_schema"
                    ).tables().select(
                        "jdbc_table"
                        ).name(
                            "adql_table"
                            );

        base().views().select(
            "adql_view"
            ).catalogs().select(
                "adql_catalog"
                ).schemas().select(
                    "adql_schema"
                    ).tables().select(
                        "adql_table"
                        ).columns().select(
                            "jdbc_ra"
                            ).name(
                                "adql_ra"
                                );

        base().views().select(
            "adql_view"
            ).catalogs().select(
                "adql_catalog"
                ).schemas().select(
                    "adql_schema"
                    ).tables().select(
                        "adql_table"
                        ).columns().select(
                            "jdbc_dec"
                            ).name(
                                "adql_dec"
                                );

        base().views().select(
            "adql_view"
            ).catalogs().select(
                "adql_catalog"
                ).schemas().select(
                    "adql_schema"
                    ).tables().select(
                        "adql_table"
                        ).columns().select(
                            "jdbc_pts"
                            ).name(
                                "adql_pts"
                                );

        // Create our parser:
        ADQLParser parser = new ADQLParser();

        //
        // Create our parser data.
        DBTable dbtable = new TestDBTable(
            base().views().select(
                "adql_view"
                ).catalogs().select(
                    "adql_catalog"
                    ).schemas().select(
                        "adql_schema"
                        ).tables().select(
                            "adql_table"
                            )
            );


        ArrayList tables = new ArrayList<DBTable>();
        tables.add(
            dbtable
            );
        //
        // Create our QueryChecker:
        QueryChecker checker = new DBChecker(
            tables
            );
        //
        // Add our checker to our parser.
        parser.setQueryChecker(checker);

        // Parse some ADQL.
        ADQLQuery query = parser.parseQuery(
            new StringBufferInputStream(
                  "SELECT"
                + "    psc.adql_ra  as ra,"
                + "    psc.adql_dec as dec,"
                + "    psc.adql_pts as pts"
                + " FROM"
                + "    adql_table as psc"
                + " WHERE"
                + "    adql_ra Between '56.0' AND '57.9'"
                + " AND"
                + "    adql_dec Between '24.0' AND '24.2'"
                )
            );

        log.debug("Got query [{}]", query.getName());

        log.debug("From tables --");
        FromContent from = query.getFrom();
        for (ADQLTable table : from.getTables())
            {
            log.debug(" Table [{}][{}]", table.getTableName(), table.getFullTableName());
            }

        log.debug("From columns --");
        for (DBColumn column : from.getDBColumns())
            {

            log.debug(" ADQL [{}][{}][{}][{}]", new String[]{
                column.getADQLName(),
                column.getTable().getADQLName(),
                column.getTable().getADQLSchemaName(),
                column.getTable().getADQLCatalogName()
                });

            log.debug(" JDBC [{}][{}][{}][{}]", new String[]{
                column.getDBName(),
                column.getTable().getDBName(),
                column.getTable().getDBSchemaName(),
                column.getTable().getDBCatalogName()
                });

            }
        }
    }
