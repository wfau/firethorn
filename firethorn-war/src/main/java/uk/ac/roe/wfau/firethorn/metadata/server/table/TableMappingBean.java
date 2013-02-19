/**
 *
 */
package uk.ac.roe.wfau.firethorn.metadata.server.table;

import uk.ac.roe.wfau.firethorn.meta.base.BaseTable;

/**
 * Bean wrapper for <code>TableMapping</code>.
 *
 */
public class TableMappingBean
    {

    /**
     * Public constructor.
     *
     */
    public TableMappingBean(final BaseTable<?,?> table)
        {
        this.table = table;
        }

    private final BaseTable<?,?> table ;
    protected BaseTable<?,?> table()
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
