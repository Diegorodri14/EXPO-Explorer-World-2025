package Explorer_World_Api.Model.DTO;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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
public class ViajeDestinoDTO {
    private Long idViajeDestino;

    @NotNull(message = "El ID de viaje no puede estar vacío")
    private Long idViaje;

    @NotNull(message = "El ID de destino no puede estar vacío")
    private Long idDestino;

    @NotNull(message = "El orden del destino no puede estar vacío")
    private Long ordenDestino;

    @DecimalMin(value = "0.01", message = "La duración debe ser mayor a 0")
    private BigDecimal duracionHoras;

    private Date fechaInicioEstancia;

    @Size(max = 255, message = "Las actividades planificadas no pueden exceder los 255 caracteres")
    private String actividadesPlanificadas;
}
