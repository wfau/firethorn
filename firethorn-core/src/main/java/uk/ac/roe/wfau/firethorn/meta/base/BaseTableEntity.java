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
import uk.ac.roe.wfau.firethorn.entity.exception.IdentifierNotFoundException;
import uk.ac.roe.wfau.firethorn.entity.exception.EntityNotFoundException;
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
extends BaseComponentEntity<TableType>
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
        @SelectMethod
        public BaseTable<?,?> select(final Identifier ident)
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

    /**
     * Table factory implementation.
     *
     */
    @Repository
    public static abstract class EntityFactory<SchemaType extends BaseSchema<SchemaType, TableType>, TableType extends BaseTable<TableType, ?>>
    extends AbstractEntityFactory<TableType>
    implements BaseTable.EntityFactory<SchemaType, TableType>
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


    // TODO make this abstract and implement in the entity classes.
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
                    @Override
                    public Long count()
                        {
                        return base().meta().adql().count();
                        }

                    @Override
                    public AdqlTable.AdqlStatus status()
                        {
                        return base().meta().adql().status();
                        }

                    @Override
                    public void status(final AdqlTable.AdqlStatus status)
                        {
                        // TODO Auto-generated method stub
                        }
                    };
                }
            };
        }
    }
