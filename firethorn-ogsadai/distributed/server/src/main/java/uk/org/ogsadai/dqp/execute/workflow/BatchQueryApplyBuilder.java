// Copyright (c) The University of Edinburgh, 2011.
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
import java.util.List;

import uk.org.ogsadai.client.toolkit.SingleActivityOutput;
import uk.org.ogsadai.client.toolkit.activities.generic.GenericActivity;
import uk.org.ogsadai.dqp.execute.ActivityConstructionException;
import uk.org.ogsadai.dqp.lqp.Operator;
import uk.org.ogsadai.dqp.lqp.OperatorID;
import uk.org.ogsadai.dqp.lqp.operators.QueryApplyOperator;
import uk.org.ogsadai.tuple.TupleMetadata;

/**
 * Builds activities for operator QUERY APPLY operator. The apply controls
 * a child table scan so that it is executed multple times with subsets of
 * the data that comes from the other input. This builder works in conjunction 
 * with the BatchTableScanBuilder for building the FILTERED TABLE SCAN operator.
 *
 * @author The OGSA-DAI Project Team.
 */
public class BatchQueryApplyBuilder implements ActivityPipelineBuilder
{
    /** Copyright notice. */
    private static final String COPYRIGHT_NOTICE = 
        "Copyright (c) The University of Edinburgh, 2011";

    /** Join operator annotation for which side to read first. */
    private static final String READ_FIRST = "readFirst";
    
    /** Join operator annotation left hand metadata. */
    private static final String METADATA_LEFT = "metadataLeft";
    
    /** Join operator annotation right hand metadata. */
    private static final String METADATA_RIGHT = "metadataRight";

    /** Join operator annotation to signal repeated use. */
    private static final String REPEAT = "repeat";

