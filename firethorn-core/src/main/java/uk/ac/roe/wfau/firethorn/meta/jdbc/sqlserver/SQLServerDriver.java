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
import uk.ac.roe.wfau.firethorn.exception.NotImplementedException;
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
	@Override
	public void create(JdbcSchema schema)
		{
		throw new NotImplementedException();		
		}

	@Override
	public void drop(JdbcSchema schema)
		{
		throw new NotImplementedException();		
		}
   
    @Override
    @CreateMethod
    public void create(final JdbcTable table)
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
			table.resource().connection(),
			statement.toString()
			);
        }
    
    @Override
    @DeleteMethod
    public void drop(final JdbcTable table)
        {
        log.debug("Drop JdbcTable [{}]", table.name());
        final StringBuilder statement = new StringBuilder(
    		"DROP TABLE "
    		); 
		fullname(
			statement,
			table
			);
		execute(
			table.resource().connection(),
			statement.toString()
			);
        }
    
    @Override
    @UpdateMethod
    public void delete(final JdbcTable table)
        {
        log.debug("Delete JdbcTable [{}]", table.name());
        final StringBuilder statement = new StringBuilder(
    		"DELETE FROM "
    		); 
		fullname(
			statement,
			table
			);
		execute(
			table.resource().connection(),
			statement.toString()
			);
        }
    
    @UpdateMethod
    public void truncate(final JdbcTable table)
        {
        log.debug("Truncate JdbcTable [{}]", table.name());
        final StringBuilder statement = new StringBuilder(
    		"TRUNCATE "
    		); 
		fullname(
			statement,
			table
			);
		execute(
			table.resource().connection(),
			statement.toString()
			);
        }

    protected void execute(final JdbcConnector connection, final String statement)
        {
        try {
            log.debug("SQL statement [{}]", statement);
            final int result = connection.open().createStatement().executeUpdate(
                statement.toString()
                );
            log.debug("SQL result [{}]", result);
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

	@Override
	public void create(JdbcColumn column)
		{
		throw new NotImplementedException();		
		}

	@Override
	public void drop(JdbcColumn column)
		{
		throw new NotImplementedException();		
		}

    protected void sqltype(final StringBuilder builder, final JdbcColumn.Metadata.Jdbc meta)
    	{
    	switch(meta.jdbctype())
	        {
	        case DATE :
	        case TIME :
	        case TIMESTAMP :
	            builder.append(
	                "datetime"
	                );
	            break ;
	        case REAL:
	        	builder.append(
	                "real"
	                );
	            break ;
	        case DOUBLE:
	        	builder.append(
	                "float"
	                );
	            break ;
	        default :
	            builder.append(
	                meta.jdbctype().name()
	                );
	            break ;
	        }


    	// TODO This should check for char() rather than array()
    	if (meta.jdbctype().isarray() || meta.jdbctype().equals(AdqlColumn.AdqlType.CHAR) || meta.jdbctype().equals(JdbcColumn.JdbcType.CHAR) )
    	    {
    	    if (meta.arraysize() == AdqlColumn.VAR_ARRAY_SIZE)
    	        {
    	        builder.append("(*)");
    	        }
    	    else {
    	    	if (meta.arraysize()==null){
    	    		 builder.append("(*)");
    	    	} else {
	    	        builder.append("(");
	    	        builder.append(
	    	            meta.arraysize()
	    	            );
	    	        builder.append(")");
	    	        }
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
    }
