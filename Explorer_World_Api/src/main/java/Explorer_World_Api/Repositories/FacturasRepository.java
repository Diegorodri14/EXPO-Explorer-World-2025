package Explorer_World_Api.Repositories;

import Explorer_World_Api.Entities.FacturasEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FacturasRepository extends JpaRepository<FacturasEntity, Long> {
}
