package Explorer_World_Api.Utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Component
public class JwtCookieAuthFilter extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(JwtCookieAuthFilter.class);
    private static final String AUTH_COOKIE_NAME = "authToken";
    private final JWTUtils jwtUtils;

    @Autowired
    public JwtCookieAuthFilter(JWTUtils jwtUtils) {
        this.jwtUtils = jwtUtils;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

        // Permitir todos los endpoints de la API sin autenticación
        String path = request.getRequestURI();

        // Lista de endpoints públicos
        if (isPublicEndpoint(path, request.getMethod())) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            String token = extractTokenFromCookies(request);

            // Si no hay token y es un endpoint protegido, rechazar
            if (token == null || token.isBlank()) {
                log.warn("Token no encontrado para endpoint: {}", path);
                sendError(response, "Token no encontrado", HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }

            // Validar token
            if (!jwtUtils.validate(token)) {
                log.warn("Token inválido para endpoint: {}", path);
                sendError(response, "Token inválido", HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }

            Claims claims = jwtUtils.parseToken(token);
            String rol = jwtUtils.extractRol(token);

            // Crear authorities
            Collection<? extends GrantedAuthority> authorities =
                    Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + rol));

            // Establecer autenticación
            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(
                            claims.getSubject(),
                            null,
                            authorities
                    );

            SecurityContextHolder.getContext().setAuthentication(authentication);
            log.info("Usuario autenticado: {} con rol: {}", claims.getSubject(), rol);

            filterChain.doFilter(request, response);

        } catch (ExpiredJwtException e) {
            log.warn("Token expirado: {}", e.getMessage());
            sendError(response, "Token expirado", HttpServletResponse.SC_UNAUTHORIZED);
        } catch (MalformedJwtException e) {
            log.warn("Token malformado: {}", e.getMessage());
            sendError(response, "Token inválido", HttpServletResponse.SC_FORBIDDEN);
        } catch (Exception e) {
            log.error("Error de autenticación", e);
            sendError(response, "Error de autenticación", HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    private String extractTokenFromCookies(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            // Intentar obtener token del header Authorization como fallback
            String authHeader = request.getHeader("Authorization");
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                return authHeader.substring(7);
            }
            return null;
        }

        return Arrays.stream(cookies)
                .filter(c -> AUTH_COOKIE_NAME.equals(c.getName()))
                .findFirst()
                .map(Cookie::getValue)
                .orElse(null);
    }

    private void sendError(HttpServletResponse response, String message, int status) throws IOException {
        response.setContentType("application/json");
        response.setStatus(status);
        response.getWriter().write(String.format(
                "{\"error\": \"%s\", \"status\": %d, \"message\": \"%s\"}", message, status, message));
    }

    private boolean isPublicEndpoint(String path, String method) {
        // Endpoints públicos
        List<String> publicEndpoints = Arrays.asList(
                "/api/auth/login",
                "/api/auth/register",
                "/api/auth/logout",
                "/error",
                "/favicon.ico"
        );

        // Permitir OPTIONS para CORS
        if ("OPTIONS".equals(method)) {
            return true;
        }

        // Verificar si el path coincide exactamente con algún endpoint público
        return publicEndpoints.stream().anyMatch(endpoint ->
                path.equals(endpoint) || path.startsWith(endpoint + "/")
        );
    }
}