package pl.lodz.p.it.ssbd2022.ssbd03.validation;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Constraint(validatedBy = {})
@Retention(RUNTIME)
@Positive(message = "server.error.validation.constraints.positive.price")
@NotNull(message = "server.error.validation.constraints.notNull.price")
public @interface Price {
    String message() default "server.error.validation.constraints.price";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}

