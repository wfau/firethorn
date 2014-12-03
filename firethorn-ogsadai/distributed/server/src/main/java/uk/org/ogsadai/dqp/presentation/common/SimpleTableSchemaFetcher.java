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

import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import uk.org.ogsadai.client.toolkit.DataRequestExecutionResource;
import uk.org.ogsadai.client.toolkit.PipelineWorkflow;
import uk.org.ogsadai.client.toolkit.RequestExecutionType;
import uk.org.ogsadai.client.toolkit.RequestResource;
import uk.org.ogsadai.client.toolkit.activities.delivery.DeliverToRequestStatus;
import uk.org.ogsadai.client.toolkit.activities.generic.GenericActivity;
import uk.org.ogsadai.client.toolkit.activities.sql.ExtractPhysicalSchemaToXML;
import uk.org.ogsadai.client.toolkit.exception.RequestExecutionException;
import uk.org.ogsadai.common.msgs.DAILogger;
import uk.org.ogsadai.converters.databaseschema.TableMetaData;
import uk.org.ogsadai.dqp.common.DataNode;
import uk.org.ogsadai.dqp.common.PhysicalSchema;
import uk.org.ogsadai.dqp.common.RequestDetails;
import uk.org.ogsadai.util.xml.XML;

/**
 * Table schema fetcher for Axis/GT evaluation nodes.
 * 
 * @author The OGSA-DAI Project Team.
 */
public class SimpleTableSchemaFetcher implements TableSchemaFetcher
{
    /** Copyright notice. */
    private static final String COPYRIGHT_NOTICE = 
        "Copyright (c) The University of Edinburgh, 2008";
    
    /** Logger. */
    private static final DAILogger LOG = 
        DAILogger.getLogger(SimpleTableSchemaFetcher.class);

    /**
     * {@inheritDoc}
     */
    public List<TableMetaData> fetchSchema(DataNode node,
        RequestDetails requestDetails) throws ExtractLogicalSchemaException
    {
        GenericActivity extractTableSchema = new GenericActivity(
            "uk.org.ogsadai.ExtractTableSchema");
        extractTableSchema.createInput("name");
        extractTableSchema.createOutput("data");
        extractTableSchema.setResourceID(node.getResourceID());
        extractTableSchema.addInput("name", "%");

        GenericActivity schemaData = new GenericActivity(
            "uk.org.ogsadai.TableMetadataToXMLCharArraysList");
        schemaData.createInput("data");
        schemaData.createOutput("result");
        schemaData.connectInput("data", extractTableSchema.getOutput("data"));

        GenericActivity deliverToRequestStatus = new GenericActivity(
            "uk.org.ogsadai.DeliverToRequestStatus");
        deliverToRequestStatus.createInput("input");
        deliverToRequestStatus.connectInput("input", schemaData
            .getOutput("result"));

        PipelineWorkflow pipeline = new PipelineWorkflow();
        pipeline.add(extractTableSchema);
        pipeline.add(schemaData);
        pipeline.add(deliverToRequestStatus);
        try
        {
            DataRequestExecutionResource drer = node.getEvaluationNode().getDRER(
                requestDetails);
            drer.execute(pipeline, RequestExecutionType.SYNCHRONOUS);

            TableMetadataResultExtractor resultExtractor = new TableMetadataResultExtractor(
                deliverToRequestStatus.getDataValueIterator("result"));

            List<TableMetaData> metadataList = resultExtractor.getTableMetaData();

            return metadataList;
        }
        catch (RequestExecutionException e)
        {
            throw new ExtractLogicalSchemaException(e);
        }
        catch (Throwable e)
        {
            throw new ExtractLogicalSchemaException(e);
        }
    }

    /**
     * {@inheritDoc}
     */
    public List<PhysicalSchema> fetchPhysicalSchema(DataNode node,
        RequestDetails requestDetails) throws ExtractPhysicalSchemaException
    {
        // Assemble the activity workflow
        ExtractPhysicalSchemaToXML extractTableSchema = new ExtractPhysicalSchemaToXML();
        extractTableSchema.setResourceID(node.getResourceID());
        // get data for all tables
        extractTableSchema.addName("%");

        DeliverToRequestStatus deliverToRequestStatus = new DeliverToRequestStatus();
        deliverToRequestStatus.connectInput(extractTableSchema
            .getResultOutput());

        PipelineWorkflow pipeline = new PipelineWorkflow();
        pipeline.add(extractTableSchema);
        pipeline.add(deliverToRequestStatus);

        try
        {
            DataRequestExecutionResource drer = node.getEvaluationNode().getDRER(
                requestDetails);

            RequestResource requestResource = drer.execute(pipeline,
                RequestExecutionType.SYNCHRONOUS);
        }
        catch (Exception e)
        // ServerCommsException, ServerException, ClientToolkitException,
        // ResourceUnknownException, ClientException, RequestException
        {
            throw new ExtractPhysicalSchemaException(e);
        }

        // Get results as document
        Document doc = XML.toDocument(new InputSource(extractTableSchema
            .nextResult()));

        return PhysicalSchemaResultExtractor.extractPhysicalSchema(doc);
    }

}
