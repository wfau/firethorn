// Copyright (c) The University of Edinburgh, 2009-2010.
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

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import uk.org.ogsadai.common.msgs.DAILogger;
import uk.org.ogsadai.context.OGSADAIContext;
import uk.org.ogsadai.converters.databaseschema.ColumnMetaData;
import uk.org.ogsadai.converters.databaseschema.KeyMetaData;
import uk.org.ogsadai.converters.databaseschema.TableMetaData;
import uk.org.ogsadai.dqp.common.DataDictionary;
import uk.org.ogsadai.dqp.common.DataNode;
import uk.org.ogsadai.dqp.common.EvaluationNode;
import uk.org.ogsadai.dqp.common.LogicalSchema;
import uk.org.ogsadai.dqp.common.PhysicalSchema;
import uk.org.ogsadai.dqp.common.RequestDetails;
import uk.org.ogsadai.dqp.common.TableSchema;
import uk.org.ogsadai.dqp.common.simple.SimpleDataDictionary;
import uk.org.ogsadai.dqp.common.simple.SimpleLogicalSchema;
import uk.org.ogsadai.dqp.common.simple.SimplePhysicalSchema;
import uk.org.ogsadai.dqp.common.simple.SimpleTableSchema;
import uk.org.ogsadai.dqp.lqp.Attribute;
import uk.org.ogsadai.dqp.lqp.AttributeImpl;
import uk.org.ogsadai.dqp.lqp.udf.FunctionRepository;
import uk.org.ogsadai.resource.dataresource.dqp.DQPFederation;
import uk.org.ogsadai.resource.dataresource.dqp.DuplicateResourceNameException;

/**
 * Simple DQP Federation description that uses SimpleDQPConfigReader to read
 * a configuration file.
 *
 * @author The OGSA-DAI Project Team.
 */
public class SimpleDQPFederation implements DQPFederation
{
    /** Copyright notice. */
    private static final String COPYRIGHT_NOTICE = 
        "Copyright (c) The University of Edinburgh, 2009-2011.";

    /** Logger for this class. */
    private static final DAILogger LOG = 
        DAILogger.getLogger(SimpleDQPFederation.class);

    /** DQP configuration. */
    private DQPResourceConfiguration mConfig;
    
    /** Data dictionary. */
    private DataDictionary mDictionary;
    
    /** 
     * Flag to control whether or not we get the physical schema. Not
     * We will almost always wish to do this but during development while
     * this is flakey some users do no wish this.
     */
    private boolean mFetchPhysicalSchema = true;

    /**
     * Table schema fetcher for this DQP federation.
     */
    private TableSchemaFetcher mTableSchemaFetcher;
    
    /**
     * Constructor.
     */
    public SimpleDQPFederation()
    {
    }
    
    public void setConfiguration(DQPResourceConfiguration config)
    {
        mConfig = config;
    }
    
    public void setFetchPhysicalSchema(boolean fetch)
    {
        mFetchPhysicalSchema = fetch;
    }
    
    public void setTableSchemaFetcher(TableSchemaFetcher fetcher)
    {
        mTableSchemaFetcher = fetcher;
    }

    /**
     * {@inheritDoc}
     */
    public DataDictionary getDataDictionary(RequestDetails requestDetails) 
        throws DQPResourceConfigurationException
    {
        // lazy initialisation
        if (mDictionary == null)
        {
            try
            {
                populateDataDictionary(requestDetails);
            }
            catch (DuplicateResourceNameException e)
            {
                throw new DQPResourceConfigurationException(e);
            }
            catch (ExtractLogicalSchemaException e)
            {
                throw new DQPResourceConfigurationException(e);
            }
        }
        return mDictionary;
    }

    /**
     * {@inheritDoc}
     */
    public void refreshDataDictionary(RequestDetails requestDetails) 
        throws DQPResourceConfigurationException
    {
        try
        {
            populateDataDictionary(requestDetails);
        }
        catch (DuplicateResourceNameException e)
        {
            throw new DQPResourceConfigurationException(e);
        }
        catch (ExtractLogicalSchemaException e)
        {
            throw new DQPResourceConfigurationException(e);
        }
    }        
    
    /**
     * {@inheritDoc}
     */
    public Set<DataNode> getDataNodes()
    {
        return mConfig.getDataNodes();
    }

    /**
     * {@inheritDoc}
     */
    public Set<EvaluationNode> getEvaluationNodes()
    {
        return mConfig.getEvaluationNodes();
    }

    /**
     * {@inheritDoc}
     */
    public EvaluationNode getLocalNode()
    {
        return mConfig.getLocalNode();
    }
    
