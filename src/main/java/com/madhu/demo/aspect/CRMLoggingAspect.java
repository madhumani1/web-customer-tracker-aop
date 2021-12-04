/**
 * 
 */
package com.madhu.demo.aspect;

import java.util.logging.Logger;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
 * @author 15197
 * Create an Aspect class
 * 1. setup Spring logger
 * 2. setup pointcut declarations
 * 2.1. do the same for service and dao
 * 3. add @Before advice
 * 4.add @AfterReturning advice
 */
@Aspect
@Component
public class CRMLoggingAspect {
	// setup Spring logger
	private Logger myLogger = Logger.getLogger(getClass().getName());
	
	// setup pointcut declarations
	@Pointcut("execution(* com.madhu.demo.controller.*.*(..))")
	private void forControllerPackage()	{}
	
	@Pointcut("execution(* com.madhu.demo.service.*.*(..))")
	private void forServicePackage()	{}
	
	@Pointcut("execution(* com.madhu.demo.dao.*.*(..))")
	private void forDaoPackage()	{}
	
	@Pointcut("forControllerPackage() || forServicePackage() || forDaoPackage()")
	private void forAppFlow()	{}
	
	// add @Before advice
	/**
	 * Display the method we are calling
	 * Display the arguments to the method
	 * 	Get the argument and loop through the method
	 * @param theJoinPoint
	 */
	@Before("forAppFlow()")
	public void before(JoinPoint theJoinPoint)	{
		// display the method we are calling
		String theMethod = theJoinPoint.getSignature().toShortString();
		myLogger.info("=====>> in @Before: calling method: " + theMethod);
		
		// display the method we are calling
		
		// get the argument
		Object[] args = theJoinPoint.getArgs();
		
		// loop thru and display args
		for(Object tempArg: args)	{
			myLogger.info("=====>> argument: " + tempArg);
		}
	}
	
	// add @AfterReturning advice
	@AfterReturning(pointcut="forAppFlow()", returning="theResult")
	public void afterReturning(JoinPoint theJoinPoint, Object theResult)	{
		// display method we are returning from
		String theMethod = theJoinPoint.getSignature().toShortString();
		myLogger.info("=====>> in @AfterReturning: from method: " + theMethod);
		
		// display data returned (theResult)
		myLogger.info("=====>> result: " + theResult);
	}
}
