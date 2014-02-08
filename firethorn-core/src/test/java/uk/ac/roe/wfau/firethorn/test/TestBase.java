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

import lombok.extern.slf4j.Slf4j;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import uk.ac.roe.wfau.firethorn.identity.Authentication;
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
    public static final String TEST_OPER_TARGET = "test-target" ;
    public static final String TEST_OPER_METHOD = "test-method" ;
    public static final String TEST_OPER_SOURCE = "test-source" ;

    public static final String TEST_AUTH_METHOD    = "urn:test-auth" ;
    public static final String TEST_COMMUNITY_URI  = "urn:test" ;
    public static final String TEST_COMMUNITY_NAME = "Test community" ;
    public static final String TEST_COMMUNITY_USER = "Test user" ;

    /**
     * Initialise our operation and identity.
     * http://stackoverflow.com/questions/6076599/what-order-are-the-junit-before-after-called
     *
     */
    @Before
    public void oper()
        {
        final Operation operation = factories().operations().create(
            TEST_OPER_TARGET,
            TEST_OPER_METHOD,
            TEST_OPER_SOURCE
            );

        log.debug(" Oper [{}][{}][{}][{}]", operation.ident(), operation.target(), operation.method(), operation.source());

        if (operation.auth().primary() == null)
            {
            operation.auth().create(
                    factories().communities().create(
                        TEST_COMMUNITY_NAME,
                        TEST_COMMUNITY_URI
                    ).members().create(
                        TEST_COMMUNITY_USER
                    ),
                TEST_AUTH_METHOD
                );
            }

        final Authentication primary = operation.auth().primary();
        log.debug(" Auth [{}][{}][{}]", primary.method(), primary.identity().ident(), primary.identity().name());

        }

    /**
     * Empty test to prevent Eclipse from throwing an initializationError when it runs this as a test.
     *
     */
    @Test
    public void empty()
    throws Exception
        {
        }
    }

