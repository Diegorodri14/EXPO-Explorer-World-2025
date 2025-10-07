package Explorer_World_Api.Service;

import Explorer_World_Api.Entities.PagosEntity;
import Explorer_World_Api.Entities.PresupuestoEntity;
import Explorer_World_Api.Exceptions.ExceptionRangoNoRegistrado;
import Explorer_World_Api.Model.DTO.PagosDTO;
import Explorer_World_Api.Model.DTO.PresupuestoDTO;
import Explorer_World_Api.Repositories.PresupuestoRepository;
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
public class PresupuestoService {

    @Autowired
    private PresupuestoRepository repo;

    //---------Paginacion
    public Page<PresupuestoDTO> PaginacionPresupuesto(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<PresupuestoEntity> pageEntity = repo.findAll(pageable);
        return pageEntity.map(this::ConvertirADTO);
    }

    //---------Obtener datos
    public List<PresupuestoDTO> ObtenerPresupuesto() {
        List<PresupuestoEntity> list = repo.findAll();
        return list.stream()
                .map(this::ConvertirADTO)
                .collect(Collectors.toList());
    }

    /**
     *
     * @param data
     * @return
     */

    public PresupuestoDTO AgregarPresupuesto(@Valid PresupuestoDTO data) {
        if (data == null || data.getDetallesPresupuesto() == null || data.getDetallesPresupuesto().isEmpty()) {
            throw new IllegalArgumentException("Campo no pueden ser nulos");
        }
        try{
            PresupuestoEntity entity = ConvertirAEntity(data);
            PresupuestoEntity EventoGuardado = repo.save(entity);
            return ConvertirADTO(EventoGuardado);
        }catch (Exception e){
            log.error("Error al registrar " + e.getMessage());
            throw new ExceptionRangoNoRegistrado("Error al registrar el Presupuesto " + e.getMessage());
        }

    }

    /**
     *
     * @param data
     * @return
     */

    private PresupuestoEntity ConvertirAEntity(PresupuestoDTO data) {
        PresupuestoEntity entity = new PresupuestoEntity();
        entity.setIdPresupuesto(data.getIdPresupuesto());
        entity.setIdCliente(data.getIdCliente());
        entity.setCantidadPresupuesto(data.getCantidadPresupuesto());
        entity.setDetallesPresupuesto(data.getDetallesPresupuesto());
        entity.setFechaRegistro(data.getFechaRegistro());
        return entity;
    }

    /**
     * 1. Se declara el metodo convertirADTO el cual recibirá un Entity y retornará un DTO
     * @param entity
     * @return
     */

    private PresupuestoDTO ConvertirADTO(PresupuestoEntity entity) {
        PresupuestoDTO dto = new PresupuestoDTO();
        dto.setIdPresupuesto(entity.getIdPresupuesto());
        dto.setIdCliente(entity.getIdCliente());
        dto.setCantidadPresupuesto(entity.getCantidadPresupuesto());
        dto.setDetallesPresupuesto(entity.getDetallesPresupuesto());
        dto.setFechaRegistro(entity.getFechaRegistro());
        return dto;
    }


    public PresupuestoDTO ActualizarPresupuesto(Long id, @Valid PresupuestoDTO json) {
        PresupuestoEntity existente = repo.findById(id).orElseThrow(() -> new ExceptionRangoNoRegistrado("Presupuesto no encontrado"));

        existente.setIdCliente(json.getIdCliente());
        existente.setCantidadPresupuesto(json.getCantidadPresupuesto());
        existente.setDetallesPresupuesto(json.getDetallesPresupuesto());
        existente.setFechaRegistro(json.getFechaRegistro());

        PresupuestoEntity actualizado = repo.save(existente);
        return ConvertirADTO(actualizado);
    }

    public boolean BorrarPresupuesto(Long id) {
        try {

            PresupuestoEntity existente = repo.findById(id).orElse(null);
            if (existente != null) {
                repo.deleteById(id);

                return true;
            } else {
                return false;
            }
        }catch (EmptyResultDataAccessException e){
            throw new EmptyResultDataAccessException("Presupuesto no encontrado " + id + "para eliminar ", 1 );
        }

    }
}
