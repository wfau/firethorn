package uk.org.ogsadai.activity.astro;

import java.io.IOException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import uk.org.ogsadai.converters.databaseschema.ColumnMetaData;
import uk.org.ogsadai.converters.databaseschema.ColumnMetaDataImpl;
import uk.org.ogsadai.converters.databaseschema.DatabaseSchemaMetaData;
import uk.org.ogsadai.converters.databaseschema.TableMetaData;
import uk.org.ogsadai.converters.databaseschema.TableMetaDataImpl;
import uk.org.ogsadai.tuple.TupleUtilities;

public class MetadataConverter 
{
    
    private static final Logger LOG = Logger.getLogger(MetadataConverter.class);
    private static final Map<String, Integer> DATATYPE_TO_SQLTYPE = 
        new HashMap<String, Integer>();

    static 
    {
        DATATYPE_TO_SQLTYPE.put("bit", Types.BIT);
        DATATYPE_TO_SQLTYPE.put("unsignedByte", Types.TINYINT);
        DATATYPE_TO_SQLTYPE.put("tinyint", Types.TINYINT);
        DATATYPE_TO_SQLTYPE.put("smallint", Types.SMALLINT);
        DATATYPE_TO_SQLTYPE.put("short", Types.SMALLINT);
        DATATYPE_TO_SQLTYPE.put("int", Types.INTEGER);
        DATATYPE_TO_SQLTYPE.put("bigint", Types.BIGINT);
        DATATYPE_TO_SQLTYPE.put("long", Types.BIGINT);
        DATATYPE_TO_SQLTYPE.put("float", Types.FLOAT);
        DATATYPE_TO_SQLTYPE.put("real", Types.REAL);
        DATATYPE_TO_SQLTYPE.put("double", Types.DOUBLE);
        DATATYPE_TO_SQLTYPE.put("numeric", Types.NUMERIC);
        DATATYPE_TO_SQLTYPE.put("decimal", Types.DECIMAL);
        DATATYPE_TO_SQLTYPE.put("char", Types.CHAR);
        DATATYPE_TO_SQLTYPE.put("varchar", Types.VARCHAR);
        DATATYPE_TO_SQLTYPE.put("string", Types.VARCHAR);
        DATATYPE_TO_SQLTYPE.put("longvarchar", Types.LONGNVARCHAR);
        DATATYPE_TO_SQLTYPE.put("date", Types.DATE);
        DATATYPE_TO_SQLTYPE.put("time", Types.TIME);
        DATATYPE_TO_SQLTYPE.put("timestamp", Types.TIMESTAMP);
        DATATYPE_TO_SQLTYPE.put("dateTime", Types.TIMESTAMP);
        DATATYPE_TO_SQLTYPE.put("binary", Types.BINARY);
        DATATYPE_TO_SQLTYPE.put("varbinary", Types.VARBINARY);
        DATATYPE_TO_SQLTYPE.put("longvarbinary", Types.LONGVARBINARY);
        DATATYPE_TO_SQLTYPE.put("longvarbinary", Types.NULL);
        DATATYPE_TO_SQLTYPE.put("other", Types.OTHER);
        DATATYPE_TO_SQLTYPE.put("JAVA_OBJECT", Types.JAVA_OBJECT);
        DATATYPE_TO_SQLTYPE.put("distinct", Types.DISTINCT);
        DATATYPE_TO_SQLTYPE.put("struct", Types.STRUCT);
        DATATYPE_TO_SQLTYPE.put("array", Types.ARRAY);
        DATATYPE_TO_SQLTYPE.put("blob", Types.BLOB);
        DATATYPE_TO_SQLTYPE.put("clob", Types.CLOB);
        DATATYPE_TO_SQLTYPE.put("boolean", Types.BOOLEAN);
    }
    
