// Copyright (c) The University of Edinburgh, 2007 - 2008.
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

import java.util.Collections;
import java.util.Set;

import uk.org.ogsadai.dqp.lqp.Attribute;
import uk.org.ogsadai.dqp.lqp.AttributeImpl;
import uk.org.ogsadai.dqp.lqp.AttributeMatchMode;
import uk.org.ogsadai.expression.ExpressionEvaluationException;
import uk.org.ogsadai.tuple.ColumnMetadata;
import uk.org.ogsadai.tuple.ColumnNotFoundException;
import uk.org.ogsadai.tuple.SimpleColumnMetadata;
import uk.org.ogsadai.tuple.Tuple;
import uk.org.ogsadai.tuple.TupleMetadata;
import uk.org.ogsadai.tuple.TypeConversionException;
import uk.org.ogsadai.tuple.TypeConverter;
import uk.org.ogsadai.tuple.TypeMismatchException;

/** 
 * Represents a table column in an arithmetic expression.
 *
 * @author The OGSA-DAI Project Team.
 */
public class TableColumn implements ArithmeticExpression
{
    /** Copyright notice */
    private static final String COPYRIGHT_NOTICE = 
        "Copyright (c) The University of Edinburgh, 2007 - 2008.";
    
    /** Type of this table column. */
    private ColumnMetadata mType;
    /** Type of this table column. */
    private ColumnMetadata mOriginalType;
    /** Name of the column. */
    private String mName;
    /** Source of the column. */
    private String mSource;
    /** Index of the column in an input tuple. */
    private int mColumnIndex;
    /** Current column value. */
    private Object mValue;
    /** Context dependent type. */
    private int mContextType = -1;
    /** Context dependent value. */
    private Object mContextValue;

    /**
     * Constructs a new table column with the given type and column index.
     * 
     * @param name
     *            full name of the column including the table name 
     */
    public TableColumn(String name)
    {
        int dot = name.indexOf(".");
        if ( dot > -1 )
        {
            mName = name.substring(dot+1);
            mSource = name.substring(0, dot);
        }
        else
        {
            mName = name;
            mSource = null;
        }
    }
    
    /**
     * Constructs a table column with the specified name and source table.
     * 
     * @param name
     *            column name
     * @param source
     *            source table
     */
    public TableColumn(String name, String source)
    {
        mName = name;
        mSource = source;
    }
    
    /**
     * Constructs a copy of the given table column.
     * 
     * @param column
     *            table column
     */
    public TableColumn(TableColumn column) 
    {
        mName = column.mName;
        mSource = column.mSource;
        mColumnIndex = column.mColumnIndex;
        mType = column.mType;
        mValue = column.mValue;
    }

    /**
     * Constructs a table column from an attribute.
     * 
     * @param attribute attribute
     */
    public TableColumn(Attribute attribute)
    {
        mName = attribute.getName();
        mSource = attribute.getSource();
        mType = new SimpleColumnMetadata(
            null, 
            attribute.getType(), 
            0,
            ColumnMetadata.COLUMN_NO_NULLS, 
            0);
    }
    
    /**
     * {@inheritDoc}
     */
    public ArithmeticExpression[] getChildren()
    {
        return new ArithmeticExpression[0];
    }

    /**
     * {@inheritDoc}
     */
    public void configure(TupleMetadata metadata) throws TypeMismatchException
    {
        Set<Attribute> emptySet = Collections.emptySet();
        configure(metadata, emptySet);
    }
    
    /**
     * {@inheritDoc}
     */
    public void configure(
        TupleMetadata metadata, Set<Attribute> correlatedAttributes) 
        throws TypeMismatchException
    {
        try
        {
            mColumnIndex = getColumnIndex(metadata);
            mType = metadata.getColumnMetadata(mColumnIndex);
            mOriginalType = mType;
        }
        catch(ColumnNotFoundException e)
        {
            // If it is correlated then we can attempt to continue
            Attribute thisColumnAsAttribute = new AttributeImpl(mName, mSource);
            boolean foundCorrelatedAttribute = false;
            for (Attribute attr : correlatedAttributes)
            {
                if (thisColumnAsAttribute.equals(
                    attr, AttributeMatchMode.NAME_AND_NULL_SOURCE))
                {
                    // This is an correlated attribute
                    mType = mOriginalType = new SimpleColumnMetadata(
                        mName, 
                        attr.getType(), 
                        0,  // precision
                        ColumnMetadata.COLUMN_NULLABLE_UNKNOWN,
                        0);  // display size
                    foundCorrelatedAttribute = true;
                    break;
                }
            }
            if (!foundCorrelatedAttribute)
            {
                throw e;
            }
        }
    }

