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

import lombok.extern.slf4j.Slf4j;

import org.joda.time.DateTime;

import uk.ac.roe.wfau.firethorn.entity.Identifier;
import uk.ac.roe.wfau.firethorn.entity.ProxyIdentifier;
import uk.ac.roe.wfau.firethorn.entity.exception.NameFormatException;
import uk.ac.roe.wfau.firethorn.identity.Identity;
import uk.ac.roe.wfau.firethorn.meta.base.BaseColumn;
import uk.ac.roe.wfau.firethorn.spring.ComponentFactories;
import uk.ac.roe.wfau.firethorn.spring.ComponentFactoriesImpl;

/**
 *
 *
 */
@Slf4j
public class AdqlColumnProxy
implements AdqlColumn
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
     * @todo Make this generic and move to a separate class.
     *
     */
    public static class ProxyIterable
    implements Iterable<AdqlColumn>
        {
        final private Iterable<BaseColumn<?>> base ;
        final private AdqlTable table ;

        public ProxyIterable(final Iterable<BaseColumn<?>> base, final AdqlTable table)
            {
            log.debug("ProxyIterable(Iterable<BaseColumn<?>>, AdqlTable)");
            this.base  = base ;
            this.table = table;
            }
        @Override
        public Iterator<AdqlColumn> iterator()
            {
            return new ProxyIterator(
                base.iterator(),
                table
                );
            }

        /**
         * Iterator wrapper.
         *
         */
        private static class ProxyIterator
        implements Iterator<AdqlColumn>
            {
            final private Iterator<BaseColumn<?>> base ;
            final private AdqlTable table ;

            public ProxyIterator(final Iterator<BaseColumn<?>> base, final AdqlTable table)
                {
                this.base  = base ;
                this.table = table;
                }
            @Override
            public boolean hasNext()
                {
                return base.hasNext();
                }
            @Override
            public AdqlColumn next()
                {
                return new AdqlColumnProxy(
                    base.next(),
                    table
                    );
                }
            @Override
            public void remove()
                {
                base.remove();
                }
            }
        }

    /**
     * Protected constructor.
     *
     */
    public AdqlColumnProxy(final BaseColumn<?> base, final AdqlTable table)
        {
        this.base  = base  ;
        this.table = table ;
        }

    /**
     * The parent table.
     *
     */
    private final AdqlTable table ;

    /**
     * The base column.
     *
     */
    private final BaseColumn<?> base ;
    @Override
    public BaseColumn<?> base()
        {
        return base;
        }

    @Override
    public BaseColumn<?> root()
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
                table().ident(),
                base().ident()
                );
            }
        return ident ;
        }

    @Override
    public String link()
        {
        return factories().adql().columns().links().link(
            this
            );
        }

    @Override
    public Identity owner()
        {
        return table().owner();
        }

    @Override
    public DateTime created()
        {
        return table().created();
        }

    @Override
    public DateTime modified()
        {
        return table().modified();
        }

    @Override
    public String name()
        {
        return base().name();
        }

    @Override
    public void name(final String name) throws NameFormatException
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
    public void text(final String text)
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
     * Copied from BaseColumn.
     */
    @Override
    public StringBuilder namebuilder()
        {
        return this.table().namebuilder().append(".").append(this.name());
        }

    @Override
    public Status status()
        {
        return base().status();
        }

    @Override
    public void status(final Status status) throws InvalidStatusException
        {
        throw new UnsupportedOperationException(
            "Can't change a read only copy"
            );
        }

    @Override
    public CopyDepth depth()
        {
        return CopyDepth.PROXY;
        }

    @Override
    public void depth(final CopyDepth copytype)
        {
        throw new UnsupportedOperationException(
            "Can't change a read only copy"
            );
        }

    @Override
    public BaseColumn.Metadata meta()
        {
        return base().meta();
        }

    @Override
    public AdqlTable table()
        {
        return this.table;
        }

    @Override
    public AdqlSchema schema()
        {
        return table().schema();
        }

    @Override
    public AdqlResource resource()
        {
        return table().resource();
        }
    }
