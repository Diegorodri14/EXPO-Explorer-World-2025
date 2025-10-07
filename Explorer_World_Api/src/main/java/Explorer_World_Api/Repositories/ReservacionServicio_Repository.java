package Explorer_World_Api.Repositories;

import Explorer_World_Api.Entities.ReservacionServicio_Entity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReservacionServicio_Repository extends JpaRepository<ReservacionServicio_Entity, Long> {
}
