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


package uk.org.ogsadai.dqp.lqp.optimiser.rename;

import uk.org.ogsadai.dqp.lqp.operators.RenameOperator;

/**
 * Interface for RENAME pull up strategies.
 * 
 * @author The OGSA-DAI Project Team.
 */
public interface RenamePullUpStrategy
{
    /**
     * Applies strategy by trying to pull up rename.
     * 
     * @param renameOperator
     *            rename operator to be pulled up
     * @return pulled up rename operator or <code>null</code> if pull is not
     *         allowed
     */
    public RenameOperator pullUp(RenameOperator renameOperator);
}
