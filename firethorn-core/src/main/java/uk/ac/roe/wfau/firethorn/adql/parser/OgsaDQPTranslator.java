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
import java.util.Iterator;

import adql.db.DBColumn;
import adql.db.DBTable;
import adql.db.exception.UnresolvedJoinException;
import adql.query.ADQLList;
import adql.query.ADQLObject;
import adql.query.ADQLOrder;
import adql.query.ADQLQuery;
import adql.query.ClauseSelect;
import adql.query.ColumnReference;
import adql.query.IdentifierField;
import adql.query.SelectAllColumns;
import adql.query.SelectItem;
import adql.query.constraint.ConstraintsGroup;
import adql.query.from.ADQLJoin;
import adql.query.from.ADQLTable;
import adql.query.operand.ADQLColumn;
import adql.query.operand.function.ADQLFunction;
import adql.query.operand.function.CastFunction;
import adql.query.operand.function.UserDefinedFunction;
import adql.translator.PostgreSQLTranslator;
import adql.translator.TranslationException;
import lombok.extern.slf4j.Slf4j;

/**
 * OGSA-DAI DQP ADQL Translator.
 * Generates DQP compatible ADQL that can be passed to OGSA-DAI DQP.
 * @todo Remove dependency on PostgreSQLTranslator
 *
 */
