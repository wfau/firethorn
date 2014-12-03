// Copyright (c) The University of Edinburgh, 2008.
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

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import uk.org.ogsadai.tuple.Tuple;

/**
 * A simple join which produces the product of the two inputs and selects the
 * tuples which satisfy the condition.
 *
 * @author The OGSA-DAI Project Team.
 */
public class ProductJoin extends Join
{
    /** Copyright notice. */
    private static final String COPYRIGHT_NOTICE =
        "Copyright (c) The University of Edinburgh, 2008";
    
    /** List of stored tuples. */
    private List<Tuple> mStoredTuples = new LinkedList<Tuple>();

    /**
     * {@inheritDoc}
     */
    @Override
    protected Iterator<Tuple> getCandidateMatches(Tuple tuple)
    {
        return mStoredTuples.iterator();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void storeTuple(Tuple tuple)
    {
        mStoredTuples.add(tuple);
    }

}
