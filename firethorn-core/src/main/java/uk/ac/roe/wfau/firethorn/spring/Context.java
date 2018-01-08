package uk.ac.roe.wfau.firethorn.spring;

import uk.ac.roe.wfau.firethorn.identity.Authenticated;
import uk.ac.roe.wfau.firethorn.identity.Identity;
import uk.ac.roe.wfau.firethorn.identity.Operation;

/**
 * The current context.
 * @todo Move this to identity package ?
 *
 */
public interface Context
    {
    /**
     * The current Operation.
     *
     */
    public Operation oper();

    /**
     * The primary Authentication.
     *
     */
    public Authenticated auth();

    /**
     * The primary Identity.
     *
     */
    public Identity identity();

    /**
     * The data storage space.
     * 
    public DataSpace space();
     */
    
    /**
     * Factory interface.
     * 
     */
    public interface Factory
        {
        /**
         * Access to the current Context.
         * 
         */
        public Context current();

        }
    }