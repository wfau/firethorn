// Copyright (c) The University of Edinburgh, 2010.
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

package uk.org.ogsadai.tuple.join;

import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import uk.org.ogsadai.common.msgs.DAILogger;
import uk.org.ogsadai.expression.Expression;
import uk.org.ogsadai.expression.ExpressionEvaluationException;
import uk.org.ogsadai.expression.IncomparableTypesException;
import uk.org.ogsadai.expression.arithmetic.ArithmeticExpression;
import uk.org.ogsadai.relational.RelationalUtils;
import uk.org.ogsadai.tuple.ColumnNotFoundException;
import uk.org.ogsadai.tuple.Null;
import uk.org.ogsadai.tuple.Tuple;
import uk.org.ogsadai.tuple.TupleMetadata;
import uk.org.ogsadai.tuple.TypeConverter;
import uk.org.ogsadai.tuple.TypeMismatchException;

/**
 * Implements a theta join. Tuples of one input stream are stored in a map
 * indexed by the value of the expression.
 * 
 * @author The OGSA-DAI Project Team.
 */
public class ThetaJoinWithRange extends Join
{
    /** Copyright notice. */
    private static final String COPYRIGHT_NOTICE = "Copyright (c) The University of Edinburgh, 2010";

    /** Logger. */
    private static final DAILogger LOG = DAILogger
            .getLogger(ThetaJoinWithRange.class);

    /** Stored tuples, indexed by results of one side of the primary expression. */
    private MultiValueTreeMap<Object, Tuple> mMap;

    /** One operand of the primary expression which provides the stored keys. */
    private ArithmeticExpression mStoredKey;
    /** The operand of the lower bound expression. */
    private ArithmeticExpression mStreamedFromKey;
    /** The operand of the upper bound expression. */
    private ArithmeticExpression mStreamedToKey;
    /** Include the upper bound of the range? */
    private boolean mIncludeTo;
    /** Include the lower bound of the range? */
    private boolean mIncludeFrom;

    private Comparator<Object> mComparator;

    /**
     * {@inheritDoc}
     * 
     * @throws ConfigurationException
     */
    public void configure(TupleMetadata left, TupleMetadata right)
            throws ColumnNotFoundException, TypeMismatchException,
            IncomparableTypesException, ConfigurationException
    {
        super.configure(left, right);
        if (mStoreLeft)
        {
            findPrimaryExpression(mCondition, left, right);
        }
        else
        {
            findPrimaryExpression(mCondition, right, left);
        }

        int storedKeyType = mStoredKey.getMetadata().getType();
        int streamedFromKeyType = mStreamedFromKey.getMetadata().getType();
        int streamedToKeyType = mStreamedToKey.getMetadata().getType();
        int resultTypePrimary = TypeConverter.getArithmeticType(storedKeyType,
                streamedFromKeyType);
        int resultTypeSecondary = TypeConverter.getArithmeticType(
                storedKeyType, streamedToKeyType);
        int resultType = TypeConverter.getArithmeticType(resultTypePrimary,
                resultTypeSecondary);

        mComparator = RelationalUtils.getComparator(resultType, resultType);
        mMap = new MultiValueTreeMap<Object, Tuple>(mComparator);

        if (storedKeyType != resultType)
        {
            mStoredKey.resetType();
            mStoredKey.setContextType(resultType);
        }
        if (streamedFromKeyType != resultType)
        {
            mStreamedFromKey.resetType();
            mStreamedFromKey.setContextType(resultType);
        }
        if (streamedToKeyType != resultType)
        {
            mStreamedToKey.resetType();
            mStreamedToKey.setContextType(resultType);
        }
    }

