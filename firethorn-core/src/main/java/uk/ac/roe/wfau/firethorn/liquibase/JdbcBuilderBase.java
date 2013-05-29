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


import liquibase.changelog.ChangeSet;
import liquibase.changelog.DatabaseChangeLog;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.DatabaseException;
import liquibase.exception.MigrationFailedException;
import lombok.extern.slf4j.Slf4j;
import uk.ac.roe.wfau.firethorn.meta.jdbc.JdbcResource;

/**
 *
 *
 */
@Slf4j
public class JdbcBuilderBase
    {

    protected String unique()
        {
        return String.valueOf(
            System.currentTimeMillis()
            ) ;
        }

    protected DatabaseChangeLog changelog()
        {
        return new DatabaseChangeLog();
        }

    protected ChangeSet changeset()
        {
        return new ChangeSet(
            unique(), // ID
            this.getClass().getName(), // Author
            false,  // AlwaysRun
            false,  // RunOnChange
            "file", // FilePath
            "",     // ContextList
            ""      // DatabaseList
            );
        }
    
    protected Database database(final JdbcResource resource)
        {
        try {
            final DatabaseFactory factory = DatabaseFactory.getInstance();

            final JdbcConnection connection = new JdbcConnection(
                resource.connection().open()
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
    
    protected void execute(final JdbcResource resource, final ChangeSet changeset)
        {
        log.debug("Executing ChangeSet [{}]", changeset.getId());
        final Database database = database(
            resource
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
