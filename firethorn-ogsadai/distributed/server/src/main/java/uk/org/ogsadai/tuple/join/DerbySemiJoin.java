// Copyright (c) The University of Edinburgh, 2012.
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

import java.util.Arrays;
import java.util.Collections;

import uk.org.ogsadai.tuple.Tuple;
import uk.org.ogsadai.tuple.TupleMetadata;

public class DerbySemiJoin extends DerbyJoin 
{
    @Override
    public TupleMetadata getJoinMetadata() 
    {
        return mLeft;
    }

    @Override
    public Iterable<Tuple> join(final Tuple tuple) 
    {
        final ResultSetIterator candidates = getCandidateMatches(tuple);
        Iterable<Tuple> result;
        if (candidates.hasNext())
        {
            result = Arrays.asList(tuple);
        }
        else
        {
            result = Collections.<Tuple>emptyList();
        }
        candidates.close();
        return result;
    }
    
}
