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
 * Interface for accessing AttributeStatistics.
 *
 */
public interface StatisticsService
    {
    /**
     * Get the AttributeStatistics for a given Attribute.
     * @param attribute
     *      The Attribute to get the corresponding AttributeStatistics for.
     * @return The AttributeStatistics for the Attribute, or null if there is no corresponding AttributeStatistics.
     *
     */
    public AttributeStatistics getStatistics(Attribute attribute);

    /**
     * Get the AttributeStatistics given the source (table) and attribute name.
     * @param source
     *      The source (table) alias.
     *      <br/>
     *      This should be the table alias used in SQL queries passed into OGSA-DAI,
     *      before the mapping from table alias to fully qualified resource table name.
     * @param name
     *      The Attribute(column) name.
     * 
     * @return The AttributeStatistics for the corresponding Attribute, or null if there is no corresponding Attribute or AttributeStatistics.
     *
     */
    public AttributeStatistics getStatistics(String source, String name);

    }
