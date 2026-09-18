package es.jcyl.eclap.spring.backend;
import es.jcyl.eclap.spring.backend.dto.validacion.RolesExistentesValidator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class RolesExistentesValidadorTest {

    @Autowired
    private RolesExistentesValidator validador;

    @Test
    public void testRolesQueExistenSonValidos() {
        boolean resultado = validador.isValid(new String[]{"BASE", "ADMINISTRADOR"}, null);

        assertTrue(resultado);
    }

    @Test
    public void testRolQueNoExisteEsInvalido() {
        boolean resultado = validador.isValid(new String[]{"BASE", "ROL_QUE_NO_EXISTE"}, null);

        assertFalse(resultado);
    }

    @Test
    public void testArrayNuloEsValido() {
        boolean resultado = validador.isValid(null, null);

        assertTrue(resultado);
    }
}