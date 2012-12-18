/**
 * 
 */
package uk.ac.roe.wfau.firethorn.widgeon.ogsa;

import uk.ac.roe.wfau.firethorn.tuesday.TuesdayOgsaTable;

/**
 * Bean wrapper for <code>OgsaTable</code>.
 *
 */
public class OgsaTableBean
    {
    
    /**
     * Public constructor.
     * 
     */
    public OgsaTableBean(TuesdayOgsaTable<?,?> table)
        {
        this.table = table;
        }

    private final TuesdayOgsaTable<?,?> table ;
    protected TuesdayOgsaTable<?,?> table()
        {
        return this.table;
        }

    /**
     * The table alias.
     * <br/> 
     * This is the table alias used in SQL queries passed into OGSA-DAI,
     * before the mapping from table alias to fully qualified resource table name.
     * @return The table alias.
     *
     */
    public String getAlias()
        {
        return this.table.alias();
        }

    /**
     * Get the fully qualified table name (catalog.schema.table) in the target resource.  
     * @return The fully qualified table name.
     *
     */
    public String getName()
        {
        return this.table.fullname().toString();
        }
    
    /**
     * Get the target resource identifier.  
     * @return The target resource identifier.
     *
     */
    public String getResource()
        {
        return this.table.resource().ogsaid();
        }
    }
