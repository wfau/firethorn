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
import uk.ac.roe.wfau.firethorn.widgeon.adql.AdqlCatalog;
import uk.ac.roe.wfau.firethorn.widgeon.adql.AdqlResource;
import uk.ac.roe.wfau.firethorn.widgeon.adql.AdqlSchema;
import uk.ac.roe.wfau.firethorn.widgeon.data.DataResource;
import uk.ac.roe.wfau.firethorn.widgeon.data.DataComponentImpl;
import uk.ac.roe.wfau.firethorn.widgeon.data.DataComponent.Status;

/**
 * Hibernate based <code>JdbcSchema</code> implementation.
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
extends DataComponentImpl
implements JdbcSchema
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
    extends AbstractFactory<JdbcSchema>
    implements JdbcSchema.Factory
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
        protected JdbcSchema insert(final JdbcSchemaEntity entity)
            {
            super.insert(
                entity
                );
            for (final AdqlCatalog view : entity.parent().views().select())
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
        public JdbcSchema create(final JdbcCatalog parent, final String name)
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
        public Iterable<JdbcSchema> select(final JdbcCatalog parent)
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
        public JdbcSchema select(final JdbcCatalog parent, final String name)
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
        public Iterable<JdbcSchema> search(final JdbcCatalog parent, final String text)
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

        @Autowired
        protected AdqlSchema.Factory views ;

        @Override
        public AdqlSchema.Factory views()
            {
            return this.views ;
            }

        @Autowired
        protected JdbcTable.Factory tables ;

        @Override
        public JdbcTable.Factory tables()
            {
            return this.tables ;
            }

        @Autowired
        protected JdbcSchema.IdentFactory identifiers ;

        @Override
        public JdbcSchema.IdentFactory identifiers()
            {
            return identifiers ;
            }
        }

    @Override
    public JdbcSchema.Views views()
        {
        return new JdbcSchema.Views()
            {
            @Override
            public Iterable<AdqlSchema> select()
                {
                return womble().resources().adql().schemas().select(
                    JdbcSchemaEntity.this
                    );
                }

            @Override
            public AdqlSchema search(final AdqlCatalog parent)
                {
                return womble().resources().adql().schemas().select(
                    parent,
                    JdbcSchemaEntity.this
                    );
                }

            @Override
            public AdqlSchema search(final AdqlResource parent)
                {
                return womble().resources().adql().schemas().select(
                    parent,
                    JdbcSchemaEntity.this
                    );
                }
            };
        }

    @Override
    public JdbcSchema.Tables tables()
        {
        return new JdbcSchema.Tables()
            {
            @Override
            public JdbcTable create(final String name)
                {
                return womble().resources().jdbc().catalogs().schemas().tables().create(
                    JdbcSchemaEntity.this,
                    name
                    );
                }

            @Override
            public Iterable<JdbcTable> select()
                {
                return womble().resources().jdbc().catalogs().schemas().tables().select(
                    JdbcSchemaEntity.this
                    ) ;
                }

            @Override
            public JdbcTable select(final String name)
                {
                return womble().resources().jdbc().catalogs().schemas().tables().select(
                    JdbcSchemaEntity.this,
                    name
                    ) ;
                }

            @Override
            public Iterable<JdbcTable> search(final String text)
                {
                return womble().resources().jdbc().catalogs().schemas().tables().search(
                    JdbcSchemaEntity.this,
                    text
                    ) ;
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

                    final Map<String, JdbcTable> found = new HashMap<String, JdbcTable>();
                    while (tables.next())
                        {
                        final String name = tables.getString(JdbcResource.JDBC_META_TABLE_NAME);
                        final String type = tables.getString(JdbcResource.JDBC_META_TABLE_TYPE);
                        log.debug("Checking database table [{}][{}]", name, type);

                        JdbcTable table = this.select(
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
                                    new JdbcDiference(
                                        JdbcDiference.Type.TABLE,
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
                    for (final JdbcTable table : select())
                        {
                        log.debug("Checking registered table [{}]", table.name());
                        JdbcTable match = found.get(
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
                                    new JdbcDiference(
                                        JdbcDiference.Type.TABLE,
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
    protected JdbcSchemaEntity(final JdbcCatalog parent, final String name)
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
    private JdbcCatalog parent ;

    @Override
    public JdbcCatalog parent()
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
        return this.parent.resource();
        }

    @Override
    public JdbcCatalog catalog()
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

    @Override
    public String link()
        {
        return womble().resources().jdbc().catalogs().schemas().link(
            this
            );
        }
    }

