/*
 *  Copyright (C) 2018 Royal Observatory, University of Edinburgh, UK
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

package uk.ac.roe.wfau.firethorn.meta.jdbc.postgresql;

import java.sql.SQLException;

import org.postgresql.Driver;

import lombok.extern.slf4j.Slf4j;
import uk.ac.roe.wfau.firethorn.access.ProtectionException;
import uk.ac.roe.wfau.firethorn.exception.NotImplementedException;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlColumn;
import uk.ac.roe.wfau.firethorn.meta.jdbc.JdbcColumn;
import uk.ac.roe.wfau.firethorn.meta.jdbc.JdbcConnection;
import uk.ac.roe.wfau.firethorn.meta.jdbc.JdbcOperator;
import uk.ac.roe.wfau.firethorn.meta.jdbc.JdbcSchema;
import uk.ac.roe.wfau.firethorn.meta.jdbc.JdbcTable;

/**
 * Operations for a Postgresql database.
 * 
 */
@Slf4j
public class PostgresOperator
implements JdbcOperator
    {
    /*
     * 
     */
    @Override
    public Driver driver()
        {
        return new org.postgresql.Driver();
        }

    @Override
    public String url()
        {
        //jdbc:postgresql://{host}:{port}/${database}
        final StringBuilder builder = new StringBuilder();
        builder.append("jdbc:postgresql://");
        builder.append(this.connection.host());
        if (this.connection.port() != null)
            {
            builder.append(":");
            builder.append(this.connection.port());
            }
        builder.append("/");
        builder.append(this.connection.database());
        return builder.toString();
        }

    /**
     * Public constructor.
     * 
     */
    public PostgresOperator(final JdbcConnection connection)
        {
        super();
        this.connection = connection ;
        }

    /**
     * Our parent {@link JdbcConnection}.
     *  
     */
    private JdbcConnection connection;

    /**
     * Our parent {@link JdbcConnection}.
     *  
     */
    public JdbcConnection connection()
        {
        return this.connection;
        }

    @Override
    public void create(JdbcSchema schema) throws ProtectionException
        {
        throw new NotImplementedException();        
        }

    @Override
    public void drop(JdbcSchema schema) throws ProtectionException
        {
        throw new NotImplementedException();        
        }

    @Override
    public void create(JdbcTable table) throws ProtectionException
        {
        log.debug("Create JdbcTable [{}]", table.name());

        final StringBuilder statement = new StringBuilder(
            "CREATE TABLE "
            );
        fullname(
            statement,
            table
            );

        boolean comma = false ;
        statement.append(" (");
        for(JdbcColumn column : table.columns().select())
            {
            log.debug("Create JdbcColumn [{}]", column.name());
            if (comma)
                {
                statement.append(" , ");
                }
            else {
                comma = true ;
                }
            sqlname(
                statement,
                column
                );
            statement.append(" ");
            sqltype(
                statement,
                column.meta().jdbc()
                );
            }
        statement.append(" )");

        execute(
            statement.toString()
            );
        }

    @Override
    public void delete(JdbcTable table) throws ProtectionException
        {
        throw new NotImplementedException();        
        }

    @Override
    public void drop(JdbcTable table) throws ProtectionException
        {
        throw new NotImplementedException();        
        }

    @Override
    public void create(JdbcColumn column) throws ProtectionException
        {
        throw new NotImplementedException();        
        }

    @Override
    public void drop(JdbcColumn column) throws ProtectionException
        {
        throw new NotImplementedException();        
        }

    protected void execute(final String statement)
    throws ProtectionException
        {
        try {
            log.debug("SQL statement [{}]", statement);
            final int result = this.connection.open().createStatement().executeUpdate(
                statement.toString()
                );
            log.debug("SQL result [{}]", result);

// BUG
// Failed create can just return zero and no exception.            
        
            }
        catch (final SQLException ouch)
            {
            log.warn("SQL Exception [{}]", ouch.getMessage());
            log.warn("SQL Statement [{}]", statement);
            }
        finally {
            connection.close();
            }
        }

    protected void sqltype(final StringBuilder builder, final JdbcColumn.Metadata.Jdbc meta)
    throws ProtectionException
        {
        final StringBuilder tempbuilder = new StringBuilder(); 
        log.debug("sqltype()");
        log.debug("  name [{}]", meta.name());
        log.debug("  type [{}]", meta.jdbctype().name());
        log.debug("  size [{}]", meta.arraysize());
        if (meta.jdbctype().isarray())
            {
            switch(meta.jdbctype())
                {
                case CHAR:
                case NCHAR:  
                case VARCHAR:    
                case NVARCHAR: 
                    if ((meta.arraysize() != null) && (meta.arraysize() > 0))
                        {
                        tempbuilder.append(meta.jdbctype().name());
                        tempbuilder.append("(");
                        tempbuilder.append(meta.arraysize());
                        tempbuilder.append(")");
                        }
                    break;

                default :
                    if ((meta.arraysize() == null) || (meta.arraysize() == AdqlColumn.VAR_ARRAY_SIZE))
                        {
                        tempbuilder.append("(*)");
                        }
                    else {
                        tempbuilder.append("(");
                        tempbuilder.append(
                            meta.arraysize()
                            );
                        tempbuilder.append(")");
                        }
                    break;

                }
            }
        else {
            switch(meta.jdbctype())
                {
                case BOOLEAN:
                    tempbuilder.append(
                        "BIT"
                        );
                    break ;

                case DATE :
                case TIME :
                case TIMESTAMP :
                    tempbuilder.append(
                        "DATETIME"
                        );
                    break ;

                case REAL:
                    tempbuilder.append(
                        "REAL"
                        );
                    break ;

                case DOUBLE:
                    tempbuilder.append(
                        "FLOAT"
                        );
                    break ;

                default :
                    tempbuilder.append(
                        meta.jdbctype().name()
                        );
                    break ;
                }
            }
        log.debug("  temp [{}]", tempbuilder.toString());
        builder.append(tempbuilder);
        }
    
    protected void fullname(final StringBuilder builder, final JdbcSchema schema)
    throws ProtectionException
        {
        /*
        sqlname(
            builder,
            schema.catalog()
            );  
        builder.append(".");
        */
        sqlname(
            builder,
            schema.schema()
            );
        }
    
    protected void fullname(final StringBuilder builder, final JdbcTable table)
    throws ProtectionException
        {
        fullname(
            builder,
            table.schema()
            );  
        builder.append(".");
        sqlname(
            builder,
            table
            );
        }
    
    protected void sqlname(final StringBuilder builder, final JdbcSchema schema)
        {
        sqlname(
            builder,
            schema.name()
            );
        }

    protected void sqlname(final StringBuilder builder, final JdbcTable table)
        {
        sqlname(
            builder,
            table.name()
            );
        }

    protected void sqlname(final StringBuilder builder, final JdbcColumn column)
        {
        sqlname(
            builder,
            column.name()
            );
        }

    protected void sqlname(final StringBuilder builder, final String name)
        {
        /*
         * 
        builder.append("\"");
        builder.append(
            name.replace("\"", "\\\"")
            );
        builder.append("\"");
         * 
         */
        builder.append(name);
        }
    }
