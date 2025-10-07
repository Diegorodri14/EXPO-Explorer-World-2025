package Explorer_World_Api.Entities;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "CLIENTES")
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class ClienteEntity {

    @Id
    @Column(name = "IDCLIENTE")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sq_Clientes")
    @SequenceGenerator(name = "sq_Clientes", sequenceName = "sq_Clientes", allocationSize = 1)
    private Long idCliente;

    @Column(name = "NOMBRECLIENTE", nullable = false, length = 100)
    private String nombreCliente;

    @Column(name = "APELLIDOCLIENTE", nullable = false, length = 100)
    private String apellidoCliente;

    @Column(name = "EMAILCLIENTE", nullable = false, unique = true, length = 100)
    private String emailCliente;

    @Column(name = "TELEFONO", nullable = false, length = 20)
    private String telefono;

    @Column(name = "DIRECCION", nullable = false, length = 200)
    private String direccion;

    @Column(name = "DUI", unique = true, length = 10)
    private String dui;
}
