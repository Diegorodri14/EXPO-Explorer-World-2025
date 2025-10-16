package Explorer_World_Api.Service;

import Explorer_World_Api.Entities.ClienteEntity;
import Explorer_World_Api.Entities.DestinosEntity;
import Explorer_World_Api.Exceptions.ExceptionRangoNoRegistrado;
import Explorer_World_Api.Model.DTO.ClienteDTO;
import Explorer_World_Api.Model.DTO.DestinosDTO;
import Explorer_World_Api.Repositories.DestinosRepository;
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
public class DestinosService {
    @Autowired
    private DestinosRepository repo;

    //---------Paginacion
    public Page<DestinosDTO> PaginacionDestino(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<DestinosEntity> pageEntity = repo.findAll(pageable);
        return pageEntity.map(this::ConvertirADTO);
    }

    //---------Obtener datos
    public List<DestinosDTO> ObtenerDestinos() {
        List<DestinosEntity> list = repo.findAll();
        return list.stream()
                .map(this::ConvertirADTO)
                .collect(Collectors.toList());
    }

    /**
     *
     * @param data
     * @return
     */

    public DestinosDTO AgregarDestinos(@Valid DestinosDTO data) {
        if (data == null || data.getNombreDestino() == null || data.getNombreDestino().isEmpty()) {
            throw new IllegalArgumentException("Campo no pueden ser nulos");
        }
        try{
            DestinosEntity entity = ConvertirAEntity(data);
            DestinosEntity EventoGuardado = repo.save(entity);
            return ConvertirADTO(EventoGuardado);
        }catch (Exception e){
            log.error("Error al registrar " + e.getMessage());
            throw new ExceptionRangoNoRegistrado("Error al registrar el destino " + e.getMessage());
        }

    }

    /**
     *
     * @param data
     * @return
     */

    private DestinosEntity ConvertirAEntity(DestinosDTO data) {
        DestinosEntity entity = new DestinosEntity();
        entity.setIdDestino(data.getIdDestino());
        entity.setNombreDestino(data.getNombreDestino());
        entity.setLugarDestino(data.getLugarDestino());
        entity.setTipoDestino(data.getTipoDestino());
        entity.setDescripcionDestino(data.getDescripcionDestino());
        return entity;
    }

    /**
     * 1. Se declara el metodo convertirADTO el cual recibirá un Entity y retornará un DTO
     * @param entity
     * @return
     */

    private DestinosDTO ConvertirADTO(DestinosEntity entity) {
        DestinosDTO dto = new DestinosDTO();
        dto.setIdDestino(entity.getIdDestino());
        dto.setNombreDestino(entity.getNombreDestino());
        dto.setLugarDestino(entity.getLugarDestino());
        dto.setTipoDestino(entity.getTipoDestino());
        dto.setDescripcionDestino(entity.getDescripcionDestino());
        return dto;
    }


    public DestinosDTO ActualizarDestino(Long id, @Valid DestinosDTO json) {
        DestinosEntity existente = repo.findById(id).orElseThrow(() -> new ExceptionRangoNoRegistrado("Destino no encontrado"));

        existente.setNombreDestino(json.getNombreDestino());
        existente.setLugarDestino(json.getLugarDestino());
        existente.setTipoDestino(json.getTipoDestino());
        existente.setDescripcionDestino(json.getDescripcionDestino());

        DestinosEntity actualizado = repo.save(existente);
        return ConvertirADTO(actualizado);
    }

    public boolean BorrarDestino(Long id) {
        try {

            DestinosEntity existente = repo.findById(id).orElse(null);
            if (existente != null) {
                repo.deleteById(id);

                return true;
            } else {
                return false;
            }
        }catch (EmptyResultDataAccessException e){
            throw new EmptyResultDataAccessException("Destino no encontrado " + id + "para eliminar ", 1 );
        }

    }
}
