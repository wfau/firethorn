/*
 *  Copyright (C) 2014 Royal Observatory, University of Edinburgh, UK
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
package uk.ac.roe.wfau.firethorn.adql.parser.green;

import java.util.Collection;
import java.util.HashMap;

import lombok.extern.slf4j.Slf4j;

import adql.db.DBChecker;
import adql.db.DBTable;
import adql.db.SearchColumnList;
import adql.db.SearchTableApi;
import adql.db.SearchTableList;
import adql.parser.ParseException;
import adql.query.ADQLQuery;
import adql.query.from.ADQLTable;

/**
 *
 *
 */
@Slf4j
public class MyQueryCheckerImpl
    extends DBChecker
    implements MyQueryChecker
    {

    /**
     * Public constructor.
     * 
     */
    public MyQueryCheckerImpl()
        {
        super();
        }

    /**
     * Public constructor.
     * 
     */
    public MyQueryCheckerImpl(final SearchTableApi list)
        {
        super();
        this.lstTables = list ;
        }

    /**
     * Public constructor.
     * 
     */
    public MyQueryCheckerImpl(final Collection<DBTable> tables)
        {
        super(
            tables
            );
        }
    
    @Override
    public void check(final ADQLQuery query) throws ParseException
        {
        log.debug("check(ADQLQuery)");
        super.check(
            query
            );
        }

    @Override
    public void check(ADQLQuery query, SearchColumnList stackColumnList, HashMap<DBTable, ADQLTable> _mapTables) throws ParseException
        {
        log.debug("check(ADQLQuery, SearchColumnList, HashMap)");
        super.check(
            query,
            stackColumnList,
            _mapTables
            );
        }
    }
