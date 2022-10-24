package pl.lodz.p.it.ssbd2022.ssbd03.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.constraints.*;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Constraint(validatedBy = {})
@Retention(RUNTIME)
@PositiveOrZero(message = "server.error.validation.constraints.positiveOrZero.rating")
@Max(value = 5, message = "server.error.validation.constraints.max.rating")
@NotNull(message = "server.error.validation.constraints.notNull.rating")
public @interface Rating {
    String message() default "server.error.validation.constraints.rating";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
