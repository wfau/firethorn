package uk.ac.roe.wfau.firethorn.webapp.tap;

import uk.ac.roe.wfau.firethorn.webapp.tap.TapError;
import uk.ac.roe.wfau.firethorn.webapp.tap.TapJobParams;
import uk.ac.roe.wfau.firethorn.webapp.tap.TapJobErrors;

public class TapValidator {
	
	 	
	private String request;
	private String lang;
	private String query;
	private String format;
	private String version;
	private String maxrec;
	private String errorMessage;
	
	
	/**
	 * TapValidator Constructor
	 * @param request
	 * @param lang
	 * @param query
	 * @param format
	 * @param version
	 * @param maxrec
	 */
	public TapValidator(String request, String lang, String query, String format, String version, String maxrec) {
		this.request = request;
		this.lang = lang;
		this.query = query;
		this.format = format;
		this.version = version;
		this.maxrec = maxrec;
		this.errorMessage="";
	}

	public String getRequest() {
		return request;
	}

	public void setRequest(String request) {
		this.request = request;
	}

	public String getLang() {
		return lang;
	}

	public void setLang(String lang) {
		this.lang = lang;
	}

	public String getQuery() {
		return query;
	}

	public void setQuery(String query) {
		this.query = query;
	}

	public String getFormat() {
		return format;
	}

	public void setFormat(String format) {
		this.format = format;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getMaxrec() {
		return maxrec;
	}

	public void setMaxrec(String maxrec) {
		this.maxrec = maxrec;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	/**
	 * Check the parameters for a TAP job
	 * @param REQUEST
	 * @param LANG
	 * @param QUERY
	 * @param FORMAT
	 * @param VERSION
	 * @return boolean (Valid or not)
	 */
	public boolean checkParams() {

		String error_message;
		boolean valid = true;

		// Check for errors and return appropriate VOTable error messages
		if (getRequest() == null) {
			setErrorMessage(TapError.writeErrorToVotable(TapJobErrors.PARAM_REQUEST_MISSING));
			valid = false;
			return valid;
		} else if (!getRequest().equalsIgnoreCase(TapJobParams.REQUEST_DO_QUERY)
				&& !getRequest().equalsIgnoreCase(TapJobParams.REQUEST_GET_CAPABILITIES)) {
			error_message = "Invalid REQUEST: " + getRequest();
			setErrorMessage(TapError.writeErrorToVotable(error_message));
			valid = false;
			return valid;
		}

		if (getLang() == null && !getRequest().equalsIgnoreCase(TapJobParams.REQUEST_GET_CAPABILITIES)) {
			setErrorMessage(TapError.writeErrorToVotable(TapJobErrors.PARAM_LANGUAGE_MISSING));
			valid = false;
			return valid;
		} else if (getLang()  != null) {
			if (!getLang() .equalsIgnoreCase("ADQL") && 
					!getLang() .equalsIgnoreCase("ADQL-2.0")  && 
					!getLang() .equalsIgnoreCase("ADQL-1.0") && 
					!getLang() .equalsIgnoreCase("PQL")) {
				error_message = "Invalid LANGUAGE: " + getLang() ;
				setErrorMessage(TapError.writeErrorToVotable(error_message));
				valid = false;
			}
		}

		if (getQuery()  == null && !getRequest().equalsIgnoreCase(TapJobParams.REQUEST_GET_CAPABILITIES)) {
			setErrorMessage(TapError.writeErrorToVotable(TapJobErrors.PARAM_QUERY_MISSING));
			valid = false;
			return valid;
		}

		if (getFormat() != null) {
			if (!getFormat().equalsIgnoreCase("votable")) {
				error_message = "FORMAT '" + getFormat() + "'not supported" ;
				setErrorMessage(TapError.writeErrorToVotable(error_message));
				valid = false;
			}
		}
		
		if (getVersion() != null) {
			if (!getVersion().equalsIgnoreCase("1.0") || !getVersion().equalsIgnoreCase("1")) {
				error_message = "VERSION '" + getVersion() + "'not supported" ;
				setErrorMessage(TapError.writeErrorToVotable(error_message));
				valid = false;
			}
		}
		
		return valid;

	}
				 
		 
}
