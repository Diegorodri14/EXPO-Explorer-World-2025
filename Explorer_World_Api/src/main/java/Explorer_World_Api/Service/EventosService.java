package Explorer_World_Api.Service;

import Explorer_World_Api.Entities.EmpleadoEntity;
import Explorer_World_Api.Entities.EventosEntity;
import Explorer_World_Api.Exceptions.ExceptionEventoNoRegistrado;
import Explorer_World_Api.Exceptions.ExceptionRangoNoEncontrado;
import Explorer_World_Api.Model.DTO.EmpleadoDTO;
import Explorer_World_Api.Model.DTO.EventosDTO;
import Explorer_World_Api.Repositories.EventosRepository;
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
public class EventosService {
    @Autowired
    EventosRepository repo;

    //---------Paginacion
    public Page<EventosDTO> PaginacionEvento(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<EventosEntity> pageEntity = repo.findAll(pageable);
        return pageEntity.map(this::ConvertirADTO);
    }

    //---------Obtener datos
    public List<EventosDTO> ObtenerEvento() {
        List<EventosEntity> list = repo.findAll();
        return list.stream()
                .map(this::ConvertirADTO)
                .collect(Collectors.toList());
    }

    /**
     *
     * @param data
     * @return
     */

    public EventosDTO AgregarEvento(@Valid EventosDTO data) {
        if (data == null || data.getNombreEvento() == null || data.getNombreEvento().isEmpty()) {
            throw new IllegalArgumentException("Llenar campo");
        }
        try{
            EventosEntity entity = ConvertirAEntity(data);
            EventosEntity EventoGuardado = repo.save(entity);
            return ConvertirADTO(EventoGuardado);
        }catch (Exception e){
            log.error("Error al registrar el evento " + e.getMessage());
            throw new ExceptionEventoNoRegistrado("Error al registrar el evento " + e.getMessage());
        }

    }

    /**
     *
     * @param data
     * @return
     */

    private EventosEntity ConvertirAEntity(EventosDTO data){
        EventosEntity entity = new EventosEntity();
        entity.setIdEvento(data.getIdEvento());
        entity.setIdDestino(data.getIdDestino());
        entity.setNombreEvento(data.getNombreEvento());
        entity.setTipoEvento(data.getTipoEvento());
        entity.setLugarEvento(data.getLugarEvento());
        entity.setFechaEvento(data.getFechaEvento());
        entity.setDescripcionEvento(data.getDescripcionEvento());
        entity.setImage_url(data.getImage_url());
        return entity;
    }

    /**
     * 1. Se declara el metodo convertirADTO el cual recibirá un Entity y retornará un DTO
     * @param rangoEntity
     * @return
     */

    private EventosDTO ConvertirADTO(EventosEntity rangoEntity){
        EventosDTO dto = new EventosDTO();
        dto.setIdEvento(rangoEntity.getIdEvento());
        dto.setIdDestino(rangoEntity.getIdDestino());
        dto.setNombreEvento(rangoEntity.getNombreEvento());
        dto.setTipoEvento(rangoEntity.getTipoEvento());
        dto.setLugarEvento(rangoEntity.getLugarEvento());
        dto.setFechaEvento(rangoEntity.getFechaEvento());
        dto.setDescripcionEvento(rangoEntity.getDescripcionEvento());
        dto.setImage_url(rangoEntity.getImage_url());
        return dto;
    }


    public EventosDTO ActualizarEvento(Long id, @Valid EventosDTO json) {
        EventosEntity existente = repo.findById(id).orElseThrow(() -> new ExceptionRangoNoEncontrado("Evento no encontrado"));

        existente.setIdDestino(json.getIdDestino());
        existente.setNombreEvento(json.getNombreEvento());
        existente.setTipoEvento(json.getTipoEvento());
        existente.setLugarEvento(json.getLugarEvento());
        existente.setFechaEvento(json.getFechaEvento());
        existente.setDescripcionEvento(json.getDescripcionEvento());
        existente.setImage_url(json.getImage_url());

        EventosEntity actualizado = repo.save(existente);
        return ConvertirADTO(actualizado);
    }

    public boolean BorrarEvento(Long id) {
        try {

            EventosEntity eixistente = repo.findById(id).orElse(null);
            if (eixistente != null) {
                repo.deleteById(id);

                return true;
            } else {
                return false;
            }
        }catch (EmptyResultDataAccessException e){
            throw new EmptyResultDataAccessException("Evento no encontrado " + id + "para eliminar ", 1 );
        }

    }
}
