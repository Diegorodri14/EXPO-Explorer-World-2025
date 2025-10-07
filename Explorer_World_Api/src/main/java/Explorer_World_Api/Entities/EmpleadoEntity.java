package Explorer_World_Api.Entities;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "EMPLEADOS")
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class EmpleadoEntity {
    @Id
    @Column(name = "IDEMPLEADO")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sq_Empleados")
    @SequenceGenerator(name = "sq_Empleados", sequenceName = "sq_Empleados", allocationSize = 1)
    private Long idEmpleado;

    @Column(name = "IDRANGO", nullable = false)
    private Long idRango;

    @Column(name = "NOMBREEMPLEADO", nullable = false, length = 100)
    private String nombreEmpleado;

    @Column(name = "APELLIDOEMPLEADO", nullable = false, length = 100)
    private String apellidoEmpleado;

    @Column(name = "EMAILEMPLEADO", unique = true, length = 100)
    private String emailEmpleado;

    @Column(name = "FECHANACIMIENTO", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date fechaNacimiento;

    @Column(name = "TELEFONO", length = 20)
    private String telefono;

    @Column(name = "DIRECCION", length = 200)
    private String direccion;

    @Column(name = "SALARIO", precision = 10, scale = 2)
    private BigDecimal salario;

    @Column(name = "IMAGE_URL", length = 500)
    private String image_url;
}
