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

import adql.db.DBColumn;
import adql.db.DBTable;
import adql.db.DBType;
import adql.db.DBType.DBDatatype;
import adql.db.exception.UnresolvedJoinException;
import adql.query.ADQLList;
import adql.query.ADQLObject;
import adql.query.ADQLQuery;
import adql.query.ClauseSelect;
import adql.query.IdentifierField;
import adql.query.SelectAllColumns;
import adql.query.SelectItem;
import adql.query.constraint.ConstraintsGroup;
import adql.query.from.ADQLTable;
import adql.query.operand.ADQLColumn;
import adql.query.operand.StringConstant;
import adql.query.operand.function.ADQLFunction;
import adql.query.operand.function.CastFunction;
import adql.query.operand.function.MathFunction;
import adql.query.operand.function.UserDefinedFunction;
import adql.query.operand.function.geometry.AreaFunction;
import adql.query.operand.function.geometry.BoxFunction;
import adql.query.operand.function.geometry.CentroidFunction;
import adql.query.operand.function.geometry.CircleFunction;
import adql.query.operand.function.geometry.ContainsFunction;
import adql.query.operand.function.geometry.DistanceFunction;
import adql.query.operand.function.geometry.ExtractCoord;
import adql.query.operand.function.geometry.ExtractCoordSys;
import adql.query.operand.function.geometry.GeometryFunction;
import adql.query.operand.function.geometry.GeometryFunction.GeometryValue;
import adql.query.operand.function.geometry.IntersectsFunction;
import adql.query.operand.function.geometry.PointFunction;
import adql.query.operand.function.geometry.PolygonFunction;
import adql.query.operand.function.geometry.RegionFunction;
import adql.translator.ADQLTranslator;
import adql.translator.PostgreSQLTranslator;
import adql.translator.TranslationException;
import lombok.extern.slf4j.Slf4j;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlColumn;

/*
 * This file is part of ADQLLibrary.
 * 
 * ADQLLibrary is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * ADQLLibrary is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with ADQLLibrary.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * Copyright 2014 - Astronomisches Rechen Institut (ARI)
 */

/**
 * SQLServer SQL translator.
 * 
 * @todo Remove dependency on PostgreSQLTranslator
 *
 */
@Slf4j
public class SQLServerTranslator extends PostgreSQLTranslator implements ADQLTranslator {

	private final String schemaName = "dbo";

	/**
	 *
	 *
	 */
	public SQLServerTranslator() {
		super(false);

	}

	/**
	 *
	 *
	 */
	public SQLServerTranslator(final boolean column) {
		super(column);

	}

	/**
	 *
	 *
	 */
	public SQLServerTranslator(final boolean catalog, final boolean schema, final boolean table, final boolean column) {
		super(catalog, schema, table, column);

	}

	/**
	 * Replaces the PostgreSQLTranslator method to not put LIMIT at the end.
	 *
	 */
	@Override
	public String translate(final ADQLQuery query) throws TranslationException {
		log.debug("translate(ADQLQuery)");
		final StringBuilder builder = new StringBuilder();
		builder.append(translate(query.getSelect()));

		builder.append("\nFROM ").append(translate(query.getFrom()));

		if (!query.getWhere().isEmpty()) {
			builder.append('\n').append(translate(query.getWhere()));
		}

		if (!query.getGroupBy().isEmpty()) {
			builder.append('\n').append(translate(query.getGroupBy()));
		}

		if (!query.getHaving().isEmpty()) {
			builder.append('\n').append(translate(query.getHaving()));
		}

		if (!query.getOrderBy().isEmpty()) {
			builder.append('\n').append(translate(query.getOrderBy()));
		}
		return builder.toString();
	}

