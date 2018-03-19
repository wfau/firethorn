// Copyright (c) The University of Edinburgh, 2007-2012.
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

package uk.org.ogsadai.tools;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import uk.org.ogsadai.common.files.FileNotFoundException;
import uk.org.ogsadai.common.files.FileUtilities;
import uk.org.ogsadai.persistence.resource.ResourceStateDAO;
import uk.org.ogsadai.resource.ResourceID;
import uk.org.ogsadai.resource.ResourceUnknownException;
import uk.org.ogsadai.resource.dataresource.DataResourceState;
import uk.org.ogsadai.resource.dataresource.dqp.SimpleDQPResourceState;
import uk.org.ogsadai.util.xml.XML;

/**
 * Client to deploy and configure DQP resources.
 * <p>
 * Syntax is as follows where <code>DIR</code> is assumed to be the
 * OGSA-DAI configuration files directory e.g.
 * <code>$CATALINA_HOME/webapps/dai/WEB-INF/etc/dai</code>.
 * </p>
 * <pre>
# Show command syntax.
$ java uk.org.ogsadai.tools.DQPEditor help
# Deploy a new DQP resource with the given ID.
$ java uk.org.ogsadai.tools.DQPEditor deploy DIR RESOURCE_ID
# Deploy a new DQP resource with the given ID and DQP configuration as in the given file.
$ java uk.org.ogsadai.tools.DQPEditor deploy DIR RESOURCE_ID CONFIG_FILE
# Add a new evaluation node at the given URL.
$ java uk.org.ogsadai.tools.DQPEditor addEvaluationNode DIR RESOURCE_ID NODE_NAME URL IS_LOCAL
# Add a new evaluation node at the given URL with the given service names.
$ java uk.org.ogsadai.tools.DQPEditor addEvaluationNode DIR RESOURCE_ID NODE_NAME URL DRER DSOS DSIS IS_LOCAL
# Add a new local evaluation node at the given URL.
$ java uk.org.ogsadai.tools.DQPEditor addLocalEvaluationNode DIR RESOURCE_ID NODE_NAME URL
# Add a new data node with the given resource ID at the given evaluation node.
$ java uk.org.ogsadai.tools.DQPEditor addDataNode DIR RESOURCE_ID EVALUATION_NODE DATA_RESOURCE_ID
# Add a new data node with the given resource ID and a given alias.
$ java uk.org.ogsadai.tools.DQPEditor addDataNode DIR RESOURCE_ID EVALUATION_NODE DATA_RESOURCE_ID ALIAS
# Fetch physical schemas as part of the data dictionary?
$ java uk.org.ogsadai.tools.DQPEditor fetchPhysicalSchemas DIR RESOURCE_ID TRUE_OR_FALSE
 * </pre>
 * <p>
 * Note that this class uses simple reflection. Each command corresponds
 * to a method and the arguments (not counting the directory) are passed
 * as strings.
 * </p>
 * @author The OGSA-DAI Project Team
 */
public class DQPEditor extends EditorBase
{
    /** Copyright statement. */
    private static final String COPYRIGHT_NOTICE = 
        "Copyright (c) The University of Edinburgh, 2007-2012.";
    private static final String SPRING_NS =
            "http://www.springframework.org/schema/beans";
    /** DQP resource template name. */
    public static final ResourceID DQP_TEMPLATE = 
        new ResourceID("uk.org.ogsadai.DQP_RESOURCE_TEMPLATE");
    /** DQP directory. */
    private static final String DQP_DIR = "dqp";
    /** Default DQP configuration file. */
    private static final String DQP_CONTEXT_TEMPLATE = "DQPContextTemplate.xml";
    /** DQP configuration file. */
    private static final String DQP_CONTEXT_FILE = "DQPContext.xml";
    /** DQP federation XML configuration. */
    private static final String DQP_FEDERATION_XML = "DQPResourceConfig.xml";
    /** Resource editor. */
    private ResourceEditor mResourceEditor;
    
    @Override
    public void initialize(File configDir) throws FileNotFoundException
    {
        super.initialize(configDir);
        mResourceEditor = new ResourceEditor();
        mResourceEditor.initialize(configDir);
    }

