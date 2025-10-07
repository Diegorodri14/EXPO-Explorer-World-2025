package Explorer_World_Api.Repositories;


import Explorer_World_Api.Entities.EventosEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventosRepository extends JpaRepository<EventosEntity, Long> {
}
