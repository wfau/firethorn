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
package uk.ac.roe.wfau.firethorn.meta.ivoa;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import lombok.extern.slf4j.Slf4j;

import org.hibernate.annotations.NamedQueries;
import org.hibernate.annotations.NamedQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import uk.ac.roe.wfau.firethorn.entity.AbstractEntityTracker;
import uk.ac.roe.wfau.firethorn.entity.Identifier;
import uk.ac.roe.wfau.firethorn.entity.annotation.CreateMethod;
import uk.ac.roe.wfau.firethorn.entity.annotation.SelectMethod;
import uk.ac.roe.wfau.firethorn.entity.exception.DuplicateEntityException;
import uk.ac.roe.wfau.firethorn.entity.exception.IdentifierNotFoundException;
import uk.ac.roe.wfau.firethorn.entity.exception.NameNotFoundException;
import uk.ac.roe.wfau.firethorn.entity.exception.EntityNotFoundException;
import uk.ac.roe.wfau.firethorn.meta.base.BaseComponentEntity;
import uk.ac.roe.wfau.firethorn.meta.base.BaseTableEntity;
import uk.ac.roe.wfau.firethorn.meta.jdbc.JdbcTableEntity;

/**
 *
 *
 */
@Slf4j
@Entity
@Access(
    AccessType.FIELD
    )
@Table(
    name = IvoaTableEntity.DB_TABLE_NAME,
    indexes={
        @Index(
            columnList = IvoaTableEntity.DB_PARENT_COL
            )
        },
    uniqueConstraints={
        @UniqueConstraint(
            columnNames = {
                BaseComponentEntity.DB_NAME_COL,
                BaseComponentEntity.DB_PARENT_COL
                }
            )
        }
    )
@NamedQueries(
        {
        @NamedQuery(
            name  = "IvoaTable-select-parent",
            query = "FROM IvoaTableEntity WHERE parent = :parent ORDER BY ident desc"
            ),
        @NamedQuery(
            name  = "IvoaTable-select-parent.name",
            query = "FROM IvoaTableEntity WHERE ((parent = :parent) AND (name = :name)) ORDER BY ident desc"
            ),
        @NamedQuery(
            name  = "IvoaTable-search-parent.text",
            query = "FROM IvoaTableEntity WHERE ((parent = :parent) AND (name LIKE :text)) ORDER BY ident desc"
            )
        }
    )