	/**
	 * Replaces the PostgreSQLTranslator method to put TOP at the beginning.
	 *
	 */
	@Override
	public String translate(final ClauseSelect clause) throws TranslationException {
		log.debug("translate(ClauseSelect)");

		final StringBuilder builder = new StringBuilder();
		for (int i = 0; (i < clause.size()); i++) {
			if (i == 0) {
				builder.append(clause.getName());
				if (clause.hasLimit()) {
					builder.append(" TOP ").append(clause.getLimit());
				}
				if (clause.distinctColumns()) {
					builder.append(" DISTINCT ");
				}
			} else {
				builder.append(" ").append(clause.getSeparator(i));
			}
			builder.append(" ").append(translate(clause.get(i)));
		}
		return builder.toString();
	}

	@Override
	public String translate(final UserDefinedFunction function) throws TranslationException {
		log.debug("translate(UserDefinedFunction)");
		return getDefaultADQLFunction(function);
	}

	/**
	 * Gets the default SQL output for the given ADQL function.
	 *
	 * @param function
	 *            The ADQL function to format into SQL.
	 * @return The corresponding SQL.
	 * @throws TranslationException
	 *             If there is an error during the translation.
	 *
	 */
	@Override
	protected String getDefaultADQLFunction(final ADQLFunction function) throws TranslationException {
		final StringBuilder builder = new StringBuilder();
		//
		// If the function is user defined.
		// ZRQ-UDF
		if (function instanceof UserDefinedFunction) {
			//
			// TODO Check the function schema.
			if (true) {
				builder.append(this.schemaName);
				builder.append(".");
			}
		}

		builder.append(function.getName());
		builder.append("(");

		for (int param = 0; param < function.getNbParameters(); param++) {
			if (param > 0) {
				builder.append(", ");
			}
			builder.append(translate(function.getParameter(param)));
		}
		builder.append(")");
		return builder.toString();
	}

	@Override
	public String translate(final ADQLFunction function) throws TranslationException {
		if (function instanceof CastFunction) {
			return translate((CastFunction) function);
		} else {
			return super.translate(function);
		}
	}

	public String translate(final CastFunction function) throws TranslationException {
		final StringBuilder builder = new StringBuilder();

		builder.append("CAST");
		builder.append("(");
		builder.append(translate(function.oper()));
		builder.append(" AS ");
		builder.append(function.type().name());
		builder.append(")");

		return builder.toString();
	}

	/**
	 * Copy of the PostgreSQLTranslator method ...
	 *
	 */
	@Override
	public String translate(final ADQLColumn column) throws TranslationException {
		log.debug("translate(ADQLColumn)");
		// Use its DB name if known:
		if (column.getDBLink() != null) {
			DBColumn dbCol = column.getDBLink();
			StringBuffer colName = new StringBuffer();
			// Use the table alias if any:

			if (column.getAdqlTable() != null && column.getAdqlTable().hasAlias())
				appendIdentifier(colName, column.getAdqlTable().getAlias(),
						column.getAdqlTable().isCaseSensitive(IdentifierField.ALIAS)).append('.');

			// Use the DBTable if any:
			else if (dbCol.getTable() != null && dbCol.getTable().getDBName() != null) {

				if (column.getAdqlTable() != null) {
					colName.append(getQualifiedTableName(dbCol.getTable())).append('.');
				} else {
					colName.append(dbCol.getTable().getADQLName());
					colName.append('.');
				}
			}
			// Otherwise, use the prefix of the column given in the ADQL query:
			else if (column.getTableName() != null)
				colName = column.getFullColumnPrefix().append('.');

			appendIdentifier(colName, dbCol.getADQLName(), IdentifierField.COLUMN);

			return colName.toString();
		}
		// Otherwise, use the whole name given in the ADQL query:
		else
			return column.getFullColumnName();
	}

	public String translate(AdqlColumn column) throws TranslationException {
		log.debug("translate(AdqlColumn)");
		log.debug("  adql [{}][{}]", column.name(), column.getClass().getName());
		log.debug("  fullname [{}]", column.namebuilder().toString());
		log.debug("  basename [{}]", column.base().namebuilder().toString());
		log.debug("  rootname [{}]", column.root().namebuilder().toString());

		return column.root().namebuilder().toString();

	}

