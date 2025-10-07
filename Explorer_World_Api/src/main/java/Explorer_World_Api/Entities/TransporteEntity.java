package Explorer_World_Api.Entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name =  "TRANSPORTES")
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class TransporteEntity {

    @Id @Column(name = "IDTRANSPORTE")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sq_Transporte")
    @SequenceGenerator(name = "sq_Transporte", sequenceName = "sq_Transporte", allocationSize = 1)
    private Long IdTransporte;

     @Column(name = "MARCATRANSPORTE")
    private String MarcaTransporte;

     @Column(name = "NUMASIENTOS")
    private Long NumAsientos;

     @Column(name = "MODELOTRANSPORTE")
    private String ModeloTransporte;

     @Column(name = "PLACATRANSPORTE",unique = true)
    private String PlacaTransporte;

     @Column(name = "ESTADOTRANSPORTE")
    private String EstadoTransporte;

    @Column(name = "IMAGE_URL", length = 500)
    private String image_url;

}
