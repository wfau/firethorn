/**
 * 
 */
package uk.ac.roe.wfau.firethorn.blue;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import uk.ac.roe.wfau.firethorn.exception.FirethornCheckedException;

/**
 * Exception thrown in response to an invalid request.
 *
 */
@SuppressWarnings("serial")
@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public abstract class InvalidRequestException
extends FirethornCheckedException {

	/**
	 * Public constructor.
	 * @param message
	 * 
	 */
	public InvalidRequestException(final String message)
		{
		super(message);
		}

	/**
	 * Public constructor.
	 * @param cause
	 * 
	 */
	public InvalidRequestException(final Throwable cause)
		{
		super(cause);
		}

	/**
	 * Public constructor.
	 * @param message
	 * @param cause
	 * 
	 */
	public InvalidRequestException(final String message, final Throwable cause)
		{
		super(message, cause);
		}
	}
