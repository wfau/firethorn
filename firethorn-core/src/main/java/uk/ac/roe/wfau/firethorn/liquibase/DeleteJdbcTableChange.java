package uk.ac.roe.wfau.firethorn.liquibase;

import liquibase.change.core.DropTableChange;
import lombok.extern.slf4j.Slf4j;
import uk.ac.roe.wfau.firethorn.meta.jdbc.JdbcTable;

@Slf4j
public class DeleteJdbcTableChange
extends DropTableChange 
    {
    public DeleteJdbcTableChange(final JdbcTable table)
        {
        log.debug("DeleteJdbcTableChange(JdbcTable)");
        log.debug("  Table [{}][{}]", table.ident(), table.name());
        this.table = table ;
        this.setTableName(
            this.table.name()
            );
        this.setSchemaName(
            this.table.schema().name()
            );
        }

    private JdbcTable table ;
    public  JdbcTable table()
        {
        return this.table;
        }
    }