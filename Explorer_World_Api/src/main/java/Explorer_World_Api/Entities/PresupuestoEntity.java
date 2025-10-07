package Explorer_World_Api.Entities;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "PRESUPUESTOS")
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class PresupuestoEntity {
    @Id
    @Column(name = "IDPRESUPUESTO")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sq_Presupuestos")
    @SequenceGenerator(name = "sq_Presupuestos", sequenceName = "sq_Presupuestos", allocationSize = 1)
    private Long idPresupuesto;

    @Column(name = "IDCLIENTE", nullable = false)
    private Long idCliente;

    @Column(name = "CANTIDADPRESUPUESTO")
    private BigDecimal cantidadPresupuesto;

    @Column(name = "DETALLESPRESUPUESTO", length = 200)
    private String detallesPresupuesto;

    @Column(name = "FECHAREGISTRO", nullable = false)
    private Date fechaRegistro;
}