	/**
	 * Replacement for the PostgreSQLTranslator method.
	 *
	 *
	 */
	@Override
	public String translate(final MathFunction funct) throws TranslationException {
		switch (funct.getType()) {
		case LOG:
			return "log(" + translate(funct.getParameter(0)) + ")";

		case LOG10:
			return "log10(" + translate(funct.getParameter(0)) + ")";

		case RAND:
			if (funct.getNbParameters() > 0) {
				return "rand(" + translate(funct.getParameter(0)) + ")";
			} else {
				return "rand()";
			}

			// Extra param to choose the rounding method.
			// http://technet.microsoft.com/en-us/library/ms175003.aspx
		case ROUND:
			if (funct.getNbParameters() == 1) {
				return "round(" + translate(funct.getParameter(0)) + ", 0)";

			} else if (funct.getNbParameters() > 1) {
				return "round(" + translate(funct.getParameter(0)) + ", " + translate(funct.getParameter(1)) + ", 0)";

			}

			// Extra param to choose the rounding method.
			// http://technet.microsoft.com/en-us/library/ms175003.aspx
		case TRUNCATE:
			if (funct.getNbParameters() == 1) {
				return "round(" + translate(funct.getParameter(0)) + ", 1)";
			} else if (funct.getNbParameters() > 1) {
				return "round(" + translate(funct.getParameter(0)) + ", " + translate(funct.getParameter(1)) + ", 1)";
			}

		default:
			return getDefaultADQLFunction(funct);
		}
	}

	/**
	 * Override the PostgreSQLTranslator method to add '()' brackets for a
	 * ConstraintsGroup.
	 * 
	 * @see RedmineBug450TestCase
	 * @see
	 * 
	 */
	@Override
	protected String getDefaultADQLList(ADQLList<? extends ADQLObject> list) throws TranslationException {
		StringBuilder builder = new StringBuilder();

		log.debug("translate(ADQLList<>)");
		log.debug("  list [{}][{}]", list.getName(), list.getClass().getName());

		if (list instanceof ConstraintsGroup) {

			builder.append(super.getDefaultADQLList(list));

		} else {
			builder.append(super.getDefaultADQLList(list));
		}

		String result = builder.toString();
		log.debug("  result [{}]", result);
		return result;
	}

	
	/**
	 * <p>
	 * Get the qualified DB name of the given table.
	 * </p>
	 * 
	 * <p>
	 * <i>Note: This function will, by default, add double quotes if the table
	 * name must be case sensitive in the SQL query. This information is
	 * provided by {@link #isCaseSensitive(IdentifierField)}. </i>
	 * </p>
	 * 
	 * @param table
	 *            The table whose the qualified DB name is asked.
	 * 
	 * @return The qualified (with DB catalog and schema prefix if any, and with
	 *         double quotes if needed) DB table name, or an empty string if the
	 *         given table is NULL or if there is no DB name.
	 */
	public String getQualifiedTableName(final DBTable table) {
		if (table == null)
			return "";

		StringBuffer buf = new StringBuffer(getQualifiedSchemaName(table));
		if (buf.length() > 0)
			buf.append('.');

		appendIdentifier(buf, table.getDBName(), IdentifierField.TABLE);

		return buf.toString();
	}

	/* *************************** */
	/* ****** LIST & CLAUSE ****** */
	/* *************************** */
	@Override
	public String translate(SelectItem item) throws TranslationException {
		if (item instanceof SelectAllColumns)
			return translate((SelectAllColumns) item);

		StringBuffer translation = new StringBuffer(translate(item.getOperand()));
		if (item.hasAlias()) {
			translation.append(" AS ");
			appendIdentifier(translation, item.getAlias(), false);
		} else {
			translation.append(" AS ");
			appendIdentifier(translation, item.getName(), false);
		}

		return translation.toString();
	}

