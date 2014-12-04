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

package uk.org.ogsadai.dqp.lqp.udf.aggregate;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;

import uk.org.ogsadai.tuple.Null;
import uk.org.ogsadai.tuple.TupleTypes;
import uk.org.ogsadai.tuple.TypeMismatchException;
import uk.org.ogsadai.tuple.serialise.SerialisableFunction;

/**
 * Median aggregate function.  Returns the (n/2+1)th largest value.
 *
 * @author The OGSA-DAI Project Team
 */
public class Median extends SQLAggregateFunction
{
    /** Copyright notice. */
    private static final String COPYRIGHT_NOTICE = 
        "Copyright (c) The University of Edinburgh, 2009.";
    
    /** The data. */
    private ArrayList<Comparable> mData;
    
    /** The type of the data. */
    private int mType;
    
    /**
     * Default constructor.
     */
    public Median()
    {
        mData = new ArrayList<Comparable>();
    }
    
    /**
     * Copy constructor. The current values are not copied but the type 
     * information must be copied as the configure method will not be called.
     * 
     * @param median
     *            function to copy
     */
    public Median(Median median)
    {
        this();
        mType = median.mType;
    }
    
    // -----------------------
    // LogicalFunction methods 
    // -----------------------

    /**
     * {@inheritDoc}
     */
    public String getName()
    {
        return "MEDIAN";
    }

    // --------------------------
    // ExecutableFunction methods 
    // --------------------------

    /**
     * {@inheritDoc}
     */
    public void configure(int... types) throws TypeMismatchException 
    {
        validateType(types[0]);
        mType = types[0];
    }

    /**
     * {@inheritDoc}
     */
    public int getOutputType()
    {
        return mType;
    }

    /**
     * {@inheritDoc}
     */
    public void put(Object... parameters)
    {
        Object parameter = parameters[0];
        if (parameter != Null.VALUE)
        {
            mData.add((Comparable)parameter);
        }
    }

    /**
     * {@inheritDoc}
     */
    public Object getResult()
    {
        int size = mData.size();
        
        if (size == 0)
        {
            return Null.VALUE;
        }
        else
        {
            Collections.sort(mData);
            return mData.get(size/2);
        }
    }

    /**
     * Validates that the parameter type is numeric.
     * 
     * @param type
     *            input type
     * @throws TypeMismatchException
     *             if the type is not numeric
     */
    private void validateType(int type) throws TypeMismatchException
    {
        switch(type)
        {
            case TupleTypes._SHORT:
            case TupleTypes._INT:
            case TupleTypes._LONG:
            case TupleTypes._FLOAT:
            case TupleTypes._DOUBLE:
            case TupleTypes._BIGDECIMAL:
            case TupleTypes._BOOLEAN:
            case TupleTypes._CHAR:
            case TupleTypes._DATE:
            case TupleTypes._STRING:
            case TupleTypes._TIME:
            case TupleTypes._TIMESTAMP:
                break;
            default: 
                throw new TypeMismatchException(type);
        }
    }
    
    // ----------------------------
    // SerialisableFunction methods
    // ----------------------------
    
    /**
     * {@inheritDoc}
     */
    public void merge(SerialisableFunction function)
    {
        Median median = (Median)function;
        mData.addAll(median.mData);
    }
    
    /**
     * {@inheritDoc}
     */
    public SerialisableFunction deserialise(DataInputStream input) 
        throws IOException
    {
        Median result = new Median(this);
        
        int size = input.readInt();
        result.mType = input.readInt();
        
        for (int i=0; i<size; ++i)
        {
            switch(result.mType)
            {
                case TupleTypes._SHORT:
                    result.mData.add(new Short(input.readShort()));
                    break;
                case TupleTypes._INT:
                    result.mData.add(new Integer(input.readInt()));
                    break;
                case TupleTypes._LONG:
                    result.mData.add(new Long(input.readLong()));
                    break;
                case TupleTypes._FLOAT:
                    result.mData.add(new Float(input.readFloat()));
                    break;
                case TupleTypes._DOUBLE:
                    result.mData.add(new Double(input.readDouble()));
                    break;
                case TupleTypes._BOOLEAN:
                    result.mData.add(new Boolean(input.readBoolean()));
                    break;
                case TupleTypes._CHAR:
                    result.mData.add(new Character(input.readChar()));
                    break;
                case TupleTypes._DATE:
                    result.mData.add(new Date(input.readInt()));
                    break;
                case TupleTypes._STRING:
                    result.mData.add(input.readUTF());
                    break;
                case TupleTypes._TIME:
                    result.mData.add(new Time(input.readInt()));
                    break;
                case TupleTypes._TIMESTAMP:
                    result.mData.add(new Timestamp(input.readInt()));
                    break;
                case TupleTypes._BIGDECIMAL:
                    result.mData.add(new BigDecimal(input.readUTF()));
                    break;
            }
        }
        return result;
    }

    /**
     * {@inheritDoc}
     */
    public void serialise(DataOutputStream output) throws IOException
    {
        output.writeInt(mData.size());
        output.writeInt(mType);
        
        for(Object obj : mData)
        {
            switch(mType)
            {
                case TupleTypes._SHORT:
                    output.writeShort(((Number)obj).shortValue());
                    break;
                case TupleTypes._INT:
                    output.writeInt(((Number)obj).intValue());
                    break;
                case TupleTypes._LONG:
                    output.writeLong(((Number)obj).longValue());
                    break;
                case TupleTypes._FLOAT:
                    output.writeFloat(((Number)obj).floatValue());
                    break;
                case TupleTypes._DOUBLE:
                    output.writeDouble(((Number)obj).doubleValue());
                    break;
                case TupleTypes._BOOLEAN:
                    output.writeBoolean(((Boolean)obj).booleanValue());
                    break;
                case TupleTypes._CHAR:
                    output.writeChar(((Character)obj).charValue());
                    break;
                case TupleTypes._DATE:
                    output.writeLong(((Date)obj).getTime());
                    break;
                case TupleTypes._STRING:
                    output.writeUTF((String)obj);
                    break;
                case TupleTypes._TIME:
                    output.writeLong(((Time)obj).getTime());
                    break;
                case TupleTypes._TIMESTAMP:
                    output.writeLong(((Timestamp)obj).getTime());
                    break;
                case TupleTypes._BIGDECIMAL:
                    output.writeUTF(obj.toString());
                    break;
            }
        }
    }
    
    /**
     * {@inheritDoc}
     */
    public String toString()
    {
        return "MEDIAN()";
    }
}
