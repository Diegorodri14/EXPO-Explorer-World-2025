package Explorer_World_Api.Controller;

import Explorer_World_Api.Exceptions.ExcepcionReservacionEventoNoRegistrado;
import Explorer_World_Api.Exceptions.ExceptionDatosDuplicados;
import Explorer_World_Api.Model.DTO.ReservacionEventoDTO;
import Explorer_World_Api.Service.ReservacionEventoService;
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
@RequestMapping("/ApiReservacionEvento")
public class ReservacionEventoController {
    @Autowired
    private ReservacionEventoService service;

    //-----------Paginacion
    @GetMapping("/ListaReservacionEvento")
    private ResponseEntity<Page<ReservacionEventoDTO>> getDataReservacionEvento(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size
    ){
        if (size <= 0 || size > 50){
            return ResponseEntity.badRequest().body(null);
        }
        Page<ReservacionEventoDTO> reservacionEventos = service.PaginacionReservacionEvento(page, size);
        if (reservacionEventos == null || reservacionEventos.isEmpty()){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(reservacionEventos);
    }

    // ------------ CONSULTAR DATOS
    @GetMapping("/consultarReservacionEvento")
    public ResponseEntity<List<ReservacionEventoDTO>> ObtenerReservacionEvento() {
        List<ReservacionEventoDTO> resultados = service.ObtenerReservacionEvento();
        if (resultados.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(resultados);
    }

    // ------------ INSERCIÓN DE DATOS
    @PostMapping("/registrarReservacionEvento")
    public ResponseEntity<?> AgregarReservacionEvento(@Valid @RequestBody ReservacionEventoDTO json, BindingResult bindingResult, HttpServletRequest request) {
        if (bindingResult.hasErrors()) {
            Map<String, String> errores = new HashMap<>();
            bindingResult.getFieldErrors().forEach(error ->
                    errores.put(error.getField(), error.getDefaultMessage()));
            return ResponseEntity.badRequest().body(errores);
        }

        try {
            ReservacionEventoDTO respuesta = service.AgregarReservacionEvento(json);
            return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                    "status", "Registrado",
                    "data", respuesta
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of(
                            "status", "Error",
                            "message", "Error al registrar la ReservacionEvento",
                            "detail", e.getMessage()
                    ));
        }
    }

    @PutMapping("/editarReservacionEvento/{id}")
    public ResponseEntity<?> ActualizarReservacionEvento(
            @PathVariable Long id,
            @Valid @RequestBody ReservacionEventoDTO data,
            BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            Map<String, String> errores = new HashMap<>();
            bindingResult.getFieldErrors().forEach(error ->
                    errores.put(error.getField(), error.getDefaultMessage()));
            return ResponseEntity.badRequest().body(errores);
        }

        try {
            ReservacionEventoDTO dto = service.ActualizarReservacionEvento(id, data);
            return ResponseEntity.ok(Map.of(
                    "mensaje", "Registro actualizado",
                    "data", dto
            ));
        } catch (ExcepcionReservacionEventoNoRegistrado e) {
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
                    "message", "Error al actualizar la ReservacionEvento",
                    "details", e.getMessage()
            ));
        }
    }

    @DeleteMapping("/eliminarReservacionEvento/{id}")
    public ResponseEntity<?> eliminarReservacionEvento(@PathVariable Long id) {
        try {
            if (!service.BorrarReservacionEvento(id)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of(
                                "Error", "NOT FOUND",
                                "Mensaje", "ReservacionEvento no fue encontrada",
                                "timestamp", Instant.now().toString()
                        ));
            }
            return ResponseEntity.ok().body(Map.of(
                    "status", "Success",
                    "message", "Registro de la ReservacionEvento ha sido eliminado exitosamente"
            ));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of(
                    "status", "Error",
                    "message", "Error al eliminar la ReservacionEvento",
                    "details", e.getMessage()
            ));
        }
    }
}
