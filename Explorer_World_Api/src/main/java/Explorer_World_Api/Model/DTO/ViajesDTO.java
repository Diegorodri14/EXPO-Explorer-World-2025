package Explorer_World_Api.Model.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@Getter
@Setter
@EqualsAndHashCode
@ToString
public class ViajesDTO {

    private Long  idViaje;

    @Positive(message = "El id del cliente no puede ser negativo")
    private Long IdCliente;

    @Positive(message = "El id del empleado debe ser positivo")
    private Long IdEmpleado;

    @Positive(message = "El id del transporte debe ser positivo")
    private Long IdTransporte;

    private Double Precio;

    @NotBlank
    private String NombreViaje;

    @NotBlank
    private String Destino;

    private Date Fecha;

    @NotBlank
    private String Estado;

    @Positive(message = "El id del horario debe de ser positivo")
    private Long IdHorario;


    private Date FechaSalida;

    private Date FechaRegreso;


}
