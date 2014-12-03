package uk.org.ogsadai.dqp.common.simple;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import uk.org.ogsadai.dqp.common.LogicalSchema;
import uk.org.ogsadai.dqp.lqp.Attribute;

public class SimpleLogicalSchema implements LogicalSchema 
{
    
    private final List<Attribute> mAttributes;
    private final Map<String, Attribute> mAttributeNames;
    private final String mName;

    public SimpleLogicalSchema(String name, List<Attribute> attributes)
    {
        mName = name;
        mAttributes = attributes;
        mAttributeNames = new HashMap<String, Attribute>();
        for (Attribute attribute : attributes)
        {
            mAttributeNames.put(attribute.getName(), attribute);
        }
    }

    public SimpleLogicalSchema(String name, Attribute... attributes)
    {
        mName = name;
        mAttributes = Arrays.asList(attributes);
        mAttributeNames = new HashMap<String, Attribute>();
        for (Attribute attribute : attributes)
        {
            mAttributeNames.put(attribute.getName(), attribute);
        }
    }

    public SimpleLogicalSchema(String name, LogicalSchema schema) 
    {
        this(name, schema.getAttributes());
    }

    @Override
    public String getName() 
    {
        return mName;
    }

    @Override
    public List<Attribute> getAttributes() 
    {
        return mAttributes;
    }

    @Override
    public boolean containsAttribute(String name) 
    {
        return mAttributeNames.containsKey(name);
    }
    
    @Override
    public Attribute getAttribute(String name)
    {
        return mAttributeNames.get(name);
    }

}
