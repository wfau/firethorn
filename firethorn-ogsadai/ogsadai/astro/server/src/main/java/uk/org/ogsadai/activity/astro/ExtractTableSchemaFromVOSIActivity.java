package uk.org.ogsadai.activity.astro;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collection;
import java.util.Iterator;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import uk.org.ogsadai.activity.ActivityProcessingException;
import uk.org.ogsadai.activity.ActivityTerminatedException;
import uk.org.ogsadai.activity.ActivityUserException;
import uk.org.ogsadai.activity.MatchedIterativeActivity;
import uk.org.ogsadai.activity.extension.ResourceActivity;
import uk.org.ogsadai.activity.io.ActivityInput;
import uk.org.ogsadai.activity.io.BlockWriter;
import uk.org.ogsadai.activity.io.ControlBlock;
import uk.org.ogsadai.activity.io.PipeClosedException;
import uk.org.ogsadai.activity.io.PipeIOException;
import uk.org.ogsadai.activity.io.PipeTerminatedException;
import uk.org.ogsadai.activity.io.TypedActivityInput;
import uk.org.ogsadai.config.Key;
import uk.org.ogsadai.config.KeyValueProperties;
import uk.org.ogsadai.config.KeyValueUnknownException;
import uk.org.ogsadai.converters.databaseschema.DatabaseSchemaMetaData;
import uk.org.ogsadai.converters.databaseschema.RelationalSchemaParseException;
import uk.org.ogsadai.converters.databaseschema.TableMetaData;
import uk.org.ogsadai.converters.databaseschema.fromxml.XMLSchemaConverter;
import uk.org.ogsadai.exception.ErrorID;
import uk.org.ogsadai.resource.ResourceAccessor;
import uk.org.ogsadai.resource.generic.GenericResourceAccessor;
import uk.org.ogsadai.util.xml.XML;

public class ExtractTableSchemaFromVOSIActivity 
    extends MatchedIterativeActivity 
    implements ResourceActivity
{
    
    public static final Key TABLE_METADATA_URL_KEY = new Key("dai.astro.metadata.url");
    
    /** Activity input name - table name pattern. */
    public static final String INPUT_TABLE_NAME = "name";
    
    /** Activity output name - <code>TableMetaData</code>. */
    public static final String OUTPUT_DATA = "data";
    
    /** Output block writer. */
    private BlockWriter mOutput; 

    /** Generic resource. */
    private ResourceAccessor mResource;

    /** URL of the metadata document. */
    private String mMetadataURL;

    @Override
    protected ActivityInput[] getIterationInputs() 
    {
        return new ActivityInput[] {
                new TypedActivityInput(INPUT_TABLE_NAME, String.class)
        };
    }

    @Override
    protected void preprocess() throws ActivityUserException,
            ActivityProcessingException, ActivityTerminatedException
    {
        validateOutput(OUTPUT_DATA);
        mOutput = getOutput();
        KeyValueProperties config = 
            mResource.getResource().getState().getConfiguration();
        try
        {
            mMetadataURL = (String)config.get(TABLE_METADATA_URL_KEY);
        }
        catch (KeyValueUnknownException e)
        {
            throw new ActivityProcessingException(e);
        }
    }

    @Override
    protected void processIteration(Object[] iterationData)
            throws ActivityProcessingException, ActivityTerminatedException,
            ActivityUserException 
    {
        String tableNamePattern = (String)iterationData[0];
        if (!"%".equals(tableNamePattern))
        {
            throw new ActivityUserException(new IllegalArgumentException(
                    "This activity can only handle " +
                    "the table name pattern %. What is passed is " + 
                    tableNamePattern));
        }

        final InputStream metadata;
        final InputStream xslt =
            getClass().getClassLoader().getResourceAsStream(
                    "vosi-to-ogsadai.xsl");
        try
        {
            metadata = new URL(mMetadataURL).openStream();
        }
        catch (MalformedURLException e) 
        {
            throw new ActivityProcessingException(e);
        }
        catch (IOException e) 
        {
            throw new ActivityProcessingException(e);
        }
        
        final Transformer transformer;
        try
        {
            TransformerFactory factory = TransformerFactory.newInstance();
            transformer = factory.newTransformer(new StreamSource(xslt));
        }
        catch (TransformerConfigurationException e) 
        {
            throw new ActivityProcessingException(
                    ErrorID.XML_TRANSFORMER_CONFIGURATION_ERROR, e);
        }
        catch (TransformerFactoryConfigurationError e)
        {
            throw new ActivityProcessingException(
                    ErrorID.XML_TRANSFORMER_CONFIGURATION_ERROR, e);
        } 

        final StringWriter result;
        try
        {
            result = new StringWriter();
            transformer.transform(
                    new StreamSource(metadata), new StreamResult(result));
        }
        catch (TransformerException e) 
        {
            throw new ActivityProcessingException(
                    ErrorID.XML_TRANSFORMATION_ERROR, e);
        } 
        try
        {
            StringReader reader = new StringReader(result.toString());
            Document schema = XML.toDocument(new InputSource(reader));
            DatabaseSchemaMetaData dbSchema = 
                XMLSchemaConverter.convert(schema.getDocumentElement());
            Collection<?> tableMetadataCollection = dbSchema.getTables().values();
            mOutput.write(ControlBlock.LIST_BEGIN);
            for(Iterator<?> it = tableMetadataCollection.iterator(); it.hasNext();)
            {
                mOutput.write((TableMetaData)it.next());
            }
            mOutput.write(ControlBlock.LIST_END);
        }
        catch (RelationalSchemaParseException e) 
        {
            throw new ActivityUserException(e);
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

    @Override
    public void setTargetResourceAccessor(ResourceAccessor resourceAccessor)
    {
        mResource = resourceAccessor;
    }

    @Override
    public Class<GenericResourceAccessor> getTargetResourceAccessorClass() 
    {
        return GenericResourceAccessor.class;
    }

}
