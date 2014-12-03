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

import uk.org.ogsadai.client.toolkit.activities.relational.SortOrder;
import uk.org.ogsadai.tuple.Null;
import uk.org.ogsadai.tuple.Tuple;

/**
 * A comparable tuple based on a set of columns. The tuple contents are static.
 *
 * @author The OGSA-DAI Project Team.
 */
public class ComparableTuple implements Comparable<ComparableTuple>
{
    /** Copyright notice. */
    private static final String COPYRIGHT_NOTICE = 
        "Copyright (c) The University of Edinburgh, 2008";
    
    /** The wrapped tuple. */
    protected final Tuple mTuple;
    
    /** Cached values for comparison. */
    protected final Object[] mValues;
    
    protected SortOrder[] mSortOrders = null;
    
    /** Hash code. */
    private final Integer mHash;
    
    /**
     * Allow null and doc
     * 
     * @param tuple
     * @param columns
     * @param sortOrders
     */
    public ComparableTuple(Tuple tuple, int[] columns, SortOrder[] sortOrders)
    {
    	this(tuple, columns);
    	
		if (sortOrders != null && (columns.length != sortOrders.length))
		{
			throw new RuntimeException(
					"The size of columns array and sortOrders array must be the same.");
		}
    	mSortOrders = sortOrders;
    }
    
    /** 
     * Creates a new comparable tuple.
     * @param tuple content
     * @param columns columns to compare
     */
    public ComparableTuple(Tuple tuple, int[] columns)
    {
        mTuple = tuple;
        mValues = new Object[columns.length];
        for (int i=0; i<columns.length; i++)
        { 
            mValues[i] = tuple.getObject(columns[i]);
        }
        mHash = calculateHash();
    }

    /**
     * Creates a new tuple with the specified comparison values.
     * 
     * @param values
     *            column values
     */
    protected ComparableTuple(Object[] values)
    {
        mValues = values;
        mTuple = null;
        mHash = calculateHash();
    }
    
    protected ComparableTuple(ComparableTuple tuple)
    {
        mValues = tuple.mValues;
        mTuple = tuple.mTuple;
        mHash = tuple.mHash;
    }
    
    protected ComparableTuple(ComparableTuple tuple, SortOrder[] sortOrders)
    {
    	this(tuple);
    	mSortOrders = sortOrders;
    }

    /**
     * Returns the underlying tuple.
     * 
     * @return content tuple
     */
    public Tuple getTuple()
    {
        return mTuple;
    }

    /**
     * {@inheritDoc}
     */
    public int compareTo(ComparableTuple tuple)
    {
        if (mTuple == tuple)
        {
            return 0;
        }
        else if(mSortOrders == null)
        {
            Object[] values = tuple.mValues;
            int result = 0;
            for (int i=0; i<mValues.length; i++)
            {    
                if (values[i] != mValues[i])
                {
                    if (mValues[i] == Null.getValue())
                    {
                        result = -1;
                        break;
                    }
                    else if (values[i] == Null.getValue())
                    {
                        result = 1;
                        break;
                    }
                    else
                    {
                        result = ((Comparable)mValues[i]).compareTo(
                                    (Comparable)values[i]);
                    }
                    if (result != 0)
                    {
                        break;
                    }
                }
            }
            return result;
        }
        else
        {
        	// This is a variation of the previous block - introduces branches
        	// TODO: Check if performance differs and merge if no improvement
        	//
            Object[] values = tuple.mValues;
            int result = 0;
            for (int i=0; i<mValues.length; i++)
            {    
                if (values[i] != mValues[i])
                {
                    if (mValues[i] == Null.getValue())
                    {
                    	result = (mSortOrders[i] == SortOrder.ASC) ? -1 : 1;
                        break;
                    }
                    else if (values[i] == Null.getValue())
                    {
                    	result = (mSortOrders[i] == SortOrder.ASC) ? 1 : -1;
                        break;
                    }
                    else
                    {
                        result = ((Comparable)mValues[i]).compareTo(
                                    (Comparable)values[i]);
                        
                        if (mSortOrders[i] == SortOrder.DESC)
                        {
                        	result *= -1;
                        }
                    }
                    if (result != 0)
                    {
                        break;
                    }
                }
            }
            return result;
        }
    }
    
    /**
     * Calculates the hash code of the comparable tuple.
     * 
     * @return hashcode
     */
    private int calculateHash()
    {
        int hashCode = 1;
        for (Object value : mValues)
        {
            {
                hashCode = 31*hashCode + (value==null ? 0 : value.hashCode());
            }
        }
        return hashCode;
    }
    
    /**
     * {@inheritDoc}
     */
    public int hashCode()
    {
        return mHash;
    }
    
    /**
     * {@inheritDoc}
     */
    public boolean equals(Object object)
    {
        if (this == object)
        {
            return true;
        }
        if (object instanceof ComparableTuple)
        {
            return (this.compareTo((ComparableTuple)object) == 0);
        }
        else
        {
            return false;
        }
    }
    
    /**
     * {@inheritDoc}
     */
    public String toString()
    {
        StringBuffer result = new StringBuffer();
        result.append("[");
        int columnCount = mTuple.getColumnCount();
        if (columnCount != 0)
        {
            result.append(mTuple.getObject(0));
            for (int i=1; i<columnCount; i++)
            {
                result.append(", ");
                result.append(mTuple.getObject(i));
            }
        }
        result.append("]");
        return result.toString();
    }
    
}
