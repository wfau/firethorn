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

package uk.org.ogsadai.dqp.lqp.optimiser.rename;

import java.util.HashMap;
import java.util.Map;

import uk.org.ogsadai.dqp.lqp.Operator;
import uk.org.ogsadai.dqp.lqp.OperatorID;
import uk.org.ogsadai.dqp.lqp.optimiser.rename.strategies.RenamePullDefaultStrategy;
import uk.org.ogsadai.dqp.lqp.optimiser.rename.strategies.RenamePullPastAnyStrategy;
import uk.org.ogsadai.dqp.lqp.optimiser.rename.strategies.RenamePullPastAnyUpdateParentStrategy;
import uk.org.ogsadai.dqp.lqp.optimiser.rename.strategies.RenamePullPastApplyStrategy;
import uk.org.ogsadai.dqp.lqp.optimiser.rename.strategies.RenamePullPastJoinStrategy;
import uk.org.ogsadai.dqp.lqp.optimiser.rename.strategies.RenamePullPastRenameStrategy;
import uk.org.ogsadai.dqp.lqp.optimiser.rename.strategies.RenamePullPastSemiJoinStrategy;
import uk.org.ogsadai.dqp.lqp.optimiser.rename.strategies.RenamePullPastUnaryUpdateParentStrategy;

/**
 * Default factory for RENAME pull up strategies.
 * 
 * @author The OGSA-DAI Project Team.
 */
public class RenamePullUpStrategyFactoryImpl implements
    RenamePullUpStrategyFactory
{
    /** Copyright notice. */
    private static final String COPYRIGHT_NOTICE = 
        "Copyright (c) The University of Edinburgh, 2008";

    /** Strategies map */
    private Map<OperatorID, RenamePullUpStrategy> mOperatorToStrategyMap;

    /** Strategies */
    private static RenamePullUpStrategy mDefaultStrategy = 
        new RenamePullDefaultStrategy();
    private static RenamePullUpStrategy mPastAnyStrategy = 
        new RenamePullPastAnyStrategy();
    private static RenamePullUpStrategy mPastJoinStrategy = 
        new RenamePullPastJoinStrategy();
    private static RenamePullUpStrategy mPastSemiJoinStrategy = 
        new RenamePullPastSemiJoinStrategy();
    private static RenamePullUpStrategy mPastNoChangeParentUpdateStrategy = 
        new RenamePullPastAnyUpdateParentStrategy();
    private static RenamePullUpStrategy mPastUnaryParentUpdateStrategy = 
        new RenamePullPastUnaryUpdateParentStrategy();
    private static RenamePullUpStrategy mPastRenameStrategy = 
        new RenamePullPastRenameStrategy();
    private static RenamePullUpStrategy mPastApplyStrategy = 
        new RenamePullPastApplyStrategy();

    /**
     * Constructor.
     */
    public RenamePullUpStrategyFactoryImpl()
    {
        mOperatorToStrategyMap = new HashMap<OperatorID, RenamePullUpStrategy>();
        Map<OperatorID, RenamePullUpStrategy> stMap = mOperatorToStrategyMap;
                
        // Renames below set operators usually "prepare" headings to be union
        // compatible.
        stMap.put(OperatorID.UNION, mDefaultStrategy);
        stMap.put(OperatorID.INTERSECTION, mDefaultStrategy);
        stMap.put(OperatorID.DIFFERENCE, mDefaultStrategy);
        
        // TODO: Apply is a tricky one - but not that much - we just need to do
        // some thinking.
        stMap.put(OperatorID.APPLY, mPastApplyStrategy);
                
        // This one should never appear but we comment for completeness.
        stMap.put(OperatorID.TABLE_SCAN, mDefaultStrategy);
        
        // Just pass
        stMap.put(OperatorID.DUPLICATE_ELIMINATION, mPastAnyStrategy);
        stMap.put(OperatorID.ONE_ROW_ONLY, mPastAnyStrategy);
        
        // Join like
        stMap.put(OperatorID.PRODUCT, mPastJoinStrategy);
        stMap.put(OperatorID.INNER_THETA_JOIN, mPastJoinStrategy);
        stMap.put(OperatorID.FULL_OUTER_JOIN, mPastJoinStrategy);
        stMap.put(OperatorID.LEFT_OUTER_JOIN, mPastJoinStrategy);
        stMap.put(OperatorID.RIGHT_OUTER_JOIN, mPastJoinStrategy);
        
        // Semi joins
        stMap.put(OperatorID.SEMI_JOIN, mPastSemiJoinStrategy);
        stMap.put(OperatorID.ANTI_SEMI_JOIN, mPastSemiJoinStrategy);
        
        // Pass and update parent
        stMap.put(OperatorID.SORT, mPastNoChangeParentUpdateStrategy);
        stMap.put(OperatorID.SELECT, mPastNoChangeParentUpdateStrategy);
        
        // Pass with rename and parent update
        stMap.put(OperatorID.PROJECT, mPastUnaryParentUpdateStrategy);
        stMap.put(OperatorID.GROUP_BY, mPastUnaryParentUpdateStrategy);
        stMap.put(OperatorID.SCALAR_GROUP_BY, mPastUnaryParentUpdateStrategy);
        
        // Pass rename
        stMap.put(OperatorID.RENAME, mPastRenameStrategy);
    }
    
    /**
     * {@inheritDoc}
     */
    public RenamePullUpStrategy getStrategy(Operator operator)
    {
        RenamePullUpStrategy strategy = mOperatorToStrategyMap.get(operator
            .getID());
        
        if(strategy == null)
        {
            return mDefaultStrategy;
        }
        
        return strategy;
    }
}
