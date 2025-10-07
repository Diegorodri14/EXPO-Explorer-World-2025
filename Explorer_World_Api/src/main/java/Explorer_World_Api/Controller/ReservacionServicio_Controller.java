package Explorer_World_Api.Controller;

import Explorer_World_Api.Exceptions.ExcepcionUsuarioNoRegistrado;
import Explorer_World_Api.Exceptions.ExceptionDatosDuplicados;
import Explorer_World_Api.Model.DTO.ReservacionServicio_DTO;
import Explorer_World_Api.Model.DTO.ReservacionesDTO;
import Explorer_World_Api.Service.ReservacionServicio_Service;
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
@RequestMapping("/apiReservacionService")

public class ReservacionServicio_Controller {

    @Autowired
    ReservacionServicio_Service service;
    //-----------Paginacion
    @GetMapping("/ListaReservacionServicio")
    private ResponseEntity<Page<ReservacionServicio_DTO>> getDataProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size
    ){
        if (size <= 0 || size > 50){
            ResponseEntity.badRequest().body(Map.of(
                    "status", "El tamaño de la página debe estar entre 1 y 50"
            ));
            return ResponseEntity.ok(null);
        }
        Page<ReservacionServicio_DTO> categories = service.PaginacionReservacionServicio(page, size);
        if (categories == null){
            ResponseEntity.badRequest().body(Map.of(
                    "status", "No hay registros"
            ));
        }
        return ResponseEntity.ok(categories);
    }

    // ------------ CONSULTAR DATOS
    @GetMapping("/consultarReservacionService")
    public ResponseEntity<List<ReservacionServicio_DTO>> obtenerReservacionServicio() {
        return ResponseEntity.ok(service.obtenerReservacionServicio());
    }

    // ------------ INSERCIÓN DE DATOS
    @PostMapping("/registrarReservacionService")
    public ResponseEntity<?> nuevoReservacion(@Valid @RequestBody ReservacionServicio_DTO json, BindingResult bindingResult, HttpServletRequest request) {
        if (bindingResult.hasErrors()) {
            Map<String, String> errores = new HashMap<>();
            bindingResult.getFieldErrors().forEach(error ->
                    errores.put(error.getField(), error.getDefaultMessage()));
            return ResponseEntity.badRequest().body(errores);
        }

        try {
            ReservacionServicio_DTO respuesta = service.nuevaReservacionServicio(json);
            return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                    "status", "Registrado",
                    "data", respuesta
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of(
                            "status", "Error",
                            "message", "Error al registrar usuario",
                            "detail", e.getMessage()
                    ));
        }
    }

    @PutMapping("/editarReservacionService/{id}")
    public ResponseEntity<?> modificarReservacionServicio(
            @PathVariable Long id,
            @Valid @RequestBody ReservacionServicio_DTO json,
            BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            Map<String, String> errores = new HashMap<>();
            bindingResult.getFieldErrors().forEach(error ->
                    errores.put(error.getField(), error.getDefaultMessage()));
            return ResponseEntity.badRequest().body(errores);
        }

        try {
            ReservacionServicio_DTO dto = service.actualizarReservacionServicio(id, json);
            return ResponseEntity.ok(Map.of(
                    "mensaje", "Registro actualizado",
                    "data", dto
            ));
        } catch (ExcepcionUsuarioNoRegistrado e) {
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
                    "message", "Error al actualizar el usuario",
                    "details", e.getMessage()
            ));
        }
    }

    @DeleteMapping("/eliminarRegistro/{id}")
    public ResponseEntity<?> eliminarUsuario(@PathVariable Long id) {
        try {
            if (!service.eliminarReservacionServicio(id)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of(
                                "Error", "NOT FOUND",
                                "Mensaje", "El usuario no fue encontrado",
                                "timestamp", Instant.now().toString()
                        ));
            }
            return ResponseEntity.ok().body(Map.of(
                    "status", "Success",
                    "message", "Usuario eliminado exitosamente"
            ));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of(
                    "status", "Error",
                    "message", "Error al eliminar el usuario",
                    "details", e.getMessage()
            ));
        }
    }
}
