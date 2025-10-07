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
@ToString
@EqualsAndHashCode
public class ReservacionServicio_DTO {

    private Long IdReservacionServicio;

    @NotNull(message = "El ID de reservación no puede ser nulo")
    @Positive(message = "El ID de reservación debe ser positivo")
    private Long IdReservacion;

    @NotNull(message = "El ID de servicio no puede ser nulo")
    @Positive(message = "El ID de servicio debe ser positivo")
    private Long IdServicio;

    @NotNull(message = "La cantidad no puede ser nula")
    @Min(value = 1, message = "La cantidad debe ser al menos 1")
    private Long Cantidad;

    @NotNull(message = "El precio unitario no puede ser nulo")
    @DecimalMin(value = "0.01", message = "El precio unitario debe ser mayor que 0")
    private BigDecimal PrecioUnitario;

    private Date FechaHoraServicio;

    @Size(max = 255, message = "La descripción no puede exceder los 255 caracteres")
    private String Descripcion;
}
