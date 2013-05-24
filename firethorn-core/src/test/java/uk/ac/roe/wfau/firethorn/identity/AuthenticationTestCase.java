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

import lombok.extern.slf4j.Slf4j;

import org.junit.Test;
import uk.ac.roe.wfau.firethorn.test.TestBase;

/**
 *
 *
 */
@Slf4j
public class AuthenticationTestCase
extends TestBase
    {

    @Test
    public void test000()
        {
        Operation operation = factories().operations().create(
            "method",
            "source"
            ); 
        log.debug("Oper [{}]", operation.ident());

        Community community = factories().communities().create(
            "urn:community",
            "community-name"
            );

        operation.authentications().create(
            community.identities().create(
                "identity-name"
                ),
            "urn:authentication"
            );

        for (Authentication auth : operation.authentications().select())
            {
            log.debug("Auth [{}]", auth.ident());
            }

        }
    }
