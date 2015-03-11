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

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import uk.ac.roe.wfau.firethorn.ogsadai.metadata.client.AttributeService;
import uk.org.ogsadai.dqp.common.RequestDetails;
import uk.org.ogsadai.dqp.lqp.Attribute;

/**
 *
 *
 */
public class CachingAttributeServiceImpl
implements AttributeService
    {
    /**
     * Debug logger.
     *
     */
    //private static Log log = LogFactory.getLog(CachingAttributeServiceImpl.class);

    /**
     * Internal AttributeService.
     *
     */
    private final SimpleAttributeServiceImpl service;

    /**
     * Nexted map of Attributes, indexed by source and attribute name.
     *
     */
    private final Map<String, Map<String, Attribute>> outer = new HashMap<String, Map<String, Attribute>>();

    /**
     * Protected constructor.
     *
     */
    public CachingAttributeServiceImpl(final String endpoint, final RequestDetails request)
        {
        this.service = new SimpleAttributeServiceImpl(
            endpoint,
            request
            );
        }

    /**
     * Synchronised access our nested map of attributes.
     *
     */
    protected synchronized Map<String, Attribute> inner(final String source)
        {
        Map<String, Attribute> inner = this.outer.get(
            source
            );
        if (inner == null)
            {
            inner = new HashMap<String, Attribute>();
            this.outer.put(
                source,
                inner
                );
            for (final Attribute attrib : this.service.getAttributes(source))
                {
                inner.put(
                    attrib.getName(),
                    attrib
                    );
                }
            }
        return inner;
        }

    @Override
    public Attribute getAttribute(final String source, final String attrib)
        {
        //log.trace("getAttribute(String, String)");
        //log.trace("  Source [" + source + "]");
        //log.trace("  Attrib [" + attrib + "]");
        return inner(
            source
            ).get(
                attrib
                );
        }

    @Override
    public Iterable<Attribute> getAttributes(final String source)
        {
        //log.trace("getAttributes(String)");
        //log.trace("  Source [" + source + "]");
        return inner(
            source
            ).values();
        }
    }
