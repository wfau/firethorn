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
import uk.org.ogsadai.dqp.lqp.Attribute;
import uk.org.ogsadai.dqp.lqp.Operator;
import uk.org.ogsadai.dqp.lqp.operators.TableScanOperator;

/**
 * Builds activities for TABLE SCAN operator.
 *
 * @author The OGSA-DAI Project Team.
 */
public class TableScanBuilder implements ActivityPipelineBuilder
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
    {
        TableScanOperator operator = (TableScanOperator)op;
        
        GenericActivity query =
            new GenericActivity("uk.org.ogsadai.SQLQuery");
        query.createInput("expression");
        query.createOutput("data");
        
        query.setResourceID(operator.getDataNode().getResourceID());
        String expression = operator.getPhysicalDatabaseQuery();
        query.addInput("expression", expression);
        builder.add(query);
        
        GenericActivity rename =
            new GenericActivity("uk.org.ogsadai.MetadataRename");
        rename.createInput("data");
        rename.createInput("resultColumnNames");
        rename.createOutput("result", GenericActivity.LIMITED_VALIDATION);

        List<Attribute> attributes = operator.getHeading().getAttributes();
        String[] newNames = new String[attributes.size()];
        for (int i=0; i<newNames.length; i++)
        {
            newNames[i] = attributes.get(i).toString(); 
        }
        rename.addInputList("resultColumnNames", newNames);
        rename.connectInput("data", query.getOutput("data"));
        builder.add(rename);
        return rename.getOutput("result");
    }
}
