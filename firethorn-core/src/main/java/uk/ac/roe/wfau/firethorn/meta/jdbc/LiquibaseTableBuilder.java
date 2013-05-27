/*
 *  Copyright (C) 2013 Royal Observatory, University of Edinburgh, UK
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
package uk.ac.roe.wfau.firethorn.meta.jdbc;

import liquibase.change.Change;
import liquibase.change.ColumnConfig;
import liquibase.change.core.CreateTableChange;
import liquibase.change.core.DropTableChange;
import liquibase.changelog.ChangeSet;
import liquibase.changelog.DatabaseChangeLog;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.DatabaseException;
import liquibase.exception.MigrationFailedException;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Component;

import uk.ac.roe.wfau.firethorn.adql.query.AdqlQuery;

/**
 *
 *
 */
@Slf4j
@Component
public class LiquibaseTableBuilder
implements AdqlQuery.Builder
    {

    /**
     * Generate a SQL safe name.
     *
     */
    public String safe(final AdqlQuery query)
        {
        return safe(
            "QUERY_" + query.ident().toString()            
            );
        }

    /**
     * Generate a SQL safe name.
     *
     */
    public String safe(final String string)
        {
        return string.replaceAll(
            "[^\\p{Alnum}]+?",
            "_"
            );
        }

    //
    // Our default schema.
/*
 *
    private JdbcSchema schema ;
    @Override
    public JdbcSchema schema()
        {
        return this.schema ;
        }
    @Override
    public void schema(JdbcSchema schema)
        {
        this.schema = schema ;
        }
 *
 */

    //
    // Our parent resource.
/*
 *
    private JdbcResource resource ;
    @Override
    public JdbcResource resource()
        {
        return this.resource ;
        }
    @Override
    public void resource(JdbcResource resource)
        {
        this.resource = resource;
        }
 *
 */

    public LiquibaseTableBuilder()
        {
        }

    protected Change delete(final JdbcTable table)
        {
        log.debug("delete(JdbcTable)");
        log.debug("  Table [{}]", table.name());
        final DropTableChange change = new DropTableChange();
        change.setTableName(
            table.name()
            );
        change.setSchemaName(
            table.schema().name()
            );
        return change;
        }

    protected ChangeSet delete(final ChangeSet changeset, final JdbcTable table)
        {
        log.debug("delete(ChangeSet, JdbcTable)");
        log.debug("  Table [{}][{}]", table.ident(), table.name());
        changeset.addChange(
            delete(
                table
                )
            );
        return changeset;
        }

    protected Change create(final JdbcTable table)
        {
        log.debug("create(JdbcTable)");
        log.debug("  Table [{}][{}]", table.ident(), table.name());

        final CreateTableChange change = new CreateTableChange();
        change.setTableName(
            table.name()
            );
        change.setSchemaName(
            table.schema().name()
            );

        for (final JdbcColumn column : table.columns().select())
            {
            log.debug("CHANGE.addColumn() [{}]", column.name());
            change.addColumn(
                new ColumnConfig().setName(
                    column.name()
                    ).setType(
                        column.meta().jdbc().type().name()
                        )
                );
            }
        return change;
        }

    @Override
    public JdbcTable create(final JdbcSchema schema, final AdqlQuery query)
        {
        log.debug("create(AdqlQuery)");
        log.debug("  Ident [{}]", query.ident());
        log.debug("  Name  [{}]", query.name());

        String safe = safe(query); 
        log.debug("  Safe  [{}]", safe);

//TOD0 - move to JdbcTable Factory Builder
        //
        // Create the new JdbcTable .
        final JdbcTable table = schema.tables().create(
            query
            );
        //
        // Add the JdbcColumns.
        for (final AdqlQuery.SelectField field : query.fields())
            {
// Size is confused .... ?
// Include alias for unsafe names ?
// Create column direct from ColumnMeta
            log.debug("create(SelectField)");
            log.debug("  Name [{}]", field.name());
            log.debug("  Type [{}]", field.type().jdbc());
            log.debug("  Size [{}]", field.length());
            table.columns().create(
                field.name(),
                field.type().jdbc().code(),
                field.length()
                );
            }
//TOD0 - move to JdbcTable Factory Builder

        //
        // Create and execute the ChangeSet.
        final ChangeSet changeset = changeset(
            "create-".concat(safe)
            );
        //
        // Delete old table.
        if (query.results().jdbc() != null)
            {
         // TODO *IF* this is in the same resource/schema !?            
         // TODO *IF* the old results don't have any data !?            
            changeset.addChange(
                delete(
                    query.results().jdbc()
                    )
                );
            }
        //
        // Create new table.
        changeset.addChange(
            create(
                table
                )
            );
        execute(
            schema,
            changeset
            );

        return table;
        }

    protected Database database(final JdbcSchema schema)
        {
        try {
            final DatabaseFactory factory = DatabaseFactory.getInstance();

            final JdbcConnection connection = new JdbcConnection(
                schema.resource().connection().open()
                );

            return factory.findCorrectDatabaseImplementation(
                connection
                );
            }
        catch (final DatabaseException ouch)
            {
            log.debug("Exception creating Liquibase Database [{}]", ouch.getMessage());
            return null ;
            }
        }

    protected DatabaseChangeLog changelog()
        {
        return new DatabaseChangeLog();
        }

    protected ChangeSet changeset(String name)
        {
        return new ChangeSet(
            name,
            this.getClass().getName(),
            false,
            false,
            "file",
            "",
            ""
            );
        }

    protected void execute(final JdbcSchema schema, final ChangeSet changeset)
        {
        log.debug("Executing ChangeSet [{}]", changeset.getId());
        final Database database = database(
            schema
            );
        final DatabaseChangeLog changelog = changelog();
        try {
            final ChangeSet.ExecType result = changeset.execute(
                changelog,
                database
                );
            log.debug("ChangeSet result [{}]", result);
            database.commit();
            }
        catch (final MigrationFailedException ouch)
            {
            log.error("Failed to execute ChangeSet [{}]", ouch.getMessage());
            }
        catch (final DatabaseException ouch)
            {
            log.error("Failed to execute ChangeSet [{}]", ouch.getMessage());
            }
        finally
            {
            try {
                database.close();
                }
            catch (final DatabaseException ouch)
                {
                log.error("Failed to execute close database [{}]", ouch.getMessage());
                }
            }
        }
    }