public class IvoaTableEntity
    extends BaseTableEntity<IvoaTable, IvoaColumn>
    implements IvoaTable
    {
    /**
     * Hibernate database table name.
     *
     */
    protected static final String DB_TABLE_NAME = DB_TABLE_PREFIX + "IvoaTableEntity";

    /**
     * Entity builder.
     *
     */
    public static abstract class Builder
    extends AbstractEntityTracker<IvoaTable>
    implements IvoaTable.Builder
        {
        public Builder(final Iterable<IvoaTable> source)
            {
            this.init(
                source
                );
            }
        }
    
    /**
     * Alias factory implementation.
     *
     */
    @Repository
    public static class AliasFactory
    implements IvoaTable.AliasFactory
        {
        /**
         * The alias prefix for this type.
         *
         */
        protected static final String PREFIX = "IVOA_" ;

        @Override
        public String alias(final IvoaTable table)
            {
            return PREFIX + table.ident();
            }
        }

    /**
     * Table factory implementation.
     *
     */
    @Repository
    public static class EntityFactory
    extends BaseTableEntity.EntityFactory<IvoaSchema, IvoaTable>
    implements IvoaTable.EntityFactory
        {

        @Override
        public Class<?> etype()
            {
            return IvoaTableEntity.class ;
            }

        @Override
        @CreateMethod
        public IvoaTable create(final IvoaSchema parent, final String name)
            {
            return this.insert(
                new IvoaTableEntity(
                    parent,
                    name
                    )
                );
            }

        @Override
        @SelectMethod
        public Iterable<IvoaTable> select(final IvoaSchema parent)
            {
            return super.list(
                super.query(
                    "IvoaTable-select-parent"
                    ).setEntity(
                        "parent",
                        parent
                        )
                );
            }

        @Override
        @SelectMethod
        public IvoaTable select(final IvoaSchema parent, final String name)
        throws NameNotFoundException
            {
            try
                {
                return super.single(
                    super.query(
                        "IvoaTable-select-parent.name"
                        ).setEntity(
                            "parent",
                            parent
                        ).setString(
                            "name",
                            name
                        )
                    );
                }
            catch (final EntityNotFoundException ouch)
                {
                log.debug("Unable to locate table [{}][{}]", parent.namebuilder().toString(), name);
                throw new NameNotFoundException(
                    name,
                    ouch
                    );
                }
            }

        @Override
        @SelectMethod
        public IvoaTable search(final IvoaSchema parent, final String name)
            {
            return super.first(
                super.query(
                    "IvoaTable-select-parent.name"
                    ).setEntity(
                        "parent",
                        parent
                    ).setString(
                        "name",
                        name
                    )
                );
            }

        @Autowired
        protected IvoaColumn.EntityFactory columns;
        @Override
        public IvoaColumn.EntityFactory columns()
            {
            return this.columns;
            }

        @Autowired
        protected IvoaTable.IdentFactory idents;
        @Override
        public IvoaTable.IdentFactory idents()
            {
            return this.idents;
            }

        @Autowired
        protected IvoaTable.LinkFactory links;
        @Override
        public IvoaTable.LinkFactory links()
            {
            return this.links;
            }

        @Autowired
        protected IvoaTable.AliasFactory aliases;
        @Override
        public IvoaTable.AliasFactory aliases()
            {
            return this.aliases;
            }

        @Override
        public IvoaTable.NameFactory<IvoaTable> names()
            {
            // TODO Auto-generated method stub
            return null;
            }
        }

    protected IvoaTableEntity()
        {
        super();
        }

    protected IvoaTableEntity(final IvoaSchema schema, final String name)
        {
        super(schema, name);
        this.schema = schema;
        }

    @ManyToOne(
        fetch = FetchType.LAZY,
        targetEntity = IvoaSchemaEntity.class
        )
    @JoinColumn(
        name = DB_PARENT_COL,
        unique = false,
        nullable = false,
        updatable = false
        )
    private IvoaSchema schema;
    @Override
    public IvoaSchema schema()
        {
        return this.schema;
        }
    @Override
    public IvoaResource resource()
        {
        return this.schema().resource();
        }

    @Override
    public IvoaTable base()
        {
        return self();
        }
    @Override
    public IvoaTable root()
        {
        return self();
        }

    @Override
    public IvoaTable.Columns columns()
        {
        return new IvoaTable.Columns()
            {
            @Override
            public Iterable<IvoaColumn> select()
                {
                return factories().ivoa().columns().select(
                    IvoaTableEntity.this
                    );
                }

            @Override
            public IvoaColumn select(final String name)
            throws NameNotFoundException
                {
                return factories().ivoa().columns().select(
                    IvoaTableEntity.this,
                    name
                    );
                }

            @Override
            public IvoaColumn search(final String name)
                {
                return factories().ivoa().columns().search(
                    IvoaTableEntity.this,
                    name
                    );
                }

            @Override
            public IvoaColumn select(final Identifier ident)
            throws IdentifierNotFoundException
                {
                // TODO Add reference to this
                return factories().ivoa().columns().select(
                    ident
                    );
                }

            @Override
            public IvoaColumn create(final String name)
                {
                return factories().ivoa().columns().create(
                    IvoaTableEntity.this,
                    name
                    );
                }
            
            @Override
            public IvoaColumn.Builder builder()
                {
                return new IvoaColumnEntity.Builder(this.select())
                    {
                    @Override
                    protected void finish(IvoaColumn entity)
                        {
                        
                        }
                    };
                }
            };
        }

    @Override
    public String link()
        {
        return factories().ivoa().tables().links().link(
            this
            );
        }

    @Override
    public String alias()
        {
        return factories().ivoa().tables().aliases().alias(
            this
            );
        }

    @Override
    protected void scanimpl()
        {
        // TODO Auto-generated method stub

        }
    }
