/**
 * 
 */
package uk.ac.roe.wfau.firethorn.exception;

/**
 * An exception thrown to indicate something we haven't implemented yet.
 *
 */
public class NotImplementedException
extends FirethornUncheckedException
	{

	/**
	 * Auto generated serial version UID. 
	 * 
	 */
	private static final long serialVersionUID = -8142207838330839873L;

	/**
	 * Default mesage, {@value}.
	 *
	 */
	protected static final String DEFAULT_MESSAGE = "Functonality implemented yet"; 

	/**
	 * Public constructor, using the default message.
	 *
	 */
	public NotImplementedException()
		{
		this(DEFAULT_MESSAGE);
		}

	/**
	 * Public constructor.
	 *
	 */
	public NotImplementedException(String message)
		{
		super(message);
		}
	}
