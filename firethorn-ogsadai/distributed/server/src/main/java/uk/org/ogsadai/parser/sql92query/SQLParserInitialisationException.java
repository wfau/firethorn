//Copyright (c) The University of Edinburgh 2008.
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

import uk.org.ogsadai.exception.ErrorID;
import uk.org.ogsadai.parser.SQLParserException;

/**
 * Raised when an expression could not be parsed to due incorrect user input.
 *
 * @author The OGSA-DAI Project Team.
 */
public class SQLParserInitialisationException
    extends SQLParserException
{
    /** Copyright notice */
    private static final String COPYRIGHT_NOTICE = 
        "Copyright (c) The University of Edinburgh, 2008.";
    
    private static final ErrorID ERROR_ID = 
        new ErrorID("uk.org.ogsadai.SQL_PARSER_INITIALISATION_EXCEPTION");
    
    /**
     * Constructs a new initialisation exception with the specified cause.
     * 
     * @param cause
     *            error cause
     */
    public SQLParserInitialisationException(Throwable cause)
    {
        super(cause);
        setErrorID(ERROR_ID);
    }

}
