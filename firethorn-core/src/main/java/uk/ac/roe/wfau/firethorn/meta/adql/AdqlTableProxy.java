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
package uk.ac.roe.wfau.firethorn.meta.adql;

import java.util.Iterator;
import java.util.UUID;

import javax.persistence.Transient;

import lombok.extern.slf4j.Slf4j;

import org.joda.time.DateTime;

import uk.ac.roe.wfau.firethorn.adql.query.AdqlQuery;
import uk.ac.roe.wfau.firethorn.entity.Identifier;
import uk.ac.roe.wfau.firethorn.entity.ProxyIdentifier;
import uk.ac.roe.wfau.firethorn.entity.exception.NameFormatException;
import uk.ac.roe.wfau.firethorn.entity.exception.NotFoundException;
import uk.ac.roe.wfau.firethorn.identity.Identity;
import uk.ac.roe.wfau.firethorn.meta.base.BaseColumn;
import uk.ac.roe.wfau.firethorn.meta.base.BaseSchema;
import uk.ac.roe.wfau.firethorn.meta.base.BaseTable;
import uk.ac.roe.wfau.firethorn.meta.base.BaseComponent.EntityType;
import uk.ac.roe.wfau.firethorn.meta.base.BaseComponent.InvalidStatusException;
import uk.ac.roe.wfau.firethorn.meta.base.BaseComponent.Status;
import uk.ac.roe.wfau.firethorn.spring.ComponentFactories;
import uk.ac.roe.wfau.firethorn.spring.ComponentFactoriesImpl;

/**
 *
 *
 */
@Slf4j
public class AdqlTableProxy
    implements AdqlTable
    {
    /**
     * TODO Move to proxy base class
     */
    @Transient
    protected ComponentFactories factories ;

    /**
     * TODO Move to proxy base class
     */
    protected ComponentFactories factories()
        {
        if (this.factories == null)
            {
            this.factories = ComponentFactoriesImpl.instance();
            }
        return this.factories;
        }

    /**
     * TODO Move to proxy base class
     */
    @Override
    public void refresh()
        {
        throw new UnsupportedOperationException(); 
        }

    /**
     * TODO Move to proxy base class
     */
    @Override
    public void delete()
        {
        throw new UnsupportedOperationException(); 
        }

    /**
     * Iterable wrapper.
     * @todo Move to a factory
     * 
     */
    public static class ProxyIterable
    implements Iterable<AdqlTable>
        {
        final private Iterable<BaseTable<?,?>> base ;
        final private AdqlSchema schema ;

        public ProxyIterable(final Iterable<BaseTable<?,?>> base, final AdqlSchema schema)
            {
            this.base   = base  ;
            this.schema = schema;
            }

        @Override
        public Iterator<AdqlTable> iterator()
            {
            return new ProxyIterator(
                base.iterator(),
                schema
                );
            }
        }

    /**
     * Iterator wrapper.
     * @todo Move to a factory
     * 
     */
    public static class ProxyIterator
    implements Iterator<AdqlTable>
        {
        final private Iterator<BaseTable<?,?>> base ;
        final private AdqlSchema schema ;

        public ProxyIterator(final Iterator<BaseTable<?,?>> base, final AdqlSchema schema)
            {
            this.base   = base  ;
            this.schema = schema;
            }

        @Override
        public boolean hasNext()
            {
            return base.hasNext();
            }

        @Override
        public AdqlTable next()
            {
            return new AdqlTableProxy(
                base.next(),
                schema
                );
            }

        @Override
        public void remove()
            {
            throw new UnsupportedOperationException();
            }
        }
    
    /**
     * Protected constructor.
     *
     */
    public AdqlTableProxy(BaseTable<?,?> base, AdqlSchema schema)
        {
        this.base   = base   ;
        this.schema = schema ;
        this.uuid  = factories().uuids().uuid(); 
        }

    private UUID uuid;
    @Override
    public UUID uuid()
        {
        return this.uuid;
        }
    
    /**
     * The parent schema.
     * 
     */
    private AdqlSchema schema ;
    @Override
    public AdqlSchema schema()
        {
        return this.schema;
        }
    @Override
    public void schema(AdqlSchema schema)
        {
        throw new UnsupportedOperationException(
            "Can't change a read only copy"
            ); 
        }
    @Override
    public AdqlResource resource()
        {
        return this.schema.resource();
        }
    
    /**
     * The base table.
     * 
     */
    private BaseTable<?,?> base ;
    @Override
    public BaseTable<?, ?> base()
        {
        return this.base;
        }

    @Override
    public BaseTable<?, ?> root()
        {
        return base().root();
        }

    private Identifier ident ;
    @Override
    public Identifier ident()
        {
        if (ident == null)
            {
            ident = new ProxyIdentifier(
                schema().ident(),
                base().ident()
                );
            }
        return ident ;
        }
    
    @Override
    public String link()
        {
        return factories().adql().tables().links().link(
            this
            );
        }

    @Override
    public Identity owner()
        {
        return schema().owner();
        }

    @Override
    public DateTime created()
        {
        return schema().created();
        }

    @Override
    public DateTime modified()
        {
        return schema().modified();
        }

    @Override
    public String name()
        {
        return base().name();
        }

    @Override
    public void name(String name) throws NameFormatException
        {
        throw new UnsupportedOperationException(
            "Can't change a read only copy"
            ); 
        }

    @Override
    public String text()
        {
        return base().text();
        }

    @Override
    public void text(String text)
        {
        throw new UnsupportedOperationException(
            "Can't change a read only copy"
            ); 
        }

    @Override
    public String alias()
        {
        return base().alias();
        }

    /**
     * Copied from BaseTable.
     */
    @Override
    public StringBuilder namebuilder()
        {
        return this.schema().namebuilder().append(".").append(this.name());
        }

    @Override
    public Status status()
        {
        return base().status();
        }

    @Override
    public void status(Status status) throws InvalidStatusException
        {
        throw new UnsupportedOperationException(
            "Can't change a read only copy"
            ); 
        }

    @Override
    public EntityType entitytype()
        {
        return EntityType.PROXY;
        }

    @Override
    public void entitytype(EntityType copytype)
        {
        throw new UnsupportedOperationException(
            "Can't change a read only copy"
            ); 
        }

    @Override
    @Deprecated
    public BaseTable.Linked linked()
        {
        return null;
        }

    @Override
    public AdqlQuery query()
        {
        return base.query();
        }

    @Override
    public Metadata meta()
        {
        return base.meta();
        }

    @Override
    public Columns columns()
        {
        return new Columns()
            {
            @Override
            @SuppressWarnings("unchecked")
            public Iterable<AdqlColumn> select()
                {
                return new AdqlColumnProxy.ProxyIterable(
                    (Iterable<BaseColumn<?>>)base.columns().select(),
                    AdqlTableProxy.this
                    );
                }

            @Override
            @SuppressWarnings("unchecked")
            public Iterable<AdqlColumn> search(String text)
                {
                return new AdqlColumnProxy.ProxyIterable(
                    (Iterable<BaseColumn<?>>)base.columns().search(text),
                    AdqlTableProxy.this
                    );
                }

            @Override
            public AdqlColumn select(String name) throws NotFoundException
                {
                return new AdqlColumnProxy(
                    base.columns().select(name),
                    AdqlTableProxy.this
                    );
                }

            @Override
            public AdqlColumn create(BaseColumn<?> base)
                {
                return new AdqlColumnProxy(
                    base,
                    AdqlTableProxy.this
                    );
                }
            };
        }
    }