    /**
     * Deploy OGSA-DAI DQP resource from default template and a default
     * configuration. The template is assumed to be in
     * <code>resourceTemplates/uk.org.ogsadai.DQP_RESOURCE_TEMPLATE</code>. The
     * configuration is assumed to be in <code>dqp/DQPContextTemplate.xml</code>
     * .
     * 
     * @param resourceID
     *            New resource ID.
     * @throws ResourceUnknownException
     *             If the template cannot be found.
     * @throws FileNotFoundException
     *             If the given files cannot be found.
     * @throws IOException
     *             If any problem arises when copying files.
     */
    public void deploy(String resourceID)
    throws ResourceUnknownException, FileNotFoundException, IOException
    {
        // Check DQP directory exists.
        File dqpDir = new File(mConfigDir, DQP_DIR);
        if (! dqpDir.exists())
        {
            throw new FileNotFoundException(dqpDir.getPath());
        }
        // Check config file exists.
        File srcConfigFile = new File(dqpDir, DQP_CONTEXT_TEMPLATE);
        if (! srcConfigFile.exists())
        {
            throw new FileNotFoundException(srcConfigFile);
        }
        // Get DQP resource template.
        ResourceStateDAO dao = mResourceEditor.getDAO();
        DataResourceState dataResourceState = 
            (DataResourceState)dao.getResourceStateTemplate(DQP_TEMPLATE);
        // Create DQP directory for resource.
        String relativeResourceDir = DQP_DIR + "/" + resourceID;
        File resourceDir = new File(mConfigDir, relativeResourceDir);
        if (! resourceDir.exists())
        {
            resourceDir.mkdir();
        }
        // Copy in config file.
        String relativeConfigFile = relativeResourceDir + "/" + DQP_CONTEXT_FILE;
        File destConfigFile = new File(mConfigDir, relativeConfigFile);
        FileUtilities.copyFile(srcConfigFile, destConfigFile);
        // Set configuration properties.
        dataResourceState.setResourceID(new ResourceID(resourceID));
        dataResourceState.getConfiguration().put(
                SimpleDQPResourceState.CONFIG_PATH,
                relativeConfigFile);
        dao.insertResourceState(dataResourceState);
    }
    
    /** 
     * Deploy OGSA-DAI DQP resource from default template and the specified 
     * configuration. The template is assumed to be in
     * <code>resourceTemplates/uk.org.ogsadai.DQP_RESOURCE_TEMPLATE</code>.
     * 
     * @param resourceID
     *     New resource ID.
     * @param configFilePath
     *     DQP configuration file path.
     * @throws ResourceUnknownException
     *     If the template cannot be found.
     * @throws FileNotFoundException
     *     If the given files cannot be found.
     * @throws IOException
     *     If any problem arises when copying files.
     */
    public void deploy(String resourceID, String configFilePath)
    throws ResourceUnknownException, FileNotFoundException, IOException
    {
        // Check DQP directory exists.
        File dqpDir = new File(mConfigDir, DQP_DIR);
        if (! dqpDir.exists())
        {
            throw new FileNotFoundException(dqpDir.getPath());
        }
        // Check config file exists.
        File srcConfigFile = new File(configFilePath);
        if (! srcConfigFile.exists())
        {
            throw new FileNotFoundException(srcConfigFile);
        }
        // Get DQP resource template.
        ResourceStateDAO dao = mResourceEditor.getDAO();
        DataResourceState dataResourceState = 
            (DataResourceState)dao.getResourceStateTemplate(DQP_TEMPLATE);
        // Create DQP directory for resource.
        String relativeResourceDir = DQP_DIR + "/" + resourceID;
        File resourceDir = new File(mConfigDir, relativeResourceDir);
        if (! resourceDir.exists())
        {
            resourceDir.mkdir();
        }
        // Copy in config file.
        String relativeConfigFile = relativeResourceDir + "/" + DQP_CONTEXT_FILE;
        File destConfigFile = new File(mConfigDir, relativeConfigFile);
        FileUtilities.copyFile(srcConfigFile, destConfigFile);
        // Set configuration properties.
        dataResourceState.setResourceID(new ResourceID(resourceID));
        dataResourceState.getConfiguration().put(SimpleDQPResourceState.CONFIG_PATH,
            relativeConfigFile);
        dao.insertResourceState(dataResourceState);
    }