    /**
     * Fetches the table schemas from the remote resources and populates the
     * data dictionary.
     * 
     * @param requestDetails 
     *             details of parent request
     * 
     * @throws DuplicateResourceNameException
     *             if there is a duplicate resource name or alias name
     * @throws ExtractLogicalSchemaException
     *             if a problem occurred fetching the schemas
     */
    private void populateDataDictionary(RequestDetails requestDetails) 
        throws DuplicateResourceNameException, 
               ExtractLogicalSchemaException
    {
        LOG.debug("In SimpleDQPFederation#populateDataDictionary");
        
        SimpleDataDictionary dictionary = new SimpleDataDictionary();
        mDictionary = dictionary;
        dictionary.setFunctionRepository(
            (FunctionRepository) OGSADAIContext.getInstance().get(
                FunctionRepository.FUNCTION_REPOSITORY_KEY));

        try
        {
            Set<DataNode> dataNodes = getDataNodes();
            Set<String> resourceNames = new HashSet<String>();
            
            for (Iterator<DataNode> iterator = dataNodes.iterator(); 
                iterator.hasNext();)
            {
                DataNode node = iterator.next();
                if (!resourceNames.add(node.getTableNamePrefix()))
                {
                    throw new DuplicateResourceNameException(node.getTableNamePrefix());
                }
            }
            if (mTableSchemaFetcher == null)
            {
                mTableSchemaFetcher = mConfig.getTableSchemaFetcher();
            }
            for (Iterator<DataNode> iterator = dataNodes.iterator(); 
                iterator.hasNext();)
            {
                DataNode node = iterator.next();
                if (LOG.isDebugEnabled())
                {
                    LOG.debug("Fetching table schemas at " + node);
                }
                try
                {
                    List<TableMetaData> metadata = 
                        mTableSchemaFetcher.fetchSchema(node, requestDetails);
                    
                    Map<String, PhysicalSchema> nameToPhysicalMap = 
                        new HashMap<String, PhysicalSchema>();

                    if (mFetchPhysicalSchema)
                    {
                        List<PhysicalSchema> physical = 
                            mTableSchemaFetcher.fetchPhysicalSchema(
                                node, requestDetails);
                        for(PhysicalSchema ps : physical)
                        {
                            nameToPhysicalMap.put(ps.getTableName(), ps);
                        }
                    }
                    
                    for(TableMetaData md : metadata)
                    {
                        PhysicalSchema physicalSchema;
                        if (mFetchPhysicalSchema)
                        {
                            physicalSchema = 
                                nameToPhysicalMap.get(md.getName());
                        }
                        else
                        {
                            // These are simply default values if we are
                            // not fetching the physical schema
                            physicalSchema = new SimplePhysicalSchema(
                                md.getName(),
                                md.getSchemaName(),
                                10000,
                                10,
                                10);
                        }
                      
                        String federationTableName = 
                            getFederationTableName(
                                    node.getTableNamePrefix(), 
                                    md.getName());
                        TableSchema schema = new SimpleTableSchema(
                            md.getName(), 
                            node, 
                            convertToLogicalSchema(federationTableName, md), 
                            physicalSchema);
                        dictionary.add(schema);
                    }
                }
                catch (ExtractLogicalSchemaException e)
                {
                    LOG.debug("Could not access schema at " + node);
                    LOG.warn(e);
                    
                    if (LOG.isDebugEnabled())
                    {
                        StringWriter sw = new StringWriter();
                        e.printStackTrace(new PrintWriter(sw));
                        LOG.debug(sw.toString());
                    }
                }
            }
        }
        catch (Throwable e)
        {
            e.printStackTrace();
            LOG.error(e);
            throw new ExtractLogicalSchemaException(e);
        }
        
    }
    
    private LogicalSchema convertToLogicalSchema(
            String source, TableMetaData metadata)
    {
        Set<String> keyColumnNameSet = new HashSet<String>();        
        String[] primaryKeys = metadata.getPrimaryKeys();
        if(primaryKeys.length == 1)
        {
            // we can use only non composite key for attributes
            keyColumnNameSet.add(primaryKeys[0]);
        }

        KeyMetaData[] kmdArray = metadata.getImportedKeys();
        if(kmdArray != null)
        {
            for(KeyMetaData kmd : kmdArray)
            {
                keyColumnNameSet.add(kmd.getPrimaryKeyColumnName());
            }        
        }
        
        List<Attribute> attributes = new ArrayList<Attribute>();
        for (int i=1; i<=metadata.getColumnCount(); i++)
        {
            ColumnMetaData column = metadata.getColumn(i);
            int type = column.getTupleType();
            
            boolean isPrimaryKey = column.isPrimaryKey() ? true
                : keyColumnNameSet.contains(column.getName());
            
            attributes.add(
                    new AttributeImpl(
                            column.getName(), 
                            type, 
                            source,
                            isPrimaryKey));            
        }
        return new SimpleLogicalSchema(source, attributes);
    }
    
    /**
     * Gets the name of the table in the federation.
     * 
     * @param tablePrefix       table prefix
     * @param databaseTableName name of table in the database
     * 
     * @return
     */
    private String getFederationTableName(
        String tablePrefix, String databaseTableName)
    {
        String name = databaseTableName;
        name = name.replace('.', '_');
        return tablePrefix + "_" + name;
    }

}
