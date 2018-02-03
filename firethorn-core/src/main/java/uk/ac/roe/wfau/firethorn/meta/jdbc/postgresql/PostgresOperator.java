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

import org.postgresql.Driver;

import uk.ac.roe.wfau.firethorn.access.ProtectionException;
import uk.ac.roe.wfau.firethorn.exception.NotImplementedException;
import uk.ac.roe.wfau.firethorn.meta.jdbc.JdbcColumn;
import uk.ac.roe.wfau.firethorn.meta.jdbc.JdbcConnection;
import uk.ac.roe.wfau.firethorn.meta.jdbc.JdbcOperator;
import uk.ac.roe.wfau.firethorn.meta.jdbc.JdbcSchema;
import uk.ac.roe.wfau.firethorn.meta.jdbc.JdbcTable;

/**
 * 
 * 
 */
public class PostgresOperator
implements JdbcOperator
    {
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
        throw new NotImplementedException();        
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
    }
