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

import org.hibernate.annotations.Index;
import org.hibernate.annotations.NamedQueries;
import org.hibernate.annotations.NamedQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import uk.ac.roe.wfau.firethorn.entity.AbstractEntity;
import uk.ac.roe.wfau.firethorn.entity.AbstractFactory;
import uk.ac.roe.wfau.firethorn.entity.annotation.CascadeEntityMethod;
import uk.ac.roe.wfau.firethorn.entity.annotation.CreateEntityMethod;
import uk.ac.roe.wfau.firethorn.entity.annotation.SelectEntityMethod;
import uk.ac.roe.wfau.firethorn.widgeon.adql.AdqlCatalog;
import uk.ac.roe.wfau.firethorn.widgeon.adql.AdqlResource;
import uk.ac.roe.wfau.firethorn.widgeon.adql.AdqlSchema;
import uk.ac.roe.wfau.firethorn.widgeon.adql.AdqlTable;
import uk.ac.roe.wfau.firethorn.widgeon.base.BaseTable;
import uk.ac.roe.wfau.firethorn.widgeon.data.DataComponentImpl;
import uk.ac.roe.wfau.firethorn.widgeon.data.DataComponent.Status;

/**
 * Hibernate based <code>JdbcTable</code> implementation.
 *
 */
@Slf4j
@Entity()
@Access(
    AccessType.FIELD
    )
@Table(
    name = JdbcTableEntity.DB_TABLE_NAME,
    uniqueConstraints =
        @UniqueConstraint(
            name = JdbcTableEntity.DB_NAME_PARENT_IDX, 
            columnNames = {
                AbstractEntity.DB_NAME_COL,
                JdbcTableEntity.DB_PARENT_COL,
                }
            )
    )
@NamedQueries(
        {
        @NamedQuery(
            name  = "jdbc.table-select-parent",
            query = "FROM JdbcTableEntity WHERE parent = :parent ORDER BY ident desc"
            ),
            @NamedQuery(
                name  = "jdbc.table-select-parent.name",
                query = "FROM JdbcTableEntity WHERE ((parent = :parent) AND (name = :name)) ORDER BY ident desc"
                ),
        @NamedQuery(
            name  = "jdbc.table-search-parent.text",
            query = "FROM JdbcTableEntity WHERE ((parent = :parent) AND (name LIKE :text)) ORDER BY ident desc"
            )
        }
    )
@org.hibernate.annotations.Table(
    appliesTo = JdbcTableEntity.DB_TABLE_NAME, 
    indexes =
        {
        @Index(
            name= JdbcTableEntity.DB_NAME_IDX,
            columnNames =
                {
                AbstractEntity.DB_NAME_COL
                }
            )
        }
    )
