package Explorer_World_Api.Service;

import Explorer_World_Api.Entities.ServiciosEntity;
import Explorer_World_Api.Entities.TransporteEntity;
import Explorer_World_Api.Exceptions.ExceptionTransporteNoEncontrado;
import Explorer_World_Api.Exceptions.ExceptionTransporteNoRegistrado;
import Explorer_World_Api.Model.DTO.ServiciosDTO;
import Explorer_World_Api.Model.DTO.TransporteDTO;
import Explorer_World_Api.Repositories.TransporteRepository;
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
public class TransporteService {

    @Autowired
    TransporteRepository repository;

    //---------Paginacion
    public Page<TransporteDTO> PaginacionTransporte(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<TransporteEntity> pageEntity = repository.findAll(pageable);
        return pageEntity.map(this::ConvertirADTO);
    }

    //---------Obtener datos
    public List<TransporteDTO> ObtenerTransporte(){
        List<TransporteEntity> list = repository.findAll();
        return list.stream()
                .map(this::ConvertirADTO)
                .collect(Collectors.toList());
    }
    private TransporteDTO ConvertirADTO(TransporteEntity transporte){
        TransporteDTO dto = new TransporteDTO();
        dto.setIdTransporte(transporte.getIdTransporte());
        dto.setMarcaTransporte(transporte.getMarcaTransporte());
        dto.setNumAsientos(transporte.getNumAsientos());
        dto.setModeloTransporte(transporte.getModeloTransporte());
        dto.setPlacaTransporte(transporte.getPlacaTransporte());
        dto.setEstadoTransporte(transporte.getEstadoTransporte());
        dto.setImage_url(transporte.getImage_url());
        return dto;
    }

    public TransporteDTO AgregarTransporte(@Valid TransporteDTO datos) {
        if (datos == null){
            throw new IllegalArgumentException("Datos no pueden ser nulos");

        }
        try {
            TransporteEntity entity = ConvertirAEntity(datos);
            TransporteEntity nuevoTransporte = repository.save(entity);
            return ConvertirADTO(nuevoTransporte);

        }catch (Exception e){
            log.error("Error al registrar el transporte " + e.getMessage());
            throw new ExceptionTransporteNoRegistrado("Error al registrar el transporte " + e.getMessage());
        }
    }

    private TransporteEntity ConvertirAEntity(TransporteDTO datos){
        TransporteEntity entity = new TransporteEntity();
        entity.setIdTransporte(datos.getIdTransporte());
        entity.setMarcaTransporte(datos.getMarcaTransporte());
        entity.setNumAsientos(datos.getNumAsientos());
        entity.setModeloTransporte(datos.getModeloTransporte());
        entity.setPlacaTransporte(datos.getPlacaTransporte());
        entity.setEstadoTransporte(datos.getEstadoTransporte());
        entity.setImage_url(datos.getImage_url());
        return entity;
    }

    public TransporteDTO ActualizarTransporte(Long id, @Valid TransporteDTO json) {
        TransporteEntity transporte = repository.findById(id).orElseThrow(() -> new ExceptionTransporteNoEncontrado("El transporte no fue encontrado"));
        transporte.setMarcaTransporte(json.getMarcaTransporte());
        transporte.setNumAsientos(json.getNumAsientos());
        transporte.setModeloTransporte(json.getModeloTransporte());
        transporte.setPlacaTransporte(json.getPlacaTransporte());
        transporte.setEstadoTransporte(json.getEstadoTransporte());
        transporte.setImage_url(json.getImage_url());

        TransporteEntity TransporteActualizado = repository.save(transporte);
        return ConvertirADTO(TransporteActualizado);
    }

    public boolean BorrarTransporte(Long id) {
        try {
            TransporteEntity existe = repository.findById(id).orElse(null);
            if (existe != null){
                repository.deleteById(id);
                return true;
            }else {
                return false;

            }
        }catch (EmptyResultDataAccessException e){
            throw new EmptyResultDataAccessException("No se encontro el transporte " + id + " para eliminar. ", 1);
        }
    }
}
