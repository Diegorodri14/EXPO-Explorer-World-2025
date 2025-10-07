package Explorer_World_Api.Repositories;


import Explorer_World_Api.Entities.HorariosEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HorariosRepository extends JpaRepository<HorariosEntity, Long> {
}
