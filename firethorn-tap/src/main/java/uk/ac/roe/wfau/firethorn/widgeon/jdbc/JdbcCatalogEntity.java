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
package uk.ac.roe.wfau.firethorn.widgeon.jdbc ;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import lombok.extern.slf4j.Slf4j;

import org.hibernate.annotations.NamedQueries;
import org.hibernate.annotations.NamedQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import uk.ac.roe.wfau.firethorn.common.entity.AbstractEntity;
import uk.ac.roe.wfau.firethorn.common.entity.AbstractFactory;
import uk.ac.roe.wfau.firethorn.common.entity.annotation.CascadeEntityMethod;
import uk.ac.roe.wfau.firethorn.common.entity.annotation.CreateEntityMethod;
import uk.ac.roe.wfau.firethorn.common.entity.annotation.SelectEntityMethod;
import uk.ac.roe.wfau.firethorn.common.entity.exception.NameNotFoundException;
import uk.ac.roe.wfau.firethorn.widgeon.DataResource;
import uk.ac.roe.wfau.firethorn.widgeon.ResourceStatusEntity;
import uk.ac.roe.wfau.firethorn.widgeon.adql.AdqlResource;

/**
 * BaseResource.BaseCatalog implementation.
 *
 */
@Slf4j
@Entity()
@Access(
    AccessType.FIELD
    )
@Table(
    name = JdbcCatalogEntity.DB_TABLE_NAME,
    uniqueConstraints={
        @UniqueConstraint(
            columnNames = {
                AbstractEntity.DB_NAME_COL,
                JdbcCatalogEntity.DB_PARENT_COL,
                }
            )
        }
    )
@NamedQueries(
        {
        @NamedQuery(
            name  = "jdbc.catalog-select-parent",
            query = "FROM JdbcCatalogEntity WHERE (parent = :parent) ORDER BY ident desc"
            ),
        @NamedQuery(
            name  = "jdbc.catalog-select-parent-name",
            query = "FROM JdbcCatalogEntity WHERE ((parent = :parent) AND (name = :name)) ORDER BY ident desc"
            )
        }
    )
