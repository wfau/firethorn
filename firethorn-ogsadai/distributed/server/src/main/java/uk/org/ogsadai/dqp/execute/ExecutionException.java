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


package uk.org.ogsadai.dqp.execute;

import uk.org.ogsadai.exception.DAIException;
import uk.org.ogsadai.exception.ErrorID;

/**
 * Raised if an error occurred when executing a partition workflow.
 *
 * @author The OGSA-DAI Project Team.
 */

public class ExecutionException extends DAIException
{
    /** Copyright notice. */
    private static final String COPYRIGHT_NOTICE = 
        "Copyright (c) The University of Edinburgh, 2008";
    
    /** The error ID. */
    public static final ErrorID ERROR_ID = 
        new ErrorID("uk.org.ogsadai.EXECUTION_ERROR");

    /**
     * Constructs a new execution exception with the specified cause.
     * 
     * @param cause
     *            error cause
     */
    public ExecutionException(Throwable cause)
    {
        super(ERROR_ID);
        initCause(cause);
    }

}
