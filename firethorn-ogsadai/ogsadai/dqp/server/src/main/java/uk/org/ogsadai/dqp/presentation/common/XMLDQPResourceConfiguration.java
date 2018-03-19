// Copyright (c) The University of Edinburgh, 2008-2011.
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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.xml.sax.SAXException;

import uk.org.ogsadai.common.msgs.DAILogger;
import uk.org.ogsadai.context.OGSADAIConstants;
import uk.org.ogsadai.context.OGSADAIContext;
import uk.org.ogsadai.dqp.bindings.xmlresconf.DQPResourceConfigType;
import uk.org.ogsadai.dqp.bindings.xmlresconf.DataResourcesType.Resource;
import uk.org.ogsadai.dqp.bindings.xmlresconf.EvaluationNodesType.Node;
import uk.org.ogsadai.dqp.common.DataNode;
import uk.org.ogsadai.dqp.common.EvaluationNode;

/**
 * XML based DQP resource configuration.
 * 
 * @author The OGSA-DAI Project Team.
 */
public class XMLDQPResourceConfiguration implements DQPResourceConfiguration
{
    /** Copyright notice. */
    private static final String COPYRIGHT_NOTICE = 
        "Copyright (c) The University of Edinburgh, 2008 - 20011";
    
    /** Logger for this class. */
    private static final DAILogger LOG = 
        DAILogger.getLogger(XMLDQPResourceConfiguration.class);

    /** Set of data nodes. */
    private Set<DataNode> mDataNodes;
    /** Set of evaluation nodes. */
    private Set<EvaluationNode> mEvalNodes;
    /** The local node. */
    private EvaluationNode mLocalNode;
    /** Default DRER name. */
    public static final String DEFAULT_DRER = "DataRequestExecutionResource";
    /** Default DSIS name. */
    public static final String DEFAULT_DSIS = "DataSinkService";
    /** Default DSOS name. */
    public static final String DEFAULT_DSOS = "DataSourceService";

    public XMLDQPResourceConfiguration() 
    {
        // default constructor
    }
    
    /**
     * Creates a new configuration.
     * 
     * @param configPath
     *            path of the configuration file which can be either an absolute
     *            path or relative to the OGSA-DAI config path.
     */
    public XMLDQPResourceConfiguration(String configPath) 
    {
        this(configPath, new SimpleEvaluationNodeFactory());
    }
    
    /**
     * Creates a new configuration.
     * 
     * @param configPath
     *            path of the configuration file which can be either an absolute
     *            path or relative to the OGSA-DAI config path.
     */
    public XMLDQPResourceConfiguration(
            String configPath, 
            EvaluationNodeFactory factory) 
    {
        // check whether path is absolute or relative
        if(!(new File(configPath)).isAbsolute())
        {
            configPath = new File((File) OGSADAIContext.getInstance().get(
                OGSADAIConstants.CONFIG_DIR), configPath).getAbsolutePath();
        }
        try
        {
            configure(new FileInputStream(new File(configPath)), factory);
        }
        catch (FileNotFoundException e)
        {
            throw new DQPResourceConfigurationException(e);
        }
    }

