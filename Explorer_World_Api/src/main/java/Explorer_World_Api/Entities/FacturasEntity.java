package Explorer_World_Api.Entities;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "FACTURAS")
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class FacturasEntity {
    @Id
    @Column(name = "IDFACTURA")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sq_Facturas")
    @SequenceGenerator(name = "sq_Facturas", sequenceName = "sq_Facturas", allocationSize = 1)
    private Long idFactura;

    @Column(name = "IDRESERVACION", nullable = false)
    private Long idReservacion;

    @Column(name = "FECHAPAGO", nullable = false)
    private Date fechaPago;

    @Column(name = "METODOPAGO", nullable = false, length = 50)
    private String metodoPago;

    @Column(name = "MONTOTOTAL", nullable = false, precision = 10, scale = 2)
    private BigDecimal montoTotal;
}
