/**
 * 
 */
package uk.ac.roe.wfau.firethorn.entity;

import java.nio.ByteBuffer;

import com.google.common.io.BaseEncoding;

/**
 * Unique name factory, based on the {@link Entity} uid values.
 * @todo make this into the new NameFactory interface, with different implementations providing different prefixes per class.
 *
 */
public class UniqueNamefactory
extends AbstractComponent
	{
    /**
     * Our base 32 encoder.
     * 
     */
    protected static final BaseEncoding encoder = BaseEncoding.base32().omitPadding() ;

    /**
     * The default name prefix, {@value}.
     * 
     */
    protected static final String DEFAULT_NAME_PREFIX = "XX" ;

    /**
     * The default glue character, {@value}.
     * 
     */
    protected static final String DEFAULT_NAME_GLUE = "_" ;

    /**
     * Our name glue.
     * 
     */
    private String glue = DEFAULT_NAME_GLUE;

    /**
     * Our name prefix.
     * 
     */
    private String prefix = null;

    /**
     * Public constructor.
     * 
     */
    public UniqueNamefactory()
    	{
        this(
            DEFAULT_NAME_PREFIX,
            DEFAULT_NAME_GLUE
            );
    	}
    
    /**
     * Public constructor.
     * 
     */
    public UniqueNamefactory(final String prefix)
    	{
    	this(
	        prefix,
	        DEFAULT_NAME_GLUE
            );
    	}

    /**
     * Public constructor.
     * 
     */
    public UniqueNamefactory(final String prefix, final String glue)
    	{
    	this.glue   = glue;
    	this.prefix = prefix;
    	}

	protected String base(final AbstractEntity entity)
		{
        final byte[] bytes = new byte[16];
        final ByteBuffer buffer = ByteBuffer.wrap(bytes);
        
        buffer.putLong(entity.uidlo());
        buffer.putLong(entity.uidhi());
        
        final String string = encoder.encode(bytes);
		
        return string;

		}

	public String name(final AbstractEntity entity)
		{
		return name(
			this.prefix,
			entity
			);
		}

	public String name(final String prefix, final AbstractEntity entity)
		{
		final StringBuilder builder = new StringBuilder(); 
		if (prefix != null)
			{
			builder.append(
				prefix
				);
			builder.append(
				this.glue
				);
			}
		builder.append(
			base(
				entity
				)
			);
		return builder.toString();
		}
	}
