package page.clab.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import page.clab.api.type.entity.Application;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ApplicationRepository extends JpaRepository<Application, String> {

    Optional<Application> findByName(String name);

    @Query("SELECT a FROM Application a WHERE a.createdAt >= :startDate AND a.createdAt <= :endDate")
    List<Application> findApplicationsBetweenDates(LocalDateTime startDate, LocalDateTime endDate);

}
