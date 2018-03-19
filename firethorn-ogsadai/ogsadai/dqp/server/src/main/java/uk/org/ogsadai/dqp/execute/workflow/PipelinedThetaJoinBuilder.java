package uk.org.ogsadai.dqp.execute.workflow;

import java.util.List;

import uk.org.ogsadai.activity.relational.PipelinedTupleJoinActivity;
import uk.org.ogsadai.client.toolkit.SingleActivityOutput;
import uk.org.ogsadai.client.toolkit.activities.generic.GenericActivity;
import uk.org.ogsadai.dqp.execute.ActivityConstructionException;
import uk.org.ogsadai.dqp.lqp.Operator;
import uk.org.ogsadai.dqp.lqp.operators.InnerThetaJoinOperator;

public class PipelinedThetaJoinBuilder implements ActivityPipelineBuilder
{
    /** Copyright notice. */
    private static final String COPYRIGHT_NOTICE =
        "Copyright (c) The University of Edinburgh, 2008 - 2009";

    /**
     * {@inheritDoc}
     */
    public SingleActivityOutput build(Operator op,
        List<SingleActivityOutput> outputs, PipelineWorkflowBuilder builder)
        throws ActivityConstructionException
    {
        InnerThetaJoinOperator operator = (InnerThetaJoinOperator) op;
        SingleActivityOutput outputLeft = outputs.get(0);
        SingleActivityOutput outputRight = outputs.get(1);

        String sql = operator.getPredicate().toString();
        GenericActivity join =
            new GenericActivity("uk.org.ogsadai.PipelinedThetaJoin");
        
        join.createInput(PipelinedTupleJoinActivity.INPUT_DATA1);
        join.createInput(PipelinedTupleJoinActivity.INPUT_DATA2);
        join.createInput(PipelinedTupleJoinActivity.INPUT_CONDITION);
        join.createOutput(PipelinedTupleJoinActivity.OUTPUT_RESULT,
            GenericActivity.LIMITED_VALIDATION);

        join.connectInput(PipelinedTupleJoinActivity.INPUT_DATA1, outputLeft);
        join.connectInput(PipelinedTupleJoinActivity.INPUT_DATA2, outputRight);
        join.addInput(PipelinedTupleJoinActivity.INPUT_CONDITION, sql);

        builder.add(join);

        return join.getOutput(PipelinedTupleJoinActivity.OUTPUT_RESULT);
    }
}