    /**
     * Extracts the primary expression if there is one and configures it.
     * 
     * @param expression
     *            expression from which to extract the primary expression
     * @param stored
     *            tuple metadata for one join input
     * @param streamed
     *            tuple metadata for the other join input
     * @throws ConfigurationException
     * @throws TypeMismatchException
     */
    private void findPrimaryExpression(
            Expression expression,
            TupleMetadata stored, 
            TupleMetadata streamed)
       throws ConfigurationException
    {
        RangeComparisonExtractor extractor = new RangeComparisonExtractor();
        if (LOG.isDebugEnabled())
        {
            LOG.debug("Looking for range for expression: " + expression);
        }
        boolean valid = extractor.extract(expression, stored, streamed);
        if (!valid)
        {
            LOG.debug("Failed to find a range");
            throw new ConfigurationException(
                    new NoRangeExpressionException());
        }
        try
        {
            mStoredKey = extractor.getStoredExpression();
            mStoredKey.configure(stored);
            mStreamedFromKey = extractor.getFromExpression();
            mStreamedFromKey.configure(streamed);
            mStreamedToKey = extractor.getToExpression();
            mStreamedToKey.configure(streamed);
            mIncludeFrom = extractor.includeFrom();
            mIncludeTo = extractor.includeTo();
        }
        catch (TypeMismatchException e)
        {
            throw new ConfigurationException(e);
        }
        if (LOG.isDebugEnabled())
        {
            LOG.debug("Tuple metadata left: " + stored);
            LOG.debug("Tuple metadata right: " + streamed);
            LOG.debug("Expression: " + expression);
            LOG.debug("Stored Key: " + mStoredKey);
            LOG.debug("Streamed From Key: " + mStreamedFromKey);
            LOG.debug("Streamed To Key: " + mStreamedToKey);
        }
    }

    /**
     * {@inheritDoc}
     */
    protected void storeTuple(Tuple tuple)
    {
        Object key;
        try
        {
            mStoredKey.evaluate(tuple);
            key = mStoredKey.getResult();
        }
        catch (ExpressionEvaluationException e)
        {
            if (e.canTreatAsNULL())
            {
                key = Null.getValue();
            }
            else
            {
                throw new RuntimeException(e);
            }
        }

        if (LOG.isDebugEnabled())
        {
            LOG.debug("String tuple: " + tuple);
            LOG.debug("Storing key: " + key + " of type : "
                    + key.getClass().getName());
        }
        if (key != Null.getValue())
        {
            mMap.add(key, tuple);
        }
        else
        {
            LOG.debug("Not storing tuple since key is Null");
        }
    }

    /**
     * {@inheritDoc}
     */
    protected Iterator<Tuple> getCandidateMatches(Tuple tuple)
    {
        Object fromKey = Null.getValue();
        Object toKey = Null.getValue();
        try
        {
            mStreamedFromKey.evaluate(tuple);
            fromKey = mStreamedFromKey.getResult();
        }
        catch (ExpressionEvaluationException e)
        {
            if (e.canTreatAsNULL())
            {
                fromKey = Null.getValue();
            }
            else
            {
                throw new RuntimeException(e);
            }
        }
        try
        {
            mStreamedToKey.evaluate(tuple);
            toKey = mStreamedToKey.getResult();
        }
        catch (ExpressionEvaluationException e)
        {
            if (e.canTreatAsNULL())
            {
                toKey = Null.getValue();
            }
            else
            {
                throw new RuntimeException(e);
            }
        }

        if (LOG.isDebugEnabled())
        {
            LOG.debug("Streamed tuple: " + tuple);
            LOG.debug("Streamed from key: " + fromKey 
                    + " of type : " + fromKey.getClass().getName() 
                    + " inclusive : " + mIncludeFrom);
            LOG.debug("Streamed to key: " + toKey 
                    + " of type : " + toKey.getClass().getName() 
                    + " inclusive: " + mIncludeTo);
        }

        if (fromKey != Null.VALUE && toKey != Null.VALUE
                && mComparator.compare(fromKey, toKey) < 0)
        {
            return mMap.getRange(fromKey, toKey, mIncludeFrom, mIncludeTo);
        }
        else
        {
            // Null can join with nothing so return no candidate matches
            List<Tuple> emptyList = Collections.emptyList();
            return emptyList.iterator();
        }
    }

}
