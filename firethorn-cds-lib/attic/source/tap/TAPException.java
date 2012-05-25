package tap;

public class TAPException extends Exception {

	private static final long serialVersionUID = 1L;

	private String adqlQuery = null;
	private ExecutionProgression executionStatus = null;

	public TAPException(String message) {
		super(message);
	}

	public TAPException(String message, String query) {
		super(message);
		adqlQuery = query;
	}

	public TAPException(String message, String query, ExecutionProgression status) {
		this(message, query);
		executionStatus = status;
	}

	public TAPException(Throwable cause) {
		super(cause);
	}

	public TAPException(Throwable cause, String query) {
		super(cause);
		adqlQuery = query;
	}

	public TAPException(Throwable cause, String query, ExecutionProgression status) {
		this(cause, query);
		executionStatus = status;
	}

	public TAPException(String message, Throwable cause) {
		super(message, cause);
	}

	public TAPException(String message, Throwable cause, String query) {
		super(message, cause);
		adqlQuery = query;
	}

	public TAPException(String message, Throwable cause, String query, ExecutionProgression status) {
		this(message, cause, query);
		executionStatus = status;
	}

	public String getQuery(){
		return adqlQuery;
	}

	public ExecutionProgression getExecutionStatus(){
		return executionStatus;
	}

}
