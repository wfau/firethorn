// Copyright (c) The University of Edinburgh, 2012.
//
// LICENCE-START
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software 
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
// LICENCE-END

package uk.org.ogsadai.activity.derby;

import java.util.UUID;

import org.antlr.runtime.tree.CommonTree;

import uk.org.ogsadai.activity.ActivityProcessingException;
import uk.org.ogsadai.activity.ActivityUserException;
import uk.org.ogsadai.context.OGSADAIContext;
import uk.org.ogsadai.dqp.lqp.Attribute;
import uk.org.ogsadai.dqp.lqp.AttributeImpl;
import uk.org.ogsadai.dqp.lqp.SimpleRenameMap;
import uk.org.ogsadai.dqp.lqp.udf.FunctionRepository;
import uk.org.ogsadai.expression.Expression;
import uk.org.ogsadai.expression.ExpressionFactory;
import uk.org.ogsadai.expression.ExpressionUtils;
import uk.org.ogsadai.expression.arithmetic.ExpressionException;
import uk.org.ogsadai.parser.SQLParserException;
import uk.org.ogsadai.parser.sql92query.SQLQueryParser;
import uk.org.ogsadai.tuple.ColumnMetadata;
import uk.org.ogsadai.tuple.TupleMetadata;
import uk.org.ogsadai.tuple.TupleTypes;

public class DerbyUtilities
{
    /** Copyright notice. */
    private static final String COPYRIGHT_NOTICE = 
        "Copyright (c) The University of Edinburgh, 2012";

    private DerbyUtilities()
    {
        // only static methods
    }

    /**
     * Generates a CREATE TABLE statement from the given metadata. The column
     * names are generated from the source table name and the column name, that
     * is, <i>source_column</i> if a column source is not <code>null</code>, or
     * <i>column</i> if source is <code>null</code>.
     * 
     * @param tableName
     *            name of the table to be created
     * @param metadata
     *            tuple metadata of the dataset
     * @return CREATE TABLE statement
     */
    public static String getCreateTableStatement(
            String tableName, TupleMetadata metadata) 
    {
        StringBuilder result = new StringBuilder();
        result.append("CREATE TABLE ");
        result.append(tableName).append(" (");
        
        for (int i=0; i<metadata.getColumnCount(); i++)
        {
            if (i != 0)
            {
                result.append(", ");
            }
            ColumnMetadata column = metadata.getColumnMetadata(i);
            result.append(getFullName(column)).append(" ");
            result.append(getColumnTypeName(column.getType()));
        }
        
        result.append(")");
        return result.toString();
    }

    /**
     * Maps a tuple type to a Derby column type name.
     * 
     * @param type
     *            tuple type
     * @return Derby column type
     */
    public static String getColumnTypeName(int type)
    {
        switch (type)
        {
        case TupleTypes._BIGDECIMAL:
            return "NUMERIC(31)";
        case TupleTypes._BOOLEAN:
            return "BOOLEAN";
        case TupleTypes._BYTEARRAY:
            return "BINARY";
        case TupleTypes._CHAR:
            // maximum size?? <= 32672
            return "VARCHAR(4096)";
        case TupleTypes._DATE:
            return "DATE";
        case TupleTypes._DOUBLE:
            return "DOUBLE";
        case TupleTypes._FLOAT:
            return "REAL";
        case TupleTypes._INT:
            return "INT";
        case TupleTypes._LONG:
            return "BIGINT";
        case TupleTypes._ODBLOB:
            return "BLOB";
        case TupleTypes._ODCLOB:
            return "CLOB";
        case TupleTypes._SHORT:
            return "SMALLINT";
        case TupleTypes._STRING:
            return "VARCHAR(4096)";
        case TupleTypes._TIME:
            return "TIME";
        case TupleTypes._TIMESTAMP:
            return "TIMESTAMP";
        }
        throw new IllegalArgumentException("Cannot map type: " + type);
    }
    
    public static boolean canBeIndexed(int tupleType)
    {
        switch (tupleType)
        {
        case TupleTypes._BIGDECIMAL:
        case TupleTypes._DOUBLE:
        case TupleTypes._FLOAT:
        case TupleTypes._INT:
        case TupleTypes._LONG:
        case TupleTypes._SHORT:
        case TupleTypes._STRING:
        case TupleTypes._DATE:
        case TupleTypes._TIME:
        case TupleTypes._TIMESTAMP:
            return true;
        default:
            return false;
        }
    }    

