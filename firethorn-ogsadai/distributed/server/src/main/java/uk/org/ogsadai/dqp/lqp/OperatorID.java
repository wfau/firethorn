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


package uk.org.ogsadai.dqp.lqp;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Operator Identifier. This class is also a factory for OperatorID classes. The
 * only way to create a new OperatorID class is by calling the
 * <code>getInstance(String id)</code> method. This class ensures that there is
 * only one OperatorID object for unique <code>id</code>.
 * 
 * @author The OGSA-DAI Project Team.
 */
public class OperatorID
{
    /** Copyright notice. */
    private static final String COPYRIGHT_NOTICE =
        "Copyright (c) The University of Edinburgh, 2008";

    /** String ID. */
    private final String mID;

    /** Mapping of string OperatorID representations to OperatorID objects. */
    private static final Map<String, OperatorID> NAMES =
        new ConcurrentHashMap<String, OperatorID>();

    /** Unary operators. */
    public static final OperatorID SELECT = new OperatorID("SELECT");
    public static final OperatorID PROJECT = new OperatorID("PROJECT");
    public static final OperatorID RENAME = new OperatorID("RENAME");
    public static final OperatorID DUPLICATE_ELIMINATION =
        new OperatorID("DUPLICATE_ELIMINATION");
    public static final OperatorID LIMIT = new OperatorID("LIMIT");
    public static final OperatorID SORT = new OperatorID("SORT");
    public static final OperatorID TABLE_SCAN = new OperatorID("TABLE_SCAN");
    public static final OperatorID FILTERED_TABLE_SCAN = new OperatorID("FILTERED_TABLE_SCAN");
    public static final OperatorID GROUP_BY = new OperatorID("GROUP_BY");
    public static final OperatorID ONE_ROW_ONLY =
        new OperatorID("ONE_ROW_ONLY");
    public static final OperatorID SCALAR_GROUP_BY =
        new OperatorID("SCALAR_GROUP_BY");
    /** ID for PULL EXCHANGE CONSUMER. */
    public static final OperatorID PULL_EXCHANGE_CONSUMER =
        new OperatorID("PULL_EXCHANGE_CONSUMER");
    /** ID for PULL EXCHANGE PRODCUER. */
    public static final OperatorID PULL_EXCHANGE_PRODUCER =
        new OperatorID("PULL_EXCHANGE_PRODUCER");
    /** ID for PUSH EXCHANGE CONSUMER. */
    public static final OperatorID PUSH_EXCHANGE_CONSUMER =
        new OperatorID("PUSH_EXCHANGE_CONSUMER");
    /** ID for PUSH EXCHANGE PRODUCER. */
    public static final OperatorID PUSH_EXCHANGE_PRODUCER =
        new OperatorID("PUSH_EXCHANGE_PRODUCER");
    public static final OperatorID EXCHANGE =
        new OperatorID("EXCHANGE");

    /** Binary operators. */
    public static final OperatorID PRODUCT = new OperatorID("PRODUCT");
    public static final OperatorID INNER_THETA_JOIN =
        new OperatorID("INNER_THETA_JOIN");
    public static final OperatorID FULL_OUTER_JOIN =
        new OperatorID("FULL_OUTER_JOIN");
    public static final OperatorID RIGHT_OUTER_JOIN =
        new OperatorID("RIGHT_OUTER_JOIN");
    public static final OperatorID LEFT_OUTER_JOIN =
        new OperatorID("LEFT_OUTER_JOIN");
    public static final OperatorID SEMI_JOIN = new OperatorID("SEMI_JOIN");
    public static final OperatorID ANTI_SEMI_JOIN =
        new OperatorID("ANTI_SEMI_JOIN");
    public static final OperatorID APPLY = new OperatorID("APPLY");
    public static final OperatorID QUERY_APPLY = new OperatorID("QUERY_APPLY");
    public static final OperatorID SCAN_BIND_APPLY = new OperatorID("SCAN_BIND_APPLY");
    public static final OperatorID UNION = new OperatorID("UNION");
    public static final OperatorID INTERSECTION =
        new OperatorID("INTERSECTION");
    public static final OperatorID DIFFERENCE = new OperatorID("DIFFERENCE");

    /** Helper operators - should not appear in the final LQP. */
    public static final OperatorID NIL = new OperatorID("NIL");

    /** Rel functions */
    public static final OperatorID UNARY_REL_FUNCTION =
        new OperatorID("UNARY_REL_FUNCTION");
    public static final OperatorID BINARY_REL_FUNCTION =
        new OperatorID("BINARY_REL_FUNCTION");
    public static final OperatorID SCAN_REL_FUNCTION =
        new OperatorID("SCAN_REL_FUNCTION");

    /**
     * Constructs a new operator ID.
     * 
     * @param id
     *            id
     */
    private OperatorID(String id)
    {
        mID = id;
        NAMES.put(id, this);
    }

    /**
     * Creates a new instance of the OperatorID class identified by a given
     * <code>id</code> or returns previously created instance.
     * 
     * @param id
     *            operator id string
     * @return OperatorID instance
     */
    public static OperatorID getInstance(String id)
    {
        // TODO: Think if we need the NAMES map
        OperatorID result = NAMES.get(id);
        if (result == null)
        {
            OperatorID oid = new OperatorID(id);
            NAMES.put(id, oid);

            return oid;
        }
        else
        {
            return result;
        }
    }

    /**
     * {@inheritDoc}
     */
    public String toString()
    {
        return mID;
    }

    /**
     * Two OperatorIDs are equal when their string representations are equal.
     * 
     * {@inheritDoc}
     */
    public boolean equals(Object obj)
    {
        if(obj == null)
        {
            return false;
        }
        else if(!(obj instanceof OperatorID))
        {
            return false;
        }
        else
        {
            return this.mID.equals(((OperatorID) obj).mID);
        }
    }
}
