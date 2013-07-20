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

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.MapKey;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import lombok.extern.slf4j.Slf4j;

import org.hibernate.annotations.NamedQueries;
import org.hibernate.annotations.NamedQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import uk.ac.roe.wfau.firethorn.entity.AbstractEntityFactory;
import uk.ac.roe.wfau.firethorn.entity.annotation.CreateEntityMethod;
import uk.ac.roe.wfau.firethorn.entity.annotation.SelectEntityMethod;
import uk.ac.roe.wfau.firethorn.entity.exception.NotFoundException;
import uk.ac.roe.wfau.firethorn.meta.base.BaseResourceEntity;
import uk.ac.roe.wfau.firethorn.meta.base.BaseSchema;
import uk.ac.roe.wfau.firethorn.meta.base.BaseTable;

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
extends BaseResourceEntity<AdqlSchema>
    implements AdqlResource
    {
    protected static final String DB_TABLE_NAME = DB_TABLE_PREFIX + "AdqlResourceEntity";

    /**
     * Our Entity Factory implementation.
     *
     */
    @Repository
    public static class Factory
    extends AbstractEntityFactory<AdqlResource>
    implements AdqlResource.Factory
        {

        @Override
        public Class<?> etype()
            {
            return AdqlResourceEntity.class ;
            }

        @Override
        @SelectEntityMethod
        public Iterable<AdqlResource> select()
            {
            return super.iterable(
                super.query(
                    "AdqlResource-select-all"
                    )
                );
            }

        @Override
        @CreateEntityMethod
        public AdqlResource  create(final String name)
            {
            return super.insert(
                new AdqlResourceEntity(
                    name
                    )
                );
            }

        @Autowired
        protected AdqlSchema.Factory schemas;
        @Override
        public AdqlSchema.Factory schemas()
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

        @Override
        public AdqlResource select(final UUID uuid) throws NotFoundException
            {
            // TODO Auto-generated method stub
            return null;
            }
        }

    protected AdqlResourceEntity()
        {
        super();
        }

    protected AdqlResourceEntity(final String name)
        {
        super(name);
        }

    @OrderBy(
        "name ASC"
        )
    @MapKey(
        name="name"
        )
    @OneToMany(
        fetch   = FetchType.LAZY,
        mappedBy = "resource",
        targetEntity = AdqlSchemaEntity.class
        )
    private Map<String, AdqlSchema> children = new LinkedHashMap<String, AdqlSchema>();
    
    @Override
    public AdqlResource.Schemas schemas()
        {
        return new AdqlResource.Schemas()
            {
            @Override
            public Iterable<AdqlSchema> select()
                {
                return children.values();
                }

            @Override
            public AdqlSchema select(final String name)
            throws NotFoundException
                {
                AdqlSchema schema = children.get(name);
                if (schema != null)
                    {
                    return schema ;
                    }
                else {
                    throw new NotFoundException(
                        name
                        );
                    }
                }

            @Override
            public AdqlSchema create(final String name)
                {
                AdqlSchema schema = factories().adql().schemas().create(
                    AdqlResourceEntity.this,
                    name
                    );
                children.put(
                    schema.name(),
                    schema
                    );
                return schema ;
                }

            @Override
            public AdqlSchema create(final String name, final BaseTable<?, ?> base)
                {
                AdqlSchema schema = factories().adql().schemas().create(
                    AdqlResourceEntity.this,
                    name,
                    base
                    );
                children.put(
                    schema.name(),
                    schema
                    );
                return schema ;
                }

            @Override
			public AdqlSchema create(final BaseSchema<?,?> base)
			    {
			    AdqlSchema schema = factories().adql().schemas().create(
                    AdqlResourceEntity.this,
                    base.name(),
                    base
                    );
                children.put(
                    schema.name(),
                    schema
                    );
                return schema ;
				}
            @Override
            public AdqlSchema create(final CopyDepth depth, final BaseSchema<?, ?> base)
                {
                AdqlSchema schema = factories().adql().schemas().create(
                    depth,
                    AdqlResourceEntity.this,
                    base.name(),
                    base
                    );
                children.put(
                    schema.name(),
                    schema
                    );
                return schema ;
                }

            @Override
            public AdqlSchema create(final String name, final BaseSchema<?,?> base)
                {
                AdqlSchema schema = factories().adql().schemas().create(
                    AdqlResourceEntity.this,
                    name,
                    base
                    );
                children.put(
                    schema.name(),
                    schema
                    );
                return schema ;
                }

            @Override
            public AdqlSchema create(final CopyDepth depth, final String name, final BaseSchema<?, ?> base)
                {
                AdqlSchema schema = factories().adql().schemas().create(
                    depth,
                    AdqlResourceEntity.this,
                    name,
                    base
                    );
                children.put(
                    schema.name(),
                    schema
                    );
                return schema ;
                }

            @Override
            public Iterable<AdqlSchema> search(final String text)
                {
                return factories().adql().schemas().search(
                    AdqlResourceEntity.this,
                    text
                    );
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

    }
