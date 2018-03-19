// Copyright (c) The University of Edinburgh,  2002 - 2008.
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
 * Attribute interface used to represent relations attributes.
 * 
 * @author The OGSA-DAI Project Team
 */
public interface Attribute extends Annotatable
{
    /**
     * Gets attribute name.
     * 
     * @return attribute name
     */
    public String getName();

    /**
     * Gets attribute type.
     * 
     * @return attribute type or <code>-1</code> if unknown
     */
    public int getType();

    /**
     * Sets the attribute type.
     * 
     * @param type
     *            attribute type
     */
    void setType(int type);
    
    /**
     * Gets source (relation name) from which the attribute originated.
     * 
     * @return relation name or <code>null</code> if unknown
     */
    public String getSource();
    
    /**
     * Returns a copy of an attribute. Annotations map get a shallow copy.
     * 
     * @param copyAnnotations
     *            indicates if annotations should be preserved
     * @return a copy of an attribute
     */
    public Attribute getClone(boolean copyAnnotations);

    /**
     * Returns a copy of an attribute with a new name. Annotations map get a
     * shallow copy.
     * 
     * @param newName
     * @param copyAnnotations
     * @return a copy of an attribute with a new name
     */
    public Attribute getCloneNewName(String newName, boolean copyAnnotations);

    /**
     * Returns a copy of an attribute with a new source. Annotations map get a
     * shallow copy.
     * 
     * @param newSource
     * @param copyAnnotations
     * @return a copy of an attribute with a new source
     */
    public Attribute getCloneNewSource(String newSource, boolean copyAnnotations);

    /**
     * Returns a copy of an attribute with isKey set to false.
     * 
     * @param copyAnnotations
     * @return a copy of an attribute with invalidated key flag
     */
    public Attribute getCloneInvalidateKeys(boolean copyAnnotations);
    
    /**
     * Convenience function. Returns the value of the <code>corr.attr</code>
     * annotation or <code>false</code> if annotation has not been set.
     * 
     * Correlated attributes are treated specially during validation.
     * 
     * @return boolean indicating if attribute is correlated
     */
    public boolean isCorrelated();

    /**
     * Convenience function. Returns the value of the <code>temp.attr</code>
     * annotation or <code>false</code> if annotation has not been set.
     * 
     * Temporary attributes are projected out by the final PROJECT operator.
     * 
     * @return boolean indicating if attribute is temporary
     */
    public boolean isTemporary();

    /**
     * Signifies if an attribute is a key.
     * 
     * @return <code>true</code> if an attribute is a key
     */
    public boolean isKey();

    /**
     * Checks for attribute equality with an option of specifying the match mode.
     * See {@link AttributeMatchMode}.
     * 
     * @param attribute
     *            attribute to compare to
     * @param matchMode
     *            indicates what should be taken into account when calculating
     *            equality
     * @return <code>true</code> is attributes are equal
     */
    public boolean equals(Attribute attribute, AttributeMatchMode matchMode);
}
