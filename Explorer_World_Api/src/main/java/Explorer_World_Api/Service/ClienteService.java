package Explorer_World_Api.Service;

import Explorer_World_Api.Entities.BoletosEntity;
import Explorer_World_Api.Entities.ClienteEntity;
import Explorer_World_Api.Exceptions.ExceptionRangoNoRegistrado;
import Explorer_World_Api.Model.DTO.BoletosDTO;
import Explorer_World_Api.Model.DTO.ClienteDTO;
import Explorer_World_Api.Repositories.ClienteRepository;
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
public class ClienteService {

    @Autowired
    private ClienteRepository repo;

    //---------Paginacion
    public Page<ClienteDTO> PaginacionCliente(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ClienteEntity> pageEntity = repo.findAll(pageable);
        return pageEntity.map(this::ConvertirADTO);
    }

    //---------Obtener datos
    public List<ClienteDTO> ObtenerCliente() {
        List<ClienteEntity> list = repo.findAll();
        return list.stream()
                .map(this::ConvertirADTO)
                .collect(Collectors.toList());
    }

    /**
     *
     * @param data
     * @return
     */

    public ClienteDTO AgregarCliente(@Valid ClienteDTO data) {
        if (data == null || data.getNombreCliente() == null || data.getNombreCliente().isEmpty()) {
            throw new IllegalArgumentException("Campo no pueden ser nulos");
        }
        try{
            ClienteEntity entity = ConvertirAEntity(data);
            ClienteEntity EventoGuardado = repo.save(entity);
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

    private ClienteEntity ConvertirAEntity(ClienteDTO data) {
        ClienteEntity entity = new ClienteEntity();
        entity.setIdCliente(data.getIdCliente());
        entity.setNombreCliente(data.getNombreCliente());
        entity.setApellidoCliente(data.getApellidoCliente());
        entity.setEmailCliente(data.getEmailCliente());
        entity.setTelefono(data.getTelefono());
        entity.setDireccion(data.getDireccion());
        entity.setDui(data.getDui());
        return entity;
    }

    /**
     * 1. Se declara el metodo convertirADTO el cual recibirá un Entity y retornará un DTO
     * @param entity
     * @return
     */

    private ClienteDTO ConvertirADTO(ClienteEntity entity) {
        ClienteDTO dto = new ClienteDTO();
        dto.setIdCliente(entity.getIdCliente());
        dto.setNombreCliente(entity.getNombreCliente());
        dto.setApellidoCliente(entity.getApellidoCliente());
        dto.setEmailCliente(entity.getEmailCliente());
        dto.setTelefono(entity.getTelefono());
        dto.setDireccion(entity.getDireccion());
        dto.setDui(entity.getDui());
        return dto;
    }


    public ClienteDTO ActualizarCliente(Long id, @Valid ClienteDTO json) {
        ClienteEntity existente = repo.findById(id).orElseThrow(() -> new ExceptionRangoNoRegistrado("Cliente no encontrado"));

        existente.setNombreCliente(json.getNombreCliente());
        existente.setApellidoCliente(json.getApellidoCliente());
        existente.setEmailCliente(json.getEmailCliente());
        existente.setTelefono(json.getTelefono());
        existente.setDireccion(json.getDireccion());
        existente.setDui(json.getDui());

        ClienteEntity actualizado = repo.save(existente);
        return ConvertirADTO(actualizado);
    }

    public boolean BorrarCliente(Long id) {
        try {

            ClienteEntity existente = repo.findById(id).orElse(null);
            if (existente != null) {
                repo.deleteById(id);

                return true;
            } else {
                return false;
            }
        }catch (EmptyResultDataAccessException e){
            throw new EmptyResultDataAccessException("Cliente no encontrado " + id + "para eliminar ", 1 );
        }

    }
}
