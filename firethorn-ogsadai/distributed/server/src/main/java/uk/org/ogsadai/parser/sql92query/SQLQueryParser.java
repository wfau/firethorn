// Copyright (c) The University of Edinburgh, 2007 - 2008.
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


package uk.org.ogsadai.parser.sql92query;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.RecognitionException;
import org.antlr.runtime.tree.CommonTree;
import org.antlr.runtime.tree.CommonTreeAdaptor;
import org.antlr.runtime.tree.CommonTreeNodeStream;
import org.antlr.runtime.tree.DOTTreeGenerator;
import org.antlr.runtime.tree.TreeAdaptor;
import org.antlr.stringtemplate.StringTemplateGroup;

import uk.org.ogsadai.dqp.lqp.util.ASTConstants;
import uk.org.ogsadai.parser.SQLParser;
import uk.org.ogsadai.parser.SQLParserException;

/**
 * An SQL 92 Query parser.
 * 
 * @author The OGSA-DAI Project Team
 */
public class SQLQueryParser implements SQLParser
{
    /** Copyright notice */
    private static final String COPYRIGHT_NOTICE = "Copyright (c) The University of Edinburgh, 2007-2008.";

    /** Singleton instance */
    private static SQLQueryParser mInstance = new SQLQueryParser();

    /** String template library for generating SQL statements from a tree */
    private StringTemplateGroup mTemplates = null;

    /**
     * Private constructor for singleton.
     */
    private SQLQueryParser()
    {
        // singleton constructor
    }

    /**
     * Returns the instance of this singleton.
     * 
     * @return singleton instance
     */
    public static SQLQueryParser getInstance()
    {
        return mInstance;
    }

    public String generateSQL(CommonTree abstractSyntaxTree)
            throws SQLParserException, SQLParserInitialisationException
    {
        // Generate SQL from syntax tree
        try
        {
            SQL92QueryWalker treeWalker = createTreeWalker(abstractSyntaxTree);
            SQL92QueryWalker.statement_return statement = treeWalker
                    .statement();

            return statement.toString();
        }
        catch (RecognitionException e)
        {
            throw new SQLParserException(ParserRecognitionException
                    .getInstance(e));
        }
    }

    public CommonTree parseSQL(String expression) throws SQLParserException
    {
        SQL92QueryLexer lex = new SQL92QueryLexer(new ANTLRNoCaseStringStream(
                expression));
        CommonTokenStream tokens = new CommonTokenStream(lex);

        SQL92QueryParser g = new SQL92QueryParser(tokens);

        try
        {
            SQL92QueryParser.statement_return result = g.statement();
            return (CommonTree) result.getTree();
        }
        catch (RecognitionException e)
        {
            throw new SQLParserException(ParserRecognitionException
                    .getInstance(e));
        }
    }

    /**
     * Parses an SQL expression that describes a query.
     * 
     * @param expression
     *            SQL expression
     * @return abstract syntax corresponding to the input expression
     * @throws SQLParserException
     */
    public CommonTree parseSQLForQuery(String expression)
            throws SQLParserException
    {
        SQL92QueryLexer lex = new SQL92QueryLexer(new ANTLRNoCaseStringStream(
                expression));
        CommonTokenStream tokens = new CommonTokenStream(lex);

        SQL92QueryParser g = new SQL92QueryParser(tokens);

        try
        {
            SQL92QueryParser.query_return result = g.query();
            return (CommonTree) result.getTree();
        }
        catch (RecognitionException e)
        {
            throw new SQLParserException(ParserRecognitionException
                    .getInstance(e));
        }
    }

    public CommonTree parseSQLForSelectList(String expression)
        throws SQLParserException
    {
        SQL92QueryLexer lex = new SQL92QueryLexer(new ANTLRNoCaseStringStream(
            expression));
        CommonTokenStream tokens = new CommonTokenStream(lex);

        SQL92QueryParser g = new SQL92QueryParser(tokens);
        try
        {
            SQL92QueryParser.select_list_return result = g.select_list();
            CommonTree tree = (CommonTree) result.getTree();

            // if only one column we don't get a parent for some reason,
            // add one here
            if (tree.getType() == SQL92QueryParser.COLUMN)
            {
                CommonTree newRoot = new CommonTree();
                newRoot.addChild(tree);
                return newRoot;
            }
            
            return (CommonTree) result.getTree();
        }
        catch (RecognitionException e)
        {
            throw new SQLParserException(ParserRecognitionException
                .getInstance(e));
        }
    }
    
