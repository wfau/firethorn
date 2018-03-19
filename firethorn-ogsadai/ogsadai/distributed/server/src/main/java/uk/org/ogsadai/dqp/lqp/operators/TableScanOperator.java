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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import uk.org.ogsadai.common.msgs.DAILogger;
import uk.org.ogsadai.dqp.common.DataDictionary;
import uk.org.ogsadai.dqp.common.DataNode;
import uk.org.ogsadai.dqp.common.DataNodeTable;
import uk.org.ogsadai.dqp.common.EvaluationNode;
import uk.org.ogsadai.dqp.lqp.Annotation;
import uk.org.ogsadai.dqp.lqp.Attribute;
import uk.org.ogsadai.dqp.lqp.OperatorID;
import uk.org.ogsadai.dqp.lqp.OperatorVisitor;
import uk.org.ogsadai.dqp.lqp.ScanOperator;
import uk.org.ogsadai.dqp.lqp.exceptions.LQPException;

/**
 * Operator TABLE_SCAN.
 * 
 * @author The OGSA-DAI Project Team.
 */
public class TableScanOperator extends UnaryOperator implements ScanOperator
{
    /** Copyright statement */
    private static final String COPYRIGHT_NOTICE = 
        "Copyright (c) The University of Edinburgh, 2008";
    
    /** Logger. */
    private static final DAILogger LOG = 
        DAILogger.getLogger(TableScanOperator.class);

    /** Candidate data node tables. */
    protected List<DataNodeTable> mDataNodeTables;
    /** Data dictionary. */
    protected DataDictionary mDataDictionary;
    /** Scan query. */
    protected TableScanQuery mQuery;
    
    /**
     * Creates a new instance of the table scan operator. The operator head is
     * obtained from the physical schema stored in the data dictionary object.
     * 
     * @param tableScanQuery
     *            query to be executed when implementing the table scan
     *            operation.
     */
    public TableScanOperator(TableScanQuery tableScanQuery)
    {
        mID = OperatorID.TABLE_SCAN;
        mQuery = tableScanQuery;
    }

    /**
     * Creates a new instance of the table scan operator. The operator head is
     * obtained from the table scan query object. If <code>tsOperator</code> is
     * not <code>null</code> then annotations are copied.
     * 
     * @param tableScanQuery
     *            query representing table scan
     * @param tsOperator
     *            existing table scan operator to copy annotations from
     */
    public TableScanOperator(TableScanQuery tableScanQuery,
	    TableScanOperator tsOperator)
    {
	this(tableScanQuery);
	
	if (tsOperator != null)
	    mAnnotations = new HashMap<String, Object>(tsOperator.mAnnotations);
    }

    /**
     * Gets the query used to implement the table scan.  As more operators get
     * imploded into this operator the query can get progressively more 
     * complex.
     * <p>
     * Implosion optimisers will typically cast the result of this call to a
     * subclass that provides more functionality.
     * 
     * @return the query that will be executed by the operator.
     */
    public TableScanQuery getQuery()
    {
        return mQuery;
    }

    /**
     * Gets the SQL query to be sent to the physical database to implement the
     * table scan operation.
     * 
     * @return SQL query to be sent to the physical database.
     */
    public String getPhysicalDatabaseQuery()
    {
        return mQuery.getPhysicalDatabaseSQLQuery(getDataNode());
    }
    
    /**
     * Gets the physical attribute associated with the given logical attribute.
     * The physical attribute is the name of the attribute in the query that
     * will be passed to the physical database. Attributes are matched using
     * the <code>AttributeMatchMode.NO_TYPE</code>.
     * 
     * @param attr logical attribute
     * 
     * @return the physical attribute corresponding to the given logical
     *         attribute, or <code>null</code> if there is no match.
     */
    public Attribute getPhysicalAttribute(Attribute attr)
    {
        return mQuery.getPhysicalAttribute(attr, getDataNode());
    }
    
    /**
     * Gets the original table name associated with the table scan. 
     * 
     * @return original table name associated with the table scan.
     */
    public String getTableName()
    {
        return mQuery.getTableName();
    }
    
    /**
     * {@inheritDoc}
     */
    public DataNode getDataNode()
    {
        LOG.debug("getDataNode()");

        DataNode dataNode = Annotation.getDataNodeAnnotation(this);
        EvaluationNode evalNode = Annotation.getEvaluationNodeAnnotation(this);
        
        LOG.debug("  DataNode [" + dataNode  + "]");
        LOG.debug("  EvalNode [" + evalNode  + "]");
        
        if(dataNode != null)
        {
            if(!getDataNodes().contains(dataNode))
            {
                throw new RuntimeException(
                    "Inconsistent data.node annotation.");
            }
            else
            {
                return dataNode;
            }
        }
        else if(evalNode != null)
        {
            for(DataNode dn : getDataNodes())
            {
                if(dn.getEvaluationNode().equals(evalNode))
                {
                    return dn;
                }
            }
            throw new RuntimeException(
                "Inconsistent evaluation.node annotation.");
        }
        else
        {
            DataNodeTable table = mDataNodeTables.get(0);
            LOG.debug("  DataNodeTable [" + table + "]");
            DataNode node = table.getDataNode();
            LOG.debug("  DataNode [" + node  + "]");
            return node;
        }
    }

    /**
     * {@inheritDoc}
     */
    public List<DataNode> getDataNodes()
    {
        List<DataNode> dataNodeList = new ArrayList<DataNode>();
        for(DataNodeTable dnt : mDataNodeTables)
        {
            dataNodeList.add(dnt.getDataNode());
        }
        return dataNodeList;
    }

    /**
     * {@inheritDoc}
     */
    public void setDataDictionary(DataDictionary dataDictionary)
    {
        mDataDictionary = dataDictionary;
        mQuery.setDataDictionary(dataDictionary);
    }

    /**
     * {@inheritDoc}
     */
    public void update() throws LQPException
    {
        if (mDataDictionary == null)
        {
            throw new IllegalStateException(
                "Data Dictionary has not been set.");
        }

        mOperatorHeading = mQuery.getHeading();
                
        mDataNodeTables = 
            mDataDictionary.getTableSchema(
                mQuery.getTableName()).getDataNodeTables();
    }

    /**
     * {@inheritDoc}
     */
    public void validate() throws LQPException
    {
        // No validation is required as table scan is always a leaf.
    }
    
    /**
     * {@inheritDoc}
     */
    public void accept(OperatorVisitor visitor)
    {
        visitor.visit(this);
    }
}
