package uk.org.ogsadai.dqp.firethorn;

import uk.ac.roe.wfau.firethorn.ogsadai.metadata.client.TableMapping;

public class SimpleTableMapping implements TableMapping 
{
    
    private String mAlias;
    private String mLocalName;
    private String mResource;

    public SimpleTableMapping(
            String alias, String localName, String resourceIdentifier)
    {
        mAlias = alias;
        mLocalName = localName;
        mResource = resourceIdentifier;
    }

    @Override
    public String tableAlias() 
    {
        return mAlias;
    }

    @Override
    public String tableName() 
    {
        return mLocalName;
    }

    @Override
    public String resourceIdent() 
    {
        return mResource;
    }

    @Override
    public String toString() 
    {
        return "TableMapping(alias=" + mAlias + ", local name=" +
                mLocalName + ", resource=" + mResource + ")";
    }
}
