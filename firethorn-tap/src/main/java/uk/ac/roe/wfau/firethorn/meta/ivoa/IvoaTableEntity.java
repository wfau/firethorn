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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.NamedQueries;
import org.hibernate.annotations.NamedQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import uk.ac.roe.wfau.firethorn.entity.annotation.CreateEntityMethod;
import uk.ac.roe.wfau.firethorn.entity.annotation.SelectEntityMethod;
import uk.ac.roe.wfau.firethorn.entity.exception.NotFoundException;
import uk.ac.roe.wfau.firethorn.meta.base.BaseTable;
import uk.ac.roe.wfau.firethorn.meta.base.BaseTableEntity;
import uk.ac.roe.wfau.firethorn.meta.ogsa.OgsaTable;

/**
 *
 *
 */
@Entity()
@Access(
    AccessType.FIELD
    )
@Table(
    name = IvoaTableEntity.DB_TABLE_NAME,
    uniqueConstraints={
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
    protected static final String DB_TABLE_NAME = "IvoaTableEntity";

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
    public static class Factory
    extends BaseTableEntity.Factory<IvoaSchema, IvoaTable>
    implements IvoaTable.Factory
        {

        @Override
        public Class<?> etype()
            {
            return IvoaTableEntity.class ;
            }

        @Override
        @CreateEntityMethod
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
        @SelectEntityMethod
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
        @SelectEntityMethod
        public IvoaTable select(final IvoaSchema parent, final String name)
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

        @Override
        @SelectEntityMethod
        public Iterable<IvoaTable> search(final IvoaSchema parent, final String text)
            {
            return super.iterable(
                super.query(
                    "IvoaTable-search-parent.text"
                    ).setEntity(
                        "parent",
                        parent
                    ).setString(
                        "text",
                        searchParam(
                            text
                            )
                        )
                );
            }

        @Autowired
        protected IvoaColumn.Factory columns;
        @Override
        public IvoaColumn.Factory columns()
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
        fetch = FetchType.EAGER,
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
        return this;
        }
    @Override
    public IvoaTable root()
        {
        return this;
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
            throws NotFoundException
                {
                return factories().ivoa().columns().select(
                    IvoaTableEntity.this,
                    name
                    );
                }

            @Override
            public Iterable<IvoaColumn> search(final String text)
                {
                return factories().ivoa().columns().search(
                    IvoaTableEntity.this,
                    text
                    );
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
    }
