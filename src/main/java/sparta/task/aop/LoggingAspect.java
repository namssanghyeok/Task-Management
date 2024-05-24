package sparta.task.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

@Aspect
@Component
@Slf4j
public class LoggingAspect {
    @Pointcut("execution(* sparta.task.web.controller..*.*(..))")
    private void methodsFromControllerPackage() {
    }

    @Pointcut("execution(* sparta.task.service..*.*(..))")
    private void methodsFromServicePackage() {
    }

    @Before("methodsFromServicePackage() || methodsFromControllerPackage()")
    public void logBefore(JoinPoint joinPoint) {
        String className = joinPoint.getSignature().getDeclaringTypeName();
        String methodName = joinPoint.getSignature().getName();
        String arguments = Arrays.toString(joinPoint.getArgs());
        log.info("Enter method: {}.{}() with arguments: {}", className, methodName, arguments);
    }

    @AfterReturning(pointcut = "methodsFromServicePackage() || methodsFromControllerPackage()", returning = "result")
    public void logAfterReturning(JoinPoint joinPoint, Object result) {
        String className = joinPoint.getSignature().getDeclaringTypeName();
        String name = joinPoint.getSignature().getName();
        log.info("Exiting method: {}.{}() with result = {}", className, name, result);
    }

    @AfterThrowing(pointcut = "methodsFromServicePackage() || methodsFromControllerPackage() ", throwing = "e")
    public void logAfterThrowing(JoinPoint joinPoint, Throwable e) {
        String className = joinPoint.getSignature().getDeclaringTypeName();
        String name = joinPoint.getSignature().getName();
        log.error("Exception in {}.{}() with exception: {} caused: {}", className, name, e, e.getMessage());
    }


//    @After("onRequest() || methodsFromServicePackage()")
//    public void afterMethod(JoinPoint joinPoint) throws Throwable {
//        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
//        Method method = methodSignature.getMethod();
//        String methodName = method.getName();
//        // Object result = joinPoint.proceed();
//        log.info("------ after -------");
//        log.info("METHOD NAME={}", methodName);
//        // log.info("RESTULT={}", result);
//    }

    private String paramMapToString(Map<String, String[]> paramMap) {
        return paramMap.entrySet().stream()
                .map(entry -> String.format("%s -> (%s)",
                        entry.getKey(),
                        String.join(", ", entry.getValue())
                ))
                .collect(Collectors.joining(", "));
    }
}

