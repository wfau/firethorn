/**
 * 
 */
package uk.ac.roe.wfau.firethorn.annotations;

import lombok.extern.slf4j.Slf4j;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

/**
 *
 *
 */
@Slf4j
@Aspect
public class CheckpointAspect
	{

	@Pointcut("execution(public * *(..))")
	public void anyPublicMethod()
		{
		}

	@Around("anyPublicMethod() && @annotation(frog)")
	public Object toad(ProceedingJoinPoint pjp, Checkpoint frog)
	throws Throwable
		{
		log.debug("toad()");
		log.debug("  Value [{}]", frog.value());
		//log.debug("  Action [{}]", checkpoint.action());
 
		// Do what you want with the join point arguments
		for (Object object : pjp.getArgs())
			{
			log.debug("join point arg [{}][{}]", object.getClass().getName(), object);
			}
 
		return pjp.proceed();
		}	
	}
