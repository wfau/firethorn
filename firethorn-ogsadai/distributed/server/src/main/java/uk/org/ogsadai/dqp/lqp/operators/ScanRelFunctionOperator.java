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

package uk.org.ogsadai.dqp.lqp.operators;

import java.util.List;

import uk.org.ogsadai.dqp.common.DataDictionary;
import uk.org.ogsadai.dqp.common.DataNode;
import uk.org.ogsadai.dqp.lqp.HeadingImpl;
import uk.org.ogsadai.dqp.lqp.OperatorID;
import uk.org.ogsadai.dqp.lqp.OperatorVisitor;
import uk.org.ogsadai.dqp.lqp.ScanOperator;
import uk.org.ogsadai.dqp.lqp.exceptions.LQPException;
import uk.org.ogsadai.dqp.lqp.udf.LogicalFunction;

/**
 * Operator SCAN_REL_FUNCTION.
 * 
 * TODO: Incomplete implementation.
 * 
 * @author The OGSA-DAI Project Team.
 */
public class ScanRelFunctionOperator extends UnaryOperator implements
    ScanOperator
{
    /** Copyright notice. */
    private static final String COPYRIGHT_NOTICE = 
        "Copyright (c) The University of Edinburgh, 2008";

    /** Data dictionary. */
    private DataDictionary mDataDictionary;
    
    /** Function represented by this operator. */
    private LogicalFunction mRelFunction;

    /**
     * Constructor.
     */
    protected ScanRelFunctionOperator()
    {
        mID = OperatorID.SCAN_REL_FUNCTION;
    }
    
    /**
     * Constructor.
     * 
     * @param logicalFunction
     *            function represented by this operator
     */
    public ScanRelFunctionOperator(LogicalFunction logicalFunction)
    {
        this();
        mRelFunction = logicalFunction;
    }

    /**
     * {@inheritDoc}
     */
    public void setDataDictionary(DataDictionary dataDictionary)
    {
        mDataDictionary = dataDictionary;
    }
    
    /**
     * {@inheritDoc}
     */
    public void update() throws LQPException
    {
        // TODO: will need to pass data dictionary to the function?
        mOperatorHeading = new HeadingImpl(mRelFunction.getHeading());
    }

    /**
     * {@inheritDoc}
     */
    public void validate() throws LQPException
    {
        // leaf - no validation is required
    }
    
    /**
     * {@inheritDoc}
     */
    public DataNode getDataNode()
    {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * {@inheritDoc}
     */
    public List<DataNode> getDataNodes()
    {
        // TODO Auto-generated method stub
        return null;
    }
    
    /**
     * {@inheritDoc}
     */
    public void accept(OperatorVisitor visitor)
    {
        visitor.visit(this);
    }

}
