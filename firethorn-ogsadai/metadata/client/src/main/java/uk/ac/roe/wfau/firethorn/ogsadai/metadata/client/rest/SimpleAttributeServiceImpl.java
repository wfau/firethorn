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

import java.util.Arrays;
import java.util.Iterator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import uk.ac.roe.wfau.firethorn.ogsadai.metadata.client.AttributeService;
import uk.org.ogsadai.dqp.common.RequestDetails;
import uk.org.ogsadai.dqp.lqp.Attribute;
import uk.org.ogsadai.dqp.lqp.AttributeImpl;

/**
 *
 *
 */
public class SimpleAttributeServiceImpl
extends MetadataServiceBase
implements AttributeService
    {
    /**
     * Debug logger.
     *
     */
    //private static Log log = LogFactory.getLog(SimpleAttributeServiceImpl.class);

    /**
     * Webservice path for an indiviual attribute.
     *
     */
    public static final String ATTRIBUTE_NAME_PATH = "/meta/table/{source}/column/{attrib}" ;

    /**
     * Webservice path for a list of attributes.
     *
     */
    public static final String ATTRIBUTE_LIST_PATH = "/meta/table/{source}/columns" ;

    /**
     * Protected constructor.
     *
     */
    public SimpleAttributeServiceImpl(final RequestDetails request)
        {
        super(
            request
            );
        }

    protected AttributeBean bean(final String source, final String attrib)
        {
        //log.trace("bean(String, String)");
        //log.trace("  Source [" + source + "]");
        //log.trace("  Attrib [" + attrib + "]");
        return rest().getForObject(
            endpoint(
                ATTRIBUTE_NAME_PATH
                ),
            AttributeBean.class,
            source,
            attrib
            );
        }

    protected AttributeBean[] array(final String source)
        {
        //log.trace("array(String)");
        //log.trace("  Source [" + source + "]");
        return rest().getForObject(
            endpoint(
                ATTRIBUTE_LIST_PATH
                ),
                AttributeBean[].class,
            source
            );
        }

    @Override
    public Attribute getAttribute(final String source, final String attrib)
        {
        //log.trace("getAttribute(String, String)");
        //log.trace("  Source [" + source + "]");
        //log.trace("  Attrib [" + attrib + "]");
        return BeanWrapper.wrap(
            bean(
                source,
                attrib
                )
            );
        }

    @Override
    public Iterable<Attribute> getAttributes(final String source)
        {
        //log.trace("getAttributes(String)");
        //log.trace("  Source [" + source + "]");
        return BeanWrapper.wrap(
            array(
                source
                )
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
            private final AttributeBean[] array ;
            public Iter(final AttributeBean[] array)
                {
                this.array = array;
                }
            protected AttributeBean[] array()
                {
                return this.array;
                }
            @Override
            public Iterator<Attribute> iterator()
                {
                return new Iterator<Attribute>()
                    {
                    private final Iterator<AttributeBean> inner = Arrays.asList(array()).iterator();
                    @Override
                    public boolean hasNext()
                        {
                        return this.inner.hasNext();
                        }
                    @Override
                    public Attribute next()
                        {
                        return wrap(
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
         * Public wrapping method.
         *
         */
        public static Iterable<Attribute> wrap(final AttributeBean[] array)
            {
            return new BeanWrapper.Iter(
                array
                );
            }

        /**
         * Public wrapping method.
         *
         */
        public static Attribute wrap(final AttributeBean bean)
            {
            return debug(
                new BeanWrapper(
                bean
                    )
                );
            }

        /**
         * Protected constructor.
         *
         */
        protected BeanWrapper(final AttributeBean bean)
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
     * Bean class used by the JSON handler.
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

        private boolean key;
        public boolean isKey()
            {
            return this.key;
            }
        }
    
    public static Attribute debug(final Attribute attribute)
        {
        //log.trace("Attribute");
        //log.trace("  Name [" + attribute.getName() + "]");
        //log.trace("  Type [" + attribute.getType() + "]");
        //log.trace("  Source [" + attribute.getSource() + "]");
        return attribute ;
        }

    public static Iterable<Attribute> debug(final Iterable<Attribute> inner)
        {
        return new Iterable<Attribute>()
            {
            @Override
            public Iterator<Attribute> iterator()
                {
                return debug(
                    inner.iterator()
                    );
                }
            };
        }

    public static Iterator<Attribute> debug(final Iterator<Attribute> inner)
        {
        return new Iterator<Attribute>()
            {
            @Override
            public boolean hasNext()
                {
                return inner.hasNext();
                }
            @Override
            public Attribute next()
                {
                return debug(
                    inner.next()
                    );
                }
            @Override
            public void remove()
                {
                inner.remove();
                }
            };
        }
    }

