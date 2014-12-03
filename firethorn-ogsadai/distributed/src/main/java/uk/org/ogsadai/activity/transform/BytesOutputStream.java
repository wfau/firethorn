// Copyright (c) The University of Edinburgh,  2009-2011.
//
// LICENCE-START
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software 
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
// LICENCE-END

package uk.org.ogsadai.activity.transform;

import java.io.IOException;
import java.io.OutputStream;

import uk.org.ogsadai.activity.io.BlockWriter;
import uk.org.ogsadai.activity.io.PipeClosedException;
import uk.org.ogsadai.activity.io.PipeIOException;
import uk.org.ogsadai.activity.io.PipeTerminatedException;

/**
 * An output stream that writes fixed-size arrays to an OGSA-DAI block writer.
 *
 * @author The OGSA-DAI Project Team.
 */
public class BytesOutputStream extends OutputStream
{
    /** Copyright statement. */
    private static final String COPYRIGHT_NOTICE = 
        "Copyright (c) The University of Edinburgh, 2009-2011";

    /** Fixed size byte buffer. */
    private byte[] mBuffer;
    /** Size of buffer. */
    private final int mSize;
    /** Number of valid bytes in the buffer. */
    private int mCount;
    /** Output block writer. */
    private final BlockWriter mOutput;
    
    /**
     * Constructs a new output stream which writes arrays of a fixed size to the
     * block writer.
     * 
     * @param writer
     *            block writer to which byte arrays are written
     * @param size
     *            size of byte arrays
     */
    public BytesOutputStream(BlockWriter writer, int size)
    {
        mOutput = writer;
        mSize = size;
        mBuffer = new byte[mSize];
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized void write(int b)  throws IOWrapperException
    {
        if (mCount + 1 > mSize) 
        {
            flush();
        }
        mBuffer[mCount] = (byte)b;
        mCount++;
    }
    
    @Override
    public void write(byte[] bytes) throws IOException 
    {
        write(bytes, 0, bytes.length);
//        int toWrite = bytes.length;
//        int length = mSize - mCount; 
//        int position = 0;
//        while (toWrite > length)
//        {
//            System.arraycopy(bytes, position, mBuffer, mCount, length);
//            mCount += length;
//            flush();
//            position += length;
//            toWrite -= length;
//            length = mSize - mCount; 
//        }
//        
//        if (toWrite != 0)
//        {
//            System.arraycopy(bytes, position, mBuffer, mCount, toWrite);
//            mCount += toWrite;
//        }
    }
    
    @Override
    public void write(byte[] bytes, int off, int len) throws IOException 
    {
        int toWrite = len;
        int length = mSize - mCount; 
        int position = off;
        while (toWrite > length)
        {
            System.arraycopy(bytes, position, mBuffer, mCount, length);
            mCount += length;
            flush();
            position += length;
            toWrite -= length;
            length = mSize - mCount; 
        }
        
        if (toWrite != 0)
        {
            System.arraycopy(bytes, position, mBuffer, mCount, toWrite);
            mCount += toWrite;
        }
    }
    
    @Override
    public void flush() throws IOWrapperException
    {
        if (mCount > 0)
        {
            if (mCount < mSize)
            {
                byte[] newbuffer = new byte[mCount];
                System.arraycopy(mBuffer, 0, newbuffer, 0, mCount);
                mBuffer = newbuffer;
            }
            writeToOutput();
            mBuffer = new byte[mSize];
            mCount = 0;
        }
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized void close() throws IOException
    {
        flush();
        mBuffer = null;
    }

    /**
     * Writes a block to the output stream and throws the appropriate exceptions
     * if any occur.
     * 
     * @throws IOWrapperException
     *             a wrapped pipe exception
     */
    private void writeToOutput() throws IOWrapperException
    {
        try
        {
            mOutput.write(mBuffer);
        }
        catch (PipeClosedException e)
        {
            throw new IOWrapperException(e);
        }
        catch (PipeIOException e)
        {
            throw new IOWrapperException(e);
        }
        catch (PipeTerminatedException e)
        {
            throw new IOWrapperException(e);
        }

    }

    /**
     * IOException wrapper.
     *
     * @author The OGSA-DAI Project Team.
     */
    static class IOWrapperException extends IOException
    {
        /** Copyright statement. */
        private static final String COPYRIGHT_NOTICE = 
            "Copyright (c) The University of Edinburgh, 2009";

        /**
         * Constructs a new wrapped exception.
         * 
         * @param cause
         *            cause of the exception
         */
        public IOWrapperException(Throwable cause)
        {
            super.initCause(cause);
        }
    }


}
