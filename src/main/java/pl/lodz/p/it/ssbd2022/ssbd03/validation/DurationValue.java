package pl.lodz.p.it.ssbd2022.ssbd03.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Constraint(validatedBy = {})
@Retention(RUNTIME)
@Positive(message = "server.error.validation.constraints.positive.duration")
@NotNull(message = "server.error.validation.constraints.notNull.duration")
public @interface DurationValue {
    String message() default "server.error.validation.constraints.duration";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}