    /**
     * Sets the property that indicates whether the physical schemas are fetched
     * for the data dictionary.
     * 
     * @param resourceID
     *            ID of the DQP resource to configure
     * @param fetch
     *            if <code>true</code> then the physical schemas are fetched for
     *            the data dictionary
     * @throws FileNotFoundException
     *             if the configuration file could not be found
     * @throws IOException
     *             if the configuration file could not be read or written
     */
    public void fetchPhysicalSchemas(String resourceID, String fetch) 
        throws FileNotFoundException, IOException
    {
        File dqpDir = new File(mConfigDir, DQP_DIR + "/" + resourceID);
        File configFile = new File(dqpDir, DQP_CONTEXT_FILE);
        Document config = parseConfig(configFile);
        NodeList nodeList = config.getElementsByTagNameNS(SPRING_NS, "property");
        for (int i=0; i<nodeList.getLength(); i++)
        {
            Element element = (Element)nodeList.item(i);
            if ("fetchPhysicalSchema".equals(element.getAttribute("name")))
            {
                element.setAttribute("value", fetch);
            }
        }
        writeConfig(configFile, config);
    }
    
    /**
     * Configures a DQP federation with the old-style XML resource configuration
     * file.
     * 
     * @param resourceID
     *            ID of the DQP resource
     * @param configFilePath
     *            path of the XML resource configuration
     * @throws FileNotFoundException
     *             if the configuration file could not be found
     * @throws IOException
     *             if the configuration file could not be read or written
     */
    public void configureFederation(String resourceID, String configFilePath) 
        throws FileNotFoundException, IOException
    {
        // Check config file exists.
        File srcConfigFile = new File(configFilePath);
        if (! srcConfigFile.exists())
        {
            throw new FileNotFoundException(srcConfigFile);
        }
        String relativeResourceDir = DQP_DIR + "/" + resourceID;

        // Copy in config file.
        String relativeConfigFile = relativeResourceDir + "/" + DQP_FEDERATION_XML;
        File destConfigFile = new File(mConfigDir, relativeConfigFile);
        FileUtilities.copyFile(srcConfigFile, destConfigFile);
        
        setFederationFile(resourceID, relativeConfigFile);
    }
    
    public void setFederationFile(String resourceID, String relativeConfigFile) 
        throws FileNotFoundException, IOException
    {
        setFederationFile(resourceID, null, relativeConfigFile);
    }
    
    public void setFederationFile(
            String resourceID, 
            String evaluationNodeFactoryClass,
            String relativeConfigFile) 
        throws FileNotFoundException, IOException
    { 
        File dqpDir = new File(mConfigDir, DQP_DIR + "/" + resourceID);
        File configFile = new File(dqpDir, DQP_CONTEXT_FILE);
        Document config = parseConfig(configFile);
        NodeList nodeList = config.getElementsByTagNameNS(SPRING_NS, "property");
        for (int i=0; i<nodeList.getLength(); i++)
        {
            Element element = (Element)nodeList.item(i);
            if ("configuration".equals(element.getAttribute("name")))
            {
                NodeList properties = element.getChildNodes();
                for (int j=0; j<properties.getLength(); j++)
                {
                    if (properties.item(j).getNodeType() == Node.ELEMENT_NODE)
                    {
                        element.removeChild(properties.item(j));
                        Element bean = config.createElementNS(SPRING_NS, "bean");
                        bean.setAttribute("class", "uk.org.ogsadai.dqp.presentation.common.XMLDQPResourceConfiguration");
                        bean.setAttribute("init-method", "validate");
                        element.appendChild(bean);
                        Element arg = config.createElementNS(SPRING_NS, "constructor-arg");
                        arg.setAttribute("value", relativeConfigFile);
                        bean.appendChild(arg);
                        if (evaluationNodeFactoryClass != null)
                        {
                            // <constructor-arg>
                            //   <bean class="evaluationNodeFactoryClass"/>
                            // </constructor-arg>
                            arg = config.createElementNS(SPRING_NS, "constructor-arg");
                            Element factoryBean = config.createElementNS(SPRING_NS, "bean");
                            factoryBean.setAttribute("class", evaluationNodeFactoryClass);
                            arg.appendChild(factoryBean);
                            bean.appendChild(arg);
                        }
                    }
                }
                
            }
        }
        writeConfig(configFile, config);
    }
    
