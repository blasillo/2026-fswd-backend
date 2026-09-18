package es.jcyl.eclap.spring.backend.dto.validacion;


import es.jcyl.eclap.spring.backend.persistencia.repositorios.RolesRepositorio;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RolesExistentesValidator implements ConstraintValidator<RolesExistentes, String[]> {

    private final RolesRepositorio rolesRepo;

    @Override
    public boolean isValid(String[] roles, ConstraintValidatorContext context) {
        if (roles == null) {
            return true; // que sea null o no, lo decide @NotEmpty si hace falta, no esta anotación
        }
        return rolesRepo.findByNombreIn(java.util.Arrays.asList(roles)).size() == roles.length;
    }
}
