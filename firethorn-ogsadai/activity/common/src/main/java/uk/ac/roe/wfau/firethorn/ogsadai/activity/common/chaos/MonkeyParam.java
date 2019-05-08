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

package uk.ac.roe.wfau.firethorn.ogsadai.activity.common.chaos;

import java.sql.SQLException;

/**
 * Public interface for the ChaosMonkey parameter.
 * TODO Move this to a separate Maven project
 * 
 */
public interface MonkeyParam
    {
    public static final String DEFAULT_NAME = "no-name";
    public static final Object DEFAULT_DATA = null;
    
    /**
     * Get the parameter name.
     *
     */
    public String name();

    /**
     * Get the parameter value.
     *
     */
    public Object data();

    /**
     * Check the parameter name and value against an object class.
     * This method matches Object.getClass().getName() against the parameter name. 
     *
     */
    public boolean test(final Object owner, final Object value);

    /**
     * Check the parameters and throw a SQLException.
     * This method matches Object.getClass().getName() against the parameter name. 
     *
     */
    public void sqlException(final Object owner, final Object value)
        throws SQLException;

    /**
     * Check the parameters and throw a SQLException.
     * This method matches Object.getClass().getName() against the parameter name. 
     *
     */
	void sqlException(final Object owner, final Object value, final String message) throws SQLException;

    }
