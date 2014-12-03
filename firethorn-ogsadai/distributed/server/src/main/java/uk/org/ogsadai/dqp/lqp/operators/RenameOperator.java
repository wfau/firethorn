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

package uk.org.ogsadai.dqp.lqp.operators;

import java.util.ArrayList;
import java.util.List;

import uk.org.ogsadai.common.msgs.DAILogger;
import uk.org.ogsadai.dqp.lqp.Attribute;
import uk.org.ogsadai.dqp.lqp.AttributeMatchMode;
import uk.org.ogsadai.dqp.lqp.AttributeUtils;
import uk.org.ogsadai.dqp.lqp.HeadingImpl;
import uk.org.ogsadai.dqp.lqp.Operator;
import uk.org.ogsadai.dqp.lqp.OperatorID;
import uk.org.ogsadai.dqp.lqp.OperatorVisitor;
import uk.org.ogsadai.dqp.lqp.RenameMap;
import uk.org.ogsadai.dqp.lqp.SimpleRenameMap;
import uk.org.ogsadai.dqp.lqp.exceptions.LQPException;

/**
 * Operator RANAME.
 * 
 * @author The OGSA-DAI Project Team.
 */
public class RenameOperator extends UnaryOperator
{
    /** Copyright notice. */
    private static final String COPYRIGHT_NOTICE = 
        "Copyright (c) The University of Edinburgh, 2008";

    /** Rename map. */
    private RenameMap mRenameMap;
    /** Renamed relation name. */
    private String mRelationName;
    /** Logger. */
    private static final DAILogger LOG =
        DAILogger.getLogger(RenameOperator.class);

    /**
     * Constructor.
     */
    protected RenameOperator()
    {
        mID = OperatorID.RENAME;
    }

    /**
     * Constructs a disconnected RENAME operator.
     * 
     * @param renameMap
     *            rename map
     */
    public RenameOperator(RenameMap renameMap)
    {
        this();
        mRenameMap = renameMap;
    }

    /**
     * Constructs a connected RENAME operator.
     * 
     * @param child
     *            child operator
     * @param renameMap
     *            rename map
     */
    public RenameOperator(Operator child, RenameMap renameMap)
    {
        this(renameMap);
        setChild(0, child);
    }

    /**
     * Constructs a disconnected RENAME operator.
     * 
     * @param relationName
     *            renamed relation name
     */
    public RenameOperator(String relationName)
    {
        this();
        mRelationName = relationName;
    }

    /**
     * Constructs a connected RENAME operator.
     * 
     * @param child
     *            child operator
     * @param relationName
     *            renamed relation name
     */
    public RenameOperator(Operator child, String relationName)
    {
        this(relationName);
        setChild(0, child);
    }

    /**
     * Gets renamed relation name.
     * 
     * @return renamed relation name
     */
    public String getRelationName()
    {
        return mRelationName;
    }

    /**
     * Gets rename map.
     * 
     * @return rename map
     */
    public RenameMap getRenameMap()
    {
        return mRenameMap;
    }

    /**
     * {@inheritDoc}
     */
    public void update() throws LQPException
    {
        super.update();

        List<Attribute> inputAttrList = mChildOperator.getHeading()
            .getAttributes();

        // If empty rename map has been passed to the constructor we have
        // relation rename.
        if(mRelationName != null && mRenameMap == null)
        {
            mRenameMap = new SimpleRenameMap();
            for(Attribute attr : inputAttrList)
                mRenameMap.add(attr, attr.getCloneNewSource(mRelationName, false));
        }
        else if(mRelationName != null)
        {
            mRenameMap.resetRenamedAttributeSources(mRelationName);
        }
        
        checkRenameMap();
        mOperatorHeading = new HeadingImpl(mRenameMap.getRenamedAttributeList());
    }
    
    /**
     * {@inheritDoc}
     */
    public void validate() throws LQPException
    {
        super.validate();
        checkRenameMap();
    }

    /**
     * Checks validity of the rename map and reorders it if needed.
     * 
     * Original attribute list should be equal to the attribute list from the
     * child heading. Some optimisations may reorder headings of intermediate
     * relations. In such cases attribute reordering should be possible.
     * 
     * @throws LQPException
     *             if the list of original attributes does not match attributes
     *             in the child heading
     */
    private void checkRenameMap() throws LQPException
    {
        List<Attribute> origAttrList = mRenameMap.getOriginalAttributeList();
        List<Attribute> renamedAttrList = mRenameMap.getRenamedAttributeList();
        List<Attribute> inputAttrList = mChildOperator.getHeading()
            .getAttributes();

	if (!AttributeUtils.listEqual(origAttrList, inputAttrList,
		AttributeMatchMode.NAME_AND_NULL_SOURCE))
	{
	    // try to reorder
	    List<Attribute> newOrigAttrList = new ArrayList<Attribute>();
	    List<Attribute> newRenamedAttrList = new ArrayList<Attribute>();

	    for (Attribute attr : inputAttrList)
	    {
		int idx = AttributeUtils.getMatchingIndex(attr, origAttrList,
			AttributeMatchMode.NAME_AND_NULL_SOURCE);

		if (idx == -1)
		{
		    LOG.debug("ORIG: " + origAttrList);
		    LOG.debug("RENAMED: " + renamedAttrList);

		    throw new LQPException(
			    "A list of original attributes must be identical with a "
				    + "list of attributes contained in child's heading.");
		}
		else
		{
		    newOrigAttrList.add(origAttrList.get(idx));
		    newRenamedAttrList.add(renamedAttrList.get(idx));
		}
	    }
	    mRenameMap = new SimpleRenameMap(newOrigAttrList,
		    newRenamedAttrList);
	}
    }
    
    /**
     * {@inheritDoc}
     */
    public void accept(OperatorVisitor visitor)
    {
        visitor.visit(this);
    }

}
