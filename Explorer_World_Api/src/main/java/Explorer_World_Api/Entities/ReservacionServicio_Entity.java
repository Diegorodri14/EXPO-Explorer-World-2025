package Explorer_World_Api.Entities;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.Date;


@Entity
@Table(name = "RESERVACIONSERVICIO")
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class ReservacionServicio_Entity {
    @Id
    @Column(name = "IDRESERVACIONSERVICIO")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SQ_RESERVACIONSERVICIO")
    @SequenceGenerator(name = "SQ_RESERVACIONSERVICIO", sequenceName = "SQ_RESERVACIONSERVICIO", allocationSize = 1)
    private Long IdReservacionServicio;

    @Column(name = "IDRESERVACION")
    private Long IdReservacion;

    @Column(name = "IDSERVICIO")
    private Long IdServicio;

    @Column(name = "CANTIDAD")
    private Long Cantidad;

    @Column(name = "PRECIOUNITARIO")
    private BigDecimal PrecioUnitario;

    @Column(name = "FECHAHORASERVICIO")
    @Temporal(TemporalType.TIMESTAMP)
    private Date FechaHoraServicio;

    @Column(name = "DESCRIPCION")
    private String Descripcion;
}
