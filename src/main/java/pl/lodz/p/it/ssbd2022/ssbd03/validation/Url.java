package pl.lodz.p.it.ssbd2022.ssbd03.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.constraints.Pattern;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Constraint(validatedBy = {})
@Retention(RUNTIME)
@Pattern(regexp = Patterns.URL, message = "server.error.validation.constraints.url")
public @interface Url {
    String message() default "server.error.validation.constraints.url";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
