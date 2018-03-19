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

import java.util.List;

import uk.org.ogsadai.client.toolkit.SingleActivityOutput;
import uk.org.ogsadai.client.toolkit.activities.generic.GenericActivity;
import uk.org.ogsadai.common.msgs.DAILogger;
import uk.org.ogsadai.data.ListBegin;
import uk.org.ogsadai.data.ListEnd;
import uk.org.ogsadai.data.StringData;
import uk.org.ogsadai.dqp.execute.ActivityConstructionException;
import uk.org.ogsadai.dqp.lqp.Attribute;
import uk.org.ogsadai.dqp.lqp.Operator;
import uk.org.ogsadai.dqp.lqp.operators.InnerThetaJoinOperator;
import uk.org.ogsadai.expression.ArithmeticExpressionOperand;
import uk.org.ogsadai.expression.EqualExpression;
import uk.org.ogsadai.expression.arithmetic.TableColumn;
import uk.org.ogsadai.tuple.ColumnMetadata;
import uk.org.ogsadai.tuple.TupleMetadata;

/**
 * Builds activities for a merged join. A merged join is where both sides 
 * of the join are sorted by the attributes that take part in the equi-join. 
 * The builder makes use of TupleMergeJoin activity.
 *
 * @author The OGSA-DAI Project Team.
 */
public class MergeJoinBuilder implements ActivityPipelineBuilder
{
    /** Copyright notice. */
    private static final String COPYRIGHT_NOTICE = 
        "Copyright (c) The University of Edinburgh, 2010";

    /** Logger. */
    private static final DAILogger LOG =
        DAILogger.getLogger(MergeJoinBuilder.class);
    
    /**
     * {@inheritDoc}
     */
    public SingleActivityOutput build(
            Operator op,
            List<SingleActivityOutput> outputs,
            PipelineWorkflowBuilder builder) 
        throws ActivityConstructionException
    {
        // Get left and right column names
        InnerThetaJoinOperator operator = (InnerThetaJoinOperator)op;
        EqualExpression expr = (EqualExpression)operator.getPredicate().getExpression();
        
        Attribute[] expressionAttr = new Attribute[2];
        expressionAttr[0] = operator.getPredicate().getLHSAttributes().iterator().next();
        expressionAttr[1] = operator.getPredicate().getRHSAttributes().iterator().next();

        ArithmeticExpressionOperand leftOp = null;
        ArithmeticExpressionOperand rightOp = null;

        if ( operator.getChild(0).getHeading().contains(expressionAttr[0])) {
            leftOp = (ArithmeticExpressionOperand)expr.getLeftOperand();
            rightOp = (ArithmeticExpressionOperand)expr.getRightOperand();
        }else {
            leftOp = (ArithmeticExpressionOperand)expr.getRightOperand();
            rightOp = (ArithmeticExpressionOperand)expr.getLeftOperand();
        }
        ColumnMetadata leftColumn = ((TableColumn)leftOp.getExpression()).getMetadata(); 
        ColumnMetadata rightColumn = ((TableColumn)rightOp.getExpression()).getMetadata();

        // Get outputs
        SingleActivityOutput outputLeft = outputs.get(0);
        SingleActivityOutput outputRight = outputs.get(1);
        
        // Create inputs and outputs for the tuple merge join activity
        GenericActivity tmj  = 
            new GenericActivity("uk.org.ogsadai.TupleMergeJoin");
        tmj.createInput("data1");
        tmj.createInput("data2");
        tmj.createInput("columnIds1");
        tmj.createInput("columnIds2");
        tmj.createOutput("result", GenericActivity.LIMITED_VALIDATION);
        addMetadata(operator, tmj);
        
        // Set inputs for activity
        tmj.connectInput("data1", outputLeft);
        tmj.connectInput("data2", outputRight);
        tmj.addInput("columnIds1", ListBegin.VALUE);
        tmj.addInput("columnIds1", leftColumn.getName());
        tmj.addInput("columnIds1", ListEnd.VALUE);
        tmj.addInput("columnIds2", ListBegin.VALUE);
        tmj.addInput("columnIds2", rightColumn.getName());
        tmj.addInput("columnIds2", ListEnd.VALUE);
        
        // Add activity to builder
        builder.add(tmj);

        return tmj.getOutput("result");
    }
    
    /**
     * Adds metadata inputs to the given join activity if the operator has
     * <tt>metadataLeft</tt> or <tt>metdataRight</tt> annotations.
     * 
     * @param operator inner theta-join operator
     * @param join     join activity
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
