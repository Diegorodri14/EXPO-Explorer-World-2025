package Explorer_World_Api.Service;

import Explorer_World_Api.Entities.DestinosEntity;
import Explorer_World_Api.Entities.EmpleadoEntity;
import Explorer_World_Api.Exceptions.ExceptionRangoNoRegistrado;
import Explorer_World_Api.Model.DTO.DestinosDTO;
import Explorer_World_Api.Model.DTO.EmpleadoDTO;
import Explorer_World_Api.Repositories.EmpleadoRepository;
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
public class EmpleadoService {

    @Autowired
    private EmpleadoRepository repo;

    //---------Paginacion
    public Page<EmpleadoDTO> PaginacionEmpleado(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<EmpleadoEntity> pageEntity = repo.findAll(pageable);
        return pageEntity.map(this::ConvertirADTO);
    }

    //---------Obtener datos
    public List<EmpleadoDTO> ObtenerEmpleado() {
        List<EmpleadoEntity> list = repo.findAll();
        return list.stream()
                .map(this::ConvertirADTO)
                .collect(Collectors.toList());
    }

    /**
     *
     * @param data
     * @return
     */

    public EmpleadoDTO AgregarEmpleado(@Valid EmpleadoDTO data) {
        if (data == null || data.getNombreEmpleado() == null || data.getNombreEmpleado().isEmpty()) {
            throw new IllegalArgumentException("Campo no pueden ser nulos");
        }
        try{
            EmpleadoEntity entity = ConvertirAEntity(data);
            EmpleadoEntity EventoGuardado = repo.save(entity);
            return ConvertirADTO(EventoGuardado);
        }catch (Exception e){
            log.error("Error al registrar " + e.getMessage());
            throw new ExceptionRangoNoRegistrado("Error al registrar el Empleado " + e.getMessage());
        }

    }

    /**
     *
     * @param data
     * @return
     */

    private EmpleadoEntity ConvertirAEntity(EmpleadoDTO data) {
        EmpleadoEntity entity = new EmpleadoEntity();
        entity.setIdEmpleado(data.getIdEmpleado());
        entity.setIdRango(data.getIdRango());
        entity.setNombreEmpleado(data.getNombreEmpleado());
        entity.setApellidoEmpleado(data.getApellidoEmpleado());
        entity.setEmailEmpleado(data.getEmailEmpleado());
        entity.setFechaNacimiento(data.getFechaNacimiento());
        entity.setTelefono(data.getTelefono());
        entity.setDireccion(data.getDireccion());
        entity.setSalario(data.getSalario());
        entity.setImage_url(data.getImage_url());
        return entity;
    }

    /**
     * 1. Se declara el metodo convertirADTO el cual recibirá un Entity y retornará un DTO
     * @param entity
     * @return
     */

    private EmpleadoDTO ConvertirADTO(EmpleadoEntity entity) {
        EmpleadoDTO dto = new EmpleadoDTO();
        dto.setIdEmpleado(entity.getIdEmpleado());
        dto.setIdRango(entity.getIdRango());
        dto.setNombreEmpleado(entity.getNombreEmpleado());
        dto.setApellidoEmpleado(entity.getApellidoEmpleado());
        dto.setEmailEmpleado(entity.getEmailEmpleado());
        dto.setFechaNacimiento(entity.getFechaNacimiento());
        dto.setTelefono(entity.getTelefono());
        dto.setDireccion(entity.getDireccion());
        dto.setSalario(entity.getSalario());
        dto.setImage_url(entity.getImage_url());
        return dto;
    }


    public EmpleadoDTO ActualizarEmpleado(Long id, @Valid EmpleadoDTO json) {
        EmpleadoEntity existente = repo.findById(id).orElseThrow(() -> new ExceptionRangoNoRegistrado("Empleado no encontrado"));

        existente.setIdRango(json.getIdRango());
        existente.setNombreEmpleado(json.getNombreEmpleado());
        existente.setApellidoEmpleado(json.getApellidoEmpleado());
        existente.setEmailEmpleado(json.getEmailEmpleado());
        existente.setFechaNacimiento(json.getFechaNacimiento());
        existente.setTelefono(json.getTelefono());
        existente.setDireccion(json.getDireccion());
        existente.setSalario(json.getSalario());
        existente.setImage_url(json.getImage_url());

        EmpleadoEntity actualizado = repo.save(existente);
        return ConvertirADTO(actualizado);
    }

    public boolean BorrarEmpleado(Long id) {
        try {

            EmpleadoEntity existente = repo.findById(id).orElse(null);
            if (existente != null) {
                repo.deleteById(id);

                return true;
            } else {
                return false;
            }
        }catch (EmptyResultDataAccessException e){
            throw new EmptyResultDataAccessException("Empleado no encontrado " + id + "para eliminar ", 1 );
        }

    }
}
