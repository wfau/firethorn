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

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

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
     * Webservice path for an indiviual column.
     *
     */
    public static final String COLUMN_NAME_PATH = "/meta/table/{table}/column/{column}" ;

    /**
     * Webservice path for a lst of columns.
     *
     */
    public static final String COLUMN_LIST_PATH = "/meta/table/{table}/columns" ;

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
                COLUMN_NAME_PATH
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

        return new BeanWrapper(
            bean
            );
        }

    @Override
    public Iterable<Attribute> getAttributes(String source)
        {
        log.debug("getAttributes(String)");
        log.debug("  Source [" + source + "]");

        AttributeBean[] array = rest().getForObject(
            endpoint(
                COLUMN_LIST_PATH
                ),
                AttributeBean[].class,
            source
            );        
        
        return new BeanWrapper.Iter(
            array
            );
        }

    /**
     * AttributeImpl based wrapper for the Bean class.
     * 
     */
    public static class BeanWrapper
    extends AttributeImpl
    implements Attribute
        {
        public static class Iter
        implements Iterable<Attribute>
            {
            private AttributeBean[] array ;
            public Iter(AttributeBean[] array)
                {
                this.array = array;
                }
            @Override
            public Iterator<Attribute> iterator()
                {
                return new Iterator<Attribute>()
                    {
                    private Iterator<AttributeBean> inner = Arrays.asList(Iter.this.array).iterator();
                    @Override
                    public boolean hasNext()
                        {
                        return this.inner.hasNext();
                        }

                    @Override
                    public Attribute next()
                        {
                        return new BeanWrapper(
                            this.inner.next()
                            );
                        }

                    @Override
                    public void remove()
                        {
                        this.inner.remove();
                        }
                    };
                }
            }

        /**
         * Protected constructor.
         *
         */
        protected BeanWrapper(AttributeBean bean)
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


