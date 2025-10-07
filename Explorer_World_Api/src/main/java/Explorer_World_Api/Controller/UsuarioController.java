package Explorer_World_Api.Controller;

import Explorer_World_Api.Entities.TransporteEntity;
import Explorer_World_Api.Entities.UsuarioEntity;
import Explorer_World_Api.Exceptions.ExcepcionUsuarioNoRegistrado;
import Explorer_World_Api.Exceptions.ExceptionDatosDuplicados;
import Explorer_World_Api.Model.DTO.TransporteDTO;
import Explorer_World_Api.Model.DTO.UsuarioDTO;
import Explorer_World_Api.Repositories.TransporteRepository;
import Explorer_World_Api.Repositories.UsuarioRepository;
import Explorer_World_Api.Service.Cloudinary.CloudinaryService;
import Explorer_World_Api.Service.UsuarioService;
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
@RequestMapping("/apiUsuario")
public class UsuarioController {

    @Autowired
    UsuarioService service;
    @Autowired
    private CloudinaryService cloudinaryService;
    @Autowired
    private UsuarioRepository usuarioRepository;

    //-----------Paginacion
    @GetMapping("/ListaUsuario")
    private ResponseEntity<Page<UsuarioDTO>> getDataProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size
    ){
        if (size <= 0 || size > 50){
            ResponseEntity.badRequest().body(Map.of(
                    "status", "El tamaño de la página debe estar entre 1 y 50"
            ));
            return ResponseEntity.ok(null);
        }
        Page<UsuarioDTO> categories = service.PaginacionUsario(page, size);
        if (categories == null){
            ResponseEntity.badRequest().body(Map.of(
                    "status", "No hay registros"
            ));
        }
        return ResponseEntity.ok(categories);
    }

    // ------------ CONSULTAR DATOS
    @GetMapping("/consultarUsuario")
    public ResponseEntity<List<UsuarioDTO>> obtenerUsuarios() {
        return ResponseEntity.ok(service.obtenerUsuarios());
    }



    // ------------ INSERCIÓN DE DATOS
    @PostMapping("/registrarUsarios")
    public ResponseEntity<?> nuevoUsuario(@Valid @RequestBody UsuarioDTO json, BindingResult bindingResult, HttpServletRequest request) {
        if (bindingResult.hasErrors()) {
            Map<String, String> errores = new HashMap<>();
            bindingResult.getFieldErrors().forEach(error ->
                    errores.put(error.getField(), error.getDefaultMessage()));
            return ResponseEntity.badRequest().body(errores);
        }

        try {
            UsuarioDTO respuesta = service.nuevoUsuario(json);
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

    @PutMapping("/editarUsuarios/{id}")
    public ResponseEntity<?> modificarUsuario(
            @PathVariable Long id,
            @Valid @RequestBody UsuarioDTO data,
            BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            Map<String, String> errores = new HashMap<>();
            bindingResult.getFieldErrors().forEach(error ->
                    errores.put(error.getField(), error.getDefaultMessage()));
            return ResponseEntity.badRequest().body(errores);
        }

        try {
            UsuarioDTO dto = service.modificarUsuario(id, data);
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
            if (!service.eliminarUsuario(id)) {
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

    @PostMapping("/{id}/upload-image")
    public ResponseEntity<?> uploadImageToEmpleados(
            @PathVariable Long id,
            @RequestParam("image") MultipartFile file
    ) {
        try {
            UsuarioEntity usuario = usuarioRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("usuario no encontrado"));

            // Subir imagen a la carpeta "UsuariosExpo"
            String imageUrl = cloudinaryService.uploadImage(file, "UsuariosExpo");

            usuario.setImage_url(imageUrl);
            usuarioRepository.save(usuario);

            return ResponseEntity.ok(Map.of(
                    "message", "Imagen subida y asociada al usuario exitosamente",
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
