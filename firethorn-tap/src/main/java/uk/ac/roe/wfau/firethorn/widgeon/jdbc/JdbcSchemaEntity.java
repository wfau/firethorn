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

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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
import uk.ac.roe.wfau.firethorn.widgeon.ResourceStatusEntity;
import uk.ac.roe.wfau.firethorn.widgeon.adql.AdqlResource;

/**
 * BaseResource.BaseSchema implementation.
 *
 */
@Slf4j
@Entity()
@Access(
    AccessType.FIELD
    )
@Table(
    name = JdbcSchemaEntity.DB_TABLE_NAME,
    uniqueConstraints=
        @UniqueConstraint(
            columnNames = {
                AbstractEntity.DB_NAME_COL,
                JdbcSchemaEntity.DB_PARENT_COL,
                }
            )
    )
@NamedQueries(
        {
        @NamedQuery(
            name  = "jdbc.schema-select-parent",
            query = "FROM JdbcSchemaEntity WHERE parent = :parent ORDER BY ident desc"
            ),
            @NamedQuery(
                name  = "jdbc.schema-select-parent.name",
                query = "FROM JdbcSchemaEntity WHERE ((parent = :parent) AND (name = :name)) ORDER BY ident desc"
                ),
        @NamedQuery(
            name  = "jdbc.schema-search-parent.text",
            query = "FROM JdbcSchemaEntity WHERE ((parent = :parent) AND (name LIKE :text)) ORDER BY ident desc"
            )
        }
    )