	@Override
	public String translate(SelectAllColumns item) throws TranslationException {
		HashMap<String, String> mapAlias = new HashMap<String, String>();

		// Fetch the full list of columns to display:
		Iterable<DBColumn> dbCols = null;
		if (item.getAdqlTable() != null && item.getAdqlTable().getDBLink() != null) {
			ADQLTable table = item.getAdqlTable();
			dbCols = table.getDBLink();
			if (table.hasAlias()) {
				String key = getQualifiedTableName(table.getDBLink());
				mapAlias.put(key, table.isCaseSensitive(IdentifierField.ALIAS) ? (table.getAlias()) : table.getAlias());
			}
		} else if (item.getQuery() != null) {
			try {
				dbCols = item.getQuery().getFrom().getDBColumns();
			} catch (UnresolvedJoinException pe) {
				throw new TranslationException(
						"Due to a join problem, the ADQL to SQL translation can not be completed!", pe);
			}
			ArrayList<ADQLTable> tables = item.getQuery().getFrom().getTables();
			for (ADQLTable table : tables) {
				if (table.hasAlias()) {
					String key = getQualifiedTableName(table.getDBLink());
					mapAlias.put(key,
							table.isCaseSensitive(IdentifierField.ALIAS) ? (table.getAlias()) : table.getAlias());
				}
			}
		}

		// Write the DB name of all these columns:
		if (dbCols != null) {
			StringBuffer cols = new StringBuffer();
			for (DBColumn col : dbCols) {
				if (cols.length() > 0)
					cols.append(',');
				if (col.getTable() != null) {
					String fullDbName = getQualifiedTableName(col.getTable());
					if (mapAlias.containsKey(fullDbName))
						appendIdentifier(cols, mapAlias.get(fullDbName), false).append('.');
					else
						cols.append(fullDbName).append('.');
				}
				appendIdentifier(cols, col.getDBName(), IdentifierField.COLUMN);
				cols.append(" AS ").append(col.getADQLName());
			}
			return (cols.length() > 0) ? cols.toString() : item.toADQL();
		} else {
			return item.toADQL();
		}
	}

	@Override
	public String translate(StringConstant strConst) throws TranslationException {
		return "'" + strConst.getValue() + "'";
	}

	/* *********************************** */
	/* ****** GEOMETRICAL FUNCTIONS ****** */
	/* *********************************** */

	@Override
	public String translate(GeometryValue<? extends GeometryFunction> geomValue) throws TranslationException {
		return translate(geomValue.getValue());
	}

	@Override
	public String translate(ExtractCoord extractCoord) throws TranslationException {
		return getDefaultADQLFunction(extractCoord);
	}

	@Override
	public String translate(ExtractCoordSys extractCoordSys) throws TranslationException {
		return getDefaultADQLFunction(extractCoordSys);
	}

	@Override
	public String translate(AreaFunction areaFunction) throws TranslationException {
		return getDefaultADQLFunction(areaFunction);
	}

	@Override
	public String translate(CentroidFunction centroidFunction) throws TranslationException {
		return getDefaultADQLFunction(centroidFunction);
	}

	@Override
	public String translate(DistanceFunction fct) throws TranslationException {
		return getDefaultADQLFunction(fct);
	}

	@Override
	public String translate(ContainsFunction fct) throws TranslationException {
		return getDefaultADQLFunction(fct);
	}

	@Override
	public String translate(IntersectsFunction fct) throws TranslationException {
		return getDefaultADQLFunction(fct);
	}

	@Override
	public String translate(BoxFunction box) throws TranslationException {
		return getDefaultADQLFunction(box);
	}

	@Override
	public String translate(CircleFunction circle) throws TranslationException {
		return getDefaultADQLFunction(circle);
	}

	@Override
	public String translate(PointFunction point) throws TranslationException {
		return getDefaultADQLFunction(point);
	}

	@Override
	public String translate(PolygonFunction polygon) throws TranslationException {
		return getDefaultADQLFunction(polygon);
	}

	@Override
	public String translate(RegionFunction region) throws TranslationException {
		return getDefaultADQLFunction(region);
	}
	
