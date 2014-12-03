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


package uk.org.ogsadai.client.toolkit.activities.generic;

import uk.org.ogsadai.client.toolkit.activity.PipeInputDescriptor;
import uk.org.ogsadai.client.toolkit.activity.SimplePipeInputDescriptor;

/**
 * Extension of <code>SimplePipeInputDescriptor</code> to include an
 * <tt>equals</tt> method.  This method is very useful for unit tests using
 * mock objects.  
 * <p>
 * An <tt>equals</tt> method has now been added to the 
 * <code>SimplePipeInputDescriptor</code> class but we wish to release DQP
 * without requiring new OGSA-DAI jars so in the meantime we use this class.
 * When things merge this class can be removed and 
 * <code>SimplePipeInputDescriptor</code> used instead.
 * 
 * @deprecated See class documentation.  When code is merged we can remove
 *             this class.
 *
 * @author The OGSA-DAI Project Team
 */
public class SimplePipeInputDescriptorWithEquals 
    extends SimplePipeInputDescriptor
{
    /** Copyright notice. */
    private static final String COPYRIGHT_NOTICE = 
        "Copyright (c) The University of Edinburgh, 2009.";

    /**
     * Constructor.
     * 
     * @param inputName  input name
     * @param pipeName   pipe name
     */
    public SimplePipeInputDescriptorWithEquals(
        String inputName, String pipeName)
    {
        super(inputName, pipeName);
    }

    /**
     * {@inheritDoc}
     */
    public boolean equals(Object obj)
    {
        if (obj instanceof PipeInputDescriptor)
        {
            PipeInputDescriptor rhs = (PipeInputDescriptor) obj;
            return getInputName().equals(rhs.getInputName()) && 
                   getPipeName().equals(rhs.getPipeName());
        }
        return false;
    }
}