public class JdbcSchemaEntity
extends ResourceStatusEntity
implements JdbcResource.JdbcSchema
    {

    /**
     * Our persistence table name.
     *
     */
    public static final String DB_TABLE_NAME = "jdbc_schema" ;

    /**
     * The persistence column name for our parent catalog.
     *
     */
    public static final String DB_PARENT_COL = "parent" ;

    /**
     * Our Entity Factory implementation.
     *
     */
    @Repository
    public static class Factory
    extends AbstractFactory<JdbcResource.JdbcSchema>
    implements JdbcResource.JdbcSchema.Factory
        {

        @Override
        public Class<?> etype()
            {
            return JdbcSchemaEntity.class ;
            }

        /**
         * Insert a schema into the database and update all the parent catalog views.
         *
         */
        @CascadeEntityMethod
        protected JdbcResource.JdbcSchema insert(final JdbcSchemaEntity entity)
            {
            super.insert(
                entity
                );
            for (final AdqlResource.AdqlCatalog view : entity.parent().views().select())
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
        public JdbcResource.JdbcSchema create(final JdbcResource.JdbcCatalog parent, final String name)
            {
            return this.insert(
                new JdbcSchemaEntity(
                    parent,
                    name
                    )
                );
            }

        @Override
        @SelectEntityMethod
        public Iterable<JdbcResource.JdbcSchema> select(final JdbcResource.JdbcCatalog parent)
            {
            return super.iterable(
                super.query(
                    "jdbc.schema-select-parent"
                    ).setEntity(
                        "parent",
                        parent
                        )
                );
            }

        @Override
        @SelectEntityMethod
        public JdbcResource.JdbcSchema select(final JdbcResource.JdbcCatalog parent, final String name)
            {
            return super.first(
                super.query(
                    "jdbc.schema-select-parent.name"
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
        public Iterable<JdbcResource.JdbcSchema> search(final JdbcResource.JdbcCatalog parent, final String text)
            {
            return super.iterable(
                super.query(
                    "jdbc.schema-search-parent.text"
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

        /**
         * Our Autowired view factory.
         *
         */
        @Autowired
        protected AdqlResource.AdqlSchema.Factory views ;

        @Override
        public AdqlResource.AdqlSchema.Factory views()
            {
            return this.views ;
            }

        /**
         * Our Autowired table factory.
         *
         */
        @Autowired
        protected JdbcResource.JdbcTable.Factory tables ;

        @Override
        public JdbcResource.JdbcTable.Factory tables()
            {
            return this.tables ;
            }
        }

    @Override
    public JdbcResource.JdbcSchema.Views views()
        {
        return new JdbcResource.JdbcSchema.Views()
            {
            @Override
            public Iterable<AdqlResource.AdqlSchema> select()
                {
                return womble().resources().jdbc().views().catalogs().schemas().select(
                    JdbcSchemaEntity.this
                    );
                }

            @Override
            public AdqlResource.AdqlSchema search(final AdqlResource.AdqlCatalog parent)
                {
                return womble().resources().jdbc().views().catalogs().schemas().select(
                    parent,
                    JdbcSchemaEntity.this
                    );
                }

            @Override
            public AdqlResource.AdqlSchema search(final AdqlResource parent)
                {
                return womble().resources().jdbc().views().catalogs().schemas().select(
                    parent,
                    JdbcSchemaEntity.this
                    );
                }
            };
        }

    @Override
    public JdbcResource.JdbcSchema.Tables tables()
        {
        return new JdbcResource.JdbcSchema.Tables()
            {
            @Override
            public JdbcResource.JdbcTable create(final String name)
                {
                return womble().resources().jdbc().catalogs().schemas().tables().create(
                    JdbcSchemaEntity.this,
                    name
                    );
                }

            @Override
            public Iterable<JdbcResource.JdbcTable> select()
                {
                return womble().resources().jdbc().catalogs().schemas().tables().select(
                    JdbcSchemaEntity.this
                    ) ;
                }

            @Override
            public JdbcResource.JdbcTable select(final String name)
                {
                return womble().resources().jdbc().catalogs().schemas().tables().select(
                    JdbcSchemaEntity.this,
                    name
                    ) ;
                }

            @Override
            public Iterable<JdbcResource.JdbcTable> search(final String text)
                {
                return womble().resources().jdbc().catalogs().schemas().tables().search(
                    JdbcSchemaEntity.this,
                    text
                    ) ;
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
                log.debug("Comparing tables for schema [{}]", name());
                try {
                    //
                    // Scan the DatabaseMetaData for tables and views.
                    final ResultSet tables = metadata.getTables(
                        catalog().name(),
                        name(),
                        null,
                        new String[]
                            {
                            JdbcResource.JDBC_META_TABLE_TYPE_TABLE,
                            JdbcResource.JDBC_META_TABLE_TYPE_VIEW
                            }
                        );

                    final Map<String, JdbcResource.JdbcTable> found = new HashMap<String, JdbcResource.JdbcTable>();
                    while (tables.next())
                        {
                        final String name = tables.getString(JdbcResource.JDBC_META_TABLE_NAME);
                        final String type = tables.getString(JdbcResource.JDBC_META_TABLE_TYPE);
                        log.debug("Checking database table [{}][{}]", name, type);

                        JdbcResource.JdbcTable table = this.select(
                            name
                            );
                        if (table == null)
                            {
                            log.debug("Database table [{}] is not registered", name);
                            if (pull)
                                {
                                log.debug("Registering missing table [{}]", name);
                                table = this.create(
                                    name
                                    );
                                }
                            else if (push)
                                {
                                log.debug("Deleting database table [{}]", name);
                                try {
                                    final String sql = "DROP TABLE {table} ;".replace(
                                        "{table}",
                                        name
                                        );
                                    log.debug("SQL [{}]", sql);
                                    final Connection connection = metadata.getConnection();
                                    final Statement  statement  = connection.createStatement();
                                    statement.executeUpdate(sql);
                                    }
                                catch (final SQLException ouch)
                                    {
                                    log.error("Exception dropping table [{}]", name);
                                    throw new RuntimeException(
                                        ouch
                                        );
                                    }
                                }
                            else {
                                results.add(
                                    new JdbcResource.Diference(
                                        JdbcResource.Diference.Type.TABLE,
                                        null,
                                        name
                                        )
                                    );
                                }
                            }
                        if (table != null)
                            {
                            found.put(
                                name,
                                table
                                );
                            }
                        }
                    //
                    // Scan our own list of schema.
                    for (final JdbcResource.JdbcTable table : select())
                        {
                        log.debug("Checking registered table [{}]", table.name());
                        JdbcResource.JdbcTable match = found.get(
                            table.name()
                            );
                        //
                        // If we didn't find a match, create the object or disable our entry.
                        if (match == null)
                            {
                            log.debug("Registered table [{}] is not in database", table.name());
                            if (push)
                                {
                                log.debug("Creating database table [{}]", table.name());
                                try {
                                    final String sql = "CREATE TABLE {table} () ;".replace(
                                        "{table}",
                                        table.name()
                                        );
                                    log.debug("SQL [{}]", sql);
                                    final Connection connection = metadata.getConnection();
                                    final Statement  statement  = connection.createStatement();
                                    statement.executeUpdate(sql);
                                    }
                                catch (final SQLException ouch)
                                    {
                                    log.error("Exception creating table [{}]", table.name());
                                    throw new RuntimeException(
                                        ouch
                                        );
                                    }
                                match = table ;
                                }
                            else if (pull)
                                {
                                log.debug("Disabling registered table [{}]", table.name());
                                table.status(
                                    Status.MISSING
                                    );
                                }
                            else {
                                results.add(
                                    new JdbcResource.Diference(
                                        JdbcResource.Diference.Type.TABLE,
                                        table.name(),
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
    protected JdbcSchemaEntity()
        {
        super();
        }

    /**
     * Create a new catalog.
     *
     */
    protected JdbcSchemaEntity(final JdbcResource.JdbcCatalog parent, final String name)
        {
        super(name);
        log.debug("new([{}]", name);
        this.parent = parent ;
        }

    /**
     * Our parent catalog.
     *
     */
    @ManyToOne(
        fetch = FetchType.EAGER,
        targetEntity = JdbcCatalogEntity.class
        )
    @JoinColumn(
        name = DB_PARENT_COL,
        unique = false,
        nullable = false,
        updatable = false
        )
    private JdbcResource.JdbcCatalog parent ;

    @Override
    public JdbcResource.JdbcCatalog parent()
        {
        return this.parent ;
        }

    @Override
    public Status status()
        {
        if (this.parent().status() == Status.ENABLED)
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
        return this.parent.resource();
        }

    @Override
    public JdbcResource.JdbcCatalog catalog()
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
        // Check this schema.
        // ....
        //
        // Check our tables.
        return this.tables().diff(
            metadata,
            results,
            push,
            pull
            );
        }
    }

