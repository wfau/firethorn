// Copyright (c) The University of Edinburgh, 2009.
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

package uk.org.ogsadai.dqp.lqp.optimiser.visualise;

import java.io.PrintWriter;
import java.io.StringWriter;

import uk.org.ogsadai.common.msgs.DAILogger;
import uk.org.ogsadai.dqp.common.CompilerConfiguration;
import uk.org.ogsadai.dqp.common.RequestDetails;
import uk.org.ogsadai.dqp.lqp.Operator;
import uk.org.ogsadai.dqp.lqp.exceptions.LQPException;
import uk.org.ogsadai.dqp.lqp.optimiser.Optimiser;
import uk.org.ogsadai.dqp.lqp.util.DOTFileGenerator;
import uk.org.ogsadai.resource.dataresource.dqp.RequestDQPFederation;

/**
 * Optimiser that writes out the current query plan to file.  It does not
 * actually do any optimisation.  The optimiser requires come configuration
 * properties:
 * <ul>
 *   <li><tt>dotFilename</tt>: specifies the filename of file to which the
 *       query plan is written in dot format.  In the value any occurrences of 
 *       <tt>REQUESTID</tt> are replaced with the request ID and any occurrences
 *       of <tt>UUID</tt> are replaced with a UUID that is unique to this
 *       invocation of the optimiser.</li>
 *   <li><tt>command</tt>: specifies command line command to be executed after
 *       the dot file has been written.  This is an optional parameter, if
 *       not specified no command is executed.  In the value any occurrences of 
 *       <tt>REQUESTID</tt> are replaced with the request ID and any occurrencesT
 *       of <tt>UUID</tt> are replaced with a UUID that is unique to this
 *       invocation of the optimiser.</li>
 *    <li><tt>writeHeading</tt>: specifies whether or not the operator
 *       heading should be drawn. The default value is <tt>true</tt>, change to
 *       <tt>false</tt> to not write the heading.
 * </ul>
 * <p>
 * Example configuration:
 * <pre>
 * &lt;bean class="uk.org.ogsadai.dqp.lqp.optimiser.visualise.VisualiseOptimiser"&gt;
 *   &lt;property name="writeHeading" value="false"/&gt;
 *   &lt;property name="dotFilename" value="/path/REQUESTID.dot"/&gt;
 *   &lt;property name="command" value="/path/dot.exe -Tpng -q /path/REQUESTID.dot -o /path/REQUESTID.png"/&gt;
 * &lt;/bean&gt;
 * </pre>
 *
 * @author The OGSA-DAI Project Team
 */
public class VisualiseOptimiser implements Optimiser
{
    /** Copyright notice. */
    private static final String COPYRIGHT_NOTICE = 
        "Copyright (c) The University of Edinburgh, 2009";

    /** Logger for this class. */
    private static final DAILogger LOG = 
        DAILogger.getLogger(VisualiseOptimiser.class);

    /** Command key. */
    private static final String COMMAND_KEY = "command";
    /** Dot filename key. */
    private static final String FILENAME_KEY = "dotFilename";
    /** Write heading key. */
    private static final String WRITEHEADING_KEY = "writeHeading";

    /** Command template. */
    private String mCommandTemplate = null;
    
    /** Dot filename template. */
    private String mDotFilenameTemplate = null;
    
    /** Write heading to dot file or not. */
    private boolean mWriteHeading = true;
    
    /**
     * {@inheritDoc}
     */
    public void addProperty(String key, String value)
    {
        if (COMMAND_KEY.equals(key))
        {
            mCommandTemplate = value;
        }
        else if (FILENAME_KEY.equals(key))
        {
            mDotFilenameTemplate = value;
        }else if (WRITEHEADING_KEY.equals(key))
        {
            mWriteHeading = Boolean.parseBoolean(value);
        }
    }

    /**
     * {@inheritDoc}
     */
    public Operator optimise(
        Operator lqpRoot,
        RequestDQPFederation requestFederation,
        CompilerConfiguration compilerConfiguration,
        RequestDetails requestDetails) 
        throws LQPException
    {
        try
        {
            DOTFileGenerator dotFileGenerator = new DOTFileGenerator();
            
            if (mDotFilenameTemplate != null && mCommandTemplate == null)
            {
                dotFileGenerator.writeDotToFile(
                    lqpRoot, 
                    mWriteHeading, 
                    requestDetails.getRequestResource().getResourceID(),
                    mDotFilenameTemplate);
            }
            else if (mDotFilenameTemplate != null && mCommandTemplate != null)
            {
                
                dotFileGenerator.writeDotToFileAndRunCommand(
                    lqpRoot, 
                    mWriteHeading, 
                    requestDetails.getRequestResource().getResourceID(),
                    mDotFilenameTemplate,
                    mCommandTemplate);
            }
        }
        catch(Throwable t)
        {
            // Log the error to the debug log.
            if (LOG.isDebugEnabled())
            {
                StringWriter sw = new StringWriter();
                t.printStackTrace(new PrintWriter(sw));
                LOG.debug(sw.toString());
            }
        }
        
        return lqpRoot;
    }
    
    /**
     * Sets whether the heading of each operator should be drawn.
     * 
     * @param writeHeading <tt>true</tt> if the heading is to be drawn, 
     *        <tt>false</tt> otherwise.
     */
    public void setWriteHeading(boolean writeHeading)
    {
        mWriteHeading = writeHeading;
    }
    
    /**
     * Sets the command template for the optional command that can be run after
     * the dot file have been produced. Occurrences of the string 
     * <tt>REQUESTID</tt> will be replaced with the request ID.
     * 
     * @param command command template
     */
    public void setCommand(String command)
    {
        mCommandTemplate = command;
    }
    
    /**
     * Sets the name of the output dot file. Occurrences of the string 
     * <tt>REQUESTID</tt> will be replaced with the request ID.
     * 
     * @param filename the filename of the dot file
     */
    public void setDotFilename(String filename)
    {
        mDotFilenameTemplate = filename;
    }
}
