package Explorer_World_Api.Entities;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@Entity
@Table(name = "VIAJEEMPLEADO")
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class ViajeEmpleadoEntity {

    @Id
    @Column(name = "IDVIAJEEMPLEADO")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sq_ViajeEmpleado")
    @SequenceGenerator(name = "sq_ViajeEmpleado", sequenceName = "SQ_VIAJEEMPLEADO", allocationSize = 1)
    private Long idViajeEmpleado;

    @Column(name = "IDVIAJE", nullable = false)
    private Long idViaje;

    @Column(name = "IDEMPLEADO", nullable = false)
    private Long idEmpleado;

    @Column(name = "ROLENVIAJE", length = 50)
    private String rolEnViaje;

    @Column(name = "FECHAINICIOPARTICIPACION")
    @Temporal(TemporalType.DATE)
    private Date fechaInicioParticipacion;

    @Column(name = "FECHAFINPARTICIPACION")
    @Temporal(TemporalType.DATE)
    private Date fechaFinParticipacion;
}
