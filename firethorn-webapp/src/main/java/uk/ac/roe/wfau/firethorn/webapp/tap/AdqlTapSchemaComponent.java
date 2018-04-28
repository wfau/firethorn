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

package uk.ac.roe.wfau.firethorn.webapp.tap;

import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;
import uk.ac.roe.wfau.firethorn.access.ProtectionException;
import uk.ac.roe.wfau.firethorn.entity.annotation.CreateMethod;
import uk.ac.roe.wfau.firethorn.entity.exception.NameNotFoundException;

/**
 * Wrapper class to control the Transactions for JDBC and ADQL operations.
 *
 */
@Slf4j
@Component
public class AdqlTapSchemaComponent
    {
    /**
     * Public constructor.
     * 
     */
    public AdqlTapSchemaComponent()
        {
        }
    
    /**
	 * Execute the two JDBC steps in their own Transactions.
	 * @throws ProtectionException 
     * @throws NameNotFoundException 
	 * 
	 */
	public void jdbc(final TapSchemaGeneratorImpl generator)
    throws ProtectionException, NameNotFoundException
	    {
	    log.debug("Starting JDBC");
        generator.createTapSchemaJdbc();
        log.debug("Fininshed JDBC");
	    }

	/**
     * Execute the ADQL steps in a single Transaction.
     * @throws ProtectionException 
     * 
     */
    @CreateMethod
    public void adql(final TapSchemaGeneratorImpl generator)
    throws ProtectionException
        {
        log.debug("Starting ADQL");
        generator.createTapSchemaAdql();
        log.debug("Fininshed ADQL");
        }

    }