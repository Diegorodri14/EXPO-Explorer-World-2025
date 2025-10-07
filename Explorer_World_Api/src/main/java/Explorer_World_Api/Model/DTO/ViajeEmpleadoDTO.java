package Explorer_World_Api.Model.DTO;

import jakarta.validation.constraints.FutureOrPresent;
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
public class ViajeEmpleadoDTO {
    private Long idViajeEmpleado;

    @NotNull(message = "El ID de viaje no puede estar vacío")
    private Long idViaje;

    @NotNull(message = "El ID de empleado no puede estar vacío")
    private Long idEmpleado;

    @Size(max = 50, message = "El rol en viaje no puede exceder los 50 caracteres")
    private String rolEnViaje;

    @NotNull(message = "La fecha de inicio de participación no puede estar vacía")
    @FutureOrPresent(message = "La fecha de inicio de participación debe ser igual o posterior a la fecha actual")
    private Date fechaInicioParticipacion;

    private Date fechaFinParticipacion;
}
