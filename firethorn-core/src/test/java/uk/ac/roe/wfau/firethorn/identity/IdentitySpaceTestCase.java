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
package uk.ac.roe.wfau.firethorn.identity;

import static org.junit.Assert.*;
import lombok.extern.slf4j.Slf4j;

import org.junit.Test;

import uk.ac.roe.wfau.firethorn.test.TestBase;

/**
 *
 *
 */
@Slf4j
public class IdentitySpaceTestCase
    extends TestBase
    {

    @Test
    public void testCommunitySpaceNotNull()
        {

        Community community = factories().communities().create(
            "urn:community",
            unique(
                "test-community"
                )
            );
        assertNotNull(
            community
            );
        assertNotNull(
            community.space()
            );

        Identity identity = community.identities().create(
            unique(
                "test-identity"
                )
            );
        assertNotNull(
            identity
            );
        }

    @Test
    public void testIdentitySpaceNotNull()
        {

        Community community = factories().communities().create(
            unique(
                "test-community"
                ),
            "urn:community"
            );
        log.debug("Test community [{}][{}]", community.ident(), community.name());
        log.debug("Community space [{}][{}]", community.space().ident(), community.space().name());

        Identity identity = community.identities().create(
            unique(
                "test-identity"
                )
            );
        assertNotNull(
            identity
            );
        assertNotNull(
            identity.space(
                true
                )
            );
        }
}
