package Explorer_World_Api.Controller;



import Explorer_World_Api.Exceptions.ExceptionDatosDuplicados;
import Explorer_World_Api.Exceptions.ExceptionRangoNoEncontrado;
import Explorer_World_Api.Model.DTO.RangoDTO;
import Explorer_World_Api.Model.DTO.ReservacionesDTO;
import Explorer_World_Api.Service.ReservacionesService;
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
@RequestMapping("/ApiReservaciones")
public class ReservacionesController {

    @Autowired
    ReservacionesService service;

    //-----------Paginacion
    @GetMapping("/ListaReservaciones")
    private ResponseEntity<Page<ReservacionesDTO>> getDataProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size
    ){
        if (size <= 0 || size > 50){
            ResponseEntity.badRequest().body(Map.of(
                    "status", "El tamaño de la página debe estar entre 1 y 50"
            ));
            return ResponseEntity.ok(null);
        }
        Page<ReservacionesDTO> categories = service.PaginacionReservaciones(page, size);
        if (categories == null){
            ResponseEntity.badRequest().body(Map.of(
                    "status", "No hay registros"
            ));
        }
        return ResponseEntity.ok(categories);
    }

    // ------------ CONSULTAR DATOS
    @GetMapping("/ObtenerReservacion")
    public List<ReservacionesDTO> ObtenerReservacion(){
        return service.ObtenerReservacion();
    }

    @PostMapping("/AgregarReservacion")
    public ResponseEntity<?> nuevaReservacion(@Valid @RequestBody ReservacionesDTO json, BindingResult bindingResult, HttpServletRequest request){
        try{
            ReservacionesDTO respuesta  = service.AgregarReservacion(json);
            if (respuesta == null){
                return ResponseEntity.badRequest().body(Map.of(
                        "status", "error",
                        "ErrorType", "VALIDATION_ERROR",
                        "Message", "Los datos no pudieron ser registrados"
                ));
            }
            return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                    "status", "success",
                    "data", respuesta
            ));
        }
        catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "status", "Error",
                    "message", "Error no controlado",
                    "detaill", e.getMessage()
            ));

        }
    }

    @PutMapping("/EditarReservacion/{id}")
    public ResponseEntity<?> ActualizarReservacion(
            @PathVariable Long id,
            @Valid @RequestBody ReservacionesDTO json,
            BindingResult bindingResult){

        if (bindingResult.hasErrors()){
            Map<String, String> errores = new HashMap<>();
            bindingResult.getFieldErrors().forEach(Error -> errores.put(Error.getField(),Error.getDefaultMessage()));
            return ResponseEntity.badRequest().body(errores);
        }
        try{
            ReservacionesDTO dto = service.ActualizarReservacion (id, json);
            return ResponseEntity.ok(dto);
        } catch (ExceptionRangoNoEncontrado e) {
            return ResponseEntity.notFound().build();
        }catch (ExceptionDatosDuplicados e){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(
                    Map.of("Error", "Datos Duplicados",
                            "Campo", e.getCampoDuplicado())
            );
        }

    }
    @DeleteMapping("/EliminarReservacion/{id}")
    public ResponseEntity<?> EliminarReservacion(@PathVariable Long id){
        try {
            if (!service.BorrarReservacion(id)) {
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
