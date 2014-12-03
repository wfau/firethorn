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


package uk.org.ogsadai.dqp.execute.workflow;

import java.util.List;

import uk.org.ogsadai.client.toolkit.SingleActivityOutput;
import uk.org.ogsadai.client.toolkit.activities.generic.GenericActivity;
import uk.org.ogsadai.dqp.execute.ActivityConstructionException;
import uk.org.ogsadai.dqp.lqp.Attribute;
import uk.org.ogsadai.dqp.lqp.Operator;
import uk.org.ogsadai.dqp.lqp.operators.DifferenceOperator;

/**
 * Builds activities for operator DIFFERENCE.
 *
 * @author The OGSA-DAI Project Team.
 */
public class DifferenceBuilder implements ActivityPipelineBuilder
{
    /** Copyright notice. */
    private static final String COPYRIGHT_NOTICE = 
        "Copyright (c) The University of Edinburgh, 2008";
    
    /**
     * {@inheritDoc}
     */
    public SingleActivityOutput build(
            Operator op, 
            List<SingleActivityOutput> outputs,
            PipelineWorkflowBuilder builder) 
        throws ActivityConstructionException
    {
        DifferenceOperator operator = (DifferenceOperator)op;
        SingleActivityOutput outputLeft = outputs.get(0);
        SingleActivityOutput outputRight = outputs.get(1);
        
        List<Attribute> attributes = operator.getHeading().getAttributes();
        String[] columnNames = new String[attributes.size()];
        int i = 0;
        for (Attribute attribute : attributes)
        {
            columnNames[i++] = attribute.toString();
        }
        GenericActivity sortData = 
            new GenericActivity("uk.org.ogsadai.TupleSort");
        sortData.createInput("columnsIds");
        sortData.createInput("data");
        sortData.createOutput("result");
        sortData.addInputList("columnIds", columnNames);
        sortData.connectInput("data", outputLeft);
        builder.add(sortData);
        
        GenericActivity sortDiff = 
            new GenericActivity("uk.org.ogsadai.TupleSort");
        sortDiff.createInput("columnIds");
        sortDiff.createInput("data");
        sortDiff.createOutput("result");
        sortDiff.addInputList("columnsIds", columnNames);
        sortDiff.connectInput("data", outputRight);
        builder.add(sortDiff);
        builder.add(sortDiff);

        GenericActivity difference = 
            new GenericActivity("uk.org.ogsadai.SortedTupleListDifference");
        difference.createInput("data1");
        difference.createInput("data2");
        difference.createOutput("result", GenericActivity.LIMITED_VALIDATION);
        
        difference.connectInput("data1", sortData.getOutput("result"));
        difference.connectInput("data2", sortDiff.getOutput("result"));
        builder.add(difference);
        
        return difference.getOutput("result");
    }
}
