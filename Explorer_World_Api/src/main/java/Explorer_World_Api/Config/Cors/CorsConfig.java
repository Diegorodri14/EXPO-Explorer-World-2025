package Explorer_World_Api.Config.Cors;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class CorsConfig {

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();

        // Configuración esencial para el FrontEnd
        config.setAllowCredentials(true);
        config.addAllowedOrigin("http://localhost"); // Para desarrollo

        // Métodos permitidos
        config.addAllowedMethod("GET");
        config.addAllowedMethod("POST");
        config.addAllowedMethod("PUT");
        config.addAllowedMethod("DELETE");
        config.addAllowedMethod("OPTIONS");

        // Cabeceras permitidas
        config.addAllowedHeader("Origin");          //Le dice al backend desde qué dominio/origen viene la petición.
        config.addAllowedHeader("Content-Type");    //Especifica el tipo de datos que se está enviando.
        config.addAllowedHeader("Accept");          //Le dice al backend qué tipo de respuesta espera el frontend.
        config.addAllowedHeader("Authorization");   //envia tokens de autenticación una vez que el usuario hace login.

        source.registerCorsConfiguration("/**", config); //Aplica a todos los endpoints
        return new CorsFilter(source);
    }
}
