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
import uk.ac.roe.wfau.firethorn.common.entity.annotation.SelectIterableMethod;

/**
 * Widgeon Schema Catalog implementation.
 *
 */
@Entity
@Table(
    name = WidgeonSchemaCatalogEntity.DB_TABLE_NAME,
    uniqueConstraints=
        @UniqueConstraint(
            columnNames = {
                AbstractEntity.DB_NAME_COL,
                WidgeonSchemaCatalogEntity.DB_PARENT_COL,
                }
            )
    )
@NamedQueries(
        {
        @NamedQuery(
            name  = "widgeon.schema.catalog-select-all",
            query = "FROM WidgeonSchemaCatalogEntity"
            ),
        @NamedQuery(
            name  = "widgeon.schema.catalog-select-parent",
            query = "FROM WidgeonSchemaCatalogEntity WHERE parent = :parent"
            ),
        @NamedQuery(
            name  = "widgeon.schema.catalog-select-parent.name",
            query = "FROM WidgeonSchemaCatalogEntity WHERE parent = :parent AND name = :name"
            )
        }
    )
public class WidgeonSchemaCatalogEntity
extends AbstractEntity
implements Widgeon.Schema.Catalog
    {

    /**
     * Our debug logger.
     * 
     */
    private static Logger logger = LoggerFactory.getLogger(
        WidgeonSchemaCatalogEntity.class
        );

    /**
     * Our database table name.
     * 
     */
    public static final String DB_TABLE_NAME = "widgeon_schema_catalog" ;

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
            return WidgeonSchemaCatalogEntity.class ;
            }

        @Override
        @SelectIterableMethod
        public Iterable<Widgeon.Schema.Catalog> select()
            {
            return super.iterable(
                super.query(
                    "widgeon.schema.catalog-select-all"
                    )
                );
            }

        @Override
        @SelectEntityMethod
        public Widgeon.Schema.Catalog select(final Identifier ident)
            {
            return super.select(
                ident
                );
            }

        @Override
        @CreateEntityMethod
        public Widgeon.Schema.Catalog create(final Widgeon.Schema parent, final String name)
            {
            return super.insert(
                new WidgeonSchemaCatalogEntity(
                    parent,
                    name
                    )
                );
            }

        @Override
        @SelectIterableMethod
        public Iterable<Widgeon.Schema.Catalog> select(final Widgeon.Schema parent)
            {
            return super.iterable(
                this.query(
                    "widgeon.schema.catalog-select-parent"
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
                    "widgeon.schema.catalog-select-parent.name"
                    ).setEntity(
                        "parent",
                        parent
                    ).setString(
                        "name",
                        name
                    )
                );
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
                return null ;
                }
            @Override
            public Table select(String name)
                {
                return null ;
                }
            @Override
            public Iterable<Table> select()
                {
                return null ;
                }
            };
        }

    /**
     * Default constructor needs to be protected not private.
     * http://kristian-domagala.blogspot.co.uk/2008/10/proxy-instantiation-problem-from.html
     *
     */
    protected WidgeonSchemaCatalogEntity()
        {
        super();
        }


    /**
     * Create a new Catalog.
     *
     */
    protected WidgeonSchemaCatalogEntity(final Widgeon.Schema parent, final String name)
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
        targetEntity = WidgeonSchemaEntity.class
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