    /**
     * Rename columns in a JOIN condition and produce the complete SQL query.
     * 
     * @param table1
     *            name of the first table in the join
     * @param table2
     *            name of the second table in the join
     * @param condition
     *            join condition
     * @param metadata1
     *            tuple metadata of the first table
     * @param metadata2
     *            tuple metadata of the second table
     * @return SQL query
     * @throws SQLParserException
     *             if the join condition cannot be parsed
     * @throws ExpressionException
     *             if the join condition cannot be converted into an expression
     */
    public static String generateJoinQuery(
            String table1, String table2, String condition, 
            TupleMetadata metadata1,
            TupleMetadata metadata2) 
        throws SQLParserException, ExpressionException
    {
        CommonTree ast =
            SQLQueryParser.getInstance().parseSQLForCondition(condition);
        
        // Get the function repository if there is one.  Without a function
        // repository we can handle all derived attributes except those that
        // use functions.
        FunctionRepository functionRepository = null;
        if (OGSADAIContext.getInstance().containsKey(
            FunctionRepository.FUNCTION_REPOSITORY_KEY))
        {
            functionRepository = 
                (FunctionRepository) OGSADAIContext.getInstance().get(
                    FunctionRepository.FUNCTION_REPOSITORY_KEY);
        }
        
        // rename all input attributes and append them to the SELECT list
        StringBuilder query = new StringBuilder();
        query.append("SELECT ");
        SimpleRenameMap renameMap = new SimpleRenameMap();
        for (int i=0; i<metadata1.getColumnCount(); i++)
        {
            ColumnMetadata column = metadata1.getColumnMetadata(i);
            String fullName = getFullName(column);
            query.append(fullName).append(", ");
            Attribute attribute = 
                new AttributeImpl(column.getName(), column.getTableName());
            Attribute renamedAttribute = new AttributeImpl(fullName);
            renameMap.add(attribute, renamedAttribute);
        }
        for (int i=0; i<metadata2.getColumnCount(); i++)
        {
            ColumnMetadata column = metadata2.getColumnMetadata(i);
            String fullName = getFullName(column);
            query.append(fullName).append(", ");
            Attribute attribute = 
                new AttributeImpl(column.getName(), column.getTableName());
            Attribute renamedAttribute = new AttributeImpl(fullName);
            renameMap.add(attribute, renamedAttribute);
        }
        // remove the last ", " at the end of the SELECT list 
        query.delete(query.length()-2, query.length());
        
        query.append(" FROM ");
        query.append(table1).append(" JOIN ");
        query.append(table2).append(" ON ");
        
        // rename the attributes in the join condition
        Expression expression = 
            ExpressionFactory.buildExpression(ast, functionRepository);
        ExpressionUtils.renameUsedAttributes(expression, renameMap);
        String joinCondition = ExpressionUtils.generateSQL(expression);
        query.append(joinCondition);
        
        return query.toString();
    }
    
    /**
     * Generates a random table name.
     * 
     * @return table name
     */
    public static String generateTableName()
    {
        return UUID.randomUUID().toString().replaceAll("-", "_");
    }
    
    /**
     * Returns the join condition as an expression tree.
     * 
     * @param condition
     *            join condition
     * @return root of the expression tree
     * @throws ActivityProcessingException
     * @throws ActivityUserException
     */
    public static Expression getCondition(String condition) 
        throws ActivityProcessingException, ActivityUserException
    {
        // Get the function repository if there is one.
        FunctionRepository functionRepository = null;
        if (OGSADAIContext.getInstance().containsKey(
            FunctionRepository.FUNCTION_REPOSITORY_KEY))
        {
            functionRepository = 
                (FunctionRepository) OGSADAIContext.getInstance().get(
                    FunctionRepository.FUNCTION_REPOSITORY_KEY);
        }
        try
        {
            CommonTree ast = 
                SQLQueryParser.getInstance().parseSQLForCondition(condition);
            return ExpressionFactory.buildExpression(ast, functionRepository);
        }
        catch (SQLParserException e)
        {
            throw new ActivityProcessingException(e);
        }
        catch (ExpressionException e)
        {
            throw new ActivityUserException(e);
        }
    }
    
    private static String getFullName(ColumnMetadata column)
    {
        String name = column.getName();
        String source = column.getTableName();
        if (source != null)
        {
            name = source + "_" + name;
        }
        return name;
    }

}
