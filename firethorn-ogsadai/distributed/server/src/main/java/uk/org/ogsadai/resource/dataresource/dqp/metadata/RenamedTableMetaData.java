//Copyright (c) The University of Edinburgh 2008.

package uk.org.ogsadai.resource.dataresource.dqp.metadata;

import java.util.ArrayList;
import java.util.List;

import uk.org.ogsadai.converters.databaseschema.ColumnMetaData;
import uk.org.ogsadai.converters.databaseschema.KeyMetaData;
import uk.org.ogsadai.converters.databaseschema.TableMetaData;

/**
 * 
 * TableMeta
 * Renames from tableName -> ResourceID_tableName and has a different catalog
 * 
 * TODO: javadoc
 * 
 * @author The OGSA-DAI Project Team.
 */
public class RenamedTableMetaData implements TableMetaData
{
    /** Copyright notice. */
    private static final String COPYRIGHT_NOTICE = 
        "Copyright (c) The University of Edinburgh, 2009";

    private final TableMetaData mMetadata;
    private final List<ColumnMetaData> mColumns;
    private final String mName;
    private final String mCatalog;
    
    public RenamedTableMetaData(String name, String catalog, TableMetaData copy)
    {
        mMetadata = copy;
        mName = name;
        mCatalog = catalog;
        mColumns = new ArrayList<ColumnMetaData>();
        for (int i=1; i<=mMetadata.getColumnCount(); i++)
        {
            mColumns.add(
                new RenamedColumnMetaData(this, mMetadata.getColumn(i)));
        }
    }
    
    public String getCatalogName()
    {
        return mCatalog;
    }

    public ColumnMetaData getColumn(int index)
    {
        return (ColumnMetaData)mColumns.get(index-1);
    }

    public int getColumnCount()
    {
        return mColumns.size();
    }

    public KeyMetaData[] getExportedKeys()
    {
        return mMetadata.getExportedKeys();
    }

    public KeyMetaData[] getImportedKeys()
    {
        return mMetadata.getImportedKeys();
    }

    public String getName()
    {
        return mName;
    }

    public String[] getPrimaryKeys()
    {
        return mMetadata.getPrimaryKeys();
    }

    public String getSchemaName()
    {
        return mMetadata.getSchemaName();
    }

    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append(mName + '(');
        for(int i=0; i<mColumns.size(); i++)
        {
            sb.append(mColumns.get(i).getName());
            if(i<mColumns.size()-1)
            {
                sb.append(',');
            }
        }
        sb.append(')');
        return sb.toString();
    }
}
