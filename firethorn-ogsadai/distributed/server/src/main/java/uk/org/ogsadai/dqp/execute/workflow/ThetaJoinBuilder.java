// Copyright (c) The University of Edinburgh, 2008 - 2009.
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

import java.util.List;

import uk.org.ogsadai.client.toolkit.SingleActivityOutput;
import uk.org.ogsadai.client.toolkit.activities.generic.GenericActivity;
import uk.org.ogsadai.data.StringData;
import uk.org.ogsadai.dqp.execute.ActivityConstructionException;
import uk.org.ogsadai.dqp.lqp.Operator;
import uk.org.ogsadai.dqp.lqp.operators.InnerThetaJoinOperator;
import uk.org.ogsadai.tuple.ColumnMetadata;
import uk.org.ogsadai.tuple.TupleMetadata;

/**
 * Builds activities for operator INNER THETA JOIN.
 *
 * @author The OGSA-DAI Project Team.
 */
public class ThetaJoinBuilder implements ActivityPipelineBuilder
{
    /** Copyright notice. */
    private static final String COPYRIGHT_NOTICE = 
        "Copyright (c) The University of Edinburgh, 2008 - 2009";

    /**
     * {@inheritDoc}
     */
    public SingleActivityOutput build(
        Operator op, 
        List<SingleActivityOutput> outputs,
        PipelineWorkflowBuilder builder) 
    throws ActivityConstructionException
    {
        InnerThetaJoinOperator operator = (InnerThetaJoinOperator)op;
        Object repeatAnno = operator.getAnnotation("repeat");
        boolean repeat = (repeatAnno != null && ((Boolean)repeatAnno));

        if (repeat)
        {
            return buildForRepeatedUse(operator, outputs, builder);
        }
        else
        {
            return buildForSingleUse(operator, outputs, builder);
        }
    }
    
    /**
     * Builds the theta join for single use. Most theta joins will only 
     * be used once so this is the default option. 
     * 
     * @param op 
     *          LQP operator for which to build activities
     * @param outputs
     *          outputs from the children of the operator which have already
     *          been built
     * 
     * @param builder
     *          pipeline workflow builder
     * 
     * @return the unconnected activity output
     * 
     * @throws ActivityConstructionException
     */
    public SingleActivityOutput buildForSingleUse(
            Operator op, 
            List<SingleActivityOutput> outputs,
            PipelineWorkflowBuilder builder) 
        throws ActivityConstructionException
    {
        InnerThetaJoinOperator operator = (InnerThetaJoinOperator)op;
        SingleActivityOutput outputLeft = outputs.get(0);
        SingleActivityOutput outputRight = outputs.get(1);
        
        String sql = operator.getPredicate().toString();
        
        String joinActivityName = "uk.org.ogsadai.TupleThetaJoin";
        if (operator.getAnnotation("range") != null)
        {
            joinActivityName = "uk.org.ogsadai.TupleThetaJoinWithRange";
        }
        
        GenericActivity join = new GenericActivity(joinActivityName);
        join.createInput("data1");
        join.createInput("data2");
        join.createInput("condition");
        join.createOutput("result", GenericActivity.LIMITED_VALIDATION);
        addMetadata(operator, join);
        
        join.connectInput("data1", outputLeft);
        join.connectInput("data2", outputRight);
        join.addInput("condition", sql);
        
        // indicate which input must be read first
        String readFirst = (String)operator.getAnnotation("readFirst");
        if (readFirst == null || readFirst.equals("left"))
        {
            readFirst = "data1";
        }
        else
        {
            readFirst = "data2";
        }
        join.createInput("readFirst");
        join.addInput("readFirst", readFirst);
        builder.add(join);
        
        return join.getOutput("result");
    }

    /**
     * Builds the theta join for repeated use. This is required when the
     * theta join will be invoked multiple times as part of a batch join
     * execution.  When the theta join is invoked multiple times we require
     * more controlled repeaters to coordinate the inputs. 
     * 
     * @param op 
     *          LQP operator for which to build activities
     * @param outputs
     *          outputs from the children of the operator which have already
     *          been built
     * 
     * @param builder
     *          pipeline workflow builder
     * 
     * @return the unconnected activity output
     * 
     * @throws ActivityConstructionException
     */
    public SingleActivityOutput buildForRepeatedUse(
        Operator op, 
        List<SingleActivityOutput> outputs,
        PipelineWorkflowBuilder builder) 
    throws ActivityConstructionException
    {
        InnerThetaJoinOperator operator = (InnerThetaJoinOperator)op;
        
        // indicate which input must be read first
        String readFirst = (String)operator.getAnnotation("readFirst");
        int controllingOutput;
        if (readFirst == null || readFirst.equals("left"))
        {
            readFirst = "data1";
            controllingOutput = 0;
        }
        else
        {
            readFirst = "data2";
            controllingOutput = 1;
        }

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
                operator, join, builder, readFirstRepeater.getOutput("output"));
        
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
            InnerThetaJoinOperator operator,
            GenericActivity join,
            PipelineWorkflowBuilder builder,
            SingleActivityOutput controllerOutput)
    {
        SingleActivityOutput resultOutput = controllerOutput;
        
        TupleMetadata left = 
            (TupleMetadata)operator.getAnnotation("metadataLeft");
        if (left != null)
        {
            String metadata = convertMetadata(left);
            join.createInput("data1Metadata");
            GenericActivity repeater = BuilderUtils.createControlledRepeat();
            builder.add(repeater);
            repeater.connectInput("input", resultOutput);
            repeater.addInput("repeatedInput", new StringData(metadata));
            resultOutput = repeater.getOutput("output");
            join.connectInput(
                "data1Metadata", repeater.getOutput("repeatedOutput"));
        }
        TupleMetadata right =  
            (TupleMetadata)operator.getAnnotation("metadataRight");
        if (right != null)
        {
            String metadata = convertMetadata(right);
            join.createInput("data2Metadata");
            GenericActivity repeater = BuilderUtils.createControlledRepeat();
            builder.add(repeater);
            repeater.connectInput("input", resultOutput);
            repeater.addInput("repeatedInput", new StringData(metadata));
            resultOutput = repeater.getOutput("output");
            join.connectInput(
                "data2Metadata", repeater.getOutput("repeatedOutput"));
        }
        return resultOutput;
    }

    /**
     * Adds the tuple metadata as specified in the metadata annotation.
     * 
     * @param operator inner theta join operator
     * 
     * @param join join activity
     */
    private void addMetadata(
        InnerThetaJoinOperator operator,
        GenericActivity join)
    {
        TupleMetadata left = 
            (TupleMetadata)operator.getAnnotation("metadataLeft");
        if (left != null)
        {
            String metadata = convertMetadata(left);
            join.createInput("data1Metadata");
            join.addInput("data1Metadata", new StringData(metadata));
        }
        TupleMetadata right =  
            (TupleMetadata)operator.getAnnotation("metadataRight");
        if (right != null)
        {
            String metadata = convertMetadata(right);
            join.createInput("data2Metadata");
            join.addInput("data2Metadata", new StringData(metadata));
        }
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
}