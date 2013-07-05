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

import java.util.Iterator;

import lombok.extern.slf4j.Slf4j;

import org.junit.Test;

import uk.ac.roe.wfau.firethorn.meta.adql.AdqlColumn;
import uk.ac.roe.wfau.firethorn.meta.jdbc.JdbcColumn;


/**
 *
 *
 */
@Slf4j
public class QuerySelectFieldTestCase
extends TwomassQueryTestBase
    {

    /**
     * An expected result.
     * 
     */
    public static class ExpectedField
    implements AdqlQuery.SelectField
        {
        public ExpectedField(String name, AdqlColumn.Type type, Integer size)
            {
            this.name = name ;
            this.type = type ;
            this.size = size ;
            }
        private Integer size ;
        @Override
        public Integer arraysize()
            {
            return this.size;
            }
        private AdqlColumn.Type type;
        @Override
        public  AdqlColumn.Type type()
            {
            return this.type;
            }
        private String name ;
        @Override
        public String name()
            {
            return this.name;
            }
        void validate(final AdqlQuery.SelectField field)
            {
            log.debug("check()");
            log.debug("  name [{}][{}]", this.name, field.name());
            log.debug("  size [{}][{}]", this.size, field.arraysize());
            log.debug("  type [{}][{}]", this.type, field.type());
            assertEquals(
                this.name,
                field.name()
                ) ;
            assertEquals(
                this.size,
                field.arraysize()
                ) ;
            assertEquals(
                this.type,
                field.type()
                ) ;
            }
        @Override
        public AdqlColumn adql()
            {
            return null;
            }
        @Override
        public JdbcColumn jdbc()
            {
            return null;
            }
        }

    public void validate(AdqlQuery query, final ExpectedField[] results)
    throws Exception
        {
        Iterator<AdqlQuery.SelectField> iter = query.fields().iterator();
        int i = 0 ;
        while (iter.hasNext())
            {
            AdqlQuery.SelectField field = iter.next();
            log.debug("Field [{}][{}][{}]", field.name(), field.type(), field.arraysize());
            results[i++].validate(
                field
                );
            }
        assertEquals(
            i,
            results.length
            );
        }

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

    public static final ExpectedField[] RESULTS_000 = {
        new ExpectedField("cx",               AdqlColumn.Type.DOUBLE,     0),
        new ExpectedField("cy",               AdqlColumn.Type.DOUBLE,     0), 
        new ExpectedField("cz",               AdqlColumn.Type.DOUBLE,     0), 
        new ExpectedField("htmID",            AdqlColumn.Type.LONG,       0), 
        new ExpectedField("ra",               AdqlColumn.Type.DOUBLE,     0), 
        new ExpectedField("dec",              AdqlColumn.Type.DOUBLE,     0), 
        new ExpectedField("err_maj",          AdqlColumn.Type.FLOAT,      0), 
        new ExpectedField("err_min",          AdqlColumn.Type.FLOAT,      0),
        new ExpectedField("err_ang",          AdqlColumn.Type.SHORT,      0),
        new ExpectedField("designation",      AdqlColumn.Type.CHAR,      17),
        new ExpectedField("j_m",              AdqlColumn.Type.FLOAT,      0),
        new ExpectedField("j_cmsig",          AdqlColumn.Type.FLOAT,      0),
        new ExpectedField("j_msigcom",        AdqlColumn.Type.FLOAT,      0),
        new ExpectedField("j_snr",            AdqlColumn.Type.FLOAT,      0),
        new ExpectedField("h_m",              AdqlColumn.Type.FLOAT,      0),
        new ExpectedField("h_cmsig",          AdqlColumn.Type.FLOAT,      0),
        new ExpectedField("h_msigcom",        AdqlColumn.Type.FLOAT,      0),
        new ExpectedField("h_snr",            AdqlColumn.Type.FLOAT,      0),
        new ExpectedField("k_m",              AdqlColumn.Type.FLOAT,      0),
        new ExpectedField("k_cmsig",          AdqlColumn.Type.FLOAT,      0),
        new ExpectedField("k_msigcom",        AdqlColumn.Type.FLOAT,      0),
        new ExpectedField("k_snr",            AdqlColumn.Type.FLOAT,      0),
        new ExpectedField("ph_qual",          AdqlColumn.Type.CHAR,       3),
        new ExpectedField("rd_flg",           AdqlColumn.Type.CHAR,       3),
        new ExpectedField("bl_flg",           AdqlColumn.Type.CHAR,       3),
        new ExpectedField("cc_flg",           AdqlColumn.Type.CHAR,       3),
        new ExpectedField("ndet",             AdqlColumn.Type.CHAR,       6),
        new ExpectedField("prox",             AdqlColumn.Type.FLOAT,      0),
        new ExpectedField("pxpa",             AdqlColumn.Type.SHORT,      0),
        new ExpectedField("pxcntr",           AdqlColumn.Type.INTEGER,    0),
        new ExpectedField("gal_contam",       AdqlColumn.Type.SHORT,      0),
        new ExpectedField("mp_flg",           AdqlColumn.Type.SHORT,      0),
        new ExpectedField("pts_key",          AdqlColumn.Type.INTEGER,    0),
        new ExpectedField("hemis",            AdqlColumn.Type.CHAR,       1),
        new ExpectedField("date",             AdqlColumn.Type.TIMESTAMP, 23),
        new ExpectedField("scan",             AdqlColumn.Type.SHORT,      0),
        new ExpectedField("glon",             AdqlColumn.Type.FLOAT,      0),
        new ExpectedField("glat",             AdqlColumn.Type.FLOAT,      0),
        new ExpectedField("x_scan",           AdqlColumn.Type.FLOAT,      0),
        new ExpectedField("jdate",            AdqlColumn.Type.DOUBLE,     0),
        new ExpectedField("j_psfchi",         AdqlColumn.Type.FLOAT,      0),
        new ExpectedField("h_psfchi",         AdqlColumn.Type.FLOAT,      0),
        new ExpectedField("k_psfchi",         AdqlColumn.Type.FLOAT,      0),
        new ExpectedField("j_m_stdap",        AdqlColumn.Type.FLOAT,      0),
        new ExpectedField("j_msig_stdap",     AdqlColumn.Type.FLOAT,      0),
        new ExpectedField("h_m_stdap",        AdqlColumn.Type.FLOAT,      0),
        new ExpectedField("h_msig_stdap",     AdqlColumn.Type.FLOAT,      0),
        new ExpectedField("k_m_stdap",        AdqlColumn.Type.FLOAT,      0),
        new ExpectedField("k_msig_stdap",     AdqlColumn.Type.FLOAT,      0),
        new ExpectedField("dist_edge_ns",     AdqlColumn.Type.INTEGER,    0),
        new ExpectedField("dist_edge_ew",     AdqlColumn.Type.INTEGER,    0),
        new ExpectedField("dist_edge_flg",    AdqlColumn.Type.CHAR,       2),
        new ExpectedField("dup_src",          AdqlColumn.Type.SHORT,      0),
        new ExpectedField("use_src",          AdqlColumn.Type.SHORT,      0),
        new ExpectedField("a",                AdqlColumn.Type.CHAR,       1),
        new ExpectedField("dist_opt",         AdqlColumn.Type.FLOAT,      0),
        new ExpectedField("phi_opt",          AdqlColumn.Type.SHORT,      0),
        new ExpectedField("b_m_opt",          AdqlColumn.Type.FLOAT,      0),
        new ExpectedField("vr_m_opt",         AdqlColumn.Type.FLOAT,      0),
        new ExpectedField("nopt_mchs",        AdqlColumn.Type.SHORT,      0),
        new ExpectedField("ext_key",          AdqlColumn.Type.INTEGER,    0),
        new ExpectedField("scan_key",         AdqlColumn.Type.INTEGER,    0),
        new ExpectedField("coadd_key",        AdqlColumn.Type.INTEGER,    0),
        new ExpectedField("coadd",            AdqlColumn.Type.SHORT,      0)
        }; 

    @Test
    public void test000()
    throws Exception
        {
        validate(
            this.schema.queries().create(
                QUERY_000
                ),
            RESULTS_000                
            );
        }
    
    private static final String QUERY_001 =
        "SELECT"
      + "    date"
      + " FROM"
      + "    adql_twomass.twomass_psc as twomass"
      + " WHERE"
      + "    ra  BETWEEN '56.0' AND '57.9'"
      + " AND"
      + "    dec BETWEEN '24.0' AND '24.2'"
      + ""
      ;

    public static final ExpectedField[] RESULTS_001 = {
        new ExpectedField("date", AdqlColumn.Type.TIMESTAMP, 23),
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
      + " FROM"
      + "    adql_twomass.twomass_psc as twomass"
      + " WHERE"
      + "    ra  BETWEEN '56.0' AND '57.9'"
      + " AND"
      + "    dec BETWEEN '24.0' AND '24.2'"
      + ""
      ;
    
    public static final ExpectedField[] RESULTS_002 = {
        new ExpectedField("MAX", AdqlColumn.Type.DOUBLE, 0),
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

