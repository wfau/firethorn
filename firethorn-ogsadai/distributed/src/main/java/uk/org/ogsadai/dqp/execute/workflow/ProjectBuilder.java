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
import uk.org.ogsadai.dqp.lqp.operators.ProjectOperator;

/**
 * Builds activities for operator PROJECT.
 *
 * @author The OGSA-DAI Project Team.
 */
public class ProjectBuilder implements ActivityPipelineBuilder
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
        ProjectOperator operator = (ProjectOperator)op;
        SingleActivityOutput output = outputs.get(0);
        
        GenericActivity project  = 
            new GenericActivity("uk.org.ogsadai.TupleArithmeticProject");
        project.createInput("data");
        project.createInput("expressions");
        project.createInput("resultColumnNames");
        project.createOutput("result", GenericActivity.LIMITED_VALIDATION);
        
        // add projections
        List<String> attributeDefs = operator.getAttributeDefs();
        String[] projections = 
            attributeDefs.toArray(new String[attributeDefs.size()]);
        project.addInputList("expressions", projections);

        // add output column names
        List<Attribute> attributes = operator.getHeading().getAttributes();
        String[] columnNames = new String[attributes.size()];
        int i=0;
        for (Attribute attribute : attributes)
        {
            columnNames[i++] = attribute.toString();
        }
        project.addInputList("resultColumnNames", columnNames);
        
        project.connectInput("data", output);
        builder.add(project);
        return project.getOutput("result");
    }
}
