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

import uk.ac.roe.wfau.firethorn.adql.query.GreenQuery;
import uk.ac.roe.wfau.firethorn.blue.BlueQuery;
import uk.ac.roe.wfau.firethorn.entity.exception.EntityNotFoundException;
import uk.ac.roe.wfau.firethorn.entity.exception.IdentifierFormatException;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlTable;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlTable.TableStatus;
import uk.ac.roe.wfau.firethorn.meta.ivoa.IvoaTable;
import uk.ac.roe.wfau.firethorn.meta.jdbc.JdbcTable;

/**
 * {@link BaseTable} implementation.
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
extends TreeComponentEntity<TableType>
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
     */
    @Repository
    public static class EntityResolver
    implements BaseTable.EntityResolver
        {
        @Override
        public BaseTable<?,?> resolve(String link)
        throws IdentifierFormatException, EntityNotFoundException, EntityNotFoundException
            {
            if (adql.matches(link))
                {
                return adql.resolve(
                    link
                    );
                }
            else if (jdbc.matches(link))
                {
                return jdbc.resolve(
                    link
                    );
                }
            if (ivoa.matches(link))
                {
                return ivoa.resolve(
                    link
                    );
                }
            else {
                throw new EntityNotFoundException(
                    "Unable to find match for [" + link + "]"
                    );
                }
            }

        @Autowired
        private AdqlTable.LinkFactory adql;
        @Autowired
        private JdbcTable.LinkFactory jdbc;
        @Autowired
        private IvoaTable.LinkFactory ivoa;
        
        }

    /**
     * {@link BaseTable.EntityFactory} implementation.
     *
     */
    @Repository
    public static abstract class EntityFactory<SchemaType extends BaseSchema<SchemaType, TableType>, TableType extends BaseTable<TableType, ?>>
    extends TreeComponentEntity.EntityFactory<TableType>
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
    protected BaseTableEntity(final CopyDepth depth, final BaseSchema<?,TableType> parent, final String name)
        {
        super(
            depth,
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
    public String fullname()
        {
        return namebuilder().toString();
        }

    @Override
    public abstract BaseTable<?, ?> base();
    @Override
    public abstract BaseTable<?, ?> root();

    @Override
    public abstract BaseResource<?> resource();

    @Override
    public abstract String alias();

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
    protected Long adqlcount ;
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

    /*
     * 
    @Override
    @Deprecated
    public AdqlQuery greenquery()
        {
        return root().greenquery();
        }

    @Override
    public BlueQuery bluequery()
        {
        return root().bluequery();
        }

    @Override
    @Deprecated
    public AdqlQuery greenquery()
        {
        return null;
        }

    @Override
    public BlueQuery bluequery()
        {
        return null;
        }
     * 
     */
    }
