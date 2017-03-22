/*
 *  Copyright (C) 2017 Royal Observatory, University of Edinburgh, UK
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

import adql.db.DBColumn;
import adql.db.DBType;
import adql.db.DBType.DBDatatype;
import adql.db.STCS.Region;
import adql.parser.ParseException;
import adql.query.ADQLQuery;
import adql.query.ClauseSelect;
import adql.query.IdentifierField;
import adql.query.operand.ADQLColumn;
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
import adql.query.operand.function.geometry.IntersectsFunction;
import adql.query.operand.function.geometry.PointFunction;
import adql.query.operand.function.geometry.PolygonFunction;
import adql.query.operand.function.geometry.RegionFunction;
import adql.translator.JDBCTranslator;
import adql.translator.TranslationException;
import lombok.extern.slf4j.Slf4j;

/**
 * Extends {@link JDBCTranslator} to implement TAPService grammar. 
 * 
 * 
 */
@Slf4j
public class TAPServiceTranslator extends JDBCTranslator
implements BaseTranslator
    {

    /**
     * 
     */
    public TAPServiceTranslator()
        {
        super();
        }

    /**
     * Replaces the {@link JDBCTranslator} method to not put LIMIT at the end.
     *
     */
    @Override
    public String translate(final ADQLQuery query) throws TranslationException
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
     * Replaces the {@link JDBCTranslator} method to put TOP at the beginning.
     *
     */
    @Override
    public String translate(final ClauseSelect clause) throws TranslationException
        {
        log.debug("translate(ClauseSelect)");

        final StringBuilder builder = new StringBuilder();
        for (int i = 0; (i < clause.size()); i++)
            {
            if (i == 0)
                {
                builder.append(clause.getName());
                if (clause.hasLimit())
                    {
                    builder.append(" TOP ").append(clause.getLimit());
                    }
                if (clause.distinctColumns())
                    {
                    builder.append(" DISTINCT ");
                    }
                }
            else {
                builder.append(" ").append(clause.getSeparator(i));
                }
            builder.append(" ").append(translate(clause.get(i)));
            }
        return builder.toString();
        }

    @Override
    public String translate(final UserDefinedFunction function)
        throws TranslationException
        {
        return getDefaultADQLFunction(function);
        }

    /**
     * Override the default method to add support for CAST. 
     * 
     */
    @Override
    public String translate(final ADQLFunction function) throws TranslationException
        {
        if (function instanceof CastFunction)
            {
            return translate((CastFunction) function);
            }
        else {
            return super.translate(function);
            }
        }

    public String translate(final CastFunction function) throws TranslationException
        {
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
     * Overrides the {@link JDBCTranslator} method to add a check for (column.getAdqlTable() != null)
     *
     */
    @Override
    public String translate(final ADQLColumn column) throws TranslationException {
        log.debug("translate(ADQLColumn)");
        String result = null ;      
        // Use its DB name if known:
        if (column.getDBLink() != null)
            {
            DBColumn dbCol = column.getDBLink();
            StringBuffer colName = new StringBuffer();

            // Use the table alias if any:
            if ((column.getAdqlTable() != null) && (column.getAdqlTable().hasAlias()))
                {
                appendIdentifier(
                    colName,
                    column.getAdqlTable().getAlias(),
                    column.getAdqlTable().isCaseSensitive(
                        IdentifierField.ALIAS
                        )
                    ).append('.');
                }
            // Use the DBTable if any:
            else if ((dbCol.getTable() != null) && (dbCol.getTable().getDBName() != null))
                {
                log.debug("column.getAdqlTable() [{}]", column.getAdqlTable());
                if (column.getAdqlTable() != null)
                    {
                    log.debug("dbCol.getTable() [{}]", dbCol.getTable());
                    log.debug("getQualifiedTableName() [{}]", getQualifiedTableName(dbCol.getTable()));
                    colName.append(
                        getQualifiedTableName(
                            dbCol.getTable()
                            )
                        ).append('.');
                    }
                else {
                    log.debug("dbCol.getTable().getADQLName() [{}]", dbCol.getTable().getADQLName());
                    colName.append(
                        dbCol.getTable().getADQLName()
                        );
                    colName.append('.');
                    }
                }
            // Otherwise, use the prefix of the column given in the ADQL query:
            else if (column.getTableName() != null)
                {
                colName = column.getFullColumnPrefix().append('.');
                }

            log.debug("  dbCol.getADQLName [{}]", dbCol.getADQLName());
            log.debug("  dbCol.getDBName   [{}]", dbCol.getDBName());
            appendIdentifier(
                colName,
                dbCol.getDBName(),
                IdentifierField.COLUMN
                );

            result = colName.toString();
            }
        // Otherwise, use the full name given in the ADQL query:
        else{
            log.debug("  using getFullColumnName()");
            result = column.getFullColumnName();
            }
        log.debug("  result [{}]", result);
        return result ;
        }

    /**
     * Overrides the {@link JDBCTranslator} method ....
     *
     */
    @Override
    public String translate(final MathFunction funct)
        throws TranslationException
        {
        switch (funct.getType())
            {
            case LOG:
                return "log(" + translate(funct.getParameter(0)) + ")";

            case LOG10:
                return "log10(" + translate(funct.getParameter(0)) + ")";

            case RAND:
                if (funct.getNbParameters() == 0)
                    {
                    return "rand()";
                    }
                else {
                    return "rand(" + translate(funct.getParameter(0)) + ")";
                    }

            case ROUND:
                if (funct.getNbParameters() == 1)
                    {
                    return "round(" + translate(funct.getParameter(0)) + ")";
                    }
                else {
                    return "round(" + translate(funct.getParameter(0)) + ", " + translate(funct.getParameter(1)) + ")";
                    }

            case TRUNCATE:
                if (funct.getNbParameters() == 1)
                    {
                    return "truncate(" + translate(funct.getParameter(0)) + ")";
                    }
                else {
                    return "truncate(" + translate(funct.getParameter(0)) + ", " + translate(funct.getParameter(1)) + ")";
                    }

            default :
                return getDefaultADQLFunction(funct);
            }
        }

    /**
     * TODO This is no different to the method in JDBCTranslator.
     *         
    public String getQualifiedTableName(final DBTable table) {
        if (table == null)
            return "";

        StringBuffer buf = new StringBuffer(getQualifiedSchemaName(table));
        if (buf.length() > 0)
            buf.append('.');

        appendIdentifier(buf, table.getDBName(), IdentifierField.TABLE);

        return buf.toString();
    }
     *
     */

	/**
     * Overrides the JDBCTranslator method to disable case sensitivity.
     *
    @Override
    public String translate(SelectItem item) throws TranslationException{
        if (item instanceof SelectAllColumns)
            return translate((SelectAllColumns)item);

        StringBuffer translation = new StringBuffer(translate(item.getOperand()));
        if (item.hasAlias()){
            translation.append(" AS ");
            appendIdentifier(translation, item.getAlias(), false);
        } else {
            translation.append(" AS ");
            appendIdentifier(translation, item.getName(), false);
        }

        return translation.toString();
    }
     *
	 */
    
	/**
     * Overrides the JDBCTranslator method to disable case sensitivity.
     *
    @Override
    public String translate(SelectAllColumns item) throws TranslationException{
        HashMap<String,String> mapAlias = new HashMap<String,String>();

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
                throw new TranslationException("Due to a join problem, the ADQL to SQL translation can not be completed!", pe);
            }
            ArrayList<ADQLTable> tables = item.getQuery().getFrom().getTables();
            for (ADQLTable table : tables) {
                if (table.hasAlias()) {
                    String key = getQualifiedTableName(table.getDBLink());
                    mapAlias.put(key, table.isCaseSensitive(IdentifierField.ALIAS) ? (table.getAlias()) : table.getAlias());
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
     *
	 */

    /**
     * Overrides the JDBCTranslator method to disable escaping quotes.
     * 
    @Override
    public String translate(StringConstant strConst) throws TranslationException {
        return "'" + strConst.getValue() + "'";
    }
     * 
     */

	/**
     * TODO This is no different to the method in JDBCTranslator.
	 *
    @Override
    public String translate(GeometryValue<? extends GeometryFunction> geomValue) throws TranslationException {
        return translate(geomValue.getValue());
    }
     * 
     */

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
	public Region translateGeometryFromDB(final Object jdbcColValue) throws ParseException{
		throw new ParseException("Unsupported geometrical value! The value \"" + jdbcColValue + "\" can not be parsed as a region.");
	}

	@Override
	public Object translateGeometryToDB(final Region region) throws ParseException{
		throw new ParseException("Geometries can not be uploaded in the database in this implementation!");
	}
    
    /**
     * Overrides the JDBCTranslator method.
     * TODO - need to review the mappings.
     *
     */
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

    /**
     * Overrides the JDBCTranslator method.
     * TODO - need to review the mappings.
     * 
     */
    @Override
    public String convertTypeToDB(final DBType type)
        {
        if (type == null)
            {
            return "varchar";
            }
            
        switch(type.type)
            {
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

            case DOUBLE:
                return "double";

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

    /**
     * Overrides the JDBCTranslator method to only delimit column identifiers and aliases.
     * TODO This probably should be  delimit all - but that fails on the Gaia Archive at the moment.
     *
     */
	@Override
	public boolean isCaseSensitive(final IdentifierField field)
	    {
		return field == null ? false : field.isCaseSensitive((byte)0x11);
	    }
    }
