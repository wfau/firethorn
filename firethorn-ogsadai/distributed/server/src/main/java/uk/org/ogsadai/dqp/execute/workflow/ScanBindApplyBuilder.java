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

package uk.org.ogsadai.dqp.execute.workflow;

import java.util.LinkedList;
import java.util.List;

import uk.org.ogsadai.client.toolkit.SingleActivityOutput;
import uk.org.ogsadai.client.toolkit.activities.generic.GenericActivity;
import uk.org.ogsadai.client.toolkit.activities.generic.TraversableSingleActivityInput;
import uk.org.ogsadai.client.toolkit.activities.generic.TraversableSingleActivityOutput;
import uk.org.ogsadai.data.ListBegin;
import uk.org.ogsadai.data.ListEnd;
import uk.org.ogsadai.dqp.execute.ActivityConstructionException;
import uk.org.ogsadai.dqp.lqp.Attribute;
import uk.org.ogsadai.dqp.lqp.BindingPredicate;
import uk.org.ogsadai.dqp.lqp.Heading;
import uk.org.ogsadai.dqp.lqp.Operator;
import uk.org.ogsadai.dqp.lqp.OperatorID;
import uk.org.ogsadai.dqp.lqp.RenameMap;
import uk.org.ogsadai.dqp.lqp.exceptions.AmbiguousAttributeException;
import uk.org.ogsadai.dqp.lqp.exceptions.AmbiguousMappingException;
import uk.org.ogsadai.dqp.lqp.operators.ApplyOperator;
import uk.org.ogsadai.dqp.lqp.operators.RenameOperator;
import uk.org.ogsadai.dqp.lqp.operators.ScanBindApplyOperator;
import uk.org.ogsadai.dqp.lqp.operators.TableScanOperator;
import uk.org.ogsadai.expression.arithmetic.ArithmeticExpression;
import uk.org.ogsadai.expression.arithmetic.visitors.SQLGenArithmeticExpressionVisitor;

/**
 * TODO: document.
 * 
 * @author The OGSA-DAI Project Team.
 */
public class ScanBindApplyBuilder implements ActivityPipelineBuilder
{
    /** Copyright notice. */
    private static final String COPYRIGHT_NOTICE = 
	"Copyright (c) The University of Edinburgh, 2010";

