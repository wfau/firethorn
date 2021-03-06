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

import javax.persistence.Transient;

import org.joda.time.DateTime;

import lombok.extern.slf4j.Slf4j;
import uk.ac.roe.wfau.firethorn.access.ProtectionException;
import uk.ac.roe.wfau.firethorn.adql.query.blue.BlueQuery;
import uk.ac.roe.wfau.firethorn.entity.Identifier;
import uk.ac.roe.wfau.firethorn.entity.ProxyIdentifier;
import uk.ac.roe.wfau.firethorn.entity.access.EntityProtector;
import uk.ac.roe.wfau.firethorn.entity.exception.IdentifierNotFoundException;
import uk.ac.roe.wfau.firethorn.entity.exception.NameFormatException;
import uk.ac.roe.wfau.firethorn.entity.exception.NameNotFoundException;
import uk.ac.roe.wfau.firethorn.identity.Identity;
import uk.ac.roe.wfau.firethorn.meta.base.BaseColumn;
import uk.ac.roe.wfau.firethorn.meta.base.BaseTable;
import uk.ac.roe.wfau.firethorn.spring.ComponentFactories;
import uk.ac.roe.wfau.firethorn.spring.ComponentFactoriesImpl;

/**
 * Proxy used by a THIN copy schema to represent a table in the base schema.   
 *
 */
@Slf4j
public class AdqlTableProxy
    implements AdqlTable
    {
    protected AdqlTable.EntityServices services()
        {
        return AdqlTableEntity.EntityServices.instance() ; 
        }

    @Override
    public String link()
        {
        return services().links().link(
            this
            );
        }
    
    /**
     * TODO Move to proxy base class
    public AdqlTable self()
        {
        return this;
        }
     */

    /**
     * TODO Move to proxy base class.
     * 
     */
    @Transient
    protected AdqlTable.EntityFactory factory;
    public AdqlTable.EntityFactory factory()
        {
        return this.factory;
        }

    /**
     * TODO Move to proxy base class.
     * 
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
    public AdqlTableProxy(final BaseTable<?,?> base, final AdqlSchema schema)
        {
        this.base   = base   ;
        this.schema = schema ;
        }

    /**
     * The parent schema.
     *
     */
    private final AdqlSchema schema ;
    @Override
    public AdqlSchema schema()
        {
        return this.schema;
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
    private final BaseTable<?,?> base ;
    @Override
    public BaseTable<?, ?> base()
        {
        return this.base;
        }

    @Override
    public BaseTable<?, ?> root()
    throws ProtectionException
        {
        return this.base.root();
        }

    private Identifier ident ;
    @Override
    public Identifier ident()
        {
        log.debug("ident() [{}][{}][{}]", ident, schema(), base());
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
    public Identity owner()
        {
        return this.schema.owner();
        }

    @Override
    public DateTime created()
        {
        return this.schema.created();
        }

    @Override
    public DateTime modified()
        {
        return this.schema.modified();
        }

    @Override
    public String name()
        {
        return this.base.name();
        }

    @Override
    public void name(final String name) throws NameFormatException
        {
        throw new UnsupportedOperationException(
            "Can't modify a read only table"
            );
        }

    @Override
    public String text()
    throws ProtectionException
        {
        return this.base.text();
        }

    @Override
    public void text(final String text)
    throws ProtectionException
        {
        throw new UnsupportedOperationException(
            "Can't modify a read only table"
            );
        }

    @Override
    public String alias()
    throws ProtectionException
        {
        return this.base.alias();
        }

    @Override
    public StringBuilder namebuilder()
    throws ProtectionException
        {
        return this.schema.namebuilder().append(".").append(this.name());
        }
    @Override
    public String fullname()
    throws ProtectionException
        {
        return namebuilder().toString();
        }

    @Override
    public Status status()
    throws ProtectionException
        {
        return this.base.status();
        }

    @Override
    public void status(final Status status)
    throws ProtectionException, InvalidStatusException
        {
        throw new UnsupportedOperationException(
            "Can't modify a read only table"
            );
        }

    @Override
    public CopyDepth depth()
    throws ProtectionException
        {
        return CopyDepth.PROXY;
        }

    @Override
    public Metadata meta()
    throws ProtectionException
        {
        // TODO Need to make this read only.
        return base.meta();
        }

    @Override
    public Columns columns()
    throws ProtectionException
        {
        return new Columns()
            {
            @Override
            @SuppressWarnings("unchecked")
            public Iterable<AdqlColumn> select()
            throws ProtectionException
                {
                return new AdqlColumnProxy.ProxyIterable(
                    (Iterable<BaseColumn<?>>)base.columns().select(),
                    AdqlTableProxy.this
                    );
                }

            @Override
            public AdqlColumn search(final String name)
            throws ProtectionException
                {
                try {
                    return select(
                        name
                        );
                    }
                catch (final NameNotFoundException ouch)
                    {
                    return null ;
                    }
                }

            @Override
            public AdqlColumn select(final String name)
            throws ProtectionException, NameNotFoundException
                {
                return new AdqlColumnProxy(
                    base.columns().select(name),
                    AdqlTableProxy.this
                    );
                }

            @Override
            public AdqlColumn create(final BaseColumn<?> base)
            throws ProtectionException
                {
                return new AdqlColumnProxy(
                    base,
                    AdqlTableProxy.this
                    );
                }

            @Override
            public AdqlColumn create(final BaseColumn<?> base, final String name)
            throws ProtectionException
                {
                log.error("AdqlTableProxy can't rename base column [{}][{}]", base.fullname(), name);
                throw new IllegalArgumentException();
                }

            @Override
            public AdqlColumn create(final BaseColumn<?> base, final AdqlColumn.Metadata meta)
            throws ProtectionException
                {
                log.error("AdqlTableProxy can't rename base column [{}][{}]", base.fullname(), meta.adql().name());
                throw new IllegalArgumentException();
                }

            @Override
            public AdqlColumn select(final Identifier ident)
            throws ProtectionException, IdentifierNotFoundException
                {
                return new AdqlColumnProxy(
                    base().columns().select(
                        ident
                        ),
                    AdqlTableProxy.this
                    );
                }

            @Override
            public AdqlColumn inport(final String name)
            throws ProtectionException, NameNotFoundException
                {
                throw new UnsupportedOperationException(
                    "Can't modify a read only table"
                    );
                }
            };
        }

    @Override
    public EntityProtector protector()
        {
        return base.protector();
        }

	@Override
	public BlueQuery bluequery()
    throws ProtectionException
	    {
		return base.bluequery();
	    }

    @Override
    public Long rowcount()
    throws ProtectionException
        {
        return base.rowcount();
        }
    }
