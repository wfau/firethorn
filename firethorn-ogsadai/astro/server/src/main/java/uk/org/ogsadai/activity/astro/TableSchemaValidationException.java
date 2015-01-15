package uk.org.ogsadai.activity.astro;


public class TableSchemaValidationException extends Exception 
{

    public TableSchemaValidationException(Throwable cause)
    {
        super("Validation of table schema XML failed.", cause);
    }
    
}
