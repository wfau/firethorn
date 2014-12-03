// Copyright (c) The University of Edinburgh,  2002 - 2008.
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
 * Raised when referenced attribute is ambiguous in some context.
 * 
 * @author The OGSA-DAI Project Team
 */
public class AmbiguousAttributeException extends LQPException
{
    /** Copyright statement */
    private static final String COPYRIGHT_NOTICE = 
        "Copyright (c) The University of Edinburgh,  2002 - 2007"; 
    
    /** Ambiguous attribute */
    private Attribute mAttribute;
    
    /**
     * Constructor.
     * 
     * @param attribute
     *         ambiguous attribute.
     */
    public AmbiguousAttributeException(Attribute attribute)
    {
        super("Ambiguous attribute: " + attribute.getName());
    }
    
    /**
     * Gets the ambiguous attribute.
     * 
     * @return
     *         ambiguous attribute.
     */
    public Attribute getAttribute()
    {
        return mAttribute;
    }
}
