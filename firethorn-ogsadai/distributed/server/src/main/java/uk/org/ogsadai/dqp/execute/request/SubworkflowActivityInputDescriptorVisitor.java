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


package uk.org.ogsadai.dqp.execute.request;

import java.util.Iterator;

import uk.org.ogsadai.activity.pipeline.ActivityInput;
import uk.org.ogsadai.activity.pipeline.ActivityInputLiteral;
import uk.org.ogsadai.activity.pipeline.ActivityInputStream;
import uk.org.ogsadai.activity.pipeline.CompositeLiteral;
import uk.org.ogsadai.activity.pipeline.ListLiteral;
import uk.org.ogsadai.activity.pipeline.SimpleLiteral;
import uk.org.ogsadai.activity.pipeline.StreamLiteral;
import uk.org.ogsadai.client.toolkit.activity.ActivityInputDescriptorVisitor;
import uk.org.ogsadai.client.toolkit.activity.LiteralInputDescriptor;
import uk.org.ogsadai.client.toolkit.activity.PipeInputDescriptor;
import uk.org.ogsadai.data.DataValue;
import uk.org.ogsadai.data.DataValueGetObjectVisitor;
import uk.org.ogsadai.data.ListBegin;
import uk.org.ogsadai.data.ListEnd;

/**
 * Visitor that builds an internal activity input from a client toolkit input
 * descriptor.
 * 
 * @author The OGSA-DAI Project Team.
 */
public class SubworkflowActivityInputDescriptorVisitor implements
        ActivityInputDescriptorVisitor
{
    /** Copyright notice. */
    private static final String COPYRIGHT_NOTICE =
        "Copyright (c) The University of Edinburgh, 2008";

    /** The activity input that is being constructed. */
    private ActivityInput mInput;
    
    /**
     * Returns the constructed input.
     * 
     * @return activity input
     */
    public ActivityInput getInput()
    {
        return mInput;
    }
    
    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public void visitLiteralInputDescriptor(LiteralInputDescriptor input)
    {
        Iterator dataValues = input.iterator();
        CompositeLiteral root = new StreamLiteral();
        CompositeLiteral current = root;
        while (dataValues.hasNext())
        {
            DataValue value = (DataValue)dataValues.next();
            if (value == ListBegin.VALUE)
            {
                ListLiteral list = new ListLiteral();
                current.addLiteral(list);
                current = list;
            }
            else if (value == ListEnd.VALUE)
            {
                current = current.getParent();
            }
            else
            {
                DataValueGetObjectVisitor visitor = new DataValueGetObjectVisitor();
                value.accept(visitor);
                current.addLiteral(new SimpleLiteral(visitor.getObject()));
            }
            
        }
        mInput = new ActivityInputLiteral(input.getInputName(), root);
    }

    /**
     * {@inheritDoc}
     */
    public void visitPipeInputDescriptor(PipeInputDescriptor input)
    {
        mInput = new ActivityInputStream(input.getInputName(), input.getPipeName());
    }

}
