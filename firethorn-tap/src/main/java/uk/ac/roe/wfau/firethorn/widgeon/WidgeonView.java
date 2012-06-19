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
    name = WidgeonView.DB_TABLE_NAME
    )
@NamedQueries(
        {
        @NamedQuery(
            name  = "widgeon.view-select-base",
            query = "FROM WidgeonView WHERE base = :base ORDER BY ident desc"
            ),
        @NamedQuery(
            name  = "widgeon.view-select-base.name",
            query = "FROM WidgeonView WHERE base = :base AND name = :name"
            )
        }
    )
public class WidgeonView
extends AbstractEntity
implements Widgeon, Widgeon.View
    {

    /**
     * Our persistence table name.
     * 
     */
    public static final String DB_TABLE_NAME = "widgeon_view" ;

    /**
     * The persistence column name for our base Widgeon.
     * 
     */
    public static final String DB_BASE_COL = "base" ;

    /*
     * The persistence column name for our status enum.
     * 
     */
    public static final String DB_STATUS_COL  = "status" ;


    /**
     * Our Entity Factory implementation.
     *
     */
    @Repository
    public static class Factory
    extends AbstractFactory<Widgeon.View>
    implements Widgeon.View.Factory
        {

        @Override
        public Class etype()
            {
            return WidgeonView.class ;
            }

        @Override
        @SelectEntityMethod
        public Iterable<Widgeon.View> select(final Widgeon.Base base)
            {
            return super.iterable(
                super.query(
                    "widgeon.view-select-base"
                    ).setEntity(
                        "base",
                        base
                    )
                );
            }

        @Override
        @SelectEntityMethod
        public Widgeon.View select(final Widgeon.Base base, String name)
            {
            return super.single(
                super.query(
                    "widgeon.view-select-base.name"
                    ).setEntity(
                        "base",
                        base
                    ).setString(
                        "name",
                        name
                    )
                );
            }

        @Override
        @CreateEntityMethod
        public Widgeon.View create(final Widgeon.Base base, final String name)
            {
            return super.insert(
                new WidgeonView(
                    base,
                    name
                    )
                );
            }

        /**
         * Our Autowired Schema factory.
         * 
         */
        @Autowired
        protected Widgeon.View.Schema.Factory schemas ;

        /**
         * Access to our Schema factory.
         * 
         */
        @Override
        public Widgeon.View.Schema.Factory schemas()
            {
            return schemas ;
            }
        }

    @Override
    public Schemas schemas()
        {
        return new Schemas()
            {
/*
            @Override
            public Widgeon.View.Schema create(final String name)
                {
                return womble().widgeon().views().schemas().create(
                    WidgeonView.this,
                    name
                    ) ;
                }
 */

            @Override
            public Iterable<Widgeon.Schema> select()
                {
                return womble().widgeon().views().schemas().select(
                    WidgeonView.this
                    ) ;
                }

            @Override
            public Widgeon.View.Schema select(final String name)
                {
                return womble().widgeon().views().schemas().select(
                    WidgeonView.this,
                    name
                    ) ;
                }
            };
        }

    /**
     * Check a view name, using the base name if the given name is null or empty.
     *
     */
    private static String name(final Widgeon.Base base, final String name)
        {
        if ((name == null) || (name.trim().length() == 0))
            {
            return base.name();
            }
        else {
            return name.trim() ;
            }
        }

    /**
     * Default constructor needs to be protected not private.
     * http://kristian-domagala.blogspot.co.uk/2008/10/proxy-instantiation-problem-from.html
     *
     */
    protected WidgeonView()
        {
        super();
        }

    /**
     * Create a new view of a Widgeon.
     *
     */
    private WidgeonView(final Widgeon.Base base)
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
    private WidgeonView(final Widgeon.Base base, final String name)
        {
        super(
            name(
                base,
                name
                )
            );
        this.base = base ;
        }

    /**
     * Our underlying base Widgeon.
     *
     */
    @ManyToOne(
        fetch = FetchType.EAGER,
        targetEntity = WidgeonBase.class
        )
    @JoinColumn(
        name = DB_BASE_COL,
        unique = false,
        nullable = false,
        updatable = false
        )
    private Widgeon.Base base ;

    /**
     * Access to our base Widgeon.
     *
     */
    @Override
    public Widgeon.Base base()
        {
        return this.base ;
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
        if (this.base.status() == Widgeon.Status.ENABLED)
            {
            return this.status;
            }
        else {
            return this.base.status();
            }
        }

    @Override
    public void status(Widgeon.Status status)
        {
        this.status = status ;
        }

    }

