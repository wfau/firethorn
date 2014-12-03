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

package uk.org.ogsadai.dqp.lqp.optimiser.select;

import java.util.HashMap;
import java.util.Map;

import uk.org.ogsadai.dqp.lqp.Operator;
import uk.org.ogsadai.dqp.lqp.OperatorID;
import uk.org.ogsadai.dqp.lqp.optimiser.select.strategies.SelectPushDefault;
import uk.org.ogsadai.dqp.lqp.optimiser.select.strategies.SelectPushPastAnyStrategy;
import uk.org.ogsadai.dqp.lqp.optimiser.select.strategies.SelectPushPastScanBindApplyStrategy;
import uk.org.ogsadai.dqp.lqp.optimiser.select.strategies.SelectPushPastBinaryAnyBranchStrategy;
import uk.org.ogsadai.dqp.lqp.optimiser.select.strategies.SelectPushPastBinaryLeftBranchStrategy;
import uk.org.ogsadai.dqp.lqp.optimiser.select.strategies.SelectPushPastBinaryRightBranchStrategy;
import uk.org.ogsadai.dqp.lqp.optimiser.select.strategies.SelectPushPastProductStrategy;
import uk.org.ogsadai.dqp.lqp.optimiser.select.strategies.SelectPushPastProjectStrategy;
import uk.org.ogsadai.dqp.lqp.optimiser.select.strategies.SelectPushPastRenameStrategy;
import uk.org.ogsadai.dqp.lqp.optimiser.select.strategies.SelectPushPastSetOpStrategy;

/**
 * Factory for SELECT push down strategies.
 * 
 * @author The OGSA-DAI Project Team.
 */
public class SelectPushDownStrategyFactoryImpl implements
    SelectPushDownStrategyFactory
{
    /** Copyright notice. */
    private static final String COPYRIGHT_NOTICE = 
        "Copyright (c) The University of Edinburgh, 2008";

    /** Strategies map. */
    private Map<OperatorID, SelectPushDownStrategy> mOperatorToStrategyMap;

    /** Strategies */
    private static SelectPushDownStrategy mDefaultStrategy = 
        new SelectPushDefault();
    private static SelectPushDownStrategy mPastAnyStrategy = 
        new SelectPushPastAnyStrategy();
    private static SelectPushDownStrategy mPastSetOpStrategy = 
        new SelectPushPastSetOpStrategy();
    private static SelectPushDownStrategy mPastProjectStrategy = 
        new SelectPushPastProjectStrategy();
    private static SelectPushDownStrategy mPastRenameStrategy = 
        new SelectPushPastRenameStrategy();
    private static SelectPushDownStrategy mPastBinaryAnyBranchStrategy = 
        new SelectPushPastBinaryAnyBranchStrategy();
    private static SelectPushDownStrategy mPastBinaryLeftBranchStrategy = 
        new SelectPushPastBinaryLeftBranchStrategy();
    private static SelectPushDownStrategy mPastBinaryRightBranchStrategy = 
        new SelectPushPastBinaryRightBranchStrategy();
    private static SelectPushDownStrategy mPastProductStrategy = 
        new SelectPushPastProductStrategy();
    private static SelectPushDownStrategy mPastScanBindApplyStrategy = 
        new SelectPushPastScanBindApplyStrategy();
    
    
    /**
     * Constructor.
     */
    public SelectPushDownStrategyFactoryImpl()
    {
        mOperatorToStrategyMap = new HashMap<OperatorID, SelectPushDownStrategy>();
        Map<OperatorID, SelectPushDownStrategy> stMap = mOperatorToStrategyMap;
        
        stMap.put(OperatorID.FULL_OUTER_JOIN, mDefaultStrategy);
        stMap.put(OperatorID.TABLE_SCAN, mDefaultStrategy);
        stMap.put(OperatorID.GROUP_BY, mDefaultStrategy);
        stMap.put(OperatorID.SCALAR_GROUP_BY, mDefaultStrategy);

        stMap.put(OperatorID.SELECT, mPastAnyStrategy);
        stMap.put(OperatorID.DUPLICATE_ELIMINATION, mPastAnyStrategy);
        stMap.put(OperatorID.ONE_ROW_ONLY, mPastAnyStrategy);
        stMap.put(OperatorID.SORT, mPastAnyStrategy);

        stMap.put(OperatorID.UNION, mPastSetOpStrategy);
        stMap.put(OperatorID.INTERSECTION, mPastSetOpStrategy);
        stMap.put(OperatorID.DIFFERENCE, mPastSetOpStrategy);

        stMap.put(OperatorID.PROJECT, mPastProjectStrategy);
        
        stMap.put(OperatorID.RENAME, mPastRenameStrategy);
        
        stMap.put(OperatorID.INNER_THETA_JOIN, mPastBinaryAnyBranchStrategy);
        stMap.put(OperatorID.LEFT_OUTER_JOIN, mPastBinaryLeftBranchStrategy);
        stMap.put(OperatorID.RIGHT_OUTER_JOIN, mPastBinaryRightBranchStrategy);
        stMap.put(OperatorID.SEMI_JOIN, mPastBinaryLeftBranchStrategy);
        stMap.put(OperatorID.ANTI_SEMI_JOIN, mPastBinaryLeftBranchStrategy);
        
        stMap.put(OperatorID.PRODUCT, mPastProductStrategy);
        
        stMap.put(OperatorID.SCAN_BIND_APPLY, mPastScanBindApplyStrategy);

        // TODO: check if can pass via encapsulated operator, but only after
        // decorrelation
        stMap.put(OperatorID.APPLY, mDefaultStrategy);
    }
    
    /**
     * {@inheritDoc}
     */
    public SelectPushDownStrategy getStrategy(Operator operator)
    {
        SelectPushDownStrategy strategy = mOperatorToStrategyMap.get(operator
            .getID());

        if (strategy == null)
        {
            return mDefaultStrategy;
        }

        return strategy;
    }
}
