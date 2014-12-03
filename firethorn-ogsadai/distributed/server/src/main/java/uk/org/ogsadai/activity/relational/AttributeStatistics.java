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

import java.util.LinkedList;
import java.util.List;

/**
 * Holds the statistics associated with the data for an attribute.
 *
 * @author The OGSA-DAI Project Team
 */
public class AttributeStatistics
{
    /** Copyright notice. */
    private static final String COPYRIGHT_NOTICE = 
        "Copyright (c) The University of Edinburgh, 2009.";
    
    /* Minimum. */
    private String mMin = null;
    /* Maximum. */
    private String mMax = null;
    /* Absolute values. */
    private List<String> mValues = new LinkedList<String>();
    /* Ranges of values. */
    private List<AttributeValueRange> mRanges = 
        new LinkedList<AttributeValueRange>();
    
    /**
     * Constructor.
     */
    public AttributeStatistics()
    {
        // empty
    }
    
    /**
     * Gets whether there is any data.  If the tuple stream that produced the
     * statistics had no non-null values for the attribute then the attribute
     * can have no data associated with it.
     * 
     * @return <tt>true</tt> if there is data, <tt>false</tt> otherwise.
     */
    public boolean hasData()
    {
        return (! (mMin == null));
    }
    
    /**
     * Gets the minimum data value of the attribute.
     * 
     * @return minimum data value, or <tt>null</tt> if there is no data.
     */
    public String getMin()
    {
        return mMin;
    }
    
    /**
     * Gets the maximum data value of the attribute.
     * 
     * @return maximum data value, or <tt>null</tt> if there is no data.
     */
    public String getMax()
    {
        return mMax;
    }
    
    /**
     * Gets the list of absolute values associated with the attribute. Typically
     * there will only be a list of attribute values when the number values
     * obtain from the tuple stream is small.
     * 
     * @return list of absolute values in incremental order.
     */
    public List<String> getValues()
    {
        return mValues;
    }
    
    /**
     * Gets the list of value ranges.
     * 
     * @return list of value ranges in incremental order.
     */
    public List<AttributeValueRange> getRanges()
    {
        return mRanges;
    }
    
    /**
     * Adds some data to the attribute statistics.  To maintain ordered data
     * the calls to this method must be incremental with respect to the data
     * values.
     * 
     * @param min minimum value in a range, or an absolute value, or 
     *           <tt>null</tt> if there is no data.
     *        
     * @param max minimum value in a range, or <tt>null</tt> if an absolute
     *            value is being specified or if there is no data.
     */
    public void addData(String min, String max)
    {
        if (max != null)
        {
            // range
            mMax = max;
            if (mMin == null)
            {
                mMin = min;
            }
            mRanges.add(new AttributeValueRange(min, max));
            
        }
        else if (min != null)
        {
            // value
            mMax = min;
            if (mMin == null)
            {
                mMin = min;
            }
            mValues.add(min);
        }
    }
}
