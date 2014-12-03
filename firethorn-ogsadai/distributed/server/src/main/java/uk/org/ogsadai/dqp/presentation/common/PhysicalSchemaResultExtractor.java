// Copyright (c) The University of Edinburgh, 2008.
// See OGSA-DAI-Licence.txt for licencing information.

package uk.org.ogsadai.dqp.presentation.common;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import uk.org.ogsadai.dqp.common.PhysicalSchema;
import uk.org.ogsadai.dqp.common.simple.SimplePhysicalSchema;

/**
 * Extracts physical schema objects from the XML document returned from the
 * ExtractPhysicalSchemaToXML activity.
 * 
 * @author The OGSA-DAI Project Team.
 */
public class PhysicalSchemaResultExtractor
{
    /** Copyright notice. */
    private static final String COPYRIGHT_NOTICE = 
        "Copyright (c) The University of Edinburgh, 2008";

    /**
     * Parses physicalSchema document and returns a list of physical schema
     * documents.
     * 
     * @param physicalSchema physicalSchema document
     * @return
     *      a list of PhysicalSchema objects
     */
    public static List<PhysicalSchema> extractPhysicalSchema(
        Document physicalSchema)
    {
        List<PhysicalSchema> schemaList = new ArrayList<PhysicalSchema>();

        NodeList nodeList = physicalSchema.getElementsByTagName("database");
        Element databaseElement = (Element) nodeList.item(0);
        
        if(databaseElement == null)
        {
            return new ArrayList<PhysicalSchema>();
        }
        else
        {
            String dbName = databaseElement.getAttribute("name");
            
            nodeList = databaseElement.getElementsByTagName("physTable");
            for(int i=0; i<nodeList.getLength(); i++)
            {
                String avgl = ((Element) nodeList.item(i))
                    .getAttribute("avgRowLength");
                String dlen = ((Element) nodeList.item(i))
                    .getAttribute("dataLength");
                String rows = ((Element) nodeList.item(i)).getAttribute("rows");
                String name = ((Element) nodeList.item(i)).getAttribute("name");
                
                long avglLong = -1;
                long dlenLong = -1;
                long rowsLong = -1;
                
                if(!avgl.equals("") && avgl != null)
                {
                    avglLong = Long.parseLong(avgl);
                }
                if(!dlen.equals("") && dlen != null)
                {
                    dlenLong = Long.parseLong(dlen);
                }
                if(!rows.equals("") && rows != null)
                {
                    rowsLong = Long.parseLong(rows);
                }
                
                schemaList.add(new SimplePhysicalSchema(name, dbName, rowsLong,
                    avglLong, dlenLong));
            }
        }
        
        return schemaList;
    }
}
