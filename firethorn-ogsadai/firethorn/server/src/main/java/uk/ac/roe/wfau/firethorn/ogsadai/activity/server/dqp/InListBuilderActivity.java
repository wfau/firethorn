// Copyright (c) The University of Edinburgh, 2017.
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

//package uk.org.ogsadai.activity.transform;
package uk.ac.roe.wfau.firethorn.ogsadai.activity.server.dqp;

import java.util.ArrayList;
import java.util.List;

import uk.org.ogsadai.activity.ActivityProcessingException;
import uk.org.ogsadai.activity.ActivityTerminatedException;
import uk.org.ogsadai.activity.ActivityUserException;
import uk.org.ogsadai.activity.MatchedIterativeActivity;
import uk.org.ogsadai.activity.io.ActivityInput;
import uk.org.ogsadai.activity.io.BlockWriter;
import uk.org.ogsadai.activity.io.InvalidInputValueException;
import uk.org.ogsadai.activity.io.PipeClosedException;
import uk.org.ogsadai.activity.io.PipeIOException;
import uk.org.ogsadai.activity.io.PipeTerminatedException;
import uk.org.ogsadai.activity.io.TupleListActivityInput;
import uk.org.ogsadai.activity.io.TupleListIterator;
import uk.org.ogsadai.activity.io.TypedActivityInput;
import uk.org.ogsadai.tuple.ColumnNotFoundException;
import uk.org.ogsadai.tuple.Tuple;
import uk.org.ogsadai.tuple.TupleMetadata;

import uk.org.ogsadai.common.msgs.DAILogger;

/**
 * This would be better to extend StringReplaceActivity, but we need to modify
 * the inner class, which is declared private.
 * 
 * This activity accepts a string template and a list of tuples whose values are
 * inserted into the string template. For each input tuple an output string is
 * produced.
 * <p>
 * Activity inputs:
 * </p>
 * <ul>
 * <li> <code>data</code>. Type: OGSA-DAI list of
 * {@link uk.org.ogsadai.tuple.Tuple} with the first item in the list an
 * instance of {@link uk.org.ogsadai.metadata.MetadataWrapper} containing a
 * {@link uk.org.ogsadai.tuple.TupleMetadata} object. This is the input data set
 * which provides the values that are inserted into the string template. This is
 * a mandatory input.</li>
 * <li> <code>template</code>. Type: {@link java.lang.String}. This is the
 * template containing placeholders into which values from the input data set
 * are inserted. This is a mandatory input.</li>
 * </ul>
 * <p>
 * Activity outputs:
 * </p>
 * <ul>
 * <li> <code>result</code>. Type: {@link java.lang.String}. The output string
 * produced by inserting values into the template.</li>
 * </ul>
 * <p>
 * Configuration parameters: none.
 * </p>
 * <p>
 * Activity input/output ordering:
 * <ul>
 * <li>A string is read from the <code>template</code> input before the
 * <code>data</code> input is streamed through.</li>
 * </ul>
 * </p>
 * <p>
 * Activity contracts: none.
 * </p>
 * <p>
 * Target data resource: none.
 * </p>
 * <p>
 * Behaviour:
 * </p>
 * <ul>
 * <li>This activity replaces a placeholder string in the input template by
 * values from each tuple in the input data set.</li>
 * <li>The placeholder string in the template references the column whose values
 * are inserted into the string. The syntax of the replacement string is
 * <code>$REPLACE(column)</code> where <code>column</code> is the name of a
 * column in the tuple data set.</li>
 * <li>A {@link uk.org.ogsadai.tuple.ColumnNotFoundException} is raised if a
 * referenced column cannot be found in the metadata of the data input.</li>
 * <li>A {@link uk.org.ogsadai.activity.io.InvalidInputValueException} is raised
 * if the template string is malformed.
 * <li>
 * For example, (in all the examples we use '<code>{</code>' to denote the list
 * begin marker and '<code>}</code>' to denote the list end marker and
 * parentheses to denote OGSA-DAI tuples):
 * 
 * <pre>
 * data:     { metadata(name,value) ("A",1) ("B",2) }
 * template: { "This is $REPLACE(name) with value $REPLACE(value)." }
 * result:   "This is A with value 1" "This is B with value 2"
 * </pre>
 * 
 * <pre>
 * data:     { metadata(set) ("1, 2, 3, 4, 5") ("10, 11, 12, 13, 14") }
 * template: { "SELECT * FROM mytable WHERE col1 IN ($REPLACE(set))" }
 * result:   "SELECT * FROM mytable WHERE col1 IN (1, 2, 3, 4, 5)"
 *           "SELECT * FROM mytable WHERE col1 IN (10, 11, 12, 13, 14)"
 * </pre>
 * 
 * </li>
 * </ul>
 * 
 * @author The OGSA-DAI Project Team.
 */
public class InListBuilderActivity extends MatchedIterativeActivity
{
    /** Copyright statement. */
    private static final String COPYRIGHT_NOTICE =
        "Copyright (c) The University of Edinburgh, 2017.";

