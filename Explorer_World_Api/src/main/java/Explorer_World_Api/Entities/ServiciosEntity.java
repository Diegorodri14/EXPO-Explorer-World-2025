package Explorer_World_Api.Entities;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;

@Entity
@Table(name = "SERVICIOS")
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class ServiciosEntity {
    @Id
    @Column(name = "IDSERVICIO")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sq_Servicios")
    @SequenceGenerator(name = "sq_Servicios", sequenceName = "sq_Servicios", allocationSize = 1)
    private Long idServicio;

    @Column(name = "NOMBRESERVICIO", nullable = false, length = 100)
    private String nombreServicio;

    @Column(name = "DESCRIPCION", nullable = false, length = 100)
    private String descripcion;

    @Column(name = "COSTO", nullable = false)
    private Double costo;
}
