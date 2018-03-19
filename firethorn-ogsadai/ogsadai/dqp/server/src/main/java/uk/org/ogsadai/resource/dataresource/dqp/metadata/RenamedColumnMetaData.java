//Copyright (c) The University of Edinburgh 2008.

package uk.org.ogsadai.resource.dataresource.dqp.metadata;

import uk.org.ogsadai.converters.databaseschema.ColumnMetaData;
import uk.org.ogsadai.converters.databaseschema.TableMetaData;

/**
 * Wraps a column metadata object and stores a pointer to the table metadata to
 * which the column belongs.
 * 
 * @author The OGSA-DAI Project Team.
 */
public class RenamedColumnMetaData implements ColumnMetaData
{
    
    /** Copyright statement. */
    private static final String COPYRIGHT_NOTICE =
        "Copyright (c) The University of Edinburgh 2008 - 2009.";
    
    /** Wrapped column metadata. */
    private final ColumnMetaData mMetadata;
    /** Pointer to table metadata. */
    private final TableMetaData mTable;

    /**
     * Constructor.
     * 
     * @param table
     *            table to which the column belongs
     * @param column
     *            column metadata
     */
    public RenamedColumnMetaData(TableMetaData table, ColumnMetaData column) 
    {
        mMetadata = column;
        mTable = table;
    }

    public int getColumnSize()
    {
        return mMetadata.getColumnSize();
    }

    public int getDataType()
    {
        return mMetadata.getDataType();
    }

    public int getTupleType()
    {
        return mMetadata.getTupleType();
    }

    public int getDecimalDigits()
    {
        return mMetadata.getDecimalDigits();
    }

    public String getDefaultValue()
    {
        return mMetadata.getDefaultValue();
    }

    public String getFullName()
    {
        return mMetadata.getFullName();
    }

    public String getName()
    {
        return mMetadata.getName();
    }

    public int getPosition()
    {
        return mMetadata.getPosition();
    }

    public String getSQLType()
    {
        return mMetadata.getSQLType();
    }

    public TableMetaData getTable()
    {
        return mTable;
    }

    public boolean isNullable()
    {
        return mMetadata.isNullable();
    }

    public boolean isPrimaryKey()
    {
        return mMetadata.isPrimaryKey();
    }

}