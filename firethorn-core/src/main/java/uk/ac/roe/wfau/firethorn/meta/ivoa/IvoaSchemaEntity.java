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

import java.util.UUID;

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

import uk.ac.roe.wfau.firethorn.entity.AbstractEntityFactory;
import uk.ac.roe.wfau.firethorn.entity.Identifier;
import uk.ac.roe.wfau.firethorn.entity.annotation.CreateEntityMethod;
import uk.ac.roe.wfau.firethorn.entity.annotation.SelectEntityMethod;
import uk.ac.roe.wfau.firethorn.entity.exception.NotFoundException;
import uk.ac.roe.wfau.firethorn.meta.base.BaseSchemaEntity;

/**
 *
 *
 */
@Entity()
@Access(
    AccessType.FIELD
    )
@Table(
    name = IvoaSchemaEntity.DB_TABLE_NAME
    )
@NamedQueries(
        {
        @NamedQuery(
            name  = "IvoaSchema-select-parent",
            query = "FROM IvoaSchemaEntity WHERE parent = :parent ORDER BY name asc, ident desc"
            ),
        @NamedQuery(
            name  = "IvoaSchema-select-parent.name",
            query = "FROM IvoaSchemaEntity WHERE ((parent = :parent) AND (name = :name)) ORDER BY name asc, ident desc"
            ),
        @NamedQuery(
            name  = "IvoaSchema-search-parent.text",
            query = "FROM IvoaSchemaEntity WHERE ((parent = :parent) AND (name LIKE :text)) ORDER BY name asc, ident desc"
            )
        }
    )
public class IvoaSchemaEntity
    extends BaseSchemaEntity<IvoaSchema, IvoaTable>
    implements IvoaSchema
    {
    protected static final String DB_TABLE_NAME = "IvoaSchemaEntity";

    /**
     * Entity factory.
     *
     */
    @Repository
    public static class Factory
    extends AbstractEntityFactory<IvoaSchema>
    implements IvoaSchema.Factory
        {

        @Override
        public Class<?> etype()
            {
            return IvoaSchemaEntity.class ;
            }

        @Override
        @CreateEntityMethod
        public IvoaSchema create(final IvoaResource parent, final String name)
            {
            return this.insert(
                new IvoaSchemaEntity(
                    parent,
                    name
                    )
                );
            }

        @Override
        @SelectEntityMethod
        public Iterable<IvoaSchema> select(final IvoaResource parent)
            {
            return super.list(
                super.query(
                    "IvoaSchema-select-parent"
                    ).setEntity(
                        "parent",
                        parent
                        )
                );
            }

        @Override
        @SelectEntityMethod
        public IvoaSchema select(final IvoaResource parent, final String name)
            {
            return super.first(
                super.query(
                    "IvoaSchema-select-parent.name"
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
        public Iterable<IvoaSchema> search(final IvoaResource parent, final String text)
            {
            return super.iterable(
                super.query(
                    "IvoaSchema-search-parent.text"
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
        protected IvoaTable.Factory tables;
        @Override
        public IvoaTable.Factory tables()
            {
            return this.tables;
            }

        @Autowired
        protected IvoaSchema.IdentFactory idents ;
        @Override
        public IvoaSchema.IdentFactory idents()
            {
            return this.idents ;
            }

        @Autowired
        protected IvoaSchema.LinkFactory links;
        @Override
        public IvoaSchema.LinkFactory links()
            {
            return this.links;
            }

        @Override
        public IvoaSchema select(UUID uuid) throws NotFoundException
            {
            // TODO Auto-generated method stub
            return null;
            }
        }

    protected IvoaSchemaEntity()
        {
        super();
        }

    protected IvoaSchemaEntity(final IvoaResource resource, final String name)
        {
        super(resource, name);
        this.resource = resource;
        }

    @ManyToOne(
        fetch = FetchType.EAGER,
        targetEntity = IvoaResourceEntity.class
        )
    @JoinColumn(
        name = DB_PARENT_COL,
        unique = false,
        nullable = false,
        updatable = false
        )
    private IvoaResource resource;
    @Override
    public IvoaResource resource()
        {
        return this.resource;
        }

    @Override
    public IvoaSchema.Tables tables()
        {
        return new IvoaSchema.Tables()
            {
            @Override
            public Iterable<IvoaTable> select()
                {
                return factories().ivoa().tables().select(
                    IvoaSchemaEntity.this
                    );
                }
            @Override
            public IvoaTable select(final String name)
            throws NotFoundException
                {
                return factories().ivoa().tables().select(
                    IvoaSchemaEntity.this,
                    name
                    );
                }
            @Override
            public Iterable<IvoaTable> search(final String text)
                {
                return factories().ivoa().tables().search(
                    IvoaSchemaEntity.this,
                    text
                    );
                }
            @Override
            public IvoaTable select(Identifier ident)
            throws NotFoundException
                {
                // TODO Auto-generated method stub
                return null;
                }
            };
        }

    @Override
    public String link()
        {
        return factories().ivoa().schemas().links().link(
            this
            );
        }

    @Override
    protected void scanimpl()
        {
        // TODO Auto-generated method stub

        }
    }
