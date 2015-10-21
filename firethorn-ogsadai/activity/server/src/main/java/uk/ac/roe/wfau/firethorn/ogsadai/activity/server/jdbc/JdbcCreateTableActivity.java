/**
 * Copyright (c) 2013, ROE (http://www.roe.ac.uk/)
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
package uk.ac.roe.wfau.firethorn.ogsadai.activity.server.jdbc;

import java.sql.Connection;
import java.sql.Statement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.ac.roe.wfau.firethorn.ogsadai.activity.common.jdbc.JdbcCreateTableParam;
import uk.org.ogsadai.activity.ActivityProcessingException;
import uk.org.ogsadai.activity.ActivityTerminatedException;
import uk.org.ogsadai.activity.ActivityUserException;
import uk.org.ogsadai.activity.MatchedIterativeActivity;
import uk.org.ogsadai.activity.extension.ResourceActivity;
import uk.org.ogsadai.activity.io.ActivityInput;
import uk.org.ogsadai.activity.io.BlockWriter;
import uk.org.ogsadai.activity.io.ControlBlock;
import uk.org.ogsadai.activity.io.TupleListActivityInput;
import uk.org.ogsadai.activity.io.TupleListIterator;
import uk.org.ogsadai.activity.io.TypedActivityInput;
import uk.org.ogsadai.activity.sql.ActivitySQLException;
import uk.org.ogsadai.exception.ErrorID;
import uk.org.ogsadai.metadata.MetadataWrapper;
import uk.org.ogsadai.resource.ResourceAccessor;
import uk.org.ogsadai.resource.dataresource.jdbc.JDBCConnectionProvider;
import uk.org.ogsadai.resource.dataresource.jdbc.JDBCConnectionUseException;
import uk.org.ogsadai.tuple.ColumnMetadata;
import uk.org.ogsadai.tuple.Tuple;
import uk.org.ogsadai.tuple.TupleMetadata;
import uk.org.ogsadai.tuple.TupleTypes;

/**
 *
 *
 */
