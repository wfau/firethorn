package uk.ac.roe.wfau.firethorn.liquibase;

import liquibase.change.DatabaseChangeProperty;
import liquibase.statement.AbstractSqlStatement;
import lombok.extern.slf4j.Slf4j;
import uk.ac.roe.wfau.firethorn.meta.jdbc.JdbcSchema;

@Slf4j
public class CreateSchemaStatement
extends AbstractSqlStatement
    {
    public CreateSchemaStatement(final JdbcSchema schema)
        {
        this(
            schema.catalog(),
            schema.name()
            );
        }
    public CreateSchemaStatement(final String catalog, final String schema)
        {
        log.debug("CreateSchemaStatement(final String, String)");
        log.debug("  Catalog [{}]", catalog);
        log.debug("  Schema  [{}]", schema);
        this.schema  = schema  ;
        this.catalog = catalog ;
        }

    private String schema;

    @DatabaseChangeProperty()
    public String getSchemaName()
        {
        return this.schema;
        }
    public void setSchemaName(final String schema)
        {
        this.schema = schema ;
        }

    private String catalog;

    @DatabaseChangeProperty()
    public String getCatalogName()
        {
        return this.catalog;
        }
    public void setCatalogName(final String catalog)
        {
        this.catalog = catalog ;
        }
    }