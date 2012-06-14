/*
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

import uk.ac.roe.wfau.firethorn.common.ident.Identifier;

import uk.ac.roe.wfau.firethorn.common.entity.AbstractEntity;
import uk.ac.roe.wfau.firethorn.common.entity.AbstractFactory;

import uk.ac.roe.wfau.firethorn.common.entity.annotation.CreateEntityMethod;
import uk.ac.roe.wfau.firethorn.common.entity.annotation.SelectEntityMethod;

/**
 * Widgeon Catalog implementation.
 *
 */
@Slf4j
@Entity()
@Access(
    AccessType.FIELD
    )
@Table(
    name = CatalogEntity.DB_TABLE_NAME,
    uniqueConstraints=
        @UniqueConstraint(
            columnNames = {
                AbstractEntity.DB_NAME_COL,
                CatalogEntity.DB_PARENT_COL,
                }
            )
    )
@NamedQueries(
        {
        @NamedQuery(
            name  = "widgeon.catalog-select-all",
            query = "FROM CatalogEntity ORDER BY ident desc"
            ),
        @NamedQuery(
            name  = "widgeon.catalog-select-parent",
            query = "FROM CatalogEntity WHERE parent = :parent ORDER BY ident desc"
            ),
        @NamedQuery(
            name  = "widgeon.catalog-select-parent.name",
            query = "FROM CatalogEntity WHERE parent = :parent AND name = :name ORDER BY ident desc"
            )
        }
    )
public class CatalogEntity
extends AbstractEntity
implements Widgeon.Schema.Catalog
    {

    /**
     * Our database table name.
     * 
     */
    public static final String DB_TABLE_NAME = "widgeon_catalog" ;

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
    extends AbstractFactory<Widgeon.Schema.Catalog>
    implements Widgeon.Schema.Catalog.Factory
        {

        @Override
        public Class etype()
            {
            return CatalogEntity.class ;
            }

        @Override
        @SelectEntityMethod
        public Iterable<Widgeon.Schema.Catalog> select()
            {
            return super.iterable(
                super.query(
                    "widgeon.catalog-select-all"
                    )
                );
            }

        @Override
        @CreateEntityMethod
        public Widgeon.Schema.Catalog create(final Widgeon.Schema parent, final String name)
            {
            return super.insert(
                new CatalogEntity(
                    parent,
                    name
                    )
                );
            }

        @Override
        @SelectEntityMethod
        public Iterable<Widgeon.Schema.Catalog> select(final Widgeon.Schema parent)
            {
            return super.iterable(
                this.query(
                    "widgeon.catalog-select-parent"
                    ).setEntity(
                        "parent",
                        parent
                        )
                );
            }

        @Override
        @SelectEntityMethod
        public Widgeon.Schema.Catalog select(final Widgeon.Schema parent, final String name)
            {
            return super.single(
                super.query(
                    "widgeon.catalog-select-parent.name"
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
         * Our Autowired Table factory.
         * 
         */
        @Autowired
        protected Widgeon.Schema.Catalog.Table.Factory tables ;

        /**
         * Access to our Table factory.
         * 
         */
        @Override
        public Widgeon.Schema.Catalog.Table.Factory tables()
            {
            return this.tables ;
            }
        }

    @Override
    public Tables tables()
        {
        return new Tables()
            {

            @Override
            public Table create(String name)
                {
                return womble().widgeons().schemas().catalogs().tables().create(
                    CatalogEntity.this,
                    name
                    ) ;
                }

            @Override
            public Table select(String name)
                {
                return womble().widgeons().schemas().catalogs().tables().select(
                    CatalogEntity.this,
                    name
                    ) ;
                }

            @Override
            public Iterable<Table> select()
                {
                return womble().widgeons().schemas().catalogs().tables().select(
                    CatalogEntity.this
                    ) ;
                }
            };
        }

    /**
     * Default constructor needs to be protected not private.
     * http://kristian-domagala.blogspot.co.uk/2008/10/proxy-instantiation-problem-from.html
     *
     */
    protected CatalogEntity()
        {
        super();
        }

    /**
     * Create a new Catalog.
     *
     */
    protected CatalogEntity(final Widgeon.Schema parent, final String name)
        {
        super(name);
        this.parent = parent ;
        }

    /**
     * Our parent Schema.
     *
     */
    @ManyToOne(
        fetch = FetchType.LAZY,
        targetEntity = SchemaEntity.class
        )
    @JoinColumn(
        name = DB_PARENT_COL,
        unique = false,
        nullable = false,
        updatable = false
        )
    private Widgeon.Schema parent ;

    /**
     * Access to our parent Schema.
     *
     */
    @Override
    public Widgeon.Schema parent()
        {
        return this.parent ;
        }

    }

