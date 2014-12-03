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

package uk.org.ogsadai.dqp.lqp.optimiser.implosion;

import java.util.List;

import uk.org.ogsadai.dqp.common.CompilerConfiguration;
import uk.org.ogsadai.dqp.common.RequestDetails;
import uk.org.ogsadai.dqp.lqp.Operator;
import uk.org.ogsadai.dqp.lqp.OperatorID;
import uk.org.ogsadai.dqp.lqp.exceptions.LQPException;
import uk.org.ogsadai.dqp.lqp.operators.OneRowOnlyOperator;
import uk.org.ogsadai.dqp.lqp.optimiser.Optimiser;
import uk.org.ogsadai.dqp.lqp.optimiser.OptimiserUtils;
import uk.org.ogsadai.resource.dataresource.dqp.RequestDQPFederation;

/**
 * Pulls ONE_ROW_ONLY up the tree. Will pass any unary operator and stop on the
 * first encountered binary opeator.
 * 
 * @author The OGSA-DAI Project Team.
 */
class OneRowOnlyPullUpOptimiser implements Optimiser
{

    @Override
    public Operator optimise(Operator lqpRoot,
	    RequestDQPFederation requestFederation,
	    CompilerConfiguration compilerConfiguration,
	    RequestDetails requestDetails) throws LQPException
    {
	List<Operator> operators = OptimiserUtils.findOccurrences(lqpRoot,
		OperatorID.ONE_ROW_ONLY);

	for(Operator o : operators)
	{
	    pullUp((OneRowOnlyOperator) o);
	}
	return lqpRoot;
    }
    
    /**
     * Pulls ONE_ROW_ONLY up the tree.
     * 
     * @param operator
     *            ONE_ROW_ONLY operator
     * @throws LQPException
     */
    private void pullUp(OneRowOnlyOperator operator) throws LQPException
    {
	Operator parent = operator.getParent();

	if (parent.isBinary())
	{
	    return;
	}
	else
	{
	    pullUp((OneRowOnlyOperator) OptimiserUtils
		    .pullPastNoChange(operator));
	}
    }

}
