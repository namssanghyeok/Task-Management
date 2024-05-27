package sparta.task.presentational.web.validation.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import sparta.task.infrastructure.web.validator.FileValidator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = FileValidator.class)
public @interface File {
    String message() default "IMAGE FORMAT IS ALLOWED";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
