package pl.lodz.p.it.ssbd2022.ssbd03.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Constraint(validatedBy = {})
@Retention(RUNTIME)
@Size(min = 8, max = 2000, message = "server.error.validation.constraints.size.url")
@Pattern(regexp = Patterns.URL, message = "server.error.validation.constraints.url")
public @interface Url {
    String message() default "server.error.validation.constraints.url";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
