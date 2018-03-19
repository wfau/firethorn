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

package uk.org.ogsadai.tuple.join;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import uk.org.ogsadai.common.msgs.DAILogger;
import uk.org.ogsadai.expression.ComparisonExpression;
import uk.org.ogsadai.expression.Expression;
import uk.org.ogsadai.expression.ExpressionEvaluationException;
import uk.org.ogsadai.expression.IncomparableTypesException;
import uk.org.ogsadai.expression.arithmetic.ArithmeticExpression;
import uk.org.ogsadai.relational.RelationalUtils;
import uk.org.ogsadai.tuple.ColumnMetadata;
import uk.org.ogsadai.tuple.ColumnNotFoundException;
import uk.org.ogsadai.tuple.Null;
import uk.org.ogsadai.tuple.Tuple;
import uk.org.ogsadai.tuple.TupleMetadata;
import uk.org.ogsadai.tuple.TupleTypes;
import uk.org.ogsadai.tuple.TypeConverter;
import uk.org.ogsadai.tuple.TypeMismatchException;

/**
 * Implements a theta join. The left hand tuples are stored in a map indexed
 * by the value of the expression 
 *
 * @author The OGSA-DAI Project Team.
 */
public class ThetaJoin extends Join
{
    /** Copyright notice. */
    private static final String COPYRIGHT_NOTICE = 
        "Copyright (c) The University of Edinburgh, 2008";
    
    /** Logger. */
    private static final DAILogger LOG = DAILogger.getLogger(ThetaJoin.class);
    
    /** Stored tuples, indexed by results of one side of the primary expression. */
    private MultiValueTreeMap<Object, Tuple> mMap;

    /** Primary expression of the join condition. */
    private ComparisonExpression mPrimaryExpression;
    /** One operand of the primary expression which provides the stored keys. */
    private ArithmeticExpression mStoredKey;
    /** The other operand of the primary expression. */
    private ArithmeticExpression mStreamedKey;
    
    /**
     * {@inheritDoc}
     * @throws ConfigurationException 
     */
    public void configure(TupleMetadata left, TupleMetadata right) 
        throws ColumnNotFoundException, 
               TypeMismatchException,
               IncomparableTypesException, 
               ConfigurationException 
    {
        super.configure(left, right); 
        findPrimaryExpression(mCondition, left, right);

        int storedKeyType = mStoredKey.getMetadata().getType();
        int streamedKeyType = mStreamedKey.getMetadata().getType();        
        int resultType =
            TypeConverter.getArithmeticType(storedKeyType, streamedKeyType);
        
        mMap = new MultiValueTreeMap<Object, Tuple>(
                RelationalUtils.getComparator(resultType, resultType));
        
        if(storedKeyType != resultType)
        {
            mStoredKey.resetType();
            mStoredKey.setContextType(resultType);
        }
        if(streamedKeyType != resultType)
        {
            mStreamedKey.resetType();
            mStreamedKey.setContextType(resultType);
        }
    }

    /**
     * Extracts the primary expression if there is one and configures it.
     * 
     * @param expression
     *            expression from which to extract the primary expression
     * @param left
     *            tuple metadata for one join input
     * @param right
     *            tuple metadata for the other join input
     * @throws ConfigurationException 
     * @throws TypeMismatchException
     */
    private void findPrimaryExpression(
            Expression expression,
            TupleMetadata left, 
            TupleMetadata right)
        throws ConfigurationException
    {
        PrimaryComparisonExtractor extractor = new PrimaryComparisonExtractor();
        mPrimaryExpression = extractor.extract(expression, left, right);
        if (mPrimaryExpression != null)
        {
            try
            {
                if (mStoreLeft)
                {
                    mStoredKey = extractor.getLeftExpression();
                    mStoredKey.configure(left);
                    mStreamedKey = extractor.getRightExpression();
                    mStreamedKey.configure(right);
                }
                else
                {
                    mStoredKey = extractor.getRightExpression();
                    mStoredKey.configure(right);
                    mStreamedKey = extractor.getLeftExpression();
                    mStreamedKey.configure(left);
                }
            }
            catch (TypeMismatchException e)
            {
                throw new RuntimeException(e);
            }
            
            if (LOG.isDebugEnabled())
            {
                LOG.debug("Tuple metadata left: " + left);
                LOG.debug("Tuple metadata right: " + right);
                LOG.debug("Expression: " + expression);
                LOG.debug("Primary Expression: " + mPrimaryExpression);
                LOG.debug("Stored Key: " + mStoredKey);
                LOG.debug("Streamed Key: " + mStreamedKey);
            }
        }
        else
        {
            throw new ConfigurationException(
                    new NoPrimaryExpressionException());
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
            if(e.canTreatAsNULL())
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
            LOG.debug("Tuple value [" + tuple.toString() + "]");
            LOG.debug("Tuple class [" + tuple.getClass().getName() + "]");
            LOG.debug("Key value [" + key.toString() + "]");
            LOG.debug("Key class [" + key.getClass().getName() + "]");
            LOG.debug("Meta type [" + mStoredKey.getMetadata().getType() + "]");
            LOG.debug("Meta name [" + mStoredKey.getMetadata().getName() + "]");

            LOG.debug("Storing key [" + key + "] of type [" + key.getClass().getName() + "]");
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
        Object key = Null.getValue();
        try
        {
            mStreamedKey.evaluate(tuple);
            key = mStreamedKey.getResult();
        }
        catch (ExpressionEvaluationException e)
        {
            if(e.canTreatAsNULL())
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
            LOG.debug("Streamed tuple: " + tuple);
            LOG.debug("Streamed key: " + key + " of type : " + 
                      key.getClass().getName());
        }
        
        if (key != Null.getValue())
        {
            MultiValueTreeMapQuerier<Object, Tuple> querier = 
                new MultiValueTreeMapQuerier<Object, Tuple>(mMap);
            return querier.query(mPrimaryExpression, key);
        }
        else
        {
            // Null can join with nothing so return no candidate matches
            List<Tuple> emptyList = Collections.emptyList();
            return emptyList.iterator();
        }
    }

}
