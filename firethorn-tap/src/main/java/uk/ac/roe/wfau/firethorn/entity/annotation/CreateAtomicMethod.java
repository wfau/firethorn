/*
 *
 */
package uk.ac.roe.wfau.firethorn.entity.annotation;

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
public @interface CreateAtomicMethod
    {
    }

