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

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.antlr.runtime.tree.CommonTree;

import uk.org.ogsadai.activity.ActivityProcessingException;
import uk.org.ogsadai.activity.ActivityTerminatedException;
import uk.org.ogsadai.activity.ActivityUserException;
import uk.org.ogsadai.activity.MatchedIterativeActivity;
import uk.org.ogsadai.activity.io.ActivityInput;
import uk.org.ogsadai.activity.io.BlockWriter;
import uk.org.ogsadai.activity.io.ControlBlock;
import uk.org.ogsadai.activity.io.ListIterator;
import uk.org.ogsadai.activity.io.PipeClosedException;
import uk.org.ogsadai.activity.io.PipeIOException;
import uk.org.ogsadai.activity.io.PipeTerminatedException;
import uk.org.ogsadai.activity.io.TupleListActivityInput;
import uk.org.ogsadai.activity.io.TupleListIterator;
import uk.org.ogsadai.activity.io.TypedListActivityInput;
import uk.org.ogsadai.common.msgs.DAILogger;
import uk.org.ogsadai.context.OGSADAIContext;
import uk.org.ogsadai.dqp.lqp.udf.FunctionRepository;
import uk.org.ogsadai.expression.ExpressionEvaluationException;
import uk.org.ogsadai.expression.arithmetic.ArithmeticExpression;
import uk.org.ogsadai.expression.arithmetic.ArithmeticExpressionFactory;
import uk.org.ogsadai.expression.arithmetic.ExpressionException;
import uk.org.ogsadai.metadata.MetadataWrapper;
import uk.org.ogsadai.parser.SQLParserException;
import uk.org.ogsadai.parser.sql92query.SQLQueryParser;
import uk.org.ogsadai.tuple.ColumnMetadata;
import uk.org.ogsadai.tuple.Null;
import uk.org.ogsadai.tuple.SimpleColumnMetadata;
import uk.org.ogsadai.tuple.SimpleTuple;
import uk.org.ogsadai.tuple.SimpleTupleMetadata;
import uk.org.ogsadai.tuple.Tuple;
import uk.org.ogsadai.tuple.TupleMetadata;
import uk.org.ogsadai.tuple.TupleTypes;
import uk.org.ogsadai.tuple.TypeMismatchException;

/**
 * TODO: document.
 * 
 * @author The OGSA-DAI Project Team.
 */
public class ApplyActivity extends MatchedIterativeActivity
{
    /** Copyright notice. */
    private static final String COPYRIGHT_NOTICE = 
        "Copyright (c) The University of Edinburgh, 2010";
    
    private static final DAILogger LOG = 
        DAILogger.getLogger(ApplyActivity.class);

    /** Name of data input. */
    public static final String INPUT_DATA = "data";
    /** Name of attribute names input. */
    public static final String INPUT_ATTRIBUTE_NAMES = "attributeNames";
    /** Name of attribute values input. */
    public static final String INPUT_ATTRIBUTE_VALUES = "attributeValues";
    /** Name of data output. */
    public static final String OUTPUT_DATA = "data";
    /** Name of bindings output. */
    public static final String OUTPUT_BINDINGS = "bindings";
    
    /** Data output block writer. */
    private BlockWriter mDataOutput;
    /** Bindings output block writer. */
    private BlockWriter mBindingsOutput;

    private FunctionRepository mFunctionRepository;
    
    /**
     * {@inheritDoc}
     */
    protected ActivityInput[] getIterationInputs() 
    {
        return new ActivityInput[] {
            new TupleListActivityInput(INPUT_DATA),
            new TypedListActivityInput(INPUT_ATTRIBUTE_NAMES, String.class),
            new TypedListActivityInput(INPUT_ATTRIBUTE_VALUES, String.class)};
    }

    /**
     * {@inheritDoc}
     */
    protected void preprocess() 
        throws ActivityUserException, ActivityProcessingException, 
               ActivityTerminatedException 
    {
        validateOutput(OUTPUT_DATA);
        validateOutput(OUTPUT_BINDINGS);
        mDataOutput = getOutput(OUTPUT_DATA);
        mBindingsOutput = getOutput(OUTPUT_BINDINGS);
        
        mFunctionRepository = null;
        if (OGSADAIContext.getInstance().containsKey(
            FunctionRepository.FUNCTION_REPOSITORY_KEY))
        {
            mFunctionRepository = 
                (FunctionRepository) OGSADAIContext.getInstance().get(
                    FunctionRepository.FUNCTION_REPOSITORY_KEY);
        }
    }

