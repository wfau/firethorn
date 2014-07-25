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
package uk.ac.roe.wfau.firethorn.adql.query.vizier ;

import java.io.FileReader;

import lombok.extern.slf4j.Slf4j;

import org.junit.Before;
import org.junit.Ignore;

import uk.ac.roe.wfau.firethorn.adql.query.AbstractQueryTestBase;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlResource;
import uk.ac.roe.wfau.firethorn.meta.ivoa.IvoaResource;
import uk.ac.roe.wfau.firethorn.meta.jdbc.JdbcResource;
import uk.ac.roe.wfau.firethorn.meta.vosi.VosiTableSetReader;

/**
 *
 *
 */
@Slf4j
@Ignore
public abstract class VizierQueryTestBase
extends AbstractQueryTestBase
    {

    /**
     * Load our resources.
     *
     */
    @Before
    public void loadVizierResources()
    throws Exception
        {
        IvoaResource ivoaspace = factories().ivoa().resources().create(
                "urn:vizier.ivoa.resource",
                "urn:vizier.ivoa.resource"
                );
        VosiTableSetReader reader = new VosiTableSetReader(); 
/*
 * 
        log.debug("-- Reading [vizier-tableset.xml] --");
        reader.inport(
            new FileReader(
                "src/test/data/vosi/original/vizier-tableset.xml"
                ),
            resource
            );
 *
 */            
        log.debug("-- Reading [vizier-twomass.xml] --");
        reader.inport(
            new FileReader(
                "src/test/data/vosi/original/vizier-twomass.xml"
                ),
            ivoaspace
            );

        AdqlResource adqlspace = adqlResource(
            "urn:vizier.adql.resource",
            "urn:vizier.adql.resource"
            );

        testSchema(adqlspace, ivoaspace, "vizls");

        }
    }
