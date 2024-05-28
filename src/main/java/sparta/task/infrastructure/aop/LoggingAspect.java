package sparta.task.infrastructure.aop;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Component
@Aspect
public class LoggingAspect {
    @Pointcut("execution(* sparta.task.presentational.web.controller..*.*(..))")
    private void methodsFromControllerPackage() {
    }

    @Pointcut("execution(* sparta.task.application.usecase..*.*(..))")
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

    // NOTE: GlobalExceptionHandler
    @Pointcut("execution(* sparta.task.infrastructure.exception..*.*(..))")
    private void methodsFromExceptionHandler() {
    }

    @Before("methodsFromExceptionHandler()")
    public void logAroundExceptionHandler(JoinPoint joinPoint) {
        HttpServletRequest request = // 5
                ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        String uri = request.getRequestURI();
        String method = request.getMethod();
        // TODO: arguments 에서 exception 객체만 걸러내고 error 내용 정제해서 보여주기
        String arguments = Arrays.toString(joinPoint.getArgs());
        System.out.println("length: " + joinPoint.getArgs().length);
        log.info("ExceptionHandler Request [{}]{} from {}. Error: {}", method, uri, request.getRemoteHost(), arguments);
    }

    private String paramMapToString(Map<String, String[]> paramMap) {
        return paramMap.entrySet().stream()
                .map(entry -> String.format("%s -> (%s)",
                        entry.getKey(),
                        String.join(", ", entry.getValue())
                ))
                .collect(Collectors.joining(", "));
    }
}

