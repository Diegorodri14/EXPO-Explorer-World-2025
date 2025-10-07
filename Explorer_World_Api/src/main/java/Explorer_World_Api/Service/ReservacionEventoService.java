package Explorer_World_Api.Service;

import Explorer_World_Api.Entities.ReservacionEventoEntity;
import Explorer_World_Api.Exceptions.ExceptionReservacionEventoNoRegistrado;
import Explorer_World_Api.Model.DTO.ReservacionEventoDTO;
import Explorer_World_Api.Repositories.ReservacionEventoRepository;
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
public class ReservacionEventoService {
    @Autowired
    private ReservacionEventoRepository repo;

    //---------Paginacion
    public Page<ReservacionEventoDTO> PaginacionReservacionEvento(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ReservacionEventoEntity> pageEntity = repo.findAll(pageable);
        return pageEntity.map(this::ConvertirADTO);
    }

    //---------Obtener datos
    public List<ReservacionEventoDTO> ObtenerReservacionEvento() {
        List<ReservacionEventoEntity> list = repo.findAll();
        return list.stream()
                .map(this::ConvertirADTO)
                .collect(Collectors.toList());
    }

    /**
     * Agregar una nueva ReservacionEvento
     * @param data
     * @return
     */
    public ReservacionEventoDTO AgregarReservacionEvento(@Valid ReservacionEventoDTO data) {
        if (data == null || data.getCantidad() == null) {
            throw new IllegalArgumentException("Campos obligatorios no pueden ser nulos");
        }

        // Validar si ya existe la combinación única
        boolean existe = repo.existsByIdReservacionAndIdEventoAndFechaHoraEvento(
                data.getIdReservacion(),
                data.getIdEvento(),
                data.getFechaHoraEvento()
        );

        if (existe) {
            throw new IllegalArgumentException("Ya existe un registro con la misma combinación de Reservación, Evento y Fecha/Hora");
        }

        try{
            ReservacionEventoEntity entity = ConvertirAEntity(data);
            ReservacionEventoEntity ReservacionEventoGuardada = repo.save(entity);
            return ConvertirADTO(ReservacionEventoGuardada);
        }catch (Exception e){
            log.error("Error al registrar " + e.getMessage());
            throw new ExceptionReservacionEventoNoRegistrado("Error al registrar la ReservacionEvento " + e.getMessage());
        }
    }

    /**
     * Convertir DTO a Entity
     * @param data
     * @return
     */
    private ReservacionEventoEntity ConvertirAEntity(ReservacionEventoDTO data) {
        ReservacionEventoEntity entity = new ReservacionEventoEntity();
        entity.setIdReservacionEvento(data.getIdReservacionEvento());
        entity.setIdReservacion(data.getIdReservacion());
        entity.setIdEvento(data.getIdEvento());
        entity.setCantidad(data.getCantidad());
        entity.setPrecioUnitario(data.getPrecioUnitario());
        entity.setFechaHoraEvento(data.getFechaHoraEvento());
        entity.setDescripcion(data.getDescripcion());
        return entity;
    }

    /**
     * Convertir Entity a DTO
     * @param entity
     * @return
     */
    private ReservacionEventoDTO ConvertirADTO(ReservacionEventoEntity entity) {
        ReservacionEventoDTO dto = new ReservacionEventoDTO();
        dto.setIdReservacionEvento(entity.getIdReservacionEvento());
        dto.setIdReservacion(entity.getIdReservacion());
        dto.setIdEvento(entity.getIdEvento());
        dto.setCantidad(entity.getCantidad());
        dto.setPrecioUnitario(entity.getPrecioUnitario());
        dto.setFechaHoraEvento(entity.getFechaHoraEvento());
        dto.setDescripcion(entity.getDescripcion());
        return dto;
    }

    /**
     * Actualizar ReservacionEvento existente
     * @param id
     * @param json
     * @return
     */
    public ReservacionEventoDTO ActualizarReservacionEvento(Long id, @Valid ReservacionEventoDTO json) {
        ReservacionEventoEntity existente = repo.findById(id).orElseThrow(() -> new ExceptionReservacionEventoNoRegistrado("ReservacionEvento no encontrada"));

        existente.setIdReservacion(json.getIdReservacion());
        existente.setIdEvento(json.getIdEvento());
        existente.setCantidad(json.getCantidad());
        existente.setPrecioUnitario(json.getPrecioUnitario());
        existente.setFechaHoraEvento(json.getFechaHoraEvento());
        existente.setDescripcion(json.getDescripcion());

        ReservacionEventoEntity actualizado = repo.save(existente);
        return ConvertirADTO(actualizado);
    }

    /**
     * Eliminar ReservacionEvento
     * @param id
     * @return
     */
    public boolean BorrarReservacionEvento(Long id) {
        try {
            ReservacionEventoEntity existente = repo.findById(id).orElse(null);
            if (existente != null) {
                repo.deleteById(id);
                return true;
            } else {
                return false;
            }
        }catch (EmptyResultDataAccessException e){
            throw new EmptyResultDataAccessException("ReservacionEvento no encontrada con id: " + id + " para eliminar", 1);
        }
    }
}
