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
 * Widgeon Schema implementation.
 *
 */
@Entity
@Table(
    name = SchemaEntity.DB_TABLE_NAME,
    uniqueConstraints=
        @UniqueConstraint(
            columnNames = {
                AbstractEntity.DB_NAME_COL,
                SchemaEntity.DB_PARENT_COL,
                }
            )
    )
@NamedQueries(
        {
        @NamedQuery(
            name  = "widgeon.schema-select-all",
            query = "FROM SchemaEntity"
            ),
        @NamedQuery(
            name  = "widgeon.schema-select-parent",
            query = "FROM SchemaEntity WHERE parent = :parent"
            ),
        @NamedQuery(
            name  = "widgeon.schema-select-parent.name",
            query = "FROM SchemaEntity WHERE parent = :parent AND name = :name"
            )
        }
    )
public class SchemaEntity
extends AbstractEntity
implements Widgeon.Schema
    {

    /**
     * Our debug logger.
     * 
     */
    private static Logger logger = LoggerFactory.getLogger(
        SchemaEntity.class
        );

    /**
     * Our database table name.
     * 
     */
    public static final String DB_TABLE_NAME = "widgeon_schema" ;

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
    extends AbstractFactory<Widgeon.Schema>
    implements Widgeon.Schema.Factory
        {

        @Override
        public Class etype()
            {
            return SchemaEntity.class ;
            }

        @SelectIterableMethod
        public Iterable<Widgeon.Schema> select()
            {
            return super.iterable(
                super.query(
                    "widgeon.schema-select-all"
                    )
                );
            }

        @Override
        @SelectEntityMethod
        public Widgeon.Schema select(final Identifier ident)
            {
            return super.select(
                ident
                );
            }

        @Override
        @CreateEntityMethod
        public Widgeon.Schema create(final Widgeon parent, final String name)
            {
            return super.insert(
                new SchemaEntity(
                    parent,
                    name
                    )
                );
            }

        @Override
        @SelectIterableMethod
        public Iterable<Widgeon.Schema> select(final Widgeon parent)
            {
            return super.iterable(
                super.query(
                    "widgeon.schema-select-parent"
                    ).setEntity(
                        "parent",
                        parent
                        )
                );
            }

        @Override
        @SelectEntityMethod
        public Widgeon.Schema select(final Widgeon parent, final String name)
            {
            return super.single(
                super.query(
                    "widgeon.schema-select-parent.name"
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
         * Our Autowired Catalog factory.
         * 
         */
        @Autowired
        protected Widgeon.Schema.Catalog.Factory catalogs ;

        /**
         * Access to our Catalog factory.
         * 
         */
        @Override
        public Widgeon.Schema.Catalog.Factory catalogs()
            {
            return this.catalogs ;
            }
        }

    @Override
    public Catalogs catalogs()
        {
        return new Catalogs()
            {
            @Override
            public Catalog create(String name)
                {
                return womble().widgeons().schemas().catalogs().create(
                    SchemaEntity.this,
                    name
                    ) ;
                }

            @Override
            public Catalog select(String name)
                {
                return womble().widgeons().schemas().catalogs().select(
                    SchemaEntity.this,
                    name
                    ) ;
                }

            @Override
            public Iterable<Catalog> select()
                {
                return womble().widgeons().schemas().catalogs().select(
                    SchemaEntity.this
                    ) ;
                }
            };
        }

    /**
     * Default constructor needs to be protected not private.
     * http://kristian-domagala.blogspot.co.uk/2008/10/proxy-instantiation-problem-from.html
     *
     */
    protected SchemaEntity()
        {
        super();
        }

    /**
     * Create a new Schema.
     *
     */
    protected SchemaEntity(final Widgeon parent, final String name)
        {
        super(name);
        this.parent = parent ;
        }

    /**
     * Our parent Widgeon.
     *
     */
    @ManyToOne(
        fetch = FetchType.LAZY,
        targetEntity = WidgeonEntity.class
        )
    @JoinColumn(
        name = DB_PARENT_COL,
        unique = false,
        nullable = false,
        updatable = false
        )
    private Widgeon parent ;

    /**
     * Access to our parent Widgeon.
     *
     */
    @Override
    public Widgeon parent()
        {
        return this.parent ;
        }

    }

