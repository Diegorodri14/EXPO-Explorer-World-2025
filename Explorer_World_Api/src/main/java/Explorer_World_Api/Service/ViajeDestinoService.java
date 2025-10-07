package Explorer_World_Api.Service;

import Explorer_World_Api.Entities.BoletosEntity;
import Explorer_World_Api.Entities.ViajesDestinoEntity;
import Explorer_World_Api.Exceptions.ExceptionRangoNoRegistrado;
import Explorer_World_Api.Exceptions.ExceptionViajeDestinoNoRegistrado;
import Explorer_World_Api.Model.DTO.ViajeDestinoDTO;
import Explorer_World_Api.Repositories.ViajeDestinoRepository;
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
public class ViajeDestinoService {
    @Autowired
    private ViajeDestinoRepository repo;

    //---------Paginacion
    public Page<ViajeDestinoDTO> PaginacionViajeDestino(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ViajesDestinoEntity> pageEntity = repo.findAll(pageable);
        return pageEntity.map(this::ConvertirADTO);
    }

    //---------Obtener datos
    public List<ViajeDestinoDTO> ObtenerViajeDestino() {
        List<ViajesDestinoEntity> list = repo.findAll();
        return list.stream()
                .map(this::ConvertirADTO)
                .collect(Collectors.toList());
    }

    /**
     * Agregar un nuevo ViajeDestino
     * @param data
     * @return
     */
    public ViajeDestinoDTO AgregarViajeDestino(@Valid ViajeDestinoDTO data) {
        if (data == null || data.getOrdenDestino() == null) {
            throw new IllegalArgumentException("Campos obligatorios no pueden ser nulos");
        }

        // Validar si ya existe la combinación única
        boolean existe = repo.existsByIdViajeAndIdDestinoAndOrdenDestino(
                data.getIdViaje(),
                data.getIdDestino(),
                data.getOrdenDestino()
        );

        if (existe) {
            throw new IllegalArgumentException("Ya existe un registro con la misma combinación de Viaje, Destino y Orden");
        }

        try{
            ViajesDestinoEntity entity = ConvertirAEntity(data);
            ViajesDestinoEntity ViajeDestinoGuardado = repo.save(entity);
            return ConvertirADTO(ViajeDestinoGuardado);
        }catch (Exception e){
            log.error("Error al registrar " + e.getMessage());
            throw new ExceptionViajeDestinoNoRegistrado("Error al registrar el ViajeDestino " + e.getMessage());
        }
    }
    /**
     * Convertir DTO a Entity
     * @param data
     * @return
     */
    private ViajesDestinoEntity ConvertirAEntity(ViajeDestinoDTO data) {
        ViajesDestinoEntity entity = new ViajesDestinoEntity();
        entity.setIdViajeDestino(data.getIdViajeDestino());
        entity.setIdViaje(data.getIdViaje());
        entity.setIdDestino(data.getIdDestino());
        entity.setOrdenDestino(data.getOrdenDestino());
        entity.setDuracionHoras(data.getDuracionHoras());
        entity.setFechaInicioEstancia(data.getFechaInicioEstancia());
        entity.setActividadesPlanificadas(data.getActividadesPlanificadas());
        return entity;
    }

    /**
     * Convertir Entity a DTO
     * @param entity
     * @return
     */
    private ViajeDestinoDTO ConvertirADTO(ViajesDestinoEntity entity) {
        ViajeDestinoDTO dto = new ViajeDestinoDTO();
        dto.setIdViajeDestino(entity.getIdViajeDestino());
        dto.setIdViaje(entity.getIdViaje());
        dto.setIdDestino(entity.getIdDestino());
        dto.setOrdenDestino(entity.getOrdenDestino());
        dto.setDuracionHoras(entity.getDuracionHoras());
        dto.setFechaInicioEstancia(entity.getFechaInicioEstancia());
        dto.setActividadesPlanificadas(entity.getActividadesPlanificadas());
        return dto;
    }

    /**
     * Actualizar ViajeDestino existente
     * @param id
     * @param json
     * @return
     */
    public ViajeDestinoDTO ActualizarViajeDestino(Long id, @Valid ViajeDestinoDTO json) {
        ViajesDestinoEntity existente = repo.findById(id).orElseThrow(() -> new ExceptionViajeDestinoNoRegistrado("ViajeDestino no encontrado"));

        existente.setIdViaje(json.getIdViaje());
        existente.setIdDestino(json.getIdDestino());
        existente.setOrdenDestino(json.getOrdenDestino());
        existente.setDuracionHoras(json.getDuracionHoras());
        existente.setFechaInicioEstancia(json.getFechaInicioEstancia());
        existente.setActividadesPlanificadas(json.getActividadesPlanificadas());

        ViajesDestinoEntity actualizado = repo.save(existente);
        return ConvertirADTO(actualizado);
    }

    /**
     * Eliminar ViajeDestino
     * @param id
     * @return
     */
    public boolean BorrarViajeDestino(Long id) {
        try {
            ViajesDestinoEntity existente = repo.findById(id).orElse(null);
            if (existente != null) {
                repo.deleteById(id);
                return true;
            } else {
                return false;
            }
        }catch (EmptyResultDataAccessException e){
            throw new EmptyResultDataAccessException("ViajeDestino no encontrado con id: " + id + " para eliminar", 1);
        }
    }
}
