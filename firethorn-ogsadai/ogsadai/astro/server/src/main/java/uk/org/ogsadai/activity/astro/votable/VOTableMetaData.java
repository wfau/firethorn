// Copyright (c) The University of Edinburgh,  2010-2013.
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

package uk.org.ogsadai.activity.astro.votable;

import java.net.URI;

import uk.ac.starlink.table.StarTable;
import uk.org.ogsadai.resource.Resource;
import uk.org.ogsadai.tuple.ColumnIdentifier;
import uk.org.ogsadai.tuple.ColumnMetadata;
import uk.org.ogsadai.tuple.ColumnNotFoundException;
import uk.org.ogsadai.tuple.TupleMetadata;

/**
 * VOTable meta data to OGSA-DAI meta data mapping.
 * 
 * @author The OGSA-DAI Project Team.
 *
 */
public class VOTableMetaData implements TupleMetadata {

    /** Copyright notice. */
    private static final String COPYRIGHT_NOTICE = 
        "Copyright (c) The University of Edinburgh 2010-2013.";
    
    /** DRES URI. */
    private URI mDRES;
    
    /** Resource that this meta data is associated with. */
    private Resource mResource;
    
    /** Column count. */
    private int mColumnCount;
    
    /** Table name. */
    private String mName;
    
    /** Column meta objects for this table. */
    VOTableColumnMetadata[]  mColumnMetadata;
    
    /**
     * Constructor.
     * @param starTable
     *     Star table containing VOTable data.
     * @param dres
     *     DRES URI
     * @param resource
     *     resource that this meta data is associated with
     */
    public VOTableMetaData(final StarTable starTable,
                           final URI dres,
                           final Resource resource) {
        mDRES = dres;
        mResource = resource;
        mColumnCount = starTable.getColumnCount();
        mName = starTable.getName();
        mColumnMetadata = new VOTableColumnMetadata[mColumnCount];
        for (int i=0; i<mColumnCount; i++) {
            mColumnMetadata[i] = 
                new VOTableColumnMetadata(starTable, i, mDRES, mResource);
        }
    }
    
    @Override
    public int getColumnCount() 
    {
        return mColumnCount;
    }
    
    @Override
    public ColumnMetadata getColumnMetadata(int column)
            throws ColumnNotFoundException 
    {
        if (column < 0 || column >= mColumnCount)
        {
            throw new ColumnNotFoundException(column);
        }
        return mColumnMetadata[column];
    }
    
    @Override
    public ColumnMetadata getColumnMetadata(String columnName)
            throws ColumnNotFoundException
    {
        for (ColumnMetadata column : mColumnMetadata)
        {
            if (column.getName().equals(columnName))
            {
                return column;
            }
        }
        throw new ColumnNotFoundException(columnName);
    }
    
    @Override
    public ColumnMetadata getColumnMetadata(ColumnIdentifier arg0)
            throws ColumnNotFoundException 
    {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public int getColumnMetadataPosition(String columnName) 
    {
        for (int i=0; i<mColumnCount; i++)
        {
            if (columnName.equals(mColumnMetadata[i].getName()))
            {
                return i;
            }
        }
        return -1;
    }

    @Override
    public int getColumnMetadataPosition(ColumnIdentifier arg0) 
    {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public String getDescription()
    {
        return mName;
    }

    @Override
    public String toString() 
    {
        StringBuilder result = new StringBuilder();
        result.append("VOTableMetadata(");
        result.append("columns=[");
        for (int i=0; i<mColumnCount; i++)
        {
            result.append(mColumnMetadata[i]).append(", ");
        }
        result.append("])");
        return result.toString();
    }
}
