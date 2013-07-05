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

import uk.ac.roe.wfau.firethorn.adql.query.AdqlQuery;
import uk.ac.roe.wfau.firethorn.entity.AbstractEntityFactory;
import uk.ac.roe.wfau.firethorn.entity.exception.NotFoundException;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlTable;

/**
 *
 *
 */
@Entity()
@Access(
    AccessType.FIELD
    )
@Table(
    name = BaseTableEntity.DB_TABLE_NAME,
    uniqueConstraints={
        @UniqueConstraint(
            columnNames = {
                BaseComponentEntity.DB_NAME_COL,
                BaseComponentEntity.DB_PARENT_COL,
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
public abstract class BaseTableEntity<TableType extends BaseTable<TableType, ColumnType>, ColumnType extends BaseColumn<ColumnType>>
extends BaseComponentEntity
    implements BaseTable<TableType, ColumnType>
    {
    /**
     * Hibernate database table name.
     *
     */
    protected static final String DB_TABLE_NAME = "BaseTableEntity";

    /**
     * Table resolver implementation.
     *
     */
    @Repository
    public static class Resolver
    extends AbstractEntityFactory<BaseTable<?,?>>
    implements BaseTable.Resolver
        {
        @Override
        public Class<?> etype()
            {
            return BaseTableEntity.class;
            }

        @Autowired
        protected BaseTable.IdentFactory idents ;
        @Override
        public BaseTable.IdentFactory idents()
            {
            return this.idents;
            }

        @Autowired
        protected BaseTable.LinkFactory links ;
        @Override
        public BaseTable.LinkFactory links()
            {
            return this.links;
            }

        //
        // TODO Change this to use a regex to match the alias.
        protected static final String PREFIX = "BASE_" ;

        @Override
        public BaseTable<?,?> resolve(final String alias)
        throws NotFoundException
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

    /**
     * Table factory implementation.
     *
     */
    @Repository
    public static abstract class Factory<SchemaType extends BaseSchema<SchemaType, TableType>, TableType extends BaseTable<TableType, ?>>
    extends AbstractEntityFactory<TableType>
    implements BaseTable.Factory<SchemaType, TableType>
        {
        }

    protected BaseTableEntity()
        {
        super();
        }

    protected BaseTableEntity(final BaseSchema<?,TableType> parent, final String name)
        {
        super(name);
        this.parent = parent;
        }

    @Override
    public StringBuilder fullname()
        {
        return this.schema().fullname().append(".").append(this.name());
        }

    @Override
    public abstract BaseTable<?, ?> base();
    @Override
    public abstract BaseTable<?, ?> root();

    @Index(
        name=DB_TABLE_NAME + "IndexByParent"
        )
    @ManyToOne(
        fetch = FetchType.LAZY,
        targetEntity = BaseSchemaEntity.class
        )
    @JoinColumn(
        name = DB_PARENT_COL,
        unique = false,
        nullable = false,
        updatable = false
        )
    private BaseSchema<?, TableType> parent;
    @Override
    public BaseSchema<?, TableType> schema()
        {
        return this.parent;
        }
    protected void schema(final BaseSchema<?, TableType> schema)
        {
        this.parent = schema;
        }

    @Override
    public abstract BaseResource<?> resource();

    @Override
    public Linked linked()
        {
        return new Linked()
            {
            @Override
            public Iterable<AdqlTable> select()
                {
                //"SELECT FROM AdqlTable WHERE base = :base"
                return null;
                }
            };
        }

    @Override
    public abstract String alias();

    @Override
    public AdqlQuery query()
        {
        return root().query();
        }

    @Override
    public AdqlTable.Metadata meta()
        {
        return new AdqlTable.Metadata()
            {
            @Override
            public AdqlTable.Metadata.AdqlMetadata adql()
                {
                return new AdqlTable.Metadata.AdqlMetadata()
                    {

                    };
                }
            };
        }
    }
