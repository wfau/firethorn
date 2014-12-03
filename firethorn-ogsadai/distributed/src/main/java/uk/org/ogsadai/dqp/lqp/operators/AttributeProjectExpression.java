// Copyright (c) The University of Edinburgh, 2012.
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

package uk.org.ogsadai.dqp.lqp.operators;

import uk.org.ogsadai.dqp.lqp.Attribute;

/**
 * Represents a project expression that is an attribute. 
 * 
 * @author The OGSA-DAI Project Team.
 */
public class AttributeProjectExpression implements ProjectExpression
{
    /** Copyright notice. */
    private static final String COPYRIGHT_NOTICE =
        "Copyright (c) The University of Edinburgh, 2012";

    /** Attribute. */
    private Attribute mAttr;
    
    /**
     * Constructor.
     * 
     * @param attr attribute.
     */
    public AttributeProjectExpression(Attribute attr)
    {
        mAttr = attr.getClone(false);
    }
    
    /**
     * Gets the attribute.
     * 
     * @return attribute.
     */
    public Attribute getAttribute()
    {
        return mAttr;
    }
    
    public String toString()
    {
        return "[ " + mAttr + "]";
    }

}