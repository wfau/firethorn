/*
 *  Copyright (C) 2015 Royal Observatory, University of Edinburgh, UK
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
package uk.ac.roe.wfau.firethorn.ogsa.query;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import uk.ac.roe.wfau.firethorn.adql.query.AbstractQueryTestBase;
import uk.ac.roe.wfau.firethorn.adql.query.AdqlQueryBase;
import uk.ac.roe.wfau.firethorn.adql.query.blue.BlueQuery;
import uk.ac.roe.wfau.firethorn.adql.query.green.GreenJob;
import uk.ac.roe.wfau.firethorn.adql.query.green.GreenQuery;
import uk.ac.roe.wfau.firethorn.entity.annotation.CreateAtomicMethod;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlResource;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlSchema;
import uk.ac.roe.wfau.firethorn.meta.jdbc.JdbcResource;

/**
 * These tests fail due to problems with transaction boundaries.
 * http://redmine.roe.ac.uk/issues/779
 * Need to make the create activities Atomic.
 *
 */
public class CombinedQueryTestCase
    extends AbstractQueryTestBase
    {

    protected static final String ATLAS_VERSION = "ATLASDR1";

    TestConfig config = config();

    JdbcResource jdbc ;
    AdqlResource adql ;
    AdqlSchema schema ;
    BlueQuery  query  ;
    
    @CreateAtomicMethod
    public void prepare()
    throws Exception
        {
        jdbc = jdbcResource(
            "albert.atlas.jdbc", 
            ATLAS_VERSION, 
            "Atlas JDBC", 
            config.property("firethorn.atlas.url"),            
            config.property("firethorn.atlas.user"),            
            config.property("firethorn.atlas.pass"),            
            config.property("firethorn.atlas.driver")            
            );
        assertNotNull(
            jdbc
            );
        
        adql = adqlResource(
            "albert.atlas.adql", 
            "Atlas ADQL" 
            );
        assertNotNull(
            adql
            );

        schema = testSchema(
            adql,
            jdbc,
            ATLAS_VERSION,
            ATLAS_VERSION,
            "dbo"
            );
        assertNotNull(
            schema
            );
        
        query = schema.resource().blues().create(
            "SELECT COUNT(ra) FROM atlasSource WHERE ra BETWEEN 80.0 AND 80.1"
            );
        assertNotNull(
            query
            );
        }

    @Test
    public void testOne()
        throws Exception
        {
        prepare();

        factories().greens().executor().update(
            query.ident(),
            GreenJob.Status.RUNNING,
            10
            );
        }
    }
