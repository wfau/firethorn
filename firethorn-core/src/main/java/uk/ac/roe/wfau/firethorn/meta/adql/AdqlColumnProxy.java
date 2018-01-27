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

import org.joda.time.DateTime;

import lombok.extern.slf4j.Slf4j;
import uk.ac.roe.wfau.firethorn.access.ProtectionException;
import uk.ac.roe.wfau.firethorn.entity.Identifier;
import uk.ac.roe.wfau.firethorn.entity.ProxyIdentifier;
import uk.ac.roe.wfau.firethorn.entity.access.EntityProtector;
import uk.ac.roe.wfau.firethorn.entity.exception.NameFormatException;
import uk.ac.roe.wfau.firethorn.identity.Identity;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlColumn.Metadata.Adql;
import uk.ac.roe.wfau.firethorn.meta.base.BaseColumn;

/**
 *
 *
 */
@Slf4j
public class AdqlColumnProxy
implements AdqlColumn
    {
    protected AdqlColumn.EntityServices services()
        {
        log.debug("services()");
        return AdqlColumnEntity.EntityServices.instance() ; 
        }

    @Override
    public String link()
        {
        return services().links().link(
            this
            );
        }
    
    /**
     * TODO Move to proxy base class.
     * 
     */
    public AdqlColumn self()
        {
        return this;
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
    throws ProtectionException
        {
        return this.base;
        }

    @Override
    public BaseColumn<?> root()
    throws ProtectionException
        {
        return this.base.root();
        }

    private Identifier ident ;
    @Override
    public Identifier ident()
        {
        if (this.ident == null)
            {
            this.ident = new ProxyIdentifier(
                this.table.ident(),
                this.base.ident()
                );
            }
        return this.ident ;
        }

    @Override
    public Identity owner()
        {
        return this.table.owner();
        }

    @Override
    public DateTime created()
        {
        return this.table.created();
        }

    @Override
    public DateTime modified()
        {
        return this.table.modified();
        }

    @Override
    public String name()
        {
        return this.base.name();
        }

    @Override
    public void name(final String name)
    throws ProtectionException, NameFormatException
        {
        throw new UnsupportedOperationException(
            "Can't change a read only copy"
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
            "Can't change a read only copy"
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
        return this.table.namebuilder().append(".").append(this.name());
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
            "Can't change a read only copy"
            );
        }

    @Override
    public CopyDepth depth()
    throws ProtectionException
        {
        return CopyDepth.PROXY;
        }

    @Override
    public AdqlColumn.Modifier meta()
    throws ProtectionException
        {
        return this.base.meta();
        }

    @Override
    public AdqlTable table()
    throws ProtectionException
        {
        return this.table;
        }

    @Override
    public AdqlSchema schema()
    throws ProtectionException
        {
        return this.table.schema();
        }

    @Override
    public AdqlResource resource()
    throws ProtectionException
        {
        return this.table.resource();
        }

    @Override
    public EntityProtector protector()
        {
        return this.table.protector();
        }

	@Override
	public void update(Adql meta)
    throws ProtectionException
		{
        throw new UnsupportedOperationException(
            "Can't change a read only copy"
            );
		}
    }
