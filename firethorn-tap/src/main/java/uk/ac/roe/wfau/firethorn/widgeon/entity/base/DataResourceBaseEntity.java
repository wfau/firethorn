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
package uk.ac.roe.wfau.firethorn.widgeon.entity.base ;

import lombok.extern.slf4j.Slf4j;

import java.net.URI;
import java.net.URL;

import javax.sql.DataSource;

import javax.persistence.Table;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.Index;
import org.hibernate.annotations.NamedQuery;
import org.hibernate.annotations.NamedQueries;

import org.springframework.stereotype.Repository;
import org.springframework.beans.factory.annotation.Autowired;  

import uk.ac.roe.wfau.firethorn.common.womble.Womble;

import uk.ac.roe.wfau.firethorn.common.entity.Identifier;
import uk.ac.roe.wfau.firethorn.common.entity.AbstractEntity;
import uk.ac.roe.wfau.firethorn.common.entity.AbstractFactory;

import uk.ac.roe.wfau.firethorn.common.entity.exception.*;
import uk.ac.roe.wfau.firethorn.common.entity.annotation.*;

import uk.ac.roe.wfau.firethorn.widgeon.DataResource;
import uk.ac.roe.wfau.firethorn.widgeon.DataResourceBase;
import uk.ac.roe.wfau.firethorn.widgeon.DataResourceView;
import uk.ac.roe.wfau.firethorn.widgeon.DataResourceEntity;
import uk.ac.roe.wfau.firethorn.widgeon.entity.AbstractDataResourceEntity;

/**
 * DataResource implementations.
 *
 */
@Slf4j
@Entity()
@Access(
    AccessType.FIELD
    )
@Table(
    name = DataResourceBaseEntity.DB_TABLE_NAME
/*
    uniqueConstraints={
        @UniqueConstraint(
            columnNames = {
                AbstractEntity.DB_NAME_COL
                }
            )
        }
 */
    )
@NamedQueries(
        {
        @NamedQuery(
            name  = "resource.base.entity-select-all",
            query = "FROM DataResourceBaseEntity ORDER BY ident desc"
            ),
            @NamedQuery(
                name  = "resource.base.entity-select-name",
                query = "FROM DataResourceBaseEntity WHERE (name = :name) ORDER BY ident desc"
                ),
        @NamedQuery(
            name  = "resource.base.entity-search-text",
            query = "FROM DataResourceBaseEntity WHERE (name LIKE :text) ORDER BY ident desc"
            )
        }
    )
