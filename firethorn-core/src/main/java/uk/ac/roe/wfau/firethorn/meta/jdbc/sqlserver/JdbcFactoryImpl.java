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
package uk.ac.roe.wfau.firethorn.meta.jdbc.sqlserver;

import java.sql.SQLException;
import java.sql.Statement;

import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Component;

import uk.ac.roe.wfau.firethorn.entity.annotation.CreateMethod;
import uk.ac.roe.wfau.firethorn.entity.annotation.DeleteMethod;
import uk.ac.roe.wfau.firethorn.entity.annotation.UpdateMethod;
import uk.ac.roe.wfau.firethorn.meta.jdbc.JdbcTable;
import uk.ac.roe.wfau.firethorn.meta.jdbc.JdbcConnection;
import uk.ac.roe.wfau.firethorn.meta.jdbc.JdbcTable.JdbcStatus;

/**
 * SQLServer JdbcTable factory implementation.
 *
 */
@Slf4j
@Component
public class JdbcFactoryImpl
    implements JdbcTable.JdbcFactory
    {

    /**
     * SQL statement to DELETE all data from a table.
     * 
     */
    protected static final String DELETE_DATA_STATEMENT = "DELETE FROM {catalog}.{schema}.{table}" ; 

    /**
     * SQL statement to DROP a table.
     * 
     */
    protected static final String DROP_TABLE_STATEMENT = "DROP TABLE {catalog}.{schema}.{table}" ; 

    @Override
    @CreateMethod
    public void create(JdbcTable table)
        {

        }

    @Override
    @UpdateMethod
    public void delete(JdbcTable table)
        {
        log.debug("Delete JdbcTable [{}]", table.name());
        JdbcConnection connection = table.resource().connection();
        final String statement = DELETE_DATA_STATEMENT.replace(
            "{catalog}",
            table.schema().catalog()
            ).replace(
                "{schema}",
                table.schema().name()
                ).replace(
                    "{table}",
                    table.name()
                    );
        try {
            log.debug("Executing SQL [{}]", statement);
            int count = connection.open().createStatement().executeUpdate(
                statement
                );
            log.debug("Count [{}]", count);
            table.meta().jdbc().status(
                JdbcStatus.DELETED
                );
            }
        catch (SQLException ouch)
            {
            log.warn("SQLException while attempting to delete table data [{}]", ouch.getMessage());
            log.warn("SQL statement [{}]", statement);
            }
        finally {
            connection.close();
            }
        }

    @Override
    @DeleteMethod
    public void drop(JdbcTable table)
        {
        log.debug("Drop JdbcTable [{}]", table.name());
        JdbcConnection connection = table.resource().connection();
        final String statement = DELETE_DATA_STATEMENT.replace(
            "{catalog}",
            table.schema().catalog()
            ).replace(
                "{schema}",
                table.schema().name()
                ).replace(
                    "{table}",
                    table.name()
                    );
        try {
            log.debug("Executing SQL [{}]", statement);
            int count = connection.open().createStatement().executeUpdate(
                statement
                );
            log.debug("Count [{}]", count);
            table.meta().jdbc().status(
                JdbcStatus.DROPPED
                );
            }
        catch (SQLException ouch)
            {
            log.warn("SQLException while attempting to delete table data [{}]", ouch.getMessage());
            log.warn("SQL statement [{}]", statement);
            }
        finally {
            connection.close();
            }
        }
    }
