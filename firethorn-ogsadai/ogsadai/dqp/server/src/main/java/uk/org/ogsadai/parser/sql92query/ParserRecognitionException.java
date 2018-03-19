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


package uk.org.ogsadai.parser.sql92query;

import org.antlr.runtime.EarlyExitException;
import org.antlr.runtime.FailedPredicateException;
import org.antlr.runtime.MismatchedRangeException;
import org.antlr.runtime.MismatchedSetException;
import org.antlr.runtime.MismatchedTokenException;
import org.antlr.runtime.MismatchedTreeNodeException;
import org.antlr.runtime.NoViableAltException;
import org.antlr.runtime.RecognitionException;

import uk.org.ogsadai.exception.DAIException;
import uk.org.ogsadai.exception.ErrorID;
import uk.org.ogsadai.exception.UserExceptionMarker;

/**
 * Raised when a parse exception occurs.
 *
 * @author The OGSA-DAI Project Team.
 */
public class ParserRecognitionException 
    extends DAIException 
    implements UserExceptionMarker
{
    /** No viable alternative. */
    public static final ErrorID NO_VIABLE_ALT_EXCEPTION = 
        new ErrorID("uk.org.ogsadai.NO_VIABLE_ALT_EXCEPTION"); 
    
    /** An alternative must match at least once but did not match anything. */
    public static final ErrorID EARLY_EXIT_EXCEPTION = 
        new ErrorID("uk.org.ogsadai.EARLY_EXIT_EXCEPTION"); 

    /** Could not match a range of symbols. */
    public static final ErrorID MISMATCHED_RANGE_EXCEPTION = 
        new ErrorID("uk.org.ogsadai.MISMATCHED_RANGE_EXCEPTION");
    
    /** Could not match a set of symbols. */
    public static final ErrorID MISMATCHED_SET_EXCEPTION = 
        new ErrorID("uk.org.ogsadai.MISMATCHED_SET_EXCEPTION");
    
    /** Parser was looking for a particular symbol and did not find it. */
    public static final ErrorID MISMATCHED_TOKEN_EXCEPTION = 
        new ErrorID("uk.org.ogsadai.MISMATCHED_TOKEN_EXCEPTION");
    
    /** A tree parser was looking for a particular node but did not find it. */ 
    public static final ErrorID MISMATCHED_TREE_NODE_EXCEPTION = 
        new ErrorID("uk.org.ogsadai.MISMATCHED_TREE_NODE_EXCEPTION");
    
    /** A validating semantic predicates evaluated to false. */
    public static final ErrorID FAILED_PREDICATE_EXCEPTION = 
        new ErrorID("uk.org.ogsadai.FAILED_PREDICATE_EXCEPTION");

    /** Some other parsing error occurred which was not recognised. */
    public static final ErrorID RECOGNITION_EXCEPTION = 
        new ErrorID("uk.org.ogsadai.RECOGNITION_EXCEPTION");

    protected String mToken;
    protected int mLine;
    protected int mPositionInLine;
    
    private ParserRecognitionException(ErrorID errorID)
    {
        super(errorID);
    }
    
    /**
     * Constructs an instance of a parse exception with the given cause.
     * 
     * @param cause
     *            error cause
     * @return parser exception
     */
    public static ParserRecognitionException getInstance(
            RecognitionException cause)
    {
        ErrorID errorID;
        if (cause instanceof NoViableAltException)
        {
            errorID = NO_VIABLE_ALT_EXCEPTION;
        }
        else if (cause instanceof EarlyExitException)
        {
            errorID = EARLY_EXIT_EXCEPTION;
        }
        else if (cause instanceof MismatchedRangeException)
        {
            errorID = MISMATCHED_RANGE_EXCEPTION;
        }
        else if (cause instanceof MismatchedSetException)
        {
            errorID = MISMATCHED_SET_EXCEPTION;
        }
        else if (cause instanceof MismatchedTokenException)
        {
            errorID = MISMATCHED_TOKEN_EXCEPTION;
        }
        else if (cause instanceof MismatchedTreeNodeException)
        {
            errorID = MISMATCHED_TREE_NODE_EXCEPTION;
        }
        else if (cause instanceof FailedPredicateException)
        {
            errorID = FAILED_PREDICATE_EXCEPTION;
        }
        else
        {
            errorID = RECOGNITION_EXCEPTION;
        }
        ParserRecognitionException result = new ParserRecognitionException(errorID); 
        result.mToken = cause.token.getText();
        result.mLine = cause.line;
        result.mPositionInLine = cause.charPositionInLine;
        result.setParameters(
                new Object[] {
                        new Integer(result.mLine), 
                        new Integer(result.mPositionInLine), 
                        result.mToken});
        
        return result;
    }
    
    public String toString()
    {
        return "Line " + mLine + ":" + mPositionInLine + ": " 
            + getErrorID() + " at input '" + mToken + "'";
    }

}
