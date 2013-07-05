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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.Iterator;

import lombok.extern.slf4j.Slf4j;

import org.junit.Test;

import uk.ac.roe.wfau.firethorn.adql.query.QuerySelectFieldTestCase.ExpectedField;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlColumn;
import uk.ac.roe.wfau.firethorn.meta.jdbc.JdbcColumn;


/**
 *
 *
 */
@Slf4j
public class QueryResultsTableTestCase
extends TwomassQueryTestBase
    {

    /**
     * An expected column.
     * todo refactor this as a JdbcColumn.Metadata 
     * 
     */
    public static class ExpectedColumn
        {
        public ExpectedColumn(String adqlname, AdqlColumn.Type adqltype, Integer adqlsize, String jdbcname, JdbcColumn.Type jdbctype, Integer jdbcsize)
            {
            this.adqlname = adqlname ;
            this.adqltype = adqltype ;
            this.adqlsize = adqlsize ;

            this.jdbcname = jdbcname ;
            this.jdbctype = jdbctype ;
            this.jdbcsize = jdbcsize ;
            }

        private Integer adqlsize ;
        public Integer adqlsize()
            {
            return this.adqlsize;
            }
        private AdqlColumn.Type adqltype;
        public  AdqlColumn.Type adqltype()
            {
            return this.adqltype;
            }
        private String adqlname ;
        public String adqlname()
            {
            return this.adqlname;
            }

        private Integer jdbcsize ;
        public Integer jdbcsize()
            {
            return this.jdbcsize;
            }
        private JdbcColumn.Type jdbctype;
        public  JdbcColumn.Type jdbctype()
            {
            return this.jdbctype;
            }
        private String jdbcname ;
        public String jdbcname()
            {
            return this.jdbcname;
            }
        
        void validate(final AdqlColumn column)
            {
            log.debug("check()");
            log.debug("  name [{}][{}]", this.adqlname, column.name());
            log.debug("  size [{}][{}]", this.adqlsize, column.meta().adql().arraysize());
            log.debug("  type [{}][{}]", this.adqltype, column.meta().adql().type());
            assertEquals(
                this.adqlname,
                column.name()
                ) ;
            assertEquals(
                this.adqlsize,
                column.meta().adql().arraysize()
                ) ;
            assertEquals(
                this.adqltype,
                column.meta().adql().type()
                ) ;
            }

        void validate(final JdbcColumn column)
            {
            log.debug("check()");
            log.debug("  name [{}][{}]", this.jdbcname, column.name());
            log.debug("  size [{}][{}]", this.jdbcsize, column.meta().jdbc().size());
            log.debug("  type [{}][{}]", this.jdbctype, column.meta().jdbc().type());
            assertEquals(
                this.jdbcname,
                column.name()
                ) ;
            assertEquals(
                this.jdbcsize,
                column.meta().jdbc().size()
                ) ;
            assertEquals(
                this.jdbctype,
                column.meta().jdbc().type()
                ) ;
            }
        }

    public void validate(AdqlQuery query, final ExpectedColumn[] expected)
    throws Exception
        {
        assertNotNull(
            query.results().adql()
            );
        int i = 0 ;
        for (AdqlColumn column : query.results().adql().columns().select())
            {
            expected[i++].validate(
                column
                );
            }
        assertEquals(
            expected.length,
            i
            );

        assertNotNull(
            query.results().jdbc()
            );
        int j = 0 ;
        for (JdbcColumn column : query.results().jdbc().columns().select())
            {
            expected[j++].validate(
                column
                );
            }
        assertEquals(
            expected.length,
            j
            );
        }
    
    private static final String QUERY_001 =
        "SELECT"
      + "    date as mydate"
      + " FROM"
      + "    adql_twomass.twomass_psc as twomass"
      + " WHERE"
      + "    ra  BETWEEN '56.0' AND '57.9'"
      + " AND"
      + "    dec BETWEEN '24.0' AND '24.2'"
      + ""
      ;

    public static final ExpectedColumn[] RESULTS_001 = {
        new ExpectedColumn("mydate", AdqlColumn.Type.TIMESTAMP, 23, "mydate", JdbcColumn.Type.TIMESTAMP, 23)
        }; 

    @Test
    public void test001()
    throws Exception
        {
        validate(
            this.schema.queries().create(
                QUERY_001
                ),
            RESULTS_001                
            );
        }

    private static final String QUERY_002 =
        "SELECT"
    + "    MAX(ra)"
    + "    MAX(dec) as maxdec"
      + " FROM"
      + "    adql_twomass.twomass_psc as twomass"
      + " WHERE"
      + "    ra  BETWEEN '56.0' AND '57.9'"
      + " AND"
      + "    dec BETWEEN '24.0' AND '24.2'"
      + ""
      ;
    
    public static final ExpectedColumn[] RESULTS_002 = {
        new ExpectedColumn("MAX",    AdqlColumn.Type.DOUBLE, 0, "MAX",    JdbcColumn.Type.DOUBLE, 6),
        new ExpectedColumn("maxdec", AdqlColumn.Type.DOUBLE, 0, "maxdec", JdbcColumn.Type.DOUBLE, 6),
        }; 
    
    @Test
    public void test002()
    throws Exception
        {
        validate(
            this.schema.queries().create(
                QUERY_002
                ),
            RESULTS_002
            );
        }
    }

