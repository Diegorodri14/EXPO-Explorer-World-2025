package Explorer_World_Api.Repositories;

import Explorer_World_Api.Entities.RangoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RangoRepository extends JpaRepository<RangoEntity, Long> {
}
