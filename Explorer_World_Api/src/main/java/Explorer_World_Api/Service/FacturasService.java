package Explorer_World_Api.Service;

import Explorer_World_Api.Entities.EventosEntity;
import Explorer_World_Api.Entities.FacturasEntity;
import Explorer_World_Api.Exceptions.ExceptionRangoNoRegistrado;
import Explorer_World_Api.Model.DTO.EventosDTO;
import Explorer_World_Api.Model.DTO.FacturasDTO;
import Explorer_World_Api.Repositories.FacturasRepository;
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
public class FacturasService {
    @Autowired
    private FacturasRepository repo;

    //---------Paginacion
    public Page<FacturasDTO> PaginacionFactura(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<FacturasEntity> pageEntity = repo.findAll(pageable);
        return pageEntity.map(this::ConvertirADTO);
    }

    //---------Obtener datos
    public List<FacturasDTO> ObtenerFacturas() {
        List<FacturasEntity> list = repo.findAll();
        return list.stream()
                .map(this::ConvertirADTO)
                .collect(Collectors.toList());
    }

    /**
     *
     * @param data
     * @return
     */

    public FacturasDTO AgregarFacturas(@Valid FacturasDTO data) {
        if (data == null || data.getMetodoPago() == null || data.getMetodoPago().isEmpty()) {
            throw new IllegalArgumentException("Campo no pueden ser nulos");
        }
        try{
            FacturasEntity entity = ConvertirAEntity(data);
            FacturasEntity EventoGuardado = repo.save(entity);
            return ConvertirADTO(EventoGuardado);
        }catch (Exception e){
            log.error("Error al registrar " + e.getMessage());
            throw new ExceptionRangoNoRegistrado("Error al registrar la Factura " + e.getMessage());
        }

    }

    /**
     *
     * @param data
     * @return
     */

    private FacturasEntity ConvertirAEntity(FacturasDTO data) {
        FacturasEntity entity = new FacturasEntity();
        entity.setIdFactura(data.getIdFactura());
        entity.setIdReservacion(data.getIdReservacion());
        entity.setFechaPago(data.getFechaPago());
        entity.setMetodoPago(data.getMetodoPago());
        entity.setMontoTotal(data.getMontoTotal());
        return entity;
    }

    /**
     * 1. Se declara el metodo convertirADTO el cual recibirá un Entity y retornará un DTO
     * @param entity
     * @return
     */

    private FacturasDTO ConvertirADTO(FacturasEntity entity) {
        FacturasDTO dto = new FacturasDTO();
        dto.setIdFactura(entity.getIdFactura());
        dto.setIdReservacion(entity.getIdReservacion());
        dto.setFechaPago(entity.getFechaPago());
        dto.setMetodoPago(entity.getMetodoPago());
        dto.setMontoTotal(entity.getMontoTotal());
        return dto;
    }


    public FacturasDTO ActualizarFacturas(Long id, @Valid FacturasDTO json) {
        FacturasEntity existente = repo.findById(id).orElseThrow(() -> new ExceptionRangoNoRegistrado("Factura no encontrado"));

        existente.setIdReservacion(json.getIdReservacion());
        existente.setFechaPago(json.getFechaPago());
        existente.setMetodoPago(json.getMetodoPago());
        existente.setMontoTotal(json.getMontoTotal());

        FacturasEntity actualizado = repo.save(existente);
        return ConvertirADTO(actualizado);
    }

    public boolean BorrarFacturas(Long id) {
        try {

            FacturasEntity existente = repo.findById(id).orElse(null);
            if (existente != null) {
                repo.deleteById(id);

                return true;
            } else {
                return false;
            }
        }catch (EmptyResultDataAccessException e){
            throw new EmptyResultDataAccessException("Factura no encontrado " + id + "para eliminar ", 1 );
        }

    }
}
