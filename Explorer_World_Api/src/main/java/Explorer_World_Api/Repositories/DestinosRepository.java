package Explorer_World_Api.Repositories;

import Explorer_World_Api.Entities.DestinosEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DestinosRepository extends JpaRepository<DestinosEntity, Long> {
}
