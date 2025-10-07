package Explorer_World_Api.Service;

import Explorer_World_Api.Entities.PresupuestoEntity;
import Explorer_World_Api.Entities.RangoEntity;
import Explorer_World_Api.Exceptions.ExceptionRangoNoRegistrado;
import Explorer_World_Api.Model.DTO.PresupuestoDTO;
import Explorer_World_Api.Model.DTO.RangoDTO;
import Explorer_World_Api.Repositories.RangoRepository;
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
public class RangoService {

    @Autowired
    RangoRepository repo;

    //---------Paginacion
    public Page<RangoDTO> PaginacionRango(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<RangoEntity> pageEntity = repo.findAll(pageable);
        return pageEntity.map(this::ConvertirADTO);
    }

    //---------Obtener datos
    public List<RangoDTO> ObtenerRangos() {
        List<RangoEntity> list = repo.findAll();
        return list.stream()
                .map(this::ConvertirADTO)
                .collect(Collectors.toList());
    }

    /**
     *
     * @param data
     * @return
     */

    public RangoDTO AgregarRango(@Valid RangoDTO data) {
        if (data == null || data.getNombreRango() == null || data.getNombreRango().isEmpty()) {
            throw new IllegalArgumentException("Llenar campo");
        }
        try{
            RangoEntity entity = ConvertirAEntity(data);
            RangoEntity EventoGuardado = repo.save(entity);
            return ConvertirADTO(EventoGuardado);
        }catch (Exception e){
            log.error("Error al registrar un rango " + e.getMessage());
            throw new ExceptionRangoNoRegistrado("Error al registrar el evento " + e.getMessage());
        }

    }

    /**
     *
     * @param data
     * @return
     */

    private RangoEntity ConvertirAEntity(RangoDTO data){
        RangoEntity entity = new RangoEntity();
        entity.setIdRango(data.getIdRango());
        entity.setNombreRango(data.getNombreRango());
        return entity;
    }

    /**
     * 1. Se declara el metodo convertirADTO el cual recibirá un Entity y retornará un DTO
     * @param rangoEntity
     * @return
     */

    private RangoDTO ConvertirADTO(RangoEntity rangoEntity){
        RangoDTO dto = new RangoDTO();
        dto.setIdRango(rangoEntity.getIdRango());
        dto.setNombreRango(rangoEntity.getNombreRango());
        return dto;
    }


    public RangoDTO ActualizarRango(Long id, @Valid RangoDTO json) {
        RangoEntity existente = repo.findById(id).orElseThrow(() -> new ExceptionRangoNoRegistrado("Rango no encontrado"));

        existente.setNombreRango(json.getNombreRango());

        RangoEntity actualizado = repo.save(existente);
        return ConvertirADTO(actualizado);
    }

    public boolean BorrarRango(Long id) {
        try {

            RangoEntity eixistente = repo.findById(id).orElse(null);
            if (eixistente != null) {
                repo.deleteById(id);

                return true;
            } else {
                return false;
            }
        }catch (EmptyResultDataAccessException e){
            throw new EmptyResultDataAccessException("Rango no encontrado " + id + "para eliminar ", 1 );
        }

    }
}
