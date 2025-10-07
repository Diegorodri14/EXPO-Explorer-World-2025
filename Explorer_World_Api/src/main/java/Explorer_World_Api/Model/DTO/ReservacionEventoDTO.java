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
public class ReservacionEventoDTO {
    private Long idReservacionEvento;

    @NotNull(message = "El ID de reservación no puede estar vacío")
    private Long idReservacion;

    @NotNull(message = "El ID de evento no puede estar vacío")
    private Long idEvento;

    @NotNull(message = "La cantidad no puede estar vacía")
    @DecimalMin(value = "1", message = "La cantidad debe ser mayor a 0")
    private Long cantidad;

    @DecimalMin(value = "0.0", message = "El precio unitario no puede ser negativo")
    private BigDecimal precioUnitario;

    @NotNull(message = "La fecha y hora del evento no puede estar vacía")
    private Date fechaHoraEvento;

    @Size(max = 200, message = "La descripción no puede exceder los 200 caracteres")
    private String descripcion;
}
