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
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import lombok.extern.slf4j.Slf4j;
import uk.ac.roe.wfau.firethorn.adql.query.AdqlQueryBase.Syntax.State;
import uk.ac.roe.wfau.firethorn.adql.query.blue.BlueQuery;
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
        public ExpectedColumn(final String adqlname, final AdqlColumn.AdqlType adqltype, final Integer adqlsize, final String jdbcname, final JdbcColumn.JdbcType jdbctype, final Integer jdbcsize)
            {
            this.adqlname = adqlname ;
            this.adqltype = adqltype ;
            this.adqlsize = adqlsize ;

            this.jdbcname = jdbcname ;
            this.jdbctype = jdbctype ;
            this.jdbcsize = jdbcsize ;
            }

        private final Integer adqlsize ;
        public Integer adqlsize()
            {
            return this.adqlsize;
            }
        private final AdqlColumn.AdqlType adqltype;
        public  AdqlColumn.AdqlType adqltype()
            {
            return this.adqltype;
            }
        private final String adqlname ;
        public String adqlname()
            {
            return this.adqlname;
            }

        private final Integer jdbcsize ;
        public Integer jdbcsize()
            {
            return this.jdbcsize;
            }
        private final JdbcColumn.JdbcType jdbctype;
        public  JdbcColumn.JdbcType jdbctype()
            {
            return this.jdbctype;
            }
        private final String jdbcname ;
        public String jdbcname()
            {
            return this.jdbcname;
            }

        void validate(final BlueQuery.SelectField field)
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
            log.debug("  size [{}][{}]", this.jdbcsize, column.meta().jdbc().arraysize());
            log.debug("  type [{}][{}]", this.jdbctype, column.meta().jdbc().jdbctype());
            assertEquals(
                this.jdbcname,
                column.name()
                ) ;
            assertEquals(
                this.jdbcsize,
                column.meta().jdbc().arraysize()
                ) ;
            assertEquals(
                this.jdbctype,
                column.meta().jdbc().jdbctype()
                ) ;
            }
        }

    public void validate(final BlueQuery query, final ExpectedColumn[] expected)
    throws Exception
        {
        if (expected.length > 0)
            {
            assertEquals(
                State.VALID,
                query.syntax().state()
                );

            assertNotNull(
                query.fields()
                );
            int i = 0 ;
            for (final BlueQuery.SelectField field : query.fields().select())
                {
                expected[i++].validate(
                    field
                    );
                }
            assertNotNull(
                query.results().adql()
                );

            int j = 0 ;
            for (final AdqlColumn column : query.results().adql().columns().select())
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
            for (final JdbcColumn column : query.results().jdbc().columns().select())
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
            assertEquals(
                State.PARSE_ERROR,
                query.syntax().state()
                );
            assertTrue(
                ! query.fields().select().iterator().hasNext()
                );
            assertNull(
                query.results().adql()
                );
            assertNull(
                query.results().jdbc()
                );
            }
        }

    @Test
    public void test000()
    throws Exception
        {
        validate(
            this.workspace.blues().create(
                "SELECT"
                + "    ra,"
                + "    dec"
                + " FROM"
                + "    adql_twomass.twomass_psc as twomass"
                + " WHERE"
                + "    ra  BETWEEN '56.0' AND '57.9'"
                + " AND"
                + "    dec BETWEEN '24.0' AND '24.2'"
                + ""
                ),
            new ExpectedColumn[] {
                new ExpectedColumn("ra",  AdqlColumn.AdqlType.DOUBLE, 0, "ra",  JdbcColumn.JdbcType.DOUBLE, 0),
                new ExpectedColumn("dec", AdqlColumn.AdqlType.DOUBLE, 0, "dec", JdbcColumn.JdbcType.DOUBLE, 0)
                }
            );
        }


    //
    // Query syntax error is deliberate.
    @Test
    public void test001()
    throws Exception
        {
        validate(
            this.workspace.blues().create(
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

    @Test
    public void test002()
    throws Exception
        {
        validate(
            this.workspace.blues().create(
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
                new ExpectedColumn("mydate", AdqlColumn.AdqlType.DATETIME, 0, "mydate", JdbcColumn.JdbcType.TIMESTAMP, 0)
                }
            );
        }

    @Test
    public void test003()
    throws Exception
        {
        validate(
            this.workspace.blues().create(
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
                new ExpectedColumn("cx",               AdqlColumn.AdqlType.DOUBLE,     0,   "cx",               JdbcColumn.JdbcType.DOUBLE,    0),
                new ExpectedColumn("cy",               AdqlColumn.AdqlType.DOUBLE,     0,   "cy",               JdbcColumn.JdbcType.DOUBLE,    0),
                new ExpectedColumn("cz",               AdqlColumn.AdqlType.DOUBLE,     0,   "cz",               JdbcColumn.JdbcType.DOUBLE,    0),
                new ExpectedColumn("htmID",            AdqlColumn.AdqlType.LONG,       0,   "htmID",            JdbcColumn.JdbcType.BIGINT,    0),
                new ExpectedColumn("ra",               AdqlColumn.AdqlType.DOUBLE,     0,   "ra",               JdbcColumn.JdbcType.DOUBLE,    0),
                new ExpectedColumn("dec",              AdqlColumn.AdqlType.DOUBLE,     0,   "dec",              JdbcColumn.JdbcType.DOUBLE,    0),
                new ExpectedColumn("err_maj",          AdqlColumn.AdqlType.FLOAT,      0,   "err_maj",          JdbcColumn.JdbcType.REAL,      0),
                new ExpectedColumn("err_min",          AdqlColumn.AdqlType.FLOAT,      0,   "err_min",          JdbcColumn.JdbcType.REAL,      0),
                new ExpectedColumn("err_ang",          AdqlColumn.AdqlType.SHORT,      0,   "err_ang",          JdbcColumn.JdbcType.SMALLINT,  0),
                new ExpectedColumn("designation",      AdqlColumn.AdqlType.CHAR,      17,   "designation",      JdbcColumn.JdbcType.VARCHAR,   17),
                new ExpectedColumn("j_m",              AdqlColumn.AdqlType.FLOAT,      0,   "j_m",              JdbcColumn.JdbcType.REAL,      0),
                new ExpectedColumn("j_cmsig",          AdqlColumn.AdqlType.FLOAT,      0,   "j_cmsig",          JdbcColumn.JdbcType.REAL,      0),
                new ExpectedColumn("j_msigcom",        AdqlColumn.AdqlType.FLOAT,      0,   "j_msigcom",        JdbcColumn.JdbcType.REAL,      0),
                new ExpectedColumn("j_snr",            AdqlColumn.AdqlType.FLOAT,      0,   "j_snr",            JdbcColumn.JdbcType.REAL,      0),
                new ExpectedColumn("h_m",              AdqlColumn.AdqlType.FLOAT,      0,   "h_m",              JdbcColumn.JdbcType.REAL,      0),
                new ExpectedColumn("h_cmsig",          AdqlColumn.AdqlType.FLOAT,      0,   "h_cmsig",          JdbcColumn.JdbcType.REAL,      0),
                new ExpectedColumn("h_msigcom",        AdqlColumn.AdqlType.FLOAT,      0,   "h_msigcom",        JdbcColumn.JdbcType.REAL,      0),
                new ExpectedColumn("h_snr",            AdqlColumn.AdqlType.FLOAT,      0,   "h_snr",            JdbcColumn.JdbcType.REAL,      0),
                new ExpectedColumn("k_m",              AdqlColumn.AdqlType.FLOAT,      0,   "k_m",              JdbcColumn.JdbcType.REAL,      0),
                new ExpectedColumn("k_cmsig",          AdqlColumn.AdqlType.FLOAT,      0,   "k_cmsig",          JdbcColumn.JdbcType.REAL,      0),
                new ExpectedColumn("k_msigcom",        AdqlColumn.AdqlType.FLOAT,      0,   "k_msigcom",        JdbcColumn.JdbcType.REAL,      0),
                new ExpectedColumn("k_snr",            AdqlColumn.AdqlType.FLOAT,      0,   "k_snr",            JdbcColumn.JdbcType.REAL,      0),
                new ExpectedColumn("ph_qual",          AdqlColumn.AdqlType.CHAR,       3,   "ph_qual",          JdbcColumn.JdbcType.VARCHAR,   3),
                new ExpectedColumn("rd_flg",           AdqlColumn.AdqlType.CHAR,       3,   "rd_flg",           JdbcColumn.JdbcType.VARCHAR,   3),
                new ExpectedColumn("bl_flg",           AdqlColumn.AdqlType.CHAR,       3,   "bl_flg",           JdbcColumn.JdbcType.VARCHAR,   3),
                new ExpectedColumn("cc_flg",           AdqlColumn.AdqlType.CHAR,       3,   "cc_flg",           JdbcColumn.JdbcType.VARCHAR,   3),
                new ExpectedColumn("ndet",             AdqlColumn.AdqlType.CHAR,       6,   "ndet",             JdbcColumn.JdbcType.VARCHAR,   6),
                new ExpectedColumn("prox",             AdqlColumn.AdqlType.FLOAT,      0,   "prox",             JdbcColumn.JdbcType.REAL,      0),
                new ExpectedColumn("pxpa",             AdqlColumn.AdqlType.SHORT,      0,   "pxpa",             JdbcColumn.JdbcType.SMALLINT,  0),
                new ExpectedColumn("pxcntr",           AdqlColumn.AdqlType.INTEGER,    0,   "pxcntr",           JdbcColumn.JdbcType.INTEGER,   0),
                new ExpectedColumn("gal_contam",       AdqlColumn.AdqlType.SHORT,      0,   "gal_contam",       JdbcColumn.JdbcType.SMALLINT,  0),
                new ExpectedColumn("mp_flg",           AdqlColumn.AdqlType.SHORT,      0,   "mp_flg",           JdbcColumn.JdbcType.SMALLINT,  0),
                new ExpectedColumn("pts_key",          AdqlColumn.AdqlType.INTEGER,    0,   "pts_key",          JdbcColumn.JdbcType.INTEGER,   0),
                new ExpectedColumn("hemis",            AdqlColumn.AdqlType.CHAR,       1,   "hemis",            JdbcColumn.JdbcType.VARCHAR,   1),
                new ExpectedColumn("date",             AdqlColumn.AdqlType.DATETIME,   0,   "date",             JdbcColumn.JdbcType.TIMESTAMP, 0),
                new ExpectedColumn("scan",             AdqlColumn.AdqlType.SHORT,      0,   "scan",             JdbcColumn.JdbcType.SMALLINT,  0),
                new ExpectedColumn("glon",             AdqlColumn.AdqlType.FLOAT,      0,   "glon",             JdbcColumn.JdbcType.REAL,      0),
                new ExpectedColumn("glat",             AdqlColumn.AdqlType.FLOAT,      0,   "glat",             JdbcColumn.JdbcType.REAL,      0),
                new ExpectedColumn("x_scan",           AdqlColumn.AdqlType.FLOAT,      0,   "x_scan",           JdbcColumn.JdbcType.REAL,      0),
                new ExpectedColumn("jdate",            AdqlColumn.AdqlType.DOUBLE,     0,   "jdate",            JdbcColumn.JdbcType.DOUBLE,    0),
                new ExpectedColumn("j_psfchi",         AdqlColumn.AdqlType.FLOAT,      0,   "j_psfchi",         JdbcColumn.JdbcType.REAL,      0),
                new ExpectedColumn("h_psfchi",         AdqlColumn.AdqlType.FLOAT,      0,   "h_psfchi",         JdbcColumn.JdbcType.REAL,      0),
                new ExpectedColumn("k_psfchi",         AdqlColumn.AdqlType.FLOAT,      0,   "k_psfchi",         JdbcColumn.JdbcType.REAL,      0),
                new ExpectedColumn("j_m_stdap",        AdqlColumn.AdqlType.FLOAT,      0,   "j_m_stdap",        JdbcColumn.JdbcType.REAL,      0),
                new ExpectedColumn("j_msig_stdap",     AdqlColumn.AdqlType.FLOAT,      0,   "j_msig_stdap",     JdbcColumn.JdbcType.REAL,      0),
                new ExpectedColumn("h_m_stdap",        AdqlColumn.AdqlType.FLOAT,      0,   "h_m_stdap",        JdbcColumn.JdbcType.REAL,      0),
                new ExpectedColumn("h_msig_stdap",     AdqlColumn.AdqlType.FLOAT,      0,   "h_msig_stdap",     JdbcColumn.JdbcType.REAL,      0),
                new ExpectedColumn("k_m_stdap",        AdqlColumn.AdqlType.FLOAT,      0,   "k_m_stdap",        JdbcColumn.JdbcType.REAL,      0),
                new ExpectedColumn("k_msig_stdap",     AdqlColumn.AdqlType.FLOAT,      0,   "k_msig_stdap",     JdbcColumn.JdbcType.REAL,      0),
                new ExpectedColumn("dist_edge_ns",     AdqlColumn.AdqlType.INTEGER,    0,   "dist_edge_ns",     JdbcColumn.JdbcType.INTEGER,   0),
                new ExpectedColumn("dist_edge_ew",     AdqlColumn.AdqlType.INTEGER,    0,   "dist_edge_ew",     JdbcColumn.JdbcType.INTEGER,   0),
                new ExpectedColumn("dist_edge_flg",    AdqlColumn.AdqlType.CHAR,       2,   "dist_edge_flg",    JdbcColumn.JdbcType.VARCHAR,   2),
                new ExpectedColumn("dup_src",          AdqlColumn.AdqlType.SHORT,      0,   "dup_src",          JdbcColumn.JdbcType.SMALLINT,  0),
                new ExpectedColumn("use_src",          AdqlColumn.AdqlType.SHORT,      0,   "use_src",          JdbcColumn.JdbcType.SMALLINT,  0),
                new ExpectedColumn("a",                AdqlColumn.AdqlType.CHAR,       1,   "a",                JdbcColumn.JdbcType.VARCHAR,   1),
                new ExpectedColumn("dist_opt",         AdqlColumn.AdqlType.FLOAT,      0,   "dist_opt",         JdbcColumn.JdbcType.REAL,      0),
                new ExpectedColumn("phi_opt",          AdqlColumn.AdqlType.SHORT,      0,   "phi_opt",          JdbcColumn.JdbcType.SMALLINT,  0),
                new ExpectedColumn("b_m_opt",          AdqlColumn.AdqlType.FLOAT,      0,   "b_m_opt",          JdbcColumn.JdbcType.REAL,      0),
                new ExpectedColumn("vr_m_opt",         AdqlColumn.AdqlType.FLOAT,      0,   "vr_m_opt",         JdbcColumn.JdbcType.REAL,      0),
                new ExpectedColumn("nopt_mchs",        AdqlColumn.AdqlType.SHORT,      0,   "nopt_mchs",        JdbcColumn.JdbcType.SMALLINT,  0),
                new ExpectedColumn("ext_key",          AdqlColumn.AdqlType.INTEGER,    0,   "ext_key",          JdbcColumn.JdbcType.INTEGER,   0),
                new ExpectedColumn("scan_key",         AdqlColumn.AdqlType.INTEGER,    0,   "scan_key",         JdbcColumn.JdbcType.INTEGER,   0),
                new ExpectedColumn("coadd_key",        AdqlColumn.AdqlType.INTEGER,    0,   "coadd_key",        JdbcColumn.JdbcType.INTEGER,   0),
                new ExpectedColumn("coadd",            AdqlColumn.AdqlType.SHORT,      0,   "coadd",            JdbcColumn.JdbcType.SMALLINT,  0)
                }
            );
        }

    @Test
    public void test004()
    throws Exception
        {
        validate(
            this.workspace.blues().create(
                "SELECT"

                + "    COUNT(ra),"
                + "    count(ra)   as countra,"
                + "    count(dec)  as countdec,"
                + "    count(*)    as countall,"

                + "    MIN(ra),"
                + "    MIN(ra)  as minra,"
                + "    min(dec) as mindec,"

                + "    MAX(ra),"
                + "    MAX(ra)  as maxra,"
                + "    max(dec) as maxdec,"

                + "    AVG(ra),"
                + "    AVG(ra)  as avgra,"
                + "    avg(dec) as avgdec,"

                + "    SUM(ra),"
                + "    SUM(ra)  as sumra,"
                + "    sum(dec) as sumdec"

                + " FROM"
                + "    adql_twomass.twomass_psc as twomass"
                + " WHERE"
                + "    ra  BETWEEN '56.0' AND '57.9'"
                + " AND"
                + "    dec BETWEEN '24.0' AND '24.2'"
                + ""
                ),
            new ExpectedColumn[] {

                new ExpectedColumn("COUNT",    AdqlColumn.AdqlType.LONG, 0, "COUNT",    JdbcColumn.JdbcType.BIGINT, 0),
                new ExpectedColumn("countra",  AdqlColumn.AdqlType.LONG, 0, "countra",  JdbcColumn.JdbcType.BIGINT, 0),
                new ExpectedColumn("countdec", AdqlColumn.AdqlType.LONG, 0, "countdec", JdbcColumn.JdbcType.BIGINT, 0),
                new ExpectedColumn("countall", AdqlColumn.AdqlType.LONG, 0, "countall", JdbcColumn.JdbcType.BIGINT, 0),

                new ExpectedColumn("MIN",    AdqlColumn.AdqlType.DOUBLE, 0, "MIN",      JdbcColumn.JdbcType.DOUBLE, 0),
                new ExpectedColumn("minra",  AdqlColumn.AdqlType.DOUBLE, 0, "minra",    JdbcColumn.JdbcType.DOUBLE, 0),
                new ExpectedColumn("mindec", AdqlColumn.AdqlType.DOUBLE, 0, "mindec",   JdbcColumn.JdbcType.DOUBLE, 0),

                new ExpectedColumn("MAX",    AdqlColumn.AdqlType.DOUBLE, 0, "MAX",      JdbcColumn.JdbcType.DOUBLE, 0),
                new ExpectedColumn("maxra",  AdqlColumn.AdqlType.DOUBLE, 0, "maxra",    JdbcColumn.JdbcType.DOUBLE, 0),
                new ExpectedColumn("maxdec", AdqlColumn.AdqlType.DOUBLE, 0, "maxdec",   JdbcColumn.JdbcType.DOUBLE, 0),

                new ExpectedColumn("AVG",    AdqlColumn.AdqlType.DOUBLE, 0, "AVG",      JdbcColumn.JdbcType.DOUBLE, 0),
                new ExpectedColumn("avgra",  AdqlColumn.AdqlType.DOUBLE, 0, "avgra",    JdbcColumn.JdbcType.DOUBLE, 0),
                new ExpectedColumn("avgdec", AdqlColumn.AdqlType.DOUBLE, 0, "avgdec",   JdbcColumn.JdbcType.DOUBLE, 0),

                new ExpectedColumn("SUM",    AdqlColumn.AdqlType.DOUBLE, 0, "SUM",      JdbcColumn.JdbcType.DOUBLE, 0),
                new ExpectedColumn("sumra",  AdqlColumn.AdqlType.DOUBLE, 0, "sumra",    JdbcColumn.JdbcType.DOUBLE, 0),
                new ExpectedColumn("sumdec", AdqlColumn.AdqlType.DOUBLE, 0, "sumdec",   JdbcColumn.JdbcType.DOUBLE, 0),

                }
            );
        }

    /*
    @Test
    public void test005()
    throws Exception
        {
        final ResultSet results = this.twomass.connection().metadata().getTypeInfo();
        while (results.next())
            {
            final Integer size = results.getInt("PRECISION");
            final Integer type = results.getInt("DATA_TYPE");
            final String  name = results.getString("TYPE_NAME");
            log.debug(" Type [{}][{}][{}]", name, type, size);
            }
        }
    */
    }

