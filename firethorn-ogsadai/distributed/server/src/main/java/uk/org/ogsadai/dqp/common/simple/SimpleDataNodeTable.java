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

package uk.org.ogsadai.dqp.common.simple;

import uk.org.ogsadai.common.msgs.DAILogger;
import uk.org.ogsadai.dqp.common.DataNode;
import uk.org.ogsadai.dqp.common.DataNodeTable;
import uk.org.ogsadai.dqp.lqp.operators.TableScanOperator;

/**
 * Simple implementation of a data node table.
 *
 * @author The OGSA-DAI Project Team
 */
public class SimpleDataNodeTable implements DataNodeTable
{
    /** Copyright notice. */
    private static final String COPYRIGHT_NOTICE = 
        "Copyright (c) The University of Edinburgh, 2009.";

    /** Logger. */
    private static final DAILogger LOG = 
        DAILogger.getLogger(SimpleDataNodeTable .class);

    /** Data node associated with the table. */
    protected DataNode mDataNode;
    /** Name of the table at source. */
    protected String mOriginalTableName;

    /**
     * Constructor.
     * 
     * @param dataNode
     *    data node that contains a resource that contains the table.
     * @param originalTableName
     *    name of table at the source resource.
     */
    public SimpleDataNodeTable(
        DataNode dataNode, String originalTableName)
    {
        // ++ ZRQ
        LOG.debug("SimpleDataNodeTable()");
        LOG.debug("  DataNode  [" + dataNode + "]");
        LOG.debug("  TableName [" + originalTableName + "]");
        if (dataNode ==null)
            {
            throw new RuntimeException(
                "DataNodeTable with null DataNode"
                ); 
            }
        //-- ZRQ
        mDataNode = dataNode;
        mOriginalTableName = originalTableName;
    }
    
    /**
     * {@inheritDoc}
     */
    public DataNode getDataNode()
    {
        return mDataNode;
    }

    /**
     * {@inheritDoc}
     */
    public String getOriginalTableName()
    {
        return mOriginalTableName;
    }
}
