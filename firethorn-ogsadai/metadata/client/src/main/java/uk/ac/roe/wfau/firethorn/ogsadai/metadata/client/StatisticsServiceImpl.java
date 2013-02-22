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
package uk.ac.roe.wfau.firethorn.ogsadai.metadata.client;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import uk.org.ogsadai.dqp.common.RequestDetails;

import uk.org.ogsadai.dqp.lqp.Attribute;
import uk.org.ogsadai.dqp.lqp.cardinality.ArithmeticOperator;
import uk.org.ogsadai.dqp.lqp.cardinality.AttributeStatistics;
import uk.org.ogsadai.dqp.lqp.cardinality.HistogramBasedAttributeStatistics;

import uk.ac.roe.wfau.firethorn.ogsadai.metadata.AttributeService;
import uk.ac.roe.wfau.firethorn.ogsadai.metadata.StatisticsService;
import uk.ac.roe.wfau.firethorn.ogsadai.metadata.TableMappingService;

/**
 *
 *
 */
public class StatisticsServiceImpl
extends MetadataServiceBase
implements StatisticsService
    {

    private static Log log = LogFactory.getLog(StatisticsServiceImpl.class);

    /*
     *
     *
     */
    public StatisticsServiceImpl(String endpoint, RequestDetails request)
        {
        super(
            endpoint,
            request
            );
        log.debug("StatisticsServiceImpl()");
        }

    @Override
    public AttributeStatistics getStatistics(Attribute attrib)
        {
        log.debug("getStatistics(Attribute)");
        log.debug("  Attrib [" + attrib.getSource()  + "][" + attrib.getName() + "]");
        return getStatistics(
            attrib.getSource(),
            attrib.getName()
            );
        }

    @Override
    public AttributeStatistics getStatistics(String table, String column)
        {
        log.debug("getStatistics(String, String)");
        log.debug("  Table  [" + table  + "]");
        log.debug("  Column [" + column + "]");
        return new HistogramBasedAttributeStatistics(); 
        }
    }

