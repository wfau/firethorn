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
import uk.ac.roe.wfau.firethorn.widgeon.adql.AdqlCatalog;
import uk.ac.roe.wfau.firethorn.widgeon.adql.AdqlResource;
import uk.ac.roe.wfau.firethorn.widgeon.data.DataResource;
import uk.ac.roe.wfau.firethorn.widgeon.data.DataComponentImpl;
import uk.ac.roe.wfau.firethorn.widgeon.data.DataComponent.Status;

/**
 * Hibernate based <code>JdbcCatalog</code> implementation.
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
            name  = "jdbc.catalog-select-parent.name",
            query = "FROM JdbcCatalogEntity WHERE ((parent = :parent) AND (name = :name)) ORDER BY ident desc"
            ),
        @NamedQuery(
            name  = "jdbc.catalog-search-parent.text",
            query = "FROM JdbcCatalogEntity WHERE ((parent = :parent) AND (name LIKE :text)) ORDER BY ident desc"
            )
        }
    )
public class JdbcCatalogEntity
extends DataComponentImpl
implements JdbcCatalog
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
    extends AbstractFactory<JdbcCatalog>
    implements JdbcCatalog.Factory
        {

        @Override
        public Class<?> etype()
            {
            return JdbcCatalogEntity.class ;
            }

        /**
         * Insert a catalog into the database and update all the views of the parent resource.
         *
        @CascadeEntityMethod
        protected JdbcCatalog insert(final JdbcCatalogEntity entity)
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
         */

        @Override
        @CreateEntityMethod
        public JdbcCatalog create(final JdbcResource parent, final String name)
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
        public Iterable<JdbcCatalog> select(final JdbcResource parent)
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
        public JdbcCatalog select(final JdbcResource parent, final String name)
            {
            return super.first(
                super.query(
                    "jdbc.catalog-select-parent.name"
                    ).setEntity(
                        "parent",
                        parent
                    ).setString(
                        "name",
                        name
                    )
                );
            }

        @Override
        @SelectEntityMethod
        public Iterable<JdbcCatalog> search(final JdbcResource parent, final String text)
            {
            return super.iterable(
                super.query(
                    "jdbc.catalog-search-parent.text"
                    ).setEntity(
                        "parent",
                        parent
                    ).setString(
                        "text",
                        searchParam(
                            text
                            )
                        )
                );
            }

        /*
        @Autowired
        protected AdqlCatalog.Factory views ;

        @Override
        public AdqlCatalog.Factory views()
            {
            return this.views ;
            }
         */
        
        @Autowired
        protected JdbcSchema.Factory schemas ;

        @Override
        public JdbcSchema.Factory schemas()
            {
            return this.schemas ;
            }

        @Autowired
        protected JdbcCatalog.IdentFactory identifiers ;

        @Override
        public JdbcCatalog.IdentFactory identifiers()
            {
            return identifiers ;
            }
        }

    @Override
    public JdbcCatalog.Schemas schemas()
        {
        return new JdbcCatalog.Schemas()
            {
            @Override
            public JdbcSchema create(final String name)
                {
                return womble().jdbc().catalogs().schemas().create(
                    JdbcCatalogEntity.this,
                    name
                    );
                }

            @Override
            public Iterable<JdbcSchema> select()
                {
                return womble().jdbc().catalogs().schemas().select(
                    JdbcCatalogEntity.this
                    );
                }

            @Override
            public JdbcSchema select(final String name)
                {
                return womble().jdbc().catalogs().schemas().select(
                    JdbcCatalogEntity.this,
                    name
                    );
                }

            @Override
            public Iterable<JdbcSchema> search(final String text)
                {
                return womble().jdbc().catalogs().schemas().search(
                    JdbcCatalogEntity.this,
                    text
                    );
                }

            @Override
            public List<JdbcDiference> diff(final boolean push, final boolean pull)
                {
                return diff(
                    resource().metadata(),
                    new ArrayList<JdbcDiference>(),
                    push,
                    pull
                    );
                }

            @Override
            public List<JdbcDiference> diff(final DatabaseMetaData metadata, final List<JdbcDiference> results, final boolean push, final boolean pull)
                {
                log.debug("Comparing schema for catalog [{}]", name());
                try {
                    //
                    // Scan the DatabaseMetaData for schema.
                    /*
                     * JDBC 4 driver method
                     * 
                     * java.lang.AbstractMethodError
                     * http://sourceforge.net/p/jtds/discussion/104389/thread/fba2b1f6
                     * "The method you are trying to call is part of the newest JDBC 4 specs (since Java 1.6),
                     * but jTDS is a JDBC 3 driver and doesn't implement that method.
                     * For the time being you will have to call getSchemas() without arguments and do the filtering yourself."
                     * 
                     * 
                    final ResultSet schemas = metadata.getSchemas(
                        name(),
                        null
                        );
                     *   
                     * JDBC 3 method has no filter.
                     *  
                     */
                    final Map<String, JdbcSchema> found = new HashMap<String, JdbcSchema>();

                    final ResultSet schemas = metadata.getSchemas();
                    while (schemas.next())
                        {
                        final String cname = schemas.getString(
                            JdbcResource.JDBC_META_TABLE_CATALOG
                            );
                        final String sname = schemas.getString(
                            JdbcResource.JDBC_META_TABLE_SCHEM
                            );
                        log.debug("Checking database schema [{}.{}]", cname, sname);

                        if (cname == null)
                            {
                            // On ROE SQLServer, the schema is *always* null.
                            log.debug("Catalog is null, processing");
                            //continue ;

                            if (sname.equals("sys"))
                                {
                                log.debug("Schema is 'sys', skipping");
                                continue ;
                                }

                            if (sname.equals("INFORMATION_SCHEMA"))
                                {
                                log.debug("Schema is 'INFORMATION_SCHEMA', skipping");
                                continue ;
                                }
                            }
                        else {
                            if (cname.equals(name()))
                                {
                                log.debug("Catalog is [{}], processing", cname);                                
                                }
                            else {
                                log.debug("Catalog is [{}], skipping", cname);                                
                                continue ;
                                }
                            }

                        JdbcSchema schema = this.select(
                            sname
                            );
                        if (schema == null)
                            {
                            log.debug("Database schema [{}] is not registered", sname);
                            if (pull)
                                {
                                log.debug("Registering missing schema [{}]", sname);
                                schema = this.create(
                                    sname
                                    );
                                }
                            else if (push)
                                {
                                log.debug("Deleting database schema [{}]", sname);
                                log.error("-- delete schema  -- ");
                                }
                            else {
                                results.add(
                                    new JdbcDiference(
                                        JdbcDiference.Type.SCHEMA,
                                        null,
                                        sname
                                        )
                                    );
                                }
                            }
                        found.put(
                            sname,
                            schema
                            );
                        }
                    //
                    // Scan our own list of schema.
                    for (final JdbcSchema schema : select())
                        {
                        log.debug("Checking registered schema [{}.{}]", new Object[]{schema.catalog().name(), schema.name()});
                        JdbcSchema match = found.get(
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
                                    new JdbcDiference(
                                        JdbcDiference.Type.SCHEMA,
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
                catch (final Error ouch)
                    {
                    log.error("Error processing JDBC catalogs", ouch);
                    throw ouch ;
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
        if (parent().status().enabled())
            {
            return super.status();
            }
        else {
            return parent().status();
            }
        }

    @Override
    public void status(final Status status)
        {
        super.status(
            status
            );
        if (status.enabled())
            {
            parent().status(
                status
                );
            }
        }

    @Override
    public JdbcResource resource()
        {
        return this.parent;
        }

    @Override
    public List<JdbcDiference> diff(final boolean push, final boolean pull)
        {
        return diff(
            resource().metadata(),
            new ArrayList<JdbcDiference>(),
            push,
            pull
            );
        }

    @Override
    public List<JdbcDiference> diff(final DatabaseMetaData metadata, final List<JdbcDiference> results, final boolean push, final boolean pull)
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

    @Override
    public String link()
        {
        return womble().jdbc().catalogs().link(
            this
            );
        }
    }

