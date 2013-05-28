package uk.ac.roe.wfau.firethorn.liquibase;

import liquibase.change.ChangeMetaData;
import liquibase.change.ColumnConfig;
import liquibase.change.DatabaseChange;
import liquibase.change.core.CreateTableChange;
import lombok.extern.slf4j.Slf4j;
import uk.ac.roe.wfau.firethorn.meta.jdbc.JdbcColumn;
import uk.ac.roe.wfau.firethorn.meta.jdbc.JdbcTable;

@Slf4j
@DatabaseChange(name="createTable", description = "Create table", priority = ChangeMetaData.PRIORITY_DEFAULT)
public class CreateJdbcTableChange
extends CreateTableChange
    {
    public CreateJdbcTableChange(final JdbcTable table)
        {
        log.debug("CreateJdbcTableChange(JdbcTable)");
        log.debug("  Table [{}][{}]", table.ident(), table.name());
        this.table = table ;
        this.setTableName(
            this.table.name()
            );
        this.setSchemaName(
            this.table.schema().name()
            );
        for (final JdbcColumn column : table.columns().select())
            {
            this.addColumn(
                new ColumnConfig().setName(
                    column.name()
                    ).setType(
                        column.meta().jdbc().type().name()
                        )
                );
            }
        }

    private JdbcTable table ;
    public  JdbcTable table()
        {
        return this.table;
        }
    }