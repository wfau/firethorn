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

package uk.org.ogsadai.dqp.execute.workflow;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import uk.org.ogsadai.client.toolkit.SingleActivityOutput;
import uk.org.ogsadai.client.toolkit.activities.generic.GenericActivity;
import uk.org.ogsadai.common.msgs.DAILogger;
import uk.org.ogsadai.data.DataValue;
import uk.org.ogsadai.data.IntegerData;
import uk.org.ogsadai.data.StringData;
import uk.org.ogsadai.dqp.execute.ActivityConstructionException;
import uk.org.ogsadai.dqp.lqp.Attribute;
import uk.org.ogsadai.dqp.lqp.Operator;
import uk.org.ogsadai.dqp.lqp.operators.InnerThetaJoinOperator;
import uk.org.ogsadai.tuple.ColumnMetadata;
import uk.org.ogsadai.tuple.TupleMetadata;

/**
 * Builds activities for operator QUERY APPLY operator. The apply controls
 * a child table scan so that it is executed multple times with subsets of
 * the data that comes from the other input. This builder works in conjunction 
 * with the BatchTableScanBuilder for building the FILTERED TABLE SCAN operator.
 *
 * @author The OGSA-DAI Project Team.
 */
public class BatchJoinBuilder implements ActivityPipelineBuilder
{
    public static final DAILogger LOG = 
        DAILogger.getLogger(BatchJoinBuilder.class);
    
    /** Copyright notice. */
    private static final String COPYRIGHT_NOTICE = 
        "Copyright (c) The University of Edinburgh, 2012";

    // default batch size 
    private DataValue mBatchSize = new IntegerData(100);
    
    public void setBatchSize(int batchSize)
    {
        mBatchSize = new IntegerData(batchSize);
    }

