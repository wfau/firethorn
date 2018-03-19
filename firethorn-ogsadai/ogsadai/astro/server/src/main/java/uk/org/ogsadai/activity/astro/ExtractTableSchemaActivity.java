// Copyright (c) The University of Edinburgh,  2010.
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

package uk.org.ogsadai.activity.astro;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Collection;
import java.util.Iterator;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.FactoryConfigurationError;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import uk.org.ogsadai.activity.ActivityProcessingException;
import uk.org.ogsadai.activity.ActivityTerminatedException;
import uk.org.ogsadai.activity.ActivityUserException;
import uk.org.ogsadai.activity.MatchedIterativeActivity;
import uk.org.ogsadai.activity.extension.ActivityInitialisationException;
import uk.org.ogsadai.activity.extension.ConfigurableActivity;
import uk.org.ogsadai.activity.extension.ResourceActivity;
import uk.org.ogsadai.activity.io.ActivityInput;
import uk.org.ogsadai.activity.io.ActivityPipeProcessingException;
import uk.org.ogsadai.activity.io.BlockWriter;
import uk.org.ogsadai.activity.io.ControlBlock;
import uk.org.ogsadai.activity.io.PipeClosedException;
import uk.org.ogsadai.activity.io.PipeIOException;
import uk.org.ogsadai.activity.io.PipeTerminatedException;
import uk.org.ogsadai.activity.io.TypedActivityInput;
import uk.org.ogsadai.config.ConfigurationValueMissingException;
import uk.org.ogsadai.config.Key;
import uk.org.ogsadai.config.KeyValueProperties;
import uk.org.ogsadai.config.KeyValueUnknownException;
import uk.org.ogsadai.converters.databaseschema.DatabaseSchemaMetaData;
import uk.org.ogsadai.converters.databaseschema.RelationalSchemaParseException;
import uk.org.ogsadai.converters.databaseschema.TableMetaData;
import uk.org.ogsadai.converters.databaseschema.fromxml.XMLSchemaConverter;
import uk.org.ogsadai.resource.ResourceAccessor;
import uk.org.ogsadai.resource.generic.GenericResourceAccessor;
import uk.org.ogsadai.util.xml.SimpleErrorHandler;
import uk.org.ogsadai.util.xml.XML;

/**
 * This activity returns schema information from a file. This may be important 
 * in cases where the resource does not provide the schema or it's not 
 * straightforward to do so. The class has been written for the ROE project 
 * where the resource is a TAP resources. There is a way to actually obtain 
 * schema information from a TAP service but it was quicker to write schema
 * information by hand rather than programming against the TAP protocol.
 * 
 * @author The OGSA-DAI Project Team.
 *
 */
