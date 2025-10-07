package Explorer_World_Api.Model.DTO;

import jakarta.validation.constraints.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
@EqualsAndHashCode
@ToString
public class EmpleadoDTO {
    private Long idEmpleado;

    @NotNull(message = "El ID de rango no puede estar vacío")
    private Long idRango;

    @NotBlank(message = "El nombre del empleado no puede estar vacío")
    @Size(max = 100, message = "El nombre no puede exceder los 100 caracteres")
    private String nombreEmpleado;

    @NotBlank(message = "El apellido del empleado no puede estar vacío")
    @Size(max = 100, message = "El apellido no puede exceder los 100 caracteres")
    private String apellidoEmpleado;

    @Size(max = 100, message = "El email no puede exceder los 100 caracteres")
    @Email(message = "El email debe tener un formato válido")
    private String emailEmpleado;

    @NotNull(message = "La fecha de nacimiento no puede estar vacía")
    @Past(message = "La fecha de nacimiento debe ser en el pasado")
    private Date fechaNacimiento;

    @Size(max = 20, message = "El teléfono no puede exceder los 20 caracteres")
    private String telefono;

    @Size(max = 200, message = "La dirección no puede exceder los 200 caracteres")
    private String direccion;

    @DecimalMin(value = "0.0", message = "El salario no puede ser negativo")
    private BigDecimal salario;

    @NotBlank
    private String image_url;
}
