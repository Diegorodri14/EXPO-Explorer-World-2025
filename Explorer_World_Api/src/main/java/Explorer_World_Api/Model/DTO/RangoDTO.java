package Explorer_World_Api.Model.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@EqualsAndHashCode
public class RangoDTO {
    @NotNull(message = "El ID del rango no puede ser nulo")
    @Positive(message = "El ID del rango debe ser un número positivo")
    private Long idRango;  //

    @NotBlank(message = "El nombre del rango no puede estar vacío")
    @Size(max = 50, message = "El nombre del rango no puede exceder los 50 caracteres")
    private String nombreRango;

    @NotBlank(message = "El rango de usuario no puede estar vacío")
    @Size(max = 10, message = "El rango de usuario no puede exceder los 10 caracteres")
    private String rangoUsuario;
}
