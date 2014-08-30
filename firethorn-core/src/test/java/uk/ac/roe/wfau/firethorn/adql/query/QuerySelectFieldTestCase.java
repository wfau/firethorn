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

import org.junit.Test;

import uk.ac.roe.wfau.firethorn.meta.adql.AdqlColumn;

/**
 *
 *
 */
public class QuerySelectFieldTestCase
extends QuerySelectFieldTestBase
    {

    @Test
    public void test000()
    throws Exception
        {
        validate(
            this.schema.queries().create(
                factories().adql().queries().params().param(),
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
            new ExpectedField[] {
                }
            );
        }

    @Test
    public void test001()
    throws Exception
        {
        validate(
            this.schema.queries().create(
                factories().adql().queries().params().param(),
                "SELECT"
                + "    date"
                + " FROM"
                + "    adql_twomass.twomass_psc as twomass"
                + " WHERE"
                + "    ra  BETWEEN '56.0' AND '57.9'"
                + " AND"
                + "    dec BETWEEN '24.0' AND '24.2'"
                + ""
                ),
            new ExpectedField[] {
                new ExpectedField("date", AdqlColumn.AdqlType.DATETIME, 23),
                }
            );
        }

    @Test
    public void test002()
    throws Exception
        {
        validate(
            this.schema.queries().create(
                factories().adql().queries().params().param(),
                "SELECT"
                + "    MAX(ra)"
                + " FROM"
                + "    adql_twomass.twomass_psc as twomass"
                + " WHERE"
                + "    ra  BETWEEN '56.0' AND '57.9'"
                + " AND"
                + "    dec BETWEEN '24.0' AND '24.2'"
                + ""
                ),
            new ExpectedField[] {
                new ExpectedField("MAX", AdqlColumn.AdqlType.DOUBLE, 0),
                }
            );
        }

    @Test
    public void test003()
    throws Exception
      {
      validate(
          this.schema.queries().create(
              factories().adql().queries().params().param(),
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
          new ExpectedField[] {
              new ExpectedField("cx",               AdqlColumn.AdqlType.DOUBLE,     0),
              new ExpectedField("cy",               AdqlColumn.AdqlType.DOUBLE,     0),
              new ExpectedField("cz",               AdqlColumn.AdqlType.DOUBLE,     0),
              new ExpectedField("htmID",            AdqlColumn.AdqlType.LONG,       0),
              new ExpectedField("ra",               AdqlColumn.AdqlType.DOUBLE,     0),
              new ExpectedField("dec",              AdqlColumn.AdqlType.DOUBLE,     0),
              new ExpectedField("err_maj",          AdqlColumn.AdqlType.FLOAT,      0),
              new ExpectedField("err_min",          AdqlColumn.AdqlType.FLOAT,      0),
              new ExpectedField("err_ang",          AdqlColumn.AdqlType.SHORT,      0),
              new ExpectedField("designation",      AdqlColumn.AdqlType.CHAR,      17),
              new ExpectedField("j_m",              AdqlColumn.AdqlType.FLOAT,      0),
              new ExpectedField("j_cmsig",          AdqlColumn.AdqlType.FLOAT,      0),
              new ExpectedField("j_msigcom",        AdqlColumn.AdqlType.FLOAT,      0),
              new ExpectedField("j_snr",            AdqlColumn.AdqlType.FLOAT,      0),
              new ExpectedField("h_m",              AdqlColumn.AdqlType.FLOAT,      0),
              new ExpectedField("h_cmsig",          AdqlColumn.AdqlType.FLOAT,      0),
              new ExpectedField("h_msigcom",        AdqlColumn.AdqlType.FLOAT,      0),
              new ExpectedField("h_snr",            AdqlColumn.AdqlType.FLOAT,      0),
              new ExpectedField("k_m",              AdqlColumn.AdqlType.FLOAT,      0),
              new ExpectedField("k_cmsig",          AdqlColumn.AdqlType.FLOAT,      0),
              new ExpectedField("k_msigcom",        AdqlColumn.AdqlType.FLOAT,      0),
              new ExpectedField("k_snr",            AdqlColumn.AdqlType.FLOAT,      0),
              new ExpectedField("ph_qual",          AdqlColumn.AdqlType.CHAR,       3),
              new ExpectedField("rd_flg",           AdqlColumn.AdqlType.CHAR,       3),
              new ExpectedField("bl_flg",           AdqlColumn.AdqlType.CHAR,       3),
              new ExpectedField("cc_flg",           AdqlColumn.AdqlType.CHAR,       3),
              new ExpectedField("ndet",             AdqlColumn.AdqlType.CHAR,       6),
              new ExpectedField("prox",             AdqlColumn.AdqlType.FLOAT,      0),
              new ExpectedField("pxpa",             AdqlColumn.AdqlType.SHORT,      0),
              new ExpectedField("pxcntr",           AdqlColumn.AdqlType.INTEGER,    0),
              new ExpectedField("gal_contam",       AdqlColumn.AdqlType.SHORT,      0),
              new ExpectedField("mp_flg",           AdqlColumn.AdqlType.SHORT,      0),
              new ExpectedField("pts_key",          AdqlColumn.AdqlType.INTEGER,    0),
              new ExpectedField("hemis",            AdqlColumn.AdqlType.CHAR,       1),
              new ExpectedField("date",             AdqlColumn.AdqlType.DATETIME,   0),
              new ExpectedField("scan",             AdqlColumn.AdqlType.SHORT,      0),
              new ExpectedField("glon",             AdqlColumn.AdqlType.FLOAT,      0),
              new ExpectedField("glat",             AdqlColumn.AdqlType.FLOAT,      0),
              new ExpectedField("x_scan",           AdqlColumn.AdqlType.FLOAT,      0),
              new ExpectedField("jdate",            AdqlColumn.AdqlType.DOUBLE,     0),
              new ExpectedField("j_psfchi",         AdqlColumn.AdqlType.FLOAT,      0),
              new ExpectedField("h_psfchi",         AdqlColumn.AdqlType.FLOAT,      0),
              new ExpectedField("k_psfchi",         AdqlColumn.AdqlType.FLOAT,      0),
              new ExpectedField("j_m_stdap",        AdqlColumn.AdqlType.FLOAT,      0),
              new ExpectedField("j_msig_stdap",     AdqlColumn.AdqlType.FLOAT,      0),
              new ExpectedField("h_m_stdap",        AdqlColumn.AdqlType.FLOAT,      0),
              new ExpectedField("h_msig_stdap",     AdqlColumn.AdqlType.FLOAT,      0),
              new ExpectedField("k_m_stdap",        AdqlColumn.AdqlType.FLOAT,      0),
              new ExpectedField("k_msig_stdap",     AdqlColumn.AdqlType.FLOAT,      0),
              new ExpectedField("dist_edge_ns",     AdqlColumn.AdqlType.INTEGER,    0),
              new ExpectedField("dist_edge_ew",     AdqlColumn.AdqlType.INTEGER,    0),
              new ExpectedField("dist_edge_flg",    AdqlColumn.AdqlType.CHAR,       2),
              new ExpectedField("dup_src",          AdqlColumn.AdqlType.SHORT,      0),
              new ExpectedField("use_src",          AdqlColumn.AdqlType.SHORT,      0),
              new ExpectedField("a",                AdqlColumn.AdqlType.CHAR,       1),
              new ExpectedField("dist_opt",         AdqlColumn.AdqlType.FLOAT,      0),
              new ExpectedField("phi_opt",          AdqlColumn.AdqlType.SHORT,      0),
              new ExpectedField("b_m_opt",          AdqlColumn.AdqlType.FLOAT,      0),
              new ExpectedField("vr_m_opt",         AdqlColumn.AdqlType.FLOAT,      0),
              new ExpectedField("nopt_mchs",        AdqlColumn.AdqlType.SHORT,      0),
              new ExpectedField("ext_key",          AdqlColumn.AdqlType.INTEGER,    0),
              new ExpectedField("scan_key",         AdqlColumn.AdqlType.INTEGER,    0),
              new ExpectedField("coadd_key",        AdqlColumn.AdqlType.INTEGER,    0),
              new ExpectedField("coadd",            AdqlColumn.AdqlType.SHORT,      0)
              }
          );
      }



    @Test
    public void test004()
    throws Exception
        {
        validate(
            this.schema.queries().create(
                factories().adql().queries().params().param(),
                "SELECT"
                + "    ra +  dec as radec"
                + " FROM"
                + "    adql_twomass.twomass_psc as twomass"
                + " WHERE"
                + "    ra  BETWEEN '56.0' AND '57.9'"
                + " AND"
                + "    dec BETWEEN '24.0' AND '24.2'"
                + ""
                ),
            new ExpectedField[] {
                new ExpectedField("radec", AdqlColumn.AdqlType.DOUBLE, 0),
                }
            );
        }

    @Test
    public void test005()
    throws Exception
        {
        validate(
            this.schema.queries().create(
                factories().adql().queries().params().param(),
                "SELECT"
                + "    ra +  dec"
                + " FROM"
                + "    adql_twomass.twomass_psc as twomass"
                + " WHERE"
                + "    ra  BETWEEN '56.0' AND '57.9'"
                + " AND"
                + "    dec BETWEEN '24.0' AND '24.2'"
                + ""
                ),
            new ExpectedField[] {
                new ExpectedField("SUM", AdqlColumn.AdqlType.DOUBLE, 0),
                }
            );
        }
    }

