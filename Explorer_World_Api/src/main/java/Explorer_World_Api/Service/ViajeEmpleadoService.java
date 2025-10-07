package Explorer_World_Api.Service;

import Explorer_World_Api.Entities.ViajeEmpleadoEntity;
import Explorer_World_Api.Exceptions.ExceptionViajeEmpleadoNoRegistrado;
import Explorer_World_Api.Model.DTO.ViajeEmpleadoDTO;
import Explorer_World_Api.Repositories.ViajeEmpleadoRepository;
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
public class ViajeEmpleadoService {
    @Autowired
    private ViajeEmpleadoRepository repo;

    //---------Paginacion
    public Page<ViajeEmpleadoDTO> PaginacionViajeEmpleado(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ViajeEmpleadoEntity> pageEntity = repo.findAll(pageable);
        return pageEntity.map(this::ConvertirADTO);
    }

    //---------Obtener datos
    public List<ViajeEmpleadoDTO> ObtenerViajeEmpleado() {
        List<ViajeEmpleadoEntity> list = repo.findAll();
        return list.stream()
                .map(this::ConvertirADTO)
                .collect(Collectors.toList());
    }

    /**
     * Agregar un nuevo ViajeEmpleado
     * @param data
     * @return
     */
    public ViajeEmpleadoDTO AgregarViajeEmpleado(@Valid ViajeEmpleadoDTO data) {
        if (data == null || data.getFechaInicioParticipacion() == null) {
            throw new IllegalArgumentException("Campos obligatorios no pueden ser nulos");
        }

        // Validar si ya existe la combinación única
        boolean existe = repo.existsByIdViajeAndIdEmpleado(
                data.getIdViaje(),
                data.getIdEmpleado()
        );

        if (existe) {
            throw new IllegalArgumentException("Ya existe un registro con la misma combinación de Viaje y Empleado");
        }

        // Validación de fechas
        if (data.getFechaFinParticipacion() != null &&
                data.getFechaFinParticipacion().before(data.getFechaInicioParticipacion())) {
            throw new IllegalArgumentException("La fecha fin debe ser posterior o igual a la fecha inicio");
        }

        try{
            ViajeEmpleadoEntity entity = ConvertirAEntity(data);
            ViajeEmpleadoEntity ViajeEmpleadoGuardado = repo.save(entity);
            return ConvertirADTO(ViajeEmpleadoGuardado);
        }catch (Exception e){
            log.error("Error al registrar " + e.getMessage());
            throw new ExceptionViajeEmpleadoNoRegistrado("Error al registrar el ViajeEmpleado " + e.getMessage());
        }
    }

    /**
     * Convertir DTO a Entity
     * @param data
     * @return
     */
    private ViajeEmpleadoEntity ConvertirAEntity(ViajeEmpleadoDTO data) {
        ViajeEmpleadoEntity entity = new ViajeEmpleadoEntity();
        entity.setIdViajeEmpleado(data.getIdViajeEmpleado());
        entity.setIdViaje(data.getIdViaje());
        entity.setIdEmpleado(data.getIdEmpleado());
        entity.setRolEnViaje(data.getRolEnViaje());
        entity.setFechaInicioParticipacion(data.getFechaInicioParticipacion());
        entity.setFechaFinParticipacion(data.getFechaFinParticipacion());
        return entity;
    }

    /**
     * Convertir Entity a DTO
     * @param entity
     * @return
     */
    private ViajeEmpleadoDTO ConvertirADTO(ViajeEmpleadoEntity entity) {
        ViajeEmpleadoDTO dto = new ViajeEmpleadoDTO();
        dto.setIdViajeEmpleado(entity.getIdViajeEmpleado());
        dto.setIdViaje(entity.getIdViaje());
        dto.setIdEmpleado(entity.getIdEmpleado());
        dto.setRolEnViaje(entity.getRolEnViaje());
        dto.setFechaInicioParticipacion(entity.getFechaInicioParticipacion());
        dto.setFechaFinParticipacion(entity.getFechaFinParticipacion());
        return dto;
    }

    /**
     * Actualizar ViajeEmpleado existente
     * @param id
     * @param json
     * @return
     */
    public ViajeEmpleadoDTO ActualizarViajeEmpleado(Long id, @Valid ViajeEmpleadoDTO json) {
        ViajeEmpleadoEntity existente = repo.findById(id).orElseThrow(() -> new ExceptionViajeEmpleadoNoRegistrado("ViajeEmpleado no encontrado"));

        // Validación de fechas
        if (json.getFechaFinParticipacion() != null &&
                json.getFechaFinParticipacion().before(json.getFechaInicioParticipacion())) {
            throw new IllegalArgumentException("La fecha fin debe ser posterior o igual a la fecha inicio");
        }

        existente.setIdViaje(json.getIdViaje());
        existente.setIdEmpleado(json.getIdEmpleado());
        existente.setRolEnViaje(json.getRolEnViaje());
        existente.setFechaInicioParticipacion(json.getFechaInicioParticipacion());
        existente.setFechaFinParticipacion(json.getFechaFinParticipacion());

        ViajeEmpleadoEntity actualizado = repo.save(existente);
        return ConvertirADTO(actualizado);
    }

    /**
     * Eliminar ViajeEmpleado
     * @param id
     * @return
     */
    public boolean BorrarViajeEmpleado(Long id) {
        try {
            ViajeEmpleadoEntity existente = repo.findById(id).orElse(null);
            if (existente != null) {
                repo.deleteById(id);
                return true;
            } else {
                return false;
            }
        }catch (EmptyResultDataAccessException e){
            throw new EmptyResultDataAccessException("ViajeEmpleado no encontrado con id: " + id + " para eliminar", 1);
        }
    }
}
