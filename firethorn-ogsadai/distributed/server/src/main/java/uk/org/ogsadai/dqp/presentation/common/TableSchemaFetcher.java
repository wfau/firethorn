// Copyright (c) The University of Edinburgh, 2008-2009.
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

package uk.org.ogsadai.dqp.presentation.common;

import java.util.List;

import uk.org.ogsadai.converters.databaseschema.TableMetaData;
import uk.org.ogsadai.dqp.common.DataNode;
import uk.org.ogsadai.dqp.common.PhysicalSchema;
import uk.org.ogsadai.dqp.common.RequestDetails;

/**
 * Fetches table schemas from a data node.
 *
 * @author The OGSA-DAI Project Team.
 */
public interface TableSchemaFetcher
{
    /**
     * Returns a list of all available table schemas for the given resource.
     * 
     * @param node
     *            a data node from the table schemas are fetched
     * @param requestDetails
     *            details of parent request
     * @return schemas of the tables in the resource
     * @throws ExtractLogicalSchemaException
     *             if an error occurred
     */
    public List<TableMetaData> fetchSchema(
        DataNode node, RequestDetails requestDetails) 
        throws ExtractLogicalSchemaException;

    /**
     * Returns a list of physical schemas for each table exposed by a data node.
     * 
     * @param node
     *            a data node from which the data is fetched
     * @param requestDetails
     *            details of parent request
     * @return a list of physical schemas
     * @throws ExtractPhysicalSchemaException
     *             when things go wrong
     */
    public List<PhysicalSchema> fetchPhysicalSchema(
        DataNode node,
        RequestDetails requestDetails) throws ExtractPhysicalSchemaException;
}
