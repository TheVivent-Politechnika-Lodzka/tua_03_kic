package pl.lodz.p.it.ssbd2022.ssbd03.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Constraint(validatedBy = {})
@Retention(RUNTIME)
@Size(min = 0, max = 1000, message = "server.error.validation.constraints.size.description")
@NotNull(message = "server.error.validation.constraints.notNull.description")
public @interface Description {
    String message() default "server.error.validation.constraints.description";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
