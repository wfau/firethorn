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

package uk.org.ogsadai.dqp.execute.workflow;

import uk.org.ogsadai.client.toolkit.activities.generic.GenericActivity;
import uk.org.ogsadai.dqp.common.EvaluationNode;

/**
 * The subclass of GenericActivity that is used by DQP to represent a 
 * DeliverToDataSink activity.  The actual activity to use and its inputs
 * is dependent on the presentation layer so DQP uses instances this class
 * to represent these activities and presentation layer workflow transformers
 * will detect the use of this class and add the extra inputs appropriate to
 * the presentation layer being used.
 *
 * @author The OGSA-DAI Project Team
 */
public class DQPDeliverToDataSink extends GenericActivity
{
    /** Copyright notice. */
    private static final String COPYRIGHT_NOTICE = 
        "Copyright (c) The University of Edinburgh, 2009.";
    
    /** Evaluation node on which the data sink resource resides. */
    protected EvaluationNode mEvaluationNode;
    
    /**
     * Constructor.
     * 
     * @param activityName activity name
     */
    public DQPDeliverToDataSink(String activityName)
    {
        super(activityName);
    }
    
    /**
     * Sets the evaluation node on which the data sink resource resides.
     * 
     * @param evaluationNode 
     *    evaluation node on which the data sink resource resides.
     *   
     */
    public void setEvaluationNode(EvaluationNode evaluationNode)
    {
        mEvaluationNode = evaluationNode;
    }
    
    /**
     * Gets the evaluation node on which the data source resource resides.
     * 
     * @return evaluation node.
     */
    public EvaluationNode getEvaluationNode()
    {
        return mEvaluationNode;
    }
}
