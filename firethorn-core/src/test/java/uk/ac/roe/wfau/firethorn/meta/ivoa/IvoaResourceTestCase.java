/*
 *  Copyright (C) 2014 Royal Observatory, University of Edinburgh, UK
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

import java.io.FileReader;
import java.net.URL;

import lombok.extern.slf4j.Slf4j;

import org.junit.Before;
import org.junit.Test;

import uk.ac.roe.wfau.firethorn.adql.query.AbstractQueryTestBase;
import uk.ac.roe.wfau.firethorn.adql.query.AbstractQueryTestBase.ExpectedField;
import uk.ac.roe.wfau.firethorn.adql.query.AdqlQuery.Syntax.Level;
import uk.ac.roe.wfau.firethorn.adql.query.AdqlQuery.Syntax.State;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlColumn;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlResource;
import uk.ac.roe.wfau.firethorn.meta.ivoa.IvoaResource;
import uk.ac.roe.wfau.firethorn.meta.jdbc.JdbcResource;
import uk.ac.roe.wfau.firethorn.meta.vosi.VosiTableSetReader;
import uk.ac.roe.wfau.firethorn.test.TestBase;

/**
 *
 *
 */
@Slf4j
public class IvoaResourceTestCase
extends AbstractQueryTestBase
    {

    
    @Test
    public void test001()
    throws Exception
        {
        IvoaResource gaia = ivoaResource(
            "ivoa.gaia.resource"
            );

        if (gaia == null)
            {
            log.debug("IvoaResource not loaded, creating a new one");
            gaia = factories().ivoa().resources().create(
                "gaia-tap",
                "gaia-tap"
                );
            ivoaResource(
                "ivoa.gaia.resource",
                gaia
                );
            
            log.debug("Resource [{}][{}]", gaia.name(), gaia.ident());
            log.debug("Reading  [gaia-tableset.xml]");
    
            VosiTableSetReader reader = new VosiTableSetReader(); 
            reader.inport(
                new FileReader(
                    "src/test/data/vosi/original/gaia-tableset.xml"
                    ),
                gaia
                );
            }
        
        AdqlResource adql = adqlResource(
            "adql.gaia.resource",
            "adql.gaia.resource"
            );

        testSchema(adql, gaia, "gaia", "public");
        
        validate(
            Level.STRICT,
            State.VALID,

            " SELECT" + 
            "     TOP 123" + 
            "     ra," + 
            "     decl" + 
            " FROM" + 
            "    gaia.twomass_psc",

            " SELECT" + 
            "     TOP 123" + 
            "     public.twomass_psc.ra   as ra," + 
            "     public.twomass_psc.decl as decl" + 
            " FROM" + 
            "    public.twomass_psc",

            new ExpectedField[] {
                new ExpectedField("ra",   AdqlColumn.AdqlType.DOUBLE, 0),
                new ExpectedField("decl", AdqlColumn.AdqlType.DOUBLE, 0)
                }
            );
        }
    }
