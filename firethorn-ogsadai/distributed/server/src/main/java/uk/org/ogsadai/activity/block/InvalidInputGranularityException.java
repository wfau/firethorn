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
 * Exception thrown an input has a granularity that is not supported by the
 * activity that processes the input.
 * <p>
 * Associated with error code: uk.org.ogsadai.INVALID_INPUT_GRANULARITY.
 * 
 * @author The OGSA-DAI Project Team
 */
public class InvalidInputGranularityException extends ActivityUserException
{
    /** Copyright statement */
    private static final String COPYRIGHT_NOTICE = 
        "Copyright (c) The University of Edinburgh, 2007";

    /**
     * Constructs a new exception.
     * 
     * @param inputName
     *            name of the input that has invalid granularity
     * @param minGranularity
     *            the minimum granularity of the input
     * 
     */
    public InvalidInputGranularityException(
            String inputName, int minGranularity) 
    {
        super(ErrorID.INVALID_INPUT_GRANULARITY, 
                new Object[] { inputName, new Integer(minGranularity) });
    }

}
