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
package uk.ac.roe.wfau.firethorn.liquibase;

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
import uk.ac.roe.wfau.firethorn.meta.jdbc.JdbcColumn;
import uk.ac.roe.wfau.firethorn.meta.jdbc.JdbcSchema;
import uk.ac.roe.wfau.firethorn.meta.jdbc.JdbcTable;

/**
 *
 *
 */
@Slf4j
@Component
public class LiquibaseTableBuilder
extends JdbcBuilderBase
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

    public LiquibaseTableBuilder()
        {
        }

    @Override
    public JdbcTable create(final JdbcSchema schema, final AdqlQuery query)
        {
        log.debug("create(AdqlQuery)");
        log.debug("  Ident [{}]", query.ident());
        log.debug("  Name  [{}]", query.name());

        String safe = safe(query); 
        log.debug("  Safe  [{}]", safe);

//
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
        //
        // Create and execute the ChangeSet.
        final ChangeSet changeset = changeset();
        //
        // Delete old table.
//
//TOD0 - move to JdbcTable Factory Builder
        if (query.results().jdbc() != null)
            {
         // TODO *IF* this is in the same resource/schema !?            
         // TODO *IF* the old results don't have any data !?            
            changeset.addChange(
                new DeleteJdbcTableChange(
                    query.results().jdbc()
                    )
                );
            }
//TOD0 - move to JdbcTable Factory Builder
//
        //
        // Create new table.
        changeset.addChange(
            new CreateJdbcTableChange(
                table
                )
            );
        execute(
            schema.resource(),
            changeset
            );

        return table;
        }

    }
