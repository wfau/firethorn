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

import java.util.Iterator;

import uk.org.ogsadai.client.toolkit.activity.LiteralInputDescriptor;
import uk.org.ogsadai.client.toolkit.activity.SimpleLiteralInputDescriptor;

/**
 * Extension of <code>SimpleLiteralInputDescriptor</code> to include an
 * <tt>equals</tt> method.  This method is very useful for unit tests using
 * mock objects.  
 * <p>
 * An <tt>equals</tt> method has now been added to the 
 * <code>SimpleLiteralInputDescriptor</code> class but we wish to release DQP
 * without requiring new OGSA-DAI jars so in the meantime we use this class.
 * When things merge this class can be removed and 
 * <code>SimpleLiteralInputDescriptor</code> used instead.
 * 
 * @deprecated See class documentation.  When code is merged we can remove
 *             this class.
 *
 * @author The OGSA-DAI Project Team
 */
public class SimpleLiteralInputDescriptorWithEquals 
    extends SimpleLiteralInputDescriptor
{
    /** Copyright notice. */
    private static final String COPYRIGHT_NOTICE = 
        "Copyright (c) The University of Edinburgh, 2009.";

    /**
     * Constructor.
     * 
     * @param inputName  input name
     */
    public SimpleLiteralInputDescriptorWithEquals(String inputName)
    {
        super(inputName);
    }
    
    /**
     * {@inheritDoc}
     */
    public boolean equals(Object obj)
    {
        if (obj instanceof LiteralInputDescriptor)
        {
            LiteralInputDescriptor rhs = (LiteralInputDescriptor) obj;
            if (!getInputName().equals(rhs.getInputName()))
            {
                return false;
            }
            Iterator lhsIt = iterator();
            Iterator rhsIt = rhs.iterator();
            
            while(lhsIt.hasNext())
            {
                if (!rhsIt.hasNext()) return false;
                if (!lhsIt.next().equals(rhsIt.next())) return false;
            }
            if (rhsIt.hasNext()) return false;
            return true;
        }
        return false;
    }
}
