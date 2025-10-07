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
public class PresupuestoDTO {
    private Long idPresupuesto;

    @NotNull(message = "El ID del cliente no puede estar vacío")
    private Long idCliente;

    @NotNull(message = "La cantidad del presupuesto no puede estar vacía")
    @DecimalMin(value = "0.01", message = "La cantidad debe ser mayor a 0")
    private BigDecimal cantidadPresupuesto;

    @Size(max = 200, message = "Los detalles no pueden exceder los 200 caracteres")
    private String detallesPresupuesto;

    @NotNull(message = "La fecha de registro no puede estar vacía")
    private Date fechaRegistro;
}
