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

package uk.org.ogsadai.activity.relational;

import uk.org.ogsadai.activity.ActivityProcessingException;
import uk.org.ogsadai.activity.ActivityTerminatedException;
import uk.org.ogsadai.activity.ActivityUserException;
import uk.org.ogsadai.activity.MatchedIterativeActivity;
import uk.org.ogsadai.activity.io.ActivityInput;
import uk.org.ogsadai.activity.io.BlockWriter;
import uk.org.ogsadai.activity.io.PipeClosedException;
import uk.org.ogsadai.activity.io.PipeIOException;
import uk.org.ogsadai.activity.io.PipeTerminatedException;
import uk.org.ogsadai.activity.io.TupleListActivityInput;
import uk.org.ogsadai.activity.io.TupleListIterator;
import uk.org.ogsadai.activity.io.TypedActivityInput;
import uk.org.ogsadai.common.msgs.DAILogger;
import uk.org.ogsadai.dqp.lqp.util.SQLStatementType;
import uk.org.ogsadai.tuple.Tuple;

/**
 * Conjunctively expands the WHERE predicate of the <code>expression</code> with
 * binding predicates extracted from the <code>bindings</code>. Binding
 * predicates should use names expected in the result of the base expression,
 * especially if column names have aliased names. For example:
 * 
 * <pre>
 * 	expression: SELECT * FROM t WHERE a=3
 * 	bindings: { b=3, c=4 }
 * 
 * 	result: SELECT * FROM t WHERE a=3 AND b=3 AND c=4
 * </pre>
 * 
 * A derived table is used in cases where the input expression is not a simple
 * SELECT-FROM-WHERE. For example:
 * 
 * <pre>
 * 	expression: SELECT * FROM t WHERE a=3 UNION SELECT * FROM p
 * 	bindings: { b=3 }
 * 
 * 	result: SELECT * FROM (SELECT * FROM t WHERE a=3 UNION SELECT * FROM p) AS Q WHERE b=3
 * </pre>
 * 
 * A derived table is also used when the base query renames columns in the
 * SELECT_LIST. For example:
 * 
 * <pre>
 * 	expression: SELECT a AS ar FROM t WHERE a=3
 * 	bindings: { ar=5 }
 * 	(note the use of column name as expected in the result of the base expression)
 * 
 * 	result: SELECT * FROM (SELECT a AS ar FROM t WHERE a=3) AS ! WHERE ar=5
 * </pre>
 * 
 * @author The OGSA-DAI Project Team.
 */
public class SQLApplyBindingsActivity extends MatchedIterativeActivity
{
    /** Copyright notice. */
    private static final String COPYRIGHT_NOTICE = 
        "Copyright (c) The University of Edinburgh, 2010";
    
    private static final DAILogger LOG = 
        DAILogger.getLogger(SQLApplyBindingsActivity.class);

    /** Name of expression input. */
    public static final String INPUT_EXPRESSION = "expression";
    /** Name of bindings input. */
    public static final String INPUT_BINDINGS = "bindings";
    /** Name of result output. */
    public static final String OUTPUT_RESULT = "result";
    
    /** Result output block writer. */
    private BlockWriter mResultOutput;
    
    /**
     * {@inheritDoc}
     */
    protected ActivityInput[] getIterationInputs()
    {
        return new ActivityInput[] {
            new TypedActivityInput(INPUT_EXPRESSION, String.class),
            new TupleListActivityInput(INPUT_BINDINGS)};
    }

    /**
     * {@inheritDoc}
     */
    protected void preprocess() throws ActivityUserException,
        ActivityProcessingException, ActivityTerminatedException
    {
        validateOutput(OUTPUT_RESULT);
        mResultOutput = getOutput(OUTPUT_RESULT);
    }

    /**
     * {@inheritDoc}
     */
    protected void processIteration(Object[] iterationData)
        throws ActivityProcessingException, ActivityTerminatedException,
        ActivityUserException
    {
        String expression = (String) iterationData[0];
        TupleListIterator bindings = (TupleListIterator) iterationData[1];

        if (LOG.isDebugEnabled())
            LOG.debug("BINDING BASE QUERY: " + expression);

        try
        {
            StringBuffer sb = new StringBuffer();

            boolean addAnd = false;
            
	    int stmtType = SQLStatementType.getStatementType(expression);
	    if (SQLStatementType.isSelectFrom(stmtType)
		    && !SQLStatementType.isSelectListAliased(stmtType))
	    {
		sb.append(expression);
		sb.append(" WHERE ");
	    }
	    else if (SQLStatementType.isSelectFromWhere(stmtType)
		    && !SQLStatementType.isSelectListAliased(stmtType))
	    {
		sb.append(expression);
		addAnd = true;
	    }
	    else
	    {
		sb.append("SELECT * FROM (");
		sb.append(expression);
		sb.append(") AS Q WHERE ");
	    }
            
            boolean appliedBindings = false;
            Object obj;
            while ((obj = bindings.nextValue()) != null)
            {
                Tuple tuple = (Tuple) obj;

                if(addAnd)
                {
                    sb.append(" AND ");
                }

                sb.append(tuple.getString(0));
                addAnd = true;
                appliedBindings = true;
            }
            String query = sb.toString();
            
            // Check if any bindings have been applied and output the original
            // expression if not.
            if(appliedBindings)
            {
        	mResultOutput.write(query);
            }
            else
            {
        	mResultOutput.write(expression);
            }
            
            if (LOG.isDebugEnabled())
                LOG.debug("BINDING QUERY: " + query);
        }  
        catch (PipeClosedException e)
        {
            iterativeStageComplete();
        } 
        catch (PipeIOException e)
        {
            throw new ActivityProcessingException(e);
        }
        catch (PipeTerminatedException e)
        {
            throw new ActivityTerminatedException();
        }
    }
    
    /**
     * {@inheritDoc}
     */
    protected void postprocess() throws ActivityUserException,
        ActivityProcessingException, ActivityTerminatedException
    {
        // No post-processing
    }
}
