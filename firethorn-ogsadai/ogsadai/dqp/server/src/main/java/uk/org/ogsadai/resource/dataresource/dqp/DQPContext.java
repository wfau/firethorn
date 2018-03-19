package uk.org.ogsadai.resource.dataresource.dqp;

import java.io.File;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import uk.org.ogsadai.dqp.common.CompilerConfiguration;

public class DQPContext 
{

    private static final String DQP_FEDERATION = "federation";
    private static final String COMPILER_CONFIGURATION = "compilerConfiguration";
    
    private ApplicationContext mSpringContext;

    public DQPContext(String configLocation)
    {
        mSpringContext = new FileSystemXmlApplicationContext(configLocation);
    }
    
    public DQPContext(File file)
    {
        mSpringContext = new FileSystemXmlApplicationContext(file.getAbsolutePath());
    }
    
    public DQPFederation getDQPFederation()
    {
        return (DQPFederation)get(DQP_FEDERATION);
    }
    
    public CompilerConfiguration getCompilerConfiguration()
    {
        return (CompilerConfiguration)get(COMPILER_CONFIGURATION);
    }
    
    public Object get(Object key) 
    {
        if (mSpringContext == null)
        {
            throw new IllegalStateException("No Spring context");
        }
        else
        {
            return mSpringContext.getBean(key.toString());
        }
    }

    
}
