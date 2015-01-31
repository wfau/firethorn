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


package uk.org.ogsadai.activity.block;

/**
 * Produces a sequence of numbers between 0 and <code>size</code>.
 *
 * @author The OGSA-DAI Project Team.
 */
public interface Split
{
    /**
     * Sets the range of numbers to be generated to between <code>0</code> and
     * <code>n-1</code>.
     * 
     * @param n
     *            maximum number to be generated (exclusive)
     */
    public void setSize(int n);
    
    /**
     * Returns the next number.
     * 
     * @return next number
     */
    public int next();
}
