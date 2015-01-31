// Copyright (c) The University of Edinburgh,  2007.
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


package uk.org.ogsadai.activity.block;

import uk.org.ogsadai.activity.ActivityUserException;
import uk.org.ogsadai.exception.ErrorID;

/**
 * Exception thrown when two inputs have different granularity but the activity
 * processing them expects the same granularity.
 * <p>
 * Associated with error code: uk.org.ogsadai.MISMATCHED_INPUTS_GRANULARITY
 * 
 * @author The OGSA-DAI Project Team
 */
public class MismatchedInputGranularityException extends ActivityUserException 
{    
    /** Copyright statement */
    private static final String COPYRIGHT_NOTICE = 
        "Copyright (c) The University of Edinburgh, 2007";

    /**
     * Constructs a new exception.
     * 
     * @param inputName
     *            the input which has invalid granularity
     * @param granularityInputName
     *            the input which provided the granularity number
     * @param granularity 
     *            the granularity that was read from the granularity input
     * 
     */
    public MismatchedInputGranularityException(
            String inputName,
            String granularityInputName,
            int granularity)
    {
        super(ErrorID.MISMATCHED_INPUTS_GRANULARITY, 
                new Object[] { 
                    inputName, 
                    granularityInputName, 
                    new Integer(granularity) });
    }
    
}
