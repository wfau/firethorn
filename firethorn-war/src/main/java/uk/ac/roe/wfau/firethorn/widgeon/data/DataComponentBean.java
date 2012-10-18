/**
 *
 *
 */
package uk.ac.roe.wfau.firethorn.widgeon.data;

import uk.ac.roe.wfau.firethorn.webapp.control.EntityBean;
import uk.ac.roe.wfau.firethorn.widgeon.data.DataComponent.Status;

/**
 * Public interface for a DataComponent bean.
 *
 */
public interface DataComponentBean<EntityType extends DataComponent>
    extends EntityBean<EntityType>
    {

    /**
     * Access the component status.
     *
     */
    public Status getStatus();
    
    }
