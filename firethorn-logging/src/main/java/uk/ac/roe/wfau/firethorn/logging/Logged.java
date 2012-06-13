/*
 *
 */
package uk.ac.roe.wfau.firethorn.logging ;

import lombok.extern.slf4j.Slf4j;

import java.lang.annotation.Target;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/*
 * Need to create our own implementation that inherits from the Lombok code.
 * https://github.com/rzwitserloot/lombok/blob/master/src/core/lombok/javac/handlers/HandleLog.java
 *
 */
@Retention(
    RetentionPolicy.RUNTIME
    )
public @interface Logged
    {
    }

