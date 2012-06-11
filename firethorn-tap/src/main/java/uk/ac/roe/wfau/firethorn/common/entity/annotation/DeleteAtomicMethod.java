/*
 *
 */
package uk.ac.roe.wfau.firethorn.common.entity.annotation;

import java.lang.annotation.Target;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional; 

@Retention(
    RetentionPolicy.RUNTIME
    )
@Transactional(
    readOnly=false,
    propagation=Propagation.REQUIRES_NEW
    )
public @interface DeleteAtomicMethod
    {
    }

