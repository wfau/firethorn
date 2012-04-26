/**
 * Copyright (c) 2012, ROE (http://www.roe.ac.uk/)
 * All rights reserved.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package uk.ac.roe.wfau.firethorn.ogsadai.activity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.ArrayList;

import java.sql.Connection;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import uk.org.ogsadai.exception.ErrorID;

import uk.org.ogsadai.activity.ActivityProcessingException;
import uk.org.ogsadai.activity.ActivityTerminatedException;
import uk.org.ogsadai.activity.ActivityUserException;
import uk.org.ogsadai.activity.IterativeActivity;
import uk.org.ogsadai.activity.MatchedIterativeActivity;

import uk.org.ogsadai.activity.io.ActivityInput;
import uk.org.ogsadai.activity.io.ActivityPipeProcessingException;
import uk.org.ogsadai.activity.io.BlockWriter;
import uk.org.ogsadai.activity.io.ControlBlock;
import uk.org.ogsadai.activity.io.PipeClosedException;
import uk.org.ogsadai.activity.io.PipeIOException;
import uk.org.ogsadai.activity.io.PipeTerminatedException;
import uk.org.ogsadai.activity.io.TupleListActivityInput;
import uk.org.ogsadai.activity.io.TupleListIterator;
import uk.org.ogsadai.activity.io.TypedActivityInput;

import uk.org.ogsadai.activity.sql.ActivitySQLException;
import uk.org.ogsadai.activity.sql.ActivitySQLUserException;

import uk.org.ogsadai.activity.extension.ResourceActivity;

import uk.org.ogsadai.tuple.Tuple;
import uk.org.ogsadai.tuple.TupleTypes;
import uk.org.ogsadai.tuple.TupleMetadata;
import uk.org.ogsadai.tuple.SimpleTuple;
import uk.org.ogsadai.tuple.SimpleTupleMetadata;
import uk.org.ogsadai.tuple.ColumnMetadata;
import uk.org.ogsadai.tuple.SimpleColumnMetadata;
import uk.org.ogsadai.tuple.ColumnNotFoundException;

import uk.org.ogsadai.resource.ResourceAccessor;
import uk.org.ogsadai.resource.dataresource.jdbc.JDBCConnectionProvider;
import uk.org.ogsadai.resource.dataresource.jdbc.JDBCConnectionUseException;

import uk.org.ogsadai.metadata.MetadataWrapper;


/**
 *
 *
 */
