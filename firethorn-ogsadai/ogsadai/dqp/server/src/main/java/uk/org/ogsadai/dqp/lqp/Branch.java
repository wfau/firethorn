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

package uk.org.ogsadai.dqp.lqp;

/**
 * The Branch enum. Used in the tree walking algorithms to specify tree
 * branches. It is supposed to be more intuitive than using child index. The
 * <code>LEFT</code> branch is equivalent to the child with
 * <code>index = 0</code>, the <code>RIGHT</code> branch is equivalent to
 * <code>index = 1</code> and the <code>SINGLE</code> is equivalent to
 * <code>index = 0</code>.
 * 
 * @author The OGSA-DAI Project Team.
 */
public enum Branch {
    LEFT,
    RIGHT,
    SINGLE
}
