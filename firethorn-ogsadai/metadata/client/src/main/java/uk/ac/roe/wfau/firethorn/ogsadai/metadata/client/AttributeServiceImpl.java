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

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import uk.org.ogsadai.dqp.lqp.Attribute;
import uk.org.ogsadai.dqp.lqp.AttributeImpl;

import uk.org.ogsadai.dqp.common.RequestDetails;

import uk.ac.roe.wfau.firethorn.ogsadai.metadata.AttributeService;
import uk.ac.roe.wfau.firethorn.ogsadai.metadata.StatisticsService;
import uk.ac.roe.wfau.firethorn.ogsadai.metadata.TableMappingService;

/**
 *
 *
 */
public class AttributeServiceImpl
extends MetadataServiceBase
implements AttributeService
    {
    private static Log log = LogFactory.getLog(AttributeServiceImpl.class);

    /*
     *
     *
     */
    public AttributeServiceImpl(String endpoint, RequestDetails request)
        {
        super(
            endpoint,
            request
            );
        log.debug("AttributeServiceImpl()");
        }

    @Override
    public Attribute getAttribute(String table, String column)
        {
        log.debug("getAttribute(String, String)");
        log.debug("  Table  [" + table  + "]");
        log.debug("  Column [" + column + "]");
        return null ;
        }

    @Override
    public Iterable<Attribute> getAttributes(String table)
        {
        log.debug("getAttributes(String)");
        log.debug("  Table [" + table  + "]");
        return null ;
        }
    }


