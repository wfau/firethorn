package tap.upload;

import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidParameterException;

import com.oreilly.servlet.multipart.ExceededSizeException;

public final class LimitedSizeInputStream extends InputStream {

	private final InputStream input;
	private final long sizeLimit;

	private long counter = 0;

	private boolean exceed = false;


	public LimitedSizeInputStream(final InputStream stream, final long sizeLimit) throws NullPointerException {
		if (stream == null)
			throw new NullPointerException("The given input stream is NULL !");
		input = stream;

		if (sizeLimit <= 0)
			throw new InvalidParameterException("The size limit must be a positive number of bytes !");
		this.sizeLimit = sizeLimit;
	}

	private void updateCounter(final long nbReads) throws ExceededSizeException {
		if (nbReads > 0){
			counter += nbReads;
			if (counter > sizeLimit){
				exceed = true;
				throw new ExceededSizeException();
			}
		}
	}

	public final boolean sizeExceeded(){
		return exceed;
	}

	@Override
	public int read() throws IOException {
		int read = input.read();
		updateCounter(1);
		return read;
	}

	@Override
	public int read(byte[] b) throws IOException {
		int nbRead = input.read(b);
		updateCounter(nbRead);
		return nbRead;
	}

	@Override
	public int read(byte[] b, int off, int len) throws IOException {
		int nbRead = input.read(b, off, len);
		updateCounter(nbRead);
		return nbRead;
	}

	@Override
	public long skip(long n) throws IOException {
		long nbSkipped = input.skip(n);
		updateCounter(nbSkipped);
		return nbSkipped;
	}

	@Override
	public int available() throws IOException {
		return input.available();
	}

	@Override
	public void close() throws IOException {
		input.close();
	}

	@Override
	public synchronized void mark(int readlimit) throws UnsupportedOperationException {
		throw new UnsupportedOperationException("mark() not supported in a LimitedSizeInputStream !");
	}

	@Override
	public boolean markSupported() {
		return false;
	}

	@Override
	public synchronized void reset() throws IOException, UnsupportedOperationException {
		throw new UnsupportedOperationException("mark() not supported in a LimitedSizeInputStream !");
	}

}
