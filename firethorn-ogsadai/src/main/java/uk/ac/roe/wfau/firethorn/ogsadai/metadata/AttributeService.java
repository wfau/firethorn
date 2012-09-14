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
package uk.ac.roe.wfau.firethorn.ogsadai.metadata;

import uk.org.ogsadai.dqp.lqp.Attribute;
import uk.org.ogsadai.dqp.lqp.cardinality.AttributeStatistics;

/**
 * Interface for an Attribute metadata service.
 *
 */
public interface AttributeService
    {

    /**
     * An interface that extends Attribute to add access to the AttributeStatistics.
     *
     */
    public interface AttributeWithStats
    extends Attribute
        {

        /**
         * Get the AttributeStatistics for this Attribute.
         * @return The AttributeStatistics for this Attribute, or null if it has none.
         *
         */
        public AttributeStatistics getAttributeStatistics();

        }

    /**
     * Get an Iterable of Attributes, based on source (table) name.
     * @param source
     *      The source (table) name.
     *      <br/>
     *      This corresponds to the table alias used in the SQL query passed to OGSA-DAI,
     *      <strong>before</strong> the mapping from ADQL alias to JDBC table has been done.
     * 
     * @return An Iterable of Attribute(s) for the source (table).
     * 
     */
    public Iterable<AttributeWithStats> getAttributes(String source);

    /**
     * Get a specific Attribute, based on source (table) name and Attribute (column) name.
     * @param source
     *      The source (table) name.
     *      <br/>
     *      This corresponds to the table alias used in the SQL query passed to OGSA-DAI,
     *      <strong>before</strong> the mapping from ADQL alias to JDBC table has been done.
     * @param name
     *      The Attribute(column) name.
     * 
     * @return The specified Attribute, or null if there is no match.
     * 
     */
    public AttributeWithStats getAttribute(String source, String name);

    /**
     * Get the AttributeStatistics for a given Attribute.
     * @return The AttributeStatistics for the Attribute, or null if it has none.
     *
     */
    public AttributeStatistics getStatistics(Attribute attribute);

    }

