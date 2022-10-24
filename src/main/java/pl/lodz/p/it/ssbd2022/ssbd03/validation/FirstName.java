package pl.lodz.p.it.ssbd2022.ssbd03.validation;

import javax.inject.Inject;
import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Interfejs weryfikujący poprawność pola imienia
 */
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Constraint(validatedBy = {})
@Retention(RUNTIME)
@Size(min = 3, max = 30, message = "server.error.validation.constraints.size.firstName")
@NotNull(message = "server.error.validation.constraints.notNull.firstName")
@Pattern(regexp = Patterns.FIRST_NAME, message = "server.error.validation.constraints.firstName")
public @interface FirstName {

    String message() default "server.error.validation.constraints.firstName";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
