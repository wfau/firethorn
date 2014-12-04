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

package uk.org.ogsadai.dqp.execute.workflow;

import java.util.LinkedList;
import java.util.List;

import uk.org.ogsadai.activity.ActivityName;
import uk.org.ogsadai.client.toolkit.activities.generic.GenericActivity;
import uk.org.ogsadai.client.toolkit.activities.generic.TraversableSingleActivityInput;
import uk.org.ogsadai.client.toolkit.activities.generic.TraversableSingleActivityOutput;
import uk.org.ogsadai.dqp.execute.ActivityConstructionException;
import uk.org.ogsadai.dqp.lqp.Operator;
import uk.org.ogsadai.dqp.lqp.OperatorID;
import uk.org.ogsadai.dqp.lqp.operators.ApplyOperator;
import uk.org.ogsadai.dqp.lqp.operators.TableScanOperator;

/**
 * Cross pipleine workflow builder associated with the APPLY operator.  This
 * builder adds the necessary activities and controlled repeaters to the
 * workflow corresponding to the correlated leaf TABLE_SCAN operator. 
 *
 * @author The OGSA-DAI Project Team.
 */
public class ApplyCrossPipelineWorkflowBuilder 
    implements CrossPipelineWorkflowBuilder
{
    /** Copyright notice. */
    private static final String COPYRIGHT_NOTICE = 
        "Copyright (c) The University of Edinburgh, 2010";
    
    /** SQLQueryActivity name. */
    private final ActivityName SQL_QUERY_ACTIVITY_NAME =
        new ActivityName("uk.org.ogsadai.SQLQuery");
    
    /** Apply operator. */ 
    private final ApplyOperator mApplyOperator;
    /** Table scan operator. */ 
    private final TableScanOperator mTableScanOperator;
    /** Hard side root operator. */ 
    private final Operator mHardSideRootOperator;

    
    public ApplyCrossPipelineWorkflowBuilder(
        ApplyOperator applyOperator,
        TableScanOperator tableScanOperator,
        Operator hardSideRootOperator)
    {
        mApplyOperator = applyOperator;
        mTableScanOperator = tableScanOperator;
        mHardSideRootOperator = hardSideRootOperator;
    }
        
    
    public void build(List<PipelineWorkflowBuilder> builders)
        throws ActivityConstructionException
    {
        PipelineWorkflowBuilder applyBuilder = 
            findBuilderForOperator(mApplyOperator, builders);
        PipelineWorkflowBuilder tableScanBuilder = 
            findBuilderForOperator(mTableScanOperator, builders);
     
        // Need to find literal inputs for all operators on the hard side
        // root that have the same builder as the the table scan
        List<TraversableSingleActivityInput> literalInputs =
            new LinkedList<TraversableSingleActivityInput>();
        
        // NB: This assumes the hard side operators all have max one child
        Operator op = mHardSideRootOperator;
        while( op != null)
        {
            // Wish to avoid exchange ops - for the moment we know we will
            // only have tablescan or rename
            if (op.getID() == OperatorID.RENAME || 
                op.getID() == OperatorID.TABLE_SCAN)
            {
                literalInputs.addAll(tableScanBuilder.getLiteralInputs(op));
            }
            if (op.getChildCount() > 0)
            {
                op = op.getChild(0);
            }
            else
            {
                op  = null;
            }
        }
        
        // Find the expression input
        TraversableSingleActivityOutput controllerOutput = null;
        List<TraversableSingleActivityInput> literalsToProcess = 
            new LinkedList<TraversableSingleActivityInput>();
        
        for (TraversableSingleActivityInput input : literalInputs)
        {
            if (input.getInputName().equals("expression") &&
                input.getActivity().getActivityName().equals(
                    SQL_QUERY_ACTIVITY_NAME))
            {
                GenericActivity sqlQuery = 
                    (GenericActivity) input.getActivity();
                
                GenericActivity sqlApplyBindings = 
                    new GenericActivity("uk.org.ogsadai.SQLApplyBindings");
                sqlApplyBindings.createInput("expression");
                sqlApplyBindings.createInput("bindings");
                sqlApplyBindings.createOutput("result");
                tableScanBuilder.add(sqlApplyBindings);
                
                GenericActivity controlledRepeat = 
                    new GenericActivity("uk.org.ogsadai.ControlledRepeat");
                controlledRepeat.createInput("repeatedInput");
                controlledRepeat.createInput("input");
                controlledRepeat.createOutput("repeatedOutput");
                controlledRepeat.createOutput("output");
                tableScanBuilder.add(controlledRepeat);
                
                controlledRepeat.addInput(
                    "repeatedInput", input.getDataValues());
                tableScanBuilder.addInput(
                    mApplyOperator, 
                    mTableScanOperator, 
                    controlledRepeat.getInput("input"));
                
                sqlApplyBindings.connectInput(
                    "expression", controlledRepeat.getOutput("repeatedOutput"));
                sqlApplyBindings.connectInput(
                    "bindings", controlledRepeat.getOutput("output"));
                controllerOutput = controlledRepeat.getOutput("repeatedOutput");
                
                sqlQuery.connectInput(
                    "expression", sqlApplyBindings.getOutput("result"));
            }
            else
            {
                literalsToProcess.add(input);
            }
        }
        
        for (TraversableSingleActivityInput input : literalsToProcess)
        {   
            GenericActivity controlledRepeat = 
                new GenericActivity("uk.org.ogsadai.ControlledRepeat");
            controlledRepeat.createInput("repeatedInput");
            controlledRepeat.createInput("input");
            controlledRepeat.createOutput("repeatedOutput");
            controlledRepeat.createOutput("output");
            tableScanBuilder.add(controlledRepeat);

            controlledRepeat.addInput("repeatedInput", input.getDataValues());
            controllerOutput.getConnectedInputs()[0].connect(
                controlledRepeat.getOutput("output"));
            
            controlledRepeat.connectInput("input", controllerOutput);
            input.connect(controlledRepeat.getOutput("repeatedOutput"));

            controllerOutput = controlledRepeat.getOutput("output");
        }
    }

    private static PipelineWorkflowBuilder findBuilderForOperator(
        Operator op,
        List<PipelineWorkflowBuilder> builders)
    {
        for (PipelineWorkflowBuilder builder : builders)
        {
            if (builder.hasBuiltOperator(op))
            {
                return builder;
            }
        }
        return null;
    }
}
