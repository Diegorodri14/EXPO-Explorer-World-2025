package Explorer_World_Api.Repositories;

import Explorer_World_Api.Entities.ReservacionEventoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public interface ReservacionEventoRepository extends JpaRepository<ReservacionEventoEntity, Long> {
    boolean existsByIdReservacionAndIdEventoAndFechaHoraEvento(Long idReservacion, Long idEvento, Date fechaHoraEvento);
}
