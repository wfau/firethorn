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

import uk.org.ogsadai.exception.DAIException;
import uk.org.ogsadai.exception.ErrorID;

/**
 * Raised when an input value that defines the size of an array was negative.
 *
 * @author The OGSA-DAI Team.
 */
public class ArraySizeInvalidException extends DAIException
{
    /** Copyright notice */
    private static final String COPYRIGHT_NOTICE = 
        "Copyright (c) The University of Edinburgh, 2007.";
    
    /** Invalid input value */
    private final int mInvalidSize;
    
    /** Input from which the invalid value was read */
    private final String mInputName;
    
    /**
     * Constructs a new exception with the given array size which is negative.
     * 
     * @param size
     *            array size
     * @param input
     *            name of the input from which the array size was read
     */
    public ArraySizeInvalidException(int size, String input)
    {
        super(ErrorID.ARRAY_SIZE_INVALID, 
                new Object[]{new Integer(size), input});
        mInvalidSize = size;
        mInputName = input;
    }
    
    /**
     * The invalid value that was read from an input.
     * 
     * @return value
     */
    public int getInvalidValue()
    {
        return mInvalidSize;
    }
    
    /**
     * Name of the input from which the invalid value was read.
     * 
     * @return input name
     */
    public String getInputName()
    {
        return mInputName;
    }
    
    
}
