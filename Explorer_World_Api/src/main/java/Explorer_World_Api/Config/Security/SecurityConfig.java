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
import org.springframework.web.cors.CorsConfigurationSource;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtCookieAuthFilter jwtCookieAuthFilter;
    private final CorsConfigurationSource corsConfigurationSource;

    public SecurityConfig(JwtCookieAuthFilter jwtCookieAuthFilter,
                          CorsConfigurationSource corsConfigurationSource) {
        this.jwtCookieAuthFilter = jwtCookieAuthFilter;
        this.corsConfigurationSource = corsConfigurationSource;
    }

    // Configuración de seguridad HTTP
    @Bean
    public SecurityFilterChain filterChain (HttpSecurity http) throws Exception{
        http
                .csrf(csrf -> csrf.disable())
                .cors(cors -> cors.configurationSource(corsConfigurationSource)) // ← Configura CORS aquí
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll() // ← Permite preflight requests
                        .requestMatchers(HttpMethod.POST,"/api/auth/login").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/auth/logout").permitAll()
                        .requestMatchers("/api/auth/me").authenticated()

                        //ENDPOINTS USUARIOS
                        .requestMatchers(HttpMethod.GET, "/apiUsuario/ListaUsuario").authenticated()
                        .requestMatchers(HttpMethod.POST, "/apiUsuario/registrarUsario").hasAuthority("ROLE_admin")
                        .requestMatchers(HttpMethod.PUT, "/apiUsuario/editarUsuario/{id}").hasAuthority("ROLE_admin")
                        .requestMatchers(HttpMethod.DELETE, "/apiUsuario/eliminarBoleto/{id}").hasAuthority("ROLE_admin")

                        //ENDPOINTS VIAJES
                        .requestMatchers(HttpMethod.GET, "/ApiViajes/ListaViajes").authenticated()
                        .requestMatchers(HttpMethod.POST, "/ApiViajes/registrarViaje").hasAuthority("ROLE_admin")
                        .requestMatchers(HttpMethod.PUT, "/ApiViajes/editarViaje/{id}").hasAuthority("ROLE_admin")
                        .requestMatchers(HttpMethod.DELETE, "/ApiViajes/eliminarViaje/{id}").hasAuthority("ROLE_admin")


                        //ENDPOINTS EMPLEADOS
                        .requestMatchers(HttpMethod.GET, "/ApiEmpleado/ListaEmpleado").authenticated()
                        .requestMatchers(HttpMethod.POST, "/ApiEmpleado/registrarEmpleado").hasAuthority("ROLE_admin")
                        .requestMatchers(HttpMethod.PUT, "/ApiEmpleado/editarEmpleado/{id}").hasAuthority("ROLE_admin")
                        .requestMatchers(HttpMethod.DELETE, "/ApiEmpleado/eliminarEmpleado/{id}").hasAuthority("ROLE_admin")


                        //ENDPOINTS DESTINOS
                        .requestMatchers(HttpMethod.GET, "/ApiDestino/ListaDestino").authenticated()
                        .requestMatchers(HttpMethod.POST, "/ApiDestino/registrarDestino").hasAuthority("ROLE_admin")
                        .requestMatchers(HttpMethod.PUT, "/ApiDestino/editarDestino/{id}").hasAuthority("ROLE_admin")
                        .requestMatchers(HttpMethod.DELETE, "/ApiDestino/eliminarDestino/{id}").hasAuthority("ROLE_admin")

                        //ENDPOINTS FACTURAS
                        .requestMatchers(HttpMethod.GET, "/ApiFactura/ListaFactura").authenticated()
                        .requestMatchers(HttpMethod.POST, "/ApiFactura/registrarFactura").hasAuthority("ROLE_admin")
                        .requestMatchers(HttpMethod.PUT, "/ApiFactura/editarFactura/{id}").hasAuthority("ROLE_admin")
                        .requestMatchers(HttpMethod.DELETE, "/ApiFactura/eliminarFactura/{id}").hasAuthority("ROLE_admin")


                        .requestMatchers("/api/test/admin-only").hasRole("admin")
                        .requestMatchers("/api/test/cliente-only").hasRole("Cliente")
                        .anyRequest().authenticated())


                .sessionManagement(sess -> sess
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtCookieAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }


    // Exponer el AuthenticationManager como bean
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
