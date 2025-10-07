package Explorer_World_Api.Entities;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@Entity
@Table(name = "VIAJES")
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class ViajesEntity {

    @Id @Column(name = "IDVIAJE")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sq_Viajes")
    @SequenceGenerator(name = "sq_Viajes", sequenceName = "sq_Viajes", allocationSize = 1)
    private Long idViaje;

    @Column(name = "IDCLIENTE")
    private Long IdCliente;

    @Column(name = "IDEMPLEADO")
    private Long IdEmpleado;

    @Column(name = "IDTRANSPORTE")
    private Long IdTransporte;

    @Column(name = "PRECIO")
    private Double Precio;

    @Column(name = "NOMBRE_VIAJE")
    private String NombreViaje;

    @Column(name = "DESTINO")
    private String Destino;

    @Column(name = "FECHA")
    private Date Fecha;

    @Column( name = "ESTADO")
    private String Estado;


    @Column( name = "IDHORARIO")
    private Long IdHorario;

    @Column(name = "FECHASALIDA")
    private Date FechaSalida;

    @Column(name = "FECHAREGRESO")
    private Date FechaRegreso;

}
