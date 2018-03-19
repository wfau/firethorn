// Copyright (c) The University of Edinburgh, 2011.
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

import uk.org.ogsadai.client.toolkit.activities.generic.GenericActivity;

/**
 * Utility methods useful by DQP builders.
 * 
 * @author The OGSA-DAI Project Team.
 */
public class BuilderUtils
{
    /** Copyright notice. */
    private static final String COPYRIGHT_NOTICE = 
        "Copyright (c) The University of Edinburgh, 2011.";

    /**
     * Creates a controlled repeat activity.
     * 
     * @return a new controlled repeat activity.
     */
    public static GenericActivity createControlledRepeat()
    {
        GenericActivity controlledRepeat = 
            new GenericActivity("uk.org.ogsadai.ControlledRepeat");
        controlledRepeat.createInput("repeatedInput");
        controlledRepeat.createInput("input");
        controlledRepeat.createOutput("repeatedOutput");
        controlledRepeat.createOutput("output");
    
        return controlledRepeat;  
    }
}
