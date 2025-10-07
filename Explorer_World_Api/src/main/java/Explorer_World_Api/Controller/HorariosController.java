package Explorer_World_Api.Controller;

import Explorer_World_Api.Exceptions.ExcepcionHorarioNoRegistrado;
import Explorer_World_Api.Exceptions.ExceptionDatosDuplicados;
import Explorer_World_Api.Model.DTO.FacturasDTO;
import Explorer_World_Api.Model.DTO.HorariosDTO;
import Explorer_World_Api.Service.HorariosService;
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
@RequestMapping("/ApiHorarios")
public class HorariosController {
    @Autowired
    private HorariosService service;

    //-----------Paginacion
    @GetMapping("/ListaHorarios")
    private ResponseEntity<Page<HorariosDTO>> getDataProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size
    ){
        if (size <= 0 || size > 50){
            ResponseEntity.badRequest().body(Map.of(
                    "status", "El tamaño de la página debe estar entre 1 y 50"
            ));
            return ResponseEntity.ok(null);
        }
        Page<HorariosDTO> categories = service.PaginacionHorarios(page, size);
        if (categories == null){
            ResponseEntity.badRequest().body(Map.of(
                    "status", "No hay registros"
            ));
        }
        return ResponseEntity.ok(categories);
    }

    // ------------ CONSULTAR DATOS
    @GetMapping("/consultarHorarios")
    public ResponseEntity<List<HorariosDTO>> ObtenerHorarios() {
        return ResponseEntity.ok(service.ObtenerHorarios());
    }



    // ------------ INSERCIÓN DE DATOS
    @PostMapping("/registrarHorarios")
    public ResponseEntity<?> AgregarHorarios(@Valid @RequestBody HorariosDTO json, BindingResult bindingResult, HttpServletRequest request) {
        if (bindingResult.hasErrors()) {
            Map<String, String> errores = new HashMap<>();
            bindingResult.getFieldErrors().forEach(error ->
                    errores.put(error.getField(), error.getDefaultMessage()));
            return ResponseEntity.badRequest().body(errores);
        }

        try {
            HorariosDTO respuesta = service.AgregarHorarios(json);
            return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                    "status", "Registrado",
                    "data", respuesta
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of(
                            "status", "Error",
                            "message", "Error al registrar horario",
                            "detail", e.getMessage()
                    ));
        }
    }

    @PutMapping("/editarHorarios/{id}")
    public ResponseEntity<?> ActualizarHorarios(
            @PathVariable Long id,
            @Valid @RequestBody HorariosDTO data,
            BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            Map<String, String> errores = new HashMap<>();
            bindingResult.getFieldErrors().forEach(error ->
                    errores.put(error.getField(), error.getDefaultMessage()));
            return ResponseEntity.badRequest().body(errores);
        }

        try {
            HorariosDTO dto = service.ActualizarHorarios(id, data);
            return ResponseEntity.ok(Map.of(
                    "mensaje", "Registro actualizado",
                    "data", dto
            ));
        } catch (ExcepcionHorarioNoRegistrado e) {
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
                    "message", "Error al actualizar el horario",
                    "details", e.getMessage()
            ));
        }
    }

    @DeleteMapping("/eliminarHorarios/{id}")
    public ResponseEntity<?> eliminarHorarios(@PathVariable Long id) {
        try {
            if (!service.BorrarHorarios(id)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of(
                                "Error", "NOT FOUND",
                                "Mensaje", "El horario no fue encontrado",
                                "timestamp", Instant.now().toString()
                        ));
            }
            return ResponseEntity.ok().body(Map.of(
                    "status", "Success",
                    "message", "Horario a sido eliminado exitosamente"
            ));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of(
                    "status", "Error",
                    "message", "Error al eliminar el horario",
                    "details", e.getMessage()
            ));
        }
    }
}
