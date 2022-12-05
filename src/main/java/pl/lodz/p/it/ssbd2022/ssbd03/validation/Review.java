package pl.lodz.p.it.ssbd2022.ssbd03.validation;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Constraint(validatedBy = {})
@Retention(RUNTIME)
@Size(min = 10, max = 1000, message = "server.error.validation.constraints.size.review")
@NotNull(message = "server.error.validation.constraints.notNull.review")
public @interface Review {
    String message() default "server.error.validation.constraints.description";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
