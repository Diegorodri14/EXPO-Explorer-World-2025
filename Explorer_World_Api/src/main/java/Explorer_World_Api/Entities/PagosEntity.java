package Explorer_World_Api.Entities;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "PAGOS")
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class PagosEntity {
    @Id
    @Column(name = "IDPAGO")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sq_Pagos")
    @SequenceGenerator(name = "sq_Pagos", sequenceName = "sq_Pagos", allocationSize = 1)
    private Long idPago;

    @Column(name = "IDRESERVACION", nullable = false)
    private Long idReservacion;

    @Column(name = "METODOPAGO", nullable = false, length = 50)
    private String metodoPago;

    @Column(name = "PAGO", nullable = false, precision = 10, scale = 2)
    private BigDecimal pago;

    @Column(name = "FECHAPAGO", nullable = false)
    private Date fechaPago;

    @Column(name = "ESTADOPAGO", nullable = false, length = 20)
    private String estadoPago;
}
