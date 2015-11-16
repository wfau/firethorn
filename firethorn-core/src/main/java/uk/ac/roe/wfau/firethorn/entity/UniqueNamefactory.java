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
public class UniqueNamefactory extends AbstractComponent
	{
    /**
     * Our base 32 encoder.
     * 
     */
    protected static final BaseEncoding encoder = BaseEncoding.base32().omitPadding() ;

    /**
     * Our name glue character, {@value}.
     * 
     */
    protected static final String NAME_GLUE = "_" ;

    /**
     * Our default name prefix, {@value}.
     * 
     */
    protected static final String NAME_PREFIX = "XX" ;

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
			NAME_PREFIX,
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
				NAME_GLUE
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