    /**
     * Adds a local evaluation node.
     * 
     * @param resourceID
     *            ID of the DQP resource to configure
     * @param name
     *            name of the evaluation node
     * @param url
     *            evaluation node URL
     * @throws FileNotFoundException
     *             if the configuration file could not be found
     * @throws IOException
     *             if the configuration file could not be read or written
     */
    public void addLocalEvaluationNode(
            String resourceID, 
            String name, 
            String url)
        throws FileNotFoundException, IOException
    {
        addEvaluationNode(resourceID, name, url, "true");
    }
    
    /**
     * Adds a new evaluation node to the DQP federation.
     * 
     * @param resourceID
     *            ID of the DQP resource to configure
     * @param name
     *            name of the evaluation node
     * @param url
     *            evaluation node URL
     * @param isLocal
     *            <code>true</code> if the evaluation node is local to this
     *            DQP server
     * @throws FileNotFoundException
     *             if the configuration file could not be found
     * @throws IOException
     *             if the configuration file could not be read or written
     */
    public void addEvaluationNode(
            String resourceID, 
            String name, 
            String url, 
            String isLocal)
        throws FileNotFoundException, IOException
    {
        addEvaluationNode(
                resourceID, 
                name,
                url, 
                "DataRequestExecutionResource", 
                "DataSourceService", 
                "DataSinkService", 
                isLocal);
    }
    
    /**
     * Adds a new evaluation node to the DQP federation.
     * 
     * @param resourceID
     *            ID of the DQP resource to configure
     * @param name
     *            name of the evaluation node
     * @param url
     *            evaluation node URL
     * @param isLocal
     *            <code>true</code> if the evaluation node is local to this
     *            DQP server
     * @param className
     *            class name of the evaluation node
     * @throws FileNotFoundException
     *             if the configuration file could not be found
     * @throws IOException
     *             if the configuration file could not be read or written
     */
    public void addEvaluationNode(
            String resourceID, 
            String name, 
            String url, 
            String isLocal,
            String className)
        throws FileNotFoundException, IOException
    {
        addEvaluationNode(
                resourceID, 
                name,
                url, 
                "DataRequestExecutionResource", 
                "DataSourceService", 
                "DataSinkService", 
                isLocal,
                className);
    }

    /**
     * Adds a new evaluation node to the DQP federation.
     * 
     * @param resourceID
     *            ID of the DQP resource to configure
     * @param name
     *            name of the evaluation node
     * @param url
     *            evaluation node URL
     * @param drer
     *            ID of the data request execution resource
     * @param dsos
     *            service name for accessing data sources
     * @param dsis
     *            service name for accessing data sinks
     * @param isLocal
     *            <code>true</code> if the evaluation node is local to this
     *            DQP server
     * @throws FileNotFoundException
     *             if the configuration file could not be found
     * @throws IOException
     *             if the configuration file could not be read or written
     */
    public void addEvaluationNode(
            String resourceID, 
            String name, 
            String url, 
            String drer, 
            String dsos, 
            String dsis, 
            String isLocal)
        throws FileNotFoundException, IOException
    {
        addEvaluationNode(
                resourceID, 
                name, 
                url, 
                drer, 
                dsos, 
                dsis,
                isLocal, 
                "uk.org.ogsadai.dqp.presentation.common.SimpleEvaluationNode");
    }

