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
package uk.ac.roe.wfau.firethorn.widgeon.adql ;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.extern.slf4j.Slf4j;

import org.hibernate.annotations.NamedQueries;
import org.hibernate.annotations.NamedQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import uk.ac.roe.wfau.firethorn.entity.AbstractFactory;
import uk.ac.roe.wfau.firethorn.entity.annotation.CreateEntityMethod;
import uk.ac.roe.wfau.firethorn.entity.annotation.SelectEntityMethod;
import uk.ac.roe.wfau.firethorn.mallard.AdqlService;
import uk.ac.roe.wfau.firethorn.widgeon.data.DataComponentImpl;
import uk.ac.roe.wfau.firethorn.widgeon.data.DataResource;

/**
 * Hibernate based <code>AdqlResource</code> implementation.
 *
 */
@Slf4j
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
            name  = "adql.resource-select-all",
            query = "FROM AdqlResourceEntity ORDER BY ident desc"
            ),
        @NamedQuery(
            name  = "adql.resource-select-name",
            query = "FROM AdqlResourceEntity WHERE (name = :name) ORDER BY ident desc"
            ),
        @NamedQuery(
            name  = "adql.resource-search-text",
            query = "FROM AdqlResourceEntity WHERE (name LIKE :text) ORDER BY ident desc"
            )
        }
    )
public class AdqlResourceEntity
extends DataComponentImpl
implements AdqlResource
    {

    /**
     * Our persistence table name.
     *
     */
    public static final String DB_TABLE_NAME = "adql_resource" ;

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

        /**
         * Insert a View into the database and create views for each child.
         *
        @CascadeEntityMethod
        protected AdqlResource insert(final AdqlResourceEntity entity)
            {
            super.insert(
                entity
                );
            for (final BaseCatalog<?> catalog : entity.base().catalogs().select())
                {
                this.catalogs().cascade(
                    entity,
                    catalog
                    );
                }
            return entity ;
            }
         */

        @Override
        @CreateEntityMethod
        public AdqlResource create(final String name)
            {
            return this.insert(
                new AdqlResourceEntity(
                    name
                    )
                );
            }

        @Override
        @SelectEntityMethod
        public Iterable<AdqlResource> select()
            {
            return super.iterable(
                super.query(
                    "adql.resource-select-all"
                    )
                );
            }

        @Override
        @SelectEntityMethod
        public Iterable<AdqlResource> select(final String name)
            {
            return super.iterable(
                super.query(
                    "adql.resource-select-name"
                    ).setString(
                        "name",
                        name
                        )
                );
            }

        @Override
        @SelectEntityMethod
        public Iterable<AdqlResource> search(final String text)
            {
            return super.iterable(
                super.query(
                    "adql.resource-search-text"
                    ).setString(
                        "text",
                        searchParam(
                            text
                            )
                        )
                );
            }

        @Autowired
        protected AdqlCatalog.Factory catalogs ;

        @Override
        public AdqlCatalog.Factory catalogs()
            {
            return catalogs ;
            }

        @Autowired
        protected AdqlResource.IdentFactory identifiers ;

        @Override
        public AdqlResource.IdentFactory identifiers()
            {
            return this.identifiers;
            }
        }

    @Override
    public AdqlResource.Catalogs catalogs()
        {
        return new AdqlResource.Catalogs()
            {
            @Override
            public Iterable<AdqlCatalog> select()
                {
                return womble().adql().catalogs().select(
                    AdqlResourceEntity.this
                    ) ;
                }

            @Override
            public AdqlCatalog select(final String name)
                {
                return womble().adql().catalogs().select(
                    AdqlResourceEntity.this,
                    name
                    ) ;
                }

            @Override
            public Iterable<AdqlCatalog> search(final String text)
                {
                return womble().adql().catalogs().search(
                    AdqlResourceEntity.this,
                    text
                    ) ;
                }

            @Override
            public AdqlCatalog create(String name)
                {
                return womble().adql().catalogs().create(
                    AdqlResourceEntity.this,
                    name
                    ) ;
                }
            };
        }

    /**
     * Default constructor needs to be protected not private.
     * http://kristian-domagala.blogspot.co.uk/2008/10/proxy-instantiation-problem-from.html
     *
     */
    protected AdqlResourceEntity()
        {
        super();
        }

    /**
     * Create a new ADQL resource.
     *
     */
    private AdqlResourceEntity(final String name)
        {
        super(name);
        }

    @Override
    public String link()
        {
        return womble().adql().resources().link(
            this
            );
        }

	@Override
	public Services services()
		{
		return new Services()
			{
			@Override
			public AdqlService create(String name)
				{
				return womble().adql().services().create(
					AdqlResourceEntity.this,
					name
					);
				}
			@Override
			public Iterable<AdqlService> select()
				{
				return womble().adql().services().select(
					AdqlResourceEntity.this
					);
				}
			};
		}
    }

