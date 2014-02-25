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
 * Spring Controller to generate response for a table.
 * 
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
         *
         */
        public String format(final ResultSet results)
        throws SQLException;
        
        public String index();
        
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
     * A simple formatter for objects.
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
            return results.getObject(
               index()
                ).toString();
            }
        
      
        }

    /*
     * TODO refactor this to use our AdqlParser classes. 
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
            log.debug("Column [{}][{}]", column.ident(), column.name());
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

    public void select(final StringBuilder builder, final BaseColumn<?> column, final JdbcProductType type)
        {
    	log.debug("select(StringBuilder, AdqlColumn, JdbcProductType)");
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
     * Generate the table header.
     * 
     */
    public abstract void head(final PrintWriter writer, final BaseTable<?,?> table);
        
    /**
     * Generate the field metadata for a column.
     * 
    public abstract void field(final PrintWriter writer, final BaseColumn<?> column);
     */

    /**
     * Generate the table rows.
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
     * Generate a table row.
     * 
     */
    public abstract void row(final List<FieldFormatter> formatters, final PrintWriter writer, final ResultSet results)
    throws SQLException;

    /**
     * Generate the table cells.
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
     * Generate a table cell.
     * 
     */
    public abstract void cell(final FieldFormatter formatter, final PrintWriter writer, final ResultSet results)
    throws SQLException;
    
    /**
     * Generate the table footer.
     * 
     */
    public abstract void foot(final PrintWriter writer, final BaseTable<?,?> table);

    /**
     * Select a formatter for a field.
     * 
     */
    public abstract FieldFormatter formatter(final BaseColumn<?> column);

    /**
     * Generate the table body.
     * 
     */
    public void body(final PrintWriter writer, final BaseTable<?,?> base)
        {
        //
        // If the root table is a JDBC table.
        if (base.root() instanceof JdbcTable)
            {
            final JdbcTable jdbc = (JdbcTable) base.root();

            final JdbcProductType type  = jdbc.resource().connection().type();
            final Connection connection = jdbc.resource().connection().open();

            int isolation = Connection.TRANSACTION_NONE;

            try {
                //
                // Use isolation level to peek at live data.
                // http://redmine.roe.ac.uk/issues/324
                // http://www.precisejava.com/javaperf/j2ee/JDBC.htm#JDBC107
                isolation = connection.getTransactionIsolation();
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
                        base,
                        type
                        )
                    );

                //final ResultSetMetaData colmeta = results.getMetaData();
                //final int colcount = colmeta.getColumnCount();

                final List<FieldFormatter> formatters = new ArrayList<FieldFormatter>();
                for (final BaseColumn<?> column : base.columns().select())
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
                        connection.setTransactionIsolation(
                            isolation
                            );
                        }
                    catch (final SQLException ouch)
                        {
                        log.error("Exception closing SQL connection [{}]", ouch.getMessage());
                        }
                    }
                }
            }
        else {
            log.error("Unable to process root table type [{}]", base.root().getClass().getName());
            }
        }
    
    /**
     * Generate the table.
     *
     */
    public void table(final PrintWriter writer, final BaseTable<?,?> table) 
        {
        head(writer, table);
        body(writer, table);
        foot(writer, table);
        }
    }
