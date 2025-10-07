package Explorer_World_Api.Controller;

import Explorer_World_Api.Entities.DestinosEntity;
import Explorer_World_Api.Exceptions.ExcepcionClienteNoRegistrado;
import Explorer_World_Api.Exceptions.ExcepcionDestinoNoRegistrado;
import Explorer_World_Api.Exceptions.ExceptionDatosDuplicados;
import Explorer_World_Api.Model.DTO.ClienteDTO;
import Explorer_World_Api.Model.DTO.DestinosDTO;
import Explorer_World_Api.Repositories.DestinosRepository;
import Explorer_World_Api.Service.ClienteService;
import Explorer_World_Api.Service.Cloudinary.CloudinaryService;
import Explorer_World_Api.Service.DestinosService;
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
@RequestMapping("/ApiDestinos")
public class DestinosController {
    @Autowired
    private DestinosService service;
    @Autowired
    private CloudinaryService cloudinaryService;
    @Autowired
    private DestinosRepository destinosRepository;

    //-----------Paginacion
    @GetMapping("/ListaDestino")
    private ResponseEntity<Page<DestinosDTO>> getDataProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size
    ){
        if (size <= 0 || size > 50){
            ResponseEntity.badRequest().body(Map.of(
                    "status", "El tamaño de la página debe estar entre 1 y 50"
            ));
            return ResponseEntity.ok(null);
        }
        Page<DestinosDTO> categories = service.PaginacionDestino(page, size);
        if (categories == null){
            ResponseEntity.badRequest().body(Map.of(
                    "status", "No hay registros"
            ));
        }
        return ResponseEntity.ok(categories);
    }

    // ------------ CONSULTAR DATOS
    @GetMapping("/consultarDestinos")
    public ResponseEntity<List<DestinosDTO>> ObtenerDestinos() {
        return ResponseEntity.ok(service.ObtenerDestinos());
    }



    // ------------ INSERCIÓN DE DATOS
    @PostMapping("/registrarDestinos")
    public ResponseEntity<?> AgregarDestinos(@Valid @RequestBody DestinosDTO json, BindingResult bindingResult, HttpServletRequest request) {
        if (bindingResult.hasErrors()) {
            Map<String, String> errores = new HashMap<>();
            bindingResult.getFieldErrors().forEach(error ->
                    errores.put(error.getField(), error.getDefaultMessage()));
            return ResponseEntity.badRequest().body(errores);
        }

        try {
            DestinosDTO respuesta = service.AgregarDestinos(json);
            return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                    "status", "Registrado",
                    "data", respuesta
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of(
                            "status", "Error",
                            "message", "Error al registrar Destino",
                            "detail", e.getMessage()
                    ));
        }
    }

    @PutMapping("/editarDestino/{id}")
    public ResponseEntity<?> ActualizarDestino(
            @PathVariable Long id,
            @Valid @RequestBody DestinosDTO data,
            BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            Map<String, String> errores = new HashMap<>();
            bindingResult.getFieldErrors().forEach(error ->
                    errores.put(error.getField(), error.getDefaultMessage()));
            return ResponseEntity.badRequest().body(errores);
        }

        try {
            DestinosDTO dto = service.ActualizarDestino(id, data);
            return ResponseEntity.ok(Map.of(
                    "mensaje", "Registro actualizado",
                    "data", dto
            ));
        } catch (ExcepcionDestinoNoRegistrado e) {
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
                    "message", "Error al actualizar el Destino",
                    "details", e.getMessage()
            ));
        }
    }

    @DeleteMapping("/eliminarDestino/{id}")
    public ResponseEntity<?> eliminarDestino(@PathVariable Long id) {
        try {
            if (!service.BorrarDestino(id)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of(
                                "Error", "NOT FOUND",
                                "Mensaje", "El Destino no fue encontrado",
                                "timestamp", Instant.now().toString()
                        ));
            }
            return ResponseEntity.ok().body(Map.of(
                    "status", "Success",
                    "message", "Destino a sido eliminado exitosamente"
            ));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of(
                    "status", "Error",
                    "message", "Error al eliminar el Destino",
                    "details", e.getMessage()
            ));
        }
    }

    @PostMapping("/{id}/upload-image")
    public ResponseEntity<?> uploadImageToDestino(
            @PathVariable Long id,
            @RequestParam("image") MultipartFile file
    ) {
        try {
            DestinosEntity destino = destinosRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Destino no encontrado"));

            // Subir imagen a la carpeta "DestinosExpo"
            String imageUrl = cloudinaryService.uploadImage(file, "DestinosExpo");

            destino.setImage_url(imageUrl);
            destinosRepository.save(destino);

            return ResponseEntity.ok(Map.of(
                    "message", "Imagen subida y asociada al destino exitosamente",
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
