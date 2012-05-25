/*
 *
 * Copyright (c) 2012, ROE (http://www.roe.ac.uk/)
 * All rights reserved.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package uk.ac.roe.wfau.firethorn.cds.tap;

import java.sql.ResultSet;


public class FireThornTapResult
    {

    protected ResultSet results ;

    public FireThornTapResult(ResultSet results)
        {
        this.results = results ;
        }

    public ResultSet results()
        {
        return this.results ;
        }

    @Override
    public String toString()
        {
        return "firethorn TAP result" ;
        }

    }
