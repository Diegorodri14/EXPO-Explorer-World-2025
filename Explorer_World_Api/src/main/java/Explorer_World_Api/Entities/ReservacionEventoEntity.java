package Explorer_World_Api.Entities;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "RESERVACIONEVENTO")
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class ReservacionEventoEntity {

    @Id
    @Column(name = "IDRESERVACIONEVENTO")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sq_ReservacionEvento")
    @SequenceGenerator(name = "sq_ReservacionEvento", sequenceName = "SQ_RESERVACIONEVENTO", allocationSize = 1)
    private Long idReservacionEvento;

    @Column(name = "IDRESERVACION", nullable = false)
    private Long idReservacion;

    @Column(name = "IDEVENTO", nullable = false)
    private Long idEvento;

    @Column(name = "CANTIDAD", nullable = false)
    private Long cantidad;

    @Column(name = "PRECIOUNITARIO", precision = 10, scale = 2)
    private BigDecimal precioUnitario;

    @Column(name = "FECHAHORAEVENTO", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaHoraEvento;

    @Column(name = "DESCRIPCION", length = 200)
    private String descripcion;
}
