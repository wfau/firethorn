// Copyright (c) The University of Edinburgh, 2010.
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

package uk.org.ogsadai.activity.astro;

import uk.org.ogsadai.activity.astro.votable.StarTableTypeToTupleTypeMapper;
import uk.org.ogsadai.tuple.TupleTypes;
import junit.framework.TestCase;

/**
 * Test for StarTableTypeToTupleTypeMapper.
 * 
 * @author The OGSA-DAI Project Team.
 *
 */
public class StarTableTypeToTupleTypeMapperTest extends TestCase
{
    /** Copyright statement. */
    private static final String COPYRIGHT_NOTICE = 
        "Copyright (c) The University of Edinburgh, 2010";
    
    /**
     * Test getType() method.
     */
    public void testGetType()
    {
        // Array types
        Character ch = new Character('a');
        Class cs = ch.getClass();
        assertEquals(TupleTypes._OBJECT,
                     StarTableTypeToTupleTypeMapper.getType(cs, true));
        
        // Non-array types
        Object[] input = {
            new Boolean(true),
            new Byte("0"),
            new Short("0"),
            new Integer(0),
            new Long(1),
            new Character('a'),
            new Float(0.0),
            new Double(0.0),
            new String(""),
            new Object(),
        };
        
        int[] expected = {
            TupleTypes._BOOLEAN,
            TupleTypes._INT,
            TupleTypes._SHORT,
            TupleTypes._INT,
            TupleTypes._LONG,
            TupleTypes._CHAR,
            TupleTypes._FLOAT,
            TupleTypes._DOUBLE,
            TupleTypes._STRING,
            TupleTypes._OBJECT
        };
        
        assertTrue("Number of inputs and outputs do not match",
                    input.length == expected.length);
        
        for (int i=0; i<input.length; i++)
        {
            Class c = input[i].getClass();
            assertEquals(expected[i],
                         StarTableTypeToTupleTypeMapper.getType(c, false));
        }

    }
}
