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

import uk.ac.roe.wfau.firethorn.common.entity.Identifier;
import uk.ac.roe.wfau.firethorn.common.entity.AbstractEntity;
import uk.ac.roe.wfau.firethorn.common.entity.AbstractFactory;

import uk.ac.roe.wfau.firethorn.common.entity.exception.*;
import uk.ac.roe.wfau.firethorn.common.entity.annotation.*;

import uk.ac.roe.wfau.firethorn.widgeon.Widgeon;
import uk.ac.roe.wfau.firethorn.widgeon.WidgeonStatus;
import uk.ac.roe.wfau.firethorn.widgeon.entity.WidgeonStatusEntity;

/**
 * Warbler implementation.
 *
 */
@Slf4j
@Entity()
@Access(
    AccessType.FIELD
    )
@Table(
    name = WarblerBaseEntity.DB_TABLE_NAME,
    uniqueConstraints=
        @UniqueConstraint(
            columnNames = {
                AbstractEntity.DB_NAME_COL,
                WarblerBaseEntity.DB_PARENT_COL,
                }
            )
    )
@NamedQueries(
        {
        @NamedQuery(
            name  = "widgeon.base.warbler-select-parent",
            query = "FROM WarblerBaseEntity WHERE parent = :parent ORDER BY ident desc"
            ),
        @NamedQuery(
            name  = "widgeon.base.warbler-select-parent.name",
            query = "FROM WarblerBaseEntity WHERE parent = :parent AND name = :name ORDER BY ident desc"
            )
        }
    )
public class WarblerBaseEntity
extends WidgeonStatusEntity
implements Widgeon.Base.Sparrow.Warbler
    {

    /**
     * Our persistence table name.
     * 
     */
    public static final String DB_TABLE_NAME = "widgeon_base_warbler" ;

    /**
     * The persistence column name for our parent Sparrow.
     * 
     */
    public static final String DB_PARENT_COL = "parent" ;

    /**
     * Our Entity Factory implementation.
     *
     */
    @Repository
    public static class Factory
    extends AbstractFactory<Widgeon.Base.Sparrow.Warbler>
    implements Widgeon.Base.Sparrow.Warbler.Factory
        {

        @Override
        public Class etype()
            {
            return WarblerBaseEntity.class ;
            }

        /**
         * Insert a Warbler into the database and update all the parent views.
         *
         */
        @CascadeEntityMethod
        protected Widgeon.Base.Sparrow.Warbler insert(final WarblerBaseEntity entity)
            {
            super.insert(
                entity
                );
            for (Widgeon.View.Sparrow view : entity.parent().views().select())
                {
                this.views().cascade(
                    view,
                    entity
                    );
                }
            return entity ;
            }

        @Override
        @CreateEntityMethod
        public Widgeon.Base.Sparrow.Warbler create(final Widgeon.Base.Sparrow parent, final String name)
            {
            return this.insert(
                new WarblerBaseEntity(
                    parent,
                    name
                    )
                );
            }

        @Override
        @SelectEntityMethod
        public Iterable<Widgeon.Base.Sparrow.Warbler> select(final Widgeon.Base.Sparrow parent)
            {
            return super.iterable(
                super.query(
                    "widgeon.base.warbler-select-parent"
                    ).setEntity(
                        "parent",
                        parent
                        )
                );
            }

        @Override
        @SelectEntityMethod
        public Widgeon.Base.Sparrow.Warbler select(final Widgeon.Base.Sparrow parent, final String name)
        throws NameNotFoundException
            {
            Widgeon.Base.Sparrow.Warbler result = this.search(
                parent,
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

        @Override
        @SelectEntityMethod
        public Widgeon.Base.Sparrow.Warbler search(final Widgeon.Base.Sparrow parent, final String name)
            {
            return super.first(
                super.query(
                    "widgeon.base.warbler-select-parent.name"
                    ).setEntity(
                        "parent",
                        parent
                    ).setString(
                        "name",
                        name
                    )
                );
            }

        /**
         * Our Autowired View factory.
         * 
         */
        @Autowired
        protected Widgeon.View.Sparrow.Warbler.Factory views ;

        @Override
        public Widgeon.View.Sparrow.Warbler.Factory views()
            {
            return this.views ;
            }

        /**
         * Our Autowired Table factory.
         * 
         */
        @Autowired
        protected Widgeon.Base.Sparrow.Warbler.Table.Factory tables ;

        @Override
        public Widgeon.Base.Sparrow.Warbler.Table.Factory tables()
            {
            return this.tables ;
            }
        }

    @Override
    public Widgeon.Base.Sparrow.Warbler.Views views()
        {
        return new Widgeon.Base.Sparrow.Warbler.Views()
            {
            public Iterable<Widgeon.View.Sparrow.Warbler> select()
                {
                return womble().widgeons().views().sparrows().warblers().select(
                    WarblerBaseEntity.this
                    );
                }
            };
        }

    @Override
    public Widgeon.Base.Sparrow.Warbler.Tables tables()
        {
        return new Widgeon.Base.Sparrow.Warbler.Tables()
            {
            @Override
            public Widgeon.Base.Sparrow.Warbler.Table create(String name)
                {
                return womble().widgeons().sparrows().warblers().tables().create(
                    WarblerBaseEntity.this,
                    name
                    );
                }

            @Override
            public Iterable<Widgeon.Base.Sparrow.Warbler.Table> select()
                {
                return womble().widgeons().sparrows().warblers().tables().select(
                    WarblerBaseEntity.this
                    ) ;
                }

            @Override
            public Widgeon.Base.Sparrow.Warbler.Table select(String name)
            throws NameNotFoundException
                {
                return womble().widgeons().sparrows().warblers().tables().select(
                    WarblerBaseEntity.this,
                    name
                    ) ;
                }

            @Override
            public Widgeon.Base.Sparrow.Warbler.Table search(String name)
                {
                return womble().widgeons().sparrows().warblers().tables().search(
                    WarblerBaseEntity.this,
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
    protected WarblerBaseEntity()
        {
        super();
        }

    /**
     * Create a new Sparrow.
     *
     */
    protected WarblerBaseEntity(final Widgeon.Base.Sparrow parent, final String name)
        {
        super(name);
        this.parent = parent ;
        }

    /**
     * Our parent Sparrow.
     *
     */
    @ManyToOne(
        fetch = FetchType.EAGER,
        targetEntity = SparrowBaseEntity.class
        )
    @JoinColumn(
        name = DB_PARENT_COL,
        unique = false,
        nullable = false,
        updatable = false
        )
    private Widgeon.Base.Sparrow parent ;

    @Override
    public Widgeon.Base.Sparrow parent()
        {
        return this.parent ;
        }

    @Override
    public Widgeon.Status status()
        {
        if (this.parent().status() == Widgeon.Status.ENABLED)
            {
            return super.status();
            }
        else {
            return this.parent().status();
            }
        }
    }