    /**
     * Parses an SQL expression that describes a column.
     * 
     * @param expression
     *            SQL expression
     * @return abstract syntax corresponding to the input expression
     * @throws SQLParserException
     */
    public CommonTree parseSQLForDerivedColumn(String expression)
            throws SQLParserException
    {        

        SQL92QueryLexer lex = new SQL92QueryLexer(new ANTLRNoCaseStringStream(
                expression));
        CommonTokenStream tokens = new CommonTokenStream(lex);

        SQL92QueryParser g = new SQL92QueryParser(tokens);
        try
        {
            SQL92QueryParser.derived_column_return result = g.derived_column();
            return (CommonTree) result.getTree();
        }
        catch (RecognitionException e)
        {
            throw new SQLParserException(ParserRecognitionException
                    .getInstance(e));
        }
    }

    public CommonTree parseSQLForValueExpressionOrStar(String expression)
        throws SQLParserException
    {
        if (expression.equals(ASTConstants.STAR_TOKEN))
        {
            TreeAdaptor adaptor = new CommonTreeAdaptor();
            CommonTree ct = (CommonTree) adaptor.create(SQL92QueryParser.ID,
                ASTConstants.STAR_TOKEN);

            return  ct;
        }

        SQL92QueryLexer lex = new SQL92QueryLexer(new ANTLRNoCaseStringStream(
            expression));
        CommonTokenStream tokens = new CommonTokenStream(lex);

        SQL92QueryParser g = new SQL92QueryParser(tokens);
        try
        {
            SQL92QueryParser.value_expression_return result = g
                .value_expression();
            return (CommonTree) result.getTree();
        }
        catch (RecognitionException e)
        {
            throw new SQLParserException(ParserRecognitionException
                .getInstance(e));
        }
    }

    /**
     * Parses an SQL expression that describes a literal.
     * 
     * @param expression
     *            SQL expression
     * @return abstract syntax corresponding to the input expression
     * @throws SQLParserException
     */
    public CommonTree parseSQLForLiteral(String expression)
            throws SQLParserException
    {
        SQL92QueryLexer lex = new SQL92QueryLexer(new ANTLRNoCaseStringStream(
                expression));
        CommonTokenStream tokens = new CommonTokenStream(lex);

        SQL92QueryParser g = new SQL92QueryParser(tokens);

        try
        {
            SQL92QueryParser.literal_return result = g.literal();
            return (CommonTree) result.getTree();
        }
        catch (RecognitionException e)
        {
            throw new SQLParserException(ParserRecognitionException
                    .getInstance(e));
        }
    }

    /**
     * Parses an SQL expression containing a condition (in a where clause, for
     * example.
     * 
     * @param expression
     *            SQL condition expression
     * @return abstract syntax tree corresponding to the input expression
     * @throws SQLParserException
     */
    public CommonTree parseSQLForCondition(String expression)
            throws SQLParserException
    {
        SQL92QueryLexer lex = new SQL92QueryLexer(new ANTLRNoCaseStringStream(
                expression));
        CommonTokenStream tokens = new CommonTokenStream(lex);

        SQL92QueryParser g = new SQL92QueryParser(tokens);

        try
        {
            SQL92QueryParser.search_condition_return result = g
                    .search_condition();
            return (CommonTree) result.getTree();
        }
        catch (RecognitionException e)
        {
            throw new SQLParserException(ParserRecognitionException
                    .getInstance(e));
        }
    }

    /**
     * Generates an SQL expression from an abstract syntax tree.
     * 
     * @param abstractSyntaxTree
     *            tree
     * @return SQL expression
     * @throws SQLParserException
     */
    public String generateSQLForQuery(CommonTree abstractSyntaxTree)
            throws SQLParserException
    {

        // Generate SQL from syntax tree
        try
        {
            SQL92QueryWalker treeWalker = createTreeWalker(abstractSyntaxTree);
            SQL92QueryWalker.query_return column = treeWalker.query();
            return column.toString();
        }
        catch (RecognitionException e)
        {
            throw new SQLParserException(ParserRecognitionException
                    .getInstance(e));
        }
    }

