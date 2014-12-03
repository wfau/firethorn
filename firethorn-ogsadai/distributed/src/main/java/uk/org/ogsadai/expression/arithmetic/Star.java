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


package uk.org.ogsadai.expression.arithmetic;

import java.util.Set;

import uk.org.ogsadai.dqp.lqp.Attribute;
import uk.org.ogsadai.tuple.ColumnMetadata;
import uk.org.ogsadai.tuple.SimpleColumnMetadata;
import uk.org.ogsadai.tuple.Tuple;
import uk.org.ogsadai.tuple.TupleMetadata;
import uk.org.ogsadai.tuple.TypeMismatchException;

/**
 * Represents function parameter '*', for example COUNT(*).
 *
 * @author The OGSA-DAI Project Team.
 */
public class Star implements ArithmeticExpression
{
    /** Copyright notice. */
    private static final String COPYRIGHT_NOTICE =
        "Copyright (c) The University of Edinburgh, 2009";

    /** Stored tuple. */
    private Tuple mTuple;
    
    /** Result type. */
    private static final ColumnMetadata TYPE = new SimpleColumnMetadata(
            null, 
            -1, 
            0, 
            ColumnMetadata.COLUMN_NO_NULLS, 
            0);

    /**
     * {@inheritDoc}
     */
    public void accept(ArithmeticExpressionVisitor visitor)
    {
        visitor.visitStar(this);
    }

    /**
     * {@inheritDoc}
     */
    public void configure(TupleMetadata metadata)
        throws TypeMismatchException
    {
        // we don't care
    }

    /**
     * {@inheritDoc}
     */
    public void configure(
        TupleMetadata metadata, Set<Attribute> correlatedAttributes)
        throws TypeMismatchException
    {
        // we don't care
    }

    /**
     * {@inheritDoc}
     */
    public void evaluate(Tuple tuple)
    {
        mTuple = tuple;
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
    public ColumnMetadata getMetadata()
    {
        return TYPE;
    }

    /**
     * {@inheritDoc}
     */
    public Object getResult()
    {
        return mTuple;
    }

    public void resetType()
    {
        // NOOP
    }
    
    public void setContextType(int type)
    {
        // NOOP
    }
}
