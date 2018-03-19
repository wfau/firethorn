// Copyright (c) The University of Edinburgh, 2009.
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

package uk.org.ogsadai.dqp.lqp.optimiser.join;

import uk.org.ogsadai.dqp.common.CompilerConfiguration;
import uk.org.ogsadai.dqp.common.RequestDetails;
import uk.org.ogsadai.dqp.lqp.Annotation;
import uk.org.ogsadai.dqp.lqp.Heading;
import uk.org.ogsadai.dqp.lqp.Operator;
import uk.org.ogsadai.dqp.lqp.OperatorID;
import uk.org.ogsadai.dqp.lqp.exceptions.LQPException;
import uk.org.ogsadai.dqp.lqp.operators.InnerThetaJoinOperator;
import uk.org.ogsadai.dqp.lqp.optimiser.Optimiser;
import uk.org.ogsadai.expression.Expression;
import uk.org.ogsadai.resource.dataresource.dqp.RequestDQPFederation;

/**
 * Annotates inner theta join operators so that the builder can choose the
 * appropriate implementation.
 * 
 * @author The OGSA-DAI Project Team.
 */
public class OLD_JoinAnnotation implements Optimiser
{
    /** Copyright notice. */
    private static final String COPYRIGHT_NOTICE = 
        "Copyright (c) The University of Edinburgh, 2009";
    
    /** There is a primary expression in the join condition. */
    public static final String PRIMARY_EXPRESSION = "PRIMARY_EXPRESSION";
    /** There is a primary expression in the join condition which can be 
     *  optimised for an ordered merge join strategy. */
    public static final String ORDERED_MERGE_JOIN = "ORDERED_MERGE_JOIN";
    /** There is no primary expression in the join condition. */
    public static final String NO_PRIMARY_EXPRESSION = "NO_PRIMARY_EXPRESSION";

    /**
     * {@inheritDoc}
     */
    public Operator optimise(
        Operator lqpRoot, 
        RequestDQPFederation requestFederation,
        CompilerConfiguration compilerConfiguration, 
        RequestDetails requestDetails) throws LQPException
    {
        chooseJoinAnnotation(lqpRoot);
        return lqpRoot;
    }

    /**
     * Walks the subtree with the given root and annotates all inner theta join
     * operators.
     * 
     * @param operator
     *            root of the subtree
     * @throws LQPException 
     */
    private void chooseJoinAnnotation(Operator operator) throws LQPException
    {
        if (operator.getID() == OperatorID.INNER_THETA_JOIN)
        {
            annotate((InnerThetaJoinOperator)operator);
        }
        Operator child = operator.getChild(0);
        if (child != null)
        {
            chooseJoinAnnotation(child);
            if (operator.isBinary())
            {
                Operator otherChild = operator.getChild(1);
                chooseJoinAnnotation(otherChild);
            }
        }
    }

    /**
     * Annotates an inner theta join operator.
     * 
     * @param operator
     *            the operator
     * @throws LQPException
     *             if there was a problem building the join expression
     */
    private void annotate(InnerThetaJoinOperator operator) throws LQPException
    {
        Expression expression = operator.getPredicate()
            .getExpression();

        PrimaryExpressionValidator extractor = new PrimaryExpressionValidator();
        Heading left = operator.getChild(0).getHeading();
        Heading right = operator.getChild(1).getHeading();
        boolean primaryExpressionExists = extractor.validate(expression, left
            .getAttributes(), right.getAttributes());
        if (primaryExpressionExists)
        {
            // primary expression exists so choose a theta join
            Annotation.addImplementationAnnotation(operator, PRIMARY_EXPRESSION);
        }
            
        // Annotatate to say which size to read first
        if (Annotation.getCardinalityAnnotation(operator.getChild(0)) < 
            Annotation.getCardinalityAnnotation(operator.getChild(1)))
        {
            // read left side into memory
            Annotation.addReadFirstAnnotation(operator, "left");
        }
        else
        {
            // read right side into memory
            Annotation.addReadFirstAnnotation(operator, "right");
        }
    }

}
