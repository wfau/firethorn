/**
 * 
 */
package uk.ac.roe.wfau.firethorn.blue;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import uk.ac.roe.wfau.firethorn.exception.FirethornCheckedException;

/**
 * Exception thrown when a internal server error occurs.
 *
 */
@SuppressWarnings("serial")
@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
public abstract class InternalServerErrorException
extends FirethornCheckedException {

	/**
	 * Public constructor.
	 * @param message
	 * 
	 */
	public InternalServerErrorException(final String message)
		{
		super(message);
		}

	/**
	 * Public constructor.
	 * @param cause
	 * 
	 */
	public InternalServerErrorException(final Throwable cause)
		{
		super(cause);
		}

	/**
	 * Public constructor.
	 * @param message
	 * @param cause
	 * 
	 */
	public InternalServerErrorException(final String message, final Throwable cause)
		{
		super(message, cause);
		}
	}
