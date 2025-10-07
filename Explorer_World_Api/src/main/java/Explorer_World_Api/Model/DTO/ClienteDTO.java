package Explorer_World_Api.Model.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@EqualsAndHashCode
@ToString
public class ClienteDTO {
    private Long idCliente;

    @NotBlank(message = "El nombre del cliente no puede estar vacío")
    @Size(max = 100, message = "El nombre no puede exceder los 100 caracteres")
    private String nombreCliente;

    @NotBlank(message = "El apellido del cliente no puede estar vacío")
    @Size(max = 100, message = "El apellido no puede exceder los 100 caracteres")
    private String apellidoCliente;

    @NotBlank(message = "El email del cliente no puede estar vacío")
    @Size(max = 100, message = "El email no puede exceder los 100 caracteres")
    @Pattern(regexp = "^[A-Za-z0-9+_.-]+@(.+)$", message = "El email debe tener un formato válido")
    private String emailCliente;

    @NotBlank(message = "El teléfono no puede estar vacío")
    @Size(max = 20, message = "El teléfono no puede exceder los 20 caracteres")
    private String telefono;

    @NotBlank(message = "La dirección no puede estar vacía")
    @Size(max = 200, message = "La dirección no puede exceder los 200 caracteres")
    private String direccion;

    @Size(max = 10, message = "El DUI no puede exceder los 10 caracteres")
    @Pattern(regexp = "^[0-9]{8}-[0-9]$", message = "El DUI debe tener el formato ########-#")
    private String dui;
}
