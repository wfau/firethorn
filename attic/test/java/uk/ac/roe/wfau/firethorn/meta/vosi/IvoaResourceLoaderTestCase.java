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
package uk.ac.roe.wfau.firethorn.meta.vosi;

import java.io.FileReader;

import org.junit.Test;

import lombok.extern.slf4j.Slf4j;
import uk.ac.roe.wfau.firethorn.meta.ivoa.IvoaResource;
import uk.ac.roe.wfau.firethorn.meta.ivoa.IvoaSchema;
import uk.ac.roe.wfau.firethorn.meta.ogsa.OgsaIvoaResource;
import uk.ac.roe.wfau.firethorn.test.TestBase;

/**
 *
 *
 */
@Slf4j
public class IvoaResourceLoaderTestCase
    extends TestBase
    {
    @Test
    public void test001()
    throws Exception
        {
        final VosiTableSetReader reader = new VosiTableSetReader(); 

        final IvoaResource cadc = factories().ivoa().resources().entities().create(
            "cadc-tap",
            "cadc-tap"
            );
        log.debug("Resource [{}][{}]", cadc.name(), cadc.ident());
        log.debug("Reading  [cadc-tableset.xml]");
        reader.inport(
            new FileReader(
                "src/test/data/vosi/original/cadc-tableset.xml"
                ),
            cadc
            );
        cadc.ogsa().primary().ogsaid(
    		OgsaIvoaResource.OgsaStatus.ACTIVE,
    		"cadc-tap"
    		);

        IvoaResource gaia = factories().ivoa().resources().entities().create(
            "gaia-tap",
            "gaia-tap"
            );
        log.debug("Resource [{}][{}]", gaia.name(), gaia.ident());
        log.debug("Reading  [gaia-tableset.xml]");
        reader.inport(
            new FileReader(
                "src/test/data/vosi/modified/gaia-tableset.xml"
                ),
            gaia
            );
        gaia.ogsa().primary().ogsaid(
    		OgsaIvoaResource.OgsaStatus.ACTIVE,
    		"gaia-tap"
    		);

        IvoaResource gavo = factories().ivoa().resources().entities().create(
            "gavo-tap",
            "gavo-tap"
            );
        log.debug("Resource [{}][{}]", gavo.name(), gavo.ident());
        log.debug("Reading  [gavo-tableset.xml]");
        reader.inport(
            new FileReader(
                "src/test/data/vosi/original/gavo-tableset.xml"
                ),
            gavo
            );
        gavo.ogsa().primary().ogsaid(
    		OgsaIvoaResource.OgsaStatus.ACTIVE,
    		"gavo-tap"
    		);

        IvoaResource vizier = factories().ivoa().resources().entities().create(
            "vizier-tap",
            "vizier-tap"
            );
        log.debug("Resource [{}][{}]", vizier.name(), vizier.ident());
        log.debug("Reading  [vizier-tableset-sample.xml]");
        reader.inport(
            new FileReader(
                "src/test/data/vosi/original/vizier-tableset-sample.xml"
                ),
            vizier
            );
        vizier.ogsa().primary().ogsaid(
    		OgsaIvoaResource.OgsaStatus.ACTIVE,
    		"vizier-tap"
    		);

        debug(cadc);
        debug(gaia);
        debug(gavo);
        debug(vizier);
        
        }

    public void debug(final IvoaResource resource)
        {
        log.debug("Resource [{}][{}]", resource.name(), resource.ident());
        for (IvoaSchema schema : resource.schemas().select())
            {
            log.debug("  Schema [{}][{}]", schema.fullname(), schema.ident());
            }
        }
    }
