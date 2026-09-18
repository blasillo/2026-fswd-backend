package es.jcyl.eclap.spring.backend.controladores;


import es.jcyl.eclap.spring.backend.dto.UsuarioCrearDto;
import es.jcyl.eclap.spring.backend.dto.UsuarioDto;
import es.jcyl.eclap.spring.backend.servicios.UsuarioServicio;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v{version}/usuarios")
@RequiredArgsConstructor
public class UsuarioControlador {

    private final UsuarioServicio servicio;

    @GetMapping(version = "1.0")
    public ResponseEntity<List<UsuarioDto>> listadoUsuarios() {
        return ResponseEntity.ok(servicio.obtenerUsuarios());
    }

    @PostMapping(version = "1.0")
    public ResponseEntity<UsuarioDto> nuevoUsuario(
            @Valid @RequestBody UsuarioCrearDto modelo) {
        return ResponseEntity.ok(servicio.crearUsuario(modelo));
    }

    @PutMapping(version = "1.0")
    public ResponseEntity<UsuarioDto> editarUsuario(
            @Valid @RequestBody UsuarioDto modelo) {
        return ResponseEntity.ok(servicio.modificarUsuario(modelo));
    }

    @DeleteMapping(version = "1.0")
    public Integer borrarUsuario(@RequestParam("usuarioId") Integer id) {
        return servicio.borrarUsuario(id);
    }


}
