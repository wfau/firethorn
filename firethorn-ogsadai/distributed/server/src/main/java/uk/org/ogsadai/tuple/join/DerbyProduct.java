// Copyright (c) The University of Edinburgh, 2012.
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

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Set;

import uk.org.ogsadai.activity.ActivityUserException;
import uk.org.ogsadai.dqp.lqp.Attribute;
import uk.org.ogsadai.expression.Expression;
import uk.org.ogsadai.expression.ExpressionEvaluationException;
import uk.org.ogsadai.expression.ExpressionVisitor;
import uk.org.ogsadai.tuple.ColumnNotFoundException;
import uk.org.ogsadai.tuple.Tuple;
import uk.org.ogsadai.tuple.TupleMetadata;
import uk.org.ogsadai.tuple.TypeMismatchException;

/**
 * Stores data input of the product in a database and retrieves it for each
 * tuple from the streamed data input.
 * 
 * @author The OGSA-DAI Project Team.
 */
public class DerbyProduct extends DerbyJoin
{
    public DerbyProduct()
    {
        mCondition = new Expression() {
            
            @Override
            public Boolean evaluate(Tuple tuple) throws ExpressionEvaluationException 
            {
                return true;
            }
            
            @Override
            public void configure(TupleMetadata metadata,
                    Set<Attribute> correlatedAttributes)
                    throws ColumnNotFoundException, TypeMismatchException
            {
                // ignore
            }
            
            @Override
            public void configure(TupleMetadata metadata)
                    throws ColumnNotFoundException, TypeMismatchException
            {
                // ignore
            }
            
            @Override
            public void accept(ExpressionVisitor visitor)
            {
                // ignore
            }
        };
    }
    
    @Override
    protected ResultSetIterator getCandidateMatches(Tuple tuple) 
    {
        try 
        {
            if (mConnection == null)
            {
                mConnection = mTable.connect();
            }
            
            Statement statement = mConnection.createStatement();
            ResultSet resultset =
                    statement.executeQuery("SELECT * FROM " + mTable.getName());
            return new ResultSetIterator(resultset);
        }
        catch (ActivityUserException e)
        {
            throw new RuntimeException(e);
        } 
        catch (SQLException e)
        {
            throw new RuntimeException(e);
        }
    }
}
