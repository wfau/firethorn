package uk.org.ogsadai.dqp.common;

import java.util.List;

import uk.org.ogsadai.dqp.lqp.Attribute;

public interface LogicalSchema
{
    
    public String getName();
    
    public List<Attribute> getAttributes();
    
    public boolean containsAttribute(String name);

    /**
     * Returns the attribute with the specified name or <code>null</code> if
     * this schema contains no such attribute.
     * 
     * @param name
     *            attribute name
     * @return attribute in the schema or <code>null</code>
     */
    public Attribute getAttribute(String name);

}
