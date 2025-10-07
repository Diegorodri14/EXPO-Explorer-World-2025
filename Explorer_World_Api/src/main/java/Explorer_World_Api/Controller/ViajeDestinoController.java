package Explorer_World_Api.Controller;

import Explorer_World_Api.Exceptions.ExcepcionViajeDestinoNoRegistrado;
import Explorer_World_Api.Exceptions.ExceptionDatosDuplicados;
import Explorer_World_Api.Model.DTO.ViajeDestinoDTO;
import Explorer_World_Api.Service.ViajeDestinoService;
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
@RequestMapping("/ApiViajeDestino")
public class ViajeDestinoController {
    @Autowired
    private ViajeDestinoService service;

    //-----------Paginacion
    @GetMapping("/ListaViajeDestino")
    private ResponseEntity<Page<ViajeDestinoDTO>> getDataViajeDestino(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size
    ){
        if (size <= 0 || size > 50){
            ResponseEntity.badRequest().body(Map.of(
                    "status", "El tamaño de la página debe estar entre 1 y 50"
            ));
            return ResponseEntity.ok(null);
        }
        Page<ViajeDestinoDTO> viajeDestinos = service.PaginacionViajeDestino(page, size);
        if (viajeDestinos == null){
            ResponseEntity.badRequest().body(Map.of(
                    "status", "No hay registros"
            ));
        }
        return ResponseEntity.ok(viajeDestinos);
    }

    // ------------ CONSULTAR DATOS
    @GetMapping("/consultarViajeDestino")
    public ResponseEntity<List<ViajeDestinoDTO>> ObtenerViajeDestino() {
        return ResponseEntity.ok(service.ObtenerViajeDestino());
    }

    // ------------ INSERCIÓN DE DATOS
    @PostMapping("/registrarViajeDestino")
    public ResponseEntity<?> AgregarViajeDestino(@Valid @RequestBody ViajeDestinoDTO json, BindingResult bindingResult, HttpServletRequest request) {
        if (bindingResult.hasErrors()) {
            Map<String, String> errores = new HashMap<>();
            bindingResult.getFieldErrors().forEach(error ->
                    errores.put(error.getField(), error.getDefaultMessage()));
            return ResponseEntity.badRequest().body(errores);
        }

        try {
            ViajeDestinoDTO respuesta = service.AgregarViajeDestino(json);
            return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                    "status", "Registrado",
                    "data", respuesta
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of(
                            "status", "Error",
                            "message", "Error al registrar el ViajeDestino",
                            "detail", e.getMessage()
                    ));
        }
    }

    @PutMapping("/editarViajeDestino/{id}")
    public ResponseEntity<?> ActualizarViajeDestino(
            @PathVariable Long id,
            @Valid @RequestBody ViajeDestinoDTO data,
            BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            Map<String, String> errores = new HashMap<>();
            bindingResult.getFieldErrors().forEach(error ->
                    errores.put(error.getField(), error.getDefaultMessage()));
            return ResponseEntity.badRequest().body(errores);
        }

        try {
            ViajeDestinoDTO dto = service.ActualizarViajeDestino(id, data);
            return ResponseEntity.ok(Map.of(
                    "mensaje", "Registro actualizado",
                    "data", dto
            ));
        } catch (ExcepcionViajeDestinoNoRegistrado e) {
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
                    "message", "Error al actualizar el ViajeDestino",
                    "details", e.getMessage()
            ));
        }
    }

    @DeleteMapping("/eliminarViajeDestino/{id}")
    public ResponseEntity<?> eliminarViajeDestino(@PathVariable Long id) {
        try {
            if (!service.BorrarViajeDestino(id)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of(
                                "Error", "NOT FOUND",
                                "Mensaje", "ViajeDestino no fue encontrado",
                                "timestamp", Instant.now().toString()
                        ));
            }
            return ResponseEntity.ok().body(Map.of(
                    "status", "Success",
                    "message", "Registro del ViajeDestino ha sido eliminado exitosamente"
            ));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of(
                    "status", "Error",
                    "message", "Error al eliminar el ViajeDestino",
                    "details", e.getMessage()
            ));
        }
    }
}
