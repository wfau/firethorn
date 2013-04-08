/*
 *  Copyright (C) 2012 Royal Observatory, University of Edinburgh, UK
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package uk.ac.roe.wfau.firethorn.liquibase;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import liquibase.change.ColumnConfig;
import liquibase.change.core.AddColumnChange;
import liquibase.change.core.CreateTableChange;
import liquibase.change.core.ModifyDataTypeChange;
import liquibase.changelog.ChangeSet;
import liquibase.changelog.DatabaseChangeLog;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import lombok.extern.slf4j.Slf4j;

import org.junit.Test;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import uk.ac.roe.wfau.firethorn.meta.jdbc.JdbcProductType;
import uk.ac.roe.wfau.firethorn.meta.jdbc.JdbcResource;
import uk.ac.roe.wfau.firethorn.test.TestBase;

/**
 * TODO experiment with this
 * http://static.springsource.org/spring/docs/3.1.x/javadoc-api/org/springframework/jdbc/datasource/embedded/EmbeddedDatabaseBuilder.html
 *
 */
@Slf4j
@Transactional(
    readOnly=false,
    propagation=Propagation.REQUIRES_NEW
    )
@TransactionConfiguration(
    transactionManager="FireThornTransactionManager",
    defaultRollback = false
    )
public class LiquibaseTestCase
extends TestBase
    {

    /**
     * SQL safe name.
     * 
     */
    public String safe(final String input)
        {
        return input.replaceAll(
            "[^\\p{Alnum}]+?",
            "_"
            );
        }
    
    /**
     * Check the database metadata.
     *
     */
    @Test
    public void test001()
    throws Exception
        {
        assertNotNull(
            factories()
            );
        final JdbcResource resource = factories().jdbc().resources().create(
            "test-resource",
            unique("resource")
            );
        assertNotNull(
            resource
            );
        resource.connection().url(
            "spring:HsqldbFileTest"
            );
        assertNotNull(
            resource.connection().open()
            );
        assertNotNull(
            resource.connection().metadata()
            );
        assertEquals(
            "HSQL Database Engine",
            resource.connection().metadata().getDatabaseProductName()
            );
        assertEquals(
            JdbcProductType.HSQLDB.alias(),
            resource.connection().metadata().getDatabaseProductName()
            );
        assertEquals(
            JdbcProductType.HSQLDB,
            JdbcProductType.match(
                resource.connection().metadata().getDatabaseProductName()
                )
            );
        }

    
    /**
     * Check stuff ...
     *
     */
    @Test
    public void test002()
    throws Exception
        {
        assertNotNull(
            factories()
            );
        final JdbcResource resource = factories().jdbc().resources().create(
            "test-resource",
            unique("resource")
            );
        assertNotNull(
            resource
            );
        resource.connection().url(
            "spring:HsqldbFileTest"
            );


        DatabaseChangeLog changelog = new DatabaseChangeLog();

        DatabaseFactory factory = DatabaseFactory.getInstance();
        
        JdbcConnection connection = new JdbcConnection(
            resource.connection().open()
            ); 

        Database database = factory.findCorrectDatabaseImplementation(
            connection 
            ); 
        
        CreateTableChange createtab = new CreateTableChange();
        createtab.setTableName(
            safe(
                unique(
                    "table"
                    )
                )
            );

        ChangeSet createtabset = new ChangeSet(
            unique("001"),
            "author",
            false,
            false,
            "file",
            "",
            ""
            ); 

        createtabset.addChange(
            createtab
            );

        ChangeSet.ExecType result = createtabset.execute(
            changelog,
            database
            );

        log.debug(" result [{}]", result);
        log.debug(" log    [{}]", changelog);

        
        
        ColumnConfig column1 = new ColumnConfig().setName(safe(unique("column"))).setType("bigint");

        ColumnConfig column2 = new ColumnConfig().setName(safe(unique("column"))).setType("float");

        AddColumnChange createcols = new AddColumnChange();
        createcols.addColumn(
            column1
            );
        createcols.addColumn(
            column2
            );
        createcols.setTableName(
            createtab.getTableName()
            );

        ChangeSet createcolset = new ChangeSet(
            unique("002"),
            "author",
            false,
            false,
            "file",
            "",
            ""
            ); 

        createcolset.addChange(
            createcols
            );

        result = createcolset.execute(
            changelog,
            database
            );
        
        log.debug(" result [{}]", result);
        log.debug(" log    [{}]", changelog);


        ModifyDataTypeChange update1 = new ModifyDataTypeChange();
        update1.setColumnName(
            column1.getName()
            );
        update1.setTableName(
            createtab.getTableName()
            );
        update1.setNewDataType(
            "float"
            );
        
        ModifyDataTypeChange update2 = new ModifyDataTypeChange();
        update2.setColumnName(
            column2.getName()
            );
        update2.setTableName(
            createtab.getTableName()
            );
        update2.setNewDataType(
            "float"
            );
        
        ChangeSet updateset = new ChangeSet(
            unique("003"),
            "author",
            false,
            false,
            "file",
            "",
            ""
            ); 

        updateset.addChange(
            update1
            );
        updateset.addChange(
            update2
            );

        result = updateset.execute(
            changelog,
            database
            );

        log.debug(" result [{}]", result);
        log.debug(" log    [{}]", changelog);

        database.commit();
        database.close();
        }
    }


