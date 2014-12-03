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


package uk.org.ogsadai.expression.arithmetic;

import uk.org.ogsadai.exception.DAIUncheckedException;
import uk.org.ogsadai.exception.ErrorID;

/**
 * Raised when a column name is ambiguous because it occurs in more than one 
 * table.
 *
 * @author The OGSA-DAI Project Team.
 */
public class ColumnNameAmbiguousException extends DAIUncheckedException
{
    
    /** Copyright notice. */
    private static final String COPYRIGHT_NOTICE = 
        "Copyright (c) The University of Edinburgh, 2008";
    
    private static final ErrorID ERROR_ID =
        new ErrorID("uk.org.ogsadai.dqp.COLUMN_NAME_AMBIGUOUS_ERROR");

    /**
     * Constructs a new exception.
     * 
     * @param name
     *            column name which is not unique
     */
    public ColumnNameAmbiguousException(String name) 
    {
        super(ERROR_ID, new Object[] {name});
    }

}