    /**
     * {@inheritDoc}
     */
    public SingleActivityOutput build(Operator op,
	    List<SingleActivityOutput> outputs, PipelineWorkflowBuilder builder)
	    throws ActivityConstructionException
    {
	ScanBindApplyOperator operator = (ScanBindApplyOperator) op;

	int hardIndex = operator.getParameterisedBranchIndex();
	int easyIndex = (hardIndex == 0) ? 1 : 0;

	SingleActivityOutput outputLeft = outputs.get(easyIndex);
	SingleActivityOutput outputRight = outputs.get(hardIndex);

	GenericActivity apply = new GenericActivity("uk.org.ogsadai.Apply");
	apply.createInput("data");
	apply.createInput("attributeNames"); // the bound attributes
	apply.createInput("attributeValues"); // the expressions they will bind
					      // to
	apply.createOutput("data");
	apply.createOutput("bindings");
	builder.add(apply);

	GenericActivity ifEmptyList = new GenericActivity(
		"uk.org.ogsadai.IfEmptyList");
	ifEmptyList.createInput("input");
	ifEmptyList.createOutput("falseOutput");
	ifEmptyList.createOutput("trueOutput");
	ifEmptyList.connectInput("input", apply.getOutput("bindings"));
	builder.add(ifEmptyList);

	// Need to generate the tuple metadata for those cases where we have
	// no binding so cannot call the bound resource
	GenericActivity generateTupleMetadata = createGenerateTupleMetadataActivity(op
		.getChild(hardIndex).getHeading());
	builder.add(generateTupleMetadata);

	// Need to tuple metadata through a controlled repeat in case this
	// path is not 'executed'.
	GenericActivity tupleMetadataControlledRepeat = createControlledRepeatActivity();
	builder.add(tupleMetadataControlledRepeat);
	tupleMetadataControlledRepeat.connectInput("input", ifEmptyList
		.getOutput("trueOutput"));
	tupleMetadataControlledRepeat.connectInput("repeatedInput",
		generateTupleMetadata.getOutput("result"));

	// Need to insert tuple metadata into the data stream produced by Apply
	// activity when there are no bindings.
	GenericActivity insertMetadata = new GenericActivity(
		"uk.org.ogsadai.InsertMetadata");
	insertMetadata.createInput("data");
	insertMetadata.createInput("metadata");
	insertMetadata.createOutput("output");
	builder.add(insertMetadata);
	insertMetadata.connectInput("data", tupleMetadataControlledRepeat
		.getOutput("output"));
	insertMetadata.connectInput("metadata", tupleMetadataControlledRepeat
		.getOutput("repeatedOutput"));

	// End of the if branch that handles when there are no bindings
	GenericActivity endIf = new GenericActivity("uk.org.ogsadai.EndIf");
	endIf.createInput("trueInput");
	endIf.createInput("falseInput");
	endIf.createOutput("output");
	endIf.connectInput("falseInput", outputRight);
	endIf.connectInput("trueInput", insertMetadata.getOutput("output"));
	builder.add(endIf);

	List<Attribute> hardAttrs = new LinkedList<Attribute>();
	List<ArithmeticExpression> easyExprs = new LinkedList<ArithmeticExpression>();

	for (BindingPredicate bindingPred : operator.getBindingPredicates())
	{
	    Attribute hardAttribute = bindingPred.getBoundAttribute();

	    hardAttrs.add(hardAttribute);
	    easyExprs.add(bindingPred.getValueExpression());
	}

	hardAttrs = applyAnyAttributeRenamings(operator, hardAttrs, hardIndex);

	String[] attributeNamesObjects = new String[hardAttrs.size()];
	int i = 0;
	for (Attribute attr : hardAttrs)
	{
	    attributeNamesObjects[i++] = attr.getName();
	}
	apply.addInputList("attributeNames", attributeNamesObjects);

	String[] attributeValuesObjects = new String[easyExprs.size()];
	int index = 0;
	SQLGenArithmeticExpressionVisitor v = new SQLGenArithmeticExpressionVisitor();
	for (ArithmeticExpression expr : easyExprs)
	{
	    expr.accept(v);
	    attributeValuesObjects[index++] = v.getSQLString();
	    v.reset();
	}
	apply.addInputList("attributeValues", attributeValuesObjects);
	apply.connectInput("data", outputLeft);

	// Build the wrapped operator
	// Inputs to this are currently endIf.getOutput("output") and
	// apply.getOutput("data")
	List<SingleActivityOutput> outputsToWrappedOperator = new LinkedList<SingleActivityOutput>();
	outputsToWrappedOperator.add(endIf.getOutput("output"));
	outputsToWrappedOperator.add(apply.getOutput("data"));

	SingleActivityOutput wrappedOperatorOutput = builder.buildOperator(
		operator.getOperator(), outputsToWrappedOperator);
	// Tell the builder that we are processing this operator again now
	builder.setCurrentOperator(operator);

	// Add the to single list activity
	GenericActivity toSingleList = new GenericActivity(
		"uk.org.ogsadai.ToSingleList");
	toSingleList.createInput("input");
	toSingleList.createOutput("output", GenericActivity.LIMITED_VALIDATION);
	builder.add(toSingleList);

	// Store the input to which the apply.getOutput("data") is connected
	TraversableSingleActivityInput inputToWrappedOperatorsSubWorkflow = 
	    (TraversableSingleActivityInput) apply.getOutput("data").getConnectedInputs()[0];

	// Need to put in controlled repeaters for each of the literals
	// created by the wrapped operator
	List<TraversableSingleActivityInput> literalInputs = 
	    builder.getLiteralInputs(operator.getOperator());
	
	TraversableSingleActivityOutput controllerOutput = apply.getOutput("data");
	
	for (TraversableSingleActivityInput input : literalInputs)
	{
	    GenericActivity controlledRepeat = createControlledRepeatActivity();
	    builder.add(controlledRepeat);

	    // Make new literal input
	    controlledRepeat.addInput("repeatedInput", input.getDataValues());
	    controlledRepeat.connectInput("input", controllerOutput);

	    ((GenericActivity) input.getActivity()).connectInput(input,
		    controlledRepeat.getOutput("repeatedOutput"));
	    controllerOutput = controlledRepeat.getOutput("output");
	}

	// Final controller output has to connect to the input to the
	// sub-workflow created by the wrapped operator
	((GenericActivity) inputToWrappedOperatorsSubWorkflow.getActivity())
		.connectInput(inputToWrappedOperatorsSubWorkflow,
			controllerOutput);

	toSingleList.connectInput("input", wrappedOperatorOutput);

	// Now we need to handle the other side of the input

	// Find leaf operator down this branch
	// This ASSUMES that all operators have only once children.
	Operator leafOperator = op.getChild(hardIndex);
	while (leafOperator.getChildCount() != 0)
	{
	    leafOperator = leafOperator.getChild(0);
	}

	builder.addOutput(operator, leafOperator, ifEmptyList
		.getOutput("falseOutput"));

	CrossPipelineWorkflowBuilder crossPipelineWorkflowBuilder = new ApplyCrossPipelineWorkflowBuilder(
		operator, (TableScanOperator) leafOperator, op
			.getChild(hardIndex));
	builder.addCrossPipelineWorkflowBuilder(crossPipelineWorkflowBuilder);

	return toSingleList.getOutput("output");
    }

