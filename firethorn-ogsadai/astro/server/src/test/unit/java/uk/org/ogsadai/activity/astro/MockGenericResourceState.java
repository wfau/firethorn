// Copyright (c) The University of Edinburgh, 2010-2013.
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

package uk.org.ogsadai.activity.astro;

import java.util.Enumeration;
import java.util.Properties;
import java.util.Map.Entry;

import uk.org.ogsadai.config.Key;
import uk.org.ogsadai.config.SimpleKeyValueProperties;
import uk.org.ogsadai.resource.ResourceID;
import uk.org.ogsadai.resource.dataresource.DataResourceState;
import uk.org.ogsadai.resource.dataresource.SimpleDataResourceState;
import uk.org.ogsadai.resource.generic.GenericResourceState;
import uk.org.ogsadai.test.TestProperties;
import uk.org.ogsadai.test.TestPropertyNotFoundException;
import uk.org.ogsadai.util.UniqueName;

/**
 * <code>GenericResourceState</code> initialised from an
 * <code>Properties</code> object. 
 * 
 * @author The OGSA-DAI Project Team.
 */
public class MockGenericResourceState 
    extends SimpleDataResourceState  
{
    /** Copyright statement. */
    private static final String COPYRIGHT_NOTICE = 
        "Copyright (c) The University of Edinburgh, 2010-2013.";

    /** Properties. */
    private TestProperties mProperties;
    
    /** Resource state. */
    private GenericResourceState mResourceState;
    
    /**
     * Constructor. Will auto-generate a resource ID.
     *
     * @param properties
     *     properties.
     * @throws TestPropertyNotFoundException 
     */
    public MockGenericResourceState(TestProperties properties)
    throws TestPropertyNotFoundException
    {
        this(new ResourceID(UniqueName.newName()), properties);
    }

    /**
     * Constructor.
     *
     * @param ResourceID
     *     Resource ID.
     * @param properties
     *     properties.
     * @throws TestPropertyNotFoundException 
     */
    public MockGenericResourceState(ResourceID resourceID,
                                    TestProperties properties)
    throws TestPropertyNotFoundException
    {
        mProperties = properties;
        DataResourceState resourceState = new SimpleDataResourceState(resourceID);
        mResourceState = new GenericResourceState(resourceState);
        mKeyValueProperties = new SimpleKeyValueProperties();
        @SuppressWarnings("unchecked")
        Enumeration<String> keys = mProperties.propertyNames();
        while(keys.hasMoreElements())
        {
            String key = keys.nextElement();
            Key keyvp = new Key(key.toString());
            mKeyValueProperties.put(keyvp, mProperties.getProperty(key));
        }
        
    }

    /**
     * Constructor. Will auto-generate a resource ID.
     *
     * @param properties
     *     properties.
     * @throws TestPropertyNotFoundException 
     */
    public MockGenericResourceState(Properties properties)
    {
        this(new ResourceID(UniqueName.newName()), properties);
    }

    /**
     * Constructor.
     *
     * @param ResourceID
     *     Resource ID.
     * @param properties
     *     properties.
     * @throws TestPropertyNotFoundException 
     */
    public MockGenericResourceState(ResourceID resourceID,
                                    Properties properties)
    {
        DataResourceState resourceState = new SimpleDataResourceState(resourceID);
        mResourceState = new GenericResourceState(resourceState);
        mKeyValueProperties = new SimpleKeyValueProperties();
        for (Entry<Object, Object> entry : properties.entrySet())
        {
            Key keyvp = new Key(entry.getKey().toString());
            mKeyValueProperties.put(keyvp, entry.getValue());
        }
        
    }
}
