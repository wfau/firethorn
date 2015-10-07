/*
 *  Copyright (C) 2012 Royal Observatory, University of Edinburgh, UK
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
package uk.ac.roe.wfau.firethorn.webapp.votable;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import lombok.extern.slf4j.Slf4j;
import uk.ac.roe.wfau.firethorn.meta.base.BaseColumn;
import uk.ac.roe.wfau.firethorn.meta.base.BaseTable;
import uk.ac.roe.wfau.firethorn.meta.jdbc.JdbcProductType;
import uk.ac.roe.wfau.firethorn.meta.jdbc.JdbcTable;
import uk.ac.roe.wfau.firethorn.webapp.control.AbstractController;

/**
 * Abstract base class for a Spring MVC Controller to format table data.
 *
 */
@Slf4j
public abstract class AbstractTableController
extends AbstractController
    {
    /**
     * Public constructor.
     *
     */
    public AbstractTableController()
        {
        super();
        }

    /**
     * Public interface for a field formatter.
     *
     */
    public interface FieldFormatter
        {
        /**
         * Format the field as a String.
         * @return The field formatted as a String
         *
         */
        public String format(final ResultSet results)
        throws SQLException;

        /**
         * The field index in a JDBC ResultSet.
         * @return The field index.
         * @see ResultSet#getObject(String)
         *
         */
        public String index();

        /**
         * The ADQL field name.
         * @return The ADQL field name.
         * @throws SQLException
         *
         */
        public String name()
        throws SQLException;
        }
   
    /**
     * Abstract base class for a FieldFormatter.
     *
     */
    public static abstract class AbstractFormatter
    implements FieldFormatter
        {
        /**
         *  Protected constructor.
         *
         */
        public AbstractFormatter(final BaseColumn<?> column)
            {
            this.column  = column;
            }

        protected final BaseColumn<?> column  ;
      
        
        private String name;
        @Override
        public String name()
            {
            if (this.name == null)
                {
                this.name = this.column.name();
                }
            return this.name;
            }

        private String index;
        @Override
        public String index()
            {
            if (this.index == null)
                {
                this.index = this.column.root().name();
                }
            return this.index;
            }
        }

    /**
     * A simple formatter for generic fields, using {@link Object#toString()}.
     * This should work for numeric data like Integer, Long, Float and Double.
     * This does *NOT* apply any additional formatting or escaping of the resulting String.
     * @see Object#toString() 
     *
     */
    public static class SimpleFormatter
    extends AbstractFormatter
        {
        public SimpleFormatter(final BaseColumn<?> column)
            {
            super(
                column
                );
            }

        @Override
        public String format(final ResultSet results)
        throws SQLException
            {
        		if (results.getObject(index())!=null){
        			return results.getObject(index()).toString();
        		} else {
        			return "";
        		}
            }
        }

    /**
     * Generate a SQL SELECT statement to get all the values from a {@link BaseTable}.  
     * @todo Refactor this to use our AdqlParser.
     * @todo Remove the {@link JdbcProductType} param.
     * 
     * @param table The {@link BaseTable} to query.
     * @param type The {@link JdbcProductType} of the database.
     * @return A {@link String} representation of the SQL {@link Statement}
     *
     */
    public String select(final BaseTable<?,?> table, final JdbcProductType type)
        {
        final StringBuilder builder = new StringBuilder();

        builder.append(
            "SELECT"
            );

        String glue = " ";
        for (final BaseColumn<?> column : table.columns().select())
            {
            log.trace("Column [{}][{}]", column.ident(), column.name());
            builder.append(
                glue
                );
            glue = ", ";
            select(
                builder,
                column,
                type
                );
            }
        builder.append(
            " FROM "
            );
        builder.append(
            table.root().namebuilder()
            );
        /*
         * Only if we have added a rownum column.
        builder.append(
            " ORDER BY 1"
            );
         */
        log.debug("VOTABLE SQL [{}]", builder.toString());
        return builder.toString();
        }

    /**
     * Add the column name for a {@link BaseColumn}.  
     * @todo Refactor this to use our AdqlParser.
     * @todo Remove the {@link JdbcProductType} param.
     * 
     * @param builder The {@link StringBuilder} to add the column name to.
     * @param column The {@link BaseColumn} to query.
     * @param type The {@link JdbcProductType} of the database.
     * 
     */
    public void select(final StringBuilder builder, final BaseColumn<?> column, final JdbcProductType type)
        {
    	log.trace("select(StringBuilder, AdqlColumn, JdbcProductType)");
        //
        // Postgresql dialect
        if (type == JdbcProductType.PGSQL)
            {
            builder.append('"');
            builder.append(
                column.root().name()
                );
            builder.append('"');
            }
        //
        // SQLServer dialect
        // http://technet.microsoft.com/en-us/library/ms174450.aspx
        else if (type == JdbcProductType.MSSQL)
            {
            builder.append(
                column.root().name()
                );
            }
        //
        // Generic SQL dialect
        else {
            builder.append(
                column.root().name()
                );
            }
        builder.append(
            " AS "
            );
        builder.append(
            column.name()
            );
        }

    /**
     * Write the header for a {@link BaseTable} to a {@link PrintWriter}.
     * @param writer The {@link PrintWriter} to write the data to.
     * @param table  The {@link BaseTable}.
     *
     */
    public abstract void head(final PrintWriter writer, final BaseTable<?,?> table);
        
    /**
     * Write all the rows from a {@link ResultSet} to a {@link PrintWriter} using a {@link List} of {@link FieldFormatter}s.
     * This will call {@link ResultSet#next()} to move the {@link ResultSet} cursor to the next row.
     * @param formatters The {@link List} of {@link FieldFormatter}s to use.
     * @param writer     The {@link PrintWriter} to write the field to.
     * @param results    The {@link ResultSet} to get the row from.
     *
     */
    public void rows(final List<FieldFormatter> formatters, final PrintWriter writer, final ResultSet results)
    throws SQLException
        {
        while (results.next())
            {
            row(
                formatters,
                writer,
                results
                );
            }
        }

    /**
     * Write a row from a {@link ResultSet} to a {@link PrintWriter} using a {@link List} of {@link FieldFormatter}s.
     * @param formatters The {@link List} of {@link FieldFormatter}s to use.
     * @param writer     The {@link PrintWriter} to write the field to.
     * @param results    The {@link ResultSet} to get the row from.
     *
     */
    public abstract void row(final List<FieldFormatter> formatters, final PrintWriter writer, final ResultSet results)
    throws SQLException;

    /**
     * Write all the fields from a {@link ResultSet} to a {@link PrintWriter} using a {@link List} of {@link FieldFormatter}s.
     * @param formatters The {@link List} of {@link FieldFormatter}s to use.
     * @param writer     The {@link PrintWriter} to write the field to.
     * @param results    The {@link ResultSet} to get the fields from.
     *
     */
    public void cells(final List<FieldFormatter> formatters, final PrintWriter writer, final ResultSet results)
    throws SQLException
        {
        for (final FieldFormatter formatter : formatters)
            {
            cell(
                formatter,
                writer,
                results
                );
            }
        }

    /**
     * Write a field from a {@link ResultSet} to a {@link PrintWriter} using a {@link FieldFormatter}.
     * @param formatter The {@link FieldFormatter} to use to get the field and format it.
     * @param writer    The {@link PrintWriter} to write the field to.
     * @param results   The {@link ResultSet} to get the field from.
     *
     */
    public abstract void cell(final FieldFormatter formatter, final PrintWriter writer, final ResultSet results)
    throws SQLException;
    
    /**
     * Write the footer for a {@link BaseTable} to a {@link PrintWriter}.
     * @param writer The {@link PrintWriter} to write the data to.
     * @param table  The {@link BaseTable}.
     *
     */
    public abstract void foot(final PrintWriter writer, final BaseTable<?,?> table);

    /**
     * Select a matching {@link FieldFormatter} for a {@link BaseColumn}.
     * @param column The {@link BaseColumn} to check for.
     * @return The corresponding {@link FieldFormatter}.
     * 
     */
    public abstract FieldFormatter formatter(final BaseColumn<?> column);

    /**
     * Write the body for a {@link BaseTable} to a {@link PrintWriter}.
     * @param writer The {@link PrintWriter} to write the data to.
     * @param table  The {@link BaseTable}.
     *
     */
    public void body(final PrintWriter writer, final BaseTable<?,?> table)
        {
        //
        // If the root table is a JDBC table.
        if (table.root() instanceof JdbcTable)
            {
            final JdbcTable jdbc = (JdbcTable) table.root();

            final JdbcProductType type  = jdbc.resource().connection().type();
            final Connection connection = jdbc.resource().connection().open();

            // int isolation = Connection.TRANSACTION_NONE;

            try {
                //
                // Use isolation level to peek at live data.
                // http://redmine.roe.ac.uk/issues/324
                // http://www.precisejava.com/javaperf/j2ee/JDBC.htm#JDBC107
                // isolation = connection.getTransactionIsolation();
                connection.setTransactionIsolation(
                    Connection.TRANSACTION_READ_UNCOMMITTED
                    );
                //
                // Use statement params to prevent buffering of large data.
                // http://stackoverflow.com/questions/858836/does-a-resultset-load-all-data-into-memory-or-only-when-requested
                final Statement statement = connection.createStatement(
                    ResultSet.TYPE_FORWARD_ONLY,
                    ResultSet.CONCUR_READ_ONLY
                    );
                statement.setFetchSize(
                    100
                    );
                final ResultSet results = statement.executeQuery(
                    select(
                        table,
                        type
                        )
                    );

                //final ResultSetMetaData colmeta = results.getMetaData();
                //final int colcount = colmeta.getColumnCount();

                final List<FieldFormatter> formatters = new ArrayList<FieldFormatter>();
                for (final BaseColumn<?> column : table.columns().select())
                    {
                    formatters.add(
                        formatter(
                            column
                            )
                        );
                    }

                rows(
                    formatters,
                    writer,
                    results
                    );
                }

            catch (final SQLException ouch)
                {
                log.error("Exception reading SQL results [{}]", ouch.getMessage());
                }
            catch (final Exception ouch)
                {
                log.error("Exception writing VOTable output [{}]", ouch.getMessage());
                }
            finally {
                if (connection != null)
                    {
                    try {
                        connection.close();
/*
                        connection.setTransactionIsolation(
                            isolation
                            );
 */
                        }
                    catch (final SQLException ouch)
                        {
                        log.error("Exception closing SQL connection [{}]", ouch.getMessage());
                        }
                    }
                }
            }
        else {
            log.error("Unable to process root table type [{}]", table.root().getClass().getName());
            }
        }
    
    /**
     * Write the contents of a {@link BaseTable} to a {@link PrintWriter}.
     * @param writer The {@link PrintWriter} to write the data to.
     * @param table  The {@link BaseTable}.
     *
     */
    public void write(final PrintWriter writer, final BaseTable<?,?> table) 
        {
        head(writer, table);
        body(writer, table);
        foot(writer, table);
        }
    }
