package Explorer_World_Api.Model.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@Getter
@Setter
@ToString
@EqualsAndHashCode
public class ReservacionesDTO {

    private Long idReservacion;

    @Positive(message = "el id del cliente no puede ser negativo")
    private Long IdCliente;

    @NotBlank
    private String nombreCliente;

    @NotBlank
    private String TipoReservacion;

    @NotBlank
    private String NombreViaje;

    @NotBlank
    private String DetallesDeReservacion;

    private Date FechaReservacion;

    @Positive
    private Long Personas;

}
