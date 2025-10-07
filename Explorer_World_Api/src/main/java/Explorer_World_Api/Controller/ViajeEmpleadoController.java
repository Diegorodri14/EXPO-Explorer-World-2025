package Explorer_World_Api.Controller;

import Explorer_World_Api.Exceptions.ExcepcionViajeEmpleadoNoRegistrado;
import Explorer_World_Api.Exceptions.ExceptionDatosDuplicados;
import Explorer_World_Api.Model.DTO.ViajeEmpleadoDTO;
import Explorer_World_Api.Service.ViajeEmpleadoService;
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
@RequestMapping("/ApiViajeEmpleado")
public class ViajeEmpleadoController {
    @Autowired
    private ViajeEmpleadoService service;

    //-----------Paginacion
    @GetMapping("/ListaViajeEmpleado")
    private ResponseEntity<Page<ViajeEmpleadoDTO>> getDataViajeEmpleado(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size
    ){
        if (size <= 0 || size > 50){
            return ResponseEntity.badRequest().body(null);
        }
        Page<ViajeEmpleadoDTO> viajeEmpleados = service.PaginacionViajeEmpleado(page, size);
        if (viajeEmpleados == null || viajeEmpleados.isEmpty()){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(viajeEmpleados);
    }

    // ------------ CONSULTAR DATOS
    @GetMapping("/consultarViajeEmpleado")
    public ResponseEntity<List<ViajeEmpleadoDTO>> ObtenerViajeEmpleado() {
        List<ViajeEmpleadoDTO> resultados = service.ObtenerViajeEmpleado();
        if (resultados.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(resultados);
    }

    // ------------ INSERCIÓN DE DATOS
    @PostMapping("/registrarViajeEmpleado")
    public ResponseEntity<?> AgregarViajeEmpleado(@Valid @RequestBody ViajeEmpleadoDTO json, BindingResult bindingResult, HttpServletRequest request) {
        if (bindingResult.hasErrors()) {
            Map<String, String> errores = new HashMap<>();
            bindingResult.getFieldErrors().forEach(error ->
                    errores.put(error.getField(), error.getDefaultMessage()));
            return ResponseEntity.badRequest().body(errores);
        }

        try {
            ViajeEmpleadoDTO respuesta = service.AgregarViajeEmpleado(json);
            return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                    "status", "Registrado",
                    "data", respuesta
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of(
                            "status", "Error",
                            "message", "Error al registrar el ViajeEmpleado",
                            "detail", e.getMessage()
                    ));
        }
    }

    @PutMapping("/editarViajeEmpleado/{id}")
    public ResponseEntity<?> ActualizarViajeEmpleado(
            @PathVariable Long id,
            @Valid @RequestBody ViajeEmpleadoDTO data,
            BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            Map<String, String> errores = new HashMap<>();
            bindingResult.getFieldErrors().forEach(error ->
                    errores.put(error.getField(), error.getDefaultMessage()));
            return ResponseEntity.badRequest().body(errores);
        }

        try {
            ViajeEmpleadoDTO dto = service.ActualizarViajeEmpleado(id, data);
            return ResponseEntity.ok(Map.of(
                    "mensaje", "Registro actualizado",
                    "data", dto
            ));
        } catch (ExcepcionViajeEmpleadoNoRegistrado e) {
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
                    "message", "Error al actualizar el ViajeEmpleado",
                    "details", e.getMessage()
            ));
        }
    }

    @DeleteMapping("/eliminarViajeEmpleado/{id}")
    public ResponseEntity<?> eliminarViajeEmpleado(@PathVariable Long id) {
        try {
            if (!service.BorrarViajeEmpleado(id)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of(
                                "Error", "NOT FOUND",
                                "Mensaje", "ViajeEmpleado no fue encontrado",
                                "timestamp", Instant.now().toString()
                        ));
            }
            return ResponseEntity.ok().body(Map.of(
                    "status", "Success",
                    "message", "Registro del ViajeEmpleado ha sido eliminado exitosamente"
            ));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of(
                    "status", "Error",
                    "message", "Error al eliminar el ViajeEmpleado",
                    "details", e.getMessage()
            ));
        }
    }
}
