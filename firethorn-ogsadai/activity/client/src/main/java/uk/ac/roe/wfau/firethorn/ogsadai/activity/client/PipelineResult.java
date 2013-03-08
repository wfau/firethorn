/**
 * Copyright (c) 2013, ROE (http://www.roe.ac.uk/)
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
package uk.ac.roe.wfau.firethorn.ogsadai.activity.client;

/**
 *
 *
 */
public interface PipelineResult
    {

    /**
     * Factory interface.
     *
     */
    public interface Factory
        {
        public PipelineResult create(final Throwable cause);
        public PipelineResult create(final Result result);
        public PipelineResult create(final Result result, final String message);
        public PipelineResult create(final Result result, final String message, final Throwable cause);
        }
    
    public enum Result {
        EDITING(),
        RUNNING(),
        COMPLETED(),
        CANCELLED(),
        FAILED(),
        UNKNOWN();
        }

    public Result result();

    public String message();

    public Throwable cause();

    }
