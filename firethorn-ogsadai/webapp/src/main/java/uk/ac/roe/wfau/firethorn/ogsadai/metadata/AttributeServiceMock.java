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
package uk.ac.roe.wfau.firethorn.ogsadai.metadata;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import uk.org.ogsadai.dqp.lqp.Attribute;
import uk.org.ogsadai.dqp.lqp.AttributeImpl;
import uk.org.ogsadai.dqp.common.RequestDetails;

/**
 *
 *
 */
public class AttributeServiceMock
    implements AttributeService
    {
    private static Log log = LogFactory.getLog(AttributeServiceMock.class);

    public class AttributeMock
    extends AttributeImpl
    implements Attribute
        {

        public AttributeMock(String name)
            {
            super(
                name
                );
            }

        public AttributeMock(String name, int type, String source, boolean isKey)
            {
            super(
                name,
                type,
                source,
                isKey
                );
            }
        }

    private Map<String, Map<String, Attribute>> map = new HashMap<String, Map<String, Attribute>>();

    public void add(String name, int type, String source, boolean isKey)
        {
        Map<String, Attribute> inner = inner(
            source
            );
        inner.put(
            name,
            new AttributeMock(
                name,
                type,
                source,
                isKey
                )
            );
        }

    public Map<String, Attribute> inner(String table)
        {
        if (map.containsKey(table))
            {
            return this.map.get(
                table
                );
            }
        else {
            Map<String, Attribute> inner = new HashMap<String, Attribute>();
            map.put(
                table,
                inner
                );
            return inner;
            }
        }

    public Iterable<Attribute> get(String table)
        {
        log.debug("get(String) [" + table + "]");        
        return this.map.get(
            table
            ).values();
        }

    public Attribute get(String table, String column)
        {
        log.debug("get(String, String) [" + table + "][" + column + "]");        
        return this.map.get(
            table
            ).get(
                column
                );
        }

    @Override
    public Attribute getAttribute(String table, String column)
        {
        return this.get(
            table,
            column
            );
        }

    @Override
    public Iterable<Attribute> getAttributes(String table)
        {
        return this.get(
            table
            );
        }
    }


