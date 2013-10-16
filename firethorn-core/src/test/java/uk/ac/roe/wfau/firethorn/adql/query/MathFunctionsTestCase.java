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
import lombok.extern.slf4j.Slf4j;

import org.junit.Test;

import uk.ac.roe.wfau.firethorn.meta.adql.AdqlColumn;


/**
 *
 *
 */
@Slf4j
public class MathFunctionsTestCase
extends QuerySelectFieldTestBase
    {

    @Test
    public void test001()
    throws Exception
        {
        validate(
            this.schema.queries().create(
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
                new ExpectedField("MAX", AdqlColumn.Type.DOUBLE, 0),
                }
            );
        }

    @Test
    public void test002()
    throws Exception
        {
        validate(
            this.schema.queries().create(
                "SELECT"
                + "   POWER(ra, 2)"
                + " FROM"
                + "    adql_twomass.twomass_psc as twomass"
                + " WHERE"
                + "    ra  BETWEEN '56.0' AND '57.9'"
                + " AND"
                + "    dec BETWEEN '24.0' AND '24.2'"
                + ""
                ),
            new ExpectedField[] {
                new ExpectedField("POWER", AdqlColumn.Type.DOUBLE, 0),
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
                + "   (ra + dec) AS diff"
                + " FROM"
                + "    adql_twomass.twomass_psc as twomass"
                + " WHERE"
                + "    ra  BETWEEN '56.0' AND '57.9'"
                + " AND"
                + "    dec BETWEEN '24.0' AND '24.2'"
                + ""
                ),
            new ExpectedField[] {
                new ExpectedField("diff", AdqlColumn.Type.DOUBLE, 0),
                }
            );
        }

    @Test
    public void test005()
    throws Exception
        {
        final AdqlQuery query = this.schema.queries().create(
            "SELECT"
            + "   ra + dec AS diff"
            + " FROM"
            + "    adql_twomass.twomass_psc as twomass"
            + " WHERE"
            + "    ra  BETWEEN '56.0' AND '57.9'"
            + " AND"
            + "    dec BETWEEN '24.0' AND '24.2'"
            + ""
            );

        log.debug(" ADQL [{}]", query.adql());
        log.debug(" OSQL [{}]", query.osql());

        assertEquals(
            "SELECT twomass.ra+twomass.dec AS diff FROM TWOMASS.dbo.twomass_psc AS twomass WHERE twomass.ra BETWEEN '56.0' AND '57.9' AND twomass.dec BETWEEN '24.0' AND '24.2'",
            query.osql().replace('\n', ' ')
            );
        }

    @Test
    public void test004()
    throws Exception
        {
        validate(
            this.schema.queries().create(
                "SELECT"
                + "   (int1 + int2) AS diff"
                + " FROM"
                + "    adql_twomass.twomass_psc as twomass"
                + " WHERE"
                + "    ra  BETWEEN '56.0' AND '57.9'"
                + " AND"
                + "    dec BETWEEN '24.0' AND '24.2'"
                + ""
                ),
            new ExpectedField[] {
                new ExpectedField("diff", AdqlColumn.Type.INTEGER, 0),
                }
            );
        }

     // int int
     // int float
     // int long
     // int double

// ....

    // double int
    // double float
    // double long
    // double double


    /**
     * This test will fail.
     * No alias on the arithmetic operation.
     * @throws Exception
     *
     */
    @Test
    public void test006()
    throws Exception
        {
        final AdqlQuery query = this.schema.queries().create(
            "SELECT"
            + "   ra + dec"
            + " FROM"
            + "    adql_twomass.twomass_psc as twomass"
            + " WHERE"
            + "    ra  BETWEEN '56.0' AND '57.9'"
            + " AND"
            + "    dec BETWEEN '24.0' AND '24.2'"
            + ""
            );

        log.debug(" ADQL [{}]", query.adql());
        log.debug(" OSQL [{}]", query.osql());

        assertEquals(
            "SELECT twomass.ra+twomass.dec AS col1 FROM TWOMASS.dbo.twomass_psc AS twomass WHERE twomass.ra BETWEEN '56.0' AND '57.9' AND twomass.dec BETWEEN '24.0' AND '24.2'",
            query.osql().replace('\n', ' ')
            );
        }

    @Test
    public void test007()
    throws Exception
        {
        validate(
            this.schema.queries().create(
                "SELECT"
                + "   (ra + dec + 2) AS diff"
                + " FROM"
                + "    adql_twomass.twomass_psc as twomass"
                + " WHERE"
                + "    ra  BETWEEN '56.0' AND '57.9'"
                + " AND"
                + "    dec BETWEEN '24.0' AND '24.2'"
                + ""
                ),
            new ExpectedField[] {
                new ExpectedField("diff", AdqlColumn.Type.DOUBLE, 0),
                }
            );
        }

    @Test
    public void test008()
    throws Exception
        {
        validate(
            this.schema.queries().create(
                "SELECT"
                + "   (htmID * pts_key) AS diff"
                + " FROM"
                + "    adql_twomass.twomass_psc as twomass"
                + " WHERE"
                + "    ra  BETWEEN '56.0' AND '57.9'"
                + " AND"
                + "    dec BETWEEN '24.0' AND '24.2'"
                + ""
                ),
            new ExpectedField[] {
                new ExpectedField("diff", AdqlColumn.Type.LONG, 0),
                }
            );
        }

    @Test
    public void test009()
    throws Exception
        {
        validate(
            this.schema.queries().create(
                "SELECT"
                + "   (err_ang / err_ang) AS diff"
                + " FROM"
                + "    adql_twomass.twomass_psc as twomass"
                + " WHERE"
                + "    ra  BETWEEN '56.0' AND '57.9'"
                + " AND"
                + "    dec BETWEEN '24.0' AND '24.2'"
                + ""
                ),
            new ExpectedField[] {
                new ExpectedField("diff", AdqlColumn.Type.DOUBLE, 0),
                }
            );
        }

    }