	@Override
	public DBType convertTypeFromDB(final int dbmsType, final String rawDbmsTypeName, String dbmsTypeName, final String[] params){
		// If no type is provided return VARCHAR:
		if (dbmsTypeName == null || dbmsTypeName.trim().length() == 0)
			return null;

		// Put the dbmsTypeName in lower case for the following comparisons:
		dbmsTypeName = dbmsTypeName.toLowerCase();

		// Extract the length parameter (always the first one):
		int lengthParam = DBType.NO_LENGTH;
		if (params != null && params.length > 0){
			try{
				lengthParam = Integer.parseInt(params[0]);
			}catch(NumberFormatException nfe){}
		}

		// SMALLINT
		if (dbmsTypeName.equals("smallint") || dbmsTypeName.equals("tinyint") || dbmsTypeName.equals("bit"))
			return new DBType(DBDatatype.SMALLINT);
		// INTEGER
		else if (dbmsTypeName.equals("int"))
			return new DBType(DBDatatype.INTEGER);
		// BIGINT
		else if (dbmsTypeName.equals("bigint"))
			return new DBType(DBDatatype.BIGINT);
		// REAL (cf https://msdn.microsoft.com/fr-fr/library/ms173773(v=sql.120).aspx)
		else if (dbmsTypeName.equals("real") || (dbmsTypeName.equals("float") && lengthParam >= 1 && lengthParam <= 24))
			return new DBType(DBDatatype.REAL);
		// DOUBLE (cf https://msdn.microsoft.com/fr-fr/library/ms173773(v=sql.120).aspx)
		else if (dbmsTypeName.equals("float") || dbmsTypeName.equals("decimal") || dbmsTypeName.equals("numeric"))
			return new DBType(DBDatatype.DOUBLE);
		// BINARY
		else if (dbmsTypeName.equals("binary"))
			return new DBType(DBDatatype.BINARY, lengthParam);
		// VARBINARY
		else if (dbmsTypeName.equals("varbinary"))
			return new DBType(DBDatatype.VARBINARY, lengthParam);
		// CHAR
		else if (dbmsTypeName.equals("char") || dbmsTypeName.equals("nchar"))
			return new DBType(DBDatatype.CHAR, lengthParam);
		// VARCHAR
		else if (dbmsTypeName.equals("varchar") || dbmsTypeName.equals("nvarchar"))
			return new DBType(DBDatatype.VARCHAR, lengthParam);
		// BLOB
		else if (dbmsTypeName.equals("image"))
			return new DBType(DBDatatype.BLOB);
		// CLOB
		else if (dbmsTypeName.equals("text") || dbmsTypeName.equals("ntext"))
			return new DBType(DBDatatype.CLOB);
		// TIMESTAMP
		else if (dbmsTypeName.equals("timestamp") || dbmsTypeName.equals("datetime") || dbmsTypeName.equals("datetime2") || dbmsTypeName.equals("datetimeoffset") || dbmsTypeName.equals("smalldatetime") || dbmsTypeName.equals("time") || dbmsTypeName.equals("date") || dbmsTypeName.equals("date"))
			return new DBType(DBDatatype.TIMESTAMP);
		// Default:
		else
			return null;
	}

	@Override
	public String convertTypeToDB(final DBType type){
		if (type == null)
			return "varchar";

		switch(type.type){

			case SMALLINT:
			case REAL:
			case BIGINT:
			case CHAR:
			case VARCHAR:
			case BINARY:
			case VARBINARY:
				return type.type.toString().toLowerCase();

			case INTEGER:
				return "int";

			// (cf https://msdn.microsoft.com/fr-fr/library/ms173773(v=sql.120).aspx)
			case DOUBLE:
				return "float(53)";

			case TIMESTAMP:
				return "datetime";

			case BLOB:
				return "image";

			case CLOB:
				return "text";

			case POINT:
			case REGION:
			default:
				return "varchar";
		}
	}

}
