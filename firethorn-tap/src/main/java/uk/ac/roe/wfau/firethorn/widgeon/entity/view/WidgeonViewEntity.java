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
package uk.ac.roe.wfau.firethorn.widgeon.entity.view ;

import lombok.extern.slf4j.Slf4j;

import java.net.URI;
import java.net.URL;

import java.util.List;
import java.util.ArrayList;

import javax.sql.DataSource;

import javax.persistence.Table;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
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
import uk.ac.roe.wfau.firethorn.widgeon.entity.base.WidgeonBaseEntity;

/**
 * Widgeon View implementations.
 *
 */
@Slf4j
@Entity()
@Access(
    AccessType.FIELD
    )
@Table(
    name = WidgeonViewEntity.DB_TABLE_NAME,
    uniqueConstraints={
        @UniqueConstraint(
            columnNames = {
                AbstractEntity.DB_NAME_COL,
                WidgeonViewEntity.DB_BASE_COL
                }
            )
        }
    )
@NamedQueries(
        {
        @NamedQuery(
            name  = "widgeon.view.entity-select-base",
            query = "FROM WidgeonViewEntity WHERE (base = :base) ORDER BY ident desc"
            ),
        @NamedQuery(
            name  = "widgeon.view.entity-select-base.name",
            query = "FROM WidgeonViewEntity WHERE ((base = :base) AND (name = :name)) ORDER BY ident desc"
            )
        }
    )
public class WidgeonViewEntity
extends WidgeonStatusEntity
implements WidgeonView
    {

    /**
     * Our persistence table name.
     * 
     */
    public static final String DB_TABLE_NAME = "widgeon_view_entity" ;

    /**
     * The persistence column name for our base Widgeon.
     * 
     */
    public static final String DB_BASE_COL = "base" ;

    /**
     * Our Entity Factory implementation.
     *
     */
    @Repository
    public static class Factory
    extends AbstractFactory<WidgeonView>
    implements WidgeonView.Factory
        {

        @Override
        public Class etype()
            {
            return WidgeonViewEntity.class ;
            }

        /**
         * Insert a View into the database and create views for each child.
         *
         */
        @CascadeEntityMethod
        protected WidgeonView insert(WidgeonViewEntity entity)
            {
            super.insert(
                entity
                );
            for (WidgeonBase.Catalog catalog : entity.base().catalogs().select())
                {
                this.catalogs().cascade(
                    entity,
                    catalog
                    );
                }
            return entity ;
            }

        @Override
        @CreateEntityMethod
        public WidgeonView create(final WidgeonBase base, final String name)
            {
            return this.insert(
                new WidgeonViewEntity(
                    base,
                    name
                    )
                );
            }

        @Override
        @SelectEntityMethod
        public Iterable<WidgeonView> select(final WidgeonBase base)
            {
            return super.iterable(
                super.query(
                    "widgeon.view.entity-select-base"
                    ).setEntity(
                        "base",
                        base
                    )
                );
            }

        @Override
        @SelectEntityMethod
        public WidgeonView select(final WidgeonBase base, String name)
        throws NameNotFoundException
            {
            WidgeonView result = this.search(
                base,
                name
                );
            if (result != null)
                {
                return result ;
                }
            else {
                throw new NameNotFoundException(
                    name
                    );
                }
            }

        /**
         * Search for a named View of a Widgeon.
         *
         */
        @SelectEntityMethod
        protected WidgeonView search(final WidgeonBase base, String name)
            {
            return super.first(
                super.query(
                    "widgeon.view.entity-select-base.name"
                    ).setEntity(
                        "base",
                        base
                    ).setString(
                        "name",
                        name
                    )
                );
            }

        /**
         * Our Autowired Catalog factory.
         * 
         */
        @Autowired
        protected WidgeonView.Catalog.Factory catalogs ;

        @Override
        public WidgeonView.Catalog.Factory catalogs()
            {
            return catalogs ;
            }
        }

    @Override
    public WidgeonView.Catalogs catalogs()
        {
        return new WidgeonView.Catalogs()
            {
            @Override
            public Iterable<WidgeonView.Catalog> select()
                {
                return womble().widgeons().views().catalogs().select(
                    WidgeonViewEntity.this
                    ) ;
                }

            @Override
            public WidgeonView.Catalog select(final String name)
            throws NameNotFoundException
                {
                return womble().widgeons().views().catalogs().select(
                    WidgeonViewEntity.this,
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
    protected WidgeonViewEntity()
        {
        super();
        }

    /**
     * Create a new view of a Widgeon.
     *
     */
    private WidgeonViewEntity(final WidgeonBase base)
        {
        this(
            base,
            null
            );
        }

    /**
     * Create a new view of a Widgeon.
     *
     */
    private WidgeonViewEntity(final WidgeonBase base, final String name)
        {
        super(
            name
            );
        this.base = base ;
        }

    /**
     * Our underlying Widgeon.
     *
     */
    @ManyToOne(
        fetch = FetchType.EAGER,
        targetEntity = WidgeonBaseEntity.class
        )
    @JoinColumn(
        name = DB_BASE_COL,
        unique = false,
        nullable = false,
        updatable = false
        )
    private WidgeonBase base ;

    @Override
    public WidgeonBase base()
        {
        return this.base ;
        }

    @Override
    public String name()
        {
        if (this.name != null)
            {
            return this.name ;
            }
        else {
            return base.name() ;
            }
        }

    @Override
    public Widgeon.Status status()
        {
        if (this.base().status() == Widgeon.Status.ENABLED)
            {
            return super.status();
            }
        else {
            return this.base().status();
            }
        }
    }