    /**
     * {@inheritDoc}
     */
    protected void processIteration(Object[] iterationData)
            throws ActivityProcessingException, ActivityTerminatedException,
            ActivityUserException 
    {
        TupleListIterator dataInput = (TupleListIterator)iterationData[0];
        ListIterator attributeNamesInput = (ListIterator) iterationData[1];
        ListIterator attributeValuesInput = (ListIterator) iterationData[2];
 
        // Read the attribute names and values
        List<String> attributeNames = readStringList(attributeNamesInput);
        List<String> attributeValues = readStringList(attributeValuesInput);
        
        if (LOG.isDebugEnabled())
        {
            LOG.debug("BINDING: " + attributeNames + " <- " + attributeValues);
        }
        
        try
        {
            // Need to build arithmetic expressions for the attribute values
            List<ArithmeticExpression> attributeValueExpressions =
                new LinkedList<ArithmeticExpression>();
            for (String expression : attributeValues)
            {
                CommonTree tree = 
                    SQLQueryParser.getInstance().parseSQLForDerivedColumn(
                        expression);
                attributeValueExpressions.add(
                    ArithmeticExpressionFactory.buildArithmeticExpression(
                        tree, 
                        mFunctionRepository));
                
            }
            
    
            // Get the metadata
            MetadataWrapper metadataWrapper = dataInput.getMetadataWrapper();
            TupleMetadata metadata = 
                (TupleMetadata) metadataWrapper.getMetadata();
            
            // Configure the expressions
            for (ArithmeticExpression expression : attributeValueExpressions)
            {
                expression.configure(metadata);
            }
            
            // Process the incoming data tuples
            Object obj;
            boolean haveIncomingData = false;
            boolean haveNonNullBindings = false;
            while ((obj=dataInput.nextValue()) != null)
            {   
                haveIncomingData = true;
                haveNonNullBindings = false;
                
                Tuple tuple = (Tuple) obj;
                
                // Output the bindings
                Iterator<String> namesIt = attributeNames.iterator();
                Iterator<ArithmeticExpression> expressionIt = 
                    attributeValueExpressions.iterator();
                
                while( namesIt.hasNext() )
                {
                    String name = namesIt.next();
                    ArithmeticExpression expression = expressionIt.next();
                    expression.evaluate(tuple);
                    Object expressionResult = expression.getResult();

                    if (LOG.isDebugEnabled())
                        LOG.debug(expression + " = " + expressionResult);
                    
                    if (expressionResult != Null.VALUE)
                    {
                        if (!haveNonNullBindings)
                        {
                            mBindingsOutput.write(ControlBlock.LIST_BEGIN);
                            mBindingsOutput.write(createBindingsMetadata());
                            haveNonNullBindings = true;
                        }
                        int resultType = expression.getMetadata().getType();
                        String binding =
                            name + " = "
                                + convertToString(resultType, expressionResult);

                        Tuple t = createBindingTuple(binding);
                        mBindingsOutput.write(t);

                        if (LOG.isDebugEnabled())
                            LOG.debug("BINDING TUPLE: " + t);
                    }
                }
                
                if (haveNonNullBindings)
                    mBindingsOutput.write(ControlBlock.LIST_END);

                // Output the tuple
                if (haveNonNullBindings)
                {
                    mDataOutput.write(ControlBlock.LIST_BEGIN);
                    mDataOutput.write(metadataWrapper);
                    mDataOutput.write(tuple);
                    mDataOutput.write(ControlBlock.LIST_END);
                }
            }
            
            if (!haveIncomingData)
            {
                // Write out special indicator when we have no bindings
                mBindingsOutput.write(ControlBlock.LIST_BEGIN);
                mBindingsOutput.write(createBindingsMetadata());
                mBindingsOutput.write(ControlBlock.LIST_END);
                
                mDataOutput.write(ControlBlock.LIST_BEGIN);
                mDataOutput.write(metadataWrapper);
                mDataOutput.write(ControlBlock.LIST_END);

            }
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
        catch (SQLParserException e)
        {
            throw new ActivityUserException(e);
        }
        catch (ExpressionException e)
        {
            throw new ActivityUserException(e);
        }
        catch (TypeMismatchException e)
        {
            throw new ActivityUserException(e);
        }
        catch (ExpressionEvaluationException e)
        {
            throw new ActivityUserException(e);
        }
    }
    
    /**
     * {@inheritDoc}
     */
    protected void postprocess() 
        throws ActivityUserException, ActivityProcessingException, 
               ActivityTerminatedException 
    {
        // No post-processing to be done.
    }
    
    private List<String> readStringList(ListIterator listIterator) 
        throws ActivityUserException, ActivityProcessingException, 
               ActivityTerminatedException
    {
        List<String> result = new LinkedList<String>();
        Object obj;
        while((obj = listIterator.nextValue()) != null)
        {
            result.add(obj.toString());
        }
        return result;
    }
    
    private MetadataWrapper createBindingsMetadata()
    {
        List<ColumnMetadata> columns = new LinkedList<ColumnMetadata>();
        ColumnMetadata column = new SimpleColumnMetadata(
            "binding",
            "",
            null,
            null,
            TupleTypes._STRING,
            0,
            ColumnMetadata.COLUMN_NULLABLE_UNKNOWN,
            0);
        columns.add(column);
        
        return new MetadataWrapper( new SimpleTupleMetadata(columns));
    }
    
    private Tuple createBindingTuple(String binding)
    {
        List<Object> elements = new LinkedList<Object>();
        elements.add(binding);
        return new SimpleTuple(elements);
    }
    
    private String convertToString(int type, Object value)
    {
        String result = null;
        switch (type)
        {
            case TupleTypes._DATE:
            case TupleTypes._CHAR:
            case TupleTypes._STRING:
            case TupleTypes._TIME:
            case TupleTypes._TIMESTAMP:
                result = "'" + value + "'";
                break;
            default:
                result = value.toString();
                break;
        }
        return result;
    }
}
