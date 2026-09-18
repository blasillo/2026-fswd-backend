package es.jcyl.eclap.spring.backend.controladores;


import es.jcyl.eclap.spring.backend.dto.TareaDto;
import es.jcyl.eclap.spring.backend.servicios.TareaServicio;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("tareas")
@RequiredArgsConstructor
public class TareaControlador {

    private final TareaServicio servicio;

    @GetMapping
    public ResponseEntity<Page<TareaDto>> listadoTareas(
            @RequestParam("correo") String correo,
            Pageable pageable) {
        return ResponseEntity.ok(servicio.obtenerTareas(correo, pageable));
    }

    @PostMapping
    public ResponseEntity<TareaDto>  nuevaTarea (
            @Valid @RequestBody TareaDto modelo) {
        return ResponseEntity.ok(  servicio.crearTarea (modelo) );
    }


    @PutMapping()
    public ResponseEntity<TareaDto> editarTarea (
            @Valid @RequestBody TareaDto modelo) {
        return ResponseEntity.ok( servicio.modificarTarea( modelo ));
    }

    @DeleteMapping()
    public Integer borrarTarea(@RequestParam("tareaId") Integer id) {
        return servicio.borrarTarea( id );
    }

}
