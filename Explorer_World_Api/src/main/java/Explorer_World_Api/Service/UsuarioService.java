package Explorer_World_Api.Service;

import Explorer_World_Api.Entities.TransporteEntity;
import Explorer_World_Api.Entities.UsuarioEntity;
import Explorer_World_Api.Exceptions.ExcepcionUsuarioNoRegistrado;
import Explorer_World_Api.Model.DTO.TransporteDTO;
import Explorer_World_Api.Model.DTO.UsuarioDTO;
import Explorer_World_Api.Repositories.UsuarioRepository;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UsuarioService {
    @Autowired
    private UsuarioRepository repo;

    //---------Paginacion
    public Page<UsuarioDTO> PaginacionUsario(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<UsuarioEntity> pageEntity = repo.findAll(pageable);
        return pageEntity.map(this::convertirADTO);
    }

    //---------Obtener datos
    public List<UsuarioDTO> obtenerUsuarios() {
        List<UsuarioEntity> lista = repo.findAll();
        return lista.stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    /**
     *
     * @param data
     * @return
     */

    public UsuarioDTO nuevoUsuario(@Valid UsuarioDTO data) {
        if (data == null || data.getContrasena() == null || data.getContrasena().isEmpty()) {
            throw new IllegalArgumentException("Usuario o contraseña no pueden ser nulos");
        }
        try {
            UsuarioEntity entity = convertirAEntity(data);
            UsuarioEntity usuarioGuardado = repo.save(entity);
            return convertirADTO(usuarioGuardado);
        } catch (Exception e) {
            log.error("Error al registrar el usuario: " + e.getMessage());
            throw new ExcepcionUsuarioNoRegistrado("Error al registrar el usuario.");
        }
    }

    /**
     *
     * @param data
     * @return
     */

    private UsuarioEntity convertirAEntity(UsuarioDTO data) {
        UsuarioEntity entity = new UsuarioEntity();
        entity.setIdUsuario(data.getIdUsuario());
        entity.setEstado(data.getEstado());
        entity.setUsuario(data.getUsuario());
        entity.setContrasena(data.getContrasena());
        entity.setCorreo(data.getCorreo());
        entity.setImage_url(data.getImage_url());
        return entity;
    }
    /**
     * 1. Se declara el metodo convertirADTO el cual recibirá un Entity y retornará un DTO
     * @param usuarioEntity
     * @return
     */

    private UsuarioDTO convertirADTO(UsuarioEntity usuarioEntity) {
        UsuarioDTO dto = new UsuarioDTO();
        dto.setIdUsuario(usuarioEntity.getIdUsuario());
        dto.setEstado(usuarioEntity.getEstado());
        dto.setUsuario(usuarioEntity.getUsuario());
        dto.setContrasena(usuarioEntity.getContrasena());
        dto.setCorreo(usuarioEntity.getCorreo());
        dto.setImage_url(usuarioEntity.getImage_url());
        return dto;
    }

    public UsuarioDTO modificarUsuario(Long id, @Valid UsuarioDTO json) {
        UsuarioEntity existente = repo.findById(id).orElseThrow(() -> new ExcepcionUsuarioNoRegistrado("Usuario no encontrado"));

        existente.setEstado(json.getEstado());
        existente.setUsuario(json.getUsuario());
        existente.setContrasena(json.getContrasena());
        existente.setCorreo(json.getCorreo());
        existente.setImage_url(json.getImage_url());

        UsuarioEntity usuarioActualizado = repo.save(existente);
        return convertirADTO(usuarioActualizado);
    }

    public boolean eliminarUsuario(Long id) {
        try {
            if (repo.existsById(id)) {
                repo.deleteById(id);
                return true;
            }
            return false;
        } catch (EmptyResultDataAccessException e) {
            throw new EmptyResultDataAccessException("No se encontró al usuario con el ID: " + id, 1);
        }
    }
}
