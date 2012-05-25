package adql.db.exception;

/*
 * This file is part of ADQLLibrary.
 * 
 * ADQLLibrary is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * ADQLLibrary is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with ADQLLibrary.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * Copyright 2012 - UDS/Centre de Données astronomiques de Strasbourg (CDS)
 */

import java.util.ArrayList;
import java.util.Iterator;

import adql.db.DBChecker;
import adql.parser.ParseException;

/**
 * <p>
 * 	This exception is thrown by {@link DBChecker} when several columns or tables do not exist.
 * 	It lists several {@link ParseException} (either {@link UnresolvedColumnException} or {@link UnresolvedTableException}).
 * </p>
 * <p>
 * 	Its message only tells the number of unresolved identifiers.
 * 	If you want to have more details about the position and the exact message of each exception, you just have to iterate
 * 	on this {@link UnresolvedIdentifiersException} (method {@link #iterator()}).
 * </p>
 * 
 * @author Gr&eacute;gory Mantelet (CDS)
 * @version 01/2012
 * 
 * @see DBChecker
 */
public class UnresolvedIdentifiersException extends ParseException implements Iterable<ParseException> {
	private static final long serialVersionUID = 1L;

	/** List of exceptions (one per unresolved identifier). */
	protected ArrayList<ParseException> exceptions;

	/**
	 * Build an empty {@link UnresolvedIdentifiersException} (that's to say: there is no unresolved identifier).
	 */
	public UnresolvedIdentifiersException() {
		exceptions = new ArrayList<ParseException>();
	}

	/**
	 * Adds a {@link ParseException} (supposed to be either an {@link UnresolvedColumnException} or an {@link UnresolvedTableException}).
	 * 
	 * @param pe	An exception.
	 */
	public final void addException(final ParseException pe){
		exceptions.add(pe);
	}

	/**
	 * Gets the number of unresolved identifiers.
	 * 
	 * @return	The number of unresolved identifiers.
	 */
	public final int getNbErrors(){
		return exceptions.size();
	}

	/**
	 * Gets the list of all errors.
	 * 
	 * @return	Errors list.
	 */
	public final Iterator<ParseException> getErrors(){
		return exceptions.iterator();
	}

	public final Iterator<ParseException> iterator(){
		return getErrors();
	}

	/**
	 * Only tells how many identifiers have not been resolved.
	 * 
	 * @see java.lang.Throwable#getMessage()
	 */
	@Override
	public String getMessage() {
		return exceptions.size()+" unresolved identifiers !";
	}



}
