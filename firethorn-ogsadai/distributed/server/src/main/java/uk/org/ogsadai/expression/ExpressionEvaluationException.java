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

package uk.org.ogsadai.expression;

import uk.org.ogsadai.tuple.TypeConversionException;
import uk.org.ogsadai.tuple.converters.StringConversionException;

/**
 * Exception thrown when there are problem with evaluating expressions.
 * 
 * @author The OGSA-DAI Project Team.
 */
public class ExpressionEvaluationException extends Exception
{
    /** Copyright notice. */
    private static final String COPYRIGHT_NOTICE = 
        "Copyright (c) The University of Edinburgh, 2008";

    /** Auto generated serial id. */
    private static final long serialVersionUID = 2460506575706863609L;

    /**
     * Wrapping constructor.
     * 
     * @param t
     *            cause
     */
    public ExpressionEvaluationException(Throwable t)
    {
        super(t);
    }
    
    /**
     * Message constructor.
     * 
     * @param message
     *            detailed message
     */
    public ExpressionEvaluationException(String message)
    {
        super(message);
    }

    /**
     * Check if the result of failed evaluation can be treated as unknown
     * (NULL).
     * 
     * @return <code>true</code> is result can be treated as NULL,
     *         <code>false</code> otherwise
     */
    public boolean canTreatAsNULL()
    {
        Throwable cause = getCause();
        
        if (cause instanceof TypeConversionException)
        {
            if (cause.getCause() instanceof StringConversionException
                || cause.getCause() instanceof NumberFormatException)
            {
                return true;
            }
        }
        
        return false;
    }
}
