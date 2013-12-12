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
package uk.ac.roe.wfau.firethorn.adql.parser;

import java.util.ArrayList;
import java.util.HashMap;

import uk.ac.roe.wfau.firethorn.adql.parser.AdqlParserTable.AdqlDBColumn;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlColumn;
import lombok.extern.slf4j.Slf4j;

import adql.db.DBColumn;
import adql.query.ADQLQuery;
import adql.query.ClauseSelect;
import adql.query.IdentifierField;
import adql.query.SelectAllColumns;
import adql.query.SelectItem;
import adql.query.from.ADQLTable;
import adql.query.operand.ADQLColumn;
import adql.query.operand.function.ADQLFunction;
import adql.query.operand.function.MathFunction;
import adql.query.operand.function.UserDefinedFunction;
import adql.translator.ADQLTranslator;
import adql.translator.PostgreSQLTranslator;
import adql.translator.TranslationException;

/**
 * SQLServer SQL translator.
 * @todo Remove dependency on PostgreSQLTranslator
 *
 */
@Slf4j
public class SQLServerTranslator
    extends PostgreSQLTranslator
    implements ADQLTranslator
    {

	private final String schemaName="dbo";

    /**
     *
     *
     */
    public SQLServerTranslator()
        {
        super(
            false
            );

        }

    /**
     *
     *
     */
    public SQLServerTranslator(final boolean column)
        {
        super(
            column
            );

        }

    /**
     *
     *
     */
    public SQLServerTranslator(final boolean catalog, final boolean schema, final boolean table, final boolean column)
        {
        super(
            catalog,
            schema,
            table,
            column
            );

        }

    /**
     * Get the schema name
     *
     * @return String schemaName
     */
    public String getSchemaName(){
    	return schemaName;
    }

    /**
     * Replaces the PostgreSQLTranslator method to not put LIMIT at the end.
     *
     */
    @Override
	public String translate(final ADQLQuery query)
        throws TranslationException
        {
        log.debug("translate(ADQLQuery)");
        final StringBuilder builder = new StringBuilder();
        builder.append(
            translate(
                query.getSelect()
                )
            );

        builder.append("\nFROM ").append(
            translate(
                query.getFrom()
                )
            );

        if (!query.getWhere().isEmpty())
            {
            builder.append('\n').append(
                translate(
                    query.getWhere()
                    )
                );
            }

        if (!query.getGroupBy().isEmpty())
            {
            builder.append('\n').append(
                translate(
                    query.getGroupBy()
                    )
                );
            }

        if (!query.getHaving().isEmpty())
            {
            builder.append('\n').append(
                translate(
                    query.getHaving()
                    )
                );
            }

        if (!query.getOrderBy().isEmpty())
            {
            builder.append('\n').append(
                translate(
                    query.getOrderBy()
                    )
                );
            }
        return builder.toString();
        }


    /**
     * Replaces the PostgreSQLTranslator method to put TOP at the beginning.
     *
     */
    @Override
	public String translate(final ClauseSelect clause)
    throws TranslationException
        {
        log.debug("translate(ClauseSelect)");

        final StringBuilder builder = new StringBuilder();
        for(int i=0; (i < clause.size()); i++)
            {
            if (i == 0)
                {
                builder.append(
                    clause.getName()
                    );
                if (clause.hasLimit())
                    {
                    builder.append(" TOP ").append(
                        clause.getLimit()
                        );
                    }
                if (clause.distinctColumns())
                    {
                    builder.append(
                        " DISTINCT "
                        );
                    }
                }
            else {
                builder.append(" ").append(
                    clause.getSeparator(i)
                    );
                }
            builder.append(" ").append(
                translate(clause.get(i)
                    )
                );
            }
        return builder.toString();
        }

    /**
     * Copy of the PostgreSQLTranslator method ...
     * @todo Need to catch date fields and format them as strings.
     *
     */
    @Override
	public String translate(final SelectItem item)
    throws TranslationException
        {
        log.debug("translate(SelectItem)");
        log.debug("  item [{}][{}]", item.getName(), item.getClass().getName());
        if (item instanceof SelectAllColumns)
            {
            return translate((SelectAllColumns)item);
            }

        final StringBuffer translation = new StringBuffer(
            translate(
                item.getOperand()
                )
            );
        if (item.hasAlias())
            {
            translation.append(" AS ");
            appendIdentifier(translation, item.getAlias(), item.isCaseSensitive());
            }
        else {
            translation.append(" AS ").append(item.getName());
            }

        return translation.toString();
        }

    @Override
	public String translate(final UserDefinedFunction function)
    throws TranslationException
        {
        log.debug("translate(UserDefinedFunction)");
		return getDefaultADQLFunction(
		    function
		    );
        }


	/**
	 * Gets the default SQL output for the given ADQL function.
	 *
	 * @param function The ADQL function to format into SQL.
	 * @return The corresponding SQL.
	 * @throws TranslationException	If there is an error during the translation.
	 *
	 */
    @Override
	protected String getDefaultADQLFunction(final ADQLFunction function)
    throws TranslationException
        {
        final StringBuilder builder = new StringBuilder();
        //
        // If the function is user defined.
//ZRQ-UDF
        if (function instanceof UserDefinedFunction)
            {
            //
            // TODO Check the function schema.
            if (true)
                {
                builder.append(this.getSchemaName());
                builder.append(".");
                }
            }

        builder.append(function.getName());
        builder.append("(");

        for(int param = 0; param < function.getNbParameters(); param++)
            {
            if (param > 0)
                {
                builder.append(", ");
                }
            builder.append(
                translate(
                    function.getParameter(
                        param
                        )
                    )
                );
            }
        builder.append(")");
		return builder.toString();
        }

    /**
     * Override the PostgreSQLTranslator method ...
     *
     */
    @Override
	public String translate(final ADQLColumn column)
        throws TranslationException
        {
        log.debug("translate(ADQLColumn)");
        log.debug("  column [{}][{}]", column.getName(), column.getClass().getName());


        if (column.getDBLink() == null)
            {
            log.warn("ADQLColumn getDBLink() is NULL");
            return super.translate(
                column
                );
            }
        else if (column.getDBLink() instanceof AdqlDBColumn)
            {
            final AdqlColumn adql = ((AdqlDBColumn) column.getDBLink()).column();
            log.debug("  adql [{}][{}]", adql.name(), adql.meta().adql().type());

            return super.translate(
                column
                );

            }
        else {
            log.warn("ADQLColumn getDBLink() is unexpected class [{}]", column.getDBLink().getClass().getName());
            return super.translate(
                column
                );
            }
        }

    /*
     *
    public String translate(AdqlColumn column)
    throws TranslationException
        {
        log.debug("translate(AdqlColumn)");
        log.debug("  adql [{}][{}]", column.name(), column.getClass().getName());
        log.debug("  fullname [{}]", column.fullname());
        log.debug("  basename [{}]", column.base().fullname());
        log.debug("  rootname [{}]", column.root().fullname());
        return "";
        }
     *
     */

    /**
     * Copy of the PostgreSQLTranslator method ...
     * @todo Need to use Firethorn metadata to iterate the AdqlTable columns.
     *
     *
     */
    @Override
	public String translate(final SelectAllColumns all)
    throws TranslationException
        {
        log.debug("translate(SelectAllColumns)");

// TODO Replace this with code that uses the Firethorn metadata.
// Need to make sure this ends up with the same list of fields as the JDBC table in user data.

// Check if ADQLTable is a AdqlParserTable
// cast into AdqlParserTable and call table.table() to get a AdqlTable
// process the column list from the AdqlTable.

        final HashMap<String, String> mapAlias = new HashMap<String, String>();

        // Fetch the full list of columns to display:
        Iterable<DBColumn> dbCols = null;
        if (all.getAdqlTable() != null)
            {
            final ADQLTable table = all.getAdqlTable();
            log.debug("Table [{}][{}]", table.getName(), table.getClass().getName());
            if (table.getDBLink() != null)
                {
                dbCols = table.getDBLink();
                if (table.hasAlias())
                    {
                    final String key = appendFullDBName(new StringBuffer(), table.getDBLink()).toString();
                    mapAlias.put(key, table.isCaseSensitive(IdentifierField.ALIAS) ? ("\""+table.getAlias()+"\"") : table.getAlias());
                    }
                }
            else {
// getDBLink is null - need to fix this
// Need to find where the ADQLTable is created
// and add a reference to the BaseTable that this refers to.
                log.warn("ADQLTable with no link to the BaseTable [{}]", table.getName());
                }
            }
        else if (all.getQuery() != null)
            {
            dbCols = all.getQuery().getFrom().getDBColumns();
            final ArrayList<ADQLTable> tables = all.getQuery().getFrom().getTables();
            for(final ADQLTable table : tables)
                {
                if (table.hasAlias())
                    {
                    final String key = appendFullDBName(new StringBuffer(), table.getDBLink()).toString();
                    mapAlias.put(key, table.isCaseSensitive(IdentifierField.ALIAS) ? ("\""+table.getAlias()+"\"") : table.getAlias());
                    }
                }
            }

        // Write the DB name of all these columns:
        if (dbCols != null)
            {
            final StringBuffer cols = new StringBuffer();
            for(final DBColumn col : dbCols)
                {
                if (cols.length() > 0)
                    {
                        cols.append(',');
                        }
                if (col.getTable() != null)
                    {
                    final String fullDbName = appendFullDBName(new StringBuffer(), col.getTable()).toString();
                    if (mapAlias.containsKey(fullDbName))
                        {
                            appendIdentifier(cols, mapAlias.get(fullDbName), false).append('.');
                            }
                    else
                        {
                            cols.append(fullDbName).append('.');
                            }
                    }
                appendIdentifier(cols, col.getDBName(), IdentifierField.COLUMN);
                cols.append(" AS ").append(col.getADQLName());
                }
            return cols.toString();
            }
        else{
            return all.toADQL();
            }
        }
    
    /**
     * Replacement for the PostgreSQLTranslator method.
     *
     *
     */
    @Override
    public String translate(MathFunction funct) throws TranslationException
        {
        switch(funct.getType())
            {
            case LOG:
                return "log(" + translate(funct.getParameter(0)) + ")";

            case LOG10:
                return "log10(" + translate(funct.getParameter(0)) + ")";

            case RAND:
                return "rand(" + translate(funct.getParameter(0)) + ")";

            // Extra param to choose the rounding method.
            // http://technet.microsoft.com/en-us/library/ms175003.aspx
            case ROUND:
                return "round(" + translate(funct.getParameter(0)) + ", " + translate(funct.getParameter(1)) + ", 0)";

            // Extra param to choose the rounding method.
            // http://technet.microsoft.com/en-us/library/ms175003.aspx
            case TRUNCATE:
                return "round(" + translate(funct.getParameter(0)) + ", " + translate(funct.getParameter(1)) + ", 1)";
            
            default:
                return getDefaultADQLFunction(
                    funct
                    );
            }
        }
    }