public class JdbcCreateTableActivity
extends MatchedIterativeActivity
implements ResourceActivity
    {

    /**
     * Our debug logger.
     *
     */
    private static Logger logger = LoggerFactory.getLogger(
        JdbcCreateTableActivity.class
        );

    /**
     * Public constructor.
     *
     */
    public JdbcCreateTableActivity()
        {
        super();
        }

    @Override
    public ActivityInput[] getIterationInputs()
        {
        return new ActivityInput[] {
            new TypedActivityInput(
                JdbcCreateTableParam.JDBC_CREATE_TABLE_NAME,
                String.class
                ),
            new TupleListActivityInput(
                JdbcCreateTableParam.JDBC_CREATE_TUPLE_INPUT
                )
            };
        }

    /**
     * Our target Resource accessor.
     * 
     */
    private ResourceAccessor accessor;

    /**
     * Our JDBC connection provider.
     *
     */
    private JDBCConnectionProvider provider;

    @Override
    public Class<? extends ResourceAccessor> getTargetResourceAccessorClass()
        {
        return JDBCConnectionProvider.class;
        }

    @Override
    public void setTargetResourceAccessor(final ResourceAccessor accessor)
        {
        this.accessor = accessor;
        this.provider = (JDBCConnectionProvider) accessor;
        }

    /**
     * Our database connection.
     *
     */
    private Connection connection;

    /**
     * Flag to indicate if we changed the autocommit state.
     *
     */
    private boolean autochanged = false;

    /**
     * Our results writer.
     *
     */
    private BlockWriter writer;

    @Override
    protected void preprocess()
    throws ActivitySQLException, ActivityProcessingException
        {
        try {
            connection = provider.getConnection();

            if (connection.getAutoCommit() == true)
                {
                autochanged = true ;
                connection.setAutoCommit(
                    false
                    );
                }
            }
        catch (final JDBCConnectionUseException ouch)
            {
            logger.warn("Exception connecting to database [{}][{}]", ouch.getClass().getName(), ouch.getMessage());
            throw new ActivitySQLException(
                ouch
                );
            }
        catch (final Throwable ouch)
            {
            logger.warn("Exception connecting to database [{}][{}]", ouch.getClass().getName(), ouch.getMessage());
            throw new ActivityProcessingException(
                ouch
                );
            }

        try {
            logger.debug("Validating outputs");
            validateOutput(
                JdbcCreateTableParam.ACTIVITY_RESULT
                );
            this.writer = this.getOutput(
                JdbcCreateTableParam.ACTIVITY_RESULT
                );
            }
        catch (final Exception ouch)
            {
            logger.warn("Exception validating outputs", ouch);
            throw new ActivityProcessingException(
                ouch
                );
            }
        logger.debug("---- preprocess done");
        }

    @Override
    protected void processIteration(final Object[] iteration)
    throws ActivityProcessingException, ActivityTerminatedException, ActivityUserException
        {
        try {
            //
            // Get the table name.
            final String name = (String) iteration[0];
            logger.debug("Table name [{}]", name);

            //
            // Get the tuple iterator.
            final TupleListIterator tuples = (TupleListIterator) iteration[1];
            //
            // Get the data metadata.
            final MetadataWrapper wrapper  = tuples.getMetadataWrapper();
            final TupleMetadata   metadata = (TupleMetadata) wrapper.getMetadata();

            //
            // Start the CREATE TABLE statement
            final StringBuilder builder = new StringBuilder();
            builder.append("CREATE TABLE");
            builder.append(" ");
            builder.append(name);
            builder.append(" ");
            builder.append("(");

            //
            // Iterate the data metadata.
            for (int index = 0 ; index < metadata.getColumnCount() ; index++)
                {
                final ColumnMetadata column = metadata.getColumnMetadata(index);
                logger.debug("ColumnMetadata");
                logger.debug("Name  [{}]", column.getName());
                logger.debug("Type  [{}]", column.getType());
                logger.debug("Type  [{}]", TupleTypes.getTypeName(column.getType()));
                logger.debug("Table [{}]", column.getTableName());

                if (index > 0)
                    {
                    builder.append(", ");
                    }
                builder.append("COL_" + column.getName());
                builder.append(" ");
                builder.append(sqltype(column.getType()));
                }

            builder.append(")");
            logger.debug("SQL [{}]", builder.toString());

            //
            // Execute the CREATE TABLE statement.
            logger.debug("Creating table");
            final Statement statement = connection.createStatement();
            final int result = statement.executeUpdate(
                builder.toString()
                );
            logger.debug("SQL statement result [{}]", result);
            //
            // Commit the changes.
            connection.commit();

            //
            // Write the LIST_BEGIN marker and metadata
            writer.write(ControlBlock.LIST_BEGIN);
            writer.write(wrapper);
            //
            // Process the tuples (pass through).
            logger.debug("Processing tuples ...");
            Tuple tuple ;
            long count ;
            for(count = 0 ; ((tuple = (Tuple) tuples.nextValue()) != null) ; count++)
                {
                writer.write(
                    tuple
                    );
                }
            logger.debug("Processing done [{}] tuples", count);
            //
            // Write the list end marker
            writer.write(ControlBlock.LIST_END);

            }
        catch (final Throwable ouch)
            {
            logger.warn("Exception during processing [{}][{}]", ouch.getClass().getName(), ouch.getMessage());
            rollback();
            throw new ActivityProcessingException(
                ouch
                );
            }
        }

    @Override
    protected void postprocess()
    throws ActivityProcessingException
        {
        try {
            if (autochanged)
                {
                connection.setAutoCommit(
                    true
                    );
                }
            }
        catch (final Throwable ouch)
            {
            logger.warn("Exception resetting autocommit [{}][{}]", ouch.getClass().getName(), ouch.getMessage());
            throw new ActivityProcessingException(
                ouch
                );
            }
        }

    /**
     * Rollback the transaction.
     *
     */
    private void rollback()
        {
        try {
            this.connection.rollback();
            }
        catch (final Exception ouch)
            {
            logger.warn("Exception during rollback [{}][{}]", ouch.getClass().getName(), ouch.getMessage());
            }
        }

    /**
     * Translate a tuple type into a SQL data type.
     * @todo This totally depends on the database provider.
     *
     */
    public String sqltype(final int type)
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

