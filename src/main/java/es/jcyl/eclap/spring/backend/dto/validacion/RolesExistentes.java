package es.jcyl.eclap.spring.backend.dto.validacion;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = RolesExistentesValidator.class)
public @interface RolesExistentes {
    String message() default "Alguno de los roles indicados no existe";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