    /**
     * Processes configuration document represented by the input stream.
     * 
     * @param inputStream
     *            stream representing the configuration document
     * @param factory
     *            factory of evaluation nodes
     * @throws DQPResourceConfigurationException
     */
    protected void configure(
            InputStream inputStream,
            EvaluationNodeFactory factory)
    throws DQPResourceConfigurationException
    {
        mDataNodes = new HashSet<DataNode>();
        mEvalNodes = new HashSet<EvaluationNode>();

        DQPResourceConfigType config;
        try
        {
            JAXBContext jc = 
                    JAXBContext.newInstance(
                            "uk.org.ogsadai.dqp.bindings.xmlresconf");
            Unmarshaller u = jc.createUnmarshaller();

            Schema mySchema;
            SchemaFactory sf = SchemaFactory.newInstance(
                    XMLConstants.W3C_XML_SCHEMA_NS_URI);

            URL schemaURL =
                    Thread.currentThread().getContextClassLoader().getResource(
                            "schema/DQPResourceConfig.xsd");
            
            if(schemaURL != null)
            {
                mySchema = sf.newSchema(schemaURL);
                u.setSchema(mySchema);
            }
            else
            {
                if (LOG.isDebugEnabled())
                {
                    LOG.debug("DQPResourceConfig.xsd schema not found. The "
                        + "resource configuration file validation will be "
                        + "disabled. This may lead to criptic errors when "
                        + "your configuration file is invalid.");
                }
            }

            config = ((JAXBElement<DQPResourceConfigType>) 
                    u.unmarshal(inputStream)).getValue();
        }
        catch (SAXException e)
        {
            throw new DQPResourceConfigurationException(e);
        }
        catch (JAXBException e)
        {
            throw new DQPResourceConfigurationException(e);
        }

        // data nodes
        for (Resource r : config.getDataResources().getResource())
        {
            String drer = r.getDrerID() == null ? DEFAULT_DRER : r.getDrerID();
            String dsos = r.getDsos() == null ? DEFAULT_DSOS : r.getDsos();
            String dsis = r.getDsis() == null ? DEFAULT_DSIS : r.getDsis();

            EvaluationNode evalNode;
            try
            {
                evalNode = factory.createEvaluationNode(
                        r.getUrl(), drer, dsos, dsis, r.isIsLocal());
            }
            catch (MalformedURLException e)
            {
                throw new DQPResourceConfigurationException(e);
            }

            DataNode dataNode = r.getAlias() == null ?
                    new SimpleDataNode(r.getResourceID(), evalNode) :
                    new SimpleDataNode(r.getResourceID(), r.getAlias(), evalNode);

            if (r.isIsLocal())
            {
                mLocalNode = dataNode.getEvaluationNode();
            }

            mDataNodes.add(dataNode);
            mEvalNodes.add(dataNode.getEvaluationNode());

        }

        // evaluation nodes
        if (config.getEvaluationNodes() != null)
        {
            for (Node n : config.getEvaluationNodes().getNode())
            {
                String drer = n.getDrerID() == null ? DEFAULT_DRER : n
                    .getDrerID();
                String dsos = n.getDsos() == null ? DEFAULT_DSOS : n.getDsos();
                String dsis = n.getDsis() == null ? DEFAULT_DSIS : n.getDsis();

                try
                {
                    EvaluationNode evalNode = factory.createEvaluationNode(n
                        .getUrl(), drer, dsos, dsis, n.isIsLocal());
                    
                    if (n.isIsLocal())
                    {
                        mLocalNode = evalNode;
                    }
                    
                    mEvalNodes.add(evalNode);
                }
                catch (MalformedURLException e)
                {
                    throw new DQPResourceConfigurationException(e);
                }
            }
        }        
        validate();
    }

    /**
     * Validates configuration. At least one local evaluation node is expected.
     * Resource name clashes are also being detected.
     */
    private void validate()
    {
        // check if there is at least one local evaluation node
        boolean local = false;
        for(EvaluationNode en : mEvalNodes)
        {
            if (en.isLocal())
            {
                local = true;
            }
        }
        
        if(!local)
        {
            throw new DQPResourceConfigurationException(new Throwable(
                "At least one local evaluation node is needed."));
        }
        
        // check for name clashes
        Set<String> resourceNames = new HashSet<String>();
        for(DataNode dn : mDataNodes)
        {
            if (resourceNames.contains(dn.getTableNamePrefix()))
            {
                throw new DQPResourceConfigurationException(new Throwable(
                    "There is a clash in resource names"));
            }
            else
            {
                resourceNames.add(dn.getTableNamePrefix());
            }
        }
    }
    
    /**
     * Returns the set of data nodes in the configuration document.
     * 
     * @return data nodes
     */
    public Set<DataNode> getDataNodes()
    {
        return mDataNodes;
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
        return mEvalNodes;
    }
    
    /**
     * Returns the local evaluation node of this DQP resource.
     * 
     * @return local node
     */
    public EvaluationNode getLocalNode()
    {
        return mLocalNode;
    }

    /**
     * {@inheritDoc}
     */
    public TableSchemaFetcher getTableSchemaFetcher()
    {
        return new SimpleTableSchemaFetcher();
    }
    
}
