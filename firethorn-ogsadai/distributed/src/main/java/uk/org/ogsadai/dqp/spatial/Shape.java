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

package uk.org.ogsadai.dqp.spatial;

/**
 * Basic shape interface. 
 * 
 * @author The OGSA-DAI Project Team.
 */
public interface Shape
{
    /**
     * Does the shape contain the given point.
     * 
     * @param point
     *            point
     * 
     * @return <tt>true</tt> if the shape contains the point, <tt>false</tt>
     *         otherwise.
     */
    boolean contains(Point point);
}
