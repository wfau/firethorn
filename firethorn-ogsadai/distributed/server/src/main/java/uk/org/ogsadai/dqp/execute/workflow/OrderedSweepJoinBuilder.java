package uk.org.ogsadai.dqp.execute.workflow;

import java.util.List;

import uk.org.ogsadai.client.toolkit.SingleActivityOutput;
import uk.org.ogsadai.client.toolkit.activities.generic.GenericActivity;
import uk.org.ogsadai.data.DoubleData;
import uk.org.ogsadai.data.StringData;
import uk.org.ogsadai.dqp.execute.ActivityConstructionException;
import uk.org.ogsadai.dqp.lqp.Attribute;
import uk.org.ogsadai.dqp.lqp.Operator;
import uk.org.ogsadai.dqp.lqp.operators.InnerThetaJoinOperator;

public class OrderedSweepJoinBuilder implements ActivityPipelineBuilder 
{

    @Override
    public SingleActivityOutput build(
            Operator op,
            List<SingleActivityOutput> outputs,
            PipelineWorkflowBuilder builder)
        throws ActivityConstructionException 
    {
        InnerThetaJoinOperator operator = (InnerThetaJoinOperator)op;
        
        // Get annotation values
        Number size = (Number) operator.getAnnotation("PLANE_SWEEP_size");
        Attribute attr0 = (Attribute)operator.getAnnotation("PLANE_SWEEP_attr0");
        Attribute attr1 = (Attribute)operator.getAnnotation("PLANE_SWEEP_attr1");
        Number attr0ChildIndex = (Number) operator.getAnnotation("PLANE_SWEEP_attr0ChildIndex");
        
        // activity data output that contains attribute 0
        SingleActivityOutput data1 = outputs.get(attr0ChildIndex.intValue());
        // activity data output that contains attribute 1
        SingleActivityOutput data2 = outputs.get(1-attr0ChildIndex.intValue());

        // Build activity
        GenericActivity orderedSweepJoin  = 
            new GenericActivity("uk.org.ogsadai.TupleSweepProduct");
        orderedSweepJoin.createInput("data1");
        orderedSweepJoin.createInput("data2");
        orderedSweepJoin.createInput("column1");
        orderedSweepJoin.createInput("column2");
        orderedSweepJoin.createInput("radius");
        orderedSweepJoin.createOutput("result", GenericActivity.LIMITED_VALIDATION);

        orderedSweepJoin.connectInput("data1", data1);
        orderedSweepJoin.connectInput("data2", data2);
        orderedSweepJoin.addInput("radius", new DoubleData(size.doubleValue()/2));
        orderedSweepJoin.addInput("column1", new StringData(attr0.toString()));
        orderedSweepJoin.addInput("column2", new StringData(attr1.toString()));
        
        // Add activity to builder
        builder.add(orderedSweepJoin);

        return orderedSweepJoin.getOutput("result");
    }

}
