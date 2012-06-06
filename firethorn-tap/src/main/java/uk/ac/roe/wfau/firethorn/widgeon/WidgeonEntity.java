/*
 *
 */
package uk.ac.roe.wfau.firethorn.widgeon ;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.net.URL;

import javax.sql.DataSource;

import javax.persistence.Table;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.Index;
import org.hibernate.annotations.NamedQuery;
import org.hibernate.annotations.NamedQueries;

import org.springframework.stereotype.Repository;
import org.springframework.beans.factory.annotation.Autowired;  

import uk.ac.roe.wfau.firethorn.common.ident.Identifier;

import uk.ac.roe.wfau.firethorn.common.womble.Womble;

import uk.ac.roe.wfau.firethorn.common.entity.AbstractEntity;
import uk.ac.roe.wfau.firethorn.common.entity.AbstractFactory;

import uk.ac.roe.wfau.firethorn.common.entity.annotation.CreateEntityMethod;
import uk.ac.roe.wfau.firethorn.common.entity.annotation.SelectEntityMethod;
import uk.ac.roe.wfau.firethorn.common.entity.annotation.SelectIterableMethod;

/**
 * Core Widgeon implementations.
 *
    uniqueConstraints=
        @UniqueConstraint(
            columnNames = {
                AbstractEntity.DB_NAME_COL
                }
            )
 */
@Entity
@Table(
    name = WidgeonEntity.DB_TABLE_NAME
    )
@NamedQueries(
        {
        @NamedQuery(
            name  = "widgeon-select",
            query = "FROM WidgeonEntity"
            ),
        @NamedQuery(
            name  = "widgeon-select-name",
            query = "FROM WidgeonEntity WHERE name = :name"
            )
        }
    )
public class WidgeonEntity
extends AbstractEntity
implements Widgeon
    {

    /**
     * Our debug logger.
     * 
     */
    private static Logger logger = LoggerFactory.getLogger(
        WidgeonEntity.class
        );

    /**
     * Our database table name.
     * 
     */
    public static final String DB_TABLE_NAME = "widgeons" ;

    /**
     * Widgeon factory.
     *
     */
    @Repository
    public static class Factory
    extends AbstractFactory<Widgeon, WidgeonEntity>
    implements Widgeon.Factory
        {

        @Override
        public Class etype()
            {
            return WidgeonEntity.class ;
            }

        @SelectIterableMethod
        public Iterable<Widgeon> select()
            {
            return this.iterable(
                this.query(
                    "widgeon-select"
                    )
                );
            }

        @Override
        @SelectEntityMethod
        public WidgeonEntity select(final Identifier ident)
            {
            return this.select(
                ident
                );
            }

        @Override
        @CreateEntityMethod
        public WidgeonEntity create(final String name, final URI uri)
            {
            return this.insert(
                new WidgeonEntity(
                    name,
                    uri
                    )
                );
            }

        @Override
        @CreateEntityMethod
        public WidgeonEntity create(final String name, final URL url)
            {
            return this.insert(
                new WidgeonEntity(
                    name,
                    url
                    )
                );
            }

        @Override
        @CreateEntityMethod
        public WidgeonEntity create(final String name, final DataSource src)
            {
            return this.insert(
                new WidgeonEntity(
                    name,
                    src
                    )
                );
            }

        /**
         * Our Schema factory.
         * 
         */
        @Autowired
        protected SchemaEntity.Factory schemas ;

        /**
         * Access to our Schema factory.
         * 
         */
        @Override
        public SchemaEntity.Factory schemas()
            {
            return this.schemas ;
            }

        }

    public Widgeon.Schemas schemas()
        {
        return new Widgeon.Schemas()
            {
            public Schema create(final String name)
                {
                return womble().widgeons().schemas().create(
                    WidgeonEntity.this,
                    name
                    ) ;
                }

            public Iterable<Schema> select()
                {
                return womble().widgeons().schemas().select(
                    WidgeonEntity.this
                    ) ;
                }

            public Schema select(final Identifier ident)
                {
                return womble().widgeons().schemas().select(
                    ident
                    ) ;
                }

            public Schema select(final String name)
                {
                return womble().widgeons().schemas().select(
                    WidgeonEntity.this,
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
    protected WidgeonEntity()
        {
        super();
        }

    /**
     * Create a new entity from VOSI metadata.
     *
     */
    private WidgeonEntity(final String name, final URI source)
        {
        super(name);
        this.init(
            source
            );
        }

    /**
     * Create a new entity from VOSI metadata.
     *
     */
    private WidgeonEntity(final String name, final URL source)
        {
        super(name);
        this.init(
            source
            );
        }

    /**
     * Create a new entity from JDBC metadata.
     *
     */
    private WidgeonEntity(final String name, final DataSource source)
        {
        super(name);
        this.init(
            source
            );
        }

    /**
     * Initialise our data from the JDBC metadata.
     *
     */
    private void init(final DataSource source)
        {
        logger.debug("init(DataSource)");
        // Process the JDBC metadata.
        }

    /**
     * Initialise our data from the VOSI metadata.
     *
     */
    private void init(final URI uri)
        {
        logger.debug("init(URI)");
        // Resolve the URI into a VOSI endpoint URL.
        }

    /**
     * Initialise our data from the VOSI metadata.
     *
     */
    private void init(final URL url)
        {
        logger.debug("init(URL)");
        // Process the VOSI metadata.
        }
    }

