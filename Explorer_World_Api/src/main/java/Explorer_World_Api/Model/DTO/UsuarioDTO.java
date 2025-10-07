package Explorer_World_Api.Model.DTO;

import jakarta.persistence.Column;
import jakarta.validation.constraints.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@EqualsAndHashCode
public class UsuarioDTO {

    private Long idUsuario;

    @Positive(message = "El ID del rango debe ser positivo")
    private Long idRango;


    @Size(max = 20, message = "El estado no puede exceder los 20 caracteres")
    private String estado;


    @Size(max = 50, message = "El usuario no puede exceder los 50 caracteres")
    private String usuario;

    @Size(min = 8, max = 50, message = "La contraseña debe tener entre 8 y 50 caracteres")
    private String contrasena;


    @Email(message = "Debe ser un correo electrónico válido")
    @Size(max = 50, message = "El correo no puede exceder los 50 caracteres")
    private String correo;

    @Column(name  = "IMAGE_URL")
    private String image_url;
}
