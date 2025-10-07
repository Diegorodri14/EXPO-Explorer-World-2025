package Explorer_World_Api.Controller;

import Explorer_World_Api.Exceptions.ExcepcionPresupuestoNoRegistrado;
import Explorer_World_Api.Exceptions.ExceptionDatosDuplicados;
import Explorer_World_Api.Model.DTO.PagosDTO;
import Explorer_World_Api.Model.DTO.PresupuestoDTO;
import Explorer_World_Api.Service.PresupuestoService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/ApiPresupuesto")
public class PresupuestoController {
    @Autowired
    private PresupuestoService service;

    //-----------Paginacion
    @GetMapping("/ListaPresupuesto")
    private ResponseEntity<Page<PresupuestoDTO>> getDataProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size
    ){
        if (size <= 0 || size > 50){
            ResponseEntity.badRequest().body(Map.of(
                    "status", "El tamaño de la página debe estar entre 1 y 50"
            ));
            return ResponseEntity.ok(null);
        }
        Page<PresupuestoDTO> categories = service.PaginacionPresupuesto(page, size);
        if (categories == null){
            ResponseEntity.badRequest().body(Map.of(
                    "status", "No hay registros"
            ));
        }
        return ResponseEntity.ok(categories);
    }

    // ------------ CONSULTAR DATOS
    @GetMapping("/consultarPresupuesto")
    public ResponseEntity<List<PresupuestoDTO>> ObtenerPresupuesto() {
        return ResponseEntity.ok(service.ObtenerPresupuesto());
    }



    // ------------ INSERCIÓN DE DATOS
    @PostMapping("/registrarPresupuesto")
    public ResponseEntity<?> AgregarPresupuesto(@Valid @RequestBody PresupuestoDTO json, BindingResult bindingResult, HttpServletRequest request) {
        if (bindingResult.hasErrors()) {
            Map<String, String> errores = new HashMap<>();
            bindingResult.getFieldErrors().forEach(error ->
                    errores.put(error.getField(), error.getDefaultMessage()));
            return ResponseEntity.badRequest().body(errores);
        }

        try {
            PresupuestoDTO respuesta = service.AgregarPresupuesto(json);
            return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                    "status", "Registrado",
                    "data", respuesta
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of(
                            "status", "Error",
                            "message", "Error al registrar Presupuesto",
                            "detail", e.getMessage()
                    ));
        }
    }

    @PutMapping("/editarPresupuesto/{id}")
    public ResponseEntity<?> ActualizarPresupuesto(
            @PathVariable Long id,
            @Valid @RequestBody PresupuestoDTO data,
            BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            Map<String, String> errores = new HashMap<>();
            bindingResult.getFieldErrors().forEach(error ->
                    errores.put(error.getField(), error.getDefaultMessage()));
            return ResponseEntity.badRequest().body(errores);
        }

        try {
            PresupuestoDTO dto = service.ActualizarPresupuesto(id, data);
            return ResponseEntity.ok(Map.of(
                    "mensaje", "Registro actualizado",
                    "data", dto
            ));
        } catch (ExcepcionPresupuestoNoRegistrado e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of(
                            "Error", "NOT FOUND",
                            "Mensaje", e.getMessage(),
                            "timestamp", Instant.now().toString()
                    ));
        } catch (ExceptionDatosDuplicados e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(
                    Map.of("Error", "Datos Duplicados",
                            "Campo", e.getCampoDuplicado(),
                            "message", e.getMessage())
            );
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of(
                    "status", "Error",
                    "message", "Error al actualizar el Presupuesto",
                    "details", e.getMessage()
            ));
        }
    }

    @DeleteMapping("/eliminarPresupuesto/{id}")
    public ResponseEntity<?> eliminarPresupuesto(@PathVariable Long id) {
        try {
            if (!service.BorrarPresupuesto(id)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of(
                                "Error", "NOT FOUND",
                                "Mensaje", "Presupuesto no fue encontrado",
                                "timestamp", Instant.now().toString()
                        ));
            }
            return ResponseEntity.ok().body(Map.of(
                    "status", "Success",
                    "message", "Presupuesto a sido eliminado exitosamente"
            ));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of(
                    "status", "Error",
                    "message", "Error al eliminar el Presupuesto",
                    "details", e.getMessage()
            ));
        }
    }
}
