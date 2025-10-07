package Explorer_World_Api.Repositories;

import Explorer_World_Api.Entities.ViajesDestinoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ViajeDestinoRepository extends JpaRepository<ViajesDestinoEntity, Long> {
    boolean existsByIdViajeAndIdDestinoAndOrdenDestino(Long idViaje, Long idDestino, Long ordenDestino);
}
