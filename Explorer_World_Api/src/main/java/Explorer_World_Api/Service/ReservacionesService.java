package Explorer_World_Api.Service;

import Explorer_World_Api.Entities.RangoEntity;
import Explorer_World_Api.Entities.ReservacionesEntity;
import Explorer_World_Api.Exceptions.ExceptionReservacionNoEncontrada;
import Explorer_World_Api.Exceptions.ExceptionReservacionNoRegistrada;
import Explorer_World_Api.Model.DTO.RangoDTO;
import Explorer_World_Api.Model.DTO.ReservacionesDTO;
import Explorer_World_Api.Repositories.ReservacionesRepository;
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
public class ReservacionesService {

    @Autowired
    ReservacionesRepository repo;

    //---------Paginacion
    public Page<ReservacionesDTO> PaginacionReservaciones(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ReservacionesEntity> pageEntity = repo.findAll(pageable);
        return pageEntity.map(this::ConvertirADTO);
    }

    //---------Obtener datos
    public List<ReservacionesDTO> ObtenerReservacion() {
        List<ReservacionesEntity> list = repo.findAll();
        return list.stream()
                .map(this::ConvertirADTO)
                .collect(Collectors.toList());
    }

    /**
     *
     * @param data
     * @return
     */

    public ReservacionesDTO AgregarReservacion(@Valid ReservacionesDTO data) {
        if (data == null || data.getNombreCliente() == null || data.getNombreCliente().isEmpty()) {
            throw new IllegalArgumentException("Llenar campo");
        }
        try{
            ReservacionesEntity entity = ConvertirAEntity(data);
            ReservacionesEntity EventoGuardado = repo.save(entity);
            return ConvertirADTO(EventoGuardado);
        }catch (Exception e){
            log.error("Error al registrar el Reservacion " + e.getMessage());
            throw new ExceptionReservacionNoRegistrada("Error al registrar el evento " + e.getMessage());
        }

    }

    /**
     *
     * @param data
     * @return
     */

    private ReservacionesEntity ConvertirAEntity(ReservacionesDTO data){
        ReservacionesEntity entity = new ReservacionesEntity();
        entity.setIdReservacion(data.getIdReservacion());
        entity.setIdCliente(data.getIdCliente());
        entity.setNombreCliente(data.getNombreCliente());
        entity.setTipoReservacion(data.getTipoReservacion());
        entity.setNombreViaje(data.getNombreViaje());
        entity.setDetallesDeReservacion(data.getDetallesDeReservacion());
        entity.setFechaReservacion(data.getFechaReservacion());
        entity.setPersonas(data.getPersonas());

        return entity;
    }

    /**
     * 1. Se declara el metodo convertirADTO el cual recibirá un Entity y retornará un DTO
     * @param reservacionesEntity
     * @return
     */

    private ReservacionesDTO ConvertirADTO(ReservacionesEntity reservacionesEntity){
        ReservacionesDTO dto = new ReservacionesDTO();

        dto.setIdReservacion(reservacionesEntity.getIdReservacion());
        dto.setIdCliente(reservacionesEntity.getIdCliente());
        dto.setNombreCliente(reservacionesEntity.getNombreCliente());
        dto.setTipoReservacion(reservacionesEntity.getTipoReservacion());
        dto.setNombreViaje(reservacionesEntity.getNombreViaje());
        dto.setDetallesDeReservacion(reservacionesEntity.getDetallesDeReservacion());
        dto.setFechaReservacion(reservacionesEntity.getFechaReservacion());
        dto.setPersonas(reservacionesEntity.getPersonas());
        return dto;
    }


    public ReservacionesDTO ActualizarReservacion(Long id, @Valid ReservacionesDTO json) {
        ReservacionesEntity existente = repo.findById(id).orElseThrow(() -> new ExceptionReservacionNoEncontrada("Evento no encontrado"));

        existente.setIdCliente(json.getIdCliente());
        existente.setNombreCliente(json.getNombreCliente());
        existente.setTipoReservacion(json.getTipoReservacion());
        existente.setNombreViaje(json.getNombreViaje());
        existente.setDetallesDeReservacion(json.getDetallesDeReservacion());
        existente.setFechaReservacion(json.getFechaReservacion());
        existente.setPersonas(json.getPersonas());

        ReservacionesEntity actualizado = repo.save(existente);
        return ConvertirADTO(actualizado);
    }

    public boolean BorrarReservacion(Long id) {
        try {

            ReservacionesEntity existente = repo.findById(id).orElse(null);
            if (existente != null) {
                repo.deleteById(id);

                return true;
            } else {
                return false;
            }
        }catch (EmptyResultDataAccessException e){
            throw new EmptyResultDataAccessException("Reservacion no encontrado " + id + "para eliminar ", 1 );
        }

    }
}
