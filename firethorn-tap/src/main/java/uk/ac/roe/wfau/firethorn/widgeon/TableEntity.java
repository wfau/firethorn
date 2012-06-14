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

import javax.persistence.Table;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Access;
import javax.persistence.AccessType;
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

import uk.ac.roe.wfau.firethorn.common.entity.annotation.CreateEntityMethod;
import uk.ac.roe.wfau.firethorn.common.entity.annotation.SelectEntityMethod;

/**
 * Widgeon Table implementation.
 *
 */
@Slf4j
@Entity()
@Access(
    AccessType.FIELD
    )
@Table(
    name = TableEntity.DB_TABLE_NAME,
    uniqueConstraints=
        @UniqueConstraint(
            columnNames = {
                AbstractEntity.DB_NAME_COL,
                TableEntity.DB_PARENT_COL,
                }
            )
    )
@NamedQueries(
        {
        @NamedQuery(
            name  = "widgeon.table-select-all",
            query = "FROM TableEntity ORDER BY ident desc"
            ),
        @NamedQuery(
            name  = "widgeon.table-select-parent",
            query = "FROM TableEntity WHERE parent = :parent ORDER BY ident desc"
            ),
        @NamedQuery(
            name  = "widgeon.table-select-parent.name",
            query = "FROM TableEntity WHERE parent = :parent AND name = :name ORDER BY ident desc"
            )
        }
    )
public class TableEntity
extends AbstractEntity
implements Widgeon.Schema.Catalog.Table
    {

    /**
     * Our database table name.
     * 
     */
    public static final String DB_TABLE_NAME = "widgeon_table" ;

    /**
     * Our parent column name.
     * 
     */
    public static final String DB_PARENT_COL = "parent" ;

    /**
     * Schema factory interface.
     *
     */
    @Repository
    public static class Factory
    extends AbstractFactory<Widgeon.Schema.Catalog.Table>
    implements Widgeon.Schema.Catalog.Table.Factory
        {

        @Override
        public Class etype()
            {
            return TableEntity.class ;
            }

        @Override
        @SelectEntityMethod
        public Iterable<Widgeon.Schema.Catalog.Table> select()
            {
            return super.iterable(
                super.query(
                    "widgeon.table-select-all"
                    )
                );
            }

        @Override
        @CreateEntityMethod
        public Widgeon.Schema.Catalog.Table create(final Widgeon.Schema.Catalog parent, final String name)
            {
            return super.insert(
                new TableEntity(
                    parent,
                    name
                    )
                );
            }

        @Override
        @SelectEntityMethod
        public Iterable<Widgeon.Schema.Catalog.Table> select(final Widgeon.Schema.Catalog parent)
            {
            return super.iterable(
                this.query(
                    "widgeon.table-select-parent"
                    ).setEntity(
                        "parent",
                        parent
                        )
                );
            }

        @Override
        @SelectEntityMethod
        public Widgeon.Schema.Catalog.Table select(final Widgeon.Schema.Catalog parent, final String name)
            {
            return super.single(
                super.query(
                    "widgeon.table-select-parent.name"
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
         * Our Autowired Column factory.
         * 
         */
        @Autowired
        protected Widgeon.Schema.Catalog.Table.Column.Factory columns ;

        /**
         * Access to our Column factory.
         * 
         */
        @Override
        public Widgeon.Schema.Catalog.Table.Column.Factory columns()
            {
            return this.columns ;
            }
        }

    @Override
    public Columns columns()
        {
        return new Columns()
            {

            @Override
            public Column create(String name)
                {
                return womble().widgeons().schemas().catalogs().tables().columns().create(
                    TableEntity.this,
                    name
                    ) ;
                }

            @Override
            public Column select(String name)
                {
                return womble().widgeons().schemas().catalogs().tables().columns().select(
                    TableEntity.this,
                    name
                    ) ;
                }

            @Override
            public Iterable<Column> select()
                {
                return womble().widgeons().schemas().catalogs().tables().columns().select(
                    TableEntity.this
                    ) ;
                }
            };
        }

    /**
     * Default constructor needs to be protected not private.
     * http://kristian-domagala.blogspot.co.uk/2008/10/proxy-instantiation-problem-from.html
     *
     */
    protected TableEntity()
        {
        super();
        }


    /**
     * Create a new Table.
     *
     */
    protected TableEntity(final Widgeon.Schema.Catalog parent, final String name)
        {
        super(name);
        this.parent = parent ;
        }

    /**
     * Our parent Catalog.
     *
     */
    @ManyToOne(
        fetch = FetchType.LAZY,
        targetEntity = CatalogEntity.class
        )
    @JoinColumn(
        name = DB_PARENT_COL,
        unique = false,
        nullable = false,
        updatable = false
        )
    private Widgeon.Schema.Catalog parent ;

    /**
     * Access to our parent Catalog.
     *
     */
    @Override
    public Widgeon.Schema.Catalog parent()
        {
        return this.parent ;
        }

    }

