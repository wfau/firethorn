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
public class ColumnTypeTestCase
extends TwomassQueryTestBase
    {

    /**
     * Expected results.
     * 
     */
    public static class Result
    implements AdqlQuery.SelectField
        {
        public Result(String name, AdqlColumn.Type type, Integer size)
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
        boolean equals(AdqlQuery.SelectField field)
            {
            boolean result = true ;
            log.debug("equals()");
            log.debug("  name [{}][{}]", this.name, field.name());
            log.debug("  size [{}][{}]", this.size, field.arraysize());
            log.debug("  type [{}][{}]", this.type, field.type());
            
            if ((this.name != null) && (field.name() != null))
                {
                result &= this.name.equals(field.name()) ;
                }
            else if ((this.name != null) && (field.name() == null))
                {
                result = false ;
                }
            else if ((this.name == null) && (field.name() != null))
                {
                result = false ;
                }

            if ((this.size != null) && (field.arraysize() != null))
                {
                result &= this.size.equals(field.arraysize()) ;
                }
            else if ((this.size != null) && (field.arraysize() == null))
                {
                result = false ;
                }
            else if ((this.size == null) && (field.arraysize() != null))
                {
                result = false ;
                }
            
            if ((this.type != null) && (field.type() != null))
                {
                result &= this.type.equals(field.type()) ;
                }
            else if ((this.type != null) && (field.type() == null))
                {
                result = false ;
                }
            else if ((this.type == null) && (field.type() != null))
                {
                result = false ;
                }
            return result ;
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

    public void validate(AdqlQuery query, final Result[] results)
    throws Exception
        {

        log.debug("SQL [{}]", query.osql());

        Iterator<AdqlQuery.SelectField> iter = query.fields().iterator();
        int i = 0 ;
        while (iter.hasNext())
            {
            AdqlQuery.SelectField field = iter.next();
            log.debug("Field [{}][{}][{}]", field.name(), field.type(), field.arraysize());
            assertTrue(
                results[i++].equals(field)
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

    public static final Result[] RESULTS_000 = {
        new Result("cx",               AdqlColumn.Type.DOUBLE,     0),
        new Result("cy",               AdqlColumn.Type.DOUBLE,     0), 
        new Result("cz",               AdqlColumn.Type.DOUBLE,     0), 
        new Result("htmID",            AdqlColumn.Type.LONG,       0), 
        new Result("ra",               AdqlColumn.Type.DOUBLE,     0), 
        new Result("dec",              AdqlColumn.Type.DOUBLE,     0), 
        new Result("err_maj",          AdqlColumn.Type.FLOAT,      0), 
        new Result("err_min",          AdqlColumn.Type.FLOAT,      0),
        new Result("err_ang",          AdqlColumn.Type.SHORT,      0),
        new Result("designation",      AdqlColumn.Type.CHAR,      17),
        new Result("j_m",              AdqlColumn.Type.FLOAT,      0),
        new Result("j_cmsig",          AdqlColumn.Type.FLOAT,      0),
        new Result("j_msigcom",        AdqlColumn.Type.FLOAT,      0),
        new Result("j_snr",            AdqlColumn.Type.FLOAT,      0),
        new Result("h_m",              AdqlColumn.Type.FLOAT,      0),
        new Result("h_cmsig",          AdqlColumn.Type.FLOAT,      0),
        new Result("h_msigcom",        AdqlColumn.Type.FLOAT,      0),
        new Result("h_snr",            AdqlColumn.Type.FLOAT,      0),
        new Result("k_m",              AdqlColumn.Type.FLOAT,      0),
        new Result("k_cmsig",          AdqlColumn.Type.FLOAT,      0),
        new Result("k_msigcom",        AdqlColumn.Type.FLOAT,      0),
        new Result("k_snr",            AdqlColumn.Type.FLOAT,      0),
        new Result("ph_qual",          AdqlColumn.Type.CHAR,       3),
        new Result("rd_flg",           AdqlColumn.Type.CHAR,       3),
        new Result("bl_flg",           AdqlColumn.Type.CHAR,       3),
        new Result("cc_flg",           AdqlColumn.Type.CHAR,       3),
        new Result("ndet",             AdqlColumn.Type.CHAR,       6),
        new Result("prox",             AdqlColumn.Type.FLOAT,      0),
        new Result("pxpa",             AdqlColumn.Type.SHORT,      0),
        new Result("pxcntr",           AdqlColumn.Type.INTEGER,    0),
        new Result("gal_contam",       AdqlColumn.Type.SHORT,      0),
        new Result("mp_flg",           AdqlColumn.Type.SHORT,      0),
        new Result("pts_key",          AdqlColumn.Type.INTEGER,    0),
        new Result("hemis",            AdqlColumn.Type.CHAR,       1),
        new Result("date",             AdqlColumn.Type.TIMESTAMP, 23),
        new Result("scan",             AdqlColumn.Type.SHORT,      0),
        new Result("glon",             AdqlColumn.Type.FLOAT,      0),
        new Result("glat",             AdqlColumn.Type.FLOAT,      0),
        new Result("x_scan",           AdqlColumn.Type.FLOAT,      0),
        new Result("jdate",            AdqlColumn.Type.DOUBLE,     0),
        new Result("j_psfchi",         AdqlColumn.Type.FLOAT,      0),
        new Result("h_psfchi",         AdqlColumn.Type.FLOAT,      0),
        new Result("k_psfchi",         AdqlColumn.Type.FLOAT,      0),
        new Result("j_m_stdap",        AdqlColumn.Type.FLOAT,      0),
        new Result("j_msig_stdap",     AdqlColumn.Type.FLOAT,      0),
        new Result("h_m_stdap",        AdqlColumn.Type.FLOAT,      0),
        new Result("h_msig_stdap",     AdqlColumn.Type.FLOAT,      0),
        new Result("k_m_stdap",        AdqlColumn.Type.FLOAT,      0),
        new Result("k_msig_stdap",     AdqlColumn.Type.FLOAT,      0),
        new Result("dist_edge_ns",     AdqlColumn.Type.INTEGER,    0),
        new Result("dist_edge_ew",     AdqlColumn.Type.INTEGER,    0),
        new Result("dist_edge_flg",    AdqlColumn.Type.CHAR,       2),
        new Result("dup_src",          AdqlColumn.Type.SHORT,      0),
        new Result("use_src",          AdqlColumn.Type.SHORT,      0),
        new Result("a",                AdqlColumn.Type.CHAR,       1),
        new Result("dist_opt",         AdqlColumn.Type.FLOAT,      0),
        new Result("phi_opt",          AdqlColumn.Type.SHORT,      0),
        new Result("b_m_opt",          AdqlColumn.Type.FLOAT,      0),
        new Result("vr_m_opt",         AdqlColumn.Type.FLOAT,      0),
        new Result("nopt_mchs",        AdqlColumn.Type.SHORT,      0),
        new Result("ext_key",          AdqlColumn.Type.INTEGER,    0),
        new Result("scan_key",         AdqlColumn.Type.INTEGER,    0),
        new Result("coadd_key",        AdqlColumn.Type.INTEGER,    0),
        new Result("coadd",            AdqlColumn.Type.SHORT,      0)
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

    public static final Result[] RESULTS_001 = {
        new Result("date", AdqlColumn.Type.TIMESTAMP, 23),
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
    }

