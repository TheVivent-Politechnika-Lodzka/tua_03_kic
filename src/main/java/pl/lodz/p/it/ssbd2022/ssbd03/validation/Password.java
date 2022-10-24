package pl.lodz.p.it.ssbd2022.ssbd03.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Constraint(validatedBy = {})
@Retention(RUNTIME)
@Size(min = 8, max = 128, message = "server.error.validation.constraints.size.password")
@NotNull(message = "server.error.validation.constraints.notNull.password")
@Pattern(regexp = Patterns.PASSWORD, message = "server.error.validation.constraints.password")
public @interface Password {
    String message() default "server.error.validation.constraints.password";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
