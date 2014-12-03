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

package uk.org.ogsadai.dqp.execute.workflow;

import uk.org.ogsadai.client.toolkit.SingleActivityOutput;
import uk.org.ogsadai.client.toolkit.activities.generic.GenericActivity;
import uk.org.ogsadai.client.toolkit.activities.generic.TraversableSingleActivityInput;
import uk.org.ogsadai.client.toolkit.activities.generic.TraversableSingleActivityOutput;
import uk.org.ogsadai.dqp.common.EvaluationNode;
import uk.org.ogsadai.dqp.execute.partition.Partition;
import uk.org.ogsadai.resource.dataresource.dqp.DQPResourceAccessor;
import uk.org.ogsadai.util.UniqueName;

/**
 * Connects activities in partitions.
 *
 * @author The OGSA-DAI Project Team.
 */
public class PipelineConnector
{
    /** Copyright notice. */
    private static final String COPYRIGHT_NOTICE =
        "Copyright (c) The University of Edinburgh, 2009";
    
    /** The input to which the exchange consumer must write the data. */
    private TraversableSingleActivityInput mInput;
    /** Workflow builder used to build the exchange consumer. */
    private PipelineWorkflowBuilder mInputBuilder;
    /** The output from which the exchaneg producer must read the data. */
    private TraversableSingleActivityOutput mOutput;
    /** Workflow build used to build the exchange produer. */
    private PipelineWorkflowBuilder mOutputBuilder;
    
    /** DQP Resource accessor, used to access configuration parameters. */
    private final DQPResourceAccessor mResourceAccessor;
    
    /***
     * Constructor.
     * 
     * @param resourceAccessor
     *           DQP resource accessor, used to supply configuration parameters.
     */
    public PipelineConnector(DQPResourceAccessor resourceAccessor)
    {
        mResourceAccessor = resourceAccessor;
    }
    
    /**
     * Sets the connector's input.
     * 
     * @param input
     *            activity input
     * @param builder
     *            builds the pipeline to which the activity belongs
     */
    public void setInput(
        TraversableSingleActivityInput input, 
        PipelineWorkflowBuilder builder)
    {
        mInput = input;
        mInputBuilder = builder;
        if (mOutput != null)
        {
            connect();
        }
    }

    /**
     * Sets the connector's output.
     * 
     * @param output
     *            activity output
     * @param builder
     *            builds the pipeline to which the activity belongs
     */
    public void setOutput(
        TraversableSingleActivityOutput output, 
        PipelineWorkflowBuilder builder)
    {
        mOutput = output;
        mOutputBuilder = builder;
        if (mInput !=null) 
        {
            connect();
        }
    }
    
    /**
     * Connects the input and output using either a push or pull model 
     * depending on configuration of the DQP resource accessor.
     */
    private void connect()
    {
        if (mResourceAccessor.getUsePushModel())
        {
            connectUsingPush();
        }
        else
        {
            connectUsingPull();
        }
    }
    
    /**
     * Connects the input and output using a pull model.
     */
    private void connectUsingPull()
    {
        if (mInputBuilder != mOutputBuilder)
        {
            String dataSource = createPullExchange(mOutputBuilder);
            insertPullExchangeProducer(mOutput, mOutputBuilder, dataSource);
            TraversableSingleActivityOutput output = 
                insertPullExchangeConsumer(
                        mInputBuilder, 
                        mOutputBuilder.getPartition(), 
                        dataSource);
            mInput.connect(output);
        }
        else
        {
            mInput.connect(mOutput);
        }

    }
    
    /**
     * Connects the input and output using a push model.
     */
    private void connectUsingPush()
    {
        if (mInputBuilder != mOutputBuilder)
        {
            String dataSink = createPushExchange(mInputBuilder);
            insertPushExchangeProducer(
                mOutput, 
                mOutputBuilder, 
                mInputBuilder.getPartition().getEvaluationNode(),
                dataSink);
            TraversableSingleActivityOutput output = 
                insertPushExchangeConsumer(mInputBuilder, dataSink);
            mInput.connect(output);
        }
        else
        {
            mInput.connect(mOutput);
        }
    }
    
    /**
     * Creates a data source name and adds it to the setup.
     * 
     * @param builder
     *            adds data source
     * @return name of new data source
     */
    private String createPullExchange(PipelineWorkflowBuilder builder)
    {
        String dataSourceID = "Exchange-" + UniqueName.newName();
        builder.addDataSourceToSetup(dataSourceID);
        return dataSourceID;
    }
    
