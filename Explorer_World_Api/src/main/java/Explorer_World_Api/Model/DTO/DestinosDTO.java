package Explorer_World_Api.Model.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@EqualsAndHashCode
@ToString
public class DestinosDTO {
    private Long idDestino;

    @NotBlank(message = "El nombre del destino no puede estar vacío")
    @Size(max = 100, message = "El nombre del destino no puede exceder los 100 caracteres")
    private String nombreDestino;

    @NotBlank(message = "El lugar del destino no puede estar vacío")
    @Size(max = 100, message = "El lugar del destino no puede exceder los 100 caracteres")
    private String lugarDestino;

    @NotBlank(message = "El tipo de destino no puede estar vacío")
    @Size(max = 50, message = "El tipo de destino no puede exceder los 50 caracteres")
    private String tipoDestino;

    @Size(max = 200, message = "La descripción no puede exceder los 200 caracteres")
    private String descripcionDestino;

    @NotBlank
    private  String image_url;
}
