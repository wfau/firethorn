/*
 *  Copyright (C) 2013 Royal Observatory, University of Edinburgh, UK
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

import org.springframework.stereotype.Component;

import liquibase.changelog.ChangeSet;
import lombok.extern.slf4j.Slf4j;
import uk.ac.roe.wfau.firethorn.meta.jdbc.JdbcTable;

/**
 *
 *
 */
@Slf4j
@Component
public class JdbcTableBuilder
extends JdbcBuilderBase
implements JdbcTable.OldBuilder
    {

    @Override
    public JdbcTable create(final JdbcTable table)
        {
        log.debug("create(JdbcTable)");
        log.debug("  table [{}][{}]", table.ident(), table.name());

        log.debug("--- 000 ");
        final ChangeSet changeset = changeset();

        log.debug("--- 001 ");
        changeset.addChange(
            new CreateJdbcTableChange(
                table
                )
            );

        log.debug("--- 002 ");
        execute(
            table.resource(),
            changeset
            );

        log.debug("--- 003 ");
        return table;
        }

    @Override
    public void delete(final JdbcTable table)
        {
        // TODO Auto-generated method stub
        }
    }
