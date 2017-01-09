// Copyright (c) The University of Edinburgh, 2011-2012.
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

import java.util.List;
import java.util.Set;

import uk.org.ogsadai.client.toolkit.SingleActivityOutput;
import uk.org.ogsadai.client.toolkit.activities.generic.GenericActivity;
import uk.org.ogsadai.dqp.execute.ActivityConstructionException;
import uk.org.ogsadai.dqp.lqp.Attribute;
import uk.org.ogsadai.dqp.lqp.CommonPredicate;
import uk.org.ogsadai.dqp.lqp.Operator;
import uk.org.ogsadai.dqp.lqp.OperatorID;
import uk.org.ogsadai.dqp.lqp.Predicate;
import uk.org.ogsadai.dqp.lqp.exceptions.LQPException;
import uk.org.ogsadai.dqp.lqp.operators.NilOperator;
import uk.org.ogsadai.dqp.lqp.operators.SelectOperator;
import uk.org.ogsadai.dqp.lqp.operators.TableScanOperator;
import uk.org.ogsadai.dqp.lqp.optimiser.Optimiser;
import uk.org.ogsadai.dqp.lqp.optimiser.join.DQPReplace;
import uk.org.ogsadai.dqp.lqp.udf.repository.SimpleFunctionRepository;
import uk.org.ogsadai.resource.dataresource.dqp.RequestDQPFederation;

import uk.org.ogsadai.common.msgs.DAILogger;

/**
 * Builds activities for TABLE SCAN operator such that the table
 * scan is implemented as multiple queries to the data source each time
 * specifying the exact values that should be obtained. This builder works
 * in conjunction with the BathJoinBuilder for building the JOIN operator.
 * <p>
 * Three activities (ignoring controllers) are built and chained together in the
 * following order:
 * <ul>
 *   <li>uk.org.ogsadai.StringReplace</li>
 *   <li>uk.org.ogsadai.SQLQuery</li>
 *   <li>uk.org.ogsadai.Rename</li>
 * </ul>
 * This activity chain will be given multiple inputs - one for each execution
 * of an SQL query. The StringReplace activity will be used to replace a
 * marker in a template SQL query to produce the actual SQL query.
 * 
 * @author The OGSA-DAI Project Team.
 */
public class BatchTableScanBuilder implements ActivityPipelineBuilder
{
    /** Copyright notice. */
    private static final String COPYRIGHT_NOTICE = 
        "Copyright (c) The University of Edinburgh, 2011-2012";

    /**
     * Debug logger.
     * 
     */
    private static final DAILogger LOG = DAILogger.getLogger(
        BatchTableScanBuilder.class
        );
    
    private Optimiser mOptimiser;

    @Override
    public SingleActivityOutput build(
        Operator op,
        List<SingleActivityOutput> outputs, 
        PipelineWorkflowBuilder builder)
    throws ActivityConstructionException
    {
        TableScanOperator operator = (TableScanOperator)op;
        
        // The data that controls the parameterization and repeated execution
        // of the SQL query comes from the Join operation above this
        // operator in the query plan.
        Operator batchJoin = findBatchJoinOperator(operator);

        // Construct CreateEmptyTupleList activity
        GenericActivity createEmptyTupleList =
            new GenericActivity("uk.org.ogsadai.CreateEmptyTupleList");
        createEmptyTupleList.createInput("resultColumnNames");
        createEmptyTupleList.createInput("resultColumnTypes");
        createEmptyTupleList.createOutput("result");
        
        List<Attribute> attributes = operator.getHeading().getAttributes();
        String[] resultColumnNames = new String[attributes.size()];
        int[] resultColumnTypes = new int[attributes.size()];
        for (int i=0; i<resultColumnNames.length; i++)
        {
            resultColumnNames[i] = attributes.get(i).toString();
            resultColumnTypes[i] = attributes.get(i).getType();
        }
        createEmptyTupleList.addInputList(
            "resultColumnNames", resultColumnNames);
        createEmptyTupleList.addInputList(
            "resultColumnTypes", resultColumnTypes);
        
        // Construct IfEmptyList activity
        GenericActivity ifEmptyList =
            new GenericActivity("uk.org.ogsadai.IfEmptyList");
        ifEmptyList.createInput("data");
        ifEmptyList.createInput("content");
        ifEmptyList.createOutput("outputEmpty");
        ifEmptyList.createOutput("outputNonEmpty");
        
        // Tell the workflow builder about this input, it will be connected to 
        // and output constructed in the builder for batch join
        builder.addInput(batchJoin, operator, ifEmptyList.getInput("data"));
        ifEmptyList.connectInput(
            "content", createEmptyTupleList.getOutput("result"));
        
        // Construct the StringReplace activity
//ZRQ
LOG.debug("ZRQ - Creating StringReplace");
    
        GenericActivity stringReplace =
            new GenericActivity("uk.ac.roe.wfau.firethorn.InListBuilder");
        stringReplace.createInput("template");
        stringReplace.createInput("data");
        stringReplace.createOutput("result");
        
        // Put an IN expression into the filtered table scan
        operator = addAndImplodeSelect(operator);

        // Need to repeat the template input for each instance of the
        // data input so we add a controlled repeater
        GenericActivity controlledRepeater =
            BuilderUtils.createControlledRepeat();
        controlledRepeater.addInput(
            "repeatedInput", 
            operator.getPhysicalDatabaseQuery().replace(
                "DQP_REPLACE", "$REPLACE"));
        controlledRepeater.connectInput(
            "input", ifEmptyList.getOutput("outputNonEmpty"));
        
        stringReplace.connectInput(
            "template", controlledRepeater.getOutput("repeatedOutput"));
        stringReplace.connectInput(
            "data", controlledRepeater.getOutput("output"));
        
        // Construct the SQLQuery activity
        GenericActivity query = new GenericActivity("uk.org.ogsadai.SQLQuery");
        query.createInput("expression");
        query.createOutput("data");
        query.setResourceID(operator.getDataNode().getResourceID());
        query.connectInput("expression", stringReplace.getOutput("result"));
        
        // Controlled Repeat for MetadataRename resultColumnNames
        GenericActivity controlledRepeatResultColumnNames = 
            BuilderUtils.createControlledRepeat();
        
        // Construct the Rename activity
        GenericActivity rename =
            new GenericActivity("uk.org.ogsadai.MetadataRename");
        rename.createInput("data");
        rename.createInput("resultColumnNames");
        rename.createOutput("result", GenericActivity.LIMITED_VALIDATION);

        String[] newNames = new String[attributes.size()];
        for (int i=0; i<newNames.length; i++)
        {
            newNames[i] = attributes.get(i).toString(); 
        }
        
        controlledRepeatResultColumnNames.connectInput(
            "input", query.getOutput("data"));
        controlledRepeatResultColumnNames.addInputList(
            "repeatedInput", newNames);

        rename.connectInput(
            "data", controlledRepeatResultColumnNames.getOutput("output"));
        rename.connectInput(
            "resultColumnNames", 
            controlledRepeatResultColumnNames.getOutput("repeatedOutput"));
        
        // Create the EndIf activity
        GenericActivity endIf = new GenericActivity("uk.org.ogsadai.EndIf");
        endIf.createInput("trueInput");
        endIf.createInput("falseInput");
        endIf.createOutput("output");
        endIf.connectInput("trueInput", rename.getOutput("result"));
        endIf.connectInput("falseInput", ifEmptyList.getOutput("outputEmpty"));
        
        // Add all activities to the builder
        builder.add(createEmptyTupleList);
        builder.add(ifEmptyList);
        builder.add(controlledRepeater);
        builder.add(stringReplace);
        builder.add(query);
        builder.add(rename);
        builder.add(controlledRepeatResultColumnNames);
        builder.add(endIf);
        
        // Final output is the output of the endIf activity
        return endIf.getOutput("output");
    }

