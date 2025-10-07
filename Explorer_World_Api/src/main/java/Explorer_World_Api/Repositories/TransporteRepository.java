package Explorer_World_Api.Repositories;

import Explorer_World_Api.Entities.TransporteEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransporteRepository extends JpaRepository<TransporteEntity, Long> {
}
