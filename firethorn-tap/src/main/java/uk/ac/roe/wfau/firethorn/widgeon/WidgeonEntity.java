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
    extends AbstractFactory<Widgeon>
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
            return super.iterable(
                super.query(
                    "widgeon-select"
                    )
                );
            }

        @Override
        @SelectEntityMethod
        public Widgeon select(final Identifier ident)
            {
            return super.select(
                ident
                );
            }

        @Override
        @CreateEntityMethod
        public Widgeon create(final String name, final URI uri)
            {
            return super.insert(
                new WidgeonEntity(
                    name,
                    uri
                    )
                );
            }

        @Override
        @CreateEntityMethod
        public Widgeon create(final String name, final URL url)
            {
            return super.insert(
                new WidgeonEntity(
                    name,
                    url
                    )
                );
            }

        @Override
        @CreateEntityMethod
        public Widgeon create(final String name, final DataSource src)
            {
            return super.insert(
                new WidgeonEntity(
                    name,
                    src
                    )
                );
            }

        /**
         * Our Autowired Schema factory.
         * 
         */
        @Autowired
        protected Schema.Factory schemas ;

        /**
         * Access to our Schema factory.
         * 
         */
        @Override
        public Schema.Factory schemas()
            {
            return this.schemas ;
            }
        }

    @Override
    public Schemas schemas()
        {
        return new Schemas()
            {
            @Override
            public Schema create(final String name)
                {
                return womble().widgeons().schemas().create(
                    WidgeonEntity.this,
                    name
                    ) ;
                }

            @Override
            public Iterable<Schema> select()
                {
                return womble().widgeons().schemas().select(
                    WidgeonEntity.this
                    ) ;
                }

            @Override
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