    /**
     * Adds a new evaluation node to the DQP federation.
     * 
     * @param resourceID
     *            ID of the DQP resource to configure
     * @param name
     *            name of the evaluation node
     * @param url
     *            evaluation node URL
     * @param drer
     *            ID of the data request execution resource
     * @param dsos
     *            service name for accessing data sources
     * @param dsis
     *            service name for accessing data sinks
     * @param isLocal
     *            <code>true</code> if the evaluation node is local to this DQP
     *            server
     * @param className
     *            class name of the EvaluationNode implementation
     * @throws FileNotFoundException
     *             if the configuration file could not be found
     * @throws IOException
     *             if the configuration file could not be read or written
     */
    public void addEvaluationNode(
            String resourceID, 
            String name, 
            String url, 
            String drer, 
            String dsos, 
            String dsis, 
            String isLocal,
            String className)
        throws FileNotFoundException, IOException
    {
        File dqpDir = new File(mConfigDir, DQP_DIR + "/" + resourceID);
        File configFile = new File(dqpDir, DQP_CONTEXT_FILE);
        Document config = parseConfig(configFile);
        
        // add the evaluation node bean
        NodeList nodeList = config.getElementsByTagNameNS(SPRING_NS, "beans");
        Element beans = (Element)nodeList.item(0);
        Element bean = config.createElementNS(SPRING_NS, "bean");
        bean.setAttribute("id", name);
        bean.setAttribute("class", className);
        Element arg = config.createElementNS(SPRING_NS, "constructor-arg");
        arg.setAttribute("name", "url");
        arg.setAttribute("value", url);
        bean.appendChild(arg);
        arg = config.createElementNS(SPRING_NS, "constructor-arg");
        arg.setAttribute("name", "drerID");
        arg.setAttribute("value", drer);
        bean.appendChild(arg);
        arg = config.createElementNS(SPRING_NS, "constructor-arg");
        arg.setAttribute("name", "dsos");
        arg.setAttribute("value", dsos);
        bean.appendChild(arg);
        arg = config.createElementNS(SPRING_NS, "constructor-arg");
        arg.setAttribute("name", "dsis");
        arg.setAttribute("value", dsis);
        bean.appendChild(arg);
        arg = config.createElementNS(SPRING_NS, "constructor-arg");
        arg.setAttribute("name", "isLocal");
        arg.setAttribute("type", "boolean");
        arg.setAttribute("value", isLocal);
        bean.appendChild(arg);
        beans.appendChild(bean);
        
        // add the evaluation node to the list
        nodeList = config.getElementsByTagNameNS(SPRING_NS, "property");
        for (int i=0; i<nodeList.getLength(); i++)
        {
            Element element = (Element)nodeList.item(i);
            if ("evaluationNodes".equals(element.getAttribute("name")))
            {
                NodeList list = element.getElementsByTagNameNS(SPRING_NS, "list");
                Element listElement = (Element)list.item(0);
                Element ref = config.createElementNS(SPRING_NS, "ref");
                ref.setAttribute("bean", name);
                listElement.appendChild(ref);
            }
        }
        
        writeConfig(configFile, config);
    }

    /**
     * Adds a new data node to the DQP federation.
     * 
     * @param resourceID
     *            ID of the DQP resource to configure
     * @param evaluationNode
     *            evaluation node that this data node belongs to
     * @param dataResource
     *            ID of the data resource to add
     * @throws FileNotFoundException
     *             if the configuration file could not be found
     * @throws IOException
     *             if the configuration file could not be read or written
     */
    public void addDataNode(
            String resourceID,
            String evaluationNode, 
            String dataResource)
        throws FileNotFoundException, IOException
    {
        addDataNode(resourceID, evaluationNode, dataResource, null);
    }
    
    /**
     * Adds a new data node to the DQP federation with the specified table
     * prefix.
     * 
     * @param resourceID
     *            ID of the DQP resource to configure
     * @param evaluationNode
     *            evaluation node that this data node belongs to
     * @param dataResource
     *            ID of the data resource to add
     * @param alias
     *            table prefix
     * @throws FileNotFoundException
     *             if the configuration file could not be found
     * @throws IOException
     *             if the configuration file could not be read or written
     */
    public void addDataNode(
            String resourceID, 
            String evaluationNode, 
            String dataResource, 
            String alias)
        throws FileNotFoundException, IOException
    {
        addDataNode(
                resourceID, 
                evaluationNode, 
                dataResource, 
                alias, 
                "defaultFeatureSupport");
    }

