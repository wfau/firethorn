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
package uk.ac.roe.wfau.firethorn.tuesday;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.Index;
import org.hibernate.annotations.NamedQueries;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import uk.ac.roe.wfau.firethorn.common.entity.AbstractFactory;
import uk.ac.roe.wfau.firethorn.common.entity.Identifier;
import uk.ac.roe.wfau.firethorn.common.entity.exception.NotFoundException;
import uk.ac.roe.wfau.firethorn.tuesday.TuesdayBaseTable.IdentFactory;

/**
 *
 *
 */
@Entity()
@Access(
    AccessType.FIELD
    )
@Table(
    name = TuesdayBaseTableEntity.DB_TABLE_NAME,
    uniqueConstraints={
        @UniqueConstraint(
            columnNames = {
                TuesdayBaseComponentEntity.DB_NAME_COL,
                TuesdayBaseComponentEntity.DB_PARENT_COL,
                }
            )
        }
    )
@Inheritance(
    strategy = InheritanceType.JOINED
    )
@NamedQueries(
        {
        }
    )
public abstract class TuesdayBaseTableEntity<TableType extends TuesdayBaseTable<TableType, ColumnType>, ColumnType extends TuesdayBaseColumn<ColumnType>>
extends TuesdayBaseComponentEntity
    implements TuesdayBaseTable<TableType, ColumnType>
    {
    protected static final String DB_TABLE_NAME = "TuesdayBaseTableEntity";

    /**
     * Alias factory implementation.
     *
     */
    @Repository
    public static class AliasFactory
    extends AbstractFactory<TuesdayBaseTable<?,?>>
    implements TuesdayBaseTable.AliasFactory
        {
        @Override
        public Class<?> etype()
            {
            return TuesdayBaseTableEntity.class;
            }

        @Autowired
        protected TuesdayBaseTable.IdentFactory idents ;
        @Override
        public TuesdayBaseTable.IdentFactory idents()
            {
            return this.idents;
            }

        protected static final String PREFIX = "TABLE_" ;
        
        @Override
        public String alias(final TuesdayBaseTable<?,?> table)
            {
            return PREFIX + table.ident().toString();
            }

        public Identifier ident(final String alias)
            {
            return this.idents.ident(
                alias.substring(
                    PREFIX.length()
                    )
                );
            }

        @Override
        public TuesdayBaseTable<?,?> select(final String alias)
        throws NotFoundException
            {
            return this.select(
                this.ident(
                    alias
                    )
                );
            }

        @Override
        public LinkFactory<TuesdayBaseTable<?, ?>> links()
            {
            return null;
            }
        }

    /**
     * Table factory implementation.
     *
     */
    @Repository
    public static abstract class Factory<SchemaType extends TuesdayBaseSchema<SchemaType, TableType>, TableType extends TuesdayBaseTable<TableType, ?>>
    extends AbstractFactory<TableType>
    implements TuesdayBaseTable.Factory<SchemaType, TableType>
        {
/*
        @Autowired
        protected TuesdayBaseTable.AliasFactory aliases;
        @Override
        public TuesdayBaseTable.AliasFactory aliases()
            {
            return this.aliases;
            }
 */
        }

    protected TuesdayBaseTableEntity()
        {
        super();
        }

    protected TuesdayBaseTableEntity(TuesdayBaseSchema<?,TableType> parent, String name)
        {
        super(name);
        this.parent = parent;
        }

    @Basic(fetch = FetchType.EAGER)
    @Column(
        name = DB_TYPE_COL,
        unique = false,
        nullable = true,
        updatable = true
        )
    private String type ;
    @Override
    public String type()
        {
        return this.type;
        }
    @Override
    public void type(String type)
        {
        this.type = type;
        }

    @Basic(fetch = FetchType.EAGER)
    @Column(
        name = DB_SIZE_COL,
        unique = false,
        nullable = true,
        updatable = true
        )
    private Integer size ;
    @Override
    public Integer size()
        {
        return this.size;
        }
    @Override
    public void size(Integer size)
        {
        this.size = size;
        }

    @Basic(fetch = FetchType.EAGER)
    @Column(
        name = DB_UCD_COL,
        unique = false,
        nullable = true,
        updatable = true
        )
    private String ucd;
    @Override
    public String ucd()
        {
        return this.ucd;
        }
    @Override
    public void ucd(String ucd)
        {
        this.ucd = ucd;
        }

    @Override
    public StringBuilder fullname()
        {
        return this.schema().fullname().append(".").append(this.name());
        }

    @Override
    public abstract TuesdayOgsaTable<?, ?> ogsa();

    @Index(
        name=DB_TABLE_NAME + "IndexByParent"
        )
    @ManyToOne(
        fetch = FetchType.EAGER,
        targetEntity = TuesdayBaseSchemaEntity.class
        )
    @JoinColumn(
        name = DB_PARENT_COL,
        unique = false,
        nullable = false,
        updatable = false
        )
    private TuesdayBaseSchema<?, TableType> parent;
    @Override
    public TuesdayBaseSchema<?, TableType> schema()
        {
        return this.parent;
        }
    protected void schema(TuesdayBaseSchema<?, TableType> schema)
        {
        this.parent = schema;
        }

    @Override
    public abstract TuesdayBaseResource<?> resource();

    @Override
    public Linked linked()
        {
        return new Linked()
            {
            @Override
            public Iterable<TuesdayAdqlTable> select()
                {
                //"SELECT FROM TuesdayAdqlTable WHERE base = :base"
                return null;
                }
            };
        }

    @Override
    public String alias()
        {
        return factories().base().tables().alias(
            this
            );
        }
    }
