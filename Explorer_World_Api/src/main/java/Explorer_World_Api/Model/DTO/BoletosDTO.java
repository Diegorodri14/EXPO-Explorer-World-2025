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
public class BoletosDTO {
    private Long idBoleto;

    @NotNull(message = "El ID de reservación no puede estar vacío")
    private Long idReservacion;

    @NotNull(message = "El ID de viaje no puede estar vacío")
    private Long idViaje;

    @NotNull(message = "La fecha de vencimiento no puede estar vacía")
    private Date fechaVencimiento;

    @NotNull(message = "El monto no puede estar vacío")
    @DecimalMin(value = "0.01", message = "El monto debe ser mayor a 0")
    private BigDecimal monto;

    @Size(max = 20, message = "Los asientos no pueden exceder los 20 caracteres")
    private String asientos;
}
