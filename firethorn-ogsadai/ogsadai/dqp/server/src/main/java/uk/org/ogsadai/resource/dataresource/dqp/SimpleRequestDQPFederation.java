// Copyright (c) The University of Edinburgh, 2008.
// See OGSA-DAI-Licence.txt for licencing information.

package uk.org.ogsadai.resource.dataresource.dqp;

import java.util.Set;

import uk.org.ogsadai.dqp.common.DataDictionary;
import uk.org.ogsadai.dqp.common.DataNode;
import uk.org.ogsadai.dqp.common.EvaluationNode;
import uk.org.ogsadai.dqp.common.RequestDetails;

/**
 * Request specific wrapper for a DQPFederation object. Provides only the
 * information needed by the optimisers.
 * 
 * @author The OGSA-DAI Project Team.
 */
public class SimpleRequestDQPFederation implements RequestDQPFederation
{
    /** Copyright notice. */
    private static final String COPYRIGHT_NOTICE = "Copyright (c) The University of Edinburgh, 2008";

    /** Wrapped federation object. */
    private DQPFederation mFederation;

    /** Request specific details. */
    private RequestDetails mRequestDetails;

    /**
     * Constructor.
     * 
     * @param dqpFederation
     *            federation object
     * @param requestDetails
     *            request details
     */
    public SimpleRequestDQPFederation(DQPFederation dqpFederation,
        RequestDetails requestDetails)
    {
        mFederation = dqpFederation;
        mRequestDetails = requestDetails;
    }

    /**
     * Returns the set of evaluation nodes from the configuration document. The
     * method <code>readConfiguration</code> must be called before this method,
     * otherwise it will return <code>null</code>.
     * 
     * @return evaluation nodes
     */
    public Set<EvaluationNode> getEvaluationNodes()
    {
        return mFederation.getEvaluationNodes();
    }

    /**
     * Returns the set of data nodes in the configuration document.
     * 
     * @return data nodes
     */
    public Set<DataNode> getDataNodes()
    {
        return mFederation.getDataNodes();
    }

    /**
     * Returns the local evaluation node of this DQP resource.
     * 
     * @return local node
     */
    public EvaluationNode getLocalNode()
    {
        return mFederation.getLocalNode();
    }

    /**
     * Returns the data dictionary that defines the schema of the global view of
     * the federation as well as mappings from tables to data nodes.
     * 
     * @return data dictionary
     */
    public DataDictionary getDataDictionary()
    {
        return mFederation.getDataDictionary(mRequestDetails);
    }

}
