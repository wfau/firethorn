/*
 *  Copyright (C) 2012 Royal Observatory, University of Edinburgh, UK
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
package uk.ac.roe.wfau.firethorn.meta.base;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import uk.ac.roe.wfau.firethorn.adql.query.AdqlQuery;
import uk.ac.roe.wfau.firethorn.entity.AbstractEntityFactory;
import uk.ac.roe.wfau.firethorn.entity.Identifier;
import uk.ac.roe.wfau.firethorn.entity.ProxyIdentifier;
import uk.ac.roe.wfau.firethorn.entity.annotation.SelectMethod;
import uk.ac.roe.wfau.firethorn.entity.exception.EntityNotFoundException;
import uk.ac.roe.wfau.firethorn.entity.exception.IdentifierNotFoundException;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlColumn;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlSchema;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlTable;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlTable.TableStatus;
import uk.ac.roe.wfau.firethorn.meta.ivoa.IvoaTableEntity;

/**
 *
 *
 */
@Slf4j
@Entity
@Access(
    AccessType.FIELD
    )
@Inheritance(
    strategy = InheritanceType.TABLE_PER_CLASS
    )
public abstract class BaseTableEntity<TableType extends BaseTable<TableType, ColumnType>, ColumnType extends BaseColumn<ColumnType>>
extends BaseComponentEntity<TableType>
implements BaseTable<TableType, ColumnType>
    {
    /**
     * Empty count value, {@value}.
     *
     */
    protected static final Long EMPTY_COUNT_VALUE = new Long(0L);

    /**
     * Hibernate column mapping.
     *
     */
    protected static final String DB_ADQL_STATUS_COL = "adqlstatus" ;

    /**
     * Hibernate column mapping.
     *
     */
    protected static final String DB_ADQL_COUNT_COL = "adqlcount" ;

    /**
     * Hibernate column mapping, {@value}.
     *
     */
    protected static final String DB_ADQL_UTYPE_COL = "adqlutype" ;
    
    /**
     * {@link BaseTable.EntityResolver} implementation.
     *
    @Repository
    public static abstract class Resolver<TableType extends BaseTable<TableType, ?>>
    extends AbstractEntityFactory<TableType>
    implements BaseTable.EntityResolver<TableType>
        {
 *
 * Do we ever have proxies that aren't Adql ?
 * 
        @Override
        public Class<?> etype()
            {
            return BaseTableEntity.class;
            }

        //TODO do we need this in the base class ? 
        @Override
        @SelectMethod
        public TableType select(final Identifier ident)
        throws IdentifierNotFoundException
            {
            log.debug("select(Identifier) [{}]", ident);
            if (ident instanceof ProxyIdentifier)
                {
                log.debug("-- proxy identifier");
                final ProxyIdentifier proxy = (ProxyIdentifier) ident;

                log.debug("-- parent schema");
                final AdqlSchema schema = factories().adql().schemas().select(
                    proxy.parent()
                    );

                log.debug("-- proxy table");
                final AdqlTable table = schema.tables().select(
                    proxy.base()
                    );

                // TODO nasty class cast :-(
                return (TableType) table;
                }
            else {
                return super.select(
                    ident
                    );
                }
            }

        @Autowired
        protected BaseTable.IdentFactory idents ;
        @Override
        public BaseTable.IdentFactory idents()
            {
            return this.idents;
            }

        @Autowired
        protected BaseTable.LinkFactory<TableType> links ;
        @Override
        public BaseTable.LinkFactory<TableType> links()
            {
            return this.links;
            }

        //
        // TODO Change this to use a regex to match the alias.
        protected static final String PREFIX = "BASE_" ;

        // TODO Move the parsing to the AliasFactory.   
        @Override
        public TableType resolve(final String alias)
        throws EntityNotFoundException
            {
            return this.select(
                this.idents.ident(
                    alias.substring(
                        PREFIX.length()
                        )
                    )
                );
            }
        }
*/
    
    /**
     * {@link BaseTable.EntityResolver} implementation.
     *
     */
    @Repository
    public static abstract class EntityResolver<SchemaType extends BaseSchema<SchemaType, TableType>, TableType extends BaseTable<TableType, ?>>
    extends AbstractEntityFactory<TableType>
    implements BaseTable.EntityResolver<SchemaType, TableType>
        {
        }
    
    /**
     * {@link BaseTable.EntityFactory} implementation.
     *
     */
    @Repository
    public static abstract class EntityFactory<SchemaType extends BaseSchema<SchemaType, TableType>, TableType extends BaseTable<TableType, ?>>
    extends AbstractEntityFactory<TableType>
    implements BaseTable.EntityFactory<SchemaType, TableType>
        {
        }

    /**
     * Protected constructor.
     *
     *
     */
    protected BaseTableEntity()
        {
        super();
        }

    /**
     * Protected constructor.
     * @todo Remove the parent reference.
     *
     */
    protected BaseTableEntity(final BaseSchema<?,TableType> parent, final String name)
        {
        this(
            CopyDepth.FULL,
            parent,
            name
            );
        }

    /**
     * Protected constructor.
     * @todo Remove the parent reference.
     *
     */
    protected BaseTableEntity(final CopyDepth type, final BaseSchema<?,TableType> parent, final String name)
        {
        super(
            type,
            name
            );
        }

    @Override
    public StringBuilder namebuilder()
        {
        StringBuilder builder = this.schema().namebuilder();
        if (this.name() != null)
            {
            if (builder.length() > 0)
                {
                builder.append(".");
                }
            builder.append(this.name());
            }
        return builder;
        }

    @Override
    public abstract BaseTable<?, ?> base();
    @Override
    public abstract BaseTable<?, ?> root();

    @Override
    public abstract BaseResource<?> resource();

    @Override
    public abstract String alias();

    @Override
    public AdqlQuery query()
        {
        return root().query();
        }

    @Basic(fetch = FetchType.EAGER)
    @Column(
        name = DB_ADQL_STATUS_COL,
        unique = false,
        nullable = true,
        updatable = true
        )
    @Enumerated(
        EnumType.STRING
        )
    protected AdqlTable.TableStatus adqlstatus ;
    protected AdqlTable.TableStatus adqlstatus()
        {
        if (this.adqlstatus != null)
            {
            return this.adqlstatus ;
            }
        else {
            return AdqlTable.TableStatus.UNKNOWN;
            }
        }
    protected void adqlstatus(final AdqlTable.TableStatus next)
        {
        if (next == AdqlTable.TableStatus.UNKNOWN)
            {
            log.warn("Setting AdqlTable.AdqlStatus to UNKNOWN [{}]", this.ident());
            }
        this.adqlstatus = next;
        }

    @Basic(fetch = FetchType.EAGER)
    @Column(
        name = DB_ADQL_COUNT_COL,
        unique = false,
        nullable = true,
        updatable = true
        )
    private Long adqlcount ;
    protected Long adqlcount()
        {
        if (this.adqlcount != null)
            {
            return this.adqlcount;
            }
        else {
            return EMPTY_COUNT_VALUE;
            }
        }
    protected void adqlcount(final Long count)
        {
        this.adqlcount = count;
        }

    @Basic(
        fetch = FetchType.EAGER
        )
    @Column(
        name = DB_ADQL_UTYPE_COL,
        unique = false,
        nullable = true,
        updatable = true
        )
    protected String adqlutype ;
    protected String adqlutype()
        {
        return this.adqlutype ;
        }
    protected void adqlutype(final String value)
        {
        this.adqlutype = emptystr(
            value
            );
        }
    
    /**
     * Generate the {@link AdqlTable.Metadata.Adql adql} metadata.
     *
     */
    protected AdqlTable.Metadata.Adql adqlmeta()
        {
        return new AdqlTable.Metadata.Adql()
            {
            @Override
            public String name()
                {
                return BaseTableEntity.this.name();
                }

            @Override
            public String text()
                {
                return BaseTableEntity.this.text();
                }

            @Override
            public Long count()
                {
                return adqlcount();
                }

            @Override
            public TableStatus status()
                {
                return adqlstatus();
                }
            @Override
            public void status(AdqlTable.TableStatus status)
                {
                adqlstatus(
                    status
                    );
                }

            @Override
            public String utype()
                {
                return adqlutype();
                }
            @Override
            public void utype(final String utype)
                {
                adqlutype(
                    utype
                    );
                }
            };
        }

    @Override
    public AdqlTable.Metadata meta()
        {
        return new AdqlTable.Metadata()
            {
            @Override
            public String name()
                {
                return BaseTableEntity.this.name();
                }
            @Override
            public AdqlTable.Metadata.Adql adql()
                {
                return adqlmeta();
                }
            };
        }
    }
