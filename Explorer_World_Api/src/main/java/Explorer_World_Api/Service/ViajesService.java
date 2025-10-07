package Explorer_World_Api.Service;

import Explorer_World_Api.Entities.UsuarioEntity;
import Explorer_World_Api.Entities.ViajesEntity;
import Explorer_World_Api.Exceptions.ExceptionRangoNoRegistrado;
import Explorer_World_Api.Model.DTO.UsuarioDTO;
import Explorer_World_Api.Model.DTO.ViajesDTO;
import Explorer_World_Api.Repositories.ViajesRepository;
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
public class ViajesService {

    @Autowired
    private ViajesRepository repo;

    //---------Paginacion
    public Page<ViajesDTO> PaginacionViajes(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ViajesEntity> pageEntity = repo.findAll(pageable);
        return pageEntity.map(this::ConvertirADTO);
    }

    //---------Obtener datos
    public List<ViajesDTO> ObtenerViaje() {
        List<ViajesEntity> list = repo.findAll();
        return list.stream()
                .map(this::ConvertirADTO)
                .collect(Collectors.toList());
    }

    /**
     *
     * @param data
     * @return
     */

    public ViajesDTO AgregarViaje(@Valid ViajesDTO data) {
        if (data == null || data.getNombreViaje() == null || data.getNombreViaje().isEmpty()) {
            throw new IllegalArgumentException("El viaje no pueden ser nulos");
        }
        try{
            ViajesEntity entity = ConvertirAEntity(data);
            ViajesEntity EventoGuardado = repo.save(entity);
            return ConvertirADTO(EventoGuardado);
        }catch (Exception e){
            log.error("Error al registrar " + e.getMessage());
            throw new ExceptionRangoNoRegistrado("Error al registrar el viaje " + e.getMessage());
        }

    }

    /**
     *
     * @param data
     * @return
     */

    private ViajesEntity ConvertirAEntity(ViajesDTO data){
        ViajesEntity entity = new ViajesEntity();
        entity.setIdViaje(data.getIdViaje());
        entity.setIdCliente(data.getIdCliente());
        entity.setIdEmpleado(data.getIdEmpleado());
        entity.setIdTransporte(data.getIdTransporte());
        entity.setPrecio(data.getPrecio());
        entity.setNombreViaje(data.getNombreViaje());
        entity.setDestino(data.getDestino());
        entity.setFecha(data.getFecha());
        entity.setEstado(data.getEstado());
        entity.setIdHorario(data.getIdHorario());
        entity.setFechaSalida(data.getFechaSalida());
        entity.setFechaRegreso(data.getFechaRegreso());
        return entity;
    }

    /**
     * 1. Se declara el metodo convertirADTO el cual recibirá un Entity y retornará un DTO
     * @param viajeEntity
     * @return
     */

    private ViajesDTO ConvertirADTO(ViajesEntity viajeEntity){
        ViajesDTO dto = new ViajesDTO();
        dto.setIdViaje(viajeEntity.getIdViaje());
        dto.setIdCliente(viajeEntity.getIdCliente());
        dto.setIdEmpleado(viajeEntity.getIdEmpleado());
        dto.setIdTransporte(viajeEntity.getIdTransporte());
        dto.setPrecio(viajeEntity.getPrecio());
        dto.setNombreViaje(viajeEntity.getNombreViaje());
        dto.setDestino(viajeEntity.getDestino());
        dto.setFecha(viajeEntity.getFecha());
        dto.setEstado(viajeEntity.getEstado());
        dto.setIdHorario(viajeEntity.getIdHorario());
        dto.setFechaSalida(viajeEntity.getFechaSalida());
        dto.setFechaRegreso(viajeEntity.getFechaRegreso());
        return dto;
    }


    public ViajesDTO ActualizarViaje(Long id, @Valid ViajesDTO json) {
        ViajesEntity existente = repo.findById(id).orElseThrow(() -> new ExceptionRangoNoRegistrado("Viaje no encontrado"));
        existente.setIdCliente(json.getIdCliente());
        existente.setIdEmpleado(json.getIdEmpleado());
        existente.setIdTransporte(json.getIdTransporte());
        existente.setPrecio(json.getPrecio());
        existente.setNombreViaje(json.getNombreViaje());
        existente.setDestino(json.getDestino());
        existente.setFecha(json.getFecha());
        existente.setEstado(json.getEstado());
        existente.setIdHorario(json.getIdHorario());
        existente.setFechaSalida(json.getFechaSalida());
        existente.setFechaRegreso(json.getFechaRegreso());

        ViajesEntity actualizado = repo.save(existente);
        return ConvertirADTO(actualizado);
    }

    public boolean BorrarViaje(Long id) {
        try {

            ViajesEntity existente = repo.findById(id).orElse(null);
            if (existente != null) {
                repo.deleteById(id);

                return true;
            } else {
                return false;
            }
        }catch (EmptyResultDataAccessException e){
            throw new EmptyResultDataAccessException("Viaje no encontrado " + id + "para eliminar ", 1 );
        }

    }
}