public class JdbcTableEntity
extends DataComponentImpl
implements JdbcTable
    {

    /**
     * Our persistence table name.
     *
     */
    public static final String DB_TABLE_NAME = "jdbc_table" ;

    /**
     * The column name for our parent.
     *
     */
    public static final String DB_PARENT_COL = "parent" ;

    /**
     * The index for our name.
     *
     */
    public static final String DB_NAME_IDX = "jdbc_table_name_idx" ;

    /**
     * The index for our parent.
     *
     */
    public static final String DB_PARENT_IDX = "jdbc_table_parent_idx" ;

    /**
     * The index for our name and parent.
     *
     */
    public static final String DB_NAME_PARENT_IDX = "jdbc_table_name_parent_idx" ;

    /**
     * Our Entity Factory implementation.
     *
     */
    @Repository
    public static class Factory
    extends AbstractFactory<JdbcTable>
    implements JdbcTable.Factory
        {

        @Override
        public Class<?> etype()
            {
            return JdbcTableEntity.class ;
            }

        /**
         * Insert a table into the database and update all the parent views.
         *
         */
        @CascadeEntityMethod
        protected JdbcTable insert(final JdbcTableEntity entity)
            {
            super.insert(
                entity
                );
/*
            for (final AdqlSchema view : entity.parent().views().select())
                {
                this.views().cascade(
                    view,
                    entity
                    );
                }
 */                
            return entity ;
            }

        @Override
        @CreateEntityMethod
        public JdbcTable create(final JdbcSchema parent, final String name)
            {
            return this.insert(
                new JdbcTableEntity(
                    parent,
                    name
                    )
                );
            }

        @Override
        @SelectEntityMethod
        public Iterable<JdbcTable> select(final JdbcSchema parent)
            {
            return super.list(
                super.query(
                    "jdbc.table-select-parent"
                    ).setEntity(
                        "parent",
                        parent
                        )
                );
            }

        @Override
        @SelectEntityMethod
        public JdbcTable select(final JdbcSchema parent, final String name)
            {
            return super.first(
                super.query(
                    "jdbc.table-select-parent.name"
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
        public Iterable<JdbcTable> search(final JdbcSchema parent, final String text)
            {
            return super.iterable(
                super.query(
                    "jdbc.table-search-parent.text"
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
        protected AdqlTable.Factory views ;

        @Override
        public AdqlTable.Factory views()
            {
            return this.views ;
            }

        @Autowired
        protected JdbcColumn.Factory columns ;

        @Override
        public JdbcColumn.Factory columns()
            {
            return this.columns ;
            }

        @Autowired
        protected JdbcTable.IdentFactory identifiers ;

        @Override
        public JdbcTable.IdentFactory identifiers()
            {
            return identifiers ;
            }
        }

    @Override
    public BaseTable.Views views()
        {
        return new BaseTable.Views()
            {
            @Override
            public Iterable<AdqlTable> select()
                {
                return womble().adql().tables().select(
                    JdbcTableEntity.this
                    );
                }
            @Override
            public AdqlTable search(final AdqlSchema parent)
                {
                return womble().adql().tables().select(
                    parent,
                    JdbcTableEntity.this
                    );
                }
            };
        }

    @Override
    public JdbcTable.Columns columns()
        {
        return new JdbcTable.Columns()
            {
            @Override
            public JdbcColumn create(final String name)
                {
                return womble().jdbc().catalogs().schemas().tables().columns().create(
                    JdbcTableEntity.this,
                    name
                    );
                }

            @Override
            public Iterable<JdbcColumn> select()
                {
                return womble().jdbc().catalogs().schemas().tables().columns().select(
                    JdbcTableEntity.this
                    ) ;
                }

            @Override
            public JdbcColumn select(final String name)
                {
                return womble().jdbc().catalogs().schemas().tables().columns().select(
                    JdbcTableEntity.this,
                    name
                    ) ;
                }

            @Override
            public Iterable<JdbcColumn> search(final String name)
                {
                return womble().jdbc().catalogs().schemas().tables().columns().search(
                    JdbcTableEntity.this,
                    name
                    ) ;
                }

            @Override
            public List<JdbcDiference> diff(final boolean push, final boolean pull)
                {
                return diff(
                    resource().jdbc().metadata(),
                    new ArrayList<JdbcDiference>(),
                    push,
                    pull
                    );
                }

            @Override
            public List<JdbcDiference> diff(final DatabaseMetaData metadata, final List<JdbcDiference> results, final boolean push, final boolean pull)
                {
                log.debug("Comparing columns for table [{}]", name());
                try {
                    //
                    // Scan the DatabaseMetaData for columns.
                    final ResultSet columns = metadata.getColumns(
                        catalog().name(),
                        schema().name(),
                        name(),
                        null
                        );

                    final Map<String, JdbcColumn> found = new HashMap<String, JdbcColumn>();
                    while (columns.next())
                        {
                        final String table = columns.getString(JdbcResource.JDBC_META_TABLE_NAME);
                        final String name  = columns.getString(JdbcResource.JDBC_META_COLUMN_NAME);
                        final String type  = columns.getString(JdbcResource.JDBC_META_COLUMN_TYPE_NAME);
                        log.debug("Checking database column [{}.{}.{}.{}]", new Object[]{catalog().name(), schema().name(), name(), name});

                        JdbcColumn column = this.select(
                            name
                            );
                        if (column == null)
                            {
                            log.debug("Database column[{}] is not registered", name);
                            if (pull)
                                {
                                log.debug("Registering missing column [{}]", name);
                                column= this.create(
                                    name
                                    );
                                }
                            else if (push)
                                {
                                log.debug("Deleting database column [{}]", name);
                                log.error("-- delete column -- ");
                                try {
                                    final String sql = "ALTER TABLE {table} DROP COLUMN {column} ;".replace(
                                        "{table}",
                                        table
                                        ).replace(
                                            "{column}",
                                            name
                                            );
                                    log.debug("SQL [{}]", sql);
                                    final Connection connection = metadata.getConnection();
                                    final Statement  statement  = connection.createStatement();
                                    statement.executeUpdate(sql);
                                    }
                                catch (final SQLException ouch)
                                    {
                                    log.error("Exception dropping column [{}]", column);
                                    throw new RuntimeException(
                                        ouch
                                        );
                                    }
                                }
                            else {
                                results.add(
                                    new JdbcDiference(
                                        JdbcDiference.Type.COLUMN,
                                        null,
                                        name
                                        )
                                    );
                                }
                            }
                        if (column != null)
                            {
                            found.put(
                                name,
                                column
                                );
                            }
                        }
                    //
                    // Scan our own list of schema.
                    for (final JdbcColumn column : select())
                        {
                        log.debug("Checking registered column [{}.{}.{}.{}]", new Object[]{column.catalog().name(), column.schema().name(), column.table().name(), column.name()});
                        JdbcColumn match = found.get(
                            column.name()
                            );
                        //
                        // If we didn't find a match, disable our entry.
                        if (match == null)
                            {
                            log.debug("Registered column [{}] is not in database", column.name());
                            if (push)
                                {
                                log.debug("Creating database column [{}]", column.name());
                                try {
                                    final String sql = "ALTER TABLE {table} ADD COLUMN {column} VARCHAR(10) ;".replace(
                                        "{table}",
                                        column.parent().name()
                                        ).replace(
                                            "{column}",
                                            column.name()
                                            );
                                    log.debug("SQL [{}]", sql);
                                    final Connection connection = metadata.getConnection();
                                    final Statement  statement  = connection.createStatement();
                                    statement.executeUpdate(sql);
                                    }
                                catch (final SQLException ouch)
                                    {
                                    log.error("Exception creating column [{}]", column.name());
                                    throw new RuntimeException(
                                        ouch
                                        );
                                    }
                                match = column ;
                                }
                            else if (pull)
                                {
                                log.debug("Disabling registered column [{}]", column.name());
                                column.status(
                                    Status.MISSING
                                    );
                                }
                            else {
                                results.add(
                                    new JdbcDiference(
                                        JdbcDiference.Type.COLUMN,
                                        column.name(),
                                        null
                                        )
                                    );
                                }
                            }
                        //
                        // If we found a match, then scan it.
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

            @Override
            public void scan()
                {
                scan(
                    resource().jdbc().metadata()
                    );
                }

            @Override
            public void scan(DatabaseMetaData metadata)
                {
                log.debug("Comparing columns for table [{}]", name());
                try {
                    //
                    // Scan the DatabaseMetaData for columns.
                    final ResultSet columns = metadata.getColumns(
                        catalog().name(),
                        schema().name(),
                        name(),
                        null
                        );

                    final Map<String, JdbcColumn> found = new HashMap<String, JdbcColumn>();
                    while (columns.next())
                        {
                        final String table = columns.getString(JdbcResource.JDBC_META_TABLE_NAME);
                        final String name  = columns.getString(JdbcResource.JDBC_META_COLUMN_NAME);
                        final String type  = columns.getString(JdbcResource.JDBC_META_COLUMN_TYPE_NAME);
                        log.debug("Checking database column [{}.{}.{}.{}]", new Object[]{catalog().name(), schema().name(), name(), name});
                        //
                        // This is a performance hot spot.
                        // It might be faster to load all of the columns for this table, and then process them in Java. 
                        JdbcColumn column = this.select(
                            name
                            );

                        if (column == null)
                            {
                            log.debug("Database column[{}] is not registered", name);
                            log.debug("Registering missing column [{}]", name);
                            column= this.create(
                                name
                                );
                            }
                        if (column != null)
                            {
                            found.put(
                                name,
                                column
                                );
                            }
                        }
                    //
                    // Scan our own list of schema.
                    for (final JdbcColumn column : select())
                        {
                        log.debug("Checking registered column [{}.{}.{}.{}]", new Object[]{column.catalog().name(), column.schema().name(), column.table().name(), column.name()});
                        JdbcColumn match = found.get(
                            column.name()
                            );
                        //
                        // If we didn't find a match, disable our entry.
                        if (match == null)
                            {
                            log.debug("Registered column [{}] is not in database", column.name());
                            log.debug("Disabling registered column [{}]", column.name());
                            column.status(
                                Status.MISSING
                                );
                            }
                        }
                    }
                catch (final SQLException ouch)
                    {
                    log.error("Error processing JDBC column metadata", ouch);
                    }
                }
            };
        }

    /**
     * Default constructor needs to be protected not private.
     * http://kristian-domagala.blogspot.co.uk/2008/10/proxy-instantiation-problem-from.html
     *
     */
    protected JdbcTableEntity()
        {
        super();
        }

    /**
     * Create a new catalog.
     *
     */
    protected JdbcTableEntity(final JdbcSchema parent, final String name)
        {
        super(name);
        log.debug("new([{}]", name);
        this.parent = parent ;
        }

    /**
     * Our parent column.
     *
     */
    @Index(
        name = DB_PARENT_IDX
        )
    @ManyToOne(
        fetch = FetchType.LAZY,
        targetEntity = JdbcSchemaEntity.class
        )
    @JoinColumn(
        name = DB_PARENT_COL,
        unique = false,
        nullable = false,
        updatable = false
        )
    private JdbcSchema parent ;

    @Override
    public JdbcSchema parent()
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
        return this.parent.catalog().resource();
        }

    @Override
    public JdbcCatalog catalog()
        {
        return this.parent.catalog();
        }

    @Override
    public JdbcSchema schema()
        {
        return this.parent;
        }

    @Override
    public List<JdbcDiference> diff(final boolean push, final boolean pull)
        {
        return diff(
            resource().jdbc().metadata(),
            new ArrayList<JdbcDiference>(),
            push,
            pull
            );
        }

    @Override
    public List<JdbcDiference> diff(final DatabaseMetaData metadata, final List<JdbcDiference> results, final boolean push, final boolean pull)
        {
        //
        // Check this table.
        // ....

        //
        // Check our columns.
        return this.columns().diff(
            metadata,
            results,
            push,
            pull
            );
        }

    @Override
    public String link()
        {
        return womble().jdbc().catalogs().schemas().tables().link(
            this
            );
        }

    @Override
    public void scan()
        {
        scan(
            resource().jdbc().metadata()
            );
        }

    @Override
    public void scan(DatabaseMetaData metadata)
        {
        columns().scan(
            metadata
            );
        }
    }