public class ExtractTableSchemaActivity
    extends MatchedIterativeActivity
    implements ResourceActivity, ConfigurableActivity {

    private static final Logger LOG =
        Logger.getLogger(ExtractTableSchemaActivity.class);
    
    /** Activity input name - table name pattern. */
    public static final String INPUT_TABLE_NAME = "name";
    
    /** 
     * Activity configuration key used to specify the table types
     * to extract.
     */
    public static final Key TABLE_TYPES = new Key("dai.table.types");
    
    /**
     * Activity configuration key for metadata file to read the schema from.
     */
    public static final Key METADATA_FILE = new Key("dai.astro.metadatafile");
    
    /** Activity output name - <code>TableMetaData</code>. */
    public static final String OUTPUT_DATA = "data";
    
    /** Output block writer. */
    private BlockWriter mOutput; 
    
    /** The resource this activity is targeted at. */
    private ResourceAccessor mResource;
    
    /** Types of table to get. */
    private String[] mTableTypes = null;
    
    /** Resource settings. */
    private KeyValueProperties mSettings;
    
    /**
     * {@inheritDoc}
     */
    public Class<GenericResourceAccessor> getTargetResourceAccessorClass() {
        return GenericResourceAccessor.class;
    }

    /**
     * {@inheritDoc}
     */
    public void setTargetResourceAccessor(ResourceAccessor resourceAccessor) {
        mResource = resourceAccessor;
    }

    /**
     * {@inheritDoc}
     */
    public void configureActivity(KeyValueProperties properties)
            throws ActivityInitialisationException {
        try {  
            String tableTypesString = (String)properties.get(TABLE_TYPES);
            mTableTypes = tableTypesString.split(",");
        }
        catch (KeyValueUnknownException e) {
            mTableTypes = null;
        }
    }
    
    /**
     * {@inheritDoc}
     */
    protected void preprocess() throws ActivityUserException,
            ActivityProcessingException, ActivityTerminatedException {
        validateOutput(OUTPUT_DATA);
        mOutput = getOutput();     
        mSettings = mResource.getResource().getState().getConfiguration();
    }

    /**
     * {@inheritDoc}
     */
    protected void processIteration(Object[] iterationData)
            throws ActivityProcessingException, ActivityTerminatedException,
            ActivityUserException {
        String tableNamePattern = (String) iterationData[0];
        if (!tableNamePattern.equals("%")) {
            Exception e =
                new IllegalArgumentException("This activity can only handle " +
            		"the table name pattern %. What is passed is " + 
            		tableNamePattern);
            throw new ActivityProcessingException(e);
        }
        // Get logical schema file
        String filePath = null;
        if (mSettings.containsKey(METADATA_FILE)) {
            filePath = (String)mSettings.get(METADATA_FILE);
        }else {
            throw new 
                ActivityUserException(
                    new ConfigurationValueMissingException(METADATA_FILE));
        }

        try {
            validateSchema(filePath);
            Document schema = XML.fileToDocument(filePath);
            DatabaseSchemaMetaData dbSchema = 
                XMLSchemaConverter.convert(schema.getDocumentElement());
            Collection tableMetadataCollection = dbSchema.getTables().values();
            TableMetaData[] tableMetaDataArray = 
                new TableMetaData[tableMetadataCollection.size()];
            mOutput.write(ControlBlock.LIST_BEGIN);
            for(Iterator it = tableMetadataCollection.iterator(); it.hasNext();)
            {
                mOutput.write((TableMetaData)it.next());
            }
            mOutput.write(ControlBlock.LIST_END);
        }catch(FileNotFoundException e) {
            throw new ActivityUserException(e);
        }catch(RelationalSchemaParseException e) {
            throw new ActivityUserException(e);
        }catch (PipeIOException e) {
            throw new ActivityPipeProcessingException(e);
        }catch (PipeTerminatedException e) {
            throw new ActivityTerminatedException();
        }catch (PipeClosedException e) {
            // No more output wanted, so finish early.
            iterativeStageComplete();
        }
    }
    
    private void validateSchema(String schema) 
        throws ActivityUserException, ActivityProcessingException
    {
        LOG.debug("Looking for resource 'TableSchema.xsd'.");
        InputStream schemaStream = 
            Thread.currentThread().getContextClassLoader().getResourceAsStream(
                "TableSchema.xsd");
        if (schemaStream == null)
        {
            LOG.debug("Could not find XML schema for validation.");
        }
        else 
        {
            LOG.debug("Validating table schema XML at " + schema);
            try 
            {
                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                factory.setValidating(true);
                factory.setNamespaceAware(true);
                factory.setAttribute(
                    "http://java.sun.com/xml/jaxp/properties/schemaLanguage", 
                    "http://www.w3.org/2001/XMLSchema");
                factory.setAttribute(
                    "http://java.sun.com/xml/jaxp/properties/schemaSource",
                    new InputSource(schemaStream));
                DocumentBuilder parser = factory.newDocumentBuilder();
                parser.setErrorHandler(new SimpleErrorHandler());
                InputSource source = new InputSource(new FileInputStream(schema));
                parser.parse(source);
                LOG.debug("Table schema is valid.");
            }
            // SAXException, IOException, ParserConfigurationException
            catch (Exception e) 
            {
                throw new ActivityProcessingException(
                        new TableSchemaValidationException(e));
            } 
            catch (FactoryConfigurationError e)
            {
                throw new ActivityProcessingException(
                        new TableSchemaValidationException(e));
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    protected void postprocess() throws ActivityUserException,
        ActivityProcessingException, ActivityTerminatedException {
        // Nothing to do
    }
   
    /**
     * {@inheritDoc}
     */
    protected ActivityInput[] getIterationInputs() {
        return new ActivityInput[]{
            new TypedActivityInput(INPUT_TABLE_NAME, String.class)};
    }
}
