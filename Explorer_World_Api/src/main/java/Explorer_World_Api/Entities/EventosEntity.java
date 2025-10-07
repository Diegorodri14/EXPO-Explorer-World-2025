package Explorer_World_Api.Entities;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@Entity
@Table(name = "EVENTOS")
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class EventosEntity {

    @Id
    @Column(name = "IDEVENTO")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sq_Eventos")
    @SequenceGenerator(name = "sq_Eventos", sequenceName = "sq_Eventos", allocationSize = 1)
    private Long idEvento;

    @Column(name = "IDDESTINO")
    private Long IdDestino;

    @Column(name = "NOMBREEVENTO")
    private String NombreEvento;

    @Column(name = "TIPOEVENTO")
    private String TipoEvento;

    @Column(name = "LUGAREVENTO")
    private String LugarEvento;

    @Column( name = "FECHAEVENTO")
    private Date FechaEvento;

    @Column(name = "DESCRIPCIONEVENTO")
    private String DescripcionEvento;

    @Column(name = "IMAGE_URL", length = 500)
    private String image_url;

}
