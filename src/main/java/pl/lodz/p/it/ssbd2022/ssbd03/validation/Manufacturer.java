package pl.lodz.p.it.ssbd2022.ssbd03.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Constraint(validatedBy = {})
@Retention(RUNTIME)
@Size(min = 10, max = 50, message = "server.error.validation.constraints.size.manufacturer")
@NotNull(message = "server.error.validation.constraints.notNull.manufacturer")
public @interface Manufacturer {
    String message() default "server.error.validation.constraints.manufacturer";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}

