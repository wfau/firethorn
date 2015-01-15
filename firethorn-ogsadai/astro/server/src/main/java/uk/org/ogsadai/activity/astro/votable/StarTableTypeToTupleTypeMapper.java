// Copyright (c) The University of Edinburgh,  2010.
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

import uk.org.ogsadai.tuple.TupleTypes;

/**
 * Utility that maps VOTable types to OGSA-DAI types. 
 * 
 * @author The OGSA-DAI Project Team.
 *
 */
public class StarTableTypeToTupleTypeMapper
{
    /** Copyright notice. */
    private static final String COPYRIGHT_NOTICE = 
        "Copyright (c) The University of Edinburgh 2010.";
    
    /**
     * <p>
     * Maps types returned by 
     * <code>uk.ac.starlink.table.StarTable.getColumnInfo.getContentClass()</code>
     * to OGSA-DAI types.
     * </p>
     * <p>
     * See also Table 1 in,
     * 
     * VOTable Format Definition
     * Version 1.2
     * IVOA Recommendation 2009-11-30
     * http://www.ivoa.net/Documents/VOTable/20091130/REC-VOTable-1.2.pdf
     * </p>
     * <p>
     * <code>Byte</code> is returned as <code>TupleTypes._INT</code>.
     * </p>
     * <p>
     * The following types are returned as <code>TupleTypes._OBJECT</code> :
     * <code>bit</code>, arrays.
     * </p>
     *  
     * @param type
     * @return
     */
    public static int getType(Class type, boolean isArray)
    {
        // Don't know what to do about array types for now
        if (isArray)
        {
            return TupleTypes._OBJECT;
        }
        
        // Non-array types
        if (type == Boolean.class)
        {
            return TupleTypes._BOOLEAN;
        }else if (type == Byte.class)
        {
            return TupleTypes._INT;
        }else if (type == Short.class)
        {
            return TupleTypes._SHORT;
        }else if (type == Integer.class)
        {
            return TupleTypes._INT;
        }else if (type == Long.class)
        {
            return TupleTypes._LONG;
        }else if (type == Character.class)
        {
            return TupleTypes._CHAR;
        }else if (type == Float.class)
        {
            return TupleTypes._FLOAT;
        }else if (type == Double.class)
        {
            return TupleTypes._DOUBLE;
        }else if (type == String.class)
        {
            return TupleTypes._STRING;
        }else{
            return TupleTypes._OBJECT;
        }
    }
}
