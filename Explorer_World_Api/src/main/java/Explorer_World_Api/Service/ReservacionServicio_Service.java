package Explorer_World_Api.Service;

import Explorer_World_Api.Entities.ReservacionServicio_Entity;
import Explorer_World_Api.Entities.ReservacionesEntity;
import Explorer_World_Api.Exceptions.ExcepcionUsuarioNoRegistrado;
import Explorer_World_Api.Model.DTO.ReservacionServicio_DTO;
import Explorer_World_Api.Model.DTO.ReservacionesDTO;
import Explorer_World_Api.Repositories.ReservacionServicio_Repository;
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
public class ReservacionServicio_Service {
    @Autowired
    private ReservacionServicio_Repository repo;

    //---------Paginacion
    public Page<ReservacionServicio_DTO> PaginacionReservacionServicio(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ReservacionServicio_Entity> pageEntity = repo.findAll(pageable);
        return pageEntity.map(this::convertirADTO_R_S);
    }

    //---------Obtener datos
    public List<ReservacionServicio_DTO> obtenerReservacionServicio() {
        List<ReservacionServicio_Entity> lista = repo.findAll();
        return lista.stream()
                .map(this::convertirADTO_R_S)
                .collect(Collectors.toList());
    }

    public ReservacionServicio_DTO nuevaReservacionServicio(@Valid ReservacionServicio_DTO data) {
        if (data == null || data.getIdReservacion() == null || data.getIdServicio() == null) {
            throw new IllegalArgumentException("Datos de reservación de servicio no pueden ser nulos");
        }
        try {
            ReservacionServicio_Entity entity = convertirAEntity_R_S(data);
            ReservacionServicio_Entity usuarioGuardado = repo.save(entity);
            return convertirADTO_R_S(usuarioGuardado);
        } catch (Exception e) {
            log.error("Error al registrar Reservicio: " + e.getMessage());
            throw new ExcepcionUsuarioNoRegistrado("Error al registrar el Servicio.");
        }
    }

    private ReservacionServicio_Entity convertirAEntity_R_S(ReservacionServicio_DTO data) {
        ReservacionServicio_Entity entity = new ReservacionServicio_Entity();
        entity.setIdReservacionServicio(data.getIdReservacionServicio());
        entity.setIdReservacion(data.getIdReservacion());
        entity.setIdServicio(data.getIdServicio());
        entity.setCantidad(data.getCantidad());
        entity.setPrecioUnitario(data.getPrecioUnitario());
        entity.setFechaHoraServicio(data.getFechaHoraServicio());
        entity.setDescripcion(data.getDescripcion());
        return entity;
    }

    private ReservacionServicio_DTO convertirADTO_R_S(ReservacionServicio_Entity entity) {
        ReservacionServicio_DTO dto = new ReservacionServicio_DTO();
        dto.setIdReservacionServicio(entity.getIdReservacionServicio());
        dto.setIdReservacion(entity.getIdReservacion());
        dto.setIdServicio(entity.getIdServicio());
        dto.setCantidad(entity.getCantidad());
        dto.setPrecioUnitario(entity.getPrecioUnitario());
        dto.setFechaHoraServicio(entity.getFechaHoraServicio());
        dto.setDescripcion(entity.getDescripcion());
        return dto;
    }

    public ReservacionServicio_DTO actualizarReservacionServicio(Long id, @Valid ReservacionServicio_DTO json) {
        ReservacionServicio_Entity existente = repo.findById(id).orElseThrow(() -> new ExcepcionUsuarioNoRegistrado("Reservacion no encontrado"));

        existente.setIdReservacion(json.getIdReservacion());
        existente.setIdServicio(json.getIdServicio());
        existente.setCantidad(json.getCantidad());
        existente.setPrecioUnitario(json.getPrecioUnitario());
        existente.setFechaHoraServicio(json.getFechaHoraServicio());
        existente.setDescripcion(json.getDescripcion());

        ReservacionServicio_Entity Actualizado = repo.save(existente);
        return convertirADTO_R_S(Actualizado);
    }

    public boolean eliminarReservacionServicio(Long id) {
        try {
            if (repo.existsById(id)) {
                repo.deleteById(id);
                return true;
            }
            return false;
        } catch (EmptyResultDataAccessException e) {
            throw new EmptyResultDataAccessException("No se encontró la reservacion " + id, 1);
        }
    }
}
