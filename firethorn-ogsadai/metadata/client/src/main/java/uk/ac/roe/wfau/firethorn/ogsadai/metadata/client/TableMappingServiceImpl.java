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

import uk.ac.roe.wfau.firethorn.ogsadai.metadata.TableMapping;
import uk.ac.roe.wfau.firethorn.ogsadai.metadata.TableMappingService;
import uk.org.ogsadai.dqp.common.RequestDetails;

/**
 *
 *
 */
public class TableMappingServiceImpl
extends MetadataServiceBase
implements TableMappingService
    {
    /**
     * Debug logger.
     *
     */
    private static Log log = LogFactory.getLog(TableMappingServiceImpl.class);

    /**
     * Web service path.
     *
     */
    public static final String TABLE_PATH = "/meta/table/{table}" ;
    
    /**
     * Protected constructor.
     *
     */
    protected TableMappingServiceImpl(final String endpoint, final RequestDetails request)
        {
        super(
            endpoint,
            request
            );
        }

    @Override
    public TableMapping getTableMapping(final String table)
        {
        log.debug("getTableMapping(String)");
        log.debug("  Table  [" + table  + "]");

        TableMappingBean bean = rest().getForObject(
            endpoint(TABLE_PATH),
            TableMappingBean.class,
            table
            );        

        log.debug("Got bean ----");
        log.debug("  Name  [" + bean.tableName()  + "]");
        log.debug("  Alias [" + bean.tableAlias() + "]");
        log.debug("  Resource [" + bean.resourceIdent() + "]");
        log.debug("----");

        return bean ;
        }

    /**
     * Bean implementation.
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
    }

