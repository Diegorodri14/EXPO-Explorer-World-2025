package Explorer_World_Api.Repositories;

import Explorer_World_Api.Entities.ViajeEmpleadoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ViajeEmpleadoRepository extends JpaRepository<ViajeEmpleadoEntity, Long> {
    boolean existsByIdViajeAndIdEmpleado(Long idViaje, Long idEmpleado);
}
