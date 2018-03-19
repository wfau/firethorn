// Copyright (c) The University of Edinburgh, 2008.
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

package uk.org.ogsadai.dqp.execute.workflow;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import uk.org.ogsadai.client.toolkit.SingleActivityOutput;
import uk.org.ogsadai.client.toolkit.activities.generic.GenericActivity;
import uk.org.ogsadai.client.toolkit.activities.generic.TraversableSingleActivityOutput;
import uk.org.ogsadai.data.IntegerData;
import uk.org.ogsadai.data.ListBegin;
import uk.org.ogsadai.data.ListEnd;
import uk.org.ogsadai.data.StringData;
import uk.org.ogsadai.dqp.execute.ActivityConstructionException;
import uk.org.ogsadai.dqp.lqp.Attribute;
import uk.org.ogsadai.dqp.lqp.Operator;
import uk.org.ogsadai.dqp.lqp.OperatorID;
import uk.org.ogsadai.dqp.lqp.exceptions.AmbiguousAttributeException;
import uk.org.ogsadai.dqp.lqp.exceptions.AttributeNotFoundException;
import uk.org.ogsadai.dqp.lqp.operators.QueryApplyOperator;
import uk.org.ogsadai.tuple.TupleMetadata;

/**
 * Builds activities for operator QUERY APPLY operator.
 *
 * @author The OGSA-DAI Project Team.
 */
public class QueryApplyBuilder implements ActivityPipelineBuilder
{
    /** Copyright notice. */
    private static final String COPYRIGHT_NOTICE = 
        "Copyright (c) The University of Edinburgh, 2008";
    
    /** Join operator annotation for which side to read first. */
    private static final String READ_FIRST = "readFirst";
    
    /** Join operator annotation left hand metadata. */
    private static final String METADATA_LEFT = "metadataLeft";
    
    /** Join operator annotation right hand metadata. */
    private static final String METADATA_RIGHT = "metadataRight";

    /**
     * {@inheritDoc}
     */
    public SingleActivityOutput build(
            Operator op, 
            List<SingleActivityOutput> outputs, 
            PipelineWorkflowBuilder builder) 
        throws ActivityConstructionException
    {
        QueryApplyOperator operator = (QueryApplyOperator)op;
        Collection<Attribute> attributesToBind = operator.getAttributesToBind();
        
        SingleActivityOutput dataOutput;
        int filteredIndex;
        // check which child is the filtered tuple stream
        if (containsAttributes(operator.getChild(0), attributesToBind))
        {
            filteredIndex = 1;
            dataOutput = outputs.get(0);
        }
        else
        {
            filteredIndex = 0;
            dataOutput = outputs.get(1);
        }
        Operator filtered = operator.getChild(filteredIndex);
        SingleActivityOutput filteredOutput = outputs.get(filteredIndex);
       
        GenericActivity stats = 
            new GenericActivity("uk.org.ogsadai.AttributeStatistics");
        stats.createInput("data");
        stats.connectInput("data", dataOutput);
        stats.createInput("columnIds");
        stats.createInput("numBuckets");
        stats.createOutput("data");
        stats.createOutput("result");
        builder.add(stats);
        
        stats.addInput("columnIds", ListBegin.VALUE);
        for (Attribute attribute : attributesToBind)
        {
            stats.addInput("columnIds", new StringData(attribute.toString()));
        }
        stats.addInput("columnIds", ListEnd.VALUE);
        // TODO number of buckets is hard coded
        stats.addInput("numBuckets", new IntegerData(100));

        List<SingleActivityOutput> joinInputs = 
            new ArrayList<SingleActivityOutput>(2);
        // output from stats must be read first by the join activity
        // otherwise the workflow may deadlock
        String readFirst;
        TupleMetadata metadataLeft = 
            operator.getChild(0).getHeading().getTupleMetadata();
        TupleMetadata metadataRight = 
            operator.getChild(1).getHeading().getTupleMetadata();
        if (filteredIndex == 0)
        {
            joinInputs.add(filteredOutput);
            joinInputs.add(stats.getOutput("data"));
            readFirst = "right";
        }
        else
        {
            joinInputs.add(stats.getOutput("data"));
            joinInputs.add(filteredOutput);
            readFirst = "left";
        }
        Operator joinOperator = operator.getOperator();
        joinOperator.addAnnotation(READ_FIRST, readFirst);
        joinOperator.addAnnotation(METADATA_LEFT, metadataLeft);
        joinOperator.addAnnotation(METADATA_RIGHT, metadataRight);
        SingleActivityOutput output = 
            builder.buildOperator(joinOperator, joinInputs);
        
        Operator filteredTableScan = findFilteredTableScanOperator(filtered);
        if (filteredTableScan == null)
        {
            throw new ActivityConstructionException(
                    new Exception("No filtered table scan operator found."));
        }
        
        // add the unconnected output to the builder
        TraversableSingleActivityOutput statsOutput = stats.getOutput("result");
        builder.addOutput(operator, filteredTableScan, statsOutput);
        return output;
    }

    /**
     * Returns <code>true</code> if the heading of the operator contains the
     * specified attributes.
     * 
     * @param operator
     *            operator
     * @param attributes
     *            attributes to check
     * @return <code>true</code> if all attributes are contained in the
     *         operator's heading, <code>false</code> otherwise
     */
    private boolean containsAttributes(Operator operator, Collection<Attribute> attributes)
    {
        boolean result = false;
        try 
        {
            operator.getHeading().containsAll(attributes);
            result = true;
        }
        catch (AmbiguousAttributeException e)
        {
            result = false;
        }
        catch (AttributeNotFoundException e)
        {
            result = false;
        }
        return result;

    }

    /**
     * Traverses the operator tree and looks for a filtered table scan operator.
     * 
     * @param operator
     *            root of the tree to traverse
     * @return the filtered table scan operator
     */
    private Operator findFilteredTableScanOperator(Operator operator) 
    {
        Operator result = null;
        if (operator.getID() == OperatorID.FILTERED_TABLE_SCAN)
        {
            result = operator;
        }
        else if (operator.getChild(0) != null)
        {
            result = findFilteredTableScanOperator(operator.getChild(0));
            if (result == null && operator.isBinary())
            {
                result = findFilteredTableScanOperator(operator.getChild(1));
            }
        }
        return result;
    }

}
