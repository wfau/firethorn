package uk.ac.roe.wfau.firethorn.liquibase;

import java.util.ArrayList;
import java.util.List;

import liquibase.CatalogAndSchema;
import liquibase.database.Database;
import liquibase.exception.ValidationErrors;
import liquibase.sql.Sql;
import liquibase.sql.UnparsedSql;
import liquibase.sqlgenerator.SqlGeneratorChain;
import liquibase.sqlgenerator.core.AbstractSqlGenerator;
import liquibase.structure.core.Schema;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CreateSchemaGenerator
extends AbstractSqlGenerator<CreateSchemaStatement>
    {

    @Override
    public Sql[] generateSql(CreateSchemaStatement statement, Database database, SqlGeneratorChain chain)
        {

        log.debug("generateSql(CreateSchemaStatement, Database, SqlGeneratorChain)");
        log.debug("  Catalog [{}]", statement.getCatalogName());
        log.debug("  Schema  [{}]", statement.getSchemaName());
        
        List<Sql> list = new ArrayList<Sql>();

        StringBuffer buffer = new StringBuffer();
        buffer.append("CREATE SCHEMA ");
        buffer.append(
            database.correctSchema(
                new CatalogAndSchema(
                    statement.getCatalogName(),
                    statement.getSchemaName()
                    )
                ).toString()
            );

        log.debug("  Buffer  [{}]", buffer.toString());

        list.add(
            new UnparsedSql(
                buffer.toString(),
                new Schema(
                    statement.getCatalogName(),
                    statement.getSchemaName()
                    )
                )
            );

        log.debug("  Buffer  [{}]", buffer.toString());

        return list.toArray(new Sql[list.size()]);
        }

    @Override
    public ValidationErrors validate(CreateSchemaStatement statement, Database database, SqlGeneratorChain chain)
        {
        ValidationErrors errors = new ValidationErrors();
        errors .checkRequiredField("schemaName", statement.getSchemaName());
        return errors ;
        }
    }