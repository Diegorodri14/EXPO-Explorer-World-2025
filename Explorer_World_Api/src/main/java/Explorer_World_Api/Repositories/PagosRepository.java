package Explorer_World_Api.Repositories;

import Explorer_World_Api.Entities.PagosEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PagosRepository extends JpaRepository<PagosEntity, Long> {
}
