package Explorer_World_Api.Controller;

import Explorer_World_Api.Exceptions.ExcepcionClienteNoRegistrado;
import Explorer_World_Api.Exceptions.ExcepcionServicioNoRegistrado;
import Explorer_World_Api.Exceptions.ExceptionDatosDuplicados;
import Explorer_World_Api.Model.DTO.ClienteDTO;
import Explorer_World_Api.Model.DTO.ReservacionesDTO;
import Explorer_World_Api.Model.DTO.ServiciosDTO;
import Explorer_World_Api.Service.ClienteService;
import Explorer_World_Api.Service.Servicios_Service;
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
@RequestMapping("/ApiServicios")
public class ServiciosController {
    @Autowired
    private Servicios_Service service;

    //-----------Paginacion
    @GetMapping("/ListaServicios")
    private ResponseEntity<Page<ServiciosDTO>> getDataProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size
    ){
        if (size <= 0 || size > 50){
            ResponseEntity.badRequest().body(Map.of(
                    "status", "El tamaño de la página debe estar entre 1 y 50"
            ));
            return ResponseEntity.ok(null);
        }
        Page<ServiciosDTO> categories = service.PaginacionServicio(page, size);
        if (categories == null){
            ResponseEntity.badRequest().body(Map.of(
                    "status", "No hay registros"
            ));
        }
        return ResponseEntity.ok(categories);
    }

    // ------------ CONSULTAR DATOS
    @GetMapping("/consultarServicio")
    public ResponseEntity<List<ServiciosDTO>> ObtenerServicio() {
        return ResponseEntity.ok(service.ObtenerServicio());
    }



    // ------------ INSERCIÓN DE DATOS
    @PostMapping("/registrarServicio")
    public ResponseEntity<?> AgregarServicio(@Valid @RequestBody ServiciosDTO json, BindingResult bindingResult, HttpServletRequest request) {
        if (bindingResult.hasErrors()) {
            Map<String, String> errores = new HashMap<>();
            bindingResult.getFieldErrors().forEach(error ->
                    errores.put(error.getField(), error.getDefaultMessage()));
            return ResponseEntity.badRequest().body(errores);
        }

        try {
            ServiciosDTO respuesta = service.AgregarServicio(json);
            return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                    "status", "Registrado",
                    "data", respuesta
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of(
                            "status", "Error",
                            "message", "Error al registrar cliente",
                            "detail", e.getMessage()
                    ));
        }
    }

    @PutMapping("/editarServicio/{id}")
    public ResponseEntity<?> ActualizarServicio(
            @PathVariable Long id,
            @Valid @RequestBody ServiciosDTO data,
            BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            Map<String, String> errores = new HashMap<>();
            bindingResult.getFieldErrors().forEach(error ->
                    errores.put(error.getField(), error.getDefaultMessage()));
            return ResponseEntity.badRequest().body(errores);
        }

        try {
            ServiciosDTO dto = service.ActualizarServicio(id, data);
            return ResponseEntity.ok(Map.of(
                    "mensaje", "Registro actualizado",
                    "data", dto
            ));
        } catch (ExcepcionServicioNoRegistrado e) {
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

    @DeleteMapping("/eliminarServicio/{id}")
    public ResponseEntity<?> eliminarServicio(@PathVariable Long id) {
        try {
            if (!service.BorrarServicio(id)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of(
                                "Error", "NOT FOUND",
                                "Mensaje", "El servicio no fue encontrado",
                                "timestamp", Instant.now().toString()
                        ));
            }
            return ResponseEntity.ok().body(Map.of(
                    "status", "Success",
                    "message", "Servicio a sido eliminado exitosamente"
            ));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of(
                    "status", "Error",
                    "message", "Error al eliminar servicio",
                    "details", e.getMessage()
            ));
        }
    }
}
