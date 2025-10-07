package Explorer_World_Api.Entities;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "Usuarios")
@Getter
@Setter
@EqualsAndHashCode
public class UsuarioEntity {

    @Id
    @Column(name = "IDUSUARIO")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sq_Usuarios")
    @SequenceGenerator(name = "sq_Usuarios", sequenceName = "sq_Usuarios", allocationSize = 1)
    private Long idUsuario;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "IDRANGO", referencedColumnName = "IDRANGO")
    private RangoEntity rangoUsuario;

    @Column(name = "ESTADO")
    private String estado;

    @Column(name = "USUARIO")
    private String usuario;

    @Column(name = "CONTRASENA")
    private String contrasena;

    @Column(name = "CORREO")
    private String correo;

    @Column(name = "IMAGE_URL", length = 500)
    private String image_url;

    @Override
    public String toString() {
        return "UsuarioEntity{" +
                "idUsuario=" + idUsuario +
                ", rangoUsuario=" + rangoUsuario +
                ", estado='" + estado + '\'' +
                ", usuario='" + usuario + '\'' +
                ", contrasena='" + contrasena + '\'' +
                ", correo='" + correo + '\'' +
                ", image_url='" + image_url + '\'' +
                '}';
    }
}
