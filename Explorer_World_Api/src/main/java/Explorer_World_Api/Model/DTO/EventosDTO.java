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
public class EventosDTO {

    private Long idEvento;

    @Positive(message = "el id del destino no puede ser negativo")
    private Long IdDestino;

    @NotBlank
    private String NombreEvento;

    @NotBlank
    private String TipoEvento;

    @NotBlank
    private String LugarEvento;

    private Date FechaEvento;

    @NotBlank
    private String DescripcionEvento;

    @NotBlank
    private  String image_url;

}
