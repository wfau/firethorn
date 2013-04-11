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
import liquibase.change.core.AddColumnChange;
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
    public String safe(final String input)
        {
        return input.replaceAll(
            "[^\\p{Alnum}]+?",
            "_"
            );
        }

    //
    // Our default schema.
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
    
    //
    // Our parent resource.
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

    public LiquibaseTableBuilder()
        {
        }

    protected Change delete(final JdbcTable table)
        {
        log.debug("delete(JdbcTable)");
        log.debug("  Table [{}]", table.name());
        DropTableChange change = new DropTableChange();
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
        CreateTableChange change = new CreateTableChange();
        change.setTableName(
            table.name()
            );
        change.setSchemaName(
            table.schema().name()
            );

        for (JdbcColumn column : table.columns().select())
            {
            change.addColumn(
                new ColumnConfig().setName(
                    column.name()
                    ).setType(
                        column.info().jdbc().type().name()
                        )
                );
            }
        return change;
        }

    /*
    protected ChangeSet create(final ChangeSet changeset, final JdbcTable table)
        {
        log.debug("create(ChangeSet, JdbcTable)");
        log.debug("  Table [{}][{}]", table.ident(), table.name());
        changeset.addChange(
            create(
                table
                )
            );
        for (JdbcColumn column : table.columns().select())
            {
            changeset.addChange(
                create(
                    column
                    )
                );
            }
        return changeset;
        }
     */

    /*
    protected ChangeSet create(final ChangeSet changeset, final JdbcColumn column)
        {
        log.debug("create(ChangeSet, JdbcColumn)");
        log.debug("  Column [{}][{}]", column.ident(), column.name());
        changeset.addChange(
            create(
                column
                )
            );
        return changeset;
        }
     */

    /*
    protected Change create(final JdbcColumn column)
        {
        log.debug("create(JdbcColumn)");
        log.debug("  Column [{}][{}]", column.ident(), column.name());
        AddColumnChange change = new AddColumnChange();
        change.addColumn(
            new ColumnConfig().setName(
                column.name()
                ).setType(
                    column.info().jdbc().toString()
                    )
            );
        return change;
        }
     */

    @Override
    public JdbcTable create(final AdqlQuery query)
        {
        log.debug("create(AdqlQuery)");
        log.debug("  Query [{}][{}]", query.ident(), query.name());
        //
        // Create the new JdbcTable .
        JdbcTable table = this.schema.tables().create(
            "RESULT_0004"
            );
        //
        // Add the JdbcColumns.
        for (AdqlQuery.ColumnMeta meta : query.items())
            {
// Size is confused .... ?
// Create column direct from ColumnMeta
            table.columns().create(
                meta.name(),
                meta.type().jdbc().code(),
                meta.size()
                );
            }
        //
        // Create our ChangeSet.
        ChangeSet changeset = changeset();
        if (query.results().jdbc() != null)
            {
            changeset.addChange(
                delete(
                    query.results().jdbc()
                    )
                );
            }
        changeset.addChange(
            create(
                table
                )
            );
        //
        // Execute the changeset.
        execute(
            changeset
            );        

        return table;
        }

    protected String name(final ChangeSet changeset)
        {
        StringBuilder name = new StringBuilder(
            "query"
            ); 
        name.append(
            "_"
            );
        name.append(
            changeset.getId()
            );
        return name.toString();
        }

    protected Database database()
        {
        try {
            DatabaseFactory factory = DatabaseFactory.getInstance();
            
            JdbcConnection connection = new JdbcConnection(
                this.resource.connection().open()
                ); 

            return factory.findCorrectDatabaseImplementation(
                connection 
                );
            }
        catch (DatabaseException ouch)
            {
            log.debug("Exception creating Liquibase Database [{}]", ouch.getMessage());
            return null ;
            } 
        }

    protected DatabaseChangeLog changelog()
        {
        return new DatabaseChangeLog();
        }

    private static int changesetcount = 0 ;
    protected ChangeSet changeset()
        {
        return new ChangeSet(
            "changeset_".concat(String.valueOf(changesetcount++)),
            "author",
            false,
            false,
            "file",
            "",
            ""
            ); 
        }

    protected void execute(Change change)
        {
        log.debug("Executing Change [{}]", change);
        ChangeSet changeset = changeset();
        changeset.addChange(
            change
            );
        execute(
            changeset
            );
        }

    protected void execute(ChangeSet changeset)
        {
        log.debug("Executing ChangeSet [{}]", changeset.getId());
        Database database = database();
        DatabaseChangeLog changelog = changelog();
        try {
            ChangeSet.ExecType result = changeset.execute(
                changelog,
                database
                );
            log.debug("ChangeSet result [{}]", result);
            //database.commit();
            //database.close();
            }
        catch (MigrationFailedException ouch)
            {
            log.error("Failed to execute ChangeSet [{}]", ouch.getMessage());
            }
        }
    }