    @Override
    public SingleActivityOutput build(
        Operator op,
        List<SingleActivityOutput> outputs, PipelineWorkflowBuilder builder)
            throws ActivityConstructionException
    {
        QueryApplyOperator operator = (QueryApplyOperator)op;

        int batchDataIndex = getFilteredTableScanIndex(operator);
        int dataIndex = 1 - batchDataIndex;
        
        // Construct the ListSplit activity
        GenericActivity listSplit =
            new GenericActivity("uk.org.ogsadai.ListSplit");
        listSplit.createInput("input");
        listSplit.createInput("size");
        listSplit.createOutput("output");
        listSplit.connectInput("input", outputs.get(dataIndex));
        listSplit.addInput("size", operator.getBatchSize());
        
        GenericActivity tee = 
            new GenericActivity("uk.org.ogsadai.Tee");
        tee.createInput("input");
        tee.createOutput("output", 2);
        tee.connectInput("input", listSplit.getOutput("output"));

        // Build the join operator
        List<SingleActivityOutput> joinInputs = 
            new ArrayList<SingleActivityOutput>(2);
        TupleMetadata metadataLeft = 
            operator.getChild(0).getHeading().getTupleMetadata();
        TupleMetadata metadataRight = 
            operator.getChild(1).getHeading().getTupleMetadata();
        
        // output from ListSplit must be read first by the join activity
        // otherwise the workflow may deadlock
        joinInputs.add(tee.getOutput("output",0));
        joinInputs.add(outputs.get(batchDataIndex));
        
        Operator joinOperator = operator.getOperator();
        joinOperator.addAnnotation(READ_FIRST, "left");
        joinOperator.addAnnotation(METADATA_LEFT, metadataLeft);
        joinOperator.addAnnotation(METADATA_RIGHT, metadataRight);
        joinOperator.addAnnotation(REPEAT, true);
        SingleActivityOutput joinOutput = 
            builder.buildOperator(joinOperator, joinInputs);

        // Construct the ToSingleList activity
        GenericActivity toSingleList =
            new GenericActivity("uk.org.ogsadai.ToSingleList");
        toSingleList.createInput("input");
        toSingleList.createOutput("output");
        toSingleList.connectInput("input", joinOutput);
        
        // Construct GroupBy activity
        GenericActivity groupBy =
            new GenericActivity("uk.org.ogsadai.GroupBy");
        groupBy.createInput("data");
        groupBy.createInput("columnIds");
        groupBy.createInput("aggregates");
        groupBy.createInput("resultColumnNames");
        groupBy.createOutput("result");

        // Connect columnIds input
        GenericActivity columnIdsControlledRepeat = 
            BuilderUtils.createControlledRepeat();
        columnIdsControlledRepeat.connectInput(
            "input",tee.getOutput("output",1));
        columnIdsControlledRepeat.addInputList(
            "repeatedInput", new String[] {} );
        groupBy.connectInput(
            "columnIds", 
            columnIdsControlledRepeat.getOutput("repeatedOutput"));
        
        // Connect aggregates input
        GenericActivity aggregatesControlledRepeat = 
            BuilderUtils.createControlledRepeat();
        aggregatesControlledRepeat.connectInput(
            "input",columnIdsControlledRepeat.getOutput("output"));
        aggregatesControlledRepeat.addInputList(
            "repeatedInput", operator.getAggregateExpressions());
        groupBy.connectInput(
            "aggregates", 
            aggregatesControlledRepeat.getOutput("repeatedOutput"));
        
        // Connect resultColumnNames input
        GenericActivity resultColumnNamesControlledRepeat = 
            BuilderUtils.createControlledRepeat();
        resultColumnNamesControlledRepeat.connectInput(
            "input",aggregatesControlledRepeat.getOutput("output"));
        resultColumnNamesControlledRepeat.addInputList(
            "repeatedInput", operator.getAggregateNames());
        groupBy.connectInput(
            "resultColumnNames", 
            resultColumnNamesControlledRepeat.getOutput("repeatedOutput"));
        
        // Connect the data input
        groupBy.connectInput(
            "data", 
            resultColumnNamesControlledRepeat.getOutput("output"));

        // Connect the GroupBy activity to the external input of the filtered
        // table scan operator
        Operator filteredTableScanOp = getFilteredTableScanOperator(operator);
        builder.addOutput(
            operator, filteredTableScanOp, groupBy.getOutput("result"));
        
        // Add all activities to the builder
        builder.add(listSplit);
        builder.add(tee);
        builder.add(toSingleList);
        builder.add(groupBy);
        builder.add(columnIdsControlledRepeat);
        builder.add(aggregatesControlledRepeat);
        builder.add(resultColumnNamesControlledRepeat);
        
        return toSingleList.getOutput("output");
    }
    
    /**
     * Gets which child of this operator is a filtered table scan.
     *  
     * @param op operator
     * 
     * @return 0 if the first child branch contains the filtered table scan, 
     * 1 if it is the second child branch.
     * 
     * @throws ActivityConstructionException if no filtered table scan operator
     * is found.
     */
    private static int getFilteredTableScanIndex(Operator op) 
        throws ActivityConstructionException
    {
        for (int i=0; i<2; i++)
        {
            Operator current = op.getChild(i);
            while(current != null && !current.isBinary())
            {
                if (current.getID() == OperatorID.FILTERED_TABLE_SCAN)
                {
                    return i;
                }
                if (current.getChildCount() > 0)
                {
                    current = current.getChild(0);
                }
                else
                {
                    current = null;
                }
            }
        }
        
        throw new ActivityConstructionException(
            new Exception("No filtered table scan operator found."));    
    }

    /**
     * Gets the filtered table scan operator associated with the given operator.
     * 
     * @param op operator
     * 
     * @return filtered table scan operator
     * 
     * @throws ActivityConstructionException
     */
    private static Operator getFilteredTableScanOperator(Operator op)
        throws ActivityConstructionException
    {
        for (int i=0; i<2; i++)
        {
            Operator current = op.getChild(i);
            while(current != null && !current.isBinary())
            {
                if (current.getID() == OperatorID.FILTERED_TABLE_SCAN)
                {
                    return current;
                }
                if (current.getChildCount() > 0)
                {
                    current = current.getChild(0);
                }
                else
                {
                    current = null;
                }
            }
        }
        
        throw new ActivityConstructionException(
            new Exception("No filtered table scan operator found."));    
    }
}
