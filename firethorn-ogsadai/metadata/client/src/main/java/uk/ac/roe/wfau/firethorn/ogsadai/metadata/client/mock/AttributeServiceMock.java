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
package uk.ac.roe.wfau.firethorn.ogsadai.metadata.client.mock;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import uk.ac.roe.wfau.firethorn.ogsadai.metadata.client.AttributeService;
import uk.org.ogsadai.dqp.lqp.Attribute;
import uk.org.ogsadai.dqp.lqp.AttributeImpl;

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

        public AttributeMock(final String name)
            {
            super(
                name
                );
            }

        public AttributeMock(final String name, final int type, final String source, final boolean isKey)
            {
            super(
                name,
                type,
                source,
                isKey
                );
            }
        }

    private final Map<String, Map<String, Attribute>> map = new HashMap<String, Map<String, Attribute>>();

    public void add(final String name, final int type, final String source, final boolean isKey)
        {
        final Map<String, Attribute> inner = inner(
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

    public Map<String, Attribute> inner(final String table)
        {
        if (this.map.containsKey(table))
            {
            return this.map.get(
                table
                );
            }
        else {
            final Map<String, Attribute> inner = new HashMap<String, Attribute>();
            this.map.put(
                table,
                inner
                );
            return inner;
            }
        }

    public Iterable<Attribute> get(final String table)
        {
        log.debug("get(String) [" + table + "]");
        return this.map.get(
            table
            ).values();
        }

    public Attribute get(final String table, final String column)
        {
        log.debug("get(String, String) [" + table + "][" + column + "]");
        return this.map.get(
            table
            ).get(
                column
                );
        }

    @Override
    public Attribute getAttribute(final String table, final String column)
        {
        return this.get(
            table,
            column
            );
        }

    @Override
    public Iterable<Attribute> getAttributes(final String table)
        {
        return this.get(
            table
            );
        }
    }


