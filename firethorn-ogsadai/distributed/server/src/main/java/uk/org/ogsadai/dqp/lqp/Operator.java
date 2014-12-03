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

import java.util.Set;

import uk.org.ogsadai.dqp.lqp.exceptions.LQPException;

/**
 * Operator interface.
 * 
 * @author The OGSA-DAI Project Team.
 */
public interface Operator extends Annotatable
{
    /**
     * Returns the parent operator.
     * 
     * @return parent operator or <code>null</code> if not connected.
     */
    public Operator getParent();

    /**
     * Sets the parent operator.
     * 
     * @param parent
     *            parent operator
     */
    public void setParent(Operator parent);

    /**
     * Gets child at the specified position. Position numbering starts from 0.
     * 
     * @param position
     *            child position
     * @return child at a given position
     */
    public Operator getChild(int position);

    /**
     * Gets the number of children the operator has.
     * 
     * @return the number of children the operator has.
     */
    public int getChildCount();

    /**
     * Sets operator child at a given position. Child's parent is also set
     * accordingly by this call.
     * 
     * @param position
     *            operator position
     * @param child
     *            child operator
     */
    public void setChild(int position, Operator child);

    /**
     * Replaces the current child with a new child. Sets parent of the replaced
     * child to <code>null</code>.
     * 
     * @param currentChild
     *            child to be replaced
     * @param newChild
     *            replacement
     */
    public void replaceChild(Operator currentChild, Operator newChild);

    /**
     * Gets operator id.
     * 
     * @return operator id
     */
    public OperatorID getID();

    /**
     * Returns true if this operator marks the leaf of a partition and any
     * children it has will be in a different partition. Exchange consumer
     * operators must return <tt>true</tt> here, other operators must return
     * <tt>false</tt>.
     * 
     * @return <tt>true</tt> if this is a partition leaf, <tt>false</tt>
     *         otherwise.
     */
    public boolean isPartitionLeaf();

    /**
     * Indicates if operator is binary.
     * 
     * @return <code>true</code> if operator is binary, <code>false</code>
     *         otherwise
     */
    public boolean isBinary();

    /**
     * Operator forwards the validation call to its children and validates
     * itself. Validation depends on operator logic. This usually involves (but
     * is not limited to) checking if all attributes required to perform
     * operator logic are in children's headings.
     * 
     * @throws LQPException
     *             when validation fails
     */
    public void validate() throws LQPException;

    /**
     * Operator forwards the update call to its children and updates itself.
     * Operators must be updated after each change of their children. Update
     * usually involves (but is not limited to) rebuilding the operator heading.
     * 
     * @throws LQPException
     *             when update fails
     */
    public void update() throws LQPException;

    /**
     * Gets operator heading.
     * 
     * @return operator heading
     */
    public Heading getHeading();

    /**
     * Disconnects operator. Sets relevant references to <code>null</code>.
     * Disconnected operator is not referring to any other operators. However,
     * children and parent may still refer to the disconnected operator.
     */
    public void disconnect();

    /**
     * Gets a set of attributes required by the operator.
     * 
     * @return set of all the attributes used by the operator.
     */
    public Set<Attribute> getUsedAttributes();

    /**
     * Rename attributes From -> To as defined in a map.
     * 
     * @param renameMap
     *            rename map
     * @throws LQPException
     *             when rename is impossible
     */
    public void renameUsedAttributes(RenameMap renameMap) throws LQPException;

    /**
     * Accepts operator visitor.
     * 
     * @param visitor
     *            visitor object
     */
    public void accept(OperatorVisitor visitor);
}
