// Copyright (c) The University of Edinburgh, 2009.
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

package uk.org.ogsadai.activity.relational;

/**
 * Holds attribute values ranges.
 *
 * @author The OGSA-DAI Project Team
 */
public class AttributeValueRange
{
    /** Copyright notice. */
    private static final String COPYRIGHT_NOTICE = 
        "Copyright (c) The University of Edinburgh, 2009.";
    
    /** Minimum value in the range. */
    private final String mMin;
    /** Maximum value in the range. */
    private final String mMax;
    
    /**
     * Constructor.
     * 
     * @param min minimum value in range.
     * @param max maximum value in range.
     */
    public AttributeValueRange(String min, String max)
    {
        mMin = min;
        mMax = max;
    }
    
    /**
     * Gets the minimum value in the range.
     * 
     * @return minimum value.
     */
    public String getMin()
    {
        return mMin;
    }
    
    /**
     * Gets the maximum value in the range.
     * 
     * @return maximum value.
     */
    public String getMax()
    {
        return mMax;
    }
}
