// Copyright (c) The University of Edinburgh, 2008-2012.
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

package uk.org.ogsadai.dqp.presentation.common;

import java.net.MalformedURLException;

import uk.org.ogsadai.dqp.common.ArithmeticExpressionSupport;
import uk.org.ogsadai.dqp.common.DataNode;
import uk.org.ogsadai.dqp.common.EvaluationNode;
import uk.org.ogsadai.dqp.common.ExpressionSupport;
import uk.org.ogsadai.dqp.common.OperatorSupport;
import uk.org.ogsadai.dqp.lqp.OperatorID;
import uk.org.ogsadai.dqp.lqp.udf.Function;
import uk.org.ogsadai.expression.Expression;
import uk.org.ogsadai.expression.arithmetic.ArithmeticExpression;
import uk.org.ogsadai.resource.ResourceID;

/**
 * Stores information about a DQP data resource, including location and names.  
 *
 * @author The OGSA-DAI Project Team.
 */
public class SimpleDataNode implements 
    DataNode, OperatorSupport, ExpressionSupport, ArithmeticExpressionSupport
{
    /** Copyright notice. */
    private static final String COPYRIGHT_NOTICE =
        "Copyright (c) The University of Edinburgh, 2008-2012";
    
    /** Data resource ID. */
    private final ResourceID mResourceID;
    /** Alias name for the resource. */
    private final String mAlias;
    /** Corresponding evaluation node. */
    private final EvaluationNode mService;

    /**
     * Helpers for supported operators, expressions and arithmetic expressions
     * can be specified when configuring the data node. If not set we are using
     * the default support helper for backwards compatibility.
     */
    private OperatorSupport mOperatorSupport = 
        new DefaultFeatureSupportHelper();
    /** Helper for expressions operators. */
    private ExpressionSupport mExpressionSupport = 
        new DefaultFeatureSupportHelper();
    /** Helper for supported arithmetic expressions. */
    private ArithmeticExpressionSupport mArithmeticExpressionSupport = 
        new DefaultFeatureSupportHelper();

    /**
     * Constructs a new data node.
     * 
     * @param resourceID
     *            DQP resource ID
     * @param alias
     *            an alias name for the DQP resource
     * @param evaluationNode
     *            corresponding evaluation node
     */
    public SimpleDataNode(
        String resourceID,
        String alias,
        EvaluationNode evaluationNode)
    {
        mResourceID = new ResourceID(resourceID);
        mAlias = alias;
        mService = evaluationNode;
    }
    
    /**
     * Constructs a new data node.
     * 
     * @param resourceID
     *            DQP resource ID
     * @param evaluationNode
     *            corresponding evaluation node
     */
    public SimpleDataNode(
        String resourceID,
        EvaluationNode evaluationNode)
    {
        this(resourceID, resourceID, evaluationNode);
    }
        
    /**
     * Constructs a new data node. The corresponding evaluation node will be
     * an instance of <tt>SimpleEvaluationNode</tt>.
     * 
     * @param url
     *            service base URL
     * @param drerID
     *            execution resource ID
     * @param dsos
     *            data source service name
     * @param dsis
     *            data sink service name
     * @param resourceID
     *            DQP resource ID
     * @param isLocal
     *            <tt>true</tt> if the evaluation node to local (on the same
     *            OGSA-DAI instance as the DQP resource), <tt>false</tt>
     *            if it is remote.
     * @throws MalformedURLException
     *             if the service base URL was malformed
     */
    public SimpleDataNode(
        String url, 
        String drerID, 
        String dsos, 
        String dsis, 
        String resourceID, 
        boolean isLocal)
        throws MalformedURLException
    {
        this(resourceID, 
             new SimpleEvaluationNode(url, drerID, dsos, dsis, isLocal));
    }

    /**
     * Constructs a new data node.  The corresponding evaluation node will be
     * an instance of <tt>SimpleEvaluationNode</tt>.
     * 
     * @param url
     *            service base URL
     * @param drerID
     *            execution resource ID
     * @param dsos
     *            data source service name
     * @param dsis
     *            data sink service name
     * @param resourceID
     *            DQP resource ID
     * @param alias
     *            an alias name for the DQP resource
     * @param isLocal
     *            <tt>true</tt> if the evaluation node to local (on the same
     *            OGSA-DAI instance as the DQP resource), <tt>false</tt>
     *            if it is remote.
     * @throws MalformedURLException
     *             if the service base URL was malformed
     */
    public SimpleDataNode(
            String url, 
            String drerID,
            String dsos,
            String dsis,
            String resourceID, 
            String alias,
            boolean isLocal)
        throws MalformedURLException
    {
        this(resourceID, 
             alias, 
             new SimpleEvaluationNode(url, drerID, dsos, dsis, isLocal));
    }

    /**
     * Sets the object used to determine which operators can be imploded into
     * SQL queries sent to the data mode.
     * 
     * @param operatorSupport
     *            object that specifies which operators are supported.
     */
    public void setOperatorSupport(OperatorSupport operatorSupport)
    {
        mOperatorSupport = operatorSupport;
    }
    
    /**
     * Sets the object used to determine which expressions can be imploded into
     * SQL queries sent to the data mode.
     * 
     * @param operatorSupport
     *            object that specifies which expressions are supported.
     */
    public void setExpressionSupport(ExpressionSupport expressionSupport)
    {
        mExpressionSupport = expressionSupport;
    }
    
    /**
     * Sets the object used to determine which arithmetic expressions can be
     * imploded into SQL queries sent to the data mode.
     * 
     * @param operatorSupport
     *            object that specifies which arithmetic expressions are
     *            supported
     */
    public void setArithmeticExpressionSupport(
        ArithmeticExpressionSupport arithmeticExpressionSupport)
    {
        mArithmeticExpressionSupport = arithmeticExpressionSupport;
    }

    /**
     * Returns the URL and execution resource ID of the OGSA-DAI instance.
     * 
     * @return service instance
     */
    public EvaluationNode getEvaluationNode()
    {
        return mService;
    }
    
    /**
     * ID of the data resource
     * 
     * @return resource ID
     */
    public ResourceID getResourceID()
    {
        return mResourceID;
    }

    /**
     * User-defined name to be used instead of the data resource ID.
     * 
     * @return alias name
     */
    public String getTableNamePrefix()
    {
        return mAlias;
    }

    /**
     * {@inheritDoc}
     */
    public String toString()
    {
        StringBuffer result = new StringBuffer();
        result.append("DataNode [service=");
        result.append(mService);
        result.append(", resource=");
        result.append(mResourceID);
        result.append(", alias=");
        result.append(mAlias);
        result.append("]");
        return result.toString();
    }
    
    
    /**
     * {@inheritDoc}
     */
    public boolean equals(Object that)
    {
        if (!(that instanceof SimpleDataNode)) return false;
        
        SimpleDataNode dataNode = (SimpleDataNode) that;
        
        return (
            ((this.mService == null && dataNode.mService == null) ||
             this.mService.equals(dataNode.mService)) && 
            this.mAlias.equals(dataNode.mAlias) && 
            this.mResourceID.equals(dataNode.mResourceID)); 
    }
    
    /**
     * {@inheritDoc}
     */
    public int hashCode()
    {
        int result = 17;
        result = 37*result + (mService != null ? mService.hashCode() : 12);
        result = 37*result + (mAlias != null ? mAlias.hashCode() : 12);
        result = 37*result + 
            (mResourceID != null ? mResourceID.hashCode() : 12);
        return result;
    }

    /**
     * {@inheritDoc}
     */
    public boolean supportsArithmeticExpression(ArithmeticExpression expr)
    {
        if (mArithmeticExpressionSupport == null) return true;
        return mArithmeticExpressionSupport.supportsArithmeticExpression(expr);
    }

    /**
     * {@inheritDoc}
     */
    public boolean supportsFunction(Function function)
    {
        if (mArithmeticExpressionSupport == null) return true;
        return mArithmeticExpressionSupport.supportsFunction(function);
    }
    
    /**
     * {@inheritDoc}
     */
    public boolean supportsExpression(Expression expr)
    {
        if (mExpressionSupport == null) return true;
        return mExpressionSupport.supportsExpression(expr);
    }

    /**
     * {@inheritDoc}
     */
    public boolean supportsOperator(OperatorID id)
    {
        if (mOperatorSupport == null) return true;
        return mOperatorSupport.supportsOperator(id);
    }
}