    private List<Attribute> applyAnyAttributeRenamings(ApplyOperator op,
	    List<Attribute> boundAttributes, int childIndex)
    {
	RenameOperator renameOp = findRenameOperator(op.getChild(childIndex));

	if (renameOp != null)
	{
	    RenameMap renameMap = renameOp.getRenameMap();
	    List<Attribute> result = new LinkedList<Attribute>();

	    for (Attribute attr : boundAttributes)
	    {
		try
		{
		    result.add(renameMap.getOriginalAttribute(attr));
		}
		catch (AmbiguousMappingException e)
		{
		    // Should not occur
		}
		catch (AmbiguousAttributeException e)
		{
		    // Should not occur
		}
	    }
	    return result;
	}
	else
	{
	    return boundAttributes;
	}
    }

    private RenameOperator findRenameOperator(Operator root)
    {
	RenameOperator result = null;

	if (root.getID() == OperatorID.RENAME)
	{
	    return (RenameOperator) root;
	}
	else
	{
	    for (int i = 0; i < root.getChildCount(); ++i)
	    {
		result = findRenameOperator(root.getChild(i));
		if (result != null)
		{
		    return result;
		}
	    }
	}
	return null;
    }

    /**
     * Created a new GenerateTupleMetadata activity that outputs metadata
     * corresponding to the given heading.
     * 
     * @param heading
     *            heading giving required metadata specification
     * 
     * @return the new activity.
     */
    private GenericActivity createGenerateTupleMetadataActivity(Heading heading)
    {
	GenericActivity activity = new GenericActivity(
		"uk.org.ogsadai.GenerateTupleMetadata");
	activity.createInput("names");
	activity.createInput("tableNames");
	activity.createInput("types");
	activity.createOutput("result");

	activity.addInput("names", ListBegin.VALUE);
	activity.addInput("tableNames", ListBegin.VALUE);
	activity.addInput("types", ListBegin.VALUE);
	for (Attribute attr : heading.getAttributes())
	{
	    activity.addInput("names", attr.getName());
	    String tableName = attr.getSource();
	    if (tableName == null)
	    {
		tableName = "";
	    }
	    activity.addInput("tableNames", tableName);
	    activity.addInput("types", attr.getType());
	}
	activity.addInput("names", ListEnd.VALUE);
	activity.addInput("tableNames", ListEnd.VALUE);
	activity.addInput("types", ListEnd.VALUE);

	return activity;
    }

    private GenericActivity createControlledRepeatActivity()
    {
	GenericActivity controlledRepeat = new GenericActivity(
		"uk.org.ogsadai.ControlledRepeat");
	controlledRepeat.createInput("repeatedInput");
	controlledRepeat.createInput("input");
	controlledRepeat.createOutput("repeatedOutput");
	controlledRepeat.createOutput("output");

	return controlledRepeat;
    }
}
