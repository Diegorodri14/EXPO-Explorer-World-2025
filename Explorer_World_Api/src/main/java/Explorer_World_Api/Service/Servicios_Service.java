package Explorer_World_Api.Service;

import Explorer_World_Api.Entities.ServiciosEntity;
import Explorer_World_Api.Exceptions.ExceptionRangoNoRegistrado;
import Explorer_World_Api.Model.DTO.ServiciosDTO;
import Explorer_World_Api.Repositories.ServiciosRepository;
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
public class Servicios_Service {

    @Autowired
    private ServiciosRepository repo;

    //---------Paginacion
    public Page<ServiciosDTO> PaginacionServicio(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ServiciosEntity> pageEntity = repo.findAll(pageable);
        return pageEntity.map(this::ConvertirADTO);
    }

    //---------Obtener datos
    public List<ServiciosDTO> ObtenerServicio() {
        List<ServiciosEntity> list = repo.findAll();
        return list.stream()
                .map(this::ConvertirADTO)
                .collect(Collectors.toList());
    }

    /**
     *
     * @param data
     * @return
     */

    public ServiciosDTO AgregarServicio(@Valid ServiciosDTO data) {
        if (data == null || data.getNombreServicio() == null || data.getNombreServicio().isEmpty()) {
            throw new IllegalArgumentException("Campo no pueden ser nulos");
        }
        try{
            ServiciosEntity entity = ConvertirAEntity(data);
            ServiciosEntity EventoGuardado = repo.save(entity);
            return ConvertirADTO(EventoGuardado);
        }catch (Exception e){
            log.error("Error al registrar " + e.getMessage());
            throw new ExceptionRangoNoRegistrado("Error al registrar el cliente " + e.getMessage());
        }

    }

    /**
     *
     * @param data
     * @return
     */

    private ServiciosEntity ConvertirAEntity(ServiciosDTO data) {
        ServiciosEntity entity = new ServiciosEntity();
        entity.setIdServicio(data.getIdServicio());
        entity.setNombreServicio(data.getNombreServicio());
        entity.setDescripcion(data.getDescripcion());
        entity.setCosto(data.getCosto());
        return entity;
    }

    /**
     * 1. Se declara el metodo convertirADTO el cual recibirá un Entity y retornará un DTO
     * @param entity
     * @return
     */

    private ServiciosDTO ConvertirADTO(ServiciosEntity entity) {
        ServiciosDTO dto = new ServiciosDTO();
        dto.setIdServicio(entity.getIdServicio());
        dto.setNombreServicio(entity.getNombreServicio());
        dto.setDescripcion(entity.getDescripcion());
        dto.setCosto(entity.getCosto());
        return dto;
    }


    public ServiciosDTO ActualizarServicio(Long id, @Valid ServiciosDTO json) {
        ServiciosEntity existente = repo.findById(id).orElseThrow(() -> new ExceptionRangoNoRegistrado("Servicio no encontrado"));

        existente.setNombreServicio(json.getNombreServicio());
        existente.setDescripcion(json.getDescripcion());
        existente.setCosto(json.getCosto());

        ServiciosEntity actualizado = repo.save(existente);
        return ConvertirADTO(actualizado);
    }

    public boolean BorrarServicio(Long id) {
        try {

            ServiciosEntity existente = repo.findById(id).orElse(null);
            if (existente != null) {
                repo.deleteById(id);

                return true;
            } else {
                return false;
            }
        }catch (EmptyResultDataAccessException e){
            throw new EmptyResultDataAccessException("Servicio no encontrado " + id + "para eliminar ", 1 );
        }

    }
}
