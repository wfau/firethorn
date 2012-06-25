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
package uk.ac.roe.wfau.firethorn.widgeon ;

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

import uk.ac.roe.wfau.firethorn.common.entity.annotation.CreateEntityMethod;
import uk.ac.roe.wfau.firethorn.common.entity.annotation.SelectEntityMethod;

/**
 * Core Widgeon implementations.
 *
 */
@Slf4j
@Entity()
@Access(
    AccessType.FIELD
    )
@Table(
    name = WidgeonBase.DB_TABLE_NAME
    )
@NamedQueries(
        {
        @NamedQuery(
            name  = "widgeon.base-select-all",
            query = "FROM WidgeonBase ORDER BY ident desc"
            ),
        @NamedQuery(
            name  = "widgeon.base-select-name",
            query = "FROM WidgeonBase WHERE name = :name"
            )
        }
    )
public class WidgeonBase
extends AbstractEntity
implements Widgeon, Widgeon.Base
    {

    /**
     * Our persistence table name.
     * 
     */
    public static final String DB_TABLE_NAME = "widgeon_base" ;

    /*
     * The persistence column name for our status enum.
     * 
     */
    public static final String DB_STATUS_COL = "status" ;

    /**
     * Our Entity Factory implementation.
     *
     */
    @Repository
    public static class Factory
    extends AbstractFactory<Widgeon.Base>
    implements Widgeon.Base.Factory
        {

        @Override
        public Class etype()
            {
            return WidgeonBase.class ;
            }

        @Override
        @SelectEntityMethod
        public Iterable<Widgeon.Base> select()
            {
            return super.iterable(
                super.query(
                    "widgeon.base-select-all"
                    )
                );
            }

        @Override
        @CreateEntityMethod
        public Widgeon.Base create(final String name, final URI uri)
            {
            return super.insert(
                new WidgeonBase(
                    name,
                    uri
                    )
                );
            }

        @Override
        @CreateEntityMethod
        public Widgeon.Base create(final String name, final URL url)
            {
            return super.insert(
                new WidgeonBase(
                    name,
                    url
                    )
                );
            }

        @Override
        @CreateEntityMethod
        public Widgeon.Base create(final String name, final DataSource src)
            {
            return super.insert(
                new WidgeonBase(
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
        protected Widgeon.View.Factory views ;

        /**
         * Access to our View factory.
         * 
         */
        public Widgeon.View.Factory views()
            {
            return this.views ;
            }

        /**
         * Our Autowired Schema factory.
         * 
         */
        @Autowired
        protected Widgeon.Base.Schema.Factory schemas ;

        /**
         * Access to our Schema factory.
         * 
         */
        @Override
        public Widgeon.Base.Schema.Factory schemas()
            {
            return this.schemas ;
            }
        }

    @Override
    public Widgeon.Base.Views views() 
        {
        return new Views()
            {
            @Override
            public Widgeon.View create(String name)
                {
                return womble().widgeon().views().create(
                    WidgeonBase.this,
                    name
                    ) ;
                }

            @Override
            public Iterable<Widgeon.View> select()
                {
                return womble().widgeon().views().select(
                    WidgeonBase.this
                    ) ;
                }

            @Override
            public Widgeon.View select(String name)
                {
                return womble().widgeon().views().select(
                    WidgeonBase.this,
                    name
                    ) ;
                }
            };
        }

    @Override
    public Widgeon.Base.Schemas schemas()
        {
        return new Widgeon.Base.Schemas()
            {
            @Override
            public Widgeon.Base.Schema create(final String name)
                {
                return womble().widgeon().schemas().create(
                    WidgeonBase.this,
                    name
                    ) ;
                }

            @Override
            public Iterable<Widgeon.Base.Schema> select()
                {
                return womble().widgeon().schemas().select(
                    WidgeonBase.this
                    ) ;
                }

            @Override
            public Widgeon.Base.Schema select(final String name)
                {
                return womble().widgeon().schemas().select(
                    WidgeonBase.this,
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
    protected WidgeonBase()
        {
        super();
        }

    /**
     * Create a new Widgeon from VOSI metadata.
     *
     */
    private WidgeonBase(final String name, final URI source)
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
    private WidgeonBase(final String name, final URL source)
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
    private WidgeonBase(final String name, final DataSource source)
        {
        super(name);
        this.init(
            source
            );
        }

    /**
     * The status of this Widgeon.
     *
     */
    @Column(
        name = DB_STATUS_COL,
        unique = false,
        nullable = false,
        updatable = true
        )
    @Enumerated(
        EnumType.STRING
        )
    private Widgeon.Status status = Widgeon.Status.CREATED ;

    @Override
    public Widgeon.Status status()
        {
        return this.status;
        }

    @Override
    public void status(Widgeon.Status status)
        {
        this.status = status ;
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

