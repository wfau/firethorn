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
package uk.ac.roe.wfau.firethorn.test ;

import org.junit.Before;
import org.junit.Ignore;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;
import uk.ac.roe.wfau.firethorn.community.Community;
import uk.ac.roe.wfau.firethorn.identity.Authentication;
import uk.ac.roe.wfau.firethorn.identity.Identity;
import uk.ac.roe.wfau.firethorn.identity.Operation;

/**
 * Transactional base class for tests.
 * Using Propagation.REQUIRES_NEW create a new transaction for each test.
 *
 */
@Slf4j
@Ignore
@Transactional(
    readOnly=false,
    propagation=Propagation.REQUIRES_NEW
    )
@TransactionConfiguration(
    transactionManager="FireThornTransactionManager",
    defaultRollback = false
    )
public abstract class TestBase
extends TestRoot
    {

    /**
     * Test {@link Operation} target.
     *
     */
    public static final String TEST_OPER_TARGET = "test" ;
    
    /**
     * Test {@link Operation} method.
     *
     */
    public static final String TEST_OPER_METHOD = "test" ;
    
    /**
     * Test {@link Operation} source.
     *
     */
    public static final String TEST_OPER_SOURCE = "test" ;

    /**
     * Test {@link Authentication} method.
     *
     */
    public static final String TEST_AUTH_METHOD    = "test" ;

    /**
     * Test {@link Identity} name.
     *
     */
    public static final String TEST_IDENTITY_NAME  = "Tester (identity)" ;

    /**
     * Test {@link Community} URI.
     *
     */
    public static final String TEST_COMMUNITY_URI  = "test" ;

    /**
     * Test {@link Community} name.
     *
     */
    public static final String TEST_COMMUNITY_NAME = "Tester (community)" ;

    /**
     * Initialise our {@link Operation} and {@link Identity}.
     * http://stackoverflow.com/questions/6076599/what-order-are-the-junit-before-after-called
     * @todo Why does this need to be final ?
     *
     */
    @Before
    public final void oper()
        {
        final Operation operation = factories().operations().entities().create(
            TEST_OPER_TARGET,
            TEST_OPER_METHOD,
            TEST_OPER_SOURCE,
            0
            );

        log.debug(" Oper [{}][{}][{}][{}]", operation.ident(), operation.url(), operation.method(), operation.source());

        Authentication primary = operation.authentications().primary();
        if (primary == null)
            {
            
            primary = operation.authentications().create(
                factories().communities().entities().create(
                    config().property(
                        "junit.community.uri",
                        TEST_COMMUNITY_URI
                        ),
                    config().property(
                        "junit.community.name",
                        TEST_COMMUNITY_NAME
                        )
                    ).members().create(
                        config().property(
                            "junit.identity.name",
                            TEST_IDENTITY_NAME
                            )
                        ),
                config().property(
                    "junit.auth.method",
                    TEST_AUTH_METHOD
                    )                        
                );
            }

        log.debug(" Auth [{}][{}][{}]", primary.method(), primary.identity().ident(), primary.identity().name());

        }

    /**
     * Empty test to prevent Eclipse from throwing an initializationError if it runs this as a test.
     * @throws Exception
     *
     */
    //@Test
    public void notest()
    throws Exception
        {
        }
    }

