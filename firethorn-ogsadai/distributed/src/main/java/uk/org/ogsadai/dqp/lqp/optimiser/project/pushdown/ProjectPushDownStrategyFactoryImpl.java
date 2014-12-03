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

package uk.org.ogsadai.dqp.lqp.optimiser.project.pushdown;

import java.util.HashMap;
import java.util.Map;

import uk.org.ogsadai.dqp.lqp.Operator;
import uk.org.ogsadai.dqp.lqp.OperatorID;
import uk.org.ogsadai.dqp.lqp.optimiser.project.pushdown.strategies.ProjectPushDownCopyPastStrategy;
import uk.org.ogsadai.dqp.lqp.optimiser.project.pushdown.strategies.ProjectPushDownMergeWithChildProjectStrategy;
import uk.org.ogsadai.dqp.lqp.optimiser.project.pushdown.strategies.ProjectPushDownNoOpStrategy;
import uk.org.ogsadai.dqp.lqp.optimiser.project.pushdown.strategies.ProjectPushDownPastAnyStrategy;
import uk.org.ogsadai.dqp.lqp.optimiser.project.pushdown.strategies.ProjectPushDownPastRenameStrategy;
import uk.org.ogsadai.dqp.lqp.optimiser.project.pushdown.strategies.ProjectPushDownPastUnionStrategy;

/**
 * Strategy factory for pushing PROJECT operators down the query plan.
 *
 * @author The OGSA-DAI Project Team
 */
public class ProjectPushDownStrategyFactoryImpl 
    implements ProjectPushDownStrategyFactory
{
    /** Copyright notice. */
    private static final String COPYRIGHT_NOTICE = 
        "Copyright (c) The University of Edinburgh, 2009.";

    /** Strategies map. */
    private Map<OperatorID, ProjectPushDownStrategy> mOperatorToStrategyMap;

    // Strategies
    
    /** No-op strategy. */
    private static ProjectPushDownStrategy mNoOpStrategy = 
        new ProjectPushDownNoOpStrategy();
    /** Copy past an operator strategy. */
    private static ProjectPushDownStrategy mCopyPastStrategy = 
        new ProjectPushDownCopyPastStrategy();
    /** Copy past left hand child operator strategy. */
    private static ProjectPushDownStrategy mCopyPastLeftHandChildStrategy = 
        new ProjectPushDownCopyPastStrategy(1);
    /** Union strategy. */
    private static ProjectPushDownStrategy mUnionStrategy = 
        new ProjectPushDownPastUnionStrategy();
    /** Rename strategy. */
    private static ProjectPushDownStrategy mRenameStrategy = 
        new ProjectPushDownPastRenameStrategy();
    /** Project merge strategy. */
    private static ProjectPushDownStrategy mProjectStrategy = 
        new ProjectPushDownMergeWithChildProjectStrategy();
    /** Jump over strategy. */
    private static ProjectPushDownPastAnyStrategy mJumpOverStrategy = 
        new ProjectPushDownPastAnyStrategy();

    /**
     * Constructor.
     */
    public ProjectPushDownStrategyFactoryImpl()
    {
        mOperatorToStrategyMap = 
            new HashMap<OperatorID, ProjectPushDownStrategy>();
        Map<OperatorID, ProjectPushDownStrategy> stMap = mOperatorToStrategyMap;

        stMap.put(OperatorID.DUPLICATE_ELIMINATION, mNoOpStrategy);
        stMap.put(OperatorID.TABLE_SCAN, mNoOpStrategy);
        stMap.put(OperatorID.UNION, mUnionStrategy);
        stMap.put(OperatorID.INTERSECTION, mNoOpStrategy);
        stMap.put(OperatorID.DIFFERENCE, mNoOpStrategy);

        stMap.put(OperatorID.ONE_ROW_ONLY, mJumpOverStrategy);

        stMap.put(OperatorID.SELECT, mCopyPastStrategy);
        stMap.put(OperatorID.SORT, mJumpOverStrategy);

        stMap.put(OperatorID.FULL_OUTER_JOIN, mCopyPastStrategy);
        stMap.put(OperatorID.LEFT_OUTER_JOIN, mCopyPastStrategy);
        stMap.put(OperatorID.RIGHT_OUTER_JOIN, mCopyPastStrategy);
        stMap.put(OperatorID.INNER_THETA_JOIN, mCopyPastStrategy);
        stMap.put(OperatorID.PRODUCT, mCopyPastStrategy);

        stMap.put(OperatorID.PROJECT, mProjectStrategy);

        stMap.put(OperatorID.GROUP_BY, mNoOpStrategy);
        stMap.put(OperatorID.SCALAR_GROUP_BY, mNoOpStrategy);

        stMap.put(OperatorID.SEMI_JOIN, mCopyPastLeftHandChildStrategy);
        stMap.put(OperatorID.ANTI_SEMI_JOIN, mCopyPastLeftHandChildStrategy);

        stMap.put(OperatorID.RENAME, mRenameStrategy);
        stMap.put(OperatorID.APPLY, mNoOpStrategy);
    }

    /**
     * {@inheritDoc}
     */
    public ProjectPushDownStrategy getStrategy(Operator operator)
    {
        ProjectPushDownStrategy strategy = 
            mOperatorToStrategyMap.get(operator.getID());

        if (strategy == null)
        {
            return mNoOpStrategy;
        }
        return strategy;
    }
}
