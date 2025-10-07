package Explorer_World_Api.Service;

import Explorer_World_Api.Entities.HorariosEntity;
import Explorer_World_Api.Entities.PagosEntity;
import Explorer_World_Api.Exceptions.ExceptionRangoNoRegistrado;
import Explorer_World_Api.Model.DTO.HorariosDTO;
import Explorer_World_Api.Model.DTO.PagosDTO;
import Explorer_World_Api.Repositories.PagosRepository;
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
public class PagosService {
    @Autowired
    private PagosRepository repo;

    //---------Paginacion
    public Page<PagosDTO> PaginacionPagos(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<PagosEntity> pageEntity = repo.findAll(pageable);
        return pageEntity.map(this::ConvertirADTO);
    }

    //---------Obtener datos
    public List<PagosDTO> ObtenerPagos() {
        List<PagosEntity> list = repo.findAll();
        return list.stream()
                .map(this::ConvertirADTO)
                .collect(Collectors.toList());
    }

    /**
     *
     * @param data
     * @return
     */

    public PagosDTO AgregarPagos(@Valid PagosDTO data) {
        if (data == null || data.getEstadoPago() == null || data.getEstadoPago().isEmpty()) {
            throw new IllegalArgumentException("Campo no pueden ser nulos");
        }
        try{
            PagosEntity entity = ConvertirAEntity(data);
            PagosEntity EventoGuardado = repo.save(entity);
            return ConvertirADTO(EventoGuardado);
        }catch (Exception e){
            log.error("Error al registrar " + e.getMessage());
            throw new ExceptionRangoNoRegistrado("Error al registrar el Pago " + e.getMessage());
        }

    }

    /**
     *
     * @param data
     * @return
     */

    private PagosEntity ConvertirAEntity(PagosDTO data) {
        PagosEntity entity = new PagosEntity();
        entity.setIdPago(data.getIdPago());
        entity.setIdReservacion(data.getIdReservacion());
        entity.setMetodoPago(data.getMetodoPago());
        entity.setPago(data.getPago());
        entity.setFechaPago(data.getFechaPago());
        entity.setEstadoPago(data.getEstadoPago());
        return entity;
    }

    /**
     * 1. Se declara el metodo convertirADTO el cual recibirá un Entity y retornará un DTO
     * @param entity
     * @return
     */

    private PagosDTO ConvertirADTO(PagosEntity entity) {
        PagosDTO dto = new PagosDTO();
        dto.setIdPago(entity.getIdPago());
        dto.setIdReservacion(entity.getIdReservacion());
        dto.setMetodoPago(entity.getMetodoPago());
        dto.setPago(entity.getPago());
        dto.setFechaPago(entity.getFechaPago());
        dto.setEstadoPago(entity.getEstadoPago());
        return dto;
    }


    public PagosDTO ActualizarPagos(Long id, @Valid PagosDTO json) {
        PagosEntity existente = repo.findById(id).orElseThrow(() -> new ExceptionRangoNoRegistrado("Pago no encontrado"));

        existente.setIdReservacion(json.getIdReservacion());
        existente.setMetodoPago(json.getMetodoPago());
        existente.setPago(json.getPago());
        existente.setFechaPago(json.getFechaPago());
        existente.setEstadoPago(json.getEstadoPago());

        PagosEntity actualizado = repo.save(existente);
        return ConvertirADTO(actualizado);
    }

    public boolean BorrarPagos(Long id) {
        try {

            PagosEntity existente = repo.findById(id).orElse(null);
            if (existente != null) {
                repo.deleteById(id);

                return true;
            } else {
                return false;
            }
        }catch (EmptyResultDataAccessException e){
            throw new EmptyResultDataAccessException("Pago no encontrado " + id + "para eliminar ", 1 );
        }

    }
}
