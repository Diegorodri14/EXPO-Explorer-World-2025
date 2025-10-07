package Explorer_World_Api.Repositories;

import Explorer_World_Api.Entities.BoletosEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BoletosRepository extends JpaRepository<BoletosEntity, Long> {
}
