/*
 *  Copyright (C) 2016 Royal Observatory, University of Edinburgh, UK
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

package uk.ac.roe.wfau.firethorn.meta.ivoa;

import org.junit.Test;
import static org.junit.Assert.*;


import uk.ac.roe.wfau.firethorn.adql.query.AbstractQueryTestBase;

/**
 *
 * 
 */
public class IvoaResourceCreateTestCase
extends AbstractQueryTestBase
    {
    @Test
    public void test001()
    throws Exception
        {
        IvoaResource found = ivoaResource(
            "ivoa-001-tag",
            "ivoa-001-name",
            "http://wfaudata.roe.ac.uk/twomass-dsa/TAP"
            ); 
        assertNotNull(
            found
            );
        }

    @Test
    public void test002()
    throws Exception
        {
        IvoaResource created = factories().ivoa().resources().entities().create(
            "ivoa-002-name",
            "http://wfaudata.roe.ac.uk/twomass-dsa/TAP"
            ); 
        assertNotNull(
            created
            );
        }
    }