    /**
     * Adds a new data node to the DQP federation with the specified table
     * prefix.
     * 
     * @param resourceID
     *            ID of the DQP resource to configure
     * @param evaluationNode
     *            evaluation node that this data node belongs to
     * @param dataResource
     *            ID of the data resource to add
     * @param alias
     *            table prefix
     * @param featureSupport
     *            reference to the feature support helper bean; it is assumed
     *            that the referenced bean is available in the context
     * @throws FileNotFoundException
     *             if the configuration file could not be found
     * @throws IOException
     *             if the configuration file could not be read or written
     */
    public void addDataNode(
            String resourceID, 
            String evaluationNode, 
            String dataResource, 
            String alias,
            String featureSupport)
        throws FileNotFoundException, IOException
    {
        File dqpDir = new File(mConfigDir, DQP_DIR + "/" + resourceID);
        File configFile = new File(dqpDir, DQP_CONTEXT_FILE);
        Document config = parseConfig(configFile);
        NodeList nodeList = config.getElementsByTagNameNS(SPRING_NS, "property");
        for (int i=0; i<nodeList.getLength(); i++)
        {
            Element element = (Element)nodeList.item(i);
            if ("dataNodes".equals(element.getAttribute("name")))
            {
                NodeList list = element.getElementsByTagNameNS(SPRING_NS, "list");
                Element listElement = (Element)list.item(0);
                Element bean = config.createElementNS(SPRING_NS, "bean");
                bean.setAttribute("class", "uk.org.ogsadai.dqp.presentation.common.SimpleDataNode");
                Element arg = config.createElementNS(SPRING_NS, "constructor-arg");
                arg.setAttribute("name", "resourceID");
                arg.setAttribute("value", dataResource);
                bean.appendChild(arg);
                if (alias != null)
                {
                    arg = config.createElementNS(SPRING_NS, "constructor-arg");
                    arg.setAttribute("name", "alias");
                    arg.setAttribute("value", alias);
                    bean.appendChild(arg);
                }
                arg = config.createElementNS(SPRING_NS, "constructor-arg");
                arg.setAttribute("name", "evaluationNode");
                arg.setAttribute("ref", evaluationNode);
                bean.appendChild(arg);
                
                // Feature helper settings
                Element property = config.createElementNS(SPRING_NS, "property");
                property.setAttribute("name", "operatorSupport");
                property.setAttribute("ref", featureSupport);
                bean.appendChild(property);
                property = config.createElementNS(SPRING_NS, "property");
                property.setAttribute("name", "expressionSupport");
                property.setAttribute("ref", featureSupport);
                bean.appendChild(property);
                property = config.createElementNS(SPRING_NS, "property");
                property.setAttribute("name", "arithmeticExpressionSupport");
                property.setAttribute("ref", featureSupport);
                bean.appendChild(property);
                listElement.appendChild(bean);
            }
        }
        writeConfig(configFile, config);
    }
    
    private Document parseConfig(File configFile) throws FileNotFoundException
    {
        Document config;
        try 
        {
            config = XML.fileToDocument(configFile.toString());
        } 
        catch (java.io.FileNotFoundException e) 
        {
            throw new FileNotFoundException(configFile);
        }
        return config;
    }
    
    private void writeConfig(File configFile, Document config) 
        throws IOException
    {
        // write the modified config
        Writer writer = new FileWriter(configFile);
        writer.write(XML.toString(config));
        writer.flush();
        writer.close();
    }
    
    @Override
    public final void printSyntax()
    {
        System.out.println("DQPEditor Syntax:");
        System.out.println("$ java uk.org.ogsadai.tools.DQPEditor help");
        System.out.println("$ java uk.org.ogsadai.tools.DQPEditor deploy DIR RESOURCE_ID");
        System.out.println("$ java uk.org.ogsadai.tools.DQPEditor deploy DIR RESOURCE_ID CONFIG_FILE");
        System.out.println("$ java uk.org.ogsadai.tools.DQPEditor addEvaluationNode DIR RESOURCE_ID NODE_NAME URL IS_LOCAL");
        System.out.println("$ java uk.org.ogsadai.tools.DQPEditor addEvaluationNode DIR RESOURCE_ID NODE_NAME URL DRER DSOS DSIS IS_LOCAL");
        System.out.println("$ java uk.org.ogsadai.tools.DQPEditor addLocalEvaluationNode DIR RESOURCE_ID NODE_NAME URL");
        System.out.println("$ java uk.org.ogsadai.tools.DQPEditor addDataNode DIR RESOURCE_ID EVALUATION_NODE DATA_RESOURCE_ID");
        System.out.println("$ java uk.org.ogsadai.tools.DQPEditor addDataNode DIR RESOURCE_ID EVALUATION_NODE DATA_RESOURCE_ID ALIAS");
        System.out.println("$ java uk.org.ogsadai.tools.DQPEditor fetchPhysicalSchemas DIR RESOURCE_ID TRUE_OR_FALSE");
    }

    /**
     * Run the client.
     *
     * @param args
     *    Command-line arguments.
     * @throws Exception
     *     If any problems arise.
     */
    public static void main(String[] args) throws Exception
    {
        DQPEditor editor = new DQPEditor();
        editor.runClient(args);
    }
}
