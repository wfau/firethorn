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

    /*
     * Try creating a table.
     *
     */
    EXECUTE AS user = '{databaseuser}'
        CREATE TABLE [{databasename}].[dbo].[test](col int)
    REVERT
    go

    /*
     * Try inserting some data.
     *
     */
    EXECUTE AS user = '{databaseuser}'
        INSERT INTO [{databasename}].[dbo].[test] (col) VALUES (1);
        INSERT INTO [{databasename}].[dbo].[test] (col) VALUES (2);
        INSERT INTO [{databasename}].[dbo].[test] (col) VALUES (3);
    REVERT
    go

    /*
     * Try selecting some data.
     *
     */
    EXECUTE AS user = '{databaseuser}'
        SELECT * FROM [{databasename}].[dbo].[test]
    REVERT
    go

