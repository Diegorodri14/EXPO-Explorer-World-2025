package Explorer_World_Api.Entities;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "BOLETOS")
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class BoletosEntity {

    @Id
    @Column(name = "IDBOLETO")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sq_Boletos")
    @SequenceGenerator(name = "sq_Boletos", sequenceName = "sq_Boletos", allocationSize = 1)
    private Long idBoleto;

    @Column(name = "IDRESERVACION", nullable = false)
    private Long idReservacion;

    @Column(name = "IDVIAJE", nullable = false)
    private Long idViaje;

    @Column(name = "FECHAVENCIMIENTO", nullable = false)
    private Date fechaVencimiento;

    @Column(name = "MONTO", nullable = false, precision = 10, scale = 2)
    private BigDecimal monto;

    @Column(name = "ASIENTOS", length = 20)
    private String asientos;
}
