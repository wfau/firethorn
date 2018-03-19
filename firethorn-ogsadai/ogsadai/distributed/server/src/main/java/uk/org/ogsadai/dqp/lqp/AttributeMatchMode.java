// Copyright (c) The University of Edinburgh, 2008-2010.
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
 * Enumeration of the attribute match modes.
 * 
 * @author The OGSA-DAI Project Team.
 */
public enum AttributeMatchMode
{
    /** Match name only. */
    NAME_ONLY,
    /** Match name and source, <code>null</code> source matches all. */
    NAME_AND_NULL_SOURCE,
    /** Match name and source. */
    NAME_AND_SOURCE,
    /** Match name, source and type. */
    STRICT,
    /** Match name and source and ignore type. */
    NO_TYPE
}
