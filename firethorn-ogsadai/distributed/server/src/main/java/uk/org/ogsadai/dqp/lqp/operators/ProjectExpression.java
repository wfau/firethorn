// Copyright (c) The University of Edinburgh, 2012.
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

package uk.org.ogsadai.dqp.lqp.operators;

/**
 * Interface to mark expressions that can be used within a project statement.
 * There are two types of expressions that can be in a project statement. These
 * are:
 * <ul>
 *   <li>Arithmetic Expressions, and</li>
 *   <li>Attributes</li>
 * </ul>
 * Various places in the code handle these differently but to preserve the
 * order it is useful to place them in a single ordered list. This empty 
 * interface is used to support this.
 * <p>
 * A future refactoring may attempt to put more common methods into this
 * interface and hence move more logic into the classes that implement this.
 * Currently these classes and just containers for the data with no methods
 * that actually do any processing.
 * 
 * @author The OGSA-DAI Project Team.
 *
 */
public interface ProjectExpression
{
}
