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

import uk.org.ogsadai.resource.ResourceID;

/**
 *
 *
 */
public interface WorkflowResult
    {
    /**
     * The workflow staus.
     *
     */
    public enum Status {
        CREATED(),
        RUNNING(),
        COMPLETED(),
        CANCELLED(),
        FAILED(),
        UNKNOWN();
        }

    /**
     * The workflow staus.
     *
     */
    public Status status();

    /**
     * The workflow request resource ID.
     *
     */
    public ResourceID request();
    
    /**
     * A text message associated with an error.
     *
     */
    public String message();

    /**
     * A text message associated with an error.
     *
     */
    public Throwable cause();

    }
