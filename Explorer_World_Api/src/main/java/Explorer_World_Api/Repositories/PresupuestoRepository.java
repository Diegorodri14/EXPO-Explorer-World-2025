package Explorer_World_Api.Repositories;

import Explorer_World_Api.Entities.PresupuestoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PresupuestoRepository extends JpaRepository<PresupuestoEntity, Long> {
}
