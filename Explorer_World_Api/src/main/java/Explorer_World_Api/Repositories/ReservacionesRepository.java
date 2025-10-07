package Explorer_World_Api.Repositories;

import Explorer_World_Api.Entities.ReservacionesEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReservacionesRepository extends JpaRepository<ReservacionesEntity, Long> {
}
