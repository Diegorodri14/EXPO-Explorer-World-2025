package Explorer_World_Api.Entities;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.lang.model.element.Name;
import java.util.Date;

@Entity
@Getter
@Setter
@ToString
@EqualsAndHashCode
@Table(name = "RESERVACIONES")
public class ReservacionesEntity {

    @Id
    @Column(name = "IDRESERVACION")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sq_Reservaciones")
    @SequenceGenerator(name = "sq_Reservaciones", sequenceName = "sq_Reservaciones", allocationSize = 1)
    private Long idReservacion;

    @Column(name = "IDCLIENTE")
    private Long IdCliente;

    @Column(name = "NOMBRECLIENTE")
    private String nombreCliente;

    @Column(name = "TIPORESERVACION")
    private String TipoReservacion;

    @Column(name = "nombreViaje")
    private String NombreViaje;

    @Column(name = "DETALLEDERESERVACION")
    private String DetallesDeReservacion;

    @Column(name = "FECHARESERVACION")
    @Temporal(TemporalType.TIMESTAMP)
    private Date FechaReservacion;

    @Column(name = "PERSONAS")
    private Long Personas;

}
