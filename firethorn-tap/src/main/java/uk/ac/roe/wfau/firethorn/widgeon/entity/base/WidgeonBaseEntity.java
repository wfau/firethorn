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

import uk.ac.roe.wfau.firethorn.widgeon.Widgeon;
import uk.ac.roe.wfau.firethorn.widgeon.WidgeonBase;
import uk.ac.roe.wfau.firethorn.widgeon.WidgeonView;
import uk.ac.roe.wfau.firethorn.widgeon.WidgeonStatus;
import uk.ac.roe.wfau.firethorn.widgeon.entity.WidgeonStatusEntity;

/**
 * Widgeon implementations.
 *
 */
@Slf4j
@Entity()
@Access(
    AccessType.FIELD
    )
@Table(
    name = WidgeonBaseEntity.DB_TABLE_NAME
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
            name  = "widgeon.base.entity-select-all",
            query = "FROM WidgeonBaseEntity ORDER BY ident desc"
            ),
        @NamedQuery(
            name  = "widgeon.base.entity-select-name",
            query = "FROM WidgeonBaseEntity WHERE (name = :name) ORDER BY ident desc"
            )
        }
    )
public class WidgeonBaseEntity
extends WidgeonStatusEntity
implements WidgeonBase
    {

    /**
     * Our persistence table name.
     * 
     */
    public static final String DB_TABLE_NAME = "widgeon_base_entity" ;

    /**
     * Our Entity Factory implementation.
     *
     */
    @Repository
    public static class Factory
    extends AbstractFactory<WidgeonBase>
    implements WidgeonBase.Factory
        {

        @Override
        public Class etype()
            {
            return WidgeonBaseEntity.class ;
            }

        @Override
        @SelectEntityMethod
        public Iterable<WidgeonBase> select()
            {
            return super.iterable(
                super.query(
                    "widgeon.base.entity-select-all"
                    )
                );
            }

        @Override
        @SelectEntityMethod
        public WidgeonBase select(String name)
        throws NameNotFoundException
            {
            WidgeonBase result = this.search(
                name
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

        /**
         * Search for a named Widgeon.
         *
         */
        @SelectEntityMethod
        protected WidgeonBase search(String name)
            {
            return super.first(
                super.query(
                    "widgeon.base.entity-select-name"
                    ).setString(
                        "name",
                        name
                        )
                );
            }

        @Override
        @CreateEntityMethod
        public WidgeonBase create(final String name, final URI uri)
            {
            return super.insert(
                new WidgeonBaseEntity(
                    name,
                    uri
                    )
                );
            }

        @Override
        @CreateEntityMethod
        public WidgeonBase create(final String name, final URL url)
            {
            return super.insert(
                new WidgeonBaseEntity(
                    name,
                    url
                    )
                );
            }

        @Override
        @CreateEntityMethod
        public WidgeonBase create(final String name, final DataSource src)
            {
            return super.insert(
                new WidgeonBaseEntity(
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
        protected WidgeonView.Factory views ;

        @Override
        public WidgeonView.Factory views()
            {
            return this.views ;
            }

        /**
         * Our Autowired Catalog factory.
         * 
         */
        @Autowired
        protected WidgeonBase.Catalog.Factory catalogs ;

        @Override
        public WidgeonBase.Catalog.Factory catalogs()
            {
            return this.catalogs ;
            }
        }

    @Override
    public WidgeonBase.Views views() 
        {
        return new WidgeonBase.Views()
            {
            @Override
            public WidgeonView create(String name)
                {
                return womble().widgeons().views().create(
                    WidgeonBaseEntity.this,
                    name
                    );
                }

            @Override
            public Iterable<WidgeonView> select()
                {
                return womble().widgeons().views().select(
                    WidgeonBaseEntity.this
                    );
                }

            @Override
            public WidgeonView select(String name)
            throws NameNotFoundException
                {
                return womble().widgeons().views().select(
                    WidgeonBaseEntity.this,
                    name
                    );
                }
            };
        }

    @Override
    public WidgeonBase.Catalogs catalogs()
        {
        return new WidgeonBase.Catalogs()
            {
            @Override
            public WidgeonBase.Catalog create(final String name)
                {
                return womble().widgeons().catalogs().create(
                    WidgeonBaseEntity.this,
                    name
                    ) ;
                }

            @Override
            public Iterable<WidgeonBase.Catalog> select()
                {
                return womble().widgeons().catalogs().select(
                    WidgeonBaseEntity.this
                    );
                }

            @Override
            public WidgeonBase.Catalog select(final String name)
            throws NameNotFoundException
                {
                return womble().widgeons().catalogs().select(
                    WidgeonBaseEntity.this,
                    name
                    );
                }

            @Override
            public WidgeonBase.Catalog search(final String name)
                {
                return womble().widgeons().catalogs().search(
                    WidgeonBaseEntity.this,
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
    protected WidgeonBaseEntity()
        {
        super();
        }

    /**
     * Create a new Widgeon from VOSI metadata.
     *
     */
    private WidgeonBaseEntity(final String name, final URI source)
        {
        super(name);
        this.init(
            source
            );
        }

    /**
     * Create a new Widgeon from VOSI metadata.
     *
     */
    private WidgeonBaseEntity(final String name, final URL source)
        {
        super(name);
        this.init(
            source
            );
        }

    /**
     * Create a new Widgeon from JDBC metadata.
     *
     */
    private WidgeonBaseEntity(final String name, final DataSource source)
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