public class CreateTableActivity
extends MatchedIterativeActivity
//extends IterativeActivity
implements ResourceActivity
    {

    /**
     * Our debug logger.
     * 
     */
    private static Logger logger = LoggerFactory.getLogger(
        CreateTableActivity.class
        );

    /**
     * Public constructor.
     *
     */
    public CreateTableActivity()
        {
        super();
        logger.debug("Constructor()");
        }

    /**
     * Get a list of the ActivityInputs.
     *
     */
    public ActivityInput[] getIterationInputs()
        {
        logger.debug("getIterationInputs()");
        return new ActivityInput[] {
            new TypedActivityInput(
                "table",
                String.class
                ),
            new TupleListActivityInput(
                "tuples"
                )
            };
        }

    /**
     * Our JDBC connection provider.
     *
     */
    private JDBCConnectionProvider resource;

    /**
     *
     *
     */ 
    public Class getTargetResourceAccessorClass()
        {
        return JDBCConnectionProvider.class;
        }

    /**
     *
     *
     */ 
    public void setTargetResourceAccessor(final ResourceAccessor resource)
        {
        this.resource = (JDBCConnectionProvider) resource;
        }

    /**
     * Our database connection.
     *
     */
    private Connection connection;

    /**
     * Block writer for output.
     *
     */
    private BlockWriter writer;

    /**
     * Pre-processing.
     *
     */
    protected void preprocess()
    throws ActivitySQLException, ActivityProcessingException
        {
        logger.debug("preprocess()");
        try {
            connection = resource.getConnection();
            }
        catch (JDBCConnectionUseException ouch)
            {
            logger.warn("Exception conncting to database", ouch);
            throw new ActivitySQLException(
                ouch
                );
            }
        catch (Throwable ouch)
            {
            logger.warn("Exception conncting to database", ouch);
            throw new ActivityProcessingException(
                ouch
                );
            }

        try {
            validateOutput("result");
            writer = getOutput("result");
            }
        catch (Exception ouch)
            {
            logger.warn("Exception validating outputs", ouch);
            throw new ActivityProcessingException(
                ouch
                );
            }
        }

    /**
     * Process an iteration.
     *
     */
    protected void processIteration(Object[] iteration)
    throws ActivityProcessingException,
           ActivityTerminatedException,
           ActivityUserException
        {
        logger.debug("processIteration(Object[])");
        try {
            //
            // Get the table name.
            String name = (String) iteration[0];
            logger.debug("Table name [{}]", name);

            //
            // Get the tuple iterator.
            TupleListIterator tuples = (TupleListIterator) iteration[1];
            //
            // Get the data metadata.
            MetadataWrapper wrapper  = tuples.getMetadataWrapper();
            TupleMetadata   metadata = (TupleMetadata) wrapper.getMetadata();
 
            StringBuilder builder = new StringBuilder();
            builder.append("CREATE TABLE");
            builder.append(" ");
            builder.append(name);
            builder.append(" ");
            builder.append("(");

            //
            // Iterate the data metadata.
            for (int index = 0 ; index < metadata.getColumnCount() ; index++)
                {
                ColumnMetadata column = metadata.getColumnMetadata(index);
                logger.debug("ColumnMetadata");
                logger.debug("Name  [{}]", column.getName());
                logger.debug("Type  [{}]", column.getType());
                logger.debug("Type  [{}]", TupleTypes.getTypeName(column.getType()));
                logger.debug("Table [{}]", column.getTableName());

                if (index > 0)
                    {
                    builder.append(", ");
                    }
                builder.append(column.getName());
                builder.append(" ");
                builder.append(sqltype(column.getType()));

                }
 
            builder.append(")");
            logger.debug("SQL [{}]", builder.toString()); 

            //
            // Commit the database transaction.

            logger.debug("Creating table");
            Statement statement = connection.createStatement();
            int result = statement.executeUpdate(
                builder.toString()
                );
            logger.debug("SQL statement result [{}]", result);
            connection.commit();

            //
            // Write the LIST_BEGIN marker and metadata
            writer.write(ControlBlock.LIST_BEGIN);
            writer.write(wrapper);
            
            //
            // Process the tuples (pass through).
            logger.debug("Processing tuples");
            Tuple tuple ;
            long count ;
            for(count = 0 ; ((tuple = (Tuple) tuples.nextValue()) != null) ; count++)
                {
                //
                // Scan for nulls ?

                //
                // Write the new tuple to our output.
                writer.write(
                    tuple
                    );
                }
            logger.debug("Processing done [{}] tuples", count);

            //
            // Write the list end marker
            writer.write(ControlBlock.LIST_END);

            }
/*
        catch (SQLException ouch)
            {
            logger.warn("Exception during processing", ouch);
            rollback();
            throw new ActivitySQLUserException(
                ouch
                );
            }
 */
        catch (Throwable ouch)
            {
            logger.warn("Exception during processing", ouch);
            rollback();
            throw new ActivityProcessingException(
                ouch
                );
            }
        }

    /**
     * Post-processing.
     *
     */
    protected void postprocess()
        {
        logger.debug("postprocess()");
        }

    /**
     * Rollback the transaction.
     *
     */
    private void rollback()
        {
        logger.debug("rollback()");
        try {
            connection.rollback();
            } 
        catch (Exception ouch)
            {
            logger.warn("Exception during rollback", ouch);
            }
        }

    /**
     * Translate a tuple type into a SQL data type.
     *
     */
    public String sqltype(int type)
    throws ActivityProcessingException
        {
        switch (type) 
            {
            case TupleTypes._BOOLEAN:   
                return "boolean"; 
            case TupleTypes._FLOAT:     
                return "real"; 
            case TupleTypes._INT:       
                return "integer"; 
            case TupleTypes._LONG:      
                return "bigint"; 
            case TupleTypes._STRING:    
                return "text"; 
            case TupleTypes._CHAR:      
                return "char(1)"; 
            case TupleTypes._DOUBLE:    
                return "double precision"; 
            case TupleTypes._SHORT:     
                return "smallint"; 
            case TupleTypes._DATE:      
                return "date"; 
            case TupleTypes._TIME:      
                return "time"; 
            case TupleTypes._TIMESTAMP: 
                return "timestamp"; 

            case TupleTypes._BYTEARRAY: 
            case TupleTypes._ODBLOB:    
                return "bytea"; 
            case TupleTypes._ODCLOB:    
                return "bytea"; 

            case TupleTypes._BIGDECIMAL:
                return "decimal"; 

            case TupleTypes._FILE:      
            case TupleTypes._ODNULL:    
            default: 
                logger.warn("Unexpected tuple type [{}]", TupleTypes.getTypeName(type));
                throw new ActivityProcessingException(
                    ErrorID.INVALID_INPUT_TYPE_EXCEPTION
                    );
            }
        }
    }

