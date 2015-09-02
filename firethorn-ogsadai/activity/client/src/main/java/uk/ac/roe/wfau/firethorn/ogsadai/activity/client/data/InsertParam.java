/**
 * 
 */
package uk.ac.roe.wfau.firethorn.ogsadai.activity.client.data;

/**
 * Public interface for insert activity parameters.
 *
 */
public interface InsertParam
	{

    /**
     * The target resource ID, as a String.
     * @return The target resource ID.
     *
     */
    public String resource();
    
    /**
     * The target table name.
     * @return The target table name.
     *
     */
    public String table();

    /**
     * The first block size.
     * @return The first block size.
     *
     */
    public Integer first();

    /**
     * The main block size.
     * @return The main block size.
     *
     */
    public Integer block();
    
    /**
     * The callback update identifier.
     * 
     */
    public String update();

	}
