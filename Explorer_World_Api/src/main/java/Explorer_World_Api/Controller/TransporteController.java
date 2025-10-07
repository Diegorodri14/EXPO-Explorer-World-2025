package Explorer_World_Api.Controller;

import Explorer_World_Api.Entities.EventosEntity;
import Explorer_World_Api.Entities.TransporteEntity;
import Explorer_World_Api.Exceptions.ExceptionDatosDuplicados;
import Explorer_World_Api.Exceptions.ExceptionTransporteNoEncontrado;
import Explorer_World_Api.Model.DTO.ServiciosDTO;
import Explorer_World_Api.Model.DTO.TransporteDTO;
import Explorer_World_Api.Repositories.EventosRepository;
import Explorer_World_Api.Repositories.TransporteRepository;
import Explorer_World_Api.Service.Cloudinary.CloudinaryService;
import Explorer_World_Api.Service.TransporteService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/ApiTransporte")
public class TransporteController {

    @Autowired
    TransporteService service;
    @Autowired
    private CloudinaryService cloudinaryService;
    @Autowired
    private TransporteRepository transporteRepository;

    //-----------Paginacion
    @GetMapping("/ListaTransporte")
    private ResponseEntity<Page<TransporteDTO>> getDataProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size
    ){
        if (size <= 0 || size > 50){
            ResponseEntity.badRequest().body(Map.of(
                    "status", "El tamaño de la página debe estar entre 1 y 50"
            ));
            return ResponseEntity.ok(null);
        }
        Page<TransporteDTO> categories = service.PaginacionTransporte(page, size);
        if (categories == null){
            ResponseEntity.badRequest().body(Map.of(
                    "status", "No hay registros"
            ));
        }
        return ResponseEntity.ok(categories);
    }

    //Obtener datos
    @GetMapping("/ObtenerTransportes")
    public List<TransporteDTO> ObtenerTransporte(){
        return service.ObtenerTransporte();
    }

    //AGREGAR DATOS
    @PostMapping("/AgregarTransportes")
    public ResponseEntity<?> NuevoTransporte(@Valid @RequestBody TransporteDTO json, HttpServletRequest request) {
        try {

            TransporteDTO respuesta = service.AgregarTransporte(json);
            if (respuesta == null) {
                return ResponseEntity.badRequest().body(Map.of(
                        "status", "Insercion fallida",
                        "ErrorType", "VALIDATION ERROR",
                        "message", "Los datos no pudieron ser ingresado"
                ));
            }
            return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                    "status", "success",
                    "data", respuesta
            ));

        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "status","ERROR",
                    "message","Error no controlado",
                    "detail", e.getMessage()
            ));
        }
    }
    @PutMapping("/ActualizarTransportes/{id}")
    public ResponseEntity<?> modificarTransporte(
            @PathVariable Long id,
            @Valid @RequestBody TransporteDTO json,
            BindingResult result
    ){
        if (result.hasErrors()){
                Map<String, String> errores = new HashMap<>();
                result.getFieldErrors().forEach(error ->
                        errores.put(error.getField(), error.getDefaultMessage()));
                return ResponseEntity.badRequest().body(errores);
        }
        try {
            TransporteDTO dto = service.ActualizarTransporte(id, json);
            return ResponseEntity.ok(dto);
        }catch (ExceptionTransporteNoEncontrado e){
            return ResponseEntity.notFound().build();
        }
        catch (ExceptionDatosDuplicados e){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of(
                    "status", "ERROR",
                    "message", "Datos duplicados",
                    "Campo", e.getCampoDuplicado()
            ));
        }
    }
    @DeleteMapping("/EliminarTransportes/{id}")
    public ResponseEntity<?> EliminarTransporte(@PathVariable Long id){
        try{
            if (!service.BorrarTransporte(id)){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).header("Mensaje de error", "Transporte no encontrao").body(Map.of(
                        "status", "Error",
                        "Mensaje", "El transporte no fue encontrado",
                        "Timestamp", Instant.now().toString())

                );
            }
            return ResponseEntity.ok().body(Map.of(
                    "status", "Proceso completado",
                    "message", "Transporte eliminado"
            ));
        }catch (Exception e){
            return ResponseEntity.internalServerError().body(Map.of(
                    "status", "error",
                    "message", "Error al eliminar al usuario",
                    "detail", e.getMessage()
            ));
        }
    }

    @PostMapping("/{id}/upload-image")
    public ResponseEntity<?> uploadImageToEmpleados(
            @PathVariable Long id,
            @RequestParam("image") MultipartFile file
    ) {
        try {
            TransporteEntity transporte = transporteRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Transporte no encontrado"));

            // Subir imagen a la carpeta "TransporteExpo"
            String imageUrl = cloudinaryService.uploadImage(file, "TransporteExpo");

            transporte.setImage_url(imageUrl);
            transporteRepository.save(transporte);

            return ResponseEntity.ok(Map.of(
                    "message", "Imagen subida y asociada al transporte exitosamente",
                    "url", imageUrl
            ));
        } catch (IOException e) {
            e.printStackTrace(); // Esto imprime la traza completa en la consola
            return ResponseEntity.internalServerError()
                    .body("Error al subir la imagen: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Archivo inválido: " + e.getMessage());
        }
    }
}
