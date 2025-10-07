package Explorer_World_Api.Entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "RANGOS")
@Getter
@Setter
@EqualsAndHashCode
public class RangoEntity {
    @Id
    @Column(name = "IDRANGO")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SQ_RANGOS")
    @SequenceGenerator(name = "SQ_RANGOS", sequenceName = "SQ_RANGOS", allocationSize = 1)
    private Long idRango;

    @Column(name = "NOMBRERANGO")
    private String NombreRango;

    @OneToMany(mappedBy = "rangoUsuario", cascade = CascadeType.ALL,  fetch = FetchType.LAZY)
    @JsonIgnore
    private List<UsuarioEntity> usuarios = new ArrayList<>();

    @Override
    public String toString() {
        return "RangoEntity{" +
                "idRango=" + idRango +
                ", NombreRango='" + NombreRango + '\'' +
                ", usuarios=" + usuarios +
                '}';
    }
}
