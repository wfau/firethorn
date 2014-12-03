// Copyright (c) The University of Edinburgh, 2009.
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


package uk.org.ogsadai.tuple.join;

import java.util.ArrayList;
import java.util.List;

import uk.org.ogsadai.tuple.ColumnMetadata;
import uk.org.ogsadai.tuple.SimpleColumnMetadata;
import uk.org.ogsadai.tuple.SimpleTupleMetadata;
import uk.org.ogsadai.tuple.TupleMetadata;

/**
 * Join utilities.
 *
 * @author The OGSA-DAI Project Team.
 */
public class JoinUtilities
{
    
    /** Copyright notice. */
    private static final String COPYRIGHT_NOTICE = 
        "Copyright (c) The University of Edinburgh, 2009";
    
    private JoinUtilities()
    {
        // static methods only
    }
    
    /**
     * Creates the metadata wrapper for the output metadata.
     * Note: This fixes a bug in OGSA-DAI 3.1. For newer versions this method
     * can be removed and replaced by
     * metadata = new SimpleTupleMetadata(left, right);
     * 
     * @param left
     *            left input metadata
     * @param right
     *            right input metadata
     * @return output metadata
     */
    public static TupleMetadata merge(TupleMetadata left, TupleMetadata right)
    {
        List<ColumnMetadata> columns = new ArrayList<ColumnMetadata>();
        for (int i=0; i<left.getColumnCount(); i++)
        {
            ColumnMetadata column = left.getColumnMetadata(i);
            ColumnMetadata newColumn = new SimpleColumnMetadata(
                    column.getName(),
                    column.getTableName(),
                    column.getResourceID(),
                    column.getDRES(),
                    column.getType(),
                    column.getPrecision(),
                    column.isNullable(),
                    column.getColumnDisplaySize());
            columns.add(newColumn);
        }
        for (int i=0; i<right.getColumnCount(); i++)
        {
            ColumnMetadata column = right.getColumnMetadata(i);
            ColumnMetadata newColumn = new SimpleColumnMetadata(
                    column.getName(),
                    column.getTableName(),
                    column.getResourceID(),
                    column.getDRES(),
                    column.getType(),
                    column.getPrecision(),
                    column.isNullable(),
                    column.getColumnDisplaySize());
            columns.add(newColumn);
        }
        TupleMetadata metadata = new SimpleTupleMetadata(columns);
        return metadata;
               
    }


}