public class JdbcCatalogEntity
extends ResourceStatusEntity
implements JdbcResource.JdbcCatalog
    {

    /**
     * Our persistence table name.
     *
     */
    public static final String DB_TABLE_NAME = "jdbc_catalog" ;

    /**
     * The persistence column name for our parent resource.
     *
     */
    public static final String DB_PARENT_COL = "parent" ;

    /**
     * Our Entity Factory implementation.
     *
     */
    @Repository
    public static class Factory
    extends AbstractFactory<JdbcResource.JdbcCatalog>
    implements JdbcResource.JdbcCatalog.Factory
        {

        @Override
        public Class<?> etype()
            {
            return JdbcCatalogEntity.class ;
            }

        /**
         * Insert a catalog into the database and update all the views of the parent resource.
         *
         */
        @CascadeEntityMethod
        protected JdbcResource.JdbcCatalog insert(final JdbcCatalogEntity entity)
            {
            super.insert(
                entity
                );
            for (final AdqlResource view : entity.parent().views().select())
                {
                this.views().cascade(
                    view,
                    entity
                    );
                }
            return entity ;
            }

        @Override
        @CreateEntityMethod
        public JdbcResource.JdbcCatalog create(final JdbcResource parent, final String name)
            {
            return this.insert(
                new JdbcCatalogEntity(
                    parent,
                    name
                    )
                );
            }

        @Override
        @SelectEntityMethod
        public Iterable<JdbcResource.JdbcCatalog> select(final JdbcResource parent)
            {
            return super.iterable(
                super.query(
                    "jdbc.catalog-select-parent"
                    ).setEntity(
                        "parent",
                        parent
                        )
                );
            }

        @Override
        @SelectEntityMethod
        public JdbcResource.JdbcCatalog select(final JdbcResource parent, final String name)
        throws NameNotFoundException
            {
            final JdbcResource.JdbcCatalog result = this.search(
                parent,
                name
                );
            if (result != null)
                {
                return result ;
                }
            else {
                throw new NameNotFoundException(
                    name
                    );
                }
            }

        @Override
        @SelectEntityMethod
        public JdbcResource.JdbcCatalog search(final JdbcResource parent, final String name)
            {
            return super.first(
                super.query(
                    "jdbc.catalog-select-parent-name"
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
         * Our Autowired view factory.
         *
         */
        @Autowired
        protected AdqlResource.AdqlCatalog.Factory views ;

        @Override
        public AdqlResource.AdqlCatalog.Factory views()
            {
            return this.views ;
            }

        /**
         * Our Autowired schema factory.
         *
         */
        @Autowired
        protected JdbcResource.JdbcSchema.Factory schemas ;

        @Override
        public JdbcResource.JdbcSchema.Factory schemas()
            {
            return this.schemas ;
            }
        }

    @Override
    public JdbcResource.JdbcCatalog.Views views()
        {
        return new JdbcResource.JdbcCatalog.Views()
            {
            @Override
            public Iterable<AdqlResource.AdqlCatalog> select()
                {
                return womble().resources().jdbc().views().catalogs().select(
                    JdbcCatalogEntity.this
                    );
                }

            @Override
            public AdqlResource.AdqlCatalog search(final AdqlResource parent)
                {
                return womble().resources().jdbc().views().catalogs().search(
                    parent,
                    JdbcCatalogEntity.this
                    );
                }
            };
        }

    @Override
    public JdbcResource.JdbcCatalog.Schemas schemas()
        {
        return new JdbcResource.JdbcCatalog.Schemas()
            {
            @Override
            public JdbcResource.JdbcSchema create(final String name)
                {
                return womble().resources().jdbc().catalogs().schemas().create(
                    JdbcCatalogEntity.this,
                    name
                    );
                }

            @Override
            public Iterable<JdbcResource.JdbcSchema> select()
                {
                return womble().resources().jdbc().catalogs().schemas().select(
                    JdbcCatalogEntity.this
                    );
                }

            @Override
            public JdbcResource.JdbcSchema select(final String name)
            throws NameNotFoundException
                {
                return womble().resources().jdbc().catalogs().schemas().select(
                    JdbcCatalogEntity.this,
                    name
                    );
                }

            @Override
            public JdbcResource.JdbcSchema search(final String name)
                {
                return womble().resources().jdbc().catalogs().schemas().search(
                    JdbcCatalogEntity.this,
                    name
                    );
                }

            @Override
            public List<JdbcResource.Diference> diff(final boolean push, final boolean pull)
                {
                return diff(
                    resource().metadata(),
                    new ArrayList<JdbcResource.Diference>(),
                    push,
                    pull
                    );
                }

            @Override
            public List<JdbcResource.Diference> diff(final DatabaseMetaData metadata, final List<JdbcResource.Diference> results, final boolean push, final boolean pull)
                {
                log.debug("Comparing schema for catalog [{}]", name());
                try {
                    //
                    // Scan the DatabaseMetaData for schema.
                    final ResultSet schemas = metadata.getSchemas(
                        name(),
                        null
                        );

                    final Map<String, JdbcResource.JdbcSchema> found = new HashMap<String, JdbcResource.JdbcSchema>();
                    while (schemas.next())
                        {
                        final String name = schemas.getString(
                            JdbcResource.JDBC_META_TABLE_SCHEM
                            );
                        log.debug("Checking database schema [{}]", name);

                        JdbcResource.JdbcSchema schema = this.search(
                            name
                            );
                        if (schema == null)
                            {
                            log.debug("Database schema [{}] is not registered", name);
                            if (pull)
                                {
                                log.debug("Registering missing schema [{}]", name);
                                schema = this.create(
                                    name
                                    );
                                }
                            else if (push)
                                {
                                log.debug("Deleting database schema [{}]", name);
                                log.error("-- delete schema  -- ");
                                }
                            else {
                                results.add(
                                    new JdbcResource.Diference(
                                        JdbcResource.Diference.Type.SCHEMA,
                                        null,
                                        name
                                        )
                                    );
                                }
                            }
                        found.put(
                            name,
                            schema
                            );
                        }
                    //
                    // Scan our own list of schema.
                    for (final JdbcResource.JdbcSchema schema : select())
                        {
                        log.debug("Checking registered schema [{}]", schema.name());
                        JdbcResource.JdbcSchema match = found.get(
                            schema.name()
                            );
                        //
                        // If we didn't find a match, create the object or disable our entry.
                        if (match == null)
                            {
                            log.debug("Registered schema [{}] is not in database", schema.name());
                            if (push)
                                {
                                log.debug("Creating database schema [{}]", schema.name());
                                log.error("-- create table -- ");
                                match = schema ;
                                }
                            else if (pull)
                                {
                                log.debug("Disabling registered schema [{}]", schema.name());
                                schema.status(
                                    Status.MISSING
                                    );
                                }
                            else {
                                results.add(
                                    new JdbcResource.Diference(
                                        JdbcResource.Diference.Type.SCHEMA,
                                        schema.name(),
                                        null
                                        )
                                    );
                                }
                            }
                        //
                        // If we have a match, then scan it.
                        if (match != null)
                            {
                            match.diff(
                                metadata,
                                results,
                                push,
                                pull
                                );
                            }
                        }
                    }
                catch (final SQLException ouch)
                    {
                    log.error("Error processing JDBC catalogs", ouch);
                    }
                return results ;
                }
            };
        }

    /**
     * Default constructor needs to be protected not private.
     * http://kristian-domagala.blogspot.co.uk/2008/10/proxy-instantiation-problem-from.html
     *
     */
    protected JdbcCatalogEntity()
        {
        super();
        }

    /**
     * Create a new catalog.
     *
     */
    protected JdbcCatalogEntity(final JdbcResource parent, final String name)
        {
        super(name);
        log.debug("new([{}]", name);
        this.parent = parent ;
        }

    /**
     * Our parent resource.
     *
     */
    @ManyToOne(
        fetch = FetchType.EAGER,
        targetEntity = JdbcResourceEntity.class
        )
    @JoinColumn(
        name = DB_PARENT_COL,
        unique = false,
        nullable = false,
        updatable = false
        )
    private JdbcResource parent ;

    @Override
    public JdbcResource parent()
        {
        return this.parent ;
        }

    @Override
    public Status status()
        {
        if (this.parent().status() == DataResource.Status.ENABLED)
            {
            return super.status();
            }
        else {
            return this.parent().status();
            }
        }

    @Override
    public JdbcResource resource()
        {
        return this.parent;
        }

    @Override
    public List<JdbcResource.Diference> diff(final boolean push, final boolean pull)
        {
        return diff(
            resource().metadata(),
            new ArrayList<JdbcResource.Diference>(),
            push,
            pull
            );
        }

    @Override
    public List<JdbcResource.Diference> diff(final DatabaseMetaData metadata, final List<JdbcResource.Diference> results, final boolean push, final boolean pull)
        {
        //
        // Check this catalog.
        // ....
        //
        // Check our schemas.
        return this.schemas().diff(
            metadata,
            results,
            push,
            pull
            );
        }
    }

