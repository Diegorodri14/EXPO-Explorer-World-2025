package Explorer_World_Api.Entities;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "DESTINOS")
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class DestinosEntity {
    @Id
    @Column(name = "IDDESTINO")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sq_Destinos")
    @SequenceGenerator(name = "sq_Destinos", sequenceName = "sq_Destinos", allocationSize = 1)
    private Long idDestino;

    @Column(name = "NOMBREDESTINO", nullable = false, length = 100)
    private String nombreDestino;

    @Column(name = "LUGARDESTINO", nullable = false, length = 100)
    private String lugarDestino;

    @Column(name = "TIPODESTINO", nullable = false, length = 50)
    private String tipoDestino;

    @Column(name = "DESCRIPCIONDESTINO", length = 200)
    private String descripcionDestino;

    @Column(name = "IMAGE_URL", length = 500)
    private String image_url;
}
