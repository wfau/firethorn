/*
 *
 */
package uk.ac.roe.wfau.firethorn.widgeon ;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.net.URL;

import javax.persistence.Table;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.Index;
import org.hibernate.annotations.NamedQuery;
import org.hibernate.annotations.NamedQueries;

import org.springframework.stereotype.Repository;
import org.springframework.beans.factory.annotation.Autowired;  

import uk.ac.roe.wfau.firethorn.common.ident.Identifier;

import uk.ac.roe.wfau.firethorn.common.entity.AbstractEntity;
import uk.ac.roe.wfau.firethorn.common.entity.AbstractFactory;

import uk.ac.roe.wfau.firethorn.common.entity.annotation.CreateEntityMethod;
import uk.ac.roe.wfau.firethorn.common.entity.annotation.SelectEntityMethod;

/**
 * Widgeon Table implementation.
 *
 */
@Entity
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
            query = "FROM TableEntity"
            ),
        @NamedQuery(
            name  = "widgeon.table-select-parent",
            query = "FROM TableEntity WHERE parent = :parent"
            ),
        @NamedQuery(
            name  = "widgeon.table-select-parent.name",
            query = "FROM TableEntity WHERE parent = :parent AND name = :name"
            )
        }
    )
public class TableEntity
extends AbstractEntity
implements Widgeon.Schema.Catalog.Table
    {

    /**
     * Our debug logger.
     * 
     */
    private static Logger logger = LoggerFactory.getLogger(
        TableEntity.class
        );

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

/*
        @Override
        @SelectEntityMethod
        public Widgeon.Schema.Catalog.Table select(final Identifier ident)
            {
            return super.select(
                ident
                );
            }
 */
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

