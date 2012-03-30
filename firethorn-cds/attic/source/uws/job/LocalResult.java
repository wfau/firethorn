package uws.job;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.servlet.ServletOutputStream;

import javax.servlet.http.HttpServletResponse;

import uws.UWSException;
import uws.UWSToolBox;

/**
 * <p>UWS Result based on a local file.</p>
 * <p>
 * 	The result file is stored on the local machine.
 * 	The public URL to get the content of this file is the default one: {baseUWSUrl}/{jobList}/{jobId}/results/{resultId}.
 * 	Thus the true path file is hidden, but it is still possible to get the result content thanks to a public URL.
 * </p>
 * <p><i><u>Note:</u> Given the result file, the MIME type of the result is automatically extracted from the file name (file extension => MIME type ; see {@link UWSToolBox#getMimeType(String)}).</i></p>
 * 
 * @author Gr&eacute;gory Mantelet (CDS)
 * @version 06/2011
 */
public class LocalResult extends Result {
	private static final long serialVersionUID = 1L;

	/** The corresponding file. */
	protected final File file;

	/**
	 * Builds a result with its corresponding file.
	 * 
	 * @param job			The job which will contain this Result instance.
	 * @param resultFile	Result file.
	 * 
	 * @see Result#Result(String)
	 */
	public LocalResult(AbstractJob job, File resultFile){
		super(DEFAULT_RESULT_NAME);
		file = resultFile;
		if (file != null && file.getName() != null){
			// Set the ID = file name:
			id = file.getName();
			// Normalize the id: the file name without its extension:
			int indExt = id.indexOf('.');
			if (indExt > 0)
				id = id.substring(0, indExt);
		}
		href = getDefaultUrl(id, job);

		String fileName = resultFile.getName();
		mimeType = UWSToolBox.getMimeType(fileName.substring(fileName.lastIndexOf('.')));
	}

	/**
	 * Builds a result with an ID/name and the file which contains the results.
	 * 
	 * @param job			Job which will contain this Result instance.
	 * @param name			Name or ID of the result.
	 * @param resultFile	Result file.
	 * 
	 * @see Result#Result()
	 */
	public LocalResult(AbstractJob job, String name, File resultFile){
		file = resultFile;
		href = getDefaultUrl(id, job);

		String fileName = resultFile.getName();
		mimeType = UWSToolBox.getMimeType(fileName.substring(fileName.lastIndexOf('.')));
	}

	/**
	 * Builds a result with an ID/name, a result type and the file which contains the results.
	 * 
	 * @param name			Name or ID or the result.
	 * @param resultType	Type of result.
	 * @param resultFile	Result file.
	 * @param job			Job which will contain this Result instance.
	 * 
	 * @see #LocalResult(AbstractJob, String, String, File)
	 */
	public LocalResult(AbstractJob job, String name, String resultType, File resultFile){
		this(job, name, resultFile);
		type = resultType;
	}

	/**
	 * Tells whether the result file is known or not.
	 * 
	 * @return	<i>true</i> if its corresponding file is known, <i>false</i> otherwise.
	 */
	public final boolean hasFile(){
		return file != null;
	}

	/**
	 * Gets the corresponding file, if any.
	 * 
	 * @return Its corresponding file.
	 */
	public final File getFile() {
		return file;
	}

	@Override
	public void writeContent(HttpServletResponse response) throws UWSException, IOException {
		if (!hasFile())
			throw new UWSException(UWSException.NO_CONTENT, "[Get result] Result content can not be found !");

		BufferedInputStream input = null;
		ServletOutputStream output = null;
		try {
			// Get an input on the file to display:
			input = new BufferedInputStream(new FileInputStream(getFile()));

			// Set the HTTP content type:
			if (mimeType != null)
				response.setContentType(mimeType);

			// Write the file into the HTTP response:
			output = response.getOutputStream();
			byte[] buffer = new byte[255];
			int length;
			while((length=input.read(buffer))>0)
				output.print(new String(buffer, 0, length));
		} catch (FileNotFoundException e) {
			throw new UWSException(UWSException.NOT_FOUND, "[Get result] Impossible to get the result \""+getId()+"\": file not found !");
		} catch (IOException e) {
			throw new UWSException(UWSException.INTERNAL_SERVER_ERROR, "[Get result] Impossible to read the content of the result \""+getId()+"\" or to write it in the HTTP output stream !");
		}finally{
			if (input != null)
				input.close();
			if (output != null)
				output.flush();
		}
	}

}
