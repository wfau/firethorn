package uk.ac.roe.wfau.firethorn.liquibase;

import java.util.HashMap;
import java.util.Map;

import liquibase.change.ChangeMetaData;
import liquibase.change.ColumnConfig;
import liquibase.change.DatabaseChange;
import liquibase.change.core.CreateTableChange;
import lombok.extern.slf4j.Slf4j;
import uk.ac.roe.wfau.firethorn.meta.base.BaseColumn;
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
        log.debug("  Table [{}][{}][{}][{}]", table.ident(), table.schema().catalog(), table.schema().schema(), table.name());
        this.table = table ;
        this.setTableName(
            this.table.name()
            );
        this.setCatalogName(
            this.table.schema().catalog()
            );
        this.setSchemaName(
            this.table.schema().schema()
            );

        for (final JdbcColumn column : table.columns().select())
            {
            log.debug("  Column [{}][{}][{}][{}]", column.ident(), column.name(), column.meta().jdbc().type(), column.meta().jdbc().size());

            final String colname = column.meta().jdbc().create().name(); 
            final String coltype = column.meta().jdbc().create().type(); 

            log.debug("  Name [{}]", colname);
            log.debug("  Type [{}]", coltype);

            this.addColumn(
                new ColumnConfig().setName(
                    colname
                    ).setType(
                        coltype
                        )
                );
            }
        }

    private final JdbcTable table ;
    public  JdbcTable table()
        {
        return this.table;
        }
    }