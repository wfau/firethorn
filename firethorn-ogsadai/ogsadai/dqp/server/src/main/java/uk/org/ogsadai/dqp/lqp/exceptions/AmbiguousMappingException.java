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


package uk.org.ogsadai.dqp.lqp.exceptions;

import uk.org.ogsadai.dqp.lqp.Attribute;
import uk.org.ogsadai.dqp.lqp.RenameMap;

/**
 * Raised when the mapping defined in the {@link RenameMap} introduces
 * ambiguity. For example with mapping defined as
 * <code>(id, id) &lt;-&gt; (id, ident)</code> going from
 * <code>ident-&gt;id</code> will introduce ambiguity when for example we want
 * to rename attribute used in a predicate like <code>ident&lt;3</code>. This
 * usually happens when we move the <code>RENAME</code> operator up and down the
 * tree.
 * 
 * @author The OGSA-DAI Project Team.
 */
public class AmbiguousMappingException extends LQPException
{
    /** Copyright notice. */
    private static final String COPYRIGHT_NOTICE = 
        "Copyright (c) The University of Edinburgh, 2008";
    
    /**
     * Constructor.
     * 
     * @param attribute
     *         ambiguous attribute.
     */
    public AmbiguousMappingException(Attribute attribute)
    {
        super("Attribute mapping introduces ambiguity: " + attribute);
    }
    
}