    /**
     * Returns the column index of this column.
     * 
     * @param metadata
     *            tuple metadata
     * @return column index
     */
    public int getColumnIndex(TupleMetadata metadata)
    {
        int result = -1;
        for (int i=0; i<metadata.getColumnCount(); i++)
        {
            ColumnMetadata column = metadata.getColumnMetadata(i);
            String name = column.getName();

            if (mSource == null)
            {
                if (name.equals(mName))
                {
                    if (result != -1)
                    {
                        throw new ColumnNameAmbiguousException(mName);
                    }
                    result = i;
                }
            }
            else
            {
                if (name.equals(mName) && mSource.equals(column.getTableName()))
                {
                    return i;
                }
            }
        }
        if (result != -1)
        {
            return result;
        }
        else
        {
            throw new ColumnNotFoundException(mSource + "." + mName);
        }
    }

    /**
     * {@inheritDoc}
     */
    public void evaluate(Tuple tuple) throws ExpressionEvaluationException
    {
        mValue = tuple.getObject(mColumnIndex);
        if (mContextType != -1)
        {
            try
            {
                mContextValue = TypeConverter.convertObject(
                    mOriginalType.getType(), mContextType, mValue);
            }
            catch (TypeConversionException e)
            {
                throw new ExpressionEvaluationException(e);
            }
        }
    }
    
    /**
     * {@inheritDoc}
     */
    public Object getResult()
    {
        if(mContextType != -1)
        {
            return mContextValue;
        }
        else
        {
            return mValue;
        }
    }

    /**
     * {@inheritDoc}
     */
    public ColumnMetadata getMetadata()
    {
        return mType;
    }

    /**
     * {@inheritDoc}
     */
    public void accept(ArithmeticExpressionVisitor visitor)
    {
        visitor.visitTableColumn(this);
    }
    
    /**
     * Returns the column name.
     * 
     * @return column name
     */
    public String getName()
    {
        return mName;
    }
    
    /**
     * Returns the table name.
     * 
     * @return table name or <code>null</code> if this is a derived column
     */
    public String getSource()
    {
        return mSource;
    }
    
    /**
     * Sets new name and source.
     * 
     * @param newName
     *            new column name
     * @param newSource
     *            new column source
     */
    public void rename(String newName, String newSource)
    {
        mName = newName;
        mSource = newSource;
    }

    /**
     * {@inheritDoc}
     */
    public void setContextType(int type)
    {
        if (type != mType.getType())
        {    
            mContextType = type;
    
            mType = new SimpleColumnMetadata(null, mContextType, 0,
                ColumnMetadata.COLUMN_NO_NULLS, 0);
        }
    }
    
    /**
     * {@inheritDoc}
     */
    public void resetType()
    {
        mContextType = -1;
        mType = mOriginalType;
    }
    
    /**
     * {@inheritDoc}
     */
    public String toString()
    {
        return "COLUMN(" + mSource + "." + mName + ", index=" + mColumnIndex + ")";
    }

    /**
     * {@inheritDoc}
     */
    public int hashCode()
    {
        int hash = 3;
        hash = 29 * hash + (this.mName != null ? this.mName.hashCode() : 0);
        hash = 29 * hash + (this.mSource != null ? this.mSource.hashCode() : 0);
        return hash;
    }

    /**
     * {@inheritDoc}
     */
    public boolean equals(Object that)
    {
        if (this == that)
        {
            return true;
        }
        else if (!(that instanceof TableColumn))
        {
            return false;
        }
        
        TableColumn tc = (TableColumn) that;
        
        // null matches all sources
        if (this.mSource != null && tc.mSource != null
            && !this.mSource.equals(tc.mSource))
        {
            return false;
        }
        
        if (mName == null)
        {
            if (tc.mName != null)
            {
                return false;
            }
        }
        else if (tc.mName == null || !tc.mName.equals(this.mName))
        {
            return false;
        }
        
        return true;
    }

}
