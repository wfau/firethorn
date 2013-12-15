/*
 *  Copyright (C) 2013 Royal Observatory, University of Edinburgh, UK
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
package uk.ac.roe.wfau.firethorn.adql.query.atlas;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.springframework.transaction.annotation.Transactional;

import uk.ac.roe.wfau.firethorn.adql.query.AdqlQuery;
import uk.ac.roe.wfau.firethorn.adql.query.AdqlQuery.Syntax.Level;
import uk.ac.roe.wfau.firethorn.entity.Identifier;
import uk.ac.roe.wfau.firethorn.entity.annotation.UpdateAtomicMethod;
import uk.ac.roe.wfau.firethorn.job.Job;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlColumn;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlTable;
import uk.ac.roe.wfau.firethorn.meta.jdbc.JdbcTable;


/**
 *
 *
 */
public class QueryResultsTestCase
    extends AtlasQueryTestBase
    {
    @Test
    public void test000()
    throws Exception
        {
        final AdqlQuery query = this.queryspace.queries().create(
            factories().queries().params().param(
                Level.STRICT
                ),
            "SELECT\n" + 
            "    ra,\n" + 
            "    dec\n" + 
            "FROM\n" + 
            "    twomass_psc\n" + 
            "WHERE\n" + 
            "    ra  BETWEEN '56.0' AND '57.9'\n" + 
            "AND\n" + 
            "    dec BETWEEN '24.0' AND '24.2'\n" + 
            "" 
            );
        assertEquals(
            AdqlQuery.Syntax.State.VALID,
            query.syntax().state()
            );
        validate(
            query,
            new ExpectedField[] {
                new ExpectedField("ra",  AdqlColumn.Type.DOUBLE, 0),
                new ExpectedField("dec", AdqlColumn.Type.DOUBLE, 0),
                }
            );
        compare(
            query,
            "select twomass.dbo.twomass_psc.ra as ra, twomass.dbo.twomass_psc.dec as dec from twomass.dbo.twomass_psc where twomass.dbo.twomass_psc.ra between '56.0' and '57.9' and twomass.dbo.twomass_psc.dec between '24.0' and '24.2'"
            );

        assertEquals(
            AdqlTable.AdqlStatus.CREATED,
            query.results().adql().meta().adql().status() 
            );

        assertEquals(
            AdqlTable.AdqlStatus.CREATED,
            query.results().jdbc().meta().adql().status() 
            );
        assertEquals(
            JdbcTable.JdbcStatus.CREATED,
            query.results().jdbc().meta().jdbc().status() 
            );

        //
        // This hangs in what looks like a database lock conflict. 
        factories().queries().executor().update(
            query.ident(),
            Job.Status.RUNNING,
            10
            );

        }
    }
