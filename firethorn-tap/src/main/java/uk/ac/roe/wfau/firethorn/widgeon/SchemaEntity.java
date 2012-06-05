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

import uk.ac.roe.wfau.firethorn.common.ident.Identifier;

import uk.ac.roe.wfau.firethorn.common.entity.NameSelector;
import uk.ac.roe.wfau.firethorn.common.entity.IdentSelector;
import uk.ac.roe.wfau.firethorn.common.entity.AbstractEntity;
import uk.ac.roe.wfau.firethorn.common.entity.AbstractFactory;

import uk.ac.roe.wfau.firethorn.common.entity.annotation.CreateEntityMethod;
import uk.ac.roe.wfau.firethorn.common.entity.annotation.SelectEntityMethod;
import uk.ac.roe.wfau.firethorn.common.entity.annotation.SelectIterableMethod;

/**
 * Core Widgeon implementations.
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
    extends AbstractFactory<Widgeon.Schema, SchemaEntity>
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
            return this.iterable(
                this.query(
                    "widgeon.schema-select-all"
                    )
                );
            }

        @Override
        @SelectEntityMethod
        public SchemaEntity select(final Identifier ident)
            {
            return this.select(
                ident
                );
            }

        @Override
        @CreateEntityMethod
        public SchemaEntity create(final Widgeon parent, final String name)
            {
            return this.insert(
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
            return this.iterable(
                this.query(
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
            return this.single(
                this.query(
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
        }

    public Catalogs catalogs()
        {
        return new Catalogs()
            {
            public Catalog create(String name)
                {
                return null ;
                }
            public Catalog select(String name)
                {
                return null ;
                }
            public Iterable<Catalog> select()
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
     * Our parent Entity.
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
     * Access to our parent Entity.
     *
     */
    @Override
    public Widgeon parent()
        {
        return this.parent ;
        }

    }

