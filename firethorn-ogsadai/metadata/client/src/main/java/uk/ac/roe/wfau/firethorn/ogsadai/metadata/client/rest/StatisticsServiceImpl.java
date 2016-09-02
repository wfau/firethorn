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
package uk.ac.roe.wfau.firethorn.ogsadai.metadata.client.rest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import uk.ac.roe.wfau.firethorn.ogsadai.metadata.client.StatisticsService;
import uk.org.ogsadai.dqp.common.RequestDetails;
import uk.org.ogsadai.dqp.lqp.Attribute;
import uk.org.ogsadai.dqp.lqp.cardinality.AttributeStatistics;
import uk.org.ogsadai.dqp.lqp.cardinality.HistogramBasedAttributeStatistics;

/**
 *
 *
 */
public class StatisticsServiceImpl
extends MetadataServiceBase
implements StatisticsService
    {

    //private static Log log = LogFactory.getLog(StatisticsServiceImpl.class);

    /*
     *
     *
     */
    public StatisticsServiceImpl(final RequestDetails request)
        {
        super(
            request
            );
        }

    @Override
    public AttributeStatistics getStatistics(final Attribute attrib)
        {
        //log.trace("getStatistics(Attribute)");
        //log.trace("  Attrib [" + attrib.getSource()  + "][" + attrib.getName() + "]");
        return getStatistics(
            attrib.getSource(),
            attrib.getName()
            );
        }

    @Override
    public AttributeStatistics getStatistics(final String table, final String column)
        {
        //log.trace("getStatistics(String, String)");
        //log.trace("  Table  [" + table  + "]");
        //log.trace("  Column [" + column + "]");
        return new HistogramBasedAttributeStatistics();
        }
    }

