package Explorer_World_Api.Repositories;

import Explorer_World_Api.Entities.ServiciosEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ServiciosRepository extends JpaRepository<ServiciosEntity, Long> {
}
