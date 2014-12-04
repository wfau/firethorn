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

import java.util.ArrayList;
import java.util.List;

import uk.org.ogsadai.client.toolkit.SingleActivityOutput;
import uk.org.ogsadai.client.toolkit.activities.generic.GenericActivity;
import uk.org.ogsadai.dqp.execute.ActivityConstructionException;
import uk.org.ogsadai.dqp.lqp.Annotation;
import uk.org.ogsadai.dqp.lqp.Attribute;
import uk.org.ogsadai.dqp.lqp.Operator;
import uk.org.ogsadai.dqp.lqp.operators.GroupByOperator;
import uk.org.ogsadai.dqp.lqp.udf.Function;

/**
 * Builds an activity for the operator GROUP BY.
 *
 * @author The OGSA-DAI Project Team.
 */
public class GroupByBuilder implements ActivityPipelineBuilder
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
        GroupByOperator operator = (GroupByOperator)op;
        SingleActivityOutput output = outputs.get(0);

        GenericActivity groupBy = new GenericActivity("uk.org.ogsadai.GroupBy");
        groupBy.createInput("data");
        groupBy.createInput("columnIds");
        groupBy.createInput("aggregates");
        groupBy.createInput("resultColumnNames");
        groupBy.createOutput("result", GenericActivity.LIMITED_VALIDATION);

        groupBy.connectInput("data",output);
        
        List<String> columnNames = new ArrayList<String>();
        
        List<Attribute> groupByAttributes = operator.getGroupingAttributes();
        String[] attributes = new String[groupByAttributes.size()];
        int i=0;
        for (Attribute column : groupByAttributes)
        {
            columnNames.add(column.toString());
            attributes[i++] = column.toString();
        }
        groupBy.addInputList("columnIds", attributes);
        
        List<Function> aggregates = operator.getAggregates();
        String[] aggregateDefs = new String[aggregates.size()];
        i=0;
        for (Function function : aggregates)
        {
            columnNames.add(Annotation.getResultNameAnnotation(function));
            aggregateDefs[i++] = function.toSQL();
        }
        groupBy.addInputList("aggregates", aggregateDefs);
        
        groupBy.addInputList(
            "resultColumnNames",
            columnNames.toArray(new String[columnNames.size()]));
        
        builder.add(groupBy);
        return groupBy.getOutput("result");
    }
}
