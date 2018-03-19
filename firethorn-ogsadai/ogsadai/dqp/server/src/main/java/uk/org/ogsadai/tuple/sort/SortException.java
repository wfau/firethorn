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


package uk.org.ogsadai.tuple.sort;

import uk.org.ogsadai.exception.DAIUncheckedException;
import uk.org.ogsadai.exception.ErrorID;

/**
 * Raised if there was a runtime exception whilst sorting a tuple list.
 *
 * @author The OGSA-DAI Project Team.
 */
public class SortException extends DAIUncheckedException
{
    
    /** Copyright notice. */
    private static final String COPYRIGHT_NOTICE = 
        "Copyright (c) The University of Edinburgh, 2008";
    
    /** Error ID of this exception. */
    public static final ErrorID ERROR_ID =
        new ErrorID("uk.org.ogsadai.SORT_RUNTIME_EXCEPTION");
    
    /**
     * Constructs a new chained exception.
     * 
     * @param cause
     *            error cause
     */
    public SortException(Throwable cause)
    {
        super(ERROR_ID);
        initCause(cause);
    }

}
