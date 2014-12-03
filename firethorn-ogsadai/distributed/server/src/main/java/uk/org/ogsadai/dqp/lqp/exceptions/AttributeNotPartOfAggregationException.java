// Copyright (c) The University of Edinburgh,  2010.
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


package uk.org.ogsadai.dqp.lqp.exceptions;

import uk.org.ogsadai.dqp.lqp.Attribute;

/**
 * Raised when attribute is not part of an aggregation so cannot be
 * used.  This is thrown when an attribute is used outside of an aggregation
 * of which it is not a part.  For example
 * <pre>
 * SELECT a, b FROM myTable GROUP BY a
 * </pre>
 * in this query attribute <tt>b</tt> is not part of the aggregation so cannot
 * be used here.
 * 
 * @author The OGSA-DAI Project Team
 */
public class AttributeNotPartOfAggregationException extends LQPException
{
    /** Copyright statement */
    private static final String COPYRIGHT_NOTICE = 
        "Copyright (c) The University of Edinburgh,  2002 - 2007"; 
    
    /** Missing attribute */
    private Attribute mAttribute;
    
    /**
     * Constructor.
     * 
     * @param attribute
     *         attribute that is not part of the aggregation
     */
    public AttributeNotPartOfAggregationException(Attribute attribute)
    {
        super("Attribute not part of aggregation: " + attribute);
        mAttribute = attribute;
    }
    
    /**
     * Gets the attribute that is not part of the aggregation.
     * 
     * @return
     *         attribute that is not part of the aggregation.
     */
    public Attribute getAttribute()
    {
        return mAttribute;
    }
}
