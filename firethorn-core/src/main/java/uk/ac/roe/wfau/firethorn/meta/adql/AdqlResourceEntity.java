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
package uk.ac.roe.wfau.firethorn.meta.adql;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.extern.slf4j.Slf4j;

import org.hibernate.annotations.NamedQueries;
import org.hibernate.annotations.NamedQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import uk.ac.roe.wfau.firethorn.blue.BlueQuery;
import uk.ac.roe.wfau.firethorn.entity.annotation.CreateMethod;
import uk.ac.roe.wfau.firethorn.entity.annotation.SelectMethod;
import uk.ac.roe.wfau.firethorn.entity.exception.NameNotFoundException;
import uk.ac.roe.wfau.firethorn.meta.base.BaseResource;
import uk.ac.roe.wfau.firethorn.meta.base.BaseResourceEntity;
import uk.ac.roe.wfau.firethorn.meta.base.BaseSchema;
import uk.ac.roe.wfau.firethorn.meta.base.BaseTable;

/**
 * {@link AdqlResource} implementation.
 *
 */
@Slf4j
@Entity
@Access(
    AccessType.FIELD
    )
@Table(
    name = AdqlResourceEntity.DB_TABLE_NAME
    )
@NamedQueries(
        {
        @NamedQuery(
            name  = "AdqlResource-select-all",
            query = "FROM AdqlResourceEntity ORDER BY name asc, ident desc"
            )
        }
    )
public class AdqlResourceEntity
extends BaseResourceEntity<AdqlResource,AdqlSchema>
implements AdqlResource
    {
    /**
     * Hibernate table mapping, {@value}}.
     * 
     */
    protected static final String DB_TABLE_NAME = DB_TABLE_PREFIX + "AdqlResourceEntity";

    /**
     * {@link AdqlResource.EntityFactory} implementation.
     *
     */
    @Repository
    public static class EntityFactory
    extends BaseResourceEntity.EntityFactory<AdqlResource>
    implements AdqlResource.EntityFactory
        {
        @Override
        public Class<?> etype()
            {
            return AdqlResourceEntity.class ;
            }

        @Override
        @SelectMethod
        public Iterable<AdqlResource> select()
            {
            return super.iterable(
                super.query(
                    "AdqlResource-select-all"
                    )
                );
            }

        @Override
        @CreateMethod
        public AdqlResource  create(final String name)
            {
            return super.insert(
                new AdqlResourceEntity(
                    name
                    )
                );
            }

        @Override
        @CreateMethod
        public AdqlResource  create()
            {
            return super.insert(
                new AdqlResourceEntity(
                    names().name()
                    )
                );
            }

        @Autowired
        protected AdqlSchema.EntityFactory schemas;
        @Override
        public AdqlSchema.EntityFactory schemas()
            {
            return this.schemas;
            }

        @Autowired
        protected AdqlResource.IdentFactory idents;
        @Override
        public AdqlResource.IdentFactory idents()
            {
            return this.idents;
            }

        @Autowired
        protected AdqlResource.LinkFactory links;
        @Override
        public AdqlResource.LinkFactory links()
            {
            return this.links;
            }

        @Autowired
        protected AdqlResource.NameFactory names;
        public AdqlResource.NameFactory names()
            {
            return this.names;
            }
        }

    protected AdqlResourceEntity()
        {
        super();
        }

    protected AdqlResourceEntity(final String name)
        {
        super(
            name
            );
        }

    @Override
    public AdqlResource.Schemas schemas()
        {
        return new AdqlResource.Schemas()
            {
            @Override
            public Iterable<AdqlSchema> select()
                {
                return factories().adql().schemas().select(
                    AdqlResourceEntity.this
                    );
                }

            @Override
            public AdqlSchema search(final String name)
                {
                return factories().adql().schemas().search(
                    AdqlResourceEntity.this,
                    name
                    );
                }

            @Override
            public AdqlSchema select(final String name)
            throws NameNotFoundException
                {
                return factories().adql().schemas().select(
                    AdqlResourceEntity.this,
                    name
                    );
                }

            @Override
            public AdqlSchema create(final String name)
                {
                return factories().adql().schemas().create(
                    AdqlResourceEntity.this,
                    name
                    );
                }

            @Override
            public AdqlSchema create(final String name, final BaseTable<?, ?> base)
                {
                return factories().adql().schemas().create(
                    AdqlResourceEntity.this,
                    name,
                    base
                    );
                }

            @Override
			public AdqlSchema create(final BaseSchema<?,?> base)
			    {
			    return factories().adql().schemas().create(
                    AdqlResourceEntity.this,
                    base.name(),
                    base
                    );
				}
            @Override
            public AdqlSchema create(final CopyDepth depth, final BaseSchema<?, ?> base)
                {
                return factories().adql().schemas().create(
                    depth,
                    AdqlResourceEntity.this,
                    base.name(),
                    base
                    );
                }

            @Override
            public AdqlSchema create(final String name, final BaseSchema<?,?> base)
                {
                return factories().adql().schemas().create(
                    AdqlResourceEntity.this,
                    name,
                    base
                    );
                }

            @Override
            public AdqlSchema create(final CopyDepth depth, final String name, final BaseSchema<?, ?> base)
                {
                return factories().adql().schemas().create(
                    depth,
                    AdqlResourceEntity.this,
                    name,
                    base
                    );
                }

            @Override
            public AdqlSchema inport(final String name, final BaseSchema<?, ?> base)
                {
                log.debug("schemas().inport(String, BaseSchema)");
                log.debug("  name [{}]", name);
                log.debug("  base [{}]", base.name());
                AdqlSchema schema = search(
                    name
                    );
                if (schema != null)
                    {
                    log.debug("Found existing schema [{}][{}]", schema.ident(), schema.name());
                    }
                else {
                    schema = create(
                        CopyDepth.PARTIAL,
                        name,
                        base
                        );
                    log.debug("Created new schema [{}][{}]", schema.ident(), schema.name());
                    }
                return schema ;
                }
            };
        }

    @Override
    public String link()
        {
        return factories().adql().resources().links().link(
            this
            );
        }

    @Override
    protected void scanimpl()
        {
        // TODO Auto-generated method stub
        }

    @Override
    public AdqlResource.Metadata meta()
        {
        return new AdqlResource.Metadata()
            {
            };
        }

    /**
     * This method does not make sense for an {@link AdqlResource}.
     * @throws UnsupportedOperationException
     *  
     */
    @Override
    public BaseResource.OgsaBaseResources ogsa()
        {
        throw new UnsupportedOperationException(
            "AdqlResource does not support OgsaBaseResources"
            );
        }

    @Override
    public Blues blues()
        {
        return new Blues()
            {
            @Override
            public Iterable<BlueQuery> select()
                {
                return factories.blues().entities().select(
                    AdqlResourceEntity.this
                    );
                }

            @Override
            public BlueQuery create(final String input)
                {
                return factories.blues().entities().create(
                    AdqlResourceEntity.this,
                    input
                    );
                }
            };
        }
    }
