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

import org.hibernate.annotations.NamedQueries;
import org.hibernate.annotations.NamedQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import uk.ac.roe.wfau.firethorn.adql.query.AdqlQuery;
import uk.ac.roe.wfau.firethorn.entity.AbstractFactory;
import uk.ac.roe.wfau.firethorn.entity.annotation.CreateEntityMethod;
import uk.ac.roe.wfau.firethorn.entity.annotation.SelectEntityMethod;
import uk.ac.roe.wfau.firethorn.meta.base.BaseResourceEntity;
import uk.ac.roe.wfau.firethorn.meta.base.BaseSchema;
import uk.ac.roe.wfau.firethorn.meta.base.BaseTable;

@Entity()
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
            ),
        @NamedQuery(
            name  = "AdqlResource-search-text",
            query = "FROM AdqlResourceEntity WHERE (name LIKE :text) ORDER BY ident desc"
            )
        }
    )
public class AdqlResourceEntity
extends BaseResourceEntity<AdqlSchema>
    implements AdqlResource
    {
    protected static final String DB_TABLE_NAME = "AdqlResourceEntity";

    /**
     * Our Entity Factory implementation.
     *
     */
    @Repository
    public static class Factory
    extends AbstractFactory<AdqlResource>
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
        @SelectEntityMethod
        public Iterable<AdqlResource> search(final String text)
            {
            return super.iterable(
                super.query(
                    "AdqlResource-search-text"
                    ).setString(
                        "text",
                        searchParam(
                            text
                            )
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
        }

    protected AdqlResourceEntity()
        {
        super();
        }

    protected AdqlResourceEntity(final String name)
        {
        super(name);
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
            public AdqlSchema select(final String name)
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
			public AdqlSchema create(final String name, final BaseSchema<?,?> base)
			    {
                return factories().adql().schemas().create(
                    AdqlResourceEntity.this,
                    name,
                    base
                    );
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

    }
