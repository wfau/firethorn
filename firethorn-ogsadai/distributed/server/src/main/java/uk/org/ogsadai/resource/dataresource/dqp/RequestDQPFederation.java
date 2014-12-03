// Copyright (c) The University of Edinburgh, 2008.
// See OGSA-DAI-Licence.txt for licencing information.

package uk.org.ogsadai.resource.dataresource.dqp;

import java.util.Set;

import uk.org.ogsadai.dqp.common.DataDictionary;
import uk.org.ogsadai.dqp.common.DataNode;
import uk.org.ogsadai.dqp.common.EvaluationNode;

/**
 * Request specific wrapper for a DQPFederation object. Provides only the
 * information needed by the optimisers.
 * 
 * TODO: Expand description
 * 
 * @author The OGSA-DAI Project Team.
 */
public interface RequestDQPFederation
{
    /**
     * Returns the set of evaluation nodes from the configuration document. The
     * method <code>readConfiguration</code> must be called before this method,
     * otherwise it will return <code>null</code>.
     * 
     * @return evaluation nodes
     */
    public Set<EvaluationNode> getEvaluationNodes();

    /**
     * Returns the set of data nodes in the configuration document.
     * 
     * @return data nodes
     */
    public Set<DataNode> getDataNodes();

    /**
     * Returns the local evaluation node of this DQP resource.
     * 
     * @return local node
     */
    public EvaluationNode getLocalNode();

    /**
     * Returns the data dictionary that defines the schema of the global view of
     * the federation as well as mappings from tables to data nodes.
     * 
     * @return data dictionary
     */
    public DataDictionary getDataDictionary();

}
