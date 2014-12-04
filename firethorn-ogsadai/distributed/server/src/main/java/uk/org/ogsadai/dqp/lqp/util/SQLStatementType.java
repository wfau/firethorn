// Copyright (c) The University of Edinburgh, 2010.
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

package uk.org.ogsadai.dqp.lqp.util;

import org.antlr.runtime.tree.CommonTree;

import uk.org.ogsadai.parser.SQLParserException;
import uk.org.ogsadai.parser.sql92query.SQLQueryParser;

/**
 * Analyzes the type of an SQL query statement.
 * 
 * @author The OGSA-DAI Project Team.
 */
public class SQLStatementType
{
    /** Copyright notice. */
    private static final String COPYRIGHT_NOTICE = 
	"Copyright (c) The University of Edinburgh, 2010";

    /** Flag set for SELECT FROM statements. */
    private static int mSelectFromFlag = 0x1;
    /** Flag set for SELECT FROM WHERE statements. */
    private static int mSelectFromWhereFlag = 0x2;
    /** Flag set for SELECT * FROM [WHERE] statements. */
    private static int mSelectStarFlag = 0x4;
    /** Flag set for aliases in SELECT_LIST. */
    private static int mSelectListAliasesFlag = 0x8;

    /**
     * Analyzes an SQL statement and returns a set of flags describing the type
     * encoded as integer. Use is* methods to query the result.
     * 
     * @param statement
     *            SQL statement to analyze
     * @return The result of analysis encoded in integer
     */
    public static int getStatementType(String statement)
    {
	int result = 0;

	// If we have SFW form then we should try to do rename "in place"
	CommonTree ast;
	try
	{
	    ast = SQLQueryParser.getInstance().parseSQL(statement);
	}
	catch (SQLParserException e)
	{
	    // Can't do much about it
	    throw new RuntimeException(e);
	}

	CommonTree n1 = (CommonTree) ast.getChild(0); // QUERY
	CommonTree n2 = (CommonTree) n1.getChild(0); // SELECT_LIST
	CommonTree n3 = (CommonTree) n2.getChild(0); // COLUMN
	CommonTree n4 = (CommonTree) n3.getChild(0); // *

	if (n1.getChildCount() > 2)
	{
	    result |= ((CommonTree) n1.getChild(2)).getText().equals("WHERE") ? 
		    mSelectFromWhereFlag : 0;
	}
	else
	{
	    result |= n1.getText().equals("QUERY") ? mSelectFromFlag : 0;
	}

	result |= (isSelectFrom(result) || isSelectFromWhere(result))
		&& n4.getText().equals("*") ? mSelectStarFlag : 0;

	// Check for aliases in the select list
	if (n2.getText().equals("SELECT_LIST"))
	{
	    for (int i = 0; i < n2.getChildCount(); i++)
	    {
		if (n2.getChild(i).getChildCount() == 2)
		{
		    result |= mSelectListAliasesFlag;
		}
	    }
	}
	
	return result;
    }

    /**
     * Checks if the first SELECT_LIST
     * @param flags
     * @return flag indicating if SELECT_LIST has aliases.
     */
    public static boolean isSelectListAliased(int flags)
    {
	return (flags & mSelectListAliasesFlag) == mSelectListAliasesFlag;
    }
    
    /**
     * Checks if flags describe a SELECT FROM ; statement.
     * 
     * @param flags
     * @return <code>true</code> if flags describe SELECT FROM ; statement
     */
    public static boolean isSelectFrom(int flags)
    {
	return (flags & mSelectFromFlag) == mSelectFromFlag;
    }

    /**
     * Checks if flags describe a SELECT FROM WHERE ; statement.
     * 
     * @param flags
     * @return <code>true</code> if flags describe SELECT FROM WHERE ; statement
     */
    public static boolean isSelectFromWhere(int flags)
    {
	return (flags & mSelectFromWhereFlag) == mSelectFromWhereFlag;
    }

    /**
     * Checks if flags describe a SELECT * FROM [WHERE] ; statement.
     * 
     * @param flags
     * @return <code>true</code> if flags describe SELECT * FROM [WHERE] ;
     *         statement
     */
    public static boolean isSelectStar(int flags)
    {
	return (flags & mSelectStarFlag) == mSelectStarFlag;
    }

}
