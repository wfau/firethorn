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

package uk.org.ogsadai.dqp.lqp.optimiser.select.strategies;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import uk.org.ogsadai.common.msgs.DAILogger;
import uk.org.ogsadai.dqp.lqp.Attribute;
import uk.org.ogsadai.dqp.lqp.RenameMap;
import uk.org.ogsadai.dqp.lqp.SimpleRenameMap;
import uk.org.ogsadai.dqp.lqp.operators.RenameOperator;
import uk.org.ogsadai.dqp.lqp.operators.SelectOperator;
import uk.org.ogsadai.dqp.lqp.optimiser.select.SelectPushDownStrategy;

/**
 * Tries to push SELECT past a RENAME operator.
 * 
 * @author The OGSA-DAI Project Team.
 */
public class SelectPushPastRenameStrategy implements SelectPushDownStrategy
{
    /** Copyright notice. */
    private static final String COPYRIGHT_NOTICE = 
        "Copyright (c) The University of Edinburgh, 2008";

    /** Logger. */
    private static final DAILogger LOG = DAILogger
        .getLogger(SelectPushPastRenameStrategy.class);
    
    /**
     * {@inheritDoc}
     */
    public SelectOperator pushDown(SelectOperator selectOperator)
    {
        RenameOperator renameOperator = (RenameOperator) selectOperator
            .getChild(0);

        Set<Attribute> usedAttributes = selectOperator.getUsedAttributes();
        RenameMap renameMap = renameOperator.getRenameMap();

	RenameMap specificRenameMap = new SimpleRenameMap();
        try
        {
            for (Attribute attr : usedAttributes)
            {
                Attribute originalAttribute = renameMap
                    .getOriginalAttribute(attr);
                if (!originalAttribute.equals(attr))
                {
                    specificRenameMap.add(attr, originalAttribute);
                }
            }
        }
        // AmbiguousAttributeException, AmbiguousMappingException
        catch (Exception e)
        {
            // Ye shall not pass if it would introduce ambiguity
            LOG.debug(e.getMessage());
            return null;
        }

	SelectOperator passSelect = null;
	try
	{
	    if (specificRenameMap.size() > 0)
	    {
		selectOperator.renameUsedAttributes(specificRenameMap);
	    }
	}
	catch (Exception e)
	{
	    throw new RuntimeException(e);
	}
        passSelect = SelectStrategyUtils.pushDownNoChange(selectOperator);

        return passSelect;
    }
}
