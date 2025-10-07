package Explorer_World_Api.Entities;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@Entity
@Table(name = "HORARIOS")
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class HorariosEntity {
    @Id
    @Column(name = "IDHORARIO")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sq_Horarios")
    @SequenceGenerator(name = "sq_Horarios", sequenceName = "sq_Horarios", allocationSize = 1)
    private Long idHorario;

    @Column(name = "HORA")
    @Temporal(TemporalType.TIMESTAMP)
    private Date hora;

    @Column(name = "TIPOHORARIO", nullable = false, length = 50)
    private String tipoHorario;

    @Column(name = "DISPONIBILIDAD", length = 50)
    private String disponibilidad;
}
