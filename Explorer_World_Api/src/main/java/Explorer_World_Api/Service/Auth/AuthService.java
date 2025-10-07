package Explorer_World_Api.Service.Auth;

import Explorer_World_Api.Config.Argon2.Argon2Password;
import Explorer_World_Api.Entities.UsuarioEntity;
import Explorer_World_Api.Repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    public boolean Login(String correo, String password){
        //Crear el objeto de tipo Argon2
        Argon2Password objHash = new Argon2Password();
        //Invocar un metodo que permita buscar al usuario por su correo
        Optional<UsuarioEntity> list = usuarioRepository.findByCorreo(correo).stream().findFirst();
        if (list.isPresent()){
            UsuarioEntity usuario = list.get();
            String nombreRango = usuario.getRangoUsuario().getNombreRango();
            System.out.println("Usuario ID encontrado: " + usuario.getIdUsuario() +
                    ", email: " + usuario.getCorreo() +
                    ", rol: " + nombreRango);
            String HashBD = usuario.getContrasena();
            return objHash.VerifyPassword(HashBD, password);
        }
        return false;
    }

    public Optional<UsuarioEntity> obtenerUsuario(String correo) {
        // Buscar usuario completo en la base de datos
        Optional<UsuarioEntity> userOpt = usuarioRepository.findByCorreo(correo);
        return (userOpt != null) ? userOpt : null;
    }
}
