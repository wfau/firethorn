// Copyright (c) The University of Edinburgh, 2002 - 2010.
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
import uk.org.ogsadai.tuple.ColumnNotFoundException;
import uk.org.ogsadai.tuple.Null;
import uk.org.ogsadai.tuple.Tuple;
import uk.org.ogsadai.tuple.TupleMetadata;
import uk.org.ogsadai.tuple.TypeConverter;
import uk.org.ogsadai.tuple.TypeMismatchException;

/**
 * Data structure used to store a relation and probe for matches.
 * 
 * @author The OGSA-DAI Project Team
 */
public class JoinRelation
{
    /** Copyright notice. */
    private static final String COPYRIGHT_NOTICE = 
        "Copyright (c) The University of Edinburgh, 2010.";
    
    /** Logger. */
    private static final DAILogger LOG =
        DAILogger.getLogger(JoinRelation.class);    
    /** Stored tuples, indexed by results of one side of the primary expression. */
    private MultiValueTreeMap<Object, Tuple> mMap;

    /** Primary expression of the join condition. */
    private ComparisonExpression mPrimaryExpression;
    /** One operand of the primary expression which provides the stored keys. */
    private ArithmeticExpression mStoredKey;
    /** The other operand of the primary expression. */
    private ArithmeticExpression mProbingKey;

    TupleMetadata mStoredRelationMD;
    
    TupleMetadata mProbingRelationMD;
    
    /**
     * 
     * @param condition
     * @param storedRelationMD
     * @param probingRelationMD
     * @throws ColumnNotFoundException
     * @throws TypeMismatchException
     * @throws IncomparableTypesException
     * @throws ConfigurationException
     */
    public void configure(Expression condition, TupleMetadata storedRelationMD,
        TupleMetadata probingRelationMD) throws ColumnNotFoundException,
        TypeMismatchException, IncomparableTypesException,
        ConfigurationException
    {
        findPrimaryExpression(condition, storedRelationMD, probingRelationMD);

        int leftKeyType = mStoredKey.getMetadata().getType();
        int rightKeyType = mProbingKey.getMetadata().getType();
        int resultType =
            TypeConverter.getArithmeticType(leftKeyType, rightKeyType);

        mMap =
            new MultiValueTreeMap<Object, Tuple>(RelationalUtils.getComparator(
                resultType, resultType));

        if (leftKeyType != resultType)
        {
            mStoredKey.resetType();
            mStoredKey.setContextType(resultType);
        }
        if (rightKeyType != resultType)
        {
            mProbingKey.resetType();
            mProbingKey.setContextType(resultType);
        }
        
        mStoredRelationMD = storedRelationMD;
        mProbingRelationMD = probingRelationMD;
    }

    /**
     * 
     * @param tupleToStore
     */
    public void store(Tuple tupleToStore)
    {
        Object key;
        try
        {
            mStoredKey.evaluate(tupleToStore);
            key = mStoredKey.getResult();
        }
        catch (ExpressionEvaluationException e)
        {
            key = checkIfCanCoerceToNull(e);
        }

        if (key != Null.getValue())
        {
            mMap.add(key, tupleToStore);
        }
        else
        {
            LOG.debug("Not storing tuple since key is Null");
        }
    }

    /**
     * 
     * @param probingTuple
     * @return iterator over tuples.
     */
    public Iterator<Tuple> probe(Tuple probingTuple)
    {
        Object key = Null.getValue();
        try
        {
            mProbingKey.evaluate(probingTuple);
            key = mProbingKey.getResult();
        }
        catch (ExpressionEvaluationException e)
        {
            key = checkIfCanCoerceToNull(e);
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

    /**
     * 
     * @param e
     * @return
     */
    private Object checkIfCanCoerceToNull(ExpressionEvaluationException e)
    {
        if(!e.canTreatAsNULL())
        {
            return Null.getValue();
        }
        else
        {
            throw new RuntimeException(e);
        }
    }
    
    /**
     * 
     * @param expression
     * @param left
     * @param right
     * @throws ConfigurationException
     */
    private void findPrimaryExpression(Expression expression,
        TupleMetadata left, TupleMetadata right) throws ConfigurationException
    {
        PrimaryComparisonExtractor extractor = new PrimaryComparisonExtractor();
        mPrimaryExpression = extractor.extract(expression, left, right);
        if (mPrimaryExpression != null)
        {
            try
            {
                mStoredKey = extractor.getLeftExpression();
                mStoredKey.configure(left);
                mProbingKey = extractor.getRightExpression();
                mProbingKey.configure(right);
            }
            catch (TypeMismatchException e)
            {
                throw new RuntimeException(e);
            }
        }
        else
        {
            throw new ConfigurationException(new NoPrimaryExpressionException());
        }
    }

    public TupleMetadata getStoredRelationMetadata()
    {
        return mStoredRelationMD;
    }

    public TupleMetadata getProbingRelationMetadata()
    {
        return mProbingRelationMD;
    }
}
