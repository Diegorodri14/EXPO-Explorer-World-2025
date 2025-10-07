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
public class PagosDTO {
    private Long idPago;

    @NotNull(message = "El ID de reservación no puede estar vacío")
    private Long idReservacion;

    @NotBlank(message = "El método de pago no puede estar vacío")
    @Size(max = 50, message = "El método de pago no puede exceder los 50 caracteres")
    private String metodoPago;

    @NotNull(message = "El monto del pago no puede estar vacío")
    @DecimalMin(value = "0.01", message = "El pago debe ser mayor a 0")
    private BigDecimal pago;

    @NotNull(message = "La fecha de pago no puede estar vacía")
    private Date fechaPago;

    @NotBlank(message = "El estado del pago no puede estar vacío")
    @Size(max = 20, message = "El estado del pago no puede exceder los 20 caracteres")
    @Pattern(regexp = "^(Completo|Pendiente|Cancelado)$",
            message = "El estado del pago debe ser COMPLETO, PENDIENTE o CANCELADO")
    private String estadoPago;
}
