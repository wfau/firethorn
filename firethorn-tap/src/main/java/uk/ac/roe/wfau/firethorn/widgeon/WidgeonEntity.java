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

import uk.ac.roe.wfau.firethorn.common.ident.Identifier;

import uk.ac.roe.wfau.firethorn.common.entity.NameSelector;
import uk.ac.roe.wfau.firethorn.common.entity.IdentSelector;
import uk.ac.roe.wfau.firethorn.common.entity.AbstractEntity;
import uk.ac.roe.wfau.firethorn.common.entity.AbstractFactory;

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
            name  = "widgeon-select-all",
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
     * Our database mapping values.
     * 
     */
    public static final String DB_TABLE_NAME = "widgeons" ;

    /**
     * Generic factory interface.
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

        public Iterable<Widgeon> select()
            {
            return iterable(
                query(
                    "widgeon-select-all"
                    )
                );
            }

        @Override
        public WidgeonEntity select(Identifier ident)
            {
            return select(
                ident
                );
            }

        @Override
        public WidgeonEntity create(String name, URI uri)
            {
            return insert(
                new WidgeonEntity(
                    name,
                    uri
                    )
                );
            }

        @Override
        public WidgeonEntity create(String name, URL url)
            {
            return insert(
                new WidgeonEntity(
                    name,
                    url
                    )
                );
            }

        @Override
        public WidgeonEntity create(String name, DataSource src)
            {
            return insert(
                new WidgeonEntity(
                    name,
                    src
                    )
                );
            }
        }

    public NameSelector<Schema> schemas()
        {
        return new NameSelector<Schema>()
            {
            public Iterable<Schema> select()
                {
                return null ;
                }
            public Schema select(Identifier ident)
                {
                return null ;
                }
            public Schema select(String name)
                {
                return null ;
                }
            };
        }

    /**
     * Private constructor.
     *
     */
    private WidgeonEntity()
        {
        super();
        logger.debug("WidgeonEntity()");
        }

    /**
     * Create a new entity from VOSI metadata.
     *
     */
    private WidgeonEntity(String name, URI source)
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
    private WidgeonEntity(String name, URL source)
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
    private WidgeonEntity(String name, DataSource source)
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
    private void init(DataSource source)
        {
        logger.debug("init(DataSource)");
        // Process the JDBC metadata.
        }

    /**
     * Initialise our data from the VOSI metadata.
     *
     */
    private void init(URI uri)
        {
        logger.debug("init(URI)");
        // Resolve the URI into a VOSI endpoint URL.
        }

    /**
     * Initialise our data from the VOSI metadata.
     *
     */
    private void init(URL url)
        {
        logger.debug("init(URL)");
        // Process the VOSI metadata.
        }
    }

