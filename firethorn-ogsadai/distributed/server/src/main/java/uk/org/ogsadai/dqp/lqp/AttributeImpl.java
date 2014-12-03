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

import java.util.HashMap;
import java.util.Map;

import uk.org.ogsadai.expression.arithmetic.TableColumn;

/**
 * The default attribute implementation.
 * 
 * @author The OGSA-DAI Project Team.
 */
public class AttributeImpl implements Attribute
{
    /** Copyright notice. */
    private static final String COPYRIGHT_NOTICE = 
        "Copyright (c) The University of Edinburgh, 2008";

    /** Name of attribute. */
    private String mName;
    /** Tuple type. */
    private int mType;
    /** Source table. */
    private String mSource;
    /** Map for annotations. */
    private Map<String, Object> mAnnotationMap = new HashMap<String, Object>();
    /** Is attribute a key. */
    private boolean mIsKey;
    
    /**
     * Constructor.
     * 
     * @param name
     *            name
     * @param type
     *            type
     * @param source
     *            source relation
     * @param isKey
     *            signifies if attribute is a key
     */
    public AttributeImpl(String name, int type, String source, boolean isKey)
    {
        mName = name;
        mType = type;
        mSource = source;
        mIsKey = isKey;
    }

    /**
     * Constructor for non key attributes.
     * 
     * @param name
     *            name
     * @param type
     *            type
     * @param source
     *            source relation
     */
    public AttributeImpl(String name, int type, String source)
    {
        this(name, type, source, false);
    }

    /**
     * Constructor. Sets type and source to default "unknown" values (-1 and
     * null).
     * 
     * @param name
     *            attribute name
     */
    public AttributeImpl(String name)
    {
        this(name, -1, null);
    }

    /**
     * Constructor. Type is set to -1 (unknown).
     * 
     * @param name
     *            name
     * @param source
     *            source relation
     */
    public AttributeImpl(String name, String source)
    {
        this(name, -1, source);
    }

    /**
     * Constructor.
     * 
     * @param fullname
     *            name including the source relation in the format
     *            <code>source.name</code>
     * @param type
     *            type
     */
    public AttributeImpl(String fullname, int type)
    {
        int dot = fullname.indexOf(".");
        if (dot >= 0)
        {
            mSource = fullname.substring(0, dot);
            mName = fullname.substring(dot + 1);
        }
        else
        {
            mName = fullname;
            mSource = null;
        }
        mType = type;
    }

    /**
     * Constructs an attribute from a table column. 
     * 
     * @param tableColumn table column
     */
    public AttributeImpl(TableColumn tableColumn)
    {
        mName = tableColumn.getName();
        mSource = tableColumn.getSource();
    }
    
    /**
     * {@inheritDoc}
     */
    public String getName()
    {
        return mName;
    }

    /**
     * {@inheritDoc}
     */
    public int getType()
    {
        return mType;
    }
    
    /**
     * {@inheritDoc}
     */
    public void setType(int type)
    {
        mType = type;
    }

    /**
     * {@inheritDoc}
     */
    public String getSource()
    {
        return mSource;
    }

    /**
     * {@inheritDoc}
     */
    public boolean equals(Attribute attribute, AttributeMatchMode matchType)
    {
	switch (matchType)
	{
	case STRICT:
	    return mType == attribute.getType()	    	    
	            && ((mSource == null && attribute.getSource() == null) || (mSource != null && mSource.equals(attribute.getSource()))) 
		    && mName != null
		    && mName.equals(attribute.getName());
    case NO_TYPE:
        return ((mSource == null && attribute.getSource() == null) 
                || (mSource != null && mSource.equals(attribute.getSource()))) 
            && mName != null
            && mName.equals(attribute.getName());
	case NAME_AND_NULL_SOURCE:
	    return (mSource == null || attribute.getSource() == null || (mSource != null && mSource
		    .equals(attribute.getSource())))
		    && mName.equals(attribute.getName());
	case NAME_AND_SOURCE:
	    return mSource != null 
	    	    && mSource.equals(attribute.getSource())
		    && mName != null 
		    && mName.equals(attribute.getName());
	case NAME_ONLY:
	    return mName.equals(attribute.getName());
	default:
	    return false;
	}
    }
    
    /**
     * {@inheritDoc}
     */
    public boolean equals(Object obj)
    {        
        if(obj == null || !(obj instanceof AttributeImpl))
        {
            return false;
        }            
        return equals((AttributeImpl) obj, AttributeMatchMode.STRICT);
    }

    /**
     * {@inheritDoc}
     */
    public int hashCode()
    {
	int code = 0;
	
	if (mName != null) code = 3 * mName.hashCode();
	if (mSource != null) code += 7 * mSource.hashCode();
	
        return mType + code;
    }

    /**
     * {@inheritDoc}
     */
    public boolean isTemporary()
    {
        Boolean tempAnnotation = Annotation.getTempAttrAnnotation(this);

        if (tempAnnotation != null)
        {
            return tempAnnotation;
        }
        else
        {
            return false;
        }
    }
    
    /**
     * {@inheritDoc}
     */
    public boolean isKey()
    {
        return mIsKey;
    }

    /**
     * {@inheritDoc}
     */
    public boolean isCorrelated()
    {
        Boolean corrAnnotation = Annotation.getCorrAttrAnnotation(this);
        
        if(corrAnnotation != null)
        {
            return corrAnnotation;
        }
        else
        {
            return false;
        }
    }

    /**
     * {@inheritDoc}
     */
    public String toString()
    {
        if (mSource != null)
        {
            return mSource + "." + mName;
        }
        else
        {
            return mName;
        }
    }
    
    /**
     * {@inheritDoc}
     */
    public void addAnnotation(String key, Object value)
    {
        mAnnotationMap.put(key, value);
    }
    
    /**
     * {@inheritDoc}
     */
    public Object getAnnotation(String key)
    {
        return mAnnotationMap.get(key);
    }

    /**
     * {@inheritDoc}
     */
    public void removeAnnotation(String key)
    {
        mAnnotationMap.remove(key);
    }
    
    /**
     * {@inheritDoc}
     */
    public Map<String, Object> getAnnotations()
    {
        return mAnnotationMap;
    }

    /**
     * {@inheritDoc}
     */
    public Attribute getClone(boolean copyAnnotations)
    {
        AttributeImpl newAttribute = new AttributeImpl(this.getName(), this
            .getType(), this.getSource(), this.isKey());
        
        if (copyAnnotations)
        {
            newAttribute.mAnnotationMap = new HashMap<String, Object>(
                this.mAnnotationMap);
        }
        
        return newAttribute;
    }
    
    /**
     * {@inheritDoc}
     */
    public Attribute getCloneNewName(String newName, boolean copyAnnotations)
    {
        AttributeImpl attr = (AttributeImpl) getClone(copyAnnotations);
        attr.mName = newName;
        
        return attr;
    }
    
    /**
     * {@inheritDoc}
     */
    public Attribute getCloneNewSource(String newSource, boolean copyAnnotations)
    {
        AttributeImpl attr = (AttributeImpl) getClone(copyAnnotations);
        attr.mSource = newSource;
        
        return attr;
    }
    
    /**
     * {@inheritDoc}
     */
    public Attribute getCloneInvalidateKeys(boolean copyAnnotations)
    {
        AttributeImpl attr = (AttributeImpl) getClone(copyAnnotations);
        attr.mIsKey = false;
        
        return attr;
    }
}