    /**
     * Creates a data sink name and adds it to the setup.
     * 
     * @param builder
     *            adds data sink
     * @return name of new data sink
     */
    private String createPushExchange(PipelineWorkflowBuilder builder)
    {
        String dataSinkID = "Exchange-" + UniqueName.newName();
        builder.addDataSinkToSetup(dataSinkID);
        return dataSinkID;
    }

    /**
     * Inserts a pull exchange producer into a workflow.
     * 
     * @param output  
     *     the output that provides the input to the exchange producer 
     *     activities. 
     *        
     * @param builder
     *     builder used to build the workflow
     *     
     * @param dataSource
     *     resource ID of the data source providing the connection.
     */
    private void insertPullExchangeProducer(
            SingleActivityOutput output, 
            PipelineWorkflowBuilder builder,
            String dataSource)
    {
        // write data from child tree to data source
        TupleSerialiser tupleSerialiser = mResourceAccessor.getTupleSerialiser();
        SingleActivityOutput serialiserOutput = 
            tupleSerialiser.addSerialiser(output, builder);

        GenericActivity write =
            new GenericActivity("uk.org.ogsadai.WriteToDataSource");
        write.createInput("input");
        write.connectInput("input", serialiserOutput);
        write.setResourceID(dataSource);
        builder.add(write);
    }
    
    /**
     * Inserts activities that obtain data from a data source.
     * 
     * @param builder
     *            builder to add activities to
     * @param source
     *            source partition from which to obtain data
     * @param dataSource
     *            data source name
     * @return output tuples
     */
    private TraversableSingleActivityOutput insertPullExchangeConsumer(
            PipelineWorkflowBuilder builder, 
            Partition source,
            String dataSource)
    {
        // obtain data from remote data source on destination partition
        DQPObtainFromDataSource obtain = 
            new DQPObtainFromDataSource("uk.org.ogsadai.ObtainFromDataSource");
        obtain.createInput("mode");
        obtain.createInput("numberOfBlocks");
        obtain.createInput("resourceID");
        obtain.createOutput("data");
        
        obtain.addInput("mode", "BLOCK");
        obtain.addInput("numberOfBlocks", mResourceAccessor.getNumBlocks());
        obtain.addInput("resourceID", dataSource);
        obtain.setEvaluationNode(source.getEvaluationNode());
        builder.add(obtain);
        
        TupleSerialiser tupleSerialiser = mResourceAccessor.getTupleSerialiser();
        return tupleSerialiser.addDeserialiser(obtain.getOutput("data"), builder);

    }

    
    /**
     * Inserts a push exchange producer into a workflow.
     * 
     * @param output  
     *     the output that provides the input to the exchange producer 
     *     activities. 
     *        
     * @param builder
     *     builder used to build the workflow
     *     
     * @param dataSinkEvaluationNode
     *     evaluation node on which the data sink being used for the connection
     *     resides.
     *     
     * @param dataSink
     *     resource ID of the data sink providing the connection.
     */
    private void insertPushExchangeProducer(
        SingleActivityOutput output,
        PipelineWorkflowBuilder builder,
        EvaluationNode dataSinkEvaluationNode,
        String dataSink)
    {
        
        // write data from child tree to data source
        TupleSerialiser tupleSerialiser = mResourceAccessor.getTupleSerialiser();
        TraversableSingleActivityOutput serialiserOutput = 
            tupleSerialiser.addSerialiser(output, builder);
        
        DQPDeliverToDataSink deliver = 
            new DQPDeliverToDataSink("uk.org.ogsadai.DeliverToDataSink");
        deliver.createInput("mode");
        deliver.createInput("numberOfBlocks");
        deliver.createInput("resourceID");
        deliver.createInput("data");
        deliver.addInput("mode", "BLOCK");
        deliver.addInput("numberOfBlocks", mResourceAccessor.getNumBlocks());
        deliver.addInput("resourceID", dataSink);
        deliver.connectInput("data", serialiserOutput);
        deliver.setEvaluationNode(dataSinkEvaluationNode);
        builder.add(deliver);
    }

    /**
     * Inserts activities that read from a local data source and deserialize
     * to tuples.
     * 
     * @param builder
     *            builder to add activities to
     * @param dataSink
     *            data sink name
     * @return output tuples
     */
    private TraversableSingleActivityOutput insertPushExchangeConsumer(
            PipelineWorkflowBuilder builder, 
            String dataSink)
    {
        GenericActivity read =
            new GenericActivity("uk.org.ogsadai.ReadFromDataSink");
        read.createOutput("output");
        read.setResourceID(dataSink);
        builder.add(read);
        
        TupleSerialiser tupleSerialiser = mResourceAccessor.getTupleSerialiser();
        return tupleSerialiser.addDeserialiser(read.getOutput("output"), builder);
    }

}
