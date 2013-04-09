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

import static org.junit.Assert.assertEquals;

import java.net.URI;

import lombok.extern.slf4j.Slf4j;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


import uk.ac.roe.wfau.firethorn.spring.ComponentFactories;

/**
 * Transactional base class for tests.
 * Using Propagation.REQUIRES_NEW create a new transaction for each test.
 *
 */
@Slf4j
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

    }

