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

import uk.ac.roe.wfau.firethorn.ogsadai.metadata.client.TableMapping;
import uk.ac.roe.wfau.firethorn.ogsadai.metadata.client.TableMappingService;
import uk.org.ogsadai.dqp.common.RequestDetails;

/**
 *
 *
 */
public class CachingTableMappingServiceImpl
implements TableMappingService
    {
    /**
     * Debug logger.
     *
     */
    private static Log log = LogFactory.getLog(CachingTableMappingServiceImpl.class);

    /**
     * Internal tableMapping service.
     *
     */
    private final SimpleTableMappingServiceImpl service ;

    /**
     * Public constructor.
     *
     */
    public CachingTableMappingServiceImpl(final String endpoint, final RequestDetails request)
        {
        this.service = new SimpleTableMappingServiceImpl(
            endpoint,
            request
            );
        }

    /**
     * Internal map of tables indexed by source alias.
     *
     */
    private final Map<String, TableMapping> map = new HashMap<String, TableMapping>();

    @Override
    public TableMapping getTableMapping(final String source)
        {
        log.debug("getTableMapping(String)");
        log.debug("  Source [" + source  + "]");

        TableMapping mapping = this.map.get(
            source
            );
        if (mapping == null)
            {
            mapping = this.service.getTableMapping(
                source
                );
            if (mapping != null)
                {
                this.map.put(
                    source,
                    mapping
                    );
                }
            }
        return mapping ;
        }
    }

