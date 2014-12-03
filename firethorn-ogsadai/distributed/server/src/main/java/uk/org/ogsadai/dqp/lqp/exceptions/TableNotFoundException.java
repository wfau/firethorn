// Copyright (c) The University of Edinburgh,  2002 - 2008.
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


package uk.org.ogsadai.dqp.lqp.exceptions;

/**
 * Exception raised when user defined table name is not accessible in the
 * current context of the LQPBuilder.
 * 
 * @author The OGSA-DAI Project Team
 */
public class TableNotFoundException extends LQPException
{
    /** Copyright statement */
    private static final String COPYRIGHT_NOTICE = 
        "Copyright (c) The University of Edinburgh,  2002 - 2007"; 

    /** Name of the missing table */
    private String mTableName;
    
    /**
     * Constructor.
     * 
     * @param tableName
     *         missing table name.
     */
    public TableNotFoundException(String tableName)
    {
        super("Table not found " + tableName);
        mTableName = tableName;
    }
    
    /**
     * Gets missing table name.
     * 
     * @return
     *         missing table name.
     */
    public String getTableName()
    {
        return mTableName;
    }
}
