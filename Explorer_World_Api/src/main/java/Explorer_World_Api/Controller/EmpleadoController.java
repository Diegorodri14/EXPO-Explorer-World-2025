package Explorer_World_Api.Controller;

import Explorer_World_Api.Entities.DestinosEntity;
import Explorer_World_Api.Entities.EmpleadoEntity;
import Explorer_World_Api.Exceptions.ExcepcionEmpleadoNoRegistrado;
import Explorer_World_Api.Exceptions.ExceptionDatosDuplicados;
import Explorer_World_Api.Model.DTO.DestinosDTO;
import Explorer_World_Api.Model.DTO.EmpleadoDTO;
import Explorer_World_Api.Repositories.DestinosRepository;
import Explorer_World_Api.Repositories.EmpleadoRepository;
import Explorer_World_Api.Service.Cloudinary.CloudinaryService;
import Explorer_World_Api.Service.EmpleadoService;
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
@RequestMapping("/ApiEmpleado")
public class EmpleadoController {
    @Autowired
    private EmpleadoService service;
    @Autowired
    private CloudinaryService cloudinaryService;
    @Autowired
    private EmpleadoRepository empleadoRepository;

    //-----------Paginacion
    @GetMapping("/ListaEmpleado")
    private ResponseEntity<Page<EmpleadoDTO>> getDataProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size
    ){
        if (size <= 0 || size > 50){
            ResponseEntity.badRequest().body(Map.of(
                    "status", "El tamaño de la página debe estar entre 1 y 50"
            ));
            return ResponseEntity.ok(null);
        }
        Page<EmpleadoDTO> categories = service.PaginacionEmpleado(page, size);
        if (categories == null){
            ResponseEntity.badRequest().body(Map.of(
                    "status", "No hay registros"
            ));
        }
        return ResponseEntity.ok(categories);
    }


    // ------------ CONSULTAR DATOS
    @GetMapping("/consultarEmpleado")
    public ResponseEntity<List<EmpleadoDTO>> ObtenerEmpleado() {
        return ResponseEntity.ok(service.ObtenerEmpleado());
    }



    // ------------ INSERCIÓN DE DATOS
    @PostMapping("/registrarEmpleado")
    public ResponseEntity<?> AgregarEmpleado(@Valid @RequestBody EmpleadoDTO json, BindingResult bindingResult, HttpServletRequest request) {
        if (bindingResult.hasErrors()) {
            Map<String, String> errores = new HashMap<>();
            bindingResult.getFieldErrors().forEach(error ->
                    errores.put(error.getField(), error.getDefaultMessage()));
            return ResponseEntity.badRequest().body(errores);
        }

        try {
            EmpleadoDTO respuesta = service.AgregarEmpleado(json);

            return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                    "status", "Registrado",
                    "data", respuesta
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of(
                            "status", "Error",
                            "message", "Error al registrar Empleado",
                            "detail", e.getMessage()
                    ));
        }
    }

    @PutMapping("/editarEmpleado/{id}")
    public ResponseEntity<?> ActualizarEmpleado(
            @PathVariable Long id,
            @Valid @RequestBody EmpleadoDTO data,
            BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            Map<String, String> errores = new HashMap<>();
            bindingResult.getFieldErrors().forEach(error ->
                    errores.put(error.getField(), error.getDefaultMessage()));
            return ResponseEntity.badRequest().body(errores);
        }

        try {
            EmpleadoDTO dto = service.ActualizarEmpleado(id, data);
            return ResponseEntity.ok(Map.of(
                    "mensaje", "Registro actualizado",
                    "data", dto
            ));
        } catch (ExcepcionEmpleadoNoRegistrado e) {
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
                    "message", "Error al actualizar empleado",
                    "details", e.getMessage()
            ));
        }
    }

    @DeleteMapping("/eliminarEmpleado/{id}")
    public ResponseEntity<?> eliminarViaje(@PathVariable Long id) {
        try {
            if (!service.BorrarEmpleado(id)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of(
                                "Error", "NOT FOUND",
                                "Mensaje", "El Empleado no fue encontrado",
                                "timestamp", Instant.now().toString()
                        ));
            }
            return ResponseEntity.ok().body(Map.of(
                    "status", "Success",
                    "message", "Empleado a sido eliminado exitosamente"
            ));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of(
                    "status", "Error",
                    "message", "Error al eliminar el Empleado",
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
            EmpleadoEntity empleado = empleadoRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Empleado no encontrado"));

            // Subir imagen a la carpeta "EmpleadosExpo"
            String imageUrl = cloudinaryService.uploadImage(file, "EmpleadosExpo");

            empleado.setImage_url(imageUrl);
            empleadoRepository.save(empleado);

            return ResponseEntity.ok(Map.of(
                    "message", "Imagen subida y asociada al empleado exitosamente",
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
