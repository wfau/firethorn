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

import javax.persistence.Table;
import javax.persistence.Entity;
import javax.persistence.Column;
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

import uk.ac.roe.wfau.firethorn.common.entity.Identifier;
import uk.ac.roe.wfau.firethorn.common.entity.AbstractEntity;
import uk.ac.roe.wfau.firethorn.common.entity.AbstractFactory;

import uk.ac.roe.wfau.firethorn.common.entity.exception.*;
import uk.ac.roe.wfau.firethorn.common.entity.annotation.*;

import uk.ac.roe.wfau.firethorn.widgeon.Widgeon;
import uk.ac.roe.wfau.firethorn.widgeon.WidgeonStatus;
import uk.ac.roe.wfau.firethorn.widgeon.entity.WidgeonStatusEntity;
import uk.ac.roe.wfau.firethorn.widgeon.entity.base.SparrowBaseEntity;

/**
 * Sparrow View implementation.
 *
 */
@Slf4j
@Entity()
@Access(
    AccessType.FIELD
    )
@Table(
    name = SparrowViewEntity.DB_TABLE_NAME,
    uniqueConstraints={
        @UniqueConstraint(
            columnNames = {
                AbstractEntity.DB_NAME_COL,
                SparrowViewEntity.DB_PARENT_COL
                }
            ),
        @UniqueConstraint(
            columnNames = {
                SparrowViewEntity.DB_BASE_COL,
                SparrowViewEntity.DB_PARENT_COL
                }
            )
        }
    )
@NamedQueries(
        {
        @NamedQuery(
            name  = "widgeon.view.sparrow-select-parent",
            query = "FROM SparrowViewEntity WHERE (parent = :parent) ORDER BY ident desc"
            ),
        @NamedQuery(
            name  = "widgeon.view.sparrow-select-parent.name",
            query = "FROM SparrowViewEntity WHERE ((parent = :parent) AND (((name IS NOT null) AND (name = :name)) OR ((name IS null) AND (base.name = :name)))) ORDER BY ident desc"
            ),
        @NamedQuery(
            name  = "widgeon.view.sparrow-select-base",
            query = "FROM SparrowViewEntity WHERE (base = :base) ORDER BY ident desc"
            ),
        @NamedQuery(
            name  = "widgeon.view.sparrow-select-parent.base",
            query = "FROM SparrowViewEntity WHERE ((parent = :parent) AND (base = :base)) ORDER BY ident desc"
            ),
        }
    )
