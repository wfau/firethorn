// Copyright (c) The University of Edinburgh, 2008.
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

package uk.org.ogsadai.dqp.lqp.optimiser.project;

import java.util.HashMap;
import java.util.Map;

import uk.org.ogsadai.dqp.lqp.Operator;
import uk.org.ogsadai.dqp.lqp.OperatorID;
import uk.org.ogsadai.dqp.lqp.optimiser.project.strategies.ProjectPullDefaultStrategy;
import uk.org.ogsadai.dqp.lqp.optimiser.project.strategies.ProjectPullPastAnyStrategy;
import uk.org.ogsadai.dqp.lqp.optimiser.project.strategies.ProjectPullPastBinaryNonDerivedStrategy;
import uk.org.ogsadai.dqp.lqp.optimiser.project.strategies.ProjectPullPastGroupByStrategy;
import uk.org.ogsadai.dqp.lqp.optimiser.project.strategies.ProjectPullPastProjectStrategy;
import uk.org.ogsadai.dqp.lqp.optimiser.project.strategies.ProjectPullPastSemiJoinStrategy;
import uk.org.ogsadai.dqp.lqp.optimiser.project.strategies.ProjectPullPastUnaryNonDerivedStrategy;

/**
 * Factory for project pull up strategies.
 * 
 * @author The OGSA-DAI Project Team.
 */
public class ProjectPullUpStrategyFactoryImpl implements
    ProjectPullUpStrategyFactory
{
    /** Copyright notice. */
    private static final String COPYRIGHT_NOTICE = 
        "Copyright (c) The University of Edinburgh, 2008";

    /** Strategies map */
    private Map<OperatorID, ProjectPullUpStrategy> mOperatorToStrategyMap;

    /** Strategies */
    private static ProjectPullUpStrategy mDefaultStrategy = 
        new ProjectPullDefaultStrategy();
    private static ProjectPullUpStrategy mPastAnyStrategy = 
        new ProjectPullPastAnyStrategy();
    private static ProjectPullUpStrategy mPastUnaryNonDerivedStrategy = 
        new ProjectPullPastUnaryNonDerivedStrategy();
    private static ProjectPullUpStrategy mPastBinaryNonDerivedStrategy = 
        new ProjectPullPastBinaryNonDerivedStrategy();
    private static ProjectPullUpStrategy mPastProjectStrategy = 
        new ProjectPullPastProjectStrategy();
    private static ProjectPullUpStrategy mPastGroupByStrategy = 
        new ProjectPullPastGroupByStrategy();
    private static ProjectPullUpStrategy mPastSemiJoinStrategy = 
        new ProjectPullPastSemiJoinStrategy();

    /**
     * Constructor.
     */
    public ProjectPullUpStrategyFactoryImpl()
    {
        mOperatorToStrategyMap = new HashMap<OperatorID, ProjectPullUpStrategy>();
        Map<OperatorID, ProjectPullUpStrategy> stMap = mOperatorToStrategyMap;

        stMap.put(OperatorID.DUPLICATE_ELIMINATION, mDefaultStrategy);
        stMap.put(OperatorID.TABLE_SCAN, mDefaultStrategy);

        // Project below set operators prepares tuple stream for union
        // compatibility if it is redundant it should be removed earlier
        stMap.put(OperatorID.UNION, mDefaultStrategy);
        stMap.put(OperatorID.INTERSECTION, mDefaultStrategy);
        stMap.put(OperatorID.DIFFERENCE, mDefaultStrategy);

        stMap.put(OperatorID.ONE_ROW_ONLY, mPastAnyStrategy);

        stMap.put(OperatorID.SELECT, mPastUnaryNonDerivedStrategy);
        stMap.put(OperatorID.SORT, mPastUnaryNonDerivedStrategy);

        stMap.put(OperatorID.FULL_OUTER_JOIN, mPastBinaryNonDerivedStrategy);
        stMap.put(OperatorID.LEFT_OUTER_JOIN, mPastBinaryNonDerivedStrategy);
        stMap.put(OperatorID.RIGHT_OUTER_JOIN, mPastBinaryNonDerivedStrategy);
        stMap.put(OperatorID.INNER_THETA_JOIN, mPastBinaryNonDerivedStrategy);
        stMap.put(OperatorID.PRODUCT, mPastBinaryNonDerivedStrategy);

        stMap.put(OperatorID.PROJECT, mPastProjectStrategy);

        stMap.put(OperatorID.GROUP_BY, mPastGroupByStrategy);
        stMap.put(OperatorID.SCALAR_GROUP_BY, mPastGroupByStrategy);

        stMap.put(OperatorID.SEMI_JOIN, mPastSemiJoinStrategy);
        stMap.put(OperatorID.ANTI_SEMI_JOIN, mPastSemiJoinStrategy);

        // TODO: We can pass rename in most cases
        stMap.put(OperatorID.RENAME, mDefaultStrategy);
        // TODO: We can pass apply in most cases
        stMap.put(OperatorID.APPLY, mDefaultStrategy);
    }

    /**
     * {@inheritDoc}
     */
    public ProjectPullUpStrategy getStrategy(Operator operator)
    {
        ProjectPullUpStrategy strategy = mOperatorToStrategyMap.get(operator
            .getID());

        if (strategy == null)
        {
            return mDefaultStrategy;
        }
        return strategy;
    }
}
