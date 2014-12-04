// Copyright (c) The University of Edinburgh, 2008-2009.
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

import java.io.Reader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import uk.org.ogsadai.client.toolkit.DataValueIterator;
import uk.org.ogsadai.client.toolkit.DataValueReader;
import uk.org.ogsadai.converters.databaseschema.DatabaseSchemaMetaData;
import uk.org.ogsadai.converters.databaseschema.RelationalSchemaParseException;
import uk.org.ogsadai.converters.databaseschema.TableMetaData;
import uk.org.ogsadai.converters.databaseschema.fromxml.XMLSchemaConverter;
import uk.org.ogsadai.util.xml.XML;

/**
 * Extracts table metadata from an data value iterator and produces nice
 * table metadata objects.
 *
 * @author The OGSA-DAI Project Team
 */
public class TableMetadataResultExtractor
{
    /** Copyright notice */
    private static final String COPYRIGHT_NOTICE = 
        "Copyright (c) The University of Edinburgh, 2009.";

    /** Iterator to the data values that contain the table metadata. */
    protected DataValueIterator mDataValueIterator;
    
    /**
     * Constructor.
     * 
     * @param dataValueIterator 
     *    iterator that gives access to the data values that contain the
     *    table meta-data.
     */
    public TableMetadataResultExtractor(DataValueIterator dataValueIterator)
    {
        mDataValueIterator = dataValueIterator;
    }
    
    /**
     * Gets the metadata for the tables.
     * 
     * @return table metadata for the tables.
     * 
     * @throws RelationalSchemaParseException
     *    if the relational schema metadata could not be parsed.
     */
    public List<TableMetaData> getTableMetaData() 
        throws RelationalSchemaParseException
    {
        Reader reader = new DataValueReader(mDataValueIterator, 1);
        
        Document schema = XML.toDocument(new InputSource(reader));
        DatabaseSchemaMetaData dbSchema = 
            XMLSchemaConverter.convert(schema.getDocumentElement());
        
        List<TableMetaData> metadataList = new ArrayList<TableMetaData>();
        Collection tableMetadataCollection = dbSchema.getTables().values();
        
        for(Iterator it = tableMetadataCollection.iterator(); it.hasNext();)
        {
            metadataList.add((TableMetaData) it.next());
        }
        return metadataList;
    }
}
