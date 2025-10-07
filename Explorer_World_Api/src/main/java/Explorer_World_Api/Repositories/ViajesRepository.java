package Explorer_World_Api.Repositories;

import Explorer_World_Api.Entities.ViajesEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ViajesRepository extends JpaRepository<ViajesEntity, Long> {
}
