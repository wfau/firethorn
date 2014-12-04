//Copyright (c) The University of Edinburgh 2008-2012.
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

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import uk.org.ogsadai.dqp.common.DataDictionary;
import uk.org.ogsadai.dqp.common.DataNode;
import uk.org.ogsadai.dqp.common.LogicalSchema;
import uk.org.ogsadai.dqp.common.TableSchema;
import uk.org.ogsadai.dqp.lqp.Attribute;
import uk.org.ogsadai.dqp.lqp.Heading;
import uk.org.ogsadai.dqp.lqp.HeadingImpl;
import uk.org.ogsadai.dqp.lqp.exceptions.AttributeNotFoundException;
import uk.org.ogsadai.dqp.lqp.exceptions.TableNotFoundException;
import uk.org.ogsadai.dqp.lqp.udf.FunctionRepository;

/**
 * Simple implementation of a data dictionary which stores the table schemas
 * in memory.
 * 
 * TODO: doc
 * 
 * @author The OGSA-DAI Project Team.
 */
public class SimpleDataDictionary implements DataDictionary
{
    /** Copyright notice. */
    private static final String COPYRIGHT_NOTICE = 
        "Copyright (c) The University of Edinburgh, 2008-2012";
    
    private final Set<TableSchema> mLogicalSchemas = new HashSet<TableSchema>();
    
    private final Map<String, TableSchema> mTableMetadata = 
        new HashMap<String, TableSchema>();

    private FunctionRepository mFunctionRepository;
    
    public Set<TableSchema> getTableSchemas()
    {
        return mLogicalSchemas;
    }

    /**
     * Add a new schema to the data dictionary.
     * 
     * @param schema
     *            table schema to add
     */
    public synchronized void add(TableSchema schema)
    {
        mLogicalSchemas.add(schema);
        mTableMetadata.put(schema.getTableName(), schema);
    }

    /**
     * Remove a schema from the data dictionary.
     * 
     * @param schema
     *            table schema to remove
     */
    public synchronized void remove(TableSchema schema)
    {
        mLogicalSchemas.remove(schema);
        mTableMetadata.remove(schema.getTableName());
    }

    public Heading getHeading(String tableName)
        throws TableNotFoundException
    {
        TableSchema schema = mTableMetadata.get(tableName);
        if (schema == null)
        {
            throw new TableNotFoundException(tableName);
        }
        
        LogicalSchema metadata = schema.getSchema();
        return new HeadingImpl(metadata.getAttributes());
    }

    @Override
    public Attribute getAttribute(Attribute attribute)
            throws AttributeNotFoundException
    {
        TableSchema schema = mTableMetadata.get(attribute.getSource());
        if (schema == null)
        {
            throw new AttributeNotFoundException(attribute);
        }
        
        LogicalSchema metadata = schema.getSchema();
        Attribute result = metadata.getAttribute(attribute.getName());
        if (result == null)
        {
            throw new AttributeNotFoundException(attribute);
        }
        return result;
    }

    public TableSchema getTableSchema(String tableName) 
        throws TableNotFoundException
    {
        TableSchema schema = mTableMetadata.get(tableName);
        if (schema == null)
        {
            throw new TableNotFoundException(tableName);
        }
        return schema;
    }

    /**
     * Sets the function repository of this data dictionary.
     * 
     * @param functionRepository
     *            repository
     */
    public void setFunctionRepository(FunctionRepository functionRepository)
    {
        mFunctionRepository = functionRepository;
    }
    
    @Override
    public FunctionRepository getFunctionRepository()
    {
        return mFunctionRepository;
    }
    
    @Override
    public String toString()
    {
        StringBuilder builder = new StringBuilder();
        builder.append("---DataDictionary\n");
        builder.append("----- Logical schemas\n");
        for( TableSchema ts : mLogicalSchemas)
        {
            builder.append(ts.toString()).append("\n");
        }
        builder.append("----- table metadata\n");
        for (Entry<String, TableSchema> es : mTableMetadata.entrySet())
        {
            builder.append(es.getKey()).append(" --> ").append(es.getValue());
            builder.append("\n");           
        }
        builder.append("-----------------------");
        return builder.toString();
    }

    @Override
    public String getOriginalTableName(String tableName, DataNode dataNode)
            throws TableNotFoundException
    {
        return getTableSchema(tableName).getDataNodeTable(dataNode).getOriginalTableName();
        
    }

}
