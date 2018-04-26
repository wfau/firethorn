/*
 *  Copyright (C) 2018 Royal Observatory, University of Edinburgh, UK
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

package uk.ac.roe.wfau.firethorn.spring;

import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 * Based on ideas from:
 * http://blog.timmattison.com/archives/2012/04/19/tips-for-debugging-springs-transactional-annotation/
 * 
 */
@Slf4j
@Component
public class TransactionTester
    {

    /**
     * 
     */
    public TransactionTester()
        {
        // TODO Auto-generated constructor stub
        }

    /**
     * Check if we are in an active Transaction.
     * 
     */
    public static boolean active()
        {
        return TransactionSynchronizationManager.isActualTransactionActive();
        }

    
    
    }
