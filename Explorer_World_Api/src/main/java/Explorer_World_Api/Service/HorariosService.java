package Explorer_World_Api.Service;

import Explorer_World_Api.Entities.FacturasEntity;
import Explorer_World_Api.Entities.HorariosEntity;
import Explorer_World_Api.Exceptions.ExceptionRangoNoRegistrado;
import Explorer_World_Api.Model.DTO.FacturasDTO;
import Explorer_World_Api.Model.DTO.HorariosDTO;
import Explorer_World_Api.Repositories.HorariosRepository;
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
public class HorariosService {
    @Autowired
    private HorariosRepository repo;

    //---------Paginacion
    public Page<HorariosDTO> PaginacionHorarios(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<HorariosEntity> pageEntity = repo.findAll(pageable);
        return pageEntity.map(this::ConvertirADTO);
    }

    //---------Obtener datos
    public List<HorariosDTO> ObtenerHorarios() {
        List<HorariosEntity> list = repo.findAll();
        return list.stream()
                .map(this::ConvertirADTO)
                .collect(Collectors.toList());
    }

    /**
     *
     * @param data
     * @return
     */

    public HorariosDTO AgregarHorarios(@Valid HorariosDTO data) {
        if (data == null || data.getTipoHorario() == null || data.getTipoHorario().isEmpty()) {
            throw new IllegalArgumentException("Campo no pueden ser nulos");
        }
        try{
            HorariosEntity entity = ConvertirAEntity(data);
            HorariosEntity EventoGuardado = repo.save(entity);
            return ConvertirADTO(EventoGuardado);
        }catch (Exception e){
            log.error("Error al registrar " + e.getMessage());
            throw new ExceptionRangoNoRegistrado("Error al registrar el horario " + e.getMessage());
        }

    }

    /**
     *
     * @param data
     * @return
     */

    private HorariosEntity ConvertirAEntity(HorariosDTO data) {
        HorariosEntity entity = new HorariosEntity();
        entity.setIdHorario(data.getIdHorario());
        entity.setHora(data.getHora());
        entity.setTipoHorario(data.getTipoHorario());
        entity.setDisponibilidad(data.getDisponibilidad());
        return entity;
    }

    /**
     * 1. Se declara el metodo convertirADTO el cual recibirá un Entity y retornará un DTO
     * @param entity
     * @return
     */

    private HorariosDTO ConvertirADTO(HorariosEntity entity) {
        HorariosDTO dto = new HorariosDTO();
        dto.setIdHorario(entity.getIdHorario());
        dto.setHora(entity.getHora());
        dto.setTipoHorario(entity.getTipoHorario());
        dto.setDisponibilidad(entity.getDisponibilidad());
        return dto;
    }


    public HorariosDTO ActualizarHorarios(Long id, @Valid HorariosDTO json) {
        HorariosEntity existente = repo.findById(id).orElseThrow(() -> new ExceptionRangoNoRegistrado("Horario no encontrado"));

        existente.setHora(json.getHora());
        existente.setTipoHorario(json.getTipoHorario());
        existente.setDisponibilidad(json.getDisponibilidad());

        HorariosEntity actualizado = repo.save(existente);
        return ConvertirADTO(actualizado);
    }

    public boolean BorrarHorarios(Long id) {
        try {

            HorariosEntity existente = repo.findById(id).orElse(null);
            if (existente != null) {
                repo.deleteById(id);


                return true;
            } else {
                return false;
            }
        }catch (EmptyResultDataAccessException e){
            throw new EmptyResultDataAccessException("Horario no encontrado " + id + "para eliminar ", 1 );
        }

    }
}
