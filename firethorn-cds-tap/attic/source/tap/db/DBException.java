package tap.db;

import tap.TAPException;

/**
 * This kind of exception is thrown by instances of {@link DBConnection}
 * if an error occurs while a DB operation (interrogation, update, transaction management).
 * 
 * @author Gr&eacute;gory Mantelet (CDS)
 * @version 09/2011
 * 
 * @see DBConnection
 */
public class DBException extends TAPException {
	private static final long serialVersionUID = 1L;

	/**
	 * Builds a DBException.
	 * 
	 * @param message	Error message.
	 */
	public DBException(String message) {
		super(message);
	}

	/**
	 * <p>Builds a DBException.</p>
	 * 
	 * <p>
	 * 	<b>WARNING:</b> The query parameter is supposed to correspond to the ADQL query.
	 * 	You can set it to the SQL query but you must be aware that it may be displayed to the user.
	 * </p>
	 * 
	 * @param message	Error message.
	 * @param query		ADQL query (this string may be displayed to the user).
	 */
	public DBException(String message, String query) {
		super(message, query);
	}

	/**
	 * Builds a DBException.
	 * 
	 * @param cause	Cause of this error.
	 */
	public DBException(Throwable cause) {
		super(cause);
	}

	/**
	 * <p>Builds a DBException.</p>
	 * 
	 * <p>
	 * 	<b>WARNING:</b> The query parameter is supposed to correspond to the ADQL query.
	 * 	You can set it to the SQL query but you must be aware that it may be displayed to the user.
	 * </p>
	 * 
	 * @param cause	Cause of this error.
	 * @param query	ADQL query (this string may be displayed to the user).
	 */
	public DBException(Throwable cause, String query) {
		super(cause, query);
	}

	/**
	 * Builds a DBException
	 * 
	 * @param message	Error message.
	 * @param cause		Cause of this error.
	 */
	public DBException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * <p>Builds a DBException.</p>
	 * 
	 * <p>
	 * 	<b>WARNING:</b> The query parameter is supposed to correspond to the ADQL query.
	 * 	You can set it to the SQL query but you must be aware that it may be displayed to the user.
	 * </p>
	 * 
	 * @param message	Error message.
	 * @param cause		Cause of this error.
	 * @param query		ADQL query (this string may be displayed to the user).
	 */
	public DBException(String message, Throwable cause, String query) {
		super(message, cause, query);
	}

}
