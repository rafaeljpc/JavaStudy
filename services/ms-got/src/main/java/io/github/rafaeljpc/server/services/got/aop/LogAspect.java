package io.github.rafaeljpc.server.services.got.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

@Aspect
@Component
public class LogAspect {

    @Around("@annotation(logExecution)")
    public Object LogExecution(ProceedingJoinPoint joinPoint, LogExecution logExecution) throws Throwable {
        final String className = joinPoint.getSignature().getDeclaringTypeName();
        final String methodName = joinPoint.getSignature().getName();
        Logger log = LoggerFactory.getLogger(className);

        Object result = null;
        log.info("Will execute method {}.", methodName);
        final StopWatch stopWatch = new StopWatch();


        try {
            stopWatch.start();

            result = joinPoint.proceed();

            stopWatch.stop();

            log.info("Method {} executed within {} miliseconds.", methodName, stopWatch.getTotalTimeMillis());
        }
        catch (Exception ex) {
            if (logExecution.logException()) {
                log.error("Exception was raised while trying to execute method " + methodName, ex);
            }

            throw ex;
        }

        return result;
    }

    @Around("@annotation(logRequest)")
    public Object LogRequest(ProceedingJoinPoint joinPoint, LogRequest logRequest) throws Throwable {
        // Intercepts called class and method name
        final String className = joinPoint.getSignature().getDeclaringTypeName();
        final Logger logger = LoggerFactory.getLogger(className);

        // Intercepts HTTP/HTTPS request
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();

        // Logs info
        logger.info("[{}][{}][{}][{}][{}]",
                request.getHeader("X-Request-ID"), 	request.getRemoteHost(), request.getHeader("X-Forwarded-For"),
                request.getHeader("X-Forwarded-Host"), request.getHeader("X-Forwarded-Proto"));

        // Allows called method to execute and return it's result, if any
        return joinPoint.proceed();
    }
}