    @Override
    public SingleActivityOutput build(
        Operator op,
        List<SingleActivityOutput> outputs, PipelineWorkflowBuilder builder)
        throws ActivityConstructionException
    {
        InnerThetaJoinOperator operator = (InnerThetaJoinOperator)op;
        
        int batchDataIndex = (Integer)operator.getAnnotation("branch");
        int dataIndex = 1 - batchDataIndex;
        TupleMetadata batchMetadata = 
            operator.getChild(batchDataIndex).getHeading().getTupleMetadata();
        TupleMetadata readFirstMetadata =
            operator.getChild(dataIndex).getHeading().getTupleMetadata();

        
        // Construct the ListSplit activity
        GenericActivity listSplit =
            new GenericActivity("uk.org.ogsadai.ListSplit");
        listSplit.createInput("input");
        listSplit.createInput("size");
        listSplit.createOutput("output");
        listSplit.connectInput("input", outputs.get(dataIndex));
        listSplit.addInput("size", mBatchSize);
        
        GenericActivity tee = 
            new GenericActivity("uk.org.ogsadai.Tee");
        tee.createInput("input");
        tee.createOutput("output", 2);
        tee.connectInput("input", listSplit.getOutput("output"));

        // Build the join operator
        List<SingleActivityOutput> joinInputs = 
            new ArrayList<SingleActivityOutput>(2);
        
        // output from ListSplit must be read first by the join activity
        // otherwise the workflow may deadlock
        joinInputs.add(tee.getOutput("output",0));
        joinInputs.add(outputs.get(batchDataIndex));
        
        SingleActivityOutput joinOutput = buildJoinForRepeatedUse(
                operator, "data1", 0,
                readFirstMetadata, batchMetadata,
                joinInputs, builder); 

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
        
        @SuppressWarnings("unchecked")
        Set<Attribute> attrToBind = 
            (Set<Attribute>) operator.getAnnotation("attributesToBind");
//            intersect(
//                operator.getChild(dataIndex).getHeading().getAttributes(),
//                operator.getPredicate().getAttributes(),
//                AttributeMatchMode.NAME_AND_NULL_SOURCE);
            
        String[] aggregateExpressions = 
            new String[] {
                "STRING_AGGREGATE(" + attrToBind.iterator().next() + ")"};
        String[] aggregateNames = new String[] { "dqpBatchData_267276" };

        // Connect aggregates input
        GenericActivity aggregatesControlledRepeat = 
            BuilderUtils.createControlledRepeat();
        aggregatesControlledRepeat.connectInput(
            "input",columnIdsControlledRepeat.getOutput("output"));
        aggregatesControlledRepeat.addInputList(
            "repeatedInput", aggregateExpressions);
        groupBy.connectInput(
            "aggregates", 
            aggregatesControlledRepeat.getOutput("repeatedOutput"));
        
        // Connect resultColumnNames input
        GenericActivity resultColumnNamesControlledRepeat = 
            BuilderUtils.createControlledRepeat();
        resultColumnNamesControlledRepeat.connectInput(
            "input",aggregatesControlledRepeat.getOutput("output"));
        resultColumnNamesControlledRepeat.addInputList(
            "repeatedInput", aggregateNames);
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
    

    private static Operator getFilteredTableScanOperator(Operator op) 
        throws ActivityConstructionException
    {
        Operator tableScan = (Operator)op.getAnnotation("tableScan");
        if (tableScan == null)
        {
            throw new ActivityConstructionException(
                    new Exception("No filtered table scan operator found."));
        }
        return tableScan;
    }

    /**
     * Builds the theta join for repeated use. This is required when the theta
     * join will be invoked multiple times as part of a batch join execution.
     * When the theta join is invoked multiple times we require more controlled
     * repeaters to coordinate the inputs.
     * 
     * @param operator
     *            LQP operator for which to build activities
     * @param readFirst
     *            the side which is read first by the join activity
     * @param controllingOutput
     *            the output which controls the repeated expression
     * @param outputs
     *            outputs from the children of the operator which have already
     *            been built
     * @param builder
     *            pipeline workflow builder
     * 
     * @return the unconnected activity output
     * 
     * @throws ActivityConstructionException
     */
    private SingleActivityOutput buildJoinForRepeatedUse(
            InnerThetaJoinOperator operator,
            String readFirst,
            int controllingOutput,
            TupleMetadata metadataLeft, TupleMetadata metadataRight,
            List<SingleActivityOutput> outputs,
            PipelineWorkflowBuilder builder) 
    throws ActivityConstructionException
    {
        String sql = operator.getPredicate().toString();

        GenericActivity conditionRepeater = 
            BuilderUtils.createControlledRepeat();
        builder.add(conditionRepeater);
        GenericActivity readFirstRepeater = 
            BuilderUtils.createControlledRepeat();
        builder.add(readFirstRepeater);
        
        SingleActivityOutput dataOutputs[] = new SingleActivityOutput[]{ 
            outputs.get(0), outputs.get(1)
        };

        conditionRepeater.connectInput("input", outputs.get(controllingOutput));
        conditionRepeater.addInput("repeatedInput", sql );
        
        readFirstRepeater.connectInput(
            "input", conditionRepeater.getOutput("output"));
        readFirstRepeater.addInput("repeatedInput", readFirst);
        
        GenericActivity join =
            new GenericActivity("uk.org.ogsadai.TupleThetaJoin");
        join.createInput("data1");
        join.createInput("data2");
        join.createInput("condition");
        join.createOutput("result", GenericActivity.LIMITED_VALIDATION);

        dataOutputs[controllingOutput] = 
            addMetadataForRepeatedUse(
                metadataLeft, metadataRight, 
                join, 
                builder, 
                readFirstRepeater.getOutput("output"));
        
        join.connectInput("data1", dataOutputs[0]);
        join.connectInput("data2", dataOutputs[1]);
        
        join.connectInput(
            "condition", conditionRepeater.getOutput("repeatedOutput"));
        
        join.createInput("readFirst");
        join.connectInput(
            "readFirst", readFirstRepeater.getOutput("repeatedOutput"));
        builder.add(join);
        
        return join.getOutput("result");
    }
    
    /**
     * Adds the tuple metadata as specified in the metadata annotation.
     * 
     * @param operator inner theta join operator
     * 
     * @param join join activity
     * 
     * @param builder
     *          pipeline workflow builder
     *          
     * @param controllerOutput
     *          output of the controller used to control the metadata
     *          
     * @return
     *          output that now returns the data that as was obtained from the
     *          controllerOutput
     */
    private SingleActivityOutput addMetadataForRepeatedUse(
            TupleMetadata left, TupleMetadata right,
            GenericActivity join,
            PipelineWorkflowBuilder builder,
            SingleActivityOutput controllerOutput)
    {
        SingleActivityOutput resultOutput = controllerOutput;
        
        String metadata = convertMetadata(left);
        join.createInput("data1Metadata");
        GenericActivity repeater = BuilderUtils.createControlledRepeat();
        builder.add(repeater);
        repeater.connectInput("input", resultOutput);
        repeater.addInput("repeatedInput", new StringData(metadata));
        resultOutput = repeater.getOutput("output");
        join.connectInput(
                "data1Metadata", repeater.getOutput("repeatedOutput"));

        metadata = convertMetadata(right);
        join.createInput("data2Metadata");
        repeater = BuilderUtils.createControlledRepeat();
        builder.add(repeater);
        repeater.connectInput("input", resultOutput);
        repeater.addInput("repeatedInput", new StringData(metadata));
        resultOutput = repeater.getOutput("output");
        join.connectInput(
                "data2Metadata", repeater.getOutput("repeatedOutput"));
        
        return resultOutput;
    }

    /**
     * Converts a <code>TupleMetadata</tt> object into a string representation
     * that can be passed to a join activity.
     * 
     * @param metadata metadata
     * 
     * @return metadata rendered as a string suitable for the join activity
     */
    private String convertMetadata(TupleMetadata metadata)
    {
        StringBuilder result = new StringBuilder();
        for (int i=0; i<metadata.getColumnCount(); i++)
        {
            ColumnMetadata column = metadata.getColumnMetadata(i);
            result.append(column.getName());
            result.append("\n");
            result.append(column.getTableName());
            result.append("\n");
            result.append(column.getResourceID());
            result.append("\n");
            result.append(column.getDRES());
            result.append("\n");
            result.append(column.getType());
            result.append("\n");
            result.append(column.isNullable());
            result.append("\n");
            result.append(column.getPrecision());
            result.append("\n");
            result.append(column.getColumnDisplaySize());
            result.append("\n");
        }
        return result.toString();
    }

    
//    /**
//     * Intersects a list and a collection. Returns a set of those entries in the
//     * list that are contained on the collection.
//     * 
//     * @param list
//     *            list
//     * @param collection
//     *            collection
//     * @param attributeMatchMode
//     *            attribute match mode
//     * 
//     * @return intersection of the list and the collection
//     */
//    private Set<Attribute> intersect(
//        List<Attribute> list,
//        Collection<Attribute> collection,
//        AttributeMatchMode attributeMatchMode)
//    {
//        Set<Attribute> intersection = new HashSet<Attribute>();
//        for (Attribute a1 : list)
//        {
//            boolean found = false;
//            for (Attribute a2 : collection)
//            {
//                if (a1.equals(a2, attributeMatchMode))
//                {
//                    found = true;
//                    break;
//                }
//            }
//            if (found)
//            {
//                intersection.add(a1);
//            }
//        }
//        return intersection;
//    }

}
