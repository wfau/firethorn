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

import uk.ac.roe.wfau.firethorn.adql.query.AdqlQuery.Syntax.State;
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

        void validate(final AdqlQuery.SelectField field)
            {
            log.debug("validate(SelectField)");
            log.debug("  name [{}][{}]", this.adqlname, field.name());
            log.debug("  size [{}][{}]", this.adqlsize, field.arraysize());
            log.debug("  type [{}][{}]", this.adqltype, field.type());
            assertEquals(
                this.adqlname,
                field.name()
                ) ;
            assertEquals(
                this.adqlsize,
                field.arraysize()
                ) ;
            assertEquals(
                this.adqltype,
                field.type()
                ) ;
            }
        
        void validate(final AdqlColumn column)
            {
            log.debug("validate(AdqlColumn)");
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
            log.debug("validate(JdbcColumn)");
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
        if (query.syntax().state() == State.VALID)
            {
            assertNotNull(
                query.fields()
                );
            int i = 0 ;
            for (AdqlQuery.SelectField field : query.fields())
                {
                expected[i++].validate(
                    field
                    );
                }
            assertNotNull(
                query.results().adql()
                );

            int j = 0 ;
            for (AdqlColumn column : query.results().adql().columns().select())
                {
                expected[j++].validate(
                    column
                    );
                }
            assertEquals(
                expected.length,
                j
                );

            assertNotNull(
                query.results().jdbc()
                );
            int k = 0 ;
            for (JdbcColumn column : query.results().jdbc().columns().select())
                {
                expected[k++].validate(
                    column
                    );
                }
            assertEquals(
                expected.length,
                k
                );
            }
        else {
            assertTrue(
                ! query.fields().iterator().hasNext()
                );
            assertNull(
                query.results().adql()
                );
            assertNull(
                query.results().jdbc()
                );
            }
        }

    //
    // Query syntax error is deliberate.
    //@Test
    public void test000()
    throws Exception
        {
        validate(
            this.schema.queries().create(
                "SELECT"
                + "    frog"
                + "    toad"
                + " FROM"
                + "    adql_twomass.twomass_psc as twomass"
                + " WHERE"
                + "    ra  BETWEEN '56.0' AND '57.9'"
                + " AND"
                + "    dec BETWEEN '24.0' AND '24.2'"
                + ""
                ),
            new ExpectedColumn[] {}
            );
        }

    //@Test
    public void test001()
    throws Exception
        {
        validate(
            this.schema.queries().create(
                "SELECT"
                + "    date as mydate"
                + " FROM"
                + "    adql_twomass.twomass_psc as twomass"
                + " WHERE"
                + "    ra  BETWEEN '56.0' AND '57.9'"
                + " AND"
                + "    dec BETWEEN '24.0' AND '24.2'"
                + ""
                ),
            new ExpectedColumn[] {
                new ExpectedColumn("mydate", AdqlColumn.Type.TIMESTAMP, 23, "mydate", JdbcColumn.Type.TIMESTAMP, 23)
                }                
            );
        }
    
    //@Test
    public void test002()
    throws Exception
        {
        validate(
            this.schema.queries().create(
                "SELECT"
                + "    MAX(ra),"
                + "    MAX(dec) as maxdec"
                + " FROM"
                + "    adql_twomass.twomass_psc as twomass"
                + " WHERE"
                + "    ra  BETWEEN '56.0' AND '57.9'"
                + " AND"
                + "    dec BETWEEN '24.0' AND '24.2'"
                + ""
                ),
            new ExpectedColumn[] {
                new ExpectedColumn("MAX",    AdqlColumn.Type.DOUBLE, 0, "MAX",    JdbcColumn.Type.DOUBLE, 53),
                new ExpectedColumn("maxdec", AdqlColumn.Type.DOUBLE, 0, "maxdec", JdbcColumn.Type.DOUBLE, 53),
                }
            );
        }

    @Test
    public void test003()
    throws Exception
        {
        validate(
            this.schema.queries().create(
                "SELECT"
                + "    *"
                + " FROM"
                + "    adql_twomass.twomass_psc as twomass"
                + " WHERE"
                + "    ra  BETWEEN '56.0' AND '57.9'"
                + " AND"
                + "    dec BETWEEN '24.0' AND '24.2'"
                + ""
                ),
            new ExpectedColumn[] {
                new ExpectedColumn("cx",               AdqlColumn.Type.DOUBLE,     0,   "cx",               JdbcColumn.Type.DOUBLE,    53),
                new ExpectedColumn("cy",               AdqlColumn.Type.DOUBLE,     0,   "cy",               JdbcColumn.Type.DOUBLE,    53),  
                new ExpectedColumn("cz",               AdqlColumn.Type.DOUBLE,     0,   "cz",               JdbcColumn.Type.DOUBLE,    53),  
                new ExpectedColumn("htmID",            AdqlColumn.Type.LONG,       0,   "htmID",            JdbcColumn.Type.BIGINT,    19),      
                new ExpectedColumn("ra",               AdqlColumn.Type.DOUBLE,     0,   "ra",               JdbcColumn.Type.DOUBLE,    53),  
                new ExpectedColumn("dec",              AdqlColumn.Type.DOUBLE,     0,   "dec",              JdbcColumn.Type.DOUBLE,    53),  
                new ExpectedColumn("err_maj",          AdqlColumn.Type.FLOAT,      0,   "err_maj",          JdbcColumn.Type.REAL,      24),  
                new ExpectedColumn("err_min",          AdqlColumn.Type.FLOAT,      0,   "err_min",          JdbcColumn.Type.REAL,      24),
                new ExpectedColumn("err_ang",          AdqlColumn.Type.SHORT,      0,   "err_ang",          JdbcColumn.Type.SMALLINT,   5),
                new ExpectedColumn("designation",      AdqlColumn.Type.CHAR,      17,   "designation",      JdbcColumn.Type.VARCHAR,   17),
                new ExpectedColumn("j_m",              AdqlColumn.Type.FLOAT,      0,   "j_m",              JdbcColumn.Type.REAL,      24),
                new ExpectedColumn("j_cmsig",          AdqlColumn.Type.FLOAT,      0,   "j_cmsig",          JdbcColumn.Type.REAL,      24),
                new ExpectedColumn("j_msigcom",        AdqlColumn.Type.FLOAT,      0,   "j_msigcom",        JdbcColumn.Type.REAL,      24),
                new ExpectedColumn("j_snr",            AdqlColumn.Type.FLOAT,      0,   "j_snr",            JdbcColumn.Type.REAL,      24),
                new ExpectedColumn("h_m",              AdqlColumn.Type.FLOAT,      0,   "h_m",              JdbcColumn.Type.REAL,      24),
                new ExpectedColumn("h_cmsig",          AdqlColumn.Type.FLOAT,      0,   "h_cmsig",          JdbcColumn.Type.REAL,      24),
                new ExpectedColumn("h_msigcom",        AdqlColumn.Type.FLOAT,      0,   "h_msigcom",        JdbcColumn.Type.REAL,      24),
                new ExpectedColumn("h_snr",            AdqlColumn.Type.FLOAT,      0,   "h_snr",            JdbcColumn.Type.REAL,      24),
                new ExpectedColumn("k_m",              AdqlColumn.Type.FLOAT,      0,   "k_m",              JdbcColumn.Type.REAL,      24),
                new ExpectedColumn("k_cmsig",          AdqlColumn.Type.FLOAT,      0,   "k_cmsig",          JdbcColumn.Type.REAL,      24),
                new ExpectedColumn("k_msigcom",        AdqlColumn.Type.FLOAT,      0,   "k_msigcom",        JdbcColumn.Type.REAL,      24),
                new ExpectedColumn("k_snr",            AdqlColumn.Type.FLOAT,      0,   "k_snr",            JdbcColumn.Type.REAL,      24),
                new ExpectedColumn("ph_qual",          AdqlColumn.Type.CHAR,       3,   "ph_qual",          JdbcColumn.Type.VARCHAR,    3),
                new ExpectedColumn("rd_flg",           AdqlColumn.Type.CHAR,       3,   "rd_flg",           JdbcColumn.Type.VARCHAR,    3),
                new ExpectedColumn("bl_flg",           AdqlColumn.Type.CHAR,       3,   "bl_flg",           JdbcColumn.Type.VARCHAR,    3),
                new ExpectedColumn("cc_flg",           AdqlColumn.Type.CHAR,       3,   "cc_flg",           JdbcColumn.Type.VARCHAR,    3),
                new ExpectedColumn("ndet",             AdqlColumn.Type.CHAR,       6,   "ndet",             JdbcColumn.Type.VARCHAR,    6),
                new ExpectedColumn("prox",             AdqlColumn.Type.FLOAT,      0,   "prox",             JdbcColumn.Type.REAL,      24),
                new ExpectedColumn("pxpa",             AdqlColumn.Type.SHORT,      0,   "pxpa",             JdbcColumn.Type.SMALLINT,   5),
                new ExpectedColumn("pxcntr",           AdqlColumn.Type.INTEGER,    0,   "pxcntr",           JdbcColumn.Type.INTEGER,   10),
                new ExpectedColumn("gal_contam",       AdqlColumn.Type.SHORT,      0,   "gal_contam",       JdbcColumn.Type.SMALLINT,   5),
                new ExpectedColumn("mp_flg",           AdqlColumn.Type.SHORT,      0,   "mp_flg",           JdbcColumn.Type.SMALLINT,   5),
                new ExpectedColumn("pts_key",          AdqlColumn.Type.INTEGER,    0,   "pts_key",          JdbcColumn.Type.INTEGER,   10),
                new ExpectedColumn("hemis",            AdqlColumn.Type.CHAR,       1,   "hemis",            JdbcColumn.Type.VARCHAR,    1),
                new ExpectedColumn("date",             AdqlColumn.Type.TIMESTAMP, 23,   "date",             JdbcColumn.Type.TIMESTAMP, 23),
                new ExpectedColumn("scan",             AdqlColumn.Type.SHORT,      0,   "scan",             JdbcColumn.Type.SMALLINT,   5),
                new ExpectedColumn("glon",             AdqlColumn.Type.FLOAT,      0,   "glon",             JdbcColumn.Type.REAL,      24),
                new ExpectedColumn("glat",             AdqlColumn.Type.FLOAT,      0,   "glat",             JdbcColumn.Type.REAL,      24),
                new ExpectedColumn("x_scan",           AdqlColumn.Type.FLOAT,      0,   "x_scan",           JdbcColumn.Type.REAL,      24),
                new ExpectedColumn("jdate",            AdqlColumn.Type.DOUBLE,     0,   "jdate",            JdbcColumn.Type.DOUBLE,    53),
                new ExpectedColumn("j_psfchi",         AdqlColumn.Type.FLOAT,      0,   "j_psfchi",         JdbcColumn.Type.REAL,      24),
                new ExpectedColumn("h_psfchi",         AdqlColumn.Type.FLOAT,      0,   "h_psfchi",         JdbcColumn.Type.REAL,      24),
                new ExpectedColumn("k_psfchi",         AdqlColumn.Type.FLOAT,      0,   "k_psfchi",         JdbcColumn.Type.REAL,      24),
                new ExpectedColumn("j_m_stdap",        AdqlColumn.Type.FLOAT,      0,   "j_m_stdap",        JdbcColumn.Type.REAL,      24),
                new ExpectedColumn("j_msig_stdap",     AdqlColumn.Type.FLOAT,      0,   "j_msig_stdap",     JdbcColumn.Type.REAL,      24),
                new ExpectedColumn("h_m_stdap",        AdqlColumn.Type.FLOAT,      0,   "h_m_stdap",        JdbcColumn.Type.REAL,      24),
                new ExpectedColumn("h_msig_stdap",     AdqlColumn.Type.FLOAT,      0,   "h_msig_stdap",     JdbcColumn.Type.REAL,      24),
                new ExpectedColumn("k_m_stdap",        AdqlColumn.Type.FLOAT,      0,   "k_m_stdap",        JdbcColumn.Type.REAL,      24),
                new ExpectedColumn("k_msig_stdap",     AdqlColumn.Type.FLOAT,      0,   "k_msig_stdap",     JdbcColumn.Type.REAL,      24),
                new ExpectedColumn("dist_edge_ns",     AdqlColumn.Type.INTEGER,    0,   "dist_edge_ns",     JdbcColumn.Type.INTEGER,   10),
                new ExpectedColumn("dist_edge_ew",     AdqlColumn.Type.INTEGER,    0,   "dist_edge_ew",     JdbcColumn.Type.INTEGER,   10),
                new ExpectedColumn("dist_edge_flg",    AdqlColumn.Type.CHAR,       2,   "dist_edge_flg",    JdbcColumn.Type.VARCHAR,    2),
                new ExpectedColumn("dup_src",          AdqlColumn.Type.SHORT,      0,   "dup_src",          JdbcColumn.Type.SMALLINT,   5),
                new ExpectedColumn("use_src",          AdqlColumn.Type.SHORT,      0,   "use_src",          JdbcColumn.Type.SMALLINT,   5),
                new ExpectedColumn("a",                AdqlColumn.Type.CHAR,       1,   "a",                JdbcColumn.Type.VARCHAR,    1),
                new ExpectedColumn("dist_opt",         AdqlColumn.Type.FLOAT,      0,   "dist_opt",         JdbcColumn.Type.REAL,      24),
                new ExpectedColumn("phi_opt",          AdqlColumn.Type.SHORT,      0,   "phi_opt",          JdbcColumn.Type.SMALLINT,   5),
                new ExpectedColumn("b_m_opt",          AdqlColumn.Type.FLOAT,      0,   "b_m_opt",          JdbcColumn.Type.REAL,      24),
                new ExpectedColumn("vr_m_opt",         AdqlColumn.Type.FLOAT,      0,   "vr_m_opt",         JdbcColumn.Type.REAL,      24),
                new ExpectedColumn("nopt_mchs",        AdqlColumn.Type.SHORT,      0,   "nopt_mchs",        JdbcColumn.Type.SMALLINT,   5),
                new ExpectedColumn("ext_key",          AdqlColumn.Type.INTEGER,    0,   "ext_key",          JdbcColumn.Type.INTEGER,   10),
                new ExpectedColumn("scan_key",         AdqlColumn.Type.INTEGER,    0,   "scan_key",         JdbcColumn.Type.INTEGER,   10),
                new ExpectedColumn("coadd_key",        AdqlColumn.Type.INTEGER,    0,   "coadd_key",        JdbcColumn.Type.INTEGER,   10),
                new ExpectedColumn("coadd",            AdqlColumn.Type.SHORT,      0,   "coadd",            JdbcColumn.Type.SMALLINT,   5)
                }
            );
        }
    }

