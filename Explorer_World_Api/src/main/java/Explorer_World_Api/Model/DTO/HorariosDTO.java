package Explorer_World_Api.Model.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@Getter
@Setter
@EqualsAndHashCode
@ToString
public class HorariosDTO {
    private Long idHorario;

    @NotNull(message = "La hora no puede ser nula")
    private Date hora;

    @NotBlank(message = "El tipo de horario no puede estar vacío")
    @Size(max = 50, message = "El tipo de horario no puede exceder los 50 caracteres")
    private String tipoHorario;

    @Size(max = 50, message = "La disponibilidad no puede exceder los 50 caracteres")
    private String disponibilidad;
}
