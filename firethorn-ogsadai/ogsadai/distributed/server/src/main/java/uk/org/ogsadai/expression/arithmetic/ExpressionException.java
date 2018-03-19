//Copyright (c) The University of Edinburgh 2007.
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

import uk.org.ogsadai.exception.DAIException;
import uk.org.ogsadai.exception.ErrorID;

/**
 * Raised when the abstract syntax tree of an arithmetic expression could not
 * be parsed into an expression.
 *
 * @author The OGSA-DAI Project Team.
 */
public class ExpressionException extends DAIException
{
    /** Copyright notice */
    private static final String COPYRIGHT_NOTICE = 
        "Copyright (c) The University of Edinburgh, 2007.";
    
    /** Error id. */
    private static final ErrorID ERROR_ID = 
        new ErrorID("uk.org.ogsadai.EXPRESSION_ERROR");
    
    /**
     * Constructor.
     * 
     * @param t
     *            cause
     */
    public ExpressionException(Throwable t)
    {
        super(ERROR_ID);
        initCause(t);
    }

}