    /**
     * Creates a merged metadoc.
     * 
     * @param tapServers
     *            details of the TAP endpoints in the federation
     * @return metadoc document
     * @throws ParserConfigurationException
     * @throws SAXException
     * @throws IOException
     */
    public static DatabaseSchemaMetaData createMetadata(Document tablesDoc) 
    {
        NodeList tables = tablesDoc.getElementsByTagNameNS("", "table");
        Map<String, TableMetaData> tablesMetadata = new HashMap<String, TableMetaData>();
        for (int i=0; i<tables.getLength(); i++)
        {
            Element table = (Element)tables.item(i);
            String tableName = table.getElementsByTagName("name").item(0).getTextContent();
            TableMetaDataImpl tableMetadata = new TableMetaDataImpl("", "", tableName);
            
            List<ColumnMetaData> columnsMetadata = new ArrayList<ColumnMetaData>();
            NodeList columns = table.getElementsByTagNameNS("", "column");
            for (int j=0; j<columns.getLength(); j++)
            {
                Element column = (Element)columns.item(j);
                String colName = column.getElementsByTagName("name").item(0).getTextContent();
                ColumnMetaDataImpl columnMetadata = new ColumnMetaDataImpl(colName, j, tableMetadata);
                NodeList dataTypeNodes = column.getElementsByTagName("dataType");
                if (dataTypeNodes.getLength() > 0)
                {
                    String dataType = convertDataType(dataTypeNodes.item(0));
                    if (dataType == null)
                    {
                        if (LOG.isDebugEnabled())
                        {
                            LOG.debug("Ignoring column '" + colName 
                                    + "', unsupported data type: "
                                    + dataTypeNodes.item(0).getTextContent());
                        }
                        continue;
                    }
                    Integer dsaType = DATATYPE_TO_SQLTYPE.get(dataType);
                    if (dsaType == null)
                    {
                        if (LOG.isDebugEnabled())
                        {
                            LOG.debug("Ignoring column '" + colName 
                                    + "', unsupported data type: "
                                    + dataTypeNodes.item(0).getTextContent());
                        }
                        continue;
                    }
                    columnMetadata.setDataType(dsaType);
                    columnMetadata.setTupleType(
                            TupleUtilities.mapSQLTypeToODTupleType(
                                    dsaType, dataType));
                    columnMetadata.setTypeName(dataType);
                }
                columnMetadata.setColumnSize(-1);
                columnMetadata.setDecimalDigits(-1);
                columnMetadata.setNullable(true);
                NodeList desc = column.getElementsByTagName("description");
                if (desc.getLength() > 0)
                {
                    columnMetadata.setFullName(desc.item(0).getTextContent());
                }
                columnsMetadata.add(columnMetadata);
            }

            tableMetadata.setColumns((ColumnMetaData[])
                    columnsMetadata.toArray(
                            new ColumnMetaData[columnsMetadata.size()]));
            tablesMetadata.put(tableName, tableMetadata);
        }
        DatabaseSchemaMetaData result = new DatabaseSchemaMetaData();
        result.setTables(tablesMetadata);
        result.setTableNames(
                (String[])tablesMetadata.keySet().toArray(
                        new String[tablesMetadata.size()]));
        return result;
    }
    
    private static String convertDataType(Node dataTypeNode) 
    {
        Element element = (Element) dataTypeNode;
        String dataType = dataTypeNode.getTextContent();
        if (element.hasAttributeNS("http://www.w3.org/2001/XMLSchema-instance", "type"))
        {
            String type = element.getAttributeNS(
                    "http://www.w3.org/2001/XMLSchema-instance", "type");
            if (type.endsWith("TAPType"))
            {
                // use the TAP type converter
                dataType = VOTableDataTypeConverter.TAP_TYPE_TO_DSA.get(dataType);
            }
            else if (type.endsWith("VOTableType"))
            {
                // just copy the type
            }
        }
        
        if (VOTableDataTypeConverter.DSA_TYPES.contains(dataType))
        {
            return dataType;
        }
        else
        {
            return null;
        }
    }

}
