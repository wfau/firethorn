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

import uk.ac.roe.wfau.firethorn.ogsadai.metadata.client.TableMapping;
import uk.ac.roe.wfau.firethorn.ogsadai.metadata.client.TableMappingService;
import uk.org.ogsadai.dqp.common.RequestDetails;

/**
 *
 *
 */
public class SimpleTableMappingServiceImpl
extends MetadataServiceBase
implements TableMappingService
    {
    /**
     * Debug logger.
     *
     */
    private static Log log = LogFactory.getLog(SimpleTableMappingServiceImpl.class);

    /**
     * Webservice path.
     *
     */
    public static final String SOURCE_NAME_PATH = "/meta/table/{source}" ;

    /**
     * Protected constructor.
     *
     */
    protected SimpleTableMappingServiceImpl(final RequestDetails request)
        {
        super(
            request
            );
        }

    @Override
    public TableMapping getTableMapping(final String source)
        {
        log.debug("getTableMapping(String)");
        log.debug("  Source [" + source  + "]");
        return debug(
    		rest().getForObject(
                endpoint(
                    SOURCE_NAME_PATH
                    ),
                TableMappingBean.class,
                source
                )
            );
        }

    /**
     * Bean class used by the JSON handler.
     *
     */
    public static class TableMappingBean
    implements TableMapping
        {
        public TableMappingBean()
            {
            }
        private String alias;
        public  void setAlias(final String value)
            {
            this.alias= value ;
            }
        private String name;
        public  void setName(final String value)
            {
            this.name = value ;
            }

        private String resource;
        public  void setResource(final String value)
            {
            this.resource = value ;
            }
        @Override
        public String resourceIdent()
            {
            return this.resource;
            }
        @Override
        public String tableAlias()
            {
            return this.alias;
            }
        @Override
        public String tableName()
            {
            return this.name;
            }
        }

    public TableMapping debug(final TableMapping mapping)
        {
        log.debug("TableMapping");
        log.debug("  Name  [" + mapping.tableName() + "]");
        log.debug("  Alias [" + mapping.tableAlias() + "]");
        log.debug("  Resource [" + mapping.resourceIdent() + "]");
        return mapping;
        }
    
    }

