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
import net.sourceforge.jtds.jdbc.Driver;
import uk.ac.roe.wfau.firethorn.access.ProtectionException;
import uk.ac.roe.wfau.firethorn.entity.annotation.CreateMethod;
import uk.ac.roe.wfau.firethorn.entity.annotation.DeleteMethod;
import uk.ac.roe.wfau.firethorn.entity.annotation.UpdateMethod;
import uk.ac.roe.wfau.firethorn.exception.NotImplementedException;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlColumn;
import uk.ac.roe.wfau.firethorn.meta.jdbc.JdbcColumn;
import uk.ac.roe.wfau.firethorn.meta.jdbc.JdbcConnector;
import uk.ac.roe.wfau.firethorn.meta.jdbc.JdbcOperator;
import uk.ac.roe.wfau.firethorn.meta.jdbc.JdbcSchema;
import uk.ac.roe.wfau.firethorn.meta.jdbc.JdbcTable;

/**
 * Operations for a SQLServer database.
 *
 */
@Slf4j
public class SQLServerOperator
implements JdbcOperator 
    {
    /*
     * 
     */
    @Override
    public Driver driver()
        {
        return new net.sourceforge.jtds.jdbc.Driver();
        }

    @Override
    public String url()
        {
        //jdbc:jtds:sqlserver://{host}:{port}/${database}
        final StringBuilder builder = new StringBuilder();
        builder.append("jdbc:jtds:sqlserver://");
        builder.append(this.connector.host());
        if (this.connector.port() != null)
            {
            builder.append(":");
            builder.append(this.connector.port());
            }
        builder.append("/");
        builder.append(this.connector.database());
        return builder.toString();
        }

    /**
     * Public constructor.
     * 
     */
    public SQLServerOperator(final JdbcConnector connector)
        {
        super();
        this.connector = connector ;
        }

    /**
     * Our parent {@link JdbcConnector}.
     *  
     */
    private JdbcConnector connector;

    /**
     * Our parent {@link JdbcConnector}.
     *  
     */
    public JdbcConnector connector()
        {
        return this.connector;
        }

    /**
     * Maximum size for a string, {@value}.
     * 
     */
	static final int MAX_CHAR_SIZE = 1024;

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
    throws ProtectionException
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
    @DeleteMethod
    public void drop(final JdbcTable table)
    throws ProtectionException
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
			statement.toString()
			);
        }
    
    @Override
    @UpdateMethod
    public void delete(final JdbcTable table)
    throws ProtectionException
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
			statement.toString()
			);
        }
    
    @UpdateMethod
    public void truncate(final JdbcTable table)
    throws ProtectionException
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
			statement.toString()
			);
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

    protected void execute(final String statement)
    throws ProtectionException
        {
        try {
            log.debug("SQL statement [{}]", statement);
            final int result = this.connector.open().createStatement().executeUpdate(
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
            connector.close();
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
                    if ((meta.arraysize() != null) && (meta.arraysize() > 0) && (meta.arraysize() < MAX_CHAR_SIZE))
                        {
                        tempbuilder.append(meta.jdbctype().name());
                        tempbuilder.append("(");
                        tempbuilder.append(meta.arraysize());
                        tempbuilder.append(")");
                        }
                    else {
                        tempbuilder.append("VARCHAR");
                        tempbuilder.append("(MAX)");
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

    @Override
    public String fullname(final JdbcSchema schema)
    throws ProtectionException
        {
        final StringBuilder builder = new StringBuilder();
        fullname(builder, schema);
        return builder.toString();
        }

    @Override
    public String fullname(final JdbcTable table)
    throws ProtectionException
        {
        final StringBuilder builder = new StringBuilder();
        fullname(builder, table);
        return builder.toString();
        }

    @Override
    public String fullname(final JdbcColumn column)
    throws ProtectionException
        {
        final StringBuilder builder = new StringBuilder();
        sqlname(builder, column);
        return builder.toString();
        }
    
    public StringBuilder fullname(final StringBuilder builder, final JdbcSchema schema)
    throws ProtectionException
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
		return builder;
    	}

    public StringBuilder fullname(final StringBuilder builder, final JdbcTable table)
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
        return builder;
    	}
    
    protected StringBuilder sqlname(final StringBuilder builder, final JdbcSchema schema)
    	{
        return sqlname(
			builder,
			schema.name()
			);
    	}

    protected StringBuilder sqlname(final StringBuilder builder, final JdbcTable table)
    	{
        return sqlname(
			builder,
			table.name()
			);
    	}

    protected StringBuilder sqlname(final StringBuilder builder, final JdbcColumn column)
    	{
    	return sqlname(
			builder,
			column.name()
			);
    	}

    protected StringBuilder sqlname(final StringBuilder builder, final String name)
    	{
    	builder.append("[");
    	builder.append(
			name.replace("]", "]]")
			);
    	builder.append("]");
        return builder;
    	}
    
    protected void sqlchar()
    	{
    	
    	}
    }