    /**
     * Debug logger.
     * 
     */
    private static final DAILogger LOG = DAILogger.getLogger(
        InListBuilderActivity.class
        );

    /** Activity input name for the template string. */
    public static final String INPUT_TEMPLATE = "template";
    /** Activity input name for input data. */
    public static final String INPUT_DATA = "data";
    /** Activity output name. */
    public static final String OUTPUT_RESULT = "result";
    
    /** Output block writer. */
    private BlockWriter mOutput;

    @Override
    protected ActivityInput[] getIterationInputs() 
    {
        return new ActivityInput[] {
                new TypedActivityInput(INPUT_TEMPLATE, String.class),
                new TupleListActivityInput(INPUT_DATA)
        };
    }
    
    @Override
    protected void preprocess() throws ActivityUserException,
            ActivityProcessingException, ActivityTerminatedException 
    {
        validateOutput(OUTPUT_RESULT);
        mOutput = getOutput();
    }
    
    @Override
    protected void processIteration(Object[] iterationData)
            throws ActivityProcessingException, ActivityTerminatedException,
            ActivityUserException
    {
        String template = (String)iterationData[0];
        TupleListIterator tuples = (TupleListIterator)iterationData[1];
        TupleMetadata metadata = (TupleMetadata) tuples.getMetadataWrapper().getMetadata();
        StringReplacer replacer = new StringReplacer(template, metadata);
        Tuple tuple;
        try
        {
            while ((tuple = (Tuple)tuples.nextValue()) != null)
            {
                String output = replacer.createString(tuple);
                mOutput.write(output);
            }
        }
        catch (PipeClosedException e) 
        {
            iterativeStageComplete();
        }
        catch (PipeIOException e) 
        {
            throw new ActivityProcessingException(e);
        }
        catch (PipeTerminatedException e) 
        {
            throw new ActivityTerminatedException();
        }
    }

    @Override
    protected void postprocess() throws ActivityUserException,
            ActivityProcessingException, ActivityTerminatedException 
    {
        // no post-processing
    }
    
    /**
     *
     */
    private static class StringReplacer
    {
        /** Copyright statement. */
        private static final String COPYRIGHT_NOTICE =
            "Copyright (c) The University of Edinburgh, 2017.";

        /** The fixed parts of the template. */
        private List<String> mFixed = new ArrayList<String>();
        /** Column numbers to fill in the placeholders. */
        private List<Integer> mColumns = new ArrayList<Integer>();
        /** Reserved word for the column placeholder. */
        private static final String REPLACE_BEGIN = "$REPLACE(";
        /** Reserved word for the column placeholder. */
        private static final String REPLACE_END = ")";

        /**
         * Constructor.
         * 
         * @param template
         *            string template
         * @param metadata
         *            tuple metadata of the input data
         * @throws ActivityUserException
         *             if a column cannot be found
         */
        public StringReplacer(String template, TupleMetadata metadata)
            throws ActivityUserException
        {
            int start = template.indexOf(REPLACE_BEGIN);
            int end = template.indexOf(REPLACE_END, start);
            int prevStart = 0;

//ZRQ
LOG.debug("ZRQ - StringReplacer(String, TupleMetadata)");
LOG.debug("ZRQ - template [" + template + "]");

            if (start > 0 && end < 0)
            {
                throw new InvalidInputValueException(INPUT_TEMPLATE, template);
            }
            
            while (start >= 0 && end >= 0)
            {
                String columnName =
                    template.substring(start + REPLACE_BEGIN.length(), end);
                int columnIndex = metadata.getColumnMetadataPosition(columnName);
                if (columnIndex == -1)
                {
                    throw new ActivityUserException(
                            new ColumnNotFoundException(columnName));
                }
                mColumns.add(columnIndex);
                mFixed.add(template.substring(prevStart, start));
                start = template.indexOf(REPLACE_BEGIN, end);
                prevStart = end + REPLACE_END.length();
                if (start >= 0)
                {
                    end = template.indexOf(REPLACE_END, start);
                    if (end < 0)
                    {
                        throw new InvalidInputValueException(
                                INPUT_TEMPLATE, template);
                    }
                }
            }
            
            mFixed.add(template.substring(prevStart));
        }
        
        /**
         * Inserts the column values into the placeholders and produces the
         * output string.
         * 
         * @param tuple
         *            data tuple
         * @return template string with tuple data
         */
        public String createString(Tuple tuple)
            {
            StringBuilder result = new StringBuilder();
            for (int index=0 ; index < mColumns.size(); index++)
                {
                String fixed = mFixed.get(index) ;
                String value = tuple.getString(mColumns.get(index));

                LOG.debug("ZRQ - column [" + index + "]");
                LOG.debug("ZRQ - fixed  [" + fixed + "]");
                LOG.debug("ZRQ - value  [" + value + "]");
            
                result.append(fixed);
                result.append(value);
                }
            result.append(mFixed.get(mFixed.size()-1));
            return result.toString();
            }

    }
}
