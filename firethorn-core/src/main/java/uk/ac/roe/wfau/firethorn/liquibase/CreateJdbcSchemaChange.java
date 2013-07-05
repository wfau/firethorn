package uk.ac.roe.wfau.firethorn.liquibase;

import java.util.ArrayList;
import java.util.List;

import liquibase.change.AbstractChange;
import liquibase.change.ChangeMetaData;
import liquibase.change.DatabaseChange;
import liquibase.database.Database;
import liquibase.statement.SqlStatement;
import lombok.extern.slf4j.Slf4j;
import uk.ac.roe.wfau.firethorn.meta.jdbc.JdbcSchema;

@Slf4j
@DatabaseChange(name="createSchema", description = "Create schema", priority = ChangeMetaData.PRIORITY_DEFAULT)
public class CreateJdbcSchemaChange extends AbstractChange
    {
    public CreateJdbcSchemaChange(final JdbcSchema schema)
        {
        super();
        log.debug("CreateJdbcSchemaChange(JdbcSchema)");
        log.debug("  Schema [{}][{}]", schema.ident(), schema.name());
        this.schema = schema ;
        }

    private final JdbcSchema schema;
    public  JdbcSchema schema()
        {
        return this.schema;
        }

    public String getCatalogName()
        {
        return null ;
        }
    public String getSchemaName()
        {
        return null ;
        }

    @Override
    public SqlStatement[] generateStatements(final Database database)
        {
        log.debug("generateStatements(Database)");

        final List<SqlStatement> statements = new ArrayList<SqlStatement>();
        statements.add(
            new CreateSchemaStatement(this.schema)
            );

        return statements.toArray(
            new SqlStatement[statements.size()]
            );

        }
    @Override
    public String getConfirmationMessage()
        {
        log.debug("getConfirmationMessage()");
        return null;
        }
    }