    private static Operator findBatchJoinOperator(Operator op) 
    throws ActivityConstructionException
    {
        Operator join = (Operator)op.getAnnotation("join");
        if (join == null)
        {
            throw new ActivityConstructionException(
                    new Exception("No batch join operator found."));
        }
        return join;
    }
    
    private static RequestDQPFederation getRequestFederation(Operator op) 
    throws ActivityConstructionException
    {
        RequestDQPFederation requestFederation = 
            (RequestDQPFederation)op.getAnnotation("requestFederation");
        if (requestFederation == null)
        {
            throw new ActivityConstructionException(
                    new Exception("No annotation 'requestFederation' found."));
        }
        return requestFederation;

    }

    private TableScanOperator addAndImplodeSelect(TableScanOperator operator) 
    throws ActivityConstructionException
    {
        RequestDQPFederation requestFederation = getRequestFederation(operator);
        
        @SuppressWarnings("unchecked")
        Set<Attribute> attrForInClause = 
            (Set<Attribute>) operator.getAnnotation("attributesForInClause");
        StringBuilder sb = new StringBuilder();
        sb.append(attrForInClause.iterator().next());
        sb.append(" IN ( DQP_REPLACE(dqpBatchData_267276) )");
        
        SimpleFunctionRepository funcRepo = new SimpleFunctionRepository();
        funcRepo.register(DQPReplace.class);
        
        // adding a SELECT operator with the in-clause for each batch
        // which has to be imploded
        // we're only interested in the subtree SELECT <- TABLE_SCAN
        Predicate pred = new CommonPredicate(sb.toString(), funcRepo);
        Operator parent = operator.getParent();
        SelectOperator select = new SelectOperator(operator, pred);
        NilOperator nil = new NilOperator();
        nil.setChild(0, select);
        try 
        {
            mOptimiser.optimise(nil, requestFederation, null, null);
        }
        catch (LQPException e) 
        {
            throw new ActivityConstructionException(e);
        }
        TableScanOperator newTableScan;
        // find the table scan operator in the new tree
        if (nil.getChild(0).getID() == OperatorID.TABLE_SCAN)
        {
            newTableScan = (TableScanOperator) nil.getChild(0);
        }
        else
        {
            // we have a rename operator above the table scan
            newTableScan = (TableScanOperator) nil.getChild(0).getChild(0);
        }
            
        // now insert the imploded query (with the rename operator) into LQP
        // builders are applied from the leaves upwards so we're ok - 
        // activities for the new rename operator will be built later
        parent.replaceChild(operator, nil.getChild(0));
        return newTableScan;
    }
    
    /**
     * Sets the optimiser in the configuration for this builder.
     * 
     * @param optimiser
     *            table scan implosion optimiser
     */
    public void setTableScanImplosionOptimiser(Optimiser optimiser)
    {
        mOptimiser = optimiser;
    }

}
