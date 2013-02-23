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

import uk.ac.roe.wfau.firethorn.ogsadai.metadata.AttributeService;
import uk.org.ogsadai.dqp.common.RequestDetails;
import uk.org.ogsadai.dqp.lqp.Attribute;
import uk.org.ogsadai.dqp.lqp.AttributeImpl;

/**
 *
 *
 */
public class AttributeServiceImpl
extends MetadataServiceBase
implements AttributeService
    {
    /**
     * Debug logger.
     *
     */
    private static Log log = LogFactory.getLog(AttributeServiceImpl.class);

    /**
     * Webservice path.
     *
     */
    public static final String SERVICE_PATH = "/meta/table/{table}/attrib/{attrib}" ;

    /**
     * Protected constructor.
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
    public Attribute getAttribute(String source, String attrib)
        {
        log.debug("getAttribute(String, String)");
        log.debug("  Source [" + source + "]");
        log.debug("  Attrib [" + attrib + "]");

        AttributeBean bean = rest().getForObject(
            endpoint(
                SERVICE_PATH
                ),
            AttributeBean.class,
            source,
            attrib
            );        

        log.debug("Got bean ----");
        log.debug("  Name   [" + bean.getName()   + "]");
        log.debug("  Source [" + bean.getSource() + "]");
        log.debug("  Type   [" + bean.getType()   + "]");
        log.debug("----");

        return new AttributeBeanWrapper(
            bean
            );
        }

    @Override
    public Iterable<Attribute> getAttributes(String table)
        {
        log.debug("getAttributes(String)");
        log.debug("  Table [" + table  + "]");
        return null ;
        }

    /**
     * AttributeImpl based wrapper for the Bean class.
     * 
     */
    public static class AttributeBeanWrapper
    extends AttributeImpl
    implements Attribute
        {
        /**
         * Protected constructor.
         *
         */
        protected AttributeBeanWrapper(AttributeBean bean)
            {
            super(
                bean.getName(),
                bean.getType(),
                bean.getSource(),
                bean.isKey()
                );
            }
        }

    /**
     * Bean used by the JSON handler.
     * 
     */
    public static class AttributeBean
        {
        public AttributeBean()
            {
            }
        
        private String name;
        public String getName()
            {
            return this.name;
            }

        private String source;
        public String getSource()
            {
            return this.source;
            }

        private int type;
        public int getType()
            {
            return this.type;
            }

        /*
         * 
uk.org.ogsadai.tuple.
TupleTypes
TupleTypeUtility
TupleUtilities
TupleTypesConverter

         *
         */
        
        private boolean key;
        public boolean isKey()
            {
            return this.key;
            }
        }
    }