@Slf4j
public class OgsaDQPTranslator
    extends PostgreSQLTranslator
    implements BaseTranslator
    {

    /**
     *
     *
     */
    public OgsaDQPTranslator()
        {
        super(
            false
            );
        }

    /**
     *
     *
     */
    public OgsaDQPTranslator(final boolean column)
        {
        super(
            column
            );
        }

    /**
     *
     *
     */
    public OgsaDQPTranslator(final boolean catalog, final boolean schema, final boolean table, final boolean column)
        {
        super(
            catalog,
            schema,
            table,
            column
            );
        }

	/**
	 * Appends the full name of the given table to the given StringBuffer.
	 * 
	 * @param str		The string buffer.
	 * @param dbTable	The table whose the full name must be appended.
	 * 
	 * @return			The string buffer + full table name.
	 */
	public final StringBuffer appendFullDBName(final StringBuffer str, final DBTable dbTable){
		if (dbTable != null){
			if (dbTable.getDBCatalogName() != null)
				appendIdentifier(str, dbTable.getDBCatalogName(), IdentifierField.CATALOG).append('.');

			if (dbTable.getDBSchemaName() != null)
				appendIdentifier(str, dbTable.getDBSchemaName(), IdentifierField.SCHEMA).append('.');

			appendIdentifier(str, dbTable.getDBName(), IdentifierField.TABLE);
		}
		return str;
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
     *
     *
     */
    @Override
	public String translate(final SelectAllColumns all)
    throws TranslationException
        {
        log.debug("translate(SelectAllColumns)");

        /*
         * TODO Replace this with code that uses the Firethorn metadata.
         *
         * Check if the ADQLTable is an AdqlParserTable
         * Cast the ADQLTable  into an AdqlParserTable
         * call table.table() to get an AdqlTable object
         * process the column list from the AdqlTable.
         *
         */

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
                    mapAlias.put(key, table.getAlias()+"\"");
                    }
                }
            else {
// getDBLink is null - need to fix this
// Need to find where the ADQLTable is created
// and add a reference to the AdqlParserTable.
                log.warn("ADQLTable with no link to the BaseTable [{}]", table.getName());
                }
            }
        else if (all.getQuery() != null)
            {
            try {
    				dbCols = all.getQuery().getFrom().getDBColumns();
    			} catch (UnresolvedJoinException e) {
    				// TODO Auto-generated catch block
    				e.printStackTrace();
    		}
            final ArrayList<ADQLTable> tables = all.getQuery().getFrom().getTables();
            for(final ADQLTable table : tables)
                {
                if (table.hasAlias())
                    {
                    final String key = appendFullDBName(new StringBuffer(), table.getDBLink()).toString();
                    mapAlias.put(key, table.getAlias());
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

	@Override
	public String translate(SelectItem item) throws TranslationException{
		if (item instanceof SelectAllColumns)
			return translate((SelectAllColumns)item);

		StringBuffer translation = new StringBuffer(translate(item.getOperand()));
		if (item.hasAlias()){
			translation.append(" AS ");
			appendIdentifier(translation, item.getAlias(), false);
		}else{
			translation.append(" AS ");
			appendIdentifier(translation, item.getName(), false);
		}

		return translation.toString();
	}

    public String translate(final CastFunction function)
    throws TranslationException
    	{
        final StringBuilder builder = new StringBuilder();

        builder.append("CAST");
        builder.append("(");
        builder.append(
    		translate(
				function.oper()
				)
    		);
        builder.append(" AS ");
        builder.append(
    		function.type().name()
    		);
        builder.append(")");

        return builder.toString();
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

    @Override
    public String translate(final ADQLFunction function)
    throws TranslationException
		{
    	if (function instanceof CastFunction)
    		{
    		return translate(
				(CastFunction) function
				);
    		}
    	else {
    		return super.translate(
				function
				);
    		}
		}

	@Override
	public String translate(ADQLTable table) throws TranslationException{
		StringBuffer sql = new StringBuffer();

		// CASE: SUB-QUERY:
		if (table.isSubQuery())
			sql.append('(').append(translate(table.getSubQuery())).append(')');

		// CASE: TABLE REFERENCE:
		else{
			// Use the corresponding DB table, if known:
			if (table.getDBLink() != null)
				sql.append(getQualifiedTableName(table.getDBLink()));
			// Otherwise, use the whole table name given in the ADQL query:
			else
				sql.append(table.getFullTableName());
		}

		// Add the table alias, if any:
		if (table.hasAlias()){
			sql.append(" AS ");
			appendIdentifier(sql, table.getAlias(), false);
		}

		return sql.toString();
	}
	

	@Override
	public String translate(ADQLJoin join) throws TranslationException{
		StringBuffer sql = new StringBuffer(translate(join.getLeftTable()));

		if (join.isNatural())
			sql.append(" NATURAL");

		sql.append(' ').append(join.getJoinType()).append(' ').append(translate(join.getRightTable())).append(' ');

		if (!join.isNatural()){
			if (join.getJoinCondition() != null)
				sql.append(translate(join.getJoinCondition()));
			else if (join.hasJoinedColumns()){
				StringBuffer cols = new StringBuffer();
				Iterator<ADQLColumn> it = join.getJoinedColumns();
				while(it.hasNext()){
					ADQLColumn item = it.next();
					if (cols.length() > 0)
						cols.append(", ");
					if (item.getDBLink() == null)
						appendIdentifier(cols, item.getColumnName(), false);
					else
						appendIdentifier(cols, item.getDBLink().getDBName(), IdentifierField.COLUMN);
				}
				sql.append("USING (").append(cols).append(')');
			}
		}

		return sql.toString();
	}


	@Override
	public String translate(ADQLColumn column) throws TranslationException{
		// Use its DB name if known:
		if (column.getDBLink() != null){
			DBColumn dbCol = column.getDBLink();
			StringBuffer colName = new StringBuffer();
			// Use the table alias if any:
			if (column.getAdqlTable() != null && column.getAdqlTable().hasAlias())
				appendIdentifier(colName, column.getAdqlTable().getAlias(), false).append('.');

			// Use the DBTable if any:
			else if (dbCol.getTable() != null && dbCol.getTable().getDBName() != null)
				colName.append(getQualifiedTableName(dbCol.getTable())).append('.');

			// Otherwise, use the prefix of the column given in the ADQL query:
			else if (column.getTableName() != null)
				colName = column.getFullColumnPrefix().append('.');

			appendIdentifier(colName, dbCol.getDBName(), IdentifierField.COLUMN);

			return colName.toString();
		}
		// Otherwise, use the whole name given in the ADQL query:
		else
			return column.getFullColumnName();
	}

	@Override
	public String translate(ColumnReference ref) throws TranslationException{
		if (ref instanceof ADQLOrder)
			return translate((ADQLOrder)ref);
		else
			return getDefaultColumnReference(ref);
	}

	/**
	 * Gets the default SQL output for a column reference.
	 * 
	 * @param ref	The column reference to format into SQL.
	 * 
	 * @return		The corresponding SQL.
	 * 
	 * @throws TranslationException If there is an error during the translation.
	 */
	protected String getDefaultColumnReference(ColumnReference ref) throws TranslationException{
		if (ref.isIndex()){
			return "" + ref.getColumnIndex();
		}else{
			if (ref.getDBLink() == null){
				return ref.getColumnName();
			}else{
				DBColumn dbCol = ref.getDBLink();
				StringBuffer colName = new StringBuffer();
				// Use the table alias if any:
				if (ref.getAdqlTable() != null && ref.getAdqlTable().hasAlias())
					appendIdentifier(colName, ref.getAdqlTable().getAlias(), ref.getAdqlTable().isCaseSensitive(IdentifierField.ALIAS)).append('.');

				// Use the DBTable if any:
				else if (dbCol.getTable() != null)
					colName.append(getQualifiedTableName(dbCol.getTable())).append('.');

				appendIdentifier(colName, dbCol.getDBName(), IdentifierField.COLUMN);

				return colName.toString();
			}
		}
	}

    /**
     * Copy of the PostgreSQLTranslator method ...
     * @todo Need to catch date fields and format them as strings.
     *
    
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
     */

    /**
     * Override the PostgreSQLTranslator method ...
     *
   
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
            //
            // If the column table has an alias.
            if ((column.getAdqlTable() != null)  && (column.getAdqlTable().hasAlias()))
                {
                return super.translate(
                    column
                    );
                }
            //
            // If the column doesn't have an alias, use the full column name
            else {
                final AdqlColumn adql = ((AdqlDBColumn) column.getDBLink()).column();
                log.debug("  adql [{}][{}]", adql.name(), adql.meta().adql().type());
                return translate(
                    adql
                    );
                }
            }
        else {
            log.warn("ADQLColumn getDBLink() is unexpected class [{}]", column.getDBLink().getClass().getName());
            return super.translate(
                column
                );
            }
        }
 
    public String translate(AdqlColumn column)
    throws TranslationException
        {
        log.debug("translate(AdqlColumn)");
        log.debug("  adql [{}][{}]", column.name(), column.getClass().getName());
        log.debug("  fullname [{}]", column.namebuilder().toString());
        log.debug("  basename [{}]", column.base().namebuilder().toString());
        log.debug("  rootname [{}]", column.root().namebuilder().toString());

        return column.root().namebuilder().toString();

        }
 */

    /**
     * Replacement for the PostgreSQLTranslator method.
     *
     *
  
    @Override
    public String translate(final MathFunction funct) throws TranslationException
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
   */
    
    
    /**
     * Override the PostgreSQLTranslator method to add '()' brackets for a ConstraintsGroup.
     * @see RedmineBug450TestCase
     * @see  
     *   
     */
	@Override
    protected String getDefaultADQLList(ADQLList<? extends ADQLObject> list)
	throws TranslationException
	    {
        StringBuilder builder = new StringBuilder();

        log.debug("translate(ADQLList<>)");
        log.debug("  list [{}][{}]", list.getName(), list.getClass().getName());

        if (list instanceof ConstraintsGroup)
            {
            builder.append(
                '('
                );
            builder.append(
                super.getDefaultADQLList(
                    list
                    )
                );
            builder.append(
                ')'
                );
            }
        else {
            builder.append(
                super.getDefaultADQLList(
                    list
                    )
                );
            }

        String result = builder.toString();
        log.debug("  result [{}]", result);
        return result;
	    }

    }
