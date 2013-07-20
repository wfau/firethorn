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

import java.util.UUID;

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

import lombok.extern.slf4j.Slf4j;

import org.hibernate.annotations.Index;
import org.hibernate.annotations.NamedQueries;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import uk.ac.roe.wfau.firethorn.adql.query.AdqlQuery;
import uk.ac.roe.wfau.firethorn.entity.AbstractEntityFactory;
import uk.ac.roe.wfau.firethorn.entity.Identifier;
import uk.ac.roe.wfau.firethorn.entity.ProxyIdentifier;
import uk.ac.roe.wfau.firethorn.entity.annotation.SelectEntityMethod;
import uk.ac.roe.wfau.firethorn.entity.exception.NotFoundException;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlSchema;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlTable;

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
extends BaseComponentEntity
    implements BaseTable<TableType, ColumnType>
    {
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

        @Override
        @SelectEntityMethod
        public BaseTable select(final Identifier ident)
        throws NotFoundException
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
                return table;
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

        @Override
        public BaseTable<?, ?> select(final UUID uuid) throws NotFoundException
            {
            // TODO Auto-generated method stub
            return null;
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
        this(
            CopyDepth.FULL,
            parent,
            name
            );
        }

    protected BaseTableEntity(final CopyDepth type, final BaseSchema<?,TableType> parent, final String name)
        {
        super(
            type,
            name
            );
        //this.parent = parent;
        }

    @Override
    public StringBuilder namebuilder()
        {
        return this.schema().namebuilder().append(".").append(this.name());
        }

    @Override
    public abstract BaseTable<?, ?> base();
    @Override
    public abstract BaseTable<?, ?> root();

    /*
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
     */

    @Override
    public abstract BaseResource<?> resource();

    @Override
    @Deprecated
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
