package Explorer_World_Api.Controller.Auth;

import Explorer_World_Api.Entities.UsuarioEntity;
import Explorer_World_Api.Model.DTO.UsuarioDTO;
import Explorer_World_Api.Service.Auth.AuthService;
import Explorer_World_Api.Utils.JWTUtils;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService service;

    @Autowired
    private JWTUtils jwtUtils;

    @PostMapping("/login")
    private ResponseEntity<?> login(@Valid @RequestBody UsuarioDTO data, HttpServletResponse response){
        System.out.println("=== INICIO LOGIN ===");
        System.out.println("Correo recibido: " + data.getCorreo());

        //1. Verificar que los datos no esten vacios
        if (data.getCorreo() == null || data.getCorreo().isBlank() ||
                data.getContrasena() == null || data.getContrasena().isBlank())  {
            System.out.println("ERROR: Credenciales vacías");
            return ResponseEntity.status(401).body("Error: Credenciales incompletas");
        }

        try {
            //Enviar los datos al metodo login contenido en el service
            boolean loginSuccess = service.Login(data.getCorreo(), data.getContrasena());
            System.out.println("Resultado login: " + loginSuccess);

            if(loginSuccess){
                return addTokenCookieAndResponse(response, data.getCorreo());
            }
            System.out.println("Login fallido - credenciales incorrectas");
            return ResponseEntity.status(401).body("Credenciales incorrectas");
        } catch (Exception e) {
            System.out.println("EXCEPCIÓN en login: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error interno del servidor");
        }
    }

    /**
     * Se genera el token y se devuelve en JSON
     */
    private ResponseEntity<?> addTokenCookieAndResponse(HttpServletResponse response, String correo) {
        // Obtener el usuario completo de la base de datos
        Optional<UsuarioEntity> userOpt = service.obtenerUsuario(correo);

        if (userOpt.isPresent()) {
            UsuarioEntity user = userOpt.get();
            String token = jwtUtils.create(
                    String.valueOf(user.getIdUsuario()),
                    user.getCorreo(),
                    user.getRangoUsuario().getNombreRango()
            );

            // Crear cookie
            Cookie cookie = new Cookie("authToken", token);
            cookie.setHttpOnly(true);
            cookie.setSecure(false); // Cambia a true en producción
            cookie.setPath("/");
            cookie.setMaxAge(86400);
            response.addCookie(cookie);

            // Crear respuesta JSON
            Map<String, Object> responseBody = new HashMap<>();
            responseBody.put("token", token);
            responseBody.put("message", "Inicio de sesión exitoso");
            responseBody.put("usuario", Map.of(
                    "idUsuario", user.getIdUsuario(),
                    "usuario", user.getUsuario(),
                    "correo", user.getCorreo(),
                    "estado", user.getEstado(),
                    "idRango", user.getRangoUsuario().getIdRango(), // Ajusta según tu entidad
                    "rol", user.getRangoUsuario().getNombreRango()
            ));

            return ResponseEntity.ok(responseBody);
        }

        return ResponseEntity.status(404).body("Usuario no encontrado");
    }

    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(Authentication authentication) {
        try {
            if (authentication == null || !authentication.isAuthenticated()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of(
                                "authenticated", false,
                                "message", "No autenticado"
                        ));
            }

            // Manejar diferentes tipos de Principal
            String username;
            Collection<? extends GrantedAuthority> authorities;

            if (authentication.getPrincipal() instanceof UserDetails) {
                UserDetails userDetails = (UserDetails) authentication.getPrincipal();
                username = userDetails.getUsername();
                authorities = userDetails.getAuthorities();
            } else {
                username = authentication.getName();
                authorities = authentication.getAuthorities();
            }

            Optional<UsuarioEntity> userOpt = service.obtenerUsuario(username);

            if (userOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of(
                                "authenticated", false,
                                "message", "Usuario no encontrado"
                        ));
            }

            UsuarioEntity user = userOpt.get();

            return ResponseEntity.ok(Map.of(
                    "authenticated", true,
                    "user", Map.of(
                            "id", user.getIdUsuario(),
                            "correo", user.getCorreo(),
                            "rol", user.getRangoUsuario().getNombreRango(),
                            "usuario", user.getUsuario(),
                            "authorities", authorities.stream()
                                    .map(GrantedAuthority::getAuthority)
                                    .collect(Collectors.toList())
                    )
            ));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of(
                            "authenticated", false,
                            "message", "Error obteniendo datos de usuario"
                    ));
        }
    }
}