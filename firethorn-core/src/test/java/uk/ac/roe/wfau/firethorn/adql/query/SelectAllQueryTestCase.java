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
package uk.ac.roe.wfau.firethorn.adql.query ;

import lombok.extern.slf4j.Slf4j;

import org.junit.Before;
import org.junit.Test;

import uk.ac.roe.wfau.firethorn.adql.query.AdqlQuery.SelectField;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlColumn;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlResource;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlSchema;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlTable;
import uk.ac.roe.wfau.firethorn.meta.base.BaseResource;
import uk.ac.roe.wfau.firethorn.meta.jdbc.JdbcResource;
import uk.ac.roe.wfau.firethorn.meta.jdbc.JdbcSchema;
import uk.ac.roe.wfau.firethorn.test.TestBase;


/**
 *
 *
 */
@Slf4j
public class SelectAllQueryTestCase
extends TestBase
    {

    private JdbcResource twomass ;
    private AdqlSchema   schema  ;  
    private AdqlResource workspace ;
    /**
     * Create our resources.
     *
     */
    @Before
    public void init()
        {
        //
        // Create our JDBC resources.
        this.twomass = factories().jdbc().resources().create(
            "twomass",
            "TWOMASS",
            "twomass",
            "spring:RoeTWOMASS"
            );
        //
        // Create our ADQL workspace.
        this.workspace = factories().adql().resources().create(
            "adql-workspace"
            );
        //
        // Import the JDBC tables into our workspace.
        this.schema = this.workspace.schemas().create(
            "adql_twomass"
            );
        this.schema.tables().create(
            this.twomass.schemas().select(
                "TWOMASS",
                "dbo"
                ).tables().select(
                    "twomass_psc"
                    )
            );
        }

    /**
     * Debug display of a query.
     *
     */
    public void debug(final AdqlQuery query)
        {
        log.debug("Query -- ");
        log.debug("Mode   [{}]", query.mode());
        log.debug("Status [{}]", query.status());
        log.debug("ADQL   [{}]", query.adql());
        log.debug("OSQL   [{}]", query.osql());
        log.debug("Target [{}]", query.primary().ident());
        log.debug("Resources -- ");
        for (final BaseResource<?> target : query.resources())
            {
            log.debug("Resource [{}]", target.fullname());
            }
        log.debug("Tables -- ");
        for (final AdqlTable table : query.tables())
            {
            log.debug("Table [{}]", table.fullname());
            }
        log.debug("Columns -- ");
        for (final AdqlColumn column : query.columns())
            {
            log.debug("Column [{}]", column.fullname());
            }
        log.debug("Fields -- ");
        for (final SelectField field : query.fields())
            {
            log.debug("Field [{}][{}][{}]", field.name(), field.type(), field.length());
            }
        }

    /**
     * Test query "SELECT *" 
     *
     */
    private static final String QUERY_000 =

          "SELECT"
        + "    *"
        + " FROM"
        + "    adql_twomass.twomass_psc as twomass"
        + " WHERE"
        + "    ra  BETWEEN '56.0' AND '57.9'"
        + " AND"
        + "    dec BETWEEN '24.0' AND '24.2'"
        + ""
        ;

	@Test
    public void test000()
    throws Exception
        {
        //
        // Create the query and check the results.
        final AdqlQuery query = this.schema.queries().create(
            QUERY_000
            );
        debug(query);
        }

    /**
     * Test query "SELECT twomass.*" 
     *
     */
    private static final String QUERY_001 =

          "SELECT"
        + "    twomass.*"
        + " FROM"
        + "    adql_twomass.twomass_psc as twomass"
        + " WHERE"
        + "    ra  BETWEEN '56.0' AND '57.9'"
        + " AND"
        + "    dec BETWEEN '24.0' AND '24.2'"
        + ""
        ;

    @Test
    public void test001()
    throws Exception
        {
        //
        // Create the query and check the results.
        final AdqlQuery query = this.schema.queries().create(
            QUERY_001
            );
        debug(query);
        }
    }

