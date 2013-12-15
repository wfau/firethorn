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
import javax.persistence.UniqueConstraint;

import lombok.extern.slf4j.Slf4j;

import org.hibernate.annotations.NamedQueries;
import org.hibernate.annotations.NamedQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import uk.ac.roe.wfau.firethorn.entity.AbstractEntityFactory;
import uk.ac.roe.wfau.firethorn.entity.Identifier;
import uk.ac.roe.wfau.firethorn.entity.annotation.CreateMethod;
import uk.ac.roe.wfau.firethorn.entity.annotation.SelectMethod;
import uk.ac.roe.wfau.firethorn.entity.exception.IdentifierNotFoundException;
import uk.ac.roe.wfau.firethorn.entity.exception.NameNotFoundException;
import uk.ac.roe.wfau.firethorn.entity.exception.NotFoundException;
import uk.ac.roe.wfau.firethorn.meta.base.BaseComponentEntity;
import uk.ac.roe.wfau.firethorn.meta.base.BaseSchema;
import uk.ac.roe.wfau.firethorn.meta.base.BaseSchemaEntity;

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
    name = IvoaSchemaEntity.DB_TABLE_NAME,
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
    protected static final String DB_TABLE_NAME = DB_TABLE_PREFIX + "IvoaSchemaEntity";

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
        @CreateMethod
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
        @SelectMethod
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
        @SelectMethod
        public IvoaSchema select(final IvoaResource parent, final String name)
        throws NameNotFoundException
            {
            try
                {
                return super.single(
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
            catch (final NotFoundException ouch)
                {
                log.debug("Unable to locate schema [{}][{}]", parent.namebuilder().toString(), name);
                throw new NameNotFoundException(
                    name,
                    ouch
                    );
                }
            }

        @Override
        @SelectMethod
        public IvoaSchema search(final IvoaResource parent, final String name)
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
        fetch = FetchType.LAZY,
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
    public BaseSchema<?, ?> base()
        {
        return this ;
        }
    @Override
    public BaseSchema<?, ?> root()
        {
        return this ;
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
            throws NameNotFoundException
                {
                return factories().ivoa().tables().select(
                    IvoaSchemaEntity.this,
                    name
                    );
                }
            @Override
            public IvoaTable search(final String name)
                {
                return factories().ivoa().tables().search(
                    IvoaSchemaEntity.this,
                    name
                    );
                }
            @Override
            public IvoaTable select(final Identifier ident)
            throws IdentifierNotFoundException
                {
                return factories().ivoa().tables().select(
                    ident
                    );
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
