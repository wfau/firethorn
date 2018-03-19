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

import java.util.Collection;
import java.util.List;

import uk.org.ogsadai.dqp.lqp.exceptions.AmbiguousAttributeException;
import uk.org.ogsadai.dqp.lqp.exceptions.AmbiguousMappingException;

/**
 * Rename map interface. The default attribute equality semantics takes into
 * account attribute name and source (where null matches all).
 * 
 * @author The OGSA-DAI Project Team.
 */
public interface RenameMap
{
    /**
     * Adds the attribute mapping from original to renamed attribute. Attributes
     * are cloned by this method before being stored.
     * 
     * @param originalAttribute
     *            original (input) attribute
     * @param renamedAttribute
     *            renamed (output) attribute
     */
    public void add(Attribute originalAttribute, Attribute renamedAttribute);

    /**
     * Get renamed (output) attribute.
     * 
     * @param originalAttribute
     *            original (input) attribute
     * @return renamed attribute or <code>null</code> if no mapping exists
     * @throws AmbiguousMappingException
     *             when an attribute could map to several renamed attributes if
     *             we would to ignore positioning (id, id, id) -> (id, id,
     *             ident)
     * @throws AmbiguousAttributeException
     *             when original attribute is not unique in the list of original
     *             attributes
     */
    public Attribute getRenamedAttribute(Attribute originalAttribute)
        throws AmbiguousMappingException, AmbiguousAttributeException;

    /**
     * Get original (input) attribute.
     * 
     * @param renamedAttribute
     *            renamed (output) attribute
     * @return original (input) attribute or <code>null</code> if no mapping
     *         exists
     * @throws AmbiguousMappingException
     *             when an attribute could map to several original attributes if
     *             we would to ignore positioning
     * @throws AmbiguousAttributeException
     *             when renamed attribute is not unique in the list of renamed
     *             attributes
     */
    public Attribute getOriginalAttribute(Attribute renamedAttribute)
        throws AmbiguousMappingException, AmbiguousAttributeException;

    /**
     * Get an ordered list of original attributes.
     * 
     * @return list of original attributes
     */
    public List<Attribute> getOriginalAttributeList();

    /**
     * Get an ordered list of renamed attributes.
     * 
     * @return list of renamed attributes
     */
    public List<Attribute> getRenamedAttributeList();

    /**
     * Resets source in each attribute that belongs to the renamed attributes
     * list.
     * 
     * @param newSource
     *            new source name
     */
    public void resetRenamedAttributeSources(String newSource);

    /**
     * Gets a map with renamed attributes as keys and original attributes as
     * values.
     * 
     * @param attributes
     *            a collection of renamed attributes
     * @return renamed to original map
     * @throws AmbiguousMappingException
     *             if an attribute maps to an attribute in attributeList
     * @throws AmbiguousAttributeException
     *             if there are several possible mappings
     */
    public RenameMap getRenamedToOriginalMap(
        Collection<Attribute> attributes) throws AmbiguousMappingException,
        AmbiguousAttributeException;
    
    /**
     * Gets a map of original attributes as keys and renamed attributes as
     * values
     * 
     * @param attributeList
     *            a list of original attributes
     * @return original to renamed map
     * @throws AmbiguousMappingException
     *             if an attribute maps to an attribute in attributeList
     * @throws AmbiguousAttributeException
     *             if there are several possible mappings
     */
    public RenameMap getOriginalToRenamedMap(
        List<Attribute> attributeList) throws AmbiguousMappingException,
        AmbiguousAttributeException;
    
    /**
     * Removes the renamed attributes for the renaming list.
     * 
     * @param attr renamed attribute to be removed.
     */
    public void removeRenamedAttribute(Attribute attr);
    
    /**
     * Gets the number of entries in the map.
     * 
     * @return number of entries in the map.
     */
    public int size();
    
    /**
     * Sets attribute match mode. The default attribute match mode is assumed
     * to be <code>NAME_AND_NULL_SOURCE</code>.
     * 
     * @param matchMode
     */
    void setAttributeMatchMode(AttributeMatchMode matchMode);
}
