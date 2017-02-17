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

import java.util.Map;

import javax.annotation.PostConstruct;
import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.NamedQueries;
import org.hibernate.annotations.NamedQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import lombok.extern.slf4j.Slf4j;
import uk.ac.roe.wfau.firethorn.adql.query.AdqlQueryBase;
import uk.ac.roe.wfau.firethorn.adql.query.blue.BlueQuery;
import uk.ac.roe.wfau.firethorn.adql.query.blue.BlueTask;
import uk.ac.roe.wfau.firethorn.adql.query.blue.InternalServerErrorException;
import uk.ac.roe.wfau.firethorn.adql.query.blue.InvalidRequestException;
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
                    names.name()
                    )
                );
            }

        @Autowired
        private AdqlResource.NameFactory names;

        }

    /**
     * {@link Entity.EntityServices} implementation.
     * 
     */
    @Slf4j
    @Component
    public static class EntityServices
    implements AdqlResource.EntityServices
        {
        /**
         * Our singleton instance.
         * 
         */
        private static AdqlResourceEntity.EntityServices instance ; 

        /**
         * Our singleton instance.
         * 
         */
        public static EntityServices instance()
            {
            return AdqlResourceEntity.EntityServices.instance ;
            }

        /**
         * Protected constructor.
         * 
         */
        protected EntityServices()
            {
            }
        
        /**
         * Protected initialiser.
         * 
         */
        @PostConstruct
        protected void init()
            {
            log.debug("init()");
            if (AdqlResourceEntity.EntityServices.instance == null)
                {
                AdqlResourceEntity.EntityServices.instance = this ;
                }
            else {
                log.error("Setting instance more than once");
                throw new IllegalStateException(
                    "Setting instance more than once"
                    );
                }
            }
        
        @Autowired
        private AdqlResource.IdentFactory idents;
        @Override
        public AdqlResource.IdentFactory idents()
            {
            return this.idents;
            }

        @Autowired
        private AdqlResource.LinkFactory links;
        @Override
        public AdqlResource.LinkFactory links()
            {
            return this.links;
            }

        @Autowired
        private AdqlResource.NameFactory names;
        @Override
        public AdqlResource.NameFactory names()
            {
            return this.names;
            }

        @Autowired
        private AdqlResource.EntityFactory entities;
        @Override
        public AdqlResource.EntityFactory entities()
            {
            return this.entities;
            }

        @Autowired
        private BlueQuery.EntityFactory blues;
        @Override
        public BlueQuery.EntityFactory blues()
            {
            return this.blues;
            }

        @Autowired
		private AdqlSchema.EntityFactory schemas;
		@Override
		public AdqlSchema.EntityFactory schemas()
			{
			return this.schemas;
			}
        }

    @Override
    protected AdqlResource.EntityFactory factory()
        {
        log.debug("factory()");
        return AdqlResourceEntity.EntityServices.instance().entities() ; 
        }

    @Override
    protected AdqlResource.EntityServices services()
        {
        log.debug("services()");
        return AdqlResourceEntity.EntityServices.instance() ; 
        }

    @Override
    public String link()
        {
        return services().links().link(
            this
            );
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
        log.debug("schemas() for [{}][{}]", ident(), namebuilder());
        scan();
        return new AdqlResource.Schemas()
            {
            @Override
            public Iterable<AdqlSchema> select()
                {
                return factories().adql().schemas().entities().select(
                    AdqlResourceEntity.this
                    );
                }

            @Override
            public AdqlSchema search(final String name)
                {
                return factories().adql().schemas().entities().search(
                    AdqlResourceEntity.this,
                    name
                    );
                }

            @Override
            public AdqlSchema select(final String name)
            throws NameNotFoundException
                {
                return factories().adql().schemas().entities().select(
                    AdqlResourceEntity.this,
                    name
                    );
                }

            @Override
            public AdqlSchema create(final String name)
                {
                return factories().adql().schemas().entities().create(
                    AdqlResourceEntity.this,
                    name
                    );
                }

            @Override
            public AdqlSchema create(final String name, final BaseTable<?, ?> base)
                {
                return factories().adql().schemas().entities().create(
                    AdqlResourceEntity.this,
                    name,
                    base
                    );
                }

            @Override
			public AdqlSchema create(final BaseSchema<?,?> base)
			    {
			    return factories().adql().schemas().entities().create(
                    AdqlResourceEntity.this,
                    base.name(),
                    base
                    );
				}
            @Override
            public AdqlSchema create(final CopyDepth depth, final BaseSchema<?, ?> base)
                {
                return factories().adql().schemas().entities().create(
                    depth,
                    AdqlResourceEntity.this,
                    base.name(),
                    base
                    );
                }

            @Override
            public AdqlSchema create(final String name, final BaseSchema<?,?> base)
                {
                return factories().adql().schemas().entities().create(
                    AdqlResourceEntity.this,
                    name,
                    base
                    );
                }

            @Override
            public AdqlSchema create(final CopyDepth depth, final String name, final BaseSchema<?, ?> base)
                {
                return factories().adql().schemas().entities().create(
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
    protected void scanimpl()
        {
        log.debug("scanimpl() for [{}][{}]", ident(), namebuilder());
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
                return services().blues().select(
                    AdqlResourceEntity.this
                    );
                }

            @Override
            public BlueQuery create(final String input)
                throws InvalidRequestException, InternalServerErrorException
                {
                return services().blues().create(
                    AdqlResourceEntity.this,
                    input
                    );
                }

            @Override
            public BlueQuery create(String input, AdqlQueryBase.Mode mode, AdqlQueryBase.Syntax.Level syntax, AdqlQueryBase.Limits limits, AdqlQueryBase.Delays delays, BlueTask.TaskState next, Long wait, final Map<String, String> triggers)
                throws InvalidRequestException, InternalServerErrorException
                {
                return services().blues().create(
                    AdqlResourceEntity.this,
                    input,
                    mode,
                    syntax,    
                    limits,
                    delays,
                    next,
                    wait,
                    triggers
                    );
                }
            };
        }
    }
