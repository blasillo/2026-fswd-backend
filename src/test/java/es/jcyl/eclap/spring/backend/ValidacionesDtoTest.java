package es.jcyl.eclap.spring.backend;

import es.jcyl.eclap.spring.backend.dto.TareaDto;
import es.jcyl.eclap.spring.backend.dto.UsuarioCrearDto;
import es.jcyl.eclap.spring.backend.dto.UsuarioDto;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.BeforeAll;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ValidacionesDtoTest {

    private static Validator validator;

    @BeforeAll
    public static void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void testValidacionesTareaDto() {
        TareaDto valido = TareaDto.builder()
                .id(1)
                .nombre("Tarea válida")
                .estado(50)
                .color("Azul")
                .usuarioCorreo("test@correo.com")
                .build();
        assertTrue(validator.validate(valido).isEmpty());

        TareaDto invalido = TareaDto.builder()
                .id(1)
                .nombre("Ho")                 // menos de 5 caracteres
                .estado(150)                  // fuera de 0-100
                .color("Turquesa")             // fuera de la lista permitida
                .usuarioCorreo("no-es-correo") // formato inválido
                .build();

        Set<ConstraintViolation<TareaDto>> violaciones = validator.validate(invalido);

        assertFalse(violaciones.isEmpty());
        assertTrue(tieneViolacionEn(violaciones, "nombre"));
        assertTrue(tieneViolacionEn(violaciones, "estado"));
        assertTrue(tieneViolacionEn(violaciones, "color"));
        assertTrue(tieneViolacionEn(violaciones, "usuarioCorreo"));
    }
    @Test
    public void testValidacionesUsuarioCrearDto() {
        UsuarioCrearDto valido = UsuarioCrearDto.builder()
                .nombreCompleto("Usuario Válido")
                .iniciales("UV")
                .correo("valido@correo.com")
                .clave("ClaveValida1")
                .roles(new String[]{"BASE"})
                .build();
        assertTrue(validator.validate(valido).isEmpty());

        UsuarioCrearDto invalido = UsuarioCrearDto.builder()
                .nombreCompleto("Usuario")
                .iniciales("UV")
                .correo("no-es-correo")  // formato inválido
                .clave("abc")             // menos de 6 caracteres
                .roles(new String[]{"BASE"})
                .build();

        Set<ConstraintViolation<UsuarioCrearDto>> violaciones = validator.validate(invalido);

        assertFalse(violaciones.isEmpty());
        assertTrue(tieneViolacionEn(violaciones, "correo"));
        assertTrue(tieneViolacionEn(violaciones, "clave"));
    }

    @Test
    public void testValidacionesUsuarioDto() {
        UsuarioDto valido = UsuarioDto.builder()
                .id(1)
                .nombreCompleto("Usuario Válido")
                .iniciales("UV")
                .correo("valido@correo.com")
                .roles(new String[]{"BASE"})
                .build();
        assertTrue(validator.validate(valido).isEmpty());

        UsuarioDto invalido = UsuarioDto.builder()
                .id(null)               // obligatorio para modificar
                .nombreCompleto("Usuario")
                .iniciales("UV")
                .correo("no-es-correo") // formato inválido
                .roles(new String[]{"BASE"})
                .build();

        Set<ConstraintViolation<UsuarioDto>> violaciones = validator.validate(invalido);

        assertFalse(violaciones.isEmpty());
        assertTrue(tieneViolacionEn(violaciones, "id"));
        assertTrue(tieneViolacionEn(violaciones, "correo"));
    }


    @Test
    public void testTareaDtoTodosLosCamposFallan() {
        // nombre, estado, color y usuarioCorreo son TODOS los campos con
        // validación en TareaDto, y aquí los 4 incumplen su restricción a la vez.
        TareaDto invalido = TareaDto.builder()
                .id(1)
                .nombre("Ho")                   // menos de 5 caracteres (min=5)
                .estado(150)                    // fuera de 0-100
                .color("Fucsia")               // fuera de la lista permitida
                .usuarioCorreo("no-es-correo")   // no es un email válido
                .build();

        Set<ConstraintViolation<TareaDto>> violaciones = validator.validate(invalido);

        assertFalse(violaciones.isEmpty());
        assertTrue(tieneViolacionEn(violaciones, "nombre"));
        assertTrue(tieneViolacionEn(violaciones, "estado"));
        assertTrue(tieneViolacionEn(violaciones, "color"));
        assertTrue(tieneViolacionEn(violaciones, "usuarioCorreo"));
    }

    @Test
    public void testUsuarioCrearDtoTodosLosCamposFallan() {
        // nombreCompleto, iniciales, correo y clave son TODOS los campos con
        // validación en UsuarioCrearDto, y aquí los 4 incumplen su restricción a la vez.
        UsuarioCrearDto invalido = UsuarioCrearDto.builder()
                .nombreCompleto("a".repeat(201)) // más de 200 caracteres
                .iniciales("ABCDEFGHIJK")         // más de 10 caracteres
                .correo("no-es-correo")           // no es un email válido
                .clave("abc")                     // menos de 6 caracteres
                .roles(new String[]{"BASE"})
                .build();

        Set<ConstraintViolation<UsuarioCrearDto>> violaciones = validator.validate(invalido);

        assertFalse(violaciones.isEmpty());
        assertTrue(tieneViolacionEn(violaciones, "nombreCompleto"));
        assertTrue(tieneViolacionEn(violaciones, "iniciales"));
        assertTrue(tieneViolacionEn(violaciones, "correo"));
        assertTrue(tieneViolacionEn(violaciones, "clave"));
    }

    @Test
    public void testUsuarioDtoTodosLosCamposFallan() {
        // id, nombreCompleto, iniciales y correo son TODOS los campos con
        // validación en UsuarioDto, y aquí los 4 incumplen su restricción a la vez.
        UsuarioDto invalido = UsuarioDto.builder()
                .id(null)                         // obligatorio para modificar
                .nombreCompleto("a".repeat(201))   // más de 200 caracteres
                .iniciales("ABCDEFGHIJK")           // más de 10 caracteres
                .correo("no-es-correo")             // no es un email válido
                .roles(new String[]{"BASE"})
                .build();

        Set<ConstraintViolation<UsuarioDto>> violaciones = validator.validate(invalido);

        assertFalse(violaciones.isEmpty());
        assertTrue(tieneViolacionEn(violaciones, "id"));
        assertTrue(tieneViolacionEn(violaciones, "nombreCompleto"));
        assertTrue(tieneViolacionEn(violaciones, "iniciales"));
        assertTrue(tieneViolacionEn(violaciones, "correo"));
    }


    private <T> boolean tieneViolacionEn(Set<ConstraintViolation<T>> violaciones, String propiedad) {
        return violaciones.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals(propiedad));
    }
}
