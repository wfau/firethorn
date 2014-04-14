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
package uk.ac.roe.wfau.firethorn.ogsadai.activity.client;

/**
 * Public interface for the pipeline parameters.
 *
 */
public interface PipelineParam
    {
    
    public String source();
    public String query();

    //public String store();
    //public String table();
    //public String rownum();

    public RownumClient.Param rows();
    
    public InsertClient.Param insert();
    
    public DelaysClient.Param  delays();

    public LimitsClient.Param limits();

    }
