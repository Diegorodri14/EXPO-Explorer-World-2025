package Explorer_World_Api.Model.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@EqualsAndHashCode
public class TransporteDTO {

    private Long IdTransporte;

    @NotBlank
    private String MarcaTransporte;


    @Positive(message = "El numero de asientos debe de ser numeros positivos")
    private Long NumAsientos;

    @NotBlank
    private String ModeloTransporte;

    @NotBlank(message = "la placa no puede estar vacia")

    private String PlacaTransporte;

    @NotBlank
    private String EstadoTransporte;

    @NotBlank
    private String Image_url;
}
