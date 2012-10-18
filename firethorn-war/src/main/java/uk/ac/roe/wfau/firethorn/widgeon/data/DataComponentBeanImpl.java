/**
 *
 *
 */
package uk.ac.roe.wfau.firethorn.widgeon.data;

import java.net.URI;

import uk.ac.roe.wfau.firethorn.webapp.control.AbstractEntityBeanImpl;
import uk.ac.roe.wfau.firethorn.widgeon.data.DataComponent.Status;

/**
 * DataComponent bean implementation.
 *
 */
public class DataComponentBeanImpl<EntityType extends DataComponent>
extends AbstractEntityBeanImpl<EntityType >
implements DataComponentBean<EntityType>
    {

    public DataComponentBeanImpl(URI type, EntityType entity)
        {
        super(
            type,
            entity
            );
       }

    public Status getStatus()
        {
        return entity().status();
        }
    }
