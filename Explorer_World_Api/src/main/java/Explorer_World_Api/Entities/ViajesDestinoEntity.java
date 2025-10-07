package Explorer_World_Api.Entities;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "VIAJEDESTINO")
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class ViajesDestinoEntity {
    @Id
    @Column(name = "IDVIAJEDESTINO")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sq_ViajeDestino")
    @SequenceGenerator(name = "sq_ViajeDestino", sequenceName = "SQ_VIAJEDESTINO", allocationSize = 1)
    private Long idViajeDestino;

    @Column(name = "IDVIAJE", nullable = false)
    private Long idViaje;

    @Column(name = "IDDESTINO", nullable = false)
    private Long idDestino;

    @Column(name = "ORDENDESTINO", nullable = false)
    private Long ordenDestino;

    @Column(name = "DURACIONHORAS", precision = 5, scale = 2)
    private BigDecimal duracionHoras;

    @Column(name = "FECHAINICIOESTANCIA")
    @Temporal(TemporalType.DATE)
    private Date fechaInicioEstancia;

    @Column(name = "ACTIVIDADESPLANIFICADAS", length = 255)
    private String actividadesPlanificadas;
}
