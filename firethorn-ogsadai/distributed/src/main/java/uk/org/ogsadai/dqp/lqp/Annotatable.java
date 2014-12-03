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

import java.util.Map;

/**
 * Interface allowing annotations.
 * 
 * @author The OGSA-DAI Project Team.
 */
public interface Annotatable
{
    /**
     * Gets an annotation.
     * 
     * @param key
     *            annotation key
     * @return the annotation value, or <code>null</code> if annotation is not
     *         set.
     */
    public Object getAnnotation(String key);

    /**
     * Adds an annotation.
     * 
     * @param key
     *            annotation key
     * @param value
     *            annotation value
     */
    public void addAnnotation(String key, Object value);

    /**
     * Removes an annotation.
     * 
     * @param key
     *            key of the annotation to be removed
     */
    public void removeAnnotation(String key);
    
    /**
     * Gets all the annotations.
     * 
     * @return annotations mapping.
     * 
     */
    public Map<String, Object> getAnnotations();
}
