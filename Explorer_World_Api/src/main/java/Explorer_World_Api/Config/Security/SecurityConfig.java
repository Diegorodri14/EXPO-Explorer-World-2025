package Explorer_World_Api.Config.Security;

import Explorer_World_Api.Utils.JwtCookieAuthFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtCookieAuthFilter jwtCookieAuthFilter;

    public SecurityConfig(JwtCookieAuthFilter jwtCookieAuthFilter) {
        this.jwtCookieAuthFilter = jwtCookieAuthFilter;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/auth/login").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/auth/logout").permitAll()
                        .requestMatchers("/api/auth/me").authenticated()

                        // ENDPOINTS DE USUARIO
                        .requestMatchers(HttpMethod.GET, "/apiUsuario/ListaUsuario").authenticated()
                        .requestMatchers(HttpMethod.POST, "/apiUsuario/registrarUsario").hasAuthority("ROLE_admin")
                        .requestMatchers(HttpMethod.PUT, "/apiUsuario/editarUsuario/{id}").hasAuthority("ROLE_admin")
                        .requestMatchers(HttpMethod.DELETE, "/apiUsuario/eliminarBoleto/{id}").hasAuthority("ROLE_admin")

                        // ENDPOINTS DE EMPLEADO
                        .requestMatchers(HttpMethod.GET, "/ApiEmpleado/ListaEmpleado").authenticated()
                        .requestMatchers(HttpMethod.GET, "/ApiEmpleado/consultarEmpleado").authenticated()
                        .requestMatchers(HttpMethod.POST, "/ApiEmpleado/registrarEmpleado").hasAuthority("ROLE_admin")
                        .requestMatchers(HttpMethod.PUT, "/ApiEmpleado/editarEmpleado/{id}").hasAuthority("ROLE_admin")
                        .requestMatchers(HttpMethod.DELETE, "/ApiEmpleado/eliminarEmpleado/{id}").hasAuthority("ROLE_admin")
                        .requestMatchers(HttpMethod.POST, "/ApiEmpleado/{id}/upload-image").hasAuthority("ROLE_admin")

                        // ENDPOINTS DE VIAJES
                        .requestMatchers(HttpMethod.GET, "/ApiViajes/ListaViajes").authenticated()
                        .requestMatchers(HttpMethod.GET, "/ApiViajes/consultarViaje").authenticated()
                        .requestMatchers(HttpMethod.POST, "/ApiViajes/registrarViaje").hasAuthority("ROLE_admin")
                        .requestMatchers(HttpMethod.PUT, "/ApiViajes/editarViaje/{id}").hasAuthority("ROLE_admin")
                        .requestMatchers(HttpMethod.DELETE, "/ApiViajes/eliminarViaje/{id}").hasAuthority("ROLE_admin")

                        // ENDPOINTS DE DESTINOS
                        .requestMatchers(HttpMethod.GET, "/ApiDestinos/ListaDestino").authenticated()
                        .requestMatchers(HttpMethod.GET, "/ApiDestinos/consultarDestinos").authenticated()
                        .requestMatchers(HttpMethod.POST, "/ApiDestinos/registrarDestinos").hasAuthority("ROLE_admin")
                        .requestMatchers(HttpMethod.PUT, "/ApiDestinos/editarDestino/{id}").hasAuthority("ROLE_admin")
                        .requestMatchers(HttpMethod.DELETE, "/ApiDestinos/eliminarDestino/{id}").hasAuthority("ROLE_admin")
                        .requestMatchers(HttpMethod.POST, "/ApiDestinos/{id}/upload-image").hasAuthority("ROLE_admin")

                        // ENDPOINTS DE FACTURAS
                        .requestMatchers(HttpMethod.GET, "/ApiFacturas/ListaFactura").authenticated()
                        .requestMatchers(HttpMethod.GET, "/ApiFacturas/consultarFactura").authenticated()
                        .requestMatchers(HttpMethod.POST, "/ApiFacturas/registrarFactura").hasAuthority("ROLE_admin")
                        .requestMatchers(HttpMethod.PUT, "/ApiFacturas/editarFactura/{id}").hasAuthority("ROLE_admin")
                        .requestMatchers(HttpMethod.DELETE, "/ApiFacturas/eliminarFactura/{id}").hasAuthority("ROLE_admin")

                        // ENDPOINTS DE RESERVACIONES
                        .requestMatchers(HttpMethod.GET, "/ApiReservaciones/ListaReservaciones").authenticated()
                        .requestMatchers(HttpMethod.GET, "/ApiReservaciones/ObtenerReservacion").authenticated()
                        .requestMatchers(HttpMethod.POST, "/ApiReservaciones/AgregarReservacion").hasAuthority("ROLE_admin")
                        .requestMatchers(HttpMethod.PUT, "/ApiReservaciones/EditarReservacion/{id}").hasAuthority("ROLE_admin")
                        .requestMatchers(HttpMethod.DELETE, "/ApiReservaciones/EliminarReservacion/{id}").hasAuthority("ROLE_admin")

                        // ENDPOINTS DE SERVICIOS
                        .requestMatchers(HttpMethod.GET, "/ApiServicios/ListaServicios").authenticated()
                        .requestMatchers(HttpMethod.GET, "/ApiServicios/consultarServicio").authenticated()
                        .requestMatchers(HttpMethod.POST, "/ApiServicios/registrarServicio").hasAuthority("ROLE_admin")
                        .requestMatchers(HttpMethod.PUT, "/ApiServicios/editarServicio/{id}").hasAuthority("ROLE_admin")
                        .requestMatchers(HttpMethod.DELETE, "/ApiServicios/eliminarServicio/{id}").hasAuthority("ROLE_admin")

                        // ENDPOINTS DE CLIENTES
                        .requestMatchers(HttpMethod.GET, "/ApiCliente/ListaClientes").authenticated()
                        .requestMatchers(HttpMethod.GET, "/ApiCliente/consultarCliente").authenticated()
                        .requestMatchers(HttpMethod.POST, "/ApiCliente/registrarCliente").hasAuthority("ROLE_admin")
                        .requestMatchers(HttpMethod.PUT, "/ApiCliente/editarCliente/{id}").hasAuthority("ROLE_admin")
                        .requestMatchers(HttpMethod.DELETE, "/ApiCliente/eliminarCliente/{id}").hasAuthority("ROLE_admin")

                        .anyRequest().authenticated())
                .sessionManagement(sess -> sess
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtCookieAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowCredentials(true);
        configuration.setAllowedOriginPatterns(Arrays.asList("*"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setExposedHeaders(Arrays.asList("Authorization"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}