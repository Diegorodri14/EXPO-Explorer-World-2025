package Explorer_World_Api.Service;

import Explorer_World_Api.Entities.BoletosEntity;
import Explorer_World_Api.Exceptions.ExceptionRangoNoRegistrado;
import Explorer_World_Api.Model.DTO.BoletosDTO;
import Explorer_World_Api.Repositories.BoletosRepository;
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
public class BoletosService {

    @Autowired
    private BoletosRepository repo;

    //---------Paginacion
    public Page<BoletosDTO> PaginacionBoletos(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<BoletosEntity> pageEntity = repo.findAll(pageable);
        return pageEntity.map(this::ConvertirADTO);
    }

    //---------Obtener datos
    public List<BoletosDTO> ObtenerBoletos() {
        List<BoletosEntity> list = repo.findAll();
        return list.stream()
                .map(this::ConvertirADTO)
                .collect(Collectors.toList());
    }

    /**
     *
     * @param data
     * @return
     */

    public BoletosDTO AgregarBoletos(@Valid BoletosDTO data) {
        if (data == null || data.getAsientos() == null || data.getAsientos().isEmpty()) {
            throw new IllegalArgumentException("Campo no pueden ser nulos");
        }
        try{
            BoletosEntity entity = ConvertirAEntity(data);
            BoletosEntity EventoGuardado = repo.save(entity);
            return ConvertirADTO(EventoGuardado);
        }catch (Exception e){
            log.error("Error al registrar " + e.getMessage());
            throw new ExceptionRangoNoRegistrado("Error al registrar el Boleto " + e.getMessage());
        }

    }

    /**
     *
     * @param data
     * @return
     */

    private BoletosEntity ConvertirAEntity(BoletosDTO data) {
        BoletosEntity entity = new BoletosEntity();
        entity.setIdBoleto(data.getIdBoleto());
        entity.setIdReservacion(data.getIdReservacion());
        entity.setIdViaje(data.getIdViaje());
        entity.setFechaVencimiento(data.getFechaVencimiento());
        entity.setMonto(data.getMonto());
        entity.setAsientos(data.getAsientos());
        return entity;
    }

    /**
     * 1. Se declara el metodo convertirADTO el cual recibirá un Entity y retornará un DTO
     * @param entity
     * @return
     */

    private BoletosDTO ConvertirADTO(BoletosEntity entity) {
        BoletosDTO dto = new BoletosDTO();
        dto.setIdBoleto(entity.getIdBoleto());
        dto.setIdReservacion(entity.getIdReservacion());
        dto.setIdViaje(entity.getIdViaje());
        dto.setFechaVencimiento(entity.getFechaVencimiento());
        dto.setMonto(entity.getMonto());
        dto.setAsientos(entity.getAsientos());
        return dto;
    }


    public BoletosDTO ActualizarBoletos(Long id, @Valid BoletosDTO json) {
        BoletosEntity existente = repo.findById(id).orElseThrow(() -> new ExceptionRangoNoRegistrado("Boleto no encontrado"));

        existente.setIdReservacion(json.getIdReservacion());
        existente.setIdViaje(json.getIdViaje());
        existente.setFechaVencimiento(json.getFechaVencimiento());
        existente.setMonto(json.getMonto());
        existente.setAsientos(json.getAsientos());

        BoletosEntity actualizado = repo.save(existente);
        return ConvertirADTO(actualizado);
    }

    public boolean BorrarBoletos(Long id) {
        try {

            BoletosEntity existente = repo.findById(id).orElse(null);
            if (existente != null) {
                repo.deleteById(id);

                return true;
            } else {
                return false;
            }
        }catch (EmptyResultDataAccessException e){
            throw new EmptyResultDataAccessException("Boleto no encontrado " + id + "para eliminar ", 1 );
        }

    }
}
