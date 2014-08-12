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
package uk.ac.roe.wfau.firethorn.ogsadai.activity.server;

/**
 *
 *
 */
public class SimpleProcessingException
extends Exception
    {
    /**
     * Serial version ID. 
     *
     */
    private static final long serialVersionUID = -6683378107860003836L;

    /**
     * Public constructor.
     * 
     */
    public SimpleProcessingException()
        {
        super();
        }

    /**
     * Public constructor.
     * 
     */
    public SimpleProcessingException(final String message)
        {
        super(message);
        }
    }
