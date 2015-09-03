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

import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Component;

import uk.ac.roe.wfau.firethorn.entity.annotation.CreateMethod;
import uk.ac.roe.wfau.firethorn.entity.annotation.DeleteMethod;
import uk.ac.roe.wfau.firethorn.entity.annotation.UpdateMethod;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlColumn;
import uk.ac.roe.wfau.firethorn.meta.jdbc.JdbcColumn;
import uk.ac.roe.wfau.firethorn.meta.jdbc.JdbcConnector;
import uk.ac.roe.wfau.firethorn.meta.jdbc.JdbcResource;
import uk.ac.roe.wfau.firethorn.meta.jdbc.JdbcSchema;
import uk.ac.roe.wfau.firethorn.meta.jdbc.JdbcTable;

/**
 * SQLServer JdbcTable factory implementation.
 *
 */
@Slf4j
@Component
public class SQLServerDriver
implements JdbcResource.JdbcDriver 
    {
    /**
     * SQL statement to CREATE a schema.
     *
     */
    protected static final String CREATE_SCHEMA_STATEMENT = "CREATE SCHEMA {name}" ;

	@Override
	public void create(JdbcSchema schema)
		{
		// TODO Auto-generated method stub
		}

    /**
     * SQL statement to DROP a schema.
     *
     */
    protected static final String DROP_SCHEMA_STATEMENT = "DROP SCHEMA {name}" ;


	@Override
	public void drop(JdbcSchema schema)
		{
		// TODO Auto-generated method stub
		}
   
    @Override
    @CreateMethod
    public void create(final JdbcTable table)
        {
        log.debug("Create JdbcTable [{}]", table.name());

        final StringBuilder builder = new StringBuilder();
        builder.append("CREATE TABLE ");
		fullname(
			builder,
			table
			);
        builder.append("(");

        boolean comma = false ;
        for(JdbcColumn column : table.columns().select())
        	{
        	if (comma)
        		{
                builder.append(",");
        		}
        	else {
            	comma = true ;
        		}
        	sqlname(
    			builder,
    			column
    			);
            builder.append(" ");
            sqltype(
				builder,
				column.meta().jdbc()
    			);
        	}

        builder.append(")");
        }

    protected void sqltype(final StringBuilder builder, final JdbcColumn.Metadata.Jdbc meta)
    	{
    	switch(meta.jdbctype())
	        {
	        case DATE :
	        case TIME :
	        case TIMESTAMP :
	            builder.append(
	                "DATETIME"
	                );
	            break ;
	        default :
	            builder.append(
	                meta.jdbctype().name()
	                );
	            break ;
	        }

    	// TODO This should check for char() rather than array()
    	if (meta.jdbctype().isarray())
    	    {
    	    if (meta.arraysize() == AdqlColumn.VAR_ARRAY_SIZE)
    	        {
    	        builder.append("(*)");
    	        }
    	    else {
    	        builder.append("(");
    	        builder.append(
    	            meta.arraysize()
    	            );
    	        builder.append(")");
    	        }
    	    }
    	}

    
    
    
    protected void fullname(final StringBuilder builder, final JdbcSchema schema)
    	{
		sqlname(
			builder,
			schema.catalog()
			);  
    	builder.append(".");
		sqlname(
			builder,
			schema.schema()
			);
    	}

    protected void fullname(final StringBuilder builder, final JdbcTable table)
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
    	builder.append("[");
    	builder.append(
			name.replace("]", "]]")
			);
    	builder.append("]");
    	}
    
    /**
     * SQL statement to DROP a table.
     *
     */
    protected static final String DROP_TABLE_STATEMENT = "DROP TABLE {name}" ;

    @Override
    @DeleteMethod
    public void drop(final JdbcTable table)
        {
        log.debug("Drop JdbcTable [{}]", table.name());
        final JdbcConnector connection = table.resource().connection();
        final String statement = DROP_TABLE_STATEMENT.replace(
            "{name}",
            table.namebuilder().toString()
            );
        try {
            log.debug("Executing SQL [{}]", statement);
            final int result = connection.open().createStatement().executeUpdate(
                statement
                );
            log.debug("result [{}]", result);
            }
        catch (final SQLException ouch)
            {
            log.warn("SQLException while attempting to drop table [{}]", ouch.getMessage());
            log.warn("SQL statement [{}]", statement);
            }
        finally {
            connection.close();
            }
        }
    
    /**
     * SQL statement to DELETE all data from a table.
     *
     */
    protected static final String DELETE_DATA_STATEMENT = "DELETE FROM {name}" ;

    @Override
    @UpdateMethod
    public void delete(final JdbcTable table)
        {
        log.debug("Delete JdbcTable [{}]", table.name());
        final JdbcConnector connection = table.resource().connection();
        final String statement = DELETE_DATA_STATEMENT.replace(
            "{name}",
            table.namebuilder().toString()
            );
        try {
            log.debug("Executing SQL [{}]", statement);
            final int result = connection.open().createStatement().executeUpdate(
                statement
                );
            log.debug("result [{}]", result);
            }
        catch (final SQLException ouch)
            {
            log.warn("SQLException while attempting to delete table data [{}]", ouch.getMessage());
            log.warn("SQL statement [{}]", statement);
            }
        finally {
            connection.close();
            }
        }

    /**
     * SQL statement to TRUNCATE all data in a table.
     *
     */
    protected static final String TRUNCATE_DATA_STATEMENT = "TRUNCATE {name}" ;

    /**
     * SQL statement to CREATE a column.
     *
     */
    protected static final String CREATE_COLUMN_STATEMENT = "----" ;
    
	@Override
	public void create(JdbcColumn column)
		{
		// TODO Auto-generated method stub
		
		}

    /**
     * SQL statement to DROP a column.
     *
     */
    protected static final String DROP_COLUMN_STATEMENT = "----" ;

	@Override
	public void drop(JdbcColumn column)
		{
		// TODO Auto-generated method stub
		
		}
    
    }
