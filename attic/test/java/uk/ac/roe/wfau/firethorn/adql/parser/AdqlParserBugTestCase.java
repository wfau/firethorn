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
package uk.ac.roe.wfau.firethorn.adql.parser ;

import java.util.Arrays;

import org.junit.Test;

import adql.db.DBChecker;
import adql.db.DBTable;
import adql.db.DefaultDBColumn;
import adql.db.DefaultDBTable;
import adql.parser.ADQLParser;
import adql.query.ADQLQuery;
import adql.translator.ADQLTranslator;
import adql.translator.PostgreSQLTranslator;
import lombok.extern.slf4j.Slf4j;
import uk.ac.roe.wfau.firethorn.test.TestBase;

/**
 *
 */
@Slf4j
public class AdqlParserBugTestCase
extends TestBase
    {

    /**
     * ADQL query using column names (adql_ra, adql_dec) in the WHERE clause.
     *
     */
    private static final String TEST_ADQL =
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
        ;

    /**
     * This test always passes, producing valid generic SQL.
     *
     */
    @Test
    public void test000()
    throws Exception
        {

        //
        // Create a parser:
        final ADQLParser parser = new ADQLParser();

        //
        // Parse the test ADQL.
        final ADQLQuery query = parser.parseQuery(
            TEST_ADQL
            );

        //
        // Translate into SQL.
        final ADQLTranslator translator = new PostgreSQLTranslator();
        log.debug("SQL [{}]", translator.translate(query));

        }


    /**
     * This test passes with PASS_ADQL, but fails with FAIL_ADQL.
     * Adding the DBTable metadata means the parser does not recognise the column aliases for ra and dec.
     *
     */
    @Test
    public void test001()
    throws Exception
        {

        //
        // Create a parser:
        final ADQLParser parser = new ADQLParser();

        //
        // Create our table metadata.
        final DefaultDBTable dbtable = new DefaultDBTable(
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
        // Add our table metadata to our parser.
        parser.setQueryChecker(
            new DBChecker(
                Arrays.asList(
                    (DBTable) dbtable
                    )
                )
            );

        //
        // Parse the test ADQL.
        final ADQLQuery query = parser.parseQuery(
            TEST_ADQL
            );

        //
        // Translate into SQL.
        final ADQLTranslator translator = new PostgreSQLTranslator();
        log.debug("SQL [{}]", translator.translate(query));

        }
    }

