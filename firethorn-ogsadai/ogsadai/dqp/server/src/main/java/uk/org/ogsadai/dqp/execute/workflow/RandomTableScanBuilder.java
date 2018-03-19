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
import uk.org.ogsadai.common.msgs.DAILogger;
import uk.org.ogsadai.dqp.execute.ActivityConstructionException;
import uk.org.ogsadai.dqp.lqp.Operator;
import uk.org.ogsadai.dqp.lqp.operators.TableScanOperator;
import uk.org.ogsadai.tuple.ColumnMetadata;
import uk.org.ogsadai.tuple.TupleMetadata;

/**
 * A builder class for TABLE_SCAN operators. It uses RandomTableScan activity
 * instead of SQLQuery to generate tuples. It is used for profiling, when there
 * is a need to eliminate database overheads.
 * 
 * @author The OGSA-DAI Project Team.
 */
public class RandomTableScanBuilder implements ActivityPipelineBuilder
{
    /** Copyright notice. */
    private static final String COPYRIGHT_NOTICE = 
        "Copyright (c) The University of Edinburgh, 2008";

    /** Logger object for logging in this class. */
    private static final DAILogger LOG = 
        DAILogger.getLogger(RandomTableScanBuilder.class);

    /** Default tuple count. TODO: allow parameterization. */
    private static final int COUNT = 10000;
    
    /**
     * {@inheritDoc}
     */
    public SingleActivityOutput build(Operator operator,
        List<SingleActivityOutput> outputs, PipelineWorkflowBuilder builder)
        throws ActivityConstructionException
    {
        TableScanOperator tableScan = (TableScanOperator) operator;

        GenericActivity randomTableScan = new GenericActivity(
            "uk.org.ogsadai.RandomTableScan");
        
        randomTableScan.createInput("metadata");
        randomTableScan.createInput("count");
        randomTableScan.createOutput("data");

        builder.add(randomTableScan);
        
        TupleMetadata md = tableScan.getHeading().getTupleMetadata();
        StringBuilder sb = new StringBuilder();
        for(int i=0; i<md.getColumnCount(); i++)
        {
            ColumnMetadata cmd = md.getColumnMetadata(i);
            sb.append(cmd.getName());
            sb.append(':');
            sb.append(cmd.getTableName());
            sb.append(':');
            sb.append(cmd.getType());
            sb.append(':');
            sb.append(cmd.isNullable());
            if(i < md.getColumnCount()-1)
            {
                sb.append(',');
            }
        }
                
        randomTableScan.addInput("metadata", sb.toString());
        randomTableScan.addInput("count", COUNT);
        
        return randomTableScan.getOutput("data");
    }
}
