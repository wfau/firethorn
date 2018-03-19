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
import uk.org.ogsadai.dqp.lqp.RenameMap;
import uk.org.ogsadai.dqp.lqp.operators.RenameOperator;

/**
 * Builds activity for operator RENAME.
 *
 * @author The OGSA-DAI Project Team.
 */
public class RenameBuilder implements ActivityPipelineBuilder
{
    
    /** Copyright notice. */
    private static final String COPYRIGHT_NOTICE = 
        "Copyright (c) The University of Edinburgh, 2009";

    /**
     * {@inheritDoc}
     */
    public SingleActivityOutput build(
            Operator op,
            List<SingleActivityOutput> outputs,
            PipelineWorkflowBuilder builder)
        throws ActivityConstructionException
    {
        RenameOperator operator = (RenameOperator)op;
        SingleActivityOutput output = outputs.get(0);

        GenericActivity rename = 
            new GenericActivity("uk.org.ogsadai.MetadataRename");
        rename.createInput("data");
        rename.createInput("resultColumnNames");
        rename.createOutput("result", GenericActivity.LIMITED_VALIDATION);
        
        RenameMap renameMap = operator.getRenameMap();
        
        List<Attribute> renamedAttrList = renameMap.getRenamedAttributeList();
        String[] newColumnNames = new String[renamedAttrList.size()];
        for(int i=0; i<newColumnNames.length; i++)
        {
            // fully qualified column name: table.col
            newColumnNames[i] = renamedAttrList.get(i).toString();
        }

        rename.addInputList("resultColumnNames", newColumnNames);
        rename.connectInput("data", output);
        builder.add(rename);
        return rename.getOutput("result");
    }
}