    /**
     * Generates an SQL for a search condition abstract syntax tree.
     * 
     * @param abstractSyntaxTree
     *            tree
     * @return SQL expression
     * @throws SQLParserException
     */
    public String generateSQLForCondition(CommonTree abstractSyntaxTree)
            throws SQLParserException
    {

        // Generate SQL from syntax tree
        try
        {
            SQL92QueryWalker treeWalker = createTreeWalker(abstractSyntaxTree);
            SQL92QueryWalker.search_condition_return column = treeWalker
                    .search_condition();
            return column.toString();
        }
        catch (RecognitionException e)
        {
            throw new SQLParserException(ParserRecognitionException
                    .getInstance(e));
        }
    }

    /**
     * Generates SQL for a value expression AST.
     * 
     * @param abstractSyntaxTree
     *            tree
     * @return SQL expression
     * @throws SQLParserException
     */
    public String generateSQLForValueExpressionOrStar(CommonTree abstractSyntaxTree)
            throws SQLParserException
    {
        if(abstractSyntaxTree.getText().equals(ASTConstants.STAR_TOKEN))
        {
            return ASTConstants.STAR_TOKEN;
        }
        
        // Generate SQL from syntax tree
        try
        {

            SQL92QueryWalker treeWalker = createTreeWalker(abstractSyntaxTree);

            SQL92QueryWalker.value_expression_return valueExpr = treeWalker
                .value_expression();
            return valueExpr.toString();
        }
        catch (RecognitionException e)
        {
            throw new SQLParserException(ParserRecognitionException
                    .getInstance(e));
        }
    }

    /**
     * Creates an abstract syntax tree walker for the given input tree.
     * 
     * @param abstractSyntaxTree
     *            tree
     * @return tree walker
     * @throws SQLParserInitialisationException
     */
    private SQL92QueryWalker createTreeWalker(CommonTree abstractSyntaxTree)
            throws SQLParserInitialisationException
    {
        loadStringTemplateLib();
        CommonTreeNodeStream ctns = new CommonTreeNodeStream(abstractSyntaxTree);
        SQL92QueryWalker treeWalker = new SQL92QueryWalker(ctns);

        treeWalker.setTemplateLib(mTemplates);
        return treeWalker;
    }

    /**
     * Loads the string template library if it has not been loaded before.
     * 
     * @throws SQLParserInitialisationException
     */
    private void loadStringTemplateLib()
            throws SQLParserInitialisationException
    {
        if (mTemplates == null)
        {
            try
            {
                // Load up template definitions from a file on the classpath
                InputStreamReader reader = new InputStreamReader(Thread
                        .currentThread().getContextClassLoader()
                        .getResourceAsStream("SQL92Query.stg"));
                mTemplates = new StringTemplateGroup(reader);
                reader.close();
            }
            catch (FileNotFoundException e)
            {
                throw new SQLParserInitialisationException(e);
            }
            catch (IOException e)
            {
                throw new SQLParserInitialisationException(e);
            }
        }
    }

    public static void main(String[] args) throws Exception
    {
        String inputFile = args[0];
        String outputFile = args[1];
        try
        {
            BufferedReader br = new BufferedReader(new FileReader(inputFile));
            StringBuilder queryBuilder = new StringBuilder();
            String line;

            while ((line = br.readLine()) != null)
            {
                queryBuilder.append(line);
                queryBuilder.append(" ");
            }
            br.close();
            SQLQueryParser parser = SQLQueryParser.getInstance();
            CommonTree ast = parser.parseSQL(queryBuilder.toString());

            DOTTreeGenerator dotGenerator = new DOTTreeGenerator();
            String dotString = dotGenerator.toDOT(ast).toString();

            Pattern myPattern = Pattern.compile("node[^;]*",
                Pattern.CASE_INSENSITIVE | Pattern.DOTALL | Pattern.MULTILINE);
            Matcher myMatcher = myPattern.matcher(dotString);

            StringBuffer sb = new StringBuffer();
            if (myMatcher.find())
                myMatcher
                    .appendReplacement(sb, "node [shape=box, fontsize=11]");
            myMatcher.appendTail(sb);

            FileWriter fw = new FileWriter(outputFile);
            fw.write(sb.toString());
            fw.close();
        }
        catch (Exception e)
        {
            System.out.println(e.getCause().getMessage());
        }
    }
}
