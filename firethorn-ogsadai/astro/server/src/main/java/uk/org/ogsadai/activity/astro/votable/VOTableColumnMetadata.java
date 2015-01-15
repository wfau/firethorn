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

import uk.ac.starlink.table.ColumnInfo;
import uk.ac.starlink.table.StarTable;
import uk.org.ogsadai.resource.Resource;
import uk.org.ogsadai.resource.ResourceID;
import uk.org.ogsadai.tuple.ColumnMetadata;
import uk.org.ogsadai.tuple.TupleTypes;

/**
 * VOTable column meta data to OGSA-DAI column meta data mapping. Conversions 
 * are done with the help of <code>uk.ac.starlink.table.StarTable</code>.
 *  
 * @author The OGSA-DAI Project Team.
 *
 */
public class VOTableColumnMetadata implements ColumnMetadata
{
    /** Copyright notice. */
    private static final String COPYRIGHT_NOTICE = 
        "Copyright (c) The University of Edinburgh 2010-2013.";
    
    /** DRES URI */
    private URI mDRES;
    
    /** Index of this column. */
    private int mColumnIndex;
    
    /** Resource that this metadata is associated with. */
    private Resource mResource;
    
    /** Column info for this column. */
    private ColumnInfo mColumnInfo;
    
    /** OGSA-DAI tuple type.*/
    private final int mType;
    
    /**
     * Constructor.
     * 
     * @param starTable
     *     star table interface to VOTable data
     * @param columnIndex
     *     index of this column
     * @param dres
     *     DRES URI
     * @param resource
     *     resource that this meta data is associated with
     */
    public VOTableColumnMetadata(final StarTable starTable,
                                 final int columnIndex,
                                 final URI dres,
                                 final Resource resource)
    {
        mDRES = dres;
        mColumnIndex = columnIndex;
        mResource = resource;
        mColumnInfo = starTable.getColumnInfo(mColumnIndex);
        mType = StarTableTypeToTupleTypeMapper.getType(
                mColumnInfo.getContentClass(), mColumnInfo.isArray());
    }
    
    @Override
    public int getColumnDisplaySize() 
    {
        return mColumnInfo.getElementSize();
    }
    
    @Override
    public URI getDRES() 
    {
        return mDRES;
    }
    
    @Override
    public String getName() 
    {
        return mColumnInfo.getName();
    }

    @Override
    public int getPrecision() 
    {
        // TODO what would be a reasonable value?
        return -1;
    }

    @Override
    public ResourceID getResourceID() 
    {
        return mResource.getResourceID();
    }
    
    @Override
    public String getTableName()
    {
        return "";
    }

    @Override
    public int getType() 
    {
        return mType;
    }

    @Override
    public int isNullable() {
        return mColumnInfo.isNullable()?ColumnMetadata.COLUMN_NULLABLE:
                                        ColumnMetadata.COLUMN_NO_NULLS;
    }
    
    @Override
    public String toString() 
    {
        return "VOTableColumn(name='" + getName() + 
                "', type=" + TupleTypes.getTypeName(getType()) + ")";
    }
}