public class SparrowViewEntity
extends WidgeonStatusEntity
implements Widgeon.View.Sparrow
    {

    /**
     * Our persistence table name.
     * 
     */
    public static final String DB_TABLE_NAME = "widgeon_view_sparrow" ;

    /**
     * The persistence column name for our parent Widgeon.
     * 
     */
    public static final String DB_PARENT_COL = "parent" ;

    /**
     * The persistence column name for our base Sparrow.
     * 
     */
    public static final String DB_BASE_COL = "base" ;

    /**
     * Our Entity Factory implementation.
     *
     */
    @Repository
    public static class Factory
    extends AbstractFactory<Widgeon.View.Sparrow>
    implements Widgeon.View.Sparrow.Factory
        {

        @Override
        public Class etype()
            {
            return SparrowViewEntity.class ;
            }

        /**
         * Insert a View into the database and create views for each child.
         *
         */
        @CascadeEntityMethod
        protected Widgeon.View.Sparrow insert(SparrowViewEntity entity)
            {
            super.insert(
                entity
                );
            for (Widgeon.Base.Sparrow.Warbler warbler : entity.base().warblers().select())
                {
                this.warblers().cascade(
                    entity,
                    warbler
                    );
                }
            return entity ;
            }

        /**
         * Create a default View of a Sparrow.
         *
         */
        @CascadeEntityMethod
        protected Widgeon.View.Sparrow create(final Widgeon.View parent, final Widgeon.Base.Sparrow base)
            {
            return this.insert(
                new SparrowViewEntity(
                    parent,
                    base
                    )
                );
            }

        /**
         * Search for an existing View of a Sparrow.
         *
         */
        @SelectEntityMethod
        protected Widgeon.View.Sparrow search(final Widgeon.View parent, final Widgeon.Base.Sparrow base)
            {
            return super.first(
                super.query(
                    "widgeon.view.sparrow-select-parent.base"
                    ).setEntity(
                        "parent",
                        parent
                    ).setEntity(
                        "base",
                        base
                    )
                );
            }

        @Override
        @CascadeEntityMethod
        public Widgeon.View.Sparrow cascade(final Widgeon.View parent, final Widgeon.Base.Sparrow base)
            {
            Widgeon.View.Sparrow result = this.search(
                parent,
                base
                );
            if (result == null)
                {
                result = this.create(
                    parent,
                    base
                    );
                }
            return result ;
            }

        @Override
        @CreateEntityMethod
        public Widgeon.View.Sparrow create(final Widgeon.View parent, final Widgeon.Base.Sparrow base, final String name)
            {
            return this.insert(
                new SparrowViewEntity(
                    parent,
                    base,
                    name
                    )
                );
            }

        @Override
        @SelectEntityMethod
        public Iterable<Widgeon.View.Sparrow> select(final Widgeon.View parent)
            {
            return super.iterable(
                super.query(
                    "widgeon.view.sparrow-select-parent"
                    ).setEntity(
                        "parent",
                        parent
                        )
                );
            }

        @Override
        @SelectEntityMethod
        public Widgeon.View.Sparrow select(final Widgeon.View parent, final String name)
        throws NameNotFoundException
            {
            Widgeon.View.Sparrow result = this.search(
                parent,
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

        @Override
        @SelectEntityMethod
        public Widgeon.View.Sparrow search(final Widgeon.View parent, final String name)
            {
            return super.first(
                super.query(
                    "widgeon.view.sparrow-select-parent.name"
                    ).setEntity(
                        "parent",
                        parent
                    ).setString(
                        "name",
                        name
                    )
                );
            }

        @Override
        @SelectEntityMethod
        public Iterable<Widgeon.View.Sparrow> select(final Widgeon.Base.Sparrow base)
            {
            return super.iterable(
                super.query(
                    "widgeon.view.sparrow-select-base"
                    ).setEntity(
                        "base",
                        base
                        )
                );
            }

        /**
         * Our Autowired Warbler factory.
         * 
         */
        @Autowired
        protected Widgeon.View.Sparrow.Warbler.Factory warblers ;

        @Override
        public Widgeon.View.Sparrow.Warbler.Factory warblers()
            {
            return this.warblers ;
            }
        }

    @Override
    public Widgeon.View.Sparrow.Warblers warblers()
        {
        return new Widgeon.View.Sparrow.Warblers()
            {

            @Override
            public Iterable<Widgeon.View.Sparrow.Warbler> select()
                {
                return womble().widgeons().views().sparrows().warblers().select(
                    SparrowViewEntity.this
                    ) ;
                }

            @Override
            public Widgeon.View.Sparrow.Warbler select(String name)
            throws NameNotFoundException
                {
                return womble().widgeons().views().sparrows().warblers().select(
                    SparrowViewEntity.this,
                    name
                    ) ;
                }

            @Override
            public Widgeon.View.Sparrow.Warbler search(String name)
                {
                return womble().widgeons().views().sparrows().warblers().search(
                    SparrowViewEntity.this,
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
    protected SparrowViewEntity()
        {
        super();
        }

    /**
     * Create a new view.
     *
     */
    protected SparrowViewEntity(final Widgeon.View parent, final Widgeon.Base.Sparrow base)
        {
        this(
            parent,
            base,
            null
            );
        }

    /**
     * Create a new view.
     *
     */
    protected SparrowViewEntity(final Widgeon.View parent, final Widgeon.Base.Sparrow base, final String name)
        {
        super(
            name
            );
        this.base   = base   ;
        this.parent = parent ;
        }

    /**
     * Our parent Widgeon.
     *
     */
    @ManyToOne(
        fetch = FetchType.EAGER,
        targetEntity = WidgeonViewEntity.class
        )
    @JoinColumn(
        name = DB_PARENT_COL,
        unique = false,
        nullable = false,
        updatable = false
        )
    private Widgeon.View parent ;

    @Override
    public Widgeon.View parent()
        {
        return this.parent ;
        }

    /**
     * Our underlying Sparrow.
     *
     */
    @ManyToOne(
        fetch = FetchType.EAGER,
        targetEntity = SparrowBaseEntity.class
        )
    @JoinColumn(
        name = DB_BASE_COL,
        unique = false,
        nullable = false,
        updatable = false
        )
    private Widgeon.Base.Sparrow base ;

    @Override
    public Widgeon.Base.Sparrow base()
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
        if (this.parent().status() == Widgeon.Status.ENABLED)
            {
            if (this.base().status() == Widgeon.Status.ENABLED)
                {
                return super.status() ;
                }
            else {
                return this.base().status();
                }
            }
        else {
            return this.parent().status();
            }
        }
    }