public class DataResourceBaseEntity
extends AbstractDataResourceEntity
implements DataResourceBase
    {

    /**
     * Our persistence table name.
     * 
     */
    public static final String DB_TABLE_NAME = "resource_base_entity" ;

    /**
     * Our Entity Factory implementation.
     *
     */
    @Repository
    public static class Factory
    extends AbstractFactory<DataResourceBase>
    implements DataResourceBase.Factory
        {

        @Override
        public Class etype()
            {
            return DataResourceBaseEntity.class ;
            }

        @Override
        @SelectEntityMethod
        public Iterable<DataResourceBase> select()
            {
            return super.iterable(
                super.query(
                    "resource.base.entity-select-all"
                    )
                );
            }

        @Override
        @SelectEntityMethod
        public DataResourceBase select(final String name)
        throws NameNotFoundException
            {
            DataResourceBase result = super.first(
                super.query(
                    "resource.base.entity-select-name"
                    ).setString(
                        "name",
                        name
                        )
                );
            if (result != null)
                {
                return result;
                }
            else {
                throw new NameNotFoundException(
                    name
                    );
                }
            }

        @Override
        @SelectEntityMethod
        public Iterable<DataResourceBase> search(final String text)
            {
            String match = new StringBuilder(text).append("%").toString();
            return super.iterable(
                super.query(
                    "resource.base.entity-search-text"
                    ).setString(
                        "text",
                        match 
                        )
                );
            }

        @Override
        @CreateEntityMethod
        public DataResourceBase create(final String name, final URI uri)
            {
            return super.insert(
                new DataResourceBaseEntity(
                    name,
                    uri
                    )
                );
            }

        @Override
        @CreateEntityMethod
        public DataResourceBase create(final String name, final URL url)
            {
            return super.insert(
                new DataResourceBaseEntity(
                    name,
                    url
                    )
                );
            }

        @Override
        @CreateEntityMethod
        public DataResourceBase create(final String name, final DataSource src)
            {
            return super.insert(
                new DataResourceBaseEntity(
                    name,
                    src
                    )
                );
            }

        /**
         * Our Autowired View factory.
         * 
         */
        @Autowired
        protected DataResourceView.Factory views ;

        @Override
        public DataResourceView.Factory views()
            {
            return this.views ;
            }

        /**
         * Our Autowired Catalog factory.
         * 
         */
        @Autowired
        protected DataResourceBase.Catalog.Factory catalogs ;

        @Override
        public DataResourceBase.Catalog.Factory catalogs()
            {
            return this.catalogs ;
            }
        }

    @Override
    public DataResourceBase.Views views() 
        {
        return new DataResourceBase.Views()
            {
            @Override
            public DataResourceView create(String name)
                {
                return womble().resources().views().create(
                    DataResourceBaseEntity.this,
                    name
                    );
                }

            @Override
            public Iterable<DataResourceView> select()
                {
                return womble().resources().views().select(
                    DataResourceBaseEntity.this
                    );
                }

            @Override
            public DataResourceView select(String name)
            throws NameNotFoundException
                {
                return womble().resources().views().select(
                    DataResourceBaseEntity.this,
                    name
                    );
                }
            };
        }

    @Override
    public DataResourceBase.Catalogs catalogs()
        {
        return new DataResourceBase.Catalogs()
            {
            @Override
            public DataResourceBase.Catalog create(final String name)
                {
                return womble().resources().catalogs().create(
                    DataResourceBaseEntity.this,
                    name
                    ) ;
                }

            @Override
            public Iterable<DataResourceBase.Catalog> select()
                {
                return womble().resources().catalogs().select(
                    DataResourceBaseEntity.this
                    );
                }

            @Override
            public DataResourceBase.Catalog select(final String name)
            throws NameNotFoundException
                {
                return womble().resources().catalogs().select(
                    DataResourceBaseEntity.this,
                    name
                    );
                }

            @Override
            public DataResourceBase.Catalog search(final String name)
                {
                return womble().resources().catalogs().search(
                    DataResourceBaseEntity.this,
                    name
                    );
                }
            };
        }

    /**
     * Default constructor needs to be protected not private.
     * http://kristian-domagala.blogspot.co.uk/2008/10/proxy-instantiation-problem-from.html
     *
     */
    protected DataResourceBaseEntity()
        {
        super();
        }

    /**
     * Create a new DataResource from VOSI metadata.
     *
     */
    private DataResourceBaseEntity(final String name, final URI source)
        {
        super(name);
        this.init(
            source
            );
        }

    /**
     * Create a new DataResource from VOSI metadata.
     *
     */
    private DataResourceBaseEntity(final String name, final URL source)
        {
        super(name);
        this.init(
            source
            );
        }

    /**
     * Create a new DataResource from JDBC metadata.
     *
     */
    private DataResourceBaseEntity(final String name, final DataSource source)
        {
        super(name);
        this.init(
            source
            );
        }

    @Override
    public void name(final String name)
    throws NameFormatException
        {
        if ((name != null) && (name.trim().length() > 0))
            {
            this.name = name.trim() ;
            }
        else {
            throw new NameFormatException(
                name
                );
            }
        }

    /**
     * Initialise our data from the JDBC metadata.
     *
     */
    private void init(final DataSource source)
        {
        log.debug("init(DataSource)");
        // Process the JDBC metadata.
        }

    /**
     * Initialise our data from the VOSI metadata.
     *
     */
    private void init(final URI uri)
        {
        log.debug("init(URI)");
        // Resolve the URI into a VOSI endpoint URL.
        }

    /**
     * Initialise our data from the VOSI metadata.
     *
     */
    private void init(final URL url)
        {
        log.debug("init(URL)");
        // Process the VOSI metadata.
        }
    }

